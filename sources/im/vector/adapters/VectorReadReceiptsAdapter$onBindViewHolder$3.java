package im.vector.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import im.vector.adapters.VectorReadReceiptsAdapter.VectorReadReceiptsAdapterListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.rest.model.RoomMember;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorReadReceiptsAdapter.kt */
final class VectorReadReceiptsAdapter$onBindViewHolder$3 implements OnClickListener {
    final /* synthetic */ RoomMember $member;
    final /* synthetic */ VectorReadReceiptsAdapter this$0;

    VectorReadReceiptsAdapter$onBindViewHolder$3(VectorReadReceiptsAdapter vectorReadReceiptsAdapter, RoomMember roomMember) {
        this.this$0 = vectorReadReceiptsAdapter;
        this.$member = roomMember;
    }

    public final void onClick(View view) {
        if (this.$member != null) {
            VectorReadReceiptsAdapterListener access$getListener$p = this.this$0.listener;
            String userId = this.$member.getUserId();
            Intrinsics.checkExpressionValueIsNotNull(userId, "member.userId");
            access$getListener$p.onMemberClicked(userId);
        }
    }
}
