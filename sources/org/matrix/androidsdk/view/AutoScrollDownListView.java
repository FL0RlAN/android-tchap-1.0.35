package org.matrix.androidsdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import org.matrix.androidsdk.core.Log;

public class AutoScrollDownListView extends ListView {
    private static final String LOG_TAG = AutoScrollDownListView.class.getSimpleName();
    private boolean mLockSelectionOnResize = false;

    public AutoScrollDownListView(Context context) {
        super(context);
    }

    public AutoScrollDownListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AutoScrollDownListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (!this.mLockSelectionOnResize && i2 < i4) {
            postDelayed(new Runnable() {
                public void run() {
                    AutoScrollDownListView autoScrollDownListView = AutoScrollDownListView.this;
                    autoScrollDownListView.setSelection(autoScrollDownListView.getCount() - 1);
                }
            }, 100);
        }
    }

    public void lockSelectionOnResize() {
        this.mLockSelectionOnResize = true;
    }

    /* access modifiers changed from: protected */
    public void layoutChildren() {
        try {
            super.layoutChildren();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## layoutChildren() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void setSelectionFromTop(int i, int i2) {
        super.setSelectionFromTop(i, i2);
    }
}
