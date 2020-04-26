package com.getbase.floatingactionbutton;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import java.util.ArrayList;

public class TouchDelegateGroup extends TouchDelegate {
    private static final Rect USELESS_HACKY_RECT = new Rect();
    private TouchDelegate mCurrentTouchDelegate;
    private boolean mEnabled;
    private final ArrayList<TouchDelegate> mTouchDelegates = new ArrayList<>();

    public TouchDelegateGroup(View view) {
        super(USELESS_HACKY_RECT, view);
    }

    public void addTouchDelegate(TouchDelegate touchDelegate) {
        this.mTouchDelegates.add(touchDelegate);
    }

    public void removeTouchDelegate(TouchDelegate touchDelegate) {
        this.mTouchDelegates.remove(touchDelegate);
        if (this.mCurrentTouchDelegate == touchDelegate) {
            this.mCurrentTouchDelegate = null;
        }
    }

    public void clearTouchDelegates() {
        this.mTouchDelegates.clear();
        this.mCurrentTouchDelegate = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
        if (r0 != 3) goto L_0x003d;
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (!this.mEnabled) {
            return false;
        }
        int action = motionEvent.getAction();
        TouchDelegate touchDelegate = null;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    touchDelegate = this.mCurrentTouchDelegate;
                }
            }
            TouchDelegate touchDelegate2 = this.mCurrentTouchDelegate;
            this.mCurrentTouchDelegate = null;
            touchDelegate = touchDelegate2;
        } else {
            for (int i = 0; i < this.mTouchDelegates.size(); i++) {
                TouchDelegate touchDelegate3 = (TouchDelegate) this.mTouchDelegates.get(i);
                if (touchDelegate3.onTouchEvent(motionEvent)) {
                    this.mCurrentTouchDelegate = touchDelegate3;
                    return true;
                }
            }
        }
        if (touchDelegate != null && touchDelegate.onTouchEvent(motionEvent)) {
            z = true;
        }
        return z;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }
}
