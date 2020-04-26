package im.vector.webview;

import kotlin.Metadata;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\u0006¸\u0006\u0000"}, d2 = {"im/vector/webview/ConsentWebViewEventListener$createRiotBotRoomIfNeeded$1$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "", "onSuccess", "", "info", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ConsentWebViewEventListener.kt */
public final class ConsentWebViewEventListener$createRiotBotRoomIfNeeded$$inlined$let$lambda$1 extends SimpleApiCallback<String> {
    final /* synthetic */ MXSession $session;
    final /* synthetic */ ConsentWebViewEventListener this$0;

    ConsentWebViewEventListener$createRiotBotRoomIfNeeded$$inlined$let$lambda$1(MXSession mXSession, ApiFailureCallback apiFailureCallback, ConsentWebViewEventListener consentWebViewEventListener) {
        this.$session = mXSession;
        this.this$0 = consentWebViewEventListener;
        super(apiFailureCallback);
    }

    public void onSuccess(String str) {
        this.$session.createDirectMessageRoom("@riot-bot:matrix.org", this.this$0.createRiotBotRoomCallback);
    }
}
