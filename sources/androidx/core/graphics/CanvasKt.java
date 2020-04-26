package androidx.core.graphics;

import android.graphics.Canvas;
import android.graphics.Matrix;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\t\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\b\u001aD\u0010\b\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\b\u001a&\u0010\r\u001a\u00020\u0001*\u00020\u00022\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\b\u001aN\u0010\u000e\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\b\u001a:\u0010\u0011\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\n2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\b\u001a:\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\n2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0006¢\u0006\u0002\b\u0007H\b¨\u0006\u0013"}, d2 = {"withMatrix", "", "Landroid/graphics/Canvas;", "matrix", "Landroid/graphics/Matrix;", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "withRotation", "degrees", "", "pivotX", "pivotY", "withSave", "withScale", "x", "y", "withSkew", "withTranslation", "core-ktx_release"}, k = 2, mv = {1, 1, 10})
/* compiled from: Canvas.kt */
public final class CanvasKt {
    public static final void withSave(Canvas canvas, Function1<? super Canvas, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static /* bridge */ /* synthetic */ void withTranslation$default(Canvas canvas, float f, float f2, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            f = 0.0f;
        }
        if ((i & 2) != 0) {
            f2 = 0.0f;
        }
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.translate(f, f2);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final void withTranslation(Canvas canvas, float f, float f2, Function1<? super Canvas, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.translate(f, f2);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static /* bridge */ /* synthetic */ void withRotation$default(Canvas canvas, float f, float f2, float f3, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            f = 0.0f;
        }
        if ((i & 2) != 0) {
            f2 = 0.0f;
        }
        if ((i & 4) != 0) {
            f3 = 0.0f;
        }
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.rotate(f, f2, f3);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final void withRotation(Canvas canvas, float f, float f2, float f3, Function1<? super Canvas, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.rotate(f, f2, f3);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static /* bridge */ /* synthetic */ void withScale$default(Canvas canvas, float f, float f2, float f3, float f4, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            f = 1.0f;
        }
        if ((i & 2) != 0) {
            f2 = 1.0f;
        }
        if ((i & 4) != 0) {
            f3 = 0.0f;
        }
        if ((i & 8) != 0) {
            f4 = 0.0f;
        }
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.scale(f, f2, f3, f4);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final void withScale(Canvas canvas, float f, float f2, float f3, float f4, Function1<? super Canvas, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.scale(f, f2, f3, f4);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static /* bridge */ /* synthetic */ void withSkew$default(Canvas canvas, float f, float f2, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            f = 0.0f;
        }
        if ((i & 2) != 0) {
            f2 = 0.0f;
        }
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.skew(f, f2);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final void withSkew(Canvas canvas, float f, float f2, Function1<? super Canvas, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.skew(f, f2);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static /* bridge */ /* synthetic */ void withMatrix$default(Canvas canvas, Matrix matrix, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            matrix = new Matrix();
        }
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(matrix, "matrix");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.concat(matrix);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final void withMatrix(Canvas canvas, Matrix matrix, Function1<? super Canvas, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(canvas, "$receiver");
        Intrinsics.checkParameterIsNotNull(matrix, "matrix");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        int save = canvas.save();
        canvas.concat(matrix);
        try {
            function1.invoke(canvas);
        } finally {
            InlineMarker.finallyStart(1);
            canvas.restoreToCount(save);
            InlineMarker.finallyEnd(1);
        }
    }
}
