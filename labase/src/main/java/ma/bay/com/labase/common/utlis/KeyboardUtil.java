package ma.bay.com.labase.common.utlis;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
	public static void showKeyboard(@NonNull final View view) {
		view.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager != null) {
			inputManager.showSoftInput(view, 0);
		}
	}

	public static void hideKeyboard(@NonNull final View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}