package im.vector.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b \u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J$\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016¨\u0006\r"}, d2 = {"Lim/vector/dialogs/DialogAdapter;", "Landroid/widget/ArrayAdapter;", "Lim/vector/dialogs/DialogListItem;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getView", "Landroid/view/View;", "position", "", "convertView", "parent", "Landroid/view/ViewGroup;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DialogAdapter.kt */
public abstract class DialogAdapter extends ArrayAdapter<DialogListItem> {
    public DialogAdapter(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, R.layout.adapter_item_dialog);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_dialog, viewGroup, false);
            Intrinsics.checkExpressionValueIsNotNull(view, "view");
            view.setTag(new DialogListItemHolder(view));
        }
        Object tag = view.getTag();
        if (tag != null) {
            DialogListItemHolder dialogListItemHolder = (DialogListItemHolder) tag;
            dialogListItemHolder.getIcon().setImageResource(((DialogListItem) getItem(i)).getIconRes());
            dialogListItemHolder.getText().setText(((DialogListItem) getItem(i)).getTitleRes());
            return view;
        }
        throw new TypeCastException("null cannot be cast to non-null type im.vector.dialogs.DialogListItemHolder");
    }
}
