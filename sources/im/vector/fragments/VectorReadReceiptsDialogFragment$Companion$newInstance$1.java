package im.vector.fragments;

import android.os.Bundle;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "Landroid/os/Bundle;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorReadReceiptsDialogFragment.kt */
final class VectorReadReceiptsDialogFragment$Companion$newInstance$1 extends Lambda implements Function1<Bundle, Unit> {
    final /* synthetic */ String $eventId;
    final /* synthetic */ String $roomId;
    final /* synthetic */ String $userId;

    VectorReadReceiptsDialogFragment$Companion$newInstance$1(String str, String str2, String str3) {
        this.$userId = str;
        this.$roomId = str2;
        this.$eventId = str3;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Bundle) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(bundle, "receiver$0");
        bundle.putString("VectorReadReceiptsDialogFragment.ARG_SESSION_ID", this.$userId);
        bundle.putString("VectorReadReceiptsDialogFragment.ARG_ROOM_ID", this.$roomId);
        bundle.putString("VectorReadReceiptsDialogFragment.ARG_EVENT_ID", this.$eventId);
    }
}
