package im.vector.webview;

import im.vector.activity.VectorAppCompatActivity;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u0007\b\u0002¢\u0006\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lim/vector/webview/WebViewMode;", "", "Lim/vector/webview/WebViewEventListenerFactory;", "(Ljava/lang/String;I)V", "DEFAULT", "CONSENT", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: WebViewMode.kt */
public enum WebViewMode implements WebViewEventListenerFactory {
    ;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"Lim/vector/webview/WebViewMode$CONSENT;", "Lim/vector/webview/WebViewMode;", "eventListener", "Lim/vector/webview/WebViewEventListener;", "activity", "Lim/vector/activity/VectorAppCompatActivity;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: WebViewMode.kt */
    static final class CONSENT extends WebViewMode {
        CONSENT(String str, int i) {
            super(str, i, null);
        }

        public WebViewEventListener eventListener(VectorAppCompatActivity vectorAppCompatActivity) {
            Intrinsics.checkParameterIsNotNull(vectorAppCompatActivity, "activity");
            return new ConsentWebViewEventListener(vectorAppCompatActivity, new DefaultWebViewEventListener());
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"Lim/vector/webview/WebViewMode$DEFAULT;", "Lim/vector/webview/WebViewMode;", "eventListener", "Lim/vector/webview/WebViewEventListener;", "activity", "Lim/vector/activity/VectorAppCompatActivity;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: WebViewMode.kt */
    static final class DEFAULT extends WebViewMode {
        DEFAULT(String str, int i) {
            super(str, i, null);
        }

        public WebViewEventListener eventListener(VectorAppCompatActivity vectorAppCompatActivity) {
            Intrinsics.checkParameterIsNotNull(vectorAppCompatActivity, "activity");
            return new DefaultWebViewEventListener();
        }
    }
}
