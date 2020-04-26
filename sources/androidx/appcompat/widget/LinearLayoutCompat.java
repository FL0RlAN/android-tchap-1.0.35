package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    /* access modifiers changed from: 0000 */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int measureNullChild(int i) {
        return 0;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersVertical(Canvas canvas) {
        int i;
        int virtualChildCount = getVirtualChildCount();
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i2))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                i = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                i = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersHorizontal(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i5 = 0; i5 < virtualChildCount; i5++) {
            View virtualChildAt = getVirtualChildAt(i5);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i5))) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    i4 = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    i4 = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, i4);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    i3 = virtualChildAt2.getLeft() - layoutParams2.leftMargin;
                    i2 = this.mDividerWidth;
                } else {
                    i = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                    drawVerticalDivider(canvas, i);
                }
            } else if (isLayoutRtl) {
                i = getPaddingLeft();
                drawVerticalDivider(canvas, i);
            } else {
                i3 = getWidth() - getPaddingRight();
                i2 = this.mDividerWidth;
            }
            i = i3 - i2;
            drawVerticalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount > i) {
            View childAt = getChildAt(i);
            int baseline = childAt.getBaseline();
            if (baseline != -1) {
                int i2 = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int i3 = this.mGravity & 112;
                    if (i3 != 48) {
                        if (i3 == 16) {
                            i2 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                        } else if (i3 == 80) {
                            i2 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                        }
                    }
                }
                return i2 + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder sb = new StringBuilder();
            sb.append("base aligned child index out of range (0, ");
            sb.append(getChildCount());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* access modifiers changed from: 0000 */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* access modifiers changed from: 0000 */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        boolean z = false;
        if (i == 0) {
            if ((this.mShowDividers & 1) != 0) {
                z = true;
            }
            return z;
        } else if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z = true;
            }
            return z;
        } else {
            if ((this.mShowDividers & 2) != 0) {
                int i2 = i - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    } else if (getChildAt(i2).getVisibility() != 8) {
                        z = true;
                        break;
                    } else {
                        i2--;
                    }
                }
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x032e  */
    public void measureVertical(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        float f;
        int i7;
        int i8;
        boolean z;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        View view;
        int i18;
        boolean z2;
        int i19;
        int i20;
        int i21;
        int i22 = i;
        int i23 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int i24 = this.mBaselineAlignedChildIndex;
        boolean z3 = this.mUseLargestChild;
        float f2 = 0.0f;
        int i25 = 0;
        int i26 = 0;
        int i27 = 0;
        int i28 = 0;
        int i29 = 0;
        int i30 = 0;
        boolean z4 = false;
        boolean z5 = true;
        boolean z6 = false;
        while (true) {
            int i31 = 8;
            int i32 = i28;
            if (i30 < virtualChildCount) {
                View virtualChildAt = getVirtualChildAt(i30);
                if (virtualChildAt == null) {
                    this.mTotalLength += measureNullChild(i30);
                    i11 = virtualChildCount;
                    i28 = i32;
                } else {
                    int i33 = i25;
                    if (virtualChildAt.getVisibility() == 8) {
                        i30 += getChildrenSkipCount(virtualChildAt, i30);
                        i11 = virtualChildCount;
                        i28 = i32;
                        i25 = i33;
                    } else {
                        if (hasDividerBeforeChildAt(i30)) {
                            this.mTotalLength += this.mDividerHeight;
                        }
                        LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                        float f3 = f2 + layoutParams.weight;
                        if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                            int i34 = this.mTotalLength;
                            int i35 = i26;
                            this.mTotalLength = Math.max(i34, layoutParams.topMargin + i34 + layoutParams.bottomMargin);
                            i18 = i27;
                            view = virtualChildAt;
                            i13 = i29;
                            i21 = virtualChildCount;
                            i17 = i33;
                            i14 = i35;
                            z4 = true;
                            i16 = i30;
                            int i36 = i32;
                            i12 = mode2;
                            i15 = i36;
                        } else {
                            int i37 = i26;
                            if (layoutParams.height != 0 || layoutParams.weight <= 0.0f) {
                                i20 = Integer.MIN_VALUE;
                            } else {
                                layoutParams.height = -2;
                                i20 = 0;
                            }
                            i17 = i33;
                            int i38 = i20;
                            i14 = i37;
                            int i39 = i27;
                            View view2 = virtualChildAt;
                            i21 = virtualChildCount;
                            int i40 = i32;
                            i12 = mode2;
                            i15 = i40;
                            i13 = i29;
                            i16 = i30;
                            measureChildBeforeLayout(virtualChildAt, i30, i, 0, i2, f3 == 0.0f ? this.mTotalLength : 0);
                            int i41 = i38;
                            if (i41 != Integer.MIN_VALUE) {
                                layoutParams.height = i41;
                            }
                            int measuredHeight = view2.getMeasuredHeight();
                            int i42 = this.mTotalLength;
                            view = view2;
                            this.mTotalLength = Math.max(i42, i42 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
                            i18 = z3 ? Math.max(measuredHeight, i39) : i39;
                        }
                        if (i24 >= 0 && i24 == i16 + 1) {
                            this.mBaselineChildTop = this.mTotalLength;
                        }
                        if (i16 >= i24 || layoutParams.weight <= 0.0f) {
                            if (mode == 1073741824 || layoutParams.width != -1) {
                                z2 = false;
                            } else {
                                z2 = true;
                                z6 = true;
                            }
                            int i43 = layoutParams.leftMargin + layoutParams.rightMargin;
                            int measuredWidth = view.getMeasuredWidth() + i43;
                            int max = Math.max(i14, measuredWidth);
                            int combineMeasuredStates = View.combineMeasuredStates(i17, view.getMeasuredState());
                            boolean z7 = z5 && layoutParams.width == -1;
                            if (layoutParams.weight > 0.0f) {
                                if (!z2) {
                                    i43 = measuredWidth;
                                }
                                i15 = Math.max(i15, i43);
                                i19 = i13;
                            } else {
                                if (!z2) {
                                    i43 = measuredWidth;
                                }
                                i19 = Math.max(i13, i43);
                            }
                            i27 = i18;
                            z5 = z7;
                            i28 = i15;
                            f2 = f3;
                            int i44 = max;
                            i29 = i19;
                            i25 = combineMeasuredStates;
                            i30 = getChildrenSkipCount(view, i16) + i16;
                            i26 = i44;
                            i30++;
                            int i45 = i;
                            int i46 = i2;
                            mode2 = i12;
                            virtualChildCount = i11;
                        } else {
                            throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                        }
                    }
                }
                i12 = mode2;
                i30++;
                int i452 = i;
                int i462 = i2;
                mode2 = i12;
                virtualChildCount = i11;
            } else {
                int i47 = i25;
                int i48 = i27;
                int i49 = i29;
                int i50 = virtualChildCount;
                int i51 = i26;
                int i52 = i32;
                int i53 = mode2;
                int i54 = i52;
                if (this.mTotalLength > 0) {
                    i3 = i50;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i50;
                }
                int i55 = i53;
                if (z3 && (i55 == Integer.MIN_VALUE || i55 == 0)) {
                    this.mTotalLength = 0;
                    int i56 = 0;
                    while (i56 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i56);
                        if (virtualChildAt2 == null) {
                            this.mTotalLength += measureNullChild(i56);
                        } else if (virtualChildAt2.getVisibility() == i31) {
                            i56 += getChildrenSkipCount(virtualChildAt2, i56);
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            int i57 = this.mTotalLength;
                            this.mTotalLength = Math.max(i57, i57 + i48 + layoutParams2.topMargin + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt2));
                        }
                        i56++;
                        i31 = 8;
                    }
                }
                this.mTotalLength += getPaddingTop() + getPaddingBottom();
                int i58 = i2;
                int i59 = i48;
                int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), i58, 0);
                int i60 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (z4 || (i60 != 0 && f2 > 0.0f)) {
                    float f4 = this.mWeightSum;
                    if (f4 > 0.0f) {
                        f2 = f4;
                    }
                    this.mTotalLength = 0;
                    float f5 = f2;
                    int i61 = 0;
                    int i62 = i47;
                    int i63 = i49;
                    i5 = i62;
                    while (i61 < i3) {
                        View virtualChildAt3 = getVirtualChildAt(i61);
                        if (virtualChildAt3.getVisibility() == 8) {
                            f = f5;
                            int i64 = i;
                        } else {
                            LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                            float f6 = layoutParams3.weight;
                            if (f6 > 0.0f) {
                                int i65 = (int) ((((float) i60) * f6) / f5);
                                i7 = i60 - i65;
                                f = f5 - f6;
                                int childMeasureSpec = getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + layoutParams3.leftMargin + layoutParams3.rightMargin, layoutParams3.width);
                                if (layoutParams3.height == 0) {
                                    i10 = 1073741824;
                                    if (i55 == 1073741824) {
                                        if (i65 <= 0) {
                                            i65 = 0;
                                        }
                                        virtualChildAt3.measure(childMeasureSpec, MeasureSpec.makeMeasureSpec(i65, 1073741824));
                                        i5 = View.combineMeasuredStates(i5, virtualChildAt3.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                                    }
                                } else {
                                    i10 = 1073741824;
                                }
                                int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i65;
                                if (measuredHeight2 < 0) {
                                    measuredHeight2 = 0;
                                }
                                virtualChildAt3.measure(childMeasureSpec, MeasureSpec.makeMeasureSpec(measuredHeight2, i10));
                                i5 = View.combineMeasuredStates(i5, virtualChildAt3.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                            } else {
                                float f7 = f5;
                                int i66 = i;
                                i7 = i60;
                                f = f7;
                            }
                            int i67 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                            int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i67;
                            i51 = Math.max(i51, measuredWidth2);
                            if (mode != 1073741824) {
                                i8 = i5;
                                i9 = -1;
                                if (layoutParams3.width == -1) {
                                    z = true;
                                    if (!z) {
                                        i67 = measuredWidth2;
                                    }
                                    i63 = Math.max(i63, i67);
                                    boolean z8 = !z5 && layoutParams3.width == i9;
                                    int i68 = this.mTotalLength;
                                    this.mTotalLength = Math.max(i68, virtualChildAt3.getMeasuredHeight() + i68 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                                    z5 = z8;
                                    i60 = i7;
                                    i5 = i8;
                                }
                            } else {
                                i8 = i5;
                                i9 = -1;
                            }
                            z = false;
                            if (!z) {
                            }
                            i63 = Math.max(i63, i67);
                            if (!z5) {
                            }
                            int i682 = this.mTotalLength;
                            this.mTotalLength = Math.max(i682, virtualChildAt3.getMeasuredHeight() + i682 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                            z5 = z8;
                            i60 = i7;
                            i5 = i8;
                        }
                        i61++;
                        f5 = f;
                    }
                    i4 = i;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    i6 = i63;
                } else {
                    i6 = Math.max(i49, i54);
                    if (z3 && i55 != 1073741824) {
                        for (int i69 = 0; i69 < i3; i69++) {
                            View virtualChildAt4 = getVirtualChildAt(i69);
                            if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                                virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(i59, 1073741824));
                            }
                        }
                    }
                    i4 = i;
                    i5 = i47;
                }
                if (z5 || mode == 1073741824) {
                    i6 = i51;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i4, i5), resolveSizeAndState);
                if (z6) {
                    forceUniformWidth(i3, i58);
                    return;
                }
                return;
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x044b  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x018b  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01cd  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01d8  */
    public void measureHorizontal(int i, int i2) {
        int[] iArr;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        float f;
        int i11;
        boolean z;
        int i12;
        int i13;
        boolean z2;
        boolean z3;
        int i14;
        View view;
        int i15;
        boolean z4;
        int i16;
        int i17 = i;
        int i18 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] iArr2 = this.mMaxAscent;
        int[] iArr3 = this.mMaxDescent;
        iArr2[3] = -1;
        iArr2[2] = -1;
        iArr2[1] = -1;
        iArr2[0] = -1;
        iArr3[3] = -1;
        iArr3[2] = -1;
        iArr3[1] = -1;
        iArr3[0] = -1;
        boolean z5 = this.mBaselineAligned;
        boolean z6 = this.mUseLargestChild;
        int i19 = 1073741824;
        boolean z7 = mode == 1073741824;
        float f2 = 0.0f;
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        boolean z8 = false;
        int i25 = 0;
        boolean z9 = true;
        boolean z10 = false;
        while (true) {
            iArr = iArr3;
            if (i20 >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(i20);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(i20);
            } else if (virtualChildAt.getVisibility() == 8) {
                i20 += getChildrenSkipCount(virtualChildAt, i20);
            } else {
                if (hasDividerBeforeChildAt(i20)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f3 = f2 + layoutParams.weight;
                if (mode == i19 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (z7) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    } else {
                        int i26 = this.mTotalLength;
                        this.mTotalLength = Math.max(i26, layoutParams.leftMargin + i26 + layoutParams.rightMargin);
                    }
                    if (z5) {
                        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(makeMeasureSpec, makeMeasureSpec);
                        i14 = i20;
                        z3 = z6;
                        z2 = z5;
                        view = virtualChildAt;
                    } else {
                        i14 = i20;
                        z3 = z6;
                        z2 = z5;
                        view = virtualChildAt;
                        i15 = 1073741824;
                        z8 = true;
                        if (mode2 == i15 && layoutParams.height == -1) {
                            z4 = true;
                            z10 = true;
                        } else {
                            z4 = false;
                        }
                        int i27 = layoutParams.topMargin + layoutParams.bottomMargin;
                        int measuredHeight = view.getMeasuredHeight() + i27;
                        int combineMeasuredStates = View.combineMeasuredStates(i25, view.getMeasuredState());
                        if (z2) {
                            int baseline = view.getBaseline();
                            if (baseline != -1) {
                                int i28 = ((((layoutParams.gravity < 0 ? this.mGravity : layoutParams.gravity) & 112) >> 4) & -2) >> 1;
                                iArr2[i28] = Math.max(iArr2[i28], baseline);
                                iArr[i28] = Math.max(iArr[i28], measuredHeight - baseline);
                            }
                        }
                        int max = Math.max(i22, measuredHeight);
                        boolean z11 = !z9 && layoutParams.height == -1;
                        if (layoutParams.weight <= 0.0f) {
                            if (!z4) {
                                i27 = measuredHeight;
                            }
                            i24 = Math.max(i24, i27);
                        } else {
                            int i29 = i24;
                            if (z4) {
                                measuredHeight = i27;
                            }
                            i23 = Math.max(i23, measuredHeight);
                            i24 = i29;
                        }
                        int i30 = i14;
                        i22 = max;
                        i25 = combineMeasuredStates;
                        z9 = z11;
                        i20 = getChildrenSkipCount(view, i30) + i30;
                        f2 = f3;
                        i20++;
                        int i31 = i2;
                        iArr3 = iArr;
                        z6 = z3;
                        z5 = z2;
                        i19 = 1073741824;
                    }
                } else {
                    if (layoutParams.width != 0 || layoutParams.weight <= 0.0f) {
                        i16 = Integer.MIN_VALUE;
                    } else {
                        layoutParams.width = -2;
                        i16 = 0;
                    }
                    i14 = i20;
                    int i32 = i16;
                    z3 = z6;
                    z2 = z5;
                    View view2 = virtualChildAt;
                    measureChildBeforeLayout(virtualChildAt, i14, i, f3 == 0.0f ? this.mTotalLength : 0, i2, 0);
                    int i33 = i32;
                    if (i33 != Integer.MIN_VALUE) {
                        layoutParams.width = i33;
                    }
                    int measuredWidth = view2.getMeasuredWidth();
                    if (z7) {
                        view = view2;
                        this.mTotalLength += layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin + getNextLocationOffset(view);
                    } else {
                        view = view2;
                        int i34 = this.mTotalLength;
                        this.mTotalLength = Math.max(i34, i34 + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
                    }
                    if (z3) {
                        i21 = Math.max(measuredWidth, i21);
                    }
                }
                i15 = 1073741824;
                if (mode2 == i15) {
                }
                z4 = false;
                int i272 = layoutParams.topMargin + layoutParams.bottomMargin;
                int measuredHeight2 = view.getMeasuredHeight() + i272;
                int combineMeasuredStates2 = View.combineMeasuredStates(i25, view.getMeasuredState());
                if (z2) {
                }
                int max2 = Math.max(i22, measuredHeight2);
                if (!z9) {
                }
                if (layoutParams.weight <= 0.0f) {
                }
                int i302 = i14;
                i22 = max2;
                i25 = combineMeasuredStates2;
                z9 = z11;
                i20 = getChildrenSkipCount(view, i302) + i302;
                f2 = f3;
                i20++;
                int i312 = i2;
                iArr3 = iArr;
                z6 = z3;
                z5 = z2;
                i19 = 1073741824;
            }
            z3 = z6;
            z2 = z5;
            i20++;
            int i3122 = i2;
            iArr3 = iArr;
            z6 = z3;
            z5 = z2;
            i19 = 1073741824;
        }
        boolean z12 = z6;
        boolean z13 = z5;
        int i35 = i22;
        int i36 = i23;
        int i37 = i24;
        int i38 = i25;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            i3 = i38;
        } else {
            i3 = i38;
            i35 = Math.max(i35, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z12 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int i39 = 0;
            while (i39 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(i39);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(i39);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    i39 += getChildrenSkipCount(virtualChildAt2, i39);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    if (z7) {
                        this.mTotalLength += layoutParams2.leftMargin + i21 + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        int i40 = this.mTotalLength;
                        i13 = i35;
                        this.mTotalLength = Math.max(i40, i40 + i21 + layoutParams2.leftMargin + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2));
                        i39++;
                        i35 = i13;
                    }
                }
                i13 = i35;
                i39++;
                i35 = i13;
            }
        }
        int i41 = i35;
        this.mTotalLength += getPaddingLeft() + getPaddingRight();
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumWidth()), i17, 0);
        int i42 = (16777215 & resolveSizeAndState) - this.mTotalLength;
        if (z8 || (i42 != 0 && f2 > 0.0f)) {
            float f4 = this.mWeightSum;
            if (f4 > 0.0f) {
                f2 = f4;
            }
            iArr2[3] = -1;
            iArr2[2] = -1;
            iArr2[1] = -1;
            iArr2[0] = -1;
            iArr[3] = -1;
            iArr[2] = -1;
            iArr[1] = -1;
            iArr[0] = -1;
            this.mTotalLength = 0;
            int i43 = i36;
            int i44 = i3;
            int i45 = -1;
            float f5 = f2;
            int i46 = 0;
            while (i46 < virtualChildCount) {
                View virtualChildAt3 = getVirtualChildAt(i46);
                if (virtualChildAt3 == null || virtualChildAt3.getVisibility() == 8) {
                    i10 = i42;
                    i9 = virtualChildCount;
                    int i47 = i2;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f6 = layoutParams3.weight;
                    if (f6 > 0.0f) {
                        int i48 = (int) ((((float) i42) * f6) / f5);
                        float f7 = f5 - f6;
                        int i49 = i42 - i48;
                        i9 = virtualChildCount;
                        int childMeasureSpec = getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom() + layoutParams3.topMargin + layoutParams3.bottomMargin, layoutParams3.height);
                        if (layoutParams3.width == 0) {
                            i12 = 1073741824;
                            if (mode == 1073741824) {
                                if (i48 <= 0) {
                                    i48 = 0;
                                }
                                virtualChildAt3.measure(MeasureSpec.makeMeasureSpec(i48, 1073741824), childMeasureSpec);
                                i44 = View.combineMeasuredStates(i44, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                                f5 = f7;
                                i10 = i49;
                            }
                        } else {
                            i12 = 1073741824;
                        }
                        int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i48;
                        if (measuredWidth2 < 0) {
                            measuredWidth2 = 0;
                        }
                        virtualChildAt3.measure(MeasureSpec.makeMeasureSpec(measuredWidth2, i12), childMeasureSpec);
                        i44 = View.combineMeasuredStates(i44, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                        f5 = f7;
                        i10 = i49;
                    } else {
                        i10 = i42;
                        i9 = virtualChildCount;
                        int i50 = i2;
                    }
                    if (z7) {
                        this.mTotalLength += virtualChildAt3.getMeasuredWidth() + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3);
                        f = f5;
                    } else {
                        int i51 = this.mTotalLength;
                        f = f5;
                        this.mTotalLength = Math.max(i51, virtualChildAt3.getMeasuredWidth() + i51 + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3));
                    }
                    boolean z14 = mode2 != 1073741824 && layoutParams3.height == -1;
                    int i52 = layoutParams3.topMargin + layoutParams3.bottomMargin;
                    int measuredHeight3 = virtualChildAt3.getMeasuredHeight() + i52;
                    i45 = Math.max(i45, measuredHeight3);
                    if (!z14) {
                        i52 = measuredHeight3;
                    }
                    int max3 = Math.max(i43, i52);
                    if (z9) {
                        i11 = -1;
                        if (layoutParams3.height == -1) {
                            z = true;
                            if (z13) {
                                int baseline2 = virtualChildAt3.getBaseline();
                                if (baseline2 != i11) {
                                    int i53 = ((((layoutParams3.gravity < 0 ? this.mGravity : layoutParams3.gravity) & 112) >> 4) & -2) >> 1;
                                    iArr2[i53] = Math.max(iArr2[i53], baseline2);
                                    iArr[i53] = Math.max(iArr[i53], measuredHeight3 - baseline2);
                                    i43 = max3;
                                    z9 = z;
                                    f5 = f;
                                }
                            }
                            i43 = max3;
                            z9 = z;
                            f5 = f;
                        }
                    } else {
                        i11 = -1;
                    }
                    z = false;
                    if (z13) {
                    }
                    i43 = max3;
                    z9 = z;
                    f5 = f;
                }
                i46++;
                int i54 = i;
                i42 = i10;
                virtualChildCount = i9;
            }
            i5 = i2;
            i4 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
                i8 = i45;
            } else {
                i8 = Math.max(i45, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            }
            i6 = i8;
            i3 = i44;
            i7 = i43;
        } else {
            i7 = Math.max(i36, i37);
            if (z12 && mode != 1073741824) {
                for (int i55 = 0; i55 < virtualChildCount; i55++) {
                    View virtualChildAt4 = getVirtualChildAt(i55);
                    if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                        virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(i21, 1073741824), MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredHeight(), 1073741824));
                    }
                }
            }
            i5 = i2;
            i4 = virtualChildCount;
            i6 = i41;
        }
        if (z9 || mode2 == 1073741824) {
            i7 = i6;
        }
        setMeasuredDimension(resolveSizeAndState | (i3 & ViewCompat.MEASURED_STATE_MASK), View.resolveSizeAndState(Math.max(i7 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i5, i3 << 16));
        if (z10) {
            forceUniformHeight(i4, i);
        }
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: 0000 */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int paddingLeft = getPaddingLeft();
        int i9 = i3 - i;
        int paddingRight = i9 - getPaddingRight();
        int paddingRight2 = (i9 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i10 = this.mGravity;
        int i11 = i10 & 112;
        int i12 = i10 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i11 == 16) {
            i5 = getPaddingTop() + (((i4 - i2) - this.mTotalLength) / 2);
        } else if (i11 != 80) {
            i5 = getPaddingTop();
        } else {
            i5 = ((getPaddingTop() + i4) - i2) - this.mTotalLength;
        }
        int i13 = 0;
        while (i13 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i13);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i13);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i14 = layoutParams.gravity;
                if (i14 < 0) {
                    i14 = i12;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i14, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i7 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                    i6 = layoutParams.rightMargin;
                    i8 = i7 - i6;
                } else if (absoluteGravity != 5) {
                    i8 = layoutParams.leftMargin + paddingLeft;
                } else {
                    i7 = paddingRight - measuredWidth;
                    i6 = layoutParams.rightMargin;
                    i8 = i7 - i6;
                }
                int i15 = i8;
                if (hasDividerBeforeChildAt(i13)) {
                    i5 += this.mDividerHeight;
                }
                int i16 = i5 + layoutParams.topMargin;
                LayoutParams layoutParams2 = layoutParams;
                setChildFrame(virtualChildAt, i15, i16 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i13 += getChildrenSkipCount(virtualChildAt, i13);
                i5 = i16 + measuredHeight + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt);
            }
            i13++;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00f8  */
    public void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i15 = i4 - i2;
        int paddingBottom = i15 - getPaddingBottom();
        int paddingBottom2 = (i15 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i16 = this.mGravity;
        int i17 = 8388615 & i16;
        int i18 = i16 & 112;
        boolean z = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i17, ViewCompat.getLayoutDirection(this));
        if (absoluteGravity == 1) {
            i5 = getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        } else if (absoluteGravity != 5) {
            i5 = getPaddingLeft();
        } else {
            i5 = ((getPaddingLeft() + i3) - i) - this.mTotalLength;
        }
        if (isLayoutRtl) {
            i7 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i7 = 0;
            i6 = 1;
        }
        int i19 = 0;
        while (i19 < virtualChildCount) {
            int i20 = i7 + (i6 * i19);
            View virtualChildAt = getVirtualChildAt(i20);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i20);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i21 = i19;
                if (z) {
                    i10 = virtualChildCount;
                    if (layoutParams.height != -1) {
                        i11 = virtualChildAt.getBaseline();
                        i12 = layoutParams.gravity;
                        if (i12 < 0) {
                            i12 = i18;
                        }
                        i13 = i12 & 112;
                        i9 = i18;
                        if (i13 != 16) {
                            i14 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + layoutParams.topMargin) - layoutParams.bottomMargin;
                        } else if (i13 == 48) {
                            int i22 = layoutParams.topMargin + paddingTop;
                            if (i11 != -1) {
                                i22 += iArr[1] - i11;
                            }
                            i14 = i22;
                        } else if (i13 != 80) {
                            i14 = paddingTop;
                        } else {
                            int i23 = (paddingBottom - measuredHeight) - layoutParams.bottomMargin;
                            if (i11 != -1) {
                                i23 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i11);
                            }
                            i14 = i23;
                        }
                        if (hasDividerBeforeChildAt(i20)) {
                            i5 += this.mDividerWidth;
                        }
                        int i24 = layoutParams.leftMargin + i5;
                        View view = virtualChildAt;
                        View view2 = view;
                        int i25 = i20;
                        int locationOffset = i24 + getLocationOffset(virtualChildAt);
                        i8 = paddingTop;
                        LayoutParams layoutParams2 = layoutParams;
                        setChildFrame(view, locationOffset, i14, measuredWidth, measuredHeight);
                        View view3 = view2;
                        i19 = i21 + getChildrenSkipCount(view3, i25);
                        i5 = i24 + measuredWidth + layoutParams2.rightMargin + getNextLocationOffset(view3);
                        i19++;
                        virtualChildCount = i10;
                        i18 = i9;
                        paddingTop = i8;
                    }
                } else {
                    i10 = virtualChildCount;
                }
                i11 = -1;
                i12 = layoutParams.gravity;
                if (i12 < 0) {
                }
                i13 = i12 & 112;
                i9 = i18;
                if (i13 != 16) {
                }
                if (hasDividerBeforeChildAt(i20)) {
                }
                int i242 = layoutParams.leftMargin + i5;
                View view4 = virtualChildAt;
                View view22 = view4;
                int i252 = i20;
                int locationOffset2 = i242 + getLocationOffset(virtualChildAt);
                i8 = paddingTop;
                LayoutParams layoutParams22 = layoutParams;
                setChildFrame(view4, locationOffset2, i14, measuredWidth, measuredHeight);
                View view32 = view22;
                i19 = i21 + getChildrenSkipCount(view32, i252);
                i5 = i242 + measuredWidth + layoutParams22.rightMargin + getNextLocationOffset(view32);
                i19++;
                virtualChildCount = i10;
                i18 = i9;
                paddingTop = i8;
            } else {
                int i26 = i19;
            }
            i8 = paddingTop;
            i10 = virtualChildCount;
            i9 = i18;
            i19++;
            virtualChildCount = i10;
            i18 = i9;
            paddingTop = i8;
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | (-8388616 & i3);
            requestLayout();
        }
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & -113);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
    }
}
