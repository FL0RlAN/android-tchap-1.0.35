package im.vector.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import im.vector.view.PillView.OnUpdateListener;
import java.lang.ref.WeakReference;

public class PillImageView extends VectorCircularImageView {
    private WeakReference<OnUpdateListener> mOnUpdateListener = null;

    public PillImageView(Context context) {
        super(context);
    }

    public PillImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PillImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void setCircularImageDrawable(RoundedBitmapDrawable roundedBitmapDrawable) {
        super.setCircularImageDrawable(roundedBitmapDrawable);
        WeakReference<OnUpdateListener> weakReference = this.mOnUpdateListener;
        if (weakReference != null && weakReference.get() != null) {
            ((OnUpdateListener) this.mOnUpdateListener.get()).onAvatarUpdate();
        }
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.mOnUpdateListener = new WeakReference<>(onUpdateListener);
    }
}
