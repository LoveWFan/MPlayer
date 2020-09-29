package com.poney.gpuimage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poney.gpuimage.R;
import com.poney.gpuimage.R2;
import com.poney.gpuimage.adapter.FilterAdapter;
import com.poney.gpuimage.filter.base.FilterAdjuster;
import com.poney.gpuimage.filter.base.FilterTypeList;
import com.poney.gpuimage.filter.base.GPUImageFilter;
import com.poney.gpuimage.filter.base.GPUImageAdjustFilter;
import com.poney.gpuimage.filter.base.MagicFilterType;
import com.poney.gpuimage.view.GPUImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends Activity {

    private static final int REQUEST_PICK_IMAGE = 1;
    @BindView(R2.id.gpu_image)
    GPUImageView gpuImageView;
    @BindView(R2.id.btn_camera_beauty)
    ImageView btnCameraBeauty;
    @BindView(R2.id.btn_camera_shutter)
    ImageView btnCameraShutter;
    @BindView(R2.id.btn_camera_filter)
    ImageView btnCameraFilter;
    @BindView(R2.id.filter_listView)
    RecyclerView filterListView;
    @BindView(R2.id.seekBar)
    SeekBar seekBar;

    private FilterAdjuster filterAdjuster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        initFilterList();
        startPhotoPicker();
    }

    private void initFilterList() {
        filterListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FilterAdapter filterAdapter = new FilterAdapter(this, FilterTypeList.TYPES);
        filterAdapter.setOnFilterChangeListener(new FilterAdapter.onFilterChangeListener() {
            @Override
            public void onFilterChanged(MagicFilterType filterType) {
                switchFilterTo(createFilterBy(filterType));
            }
        });
        filterListView.setAdapter(filterAdapter);
    }

    private void switchFilterTo(GPUImageFilter filter) {
        if (filter instanceof GPUImageAdjustFilter) {
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (filterAdjuster.canAdjust()){
                        filterAdjuster.adjust(progress);
                    }
                    gpuImageView.requestRender();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        if (gpuImageView.getFilter() == null || gpuImageView.getFilter().getClass() != filter.getClass()){
            gpuImageView.setFilter(filter);
            filterAdjuster = new FilterAdjuster(filter);
            if (filterAdjuster.canAdjust()){
                filterAdjuster.adjust(seekBar.getProgress());
            }
        }
    }

    private GPUImageFilter createFilterBy(MagicFilterType filterType) {
        switch (filterType) {
            case CONTRAST:

                break;
        }
        return null;
    }

    private void startPhotoPicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE: {
                if (resultCode == RESULT_OK) {
                    gpuImageView.setImage(data.getData());
                } else {
                    finish();
                }
            }
            break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


}