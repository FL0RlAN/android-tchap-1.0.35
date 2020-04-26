package im.vector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

public class RecentsExpandableListView extends ExpandableListView {
    private int mCurrentY = -1;
    public DragAndDropEventsListener mDragAndDropEventsListener = null;
    private int mTouchedChildPosition = -1;
    private int mTouchedGroupPosition = -1;

    public interface DragAndDropEventsListener {
        void onDrop();

        void onOverScrolled(boolean z);

        void onTouchMove(int i, int i2, int i3);
    }

    public RecentsExpandableListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onOverScrolled(int i, int i2, boolean z, boolean z2) {
        if (z2) {
            DragAndDropEventsListener dragAndDropEventsListener = this.mDragAndDropEventsListener;
            if (dragAndDropEventsListener != null) {
                dragAndDropEventsListener.onOverScrolled(getFirstVisiblePosition() == 0);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0034, code lost:
        if (r0 != 3) goto L_0x004c;
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        this.mCurrentY = y;
        long expandableListPosition = getExpandableListPosition(pointToPosition(x, y));
        int packedPositionGroup = ExpandableListView.getPackedPositionGroup(expandableListPosition);
        int packedPositionChild = ExpandableListView.getPackedPositionChild(expandableListPosition);
        this.mTouchedGroupPosition = Math.max(packedPositionGroup, 0);
        this.mTouchedChildPosition = Math.max(packedPositionChild, 0);
        if (action != 1) {
            if (action == 2) {
                DragAndDropEventsListener dragAndDropEventsListener = this.mDragAndDropEventsListener;
                if (dragAndDropEventsListener != null) {
                    dragAndDropEventsListener.onTouchMove(this.mCurrentY, this.mTouchedGroupPosition, this.mTouchedChildPosition);
                }
            }
            return super.onTouchEvent(motionEvent);
        }
        DragAndDropEventsListener dragAndDropEventsListener2 = this.mDragAndDropEventsListener;
        if (dragAndDropEventsListener2 != null) {
            dragAndDropEventsListener2.onDrop();
        }
        return super.onTouchEvent(motionEvent);
    }

    public int getTouchedY() {
        return this.mCurrentY;
    }

    public int getTouchedGroupPosition() {
        return this.mTouchedGroupPosition;
    }

    public int getTouchedChildPosition() {
        return this.mTouchedChildPosition;
    }
}
