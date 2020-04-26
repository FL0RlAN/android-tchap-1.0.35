package im.vector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;
import org.matrix.androidsdk.core.Log;

public class RiotViewPager extends ViewPager {
    private static final String LOG_TAG = RiotViewPager.class.getSimpleName();

    public RiotViewPager(Context context) {
        super(context);
    }

    public RiotViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!(getAdapter() == null || getAdapter().getCount() == 0)) {
            try {
                return super.onInterceptTouchEvent(motionEvent);
            } catch (IllegalArgumentException e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Exception: ");
                sb.append(e.getLocalizedMessage());
                Log.e(str, sb.toString());
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!(getAdapter() == null || getAdapter().getCount() == 0)) {
            try {
                return super.onTouchEvent(motionEvent);
            } catch (IllegalArgumentException e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Exception: ");
                sb.append(e.getLocalizedMessage());
                Log.e(str, sb.toString());
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int i, int i2) {
        try {
            return super.getChildDrawingOrder(i, i2);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getChildDrawingOrder() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## dispatchDraw() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }
}
