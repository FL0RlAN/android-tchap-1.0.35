package fr.gouv.tchap.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class HexagonMaskView extends AppCompatImageView {
    private Paint borderPaint;
    private int borderRatio;
    private float height;
    private Path hexagonPath;
    private float width;

    public HexagonMaskView(Context context) {
        super(context);
        init();
    }

    public HexagonMaskView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public HexagonMaskView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void setBorderSettings(int i, int i2) {
        this.borderPaint.setColor(i);
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 > 100) {
            i2 = 100;
        }
        if (this.borderRatio != i2) {
            this.borderRatio = i2;
            calculatePath();
            return;
        }
        invalidate();
    }

    private void init() {
        this.hexagonPath = new Path();
        this.borderPaint = new Paint();
        this.borderPaint.setAntiAlias(true);
        this.borderPaint.setStyle(Style.STROKE);
        this.borderPaint.setColor(-3355444);
        this.borderRatio = 3;
    }

    private void calculatePath() {
        float f = this.height / 2.0f;
        float f2 = (((float) this.borderRatio) * f) / 100.0f;
        this.borderPaint.setStrokeWidth(f2);
        float f3 = f - (f2 / 2.0f);
        double sqrt = Math.sqrt(3.0d);
        double d = (double) f3;
        Double.isNaN(d);
        float f4 = (float) ((sqrt * d) / 2.0d);
        float f5 = this.width / 2.0f;
        float f6 = this.height / 2.0f;
        this.hexagonPath.reset();
        float f7 = f6 + f3;
        this.hexagonPath.moveTo(f5, f7);
        float f8 = f5 - f4;
        float f9 = f3 / 2.0f;
        float f10 = f6 + f9;
        this.hexagonPath.lineTo(f8, f10);
        float f11 = f6 - f9;
        this.hexagonPath.lineTo(f8, f11);
        this.hexagonPath.lineTo(f5, f6 - f3);
        float f12 = f4 + f5;
        this.hexagonPath.lineTo(f12, f11);
        this.hexagonPath.lineTo(f12, f10);
        this.hexagonPath.lineTo(f5, f7);
        this.hexagonPath.lineTo(f8, f10);
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(this.hexagonPath);
        super.onDraw(canvas);
        canvas.restore();
        canvas.drawPath(this.hexagonPath, this.borderPaint);
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.width = (float) getMeasuredWidth();
        this.height = (float) getMeasuredHeight();
        calculatePath();
    }
}
