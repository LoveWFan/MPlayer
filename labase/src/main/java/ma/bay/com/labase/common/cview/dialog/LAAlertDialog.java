package ma.bay.com.labase.common.cview.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import androidx.appcompat.app.AlertDialog;

import ma.bay.com.labase.R;


/**
 * Created by wangle12138 on 8/12/16.
 */
public class LAAlertDialog {
    public static AlertDialog.Builder build(Context context) {
        int theme = R.style.Dialog_Landscape;
        if (context instanceof Activity &&
                context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            theme = R.style.Dialog_Portrait;
        }

        return new AlertDialog.Builder(context, theme);
    }
}
