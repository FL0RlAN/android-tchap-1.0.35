package im.vector.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import androidx.core.content.ContextCompat;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\r\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J`\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0016Jh\u0010\u001c\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0016J\u0010\u0010#\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020 H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lim/vector/ui/VectorQuoteSpan;", "Landroid/text/style/LeadingMarginSpan;", "Landroid/text/style/LineBackgroundSpan;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "backgroundColor", "", "gap", "", "stripeColor", "stripeWidth", "drawBackground", "", "c", "Landroid/graphics/Canvas;", "p", "Landroid/graphics/Paint;", "left", "right", "top", "baseline", "bottom", "text", "", "start", "end", "lnum", "drawLeadingMargin", "x", "dir", "first", "", "layout", "Landroid/text/Layout;", "getLeadingMargin", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorQuoteSpan.kt */
public final class VectorQuoteSpan implements LeadingMarginSpan, LineBackgroundSpan {
    private final int backgroundColor;
    private final float gap;
    private final int stripeColor;
    private final float stripeWidth;

    public VectorQuoteSpan(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.backgroundColor = ContextCompat.getColor(context, R.color.quote_background_color);
        this.stripeColor = ContextCompat.getColor(context, R.color.quote_strip_color);
        this.stripeWidth = context.getResources().getDimension(R.dimen.quote_width);
        this.gap = context.getResources().getDimension(R.dimen.quote_gap);
    }

    public int getLeadingMargin(boolean z) {
        return (int) (this.stripeWidth + this.gap);
    }

    public void drawLeadingMargin(Canvas canvas, Paint paint, int i, int i2, int i3, int i4, int i5, CharSequence charSequence, int i6, int i7, boolean z, Layout layout) {
        Paint paint2 = paint;
        Canvas canvas2 = canvas;
        Intrinsics.checkParameterIsNotNull(canvas, "c");
        Intrinsics.checkParameterIsNotNull(paint, "p");
        Intrinsics.checkParameterIsNotNull(charSequence, "text");
        Intrinsics.checkParameterIsNotNull(layout, "layout");
        Style style = paint.getStyle();
        int color = paint.getColor();
        paint.setStyle(Style.FILL);
        paint.setColor(this.stripeColor);
        float f = (float) i;
        canvas.drawRect(f, (float) i3, (((float) i2) * this.stripeWidth) + f, (float) i5, paint);
        paint.setStyle(style);
        paint.setColor(color);
    }

    public void drawBackground(Canvas canvas, Paint paint, int i, int i2, int i3, int i4, int i5, CharSequence charSequence, int i6, int i7, int i8) {
        Paint paint2 = paint;
        Canvas canvas2 = canvas;
        Intrinsics.checkParameterIsNotNull(canvas, "c");
        Intrinsics.checkParameterIsNotNull(paint, "p");
        Intrinsics.checkParameterIsNotNull(charSequence, "text");
        int color = paint.getColor();
        paint.setColor(this.backgroundColor);
        canvas.drawRect((float) i, (float) i3, (float) i2, (float) i5, paint);
        paint.setColor(color);
    }
}
