package im.vector.dialogs;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u0011"}, d2 = {"Lim/vector/dialogs/DialogListItemHolder;", "", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "icon", "Landroid/widget/ImageView;", "getIcon", "()Landroid/widget/ImageView;", "setIcon", "(Landroid/widget/ImageView;)V", "text", "Landroid/widget/TextView;", "getText", "()Landroid/widget/TextView;", "setText", "(Landroid/widget/TextView;)V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DialogListItemHolder.kt */
public final class DialogListItemHolder {
    @BindView(2131296305)
    public ImageView icon;
    @BindView(2131296306)
    public TextView text;

    public DialogListItemHolder(View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        ButterKnife.bind((Object) this, view);
    }

    public final ImageView getIcon() {
        ImageView imageView = this.icon;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("icon");
        }
        return imageView;
    }

    public final void setIcon(ImageView imageView) {
        Intrinsics.checkParameterIsNotNull(imageView, "<set-?>");
        this.icon = imageView;
    }

    public final TextView getText() {
        TextView textView = this.text;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("text");
        }
        return textView;
    }

    public final void setText(TextView textView) {
        Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
        this.text = textView;
    }
}
