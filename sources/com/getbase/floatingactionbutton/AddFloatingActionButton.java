package com.getbase.floatingactionbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;

public class AddFloatingActionButton extends FloatingActionButton {
    int mPlusColor;

    public AddFloatingActionButton(Context context) {
        this(context, null);
    }

    public AddFloatingActionButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AddFloatingActionButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: 0000 */
    public void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AddFloatingActionButton, 0, 0);
        this.mPlusColor = obtainStyledAttributes.getColor(R.styleable.AddFloatingActionButton_fab_plusIconColor, getColor(17170443));
        obtainStyledAttributes.recycle();
        super.init(context, attributeSet);
    }

    public int getPlusColor() {
        return this.mPlusColor;
    }

    public void setPlusColorResId(int i) {
        setPlusColor(getColor(i));
    }

    public void setPlusColor(int i) {
        if (this.mPlusColor != i) {
            this.mPlusColor = i;
            updateBackground();
        }
    }

    public void setIcon(int i) {
        throw new UnsupportedOperationException("Use FloatingActionButton if you want to use custom icon");
    }

    /* access modifiers changed from: 0000 */
    public Drawable getIconDrawable() {
        final float dimension = getDimension(R.dimen.fab_icon_size);
        final float f = dimension / 2.0f;
        final float dimension2 = getDimension(R.dimen.fab_plus_icon_stroke) / 2.0f;
        final float dimension3 = (dimension - getDimension(R.dimen.fab_plus_icon_size)) / 2.0f;
        AnonymousClass1 r1 = new Shape() {
            public void draw(Canvas canvas, Paint paint) {
                float f = dimension3;
                float f2 = f;
                float f3 = dimension2;
                canvas.drawRect(f, f2 - f3, dimension - f, f2 + f3, paint);
                float f4 = f;
                float f5 = dimension2;
                float f6 = f4 - f5;
                float f7 = dimension3;
                canvas.drawRect(f6, f7, f4 + f5, dimension - f7, paint);
            }
        };
        ShapeDrawable shapeDrawable = new ShapeDrawable(r1);
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(this.mPlusColor);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        return shapeDrawable;
    }
}
