package com.poney.mplayer.biz.ui.home.camera;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.poney.gpuimage.activity.GalleryActivity;
import com.poney.mplayer.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;


public class CameraFragment extends Fragment {

    @BindView(R.id.button_gallery)
    Button buttonGallery;
    @BindView(R.id.button_camera)
    Button buttonCamera;
    private CameraViewModel cameraViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cameraViewModel =
                ViewModelProviders.of(this).get(CameraViewModel.class);
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick({R.id.button_gallery, R.id.button_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_gallery: {
                initGallery();
            }

            break;
            case R.id.button_camera: {
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                Disposable disposable = rxPermissions.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission) {
                                initCamera();
                            } else {
                                Toast.makeText(getActivity(), "请授予相关权限", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            break;
        }
    }

    private void initCamera() {

    }

    private void initGallery() {
        startActivity(new Intent(getActivity(), GalleryActivity.class));
    }
}