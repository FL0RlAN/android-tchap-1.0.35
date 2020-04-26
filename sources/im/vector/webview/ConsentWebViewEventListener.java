package im.vector.webview;

import im.vector.Matrix;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.util.WeakReferenceDelegate;
import im.vector.util.WeakReferenceDelegateKt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000*\u0001\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001¢\u0006\u0002\u0010\u0005J\b\u0010\u000e\u001a\u00020\u000fH\u0002J!\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0001J\u0010\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0011\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0001J\u0011\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0001J\u0011\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u0012H\u0001R\u0010\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0004\n\u0002\u0010\bR\u000e\u0010\u0004\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u0004\u0018\u00010\u00038BX\u0002¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000b¨\u0006\u001b"}, d2 = {"Lim/vector/webview/ConsentWebViewEventListener;", "Lim/vector/webview/WebViewEventListener;", "activity", "Lim/vector/activity/VectorAppCompatActivity;", "delegate", "(Lim/vector/activity/VectorAppCompatActivity;Lim/vector/webview/WebViewEventListener;)V", "createRiotBotRoomCallback", "im/vector/webview/ConsentWebViewEventListener$createRiotBotRoomCallback$1", "Lim/vector/webview/ConsentWebViewEventListener$createRiotBotRoomCallback$1;", "safeActivity", "getSafeActivity", "()Lim/vector/activity/VectorAppCompatActivity;", "safeActivity$delegate", "Lim/vector/util/WeakReferenceDelegate;", "createRiotBotRoomIfNeeded", "", "onPageError", "url", "", "errorCode", "", "description", "onPageFinished", "onPageStarted", "pageWillStart", "shouldOverrideUrlLoading", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ConsentWebViewEventListener.kt */
public final class ConsentWebViewEventListener implements WebViewEventListener {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(ConsentWebViewEventListener.class), "safeActivity", "getSafeActivity()Lim/vector/activity/VectorAppCompatActivity;"))};
    /* access modifiers changed from: private */
    public final ConsentWebViewEventListener$createRiotBotRoomCallback$1 createRiotBotRoomCallback = new ConsentWebViewEventListener$createRiotBotRoomCallback$1(this);
    private final WebViewEventListener delegate;
    private final WeakReferenceDelegate safeActivity$delegate;

    /* access modifiers changed from: private */
    public final VectorAppCompatActivity getSafeActivity() {
        return (VectorAppCompatActivity) this.safeActivity$delegate.getValue(this, $$delegatedProperties[0]);
    }

    public void onPageError(String str, int i, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        Intrinsics.checkParameterIsNotNull(str2, "description");
        this.delegate.onPageError(str, i, str2);
    }

    public void onPageStarted(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        this.delegate.onPageStarted(str);
    }

    public void pageWillStart(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        this.delegate.pageWillStart(str);
    }

    public boolean shouldOverrideUrlLoading(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        return this.delegate.shouldOverrideUrlLoading(str);
    }

    public ConsentWebViewEventListener(VectorAppCompatActivity vectorAppCompatActivity, WebViewEventListener webViewEventListener) {
        Intrinsics.checkParameterIsNotNull(vectorAppCompatActivity, "activity");
        Intrinsics.checkParameterIsNotNull(webViewEventListener, "delegate");
        this.delegate = webViewEventListener;
        this.safeActivity$delegate = WeakReferenceDelegateKt.weak(vectorAppCompatActivity);
    }

    public void onPageFinished(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        this.delegate.onPageFinished(str);
        if (StringsKt.endsWith$default(str, "/_matrix/consent", false, 2, null)) {
            createRiotBotRoomIfNeeded();
        }
    }

    private final void createRiotBotRoomIfNeeded() {
        VectorAppCompatActivity safeActivity = getSafeActivity();
        if (safeActivity != null) {
            Matrix instance = Matrix.getInstance(safeActivity);
            Intrinsics.checkExpressionValueIsNotNull(instance, "Matrix.getInstance(it)");
            MXSession defaultSession = instance.getDefaultSession();
            IMXStore store = defaultSession.getDataHandler().getStore();
            Intrinsics.checkExpressionValueIsNotNull(store, "session.dataHandler.store");
            Collection rooms = store.getRooms();
            Intrinsics.checkExpressionValueIsNotNull(rooms, "session.dataHandler.store.rooms");
            Iterable iterable = rooms;
            Collection arrayList = new ArrayList();
            for (Object next : iterable) {
                Room room = (Room) next;
                Intrinsics.checkExpressionValueIsNotNull(room, "it");
                if (room.isJoined()) {
                    arrayList.add(next);
                }
            }
            if (((List) arrayList).isEmpty()) {
                safeActivity.showWaitingView();
                Intrinsics.checkExpressionValueIsNotNull(defaultSession, "session");
                defaultSession.getProfileApiClient().displayname("@riot-bot:matrix.org", new ConsentWebViewEventListener$createRiotBotRoomIfNeeded$$inlined$let$lambda$1(defaultSession, this.createRiotBotRoomCallback, this));
                return;
            }
            safeActivity.finish();
        }
    }
}
