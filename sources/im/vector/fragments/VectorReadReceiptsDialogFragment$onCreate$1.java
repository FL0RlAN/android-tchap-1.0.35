package im.vector.fragments;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.rest.model.RoomMember;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001J\u0016\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0016¨\u0006\u0007"}, d2 = {"im/vector/fragments/VectorReadReceiptsDialogFragment$onCreate$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "", "Lorg/matrix/androidsdk/rest/model/RoomMember;", "onSuccess", "", "info", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorReadReceiptsDialogFragment.kt */
public final class VectorReadReceiptsDialogFragment$onCreate$1 extends SimpleApiCallback<List<? extends RoomMember>> {
    final /* synthetic */ VectorReadReceiptsDialogFragment this$0;

    VectorReadReceiptsDialogFragment$onCreate$1(VectorReadReceiptsDialogFragment vectorReadReceiptsDialogFragment) {
        this.this$0 = vectorReadReceiptsDialogFragment;
    }

    public void onSuccess(List<? extends RoomMember> list) {
        Intrinsics.checkParameterIsNotNull(list, "info");
        if (this.this$0.isAdded()) {
            VectorReadReceiptsDialogFragment.access$getMAdapter$p(this.this$0).notifyDataSetChanged();
        }
    }
}
