package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.notifications.NotificationUtils;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.data.Room;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\b"}, d2 = {"Lim/vector/activity/JoinRoomActivity;", "Lim/vector/activity/VectorAppCompatActivity;", "()V", "getLayoutRes", "", "initUiAndData", "", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: JoinRoomActivity.kt */
public final class JoinRoomActivity extends VectorAppCompatActivity {
    public static final Companion Companion = new Companion(null);
    private static final String EXTRA_JOIN = "EXTRA_JOIN";
    private static final String EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID";
    private static final String EXTRA_REJECT = "EXTRA_REJECT";
    private static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = JoinRoomActivity.class.getSimpleName();
    private HashMap _$_findViewCache;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004J\u001e\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \t*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lim/vector/activity/JoinRoomActivity$Companion;", "", "()V", "EXTRA_JOIN", "", "EXTRA_MATRIX_ID", "EXTRA_REJECT", "EXTRA_ROOM_ID", "LOG_TAG", "kotlin.jvm.PlatformType", "getJoinRoomIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "roomId", "matrixId", "getRejectRoomIntent", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: JoinRoomActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Intent getJoinRoomIntent(Context context, String str, String str2) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
            Intrinsics.checkParameterIsNotNull(str2, "matrixId");
            Intent putExtra = new Intent(context, JoinRoomActivity.class).putExtra("EXTRA_ROOM_ID", str).putExtra("EXTRA_MATRIX_ID", str2).putExtra(JoinRoomActivity.EXTRA_JOIN, true);
            Intrinsics.checkExpressionValueIsNotNull(putExtra, "Intent(context, JoinRoom…utExtra(EXTRA_JOIN, true)");
            return putExtra;
        }

        public final Intent getRejectRoomIntent(Context context, String str, String str2) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
            Intrinsics.checkParameterIsNotNull(str2, "matrixId");
            Intent putExtra = new Intent(context, JoinRoomActivity.class).putExtra("EXTRA_ROOM_ID", str).putExtra("EXTRA_MATRIX_ID", str2).putExtra(JoinRoomActivity.EXTRA_REJECT, true);
            Intrinsics.checkExpressionValueIsNotNull(putExtra, "Intent(context, JoinRoom…Extra(EXTRA_REJECT, true)");
            return putExtra;
        }
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public int getLayoutRes() {
        return R.layout.activity_empty;
    }

    public void initUiAndData() {
        String stringExtra = getIntent().getStringExtra("EXTRA_ROOM_ID");
        String stringExtra2 = getIntent().getStringExtra("EXTRA_MATRIX_ID");
        boolean booleanExtra = getIntent().getBooleanExtra(EXTRA_JOIN, false);
        boolean booleanExtra2 = getIntent().getBooleanExtra(EXTRA_REJECT, false);
        NotificationUtils.INSTANCE.cancelNotificationMessage(this);
        if (TextUtils.isEmpty(stringExtra) || TextUtils.isEmpty(stringExtra2)) {
            Log.e(LOG_TAG, "## onCreate() : invalid parameters");
            finish();
            return;
        }
        Matrix instance = Matrix.getInstance(getApplicationContext());
        if (instance == null) {
            Intrinsics.throwNpe();
        }
        MXSession session = instance.getSession(stringExtra2);
        String str = "## onCreate() : undefined parameters";
        if (session == null || !session.isAlive()) {
            Log.e(LOG_TAG, str);
            finish();
            return;
        }
        Room room = session.getDataHandler().getRoom(stringExtra);
        if (room == null) {
            Log.e(LOG_TAG, str);
            finish();
            return;
        }
        if (booleanExtra) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onCreate() : Join the room ");
            sb.append(stringExtra);
            Log.d(str2, sb.toString());
            room.join(new JoinRoomActivity$initUiAndData$1());
        } else if (booleanExtra2) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## onCreate() : reject the invitation to room ");
            sb2.append(stringExtra);
            Log.d(str3, sb2.toString());
            room.leave(new JoinRoomActivity$initUiAndData$2());
        }
        finish();
    }
}
