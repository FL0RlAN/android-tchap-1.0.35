package im.vector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.State;
import fr.gouv.tchap.a.R;
import im.vector.ui.themes.ThemeUtils;

public class SimpleDividerItemDecoration extends DividerItemDecoration {
    private static final String NO_DIVIDER_TAG = "without-divider";
    private final Drawable mDivider;
    private final int mLeftMargin;
    private final int mOrientation;

    public SimpleDividerItemDecoration(Context context, int i, int i2) {
        super(context, i);
        this.mDivider = ContextCompat.getDrawable(context, ThemeUtils.INSTANCE.getResourceId(context, R.drawable.line_divider_dark));
        this.mLeftMargin = i2;
        this.mOrientation = i;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
        rect.set(0, 0, 0, 0);
    }

    public void onDraw(Canvas canvas, RecyclerView recyclerView, State state) {
        if (recyclerView.getLayoutManager() != null) {
            if (this.mOrientation == 0 || this.mLeftMargin <= 0) {
                super.onDraw(canvas, recyclerView, state);
            } else {
                canvas.save();
                int width = recyclerView.getWidth() - recyclerView.getPaddingRight();
                int childCount = recyclerView.getChildCount();
                int i = 0;
                while (i < childCount) {
                    View childAt = recyclerView.getChildAt(i);
                    i++;
                    View childAt2 = recyclerView.getChildAt(i);
                    String valueOf = String.valueOf(childAt.getTag());
                    String str = NO_DIVIDER_TAG;
                    if (!valueOf.contains(str) && childAt2 != null && !String.valueOf(childAt2.getTag()).contains(str)) {
                        int bottom = childAt.getBottom() + ((LayoutParams) childAt.getLayoutParams()).bottomMargin;
                        this.mDivider.setBounds(this.mLeftMargin, bottom, width, this.mDivider.getIntrinsicHeight() + bottom);
                        this.mDivider.draw(canvas);
                    }
                }
                canvas.restore();
            }
        }
    }
}
