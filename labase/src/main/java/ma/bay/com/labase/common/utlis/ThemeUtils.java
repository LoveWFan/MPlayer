package ma.bay.com.labase.common.utlis;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by wangl2138 on 2018/3/16.
 */

public class ThemeUtils {

	/**
	 * 隐藏虚拟按键，并且全屏
	 *
	 * @param activity
	 */
	public static void fullscreen(@NonNull Activity activity) {
		Window window = activity.getWindow();
		if (window == null) {
			return;
		}

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			// lower api
			View v = window.getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//for new api versions. api >= KITKAT
			final View decorView = window.getDecorView();
			final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
				@Override
				public void onSystemUiVisibilityChange(int visibility) {
					if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
						decorView.setSystemUiVisibility(uiOptions);
					}
				}
			});
		}
	}

	/**
	 * 沉浸式
	 *
	 * @param activity
	 */
	public static void translucent(Activity activity) {
		Window window = activity.getWindow();
		if (window == null) {
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//for new api versions. api >= LOLLIPOP
			if (!RomUtils.isEMUI3_1()) {
				// fix emui 3.1's bug
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}

			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			fitStatusBarColor(activity);
		}
	}

	/**
	 * 修改status bar字体为黑色
	 *
	 * @param activity
	 */
	public static void fitStatusBarColor(@NonNull Activity activity) {
		Window window = activity.getWindow();
		if (window == null) {
			return;
		}

		// 6.0 设置状态栏字体颜色
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			View decor = window.getDecorView();
			decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			return;
		}

		if (RomUtils.isMiui()) {
			fitMiuiStatusBarColor(activity);
		} else if (RomUtils.isFlyme()) {
			fitFlymeStatusBarColor(activity);
		}
	}

	private static void fitMiuiStatusBarColor(Activity activity) {
		Window window = activity.getWindow();
		if (window == null) {
			return;
		}

		Class<?> clazz = window.getClass();
		try {
			Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			int darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
			extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void fitFlymeStatusBarColor(Activity activity) {
		try {
			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
			Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
			Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
			darkFlag.setAccessible(true);
			meizuFlags.setAccessible(true);
			int bit = darkFlag.getInt(null);
			int value = meizuFlags.getInt(lp);
			value |= bit;
			meizuFlags.setInt(lp, value);
			activity.getWindow().setAttributes(lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
