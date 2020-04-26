package im.vector.activity;

import android.content.Intent;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.RoomPreviewData;

public class VectorFakeRoomPreviewActivity extends VectorAppCompatActivity {
    private static final String LOG_TAG = VectorFakeRoomPreviewActivity.class.getSimpleName();

    public int getLayoutRes() {
        return R.layout.activity_empty;
    }

    public void initUiAndData() {
        Intent intent = getIntent();
        if (intent == null) {
            Log.w(LOG_TAG, "## onCreate(): Failure - received intent is null");
        } else {
            String str = "EXTRA_ROOM_ID";
            String stringExtra = intent.getStringExtra(str);
            if (stringExtra == null) {
                Log.w(LOG_TAG, "## onCreate(): Failure - matrix ID is null");
            } else {
                MXSession session = Matrix.getInstance(getApplicationContext()).getSession(stringExtra);
                if (session == null) {
                    session = Matrix.getInstance(getApplicationContext()).getDefaultSession();
                }
                MXSession mXSession = session;
                if (mXSession == null || !mXSession.isAlive()) {
                    Log.w(LOG_TAG, "## onCreate(): Failure - session is null");
                } else {
                    RoomPreviewData roomPreviewData = new RoomPreviewData(mXSession, intent.getStringExtra(str), null, intent.getStringExtra(VectorRoomActivity.EXTRA_ROOM_PREVIEW_ROOM_ALIAS), null);
                    VectorRoomActivity.sRoomPreviewData = roomPreviewData;
                    Intent intent2 = new Intent(intent);
                    intent2.setClass(this, VectorRoomActivity.class);
                    intent2.setFlags(603979776);
                    startActivity(intent2);
                }
            }
        }
        finish();
    }
}
