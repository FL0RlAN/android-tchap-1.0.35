package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import im.vector.util.SystemUtilsKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomMember;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "v", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onLongClick", "im/vector/adapters/VectorReadReceiptsAdapter$onBindViewHolder$1$1"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorReadReceiptsAdapter.kt */
final class VectorReadReceiptsAdapter$onBindViewHolder$$inlined$let$lambda$1 implements OnLongClickListener {
    final /* synthetic */ TextView $it;
    final /* synthetic */ RoomMember $member$inlined;
    final /* synthetic */ ReceiptData $receipt$inlined;
    final /* synthetic */ VectorReadReceiptsAdapter this$0;

    VectorReadReceiptsAdapter$onBindViewHolder$$inlined$let$lambda$1(TextView textView, VectorReadReceiptsAdapter vectorReadReceiptsAdapter, RoomMember roomMember, ReceiptData receiptData) {
        this.$it = textView;
        this.this$0 = vectorReadReceiptsAdapter;
        this.$member$inlined = roomMember;
        this.$receipt$inlined = receiptData;
    }

    public final boolean onLongClick(View view) {
        Context access$getMContext$p = this.this$0.mContext;
        CharSequence text = this.$it.getText();
        Intrinsics.checkExpressionValueIsNotNull(text, "it.text");
        SystemUtilsKt.copyToClipboard(access$getMContext$p, text);
        return true;
    }
}
