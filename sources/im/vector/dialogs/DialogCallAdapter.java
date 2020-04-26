package im.vector.dialogs;

import android.content.Context;
import im.vector.dialogs.DialogListItem.StartVideoCall;
import im.vector.dialogs.DialogListItem.StartVoiceCall;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lim/vector/dialogs/DialogCallAdapter;", "Lim/vector/dialogs/DialogAdapter;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DialogCallAdapter.kt */
public final class DialogCallAdapter extends DialogAdapter {
    public DialogCallAdapter(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context);
        add(StartVoiceCall.INSTANCE);
        add(StartVideoCall.INSTANCE);
    }
}
