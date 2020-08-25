package ma.bay.com.labase.common.cview.nv;

import android.annotation.SuppressLint;
import android.content.Context;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by wangle12138 on 2017/6/5.
 */

public class LABottomNavigationView extends BottomNavigationView {
    public LABottomNavigationView(Context context) {
        super(context);
        init();
    }

    public LABottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LABottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setShiftMode(false);
    }

    @SuppressLint("RestrictedApi")
    public void setShiftMode(boolean enable) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, enable);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setChecked(itemView.getItemData().isChecked());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
