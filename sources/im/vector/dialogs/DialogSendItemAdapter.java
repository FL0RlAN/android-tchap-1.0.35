package im.vector.dialogs;

import android.content.Context;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"Lim/vector/dialogs/DialogSendItemAdapter;", "Lim/vector/dialogs/DialogAdapter;", "context", "Landroid/content/Context;", "items", "", "Lim/vector/dialogs/DialogListItem;", "(Landroid/content/Context;Ljava/util/List;)V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DialogSendItemAdapter.kt */
public final class DialogSendItemAdapter extends DialogAdapter {
    public DialogSendItemAdapter(Context context, List<DialogListItem> list) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(list, "items");
        super(context);
        addAll(list);
    }
}
