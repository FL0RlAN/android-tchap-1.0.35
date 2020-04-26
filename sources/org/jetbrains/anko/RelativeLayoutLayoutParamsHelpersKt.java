package org.jetbrains.anko;

import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u001a\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0017\u0010\u0007\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\r\u0010\b\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u000b\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\f\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\r\u001a\u00020\u0001*\u00020\u0002H\b\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u000f\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0012\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u0017\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0012\u0010\u0011\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u0017\u0010\u0011\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\r\u0010\u0012\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u0013\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u0014\u001a\u00020\u0001*\u00020\u0002H\b\u001a\u0015\u0010\u0015\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u0015\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u0016\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u0016\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u0017\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u0017\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u001a\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u001a\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u001b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u001b\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u001c\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u001c\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u001d\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u001d\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0015\u0010\u001e\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0017\u0010\u001e\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b\u001a\u0012\u0010\u001f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u0017\u0010\u001f\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\b¨\u0006 "}, d2 = {"above", "", "Landroid/widget/RelativeLayout$LayoutParams;", "view", "Landroid/view/View;", "id", "", "alignEnd", "alignParentBottom", "alignParentEnd", "alignParentLeft", "alignParentRight", "alignParentStart", "alignParentTop", "alignStart", "baselineOf", "below", "bottomOf", "centerHorizontally", "centerInParent", "centerVertically", "endOf", "leftOf", "rightOf", "sameBottom", "sameEnd", "sameLeft", "sameRight", "sameStart", "sameTop", "startOf", "topOf", "commons-base_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: RelativeLayoutLayoutParamsHelpers.kt */
public final class RelativeLayoutLayoutParamsHelpersKt {
    public static final void topOf(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(2, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void above(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(2, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void bottomOf(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(3, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void below(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(3, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void leftOf(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(0, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void startOf(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(16, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void rightOf(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(1, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void endOf(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(17, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void sameLeft(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(5, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void sameStart(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(18, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void sameTop(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(6, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void sameRight(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(7, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void sameEnd(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(19, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void sameBottom(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(8, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void topOf(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(2, i);
    }

    public static final void above(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(2, i);
    }

    public static final void below(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(3, i);
    }

    public static final void bottomOf(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(3, i);
    }

    public static final void leftOf(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(0, i);
    }

    public static final void startOf(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(16, i);
    }

    public static final void rightOf(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(1, i);
    }

    public static final void endOf(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(17, i);
    }

    public static final void sameLeft(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(5, i);
    }

    public static final void sameStart(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(18, i);
    }

    public static final void sameTop(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(6, i);
    }

    public static final void sameRight(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(7, i);
    }

    public static final void sameEnd(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(19, i);
    }

    public static final void sameBottom(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(8, i);
    }

    public static final void alignStart(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(18, i);
    }

    public static final void alignEnd(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(19, i);
    }

    public static final void alignParentTop(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(10);
    }

    public static final void alignParentRight(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(11);
    }

    public static final void alignParentBottom(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(12);
    }

    public static final void alignParentLeft(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(9);
    }

    public static final void centerHorizontally(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(14);
    }

    public static final void centerVertically(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(15);
    }

    public static final void centerInParent(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(13);
    }

    public static final void alignParentStart(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(20);
    }

    public static final void alignParentEnd(LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(21);
    }

    public static final void baselineOf(LayoutParams layoutParams, View view) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        Intrinsics.checkParameterIsNotNull(view, "view");
        int id = view.getId();
        if (id != -1) {
            layoutParams.addRule(4, id);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id is not set for ");
        sb.append(view);
        throw new AnkoException(sb.toString());
    }

    public static final void baselineOf(LayoutParams layoutParams, int i) {
        Intrinsics.checkParameterIsNotNull(layoutParams, "receiver$0");
        layoutParams.addRule(4, i);
    }
}
