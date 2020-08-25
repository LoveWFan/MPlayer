package ma.bay.com.labase.common.cview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import ma.bay.com.labase.R;

public class LAProgressDialog extends Dialog {

    private Context context;
    private TextView tvMessage;
    private float defaultDimAmount = -1;

    public LAProgressDialog(Context context) {
        super(context, R.style.ProgressDialog);
        this.context = context;
        this.setContentView(R.layout.layout_progress_dialog);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().gravity = Gravity.CENTER;
        }

        tvMessage = (TextView) findViewById(R.id.msg);
    }

    private void setContentMessage(String msg) {
        if (msg == null || msg.length() <= 0) {
            return;
        }
        LayoutParams lp = (LayoutParams) tvMessage.getLayoutParams();
        lp.leftMargin = (int) context.getResources().getDimension(R.dimen.margin5);
        tvMessage.setText(msg);
    }

    public void showProgressDialog() {
        showProgressDialog(null);
    }

    public void showProgressDialog(String text) {
        if (StringUtils.isNotBlank(text)) {
            setContentMessage(text);
        }

        if (!isShowing()) {
            setCanceledOnTouchOutside(false);
            setCancelable(true);
            show();
        }
    }

    public void dismissProgressDialog() {
        dismiss();
    }

    public void showProgressDialog(String text, boolean isShowShade) {
        Window window = getWindow();
        if (window == null) {
            return;
        }

        if (!isShowShade) {
            WindowManager.LayoutParams params = window.getAttributes();
            defaultDimAmount = params.dimAmount;
            window.setDimAmount(0f);
        } else {
            if (defaultDimAmount > 0) {
                window.setDimAmount(defaultDimAmount);
            }
        }
        showProgressDialog(text);
    }
}
