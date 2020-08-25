package ma.bay.com.labase.common.cview.indicator;

/**
 * Created by wangl2138 on 2017/9/1.
 */

public interface IIndicator {
    void showIndicator();

    void hideIndicator();

    void showFailureIndicator();

    void setOnHandleFailureListener(FailureListener listener);
}
