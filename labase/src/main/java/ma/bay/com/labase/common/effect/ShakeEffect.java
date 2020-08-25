package ma.bay.com.labase.common.effect;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by wangl2138 on 2018/1/31.
 */

public class ShakeEffect {

    private View mTarget;
    private ObjectAnimator mObjectAnimator;

    public void render(View target) {
        if (target == null) {
            return;
        }

        if (mTarget != target) {
            mObjectAnimator = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, 10f, -10f, 10f, -10f, 10f, -10f, 0);
            mObjectAnimator.setDuration(500);
            mTarget = target;
        }
        mObjectAnimator.start();
    }
}
