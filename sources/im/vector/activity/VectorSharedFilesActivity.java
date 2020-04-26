package im.vector.activity;

import android.content.Intent;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.NotificationListenerDetectionActivity;
import im.vector.Matrix;
import java.io.File;
import java.util.ArrayList;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.FileContentUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.data.RoomMediaMessage;

public class VectorSharedFilesActivity extends VectorAppCompatActivity {
    private static final String LOG_TAG = VectorSharedFilesActivity.class.getSimpleName();
    private final String SHARED_FOLDER = "VectorShared";

    public int getLayoutRes() {
        return R.layout.activity_empty;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x007e  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0082  */
    public void initUiAndData() {
        boolean z;
        boolean z2;
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onCreate : action ");
            sb.append(action);
            sb.append(" type ");
            sb.append(type);
            Log.d(str, sb.toString());
            if (("android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action)) && type != null) {
                boolean z3 = false;
                try {
                    MXSession defaultSession = Matrix.getInstance(this).getDefaultSession();
                    if (defaultSession != null) {
                        z2 = true;
                        try {
                            z = defaultSession.getDataHandler().getStore().isReady();
                            z3 = true;
                        } catch (Exception e) {
                            e = e;
                            String str2 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## onCreate() : failed ");
                            sb2.append(e.getMessage());
                            Log.e(str2, sb2.toString(), e);
                            z3 = z2;
                            z = false;
                            if (!z3) {
                            }
                            finish();
                        }
                        if (!z3) {
                            launchActivity(intent, z);
                        } else {
                            Log.d(LOG_TAG, "onCreate : go to login screen");
                            startActivity(new Intent(this, NotificationListenerDetectionActivity.class));
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    z2 = false;
                    String str22 = LOG_TAG;
                    StringBuilder sb22 = new StringBuilder();
                    sb22.append("## onCreate() : failed ");
                    sb22.append(e.getMessage());
                    Log.e(str22, sb22.toString(), e);
                    z3 = z2;
                    z = false;
                    if (!z3) {
                    }
                    finish();
                }
                z = false;
                if (!z3) {
                }
            } else {
                Log.d(LOG_TAG, "onCreate : unsupported action");
                Intent intent2 = new Intent(this, VectorHomeActivity.class);
                intent2.addFlags(335577088);
                startActivity(intent2);
            }
        } else {
            Log.d(LOG_TAG, "onCreate : null intent");
            Intent intent3 = new Intent(this, VectorHomeActivity.class);
            intent3.addFlags(335577088);
            startActivity(intent3);
        }
        finish();
    }

    private void launchActivity(Intent intent, boolean z) {
        Intent intent2;
        File file = new File(getCacheDir(), "VectorShared");
        if (file.exists()) {
            FileContentUtils.deleteDirectory(file);
        }
        file.mkdir();
        ArrayList<RoomMediaMessage> arrayList = new ArrayList<>(RoomMediaMessage.listRoomMediaMessages(intent));
        for (RoomMediaMessage saveMedia : arrayList) {
            saveMedia.saveMedia(this, file);
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate : launch home activity with the files list ");
        sb.append(arrayList.size());
        sb.append(" files");
        Log.d(str, sb.toString());
        if (z) {
            intent2 = new Intent(this, VectorHomeActivity.class);
        } else {
            intent2 = new Intent(this, SplashActivity.class);
        }
        intent2.addFlags(335577088);
        if (arrayList.size() != 0) {
            Intent intent3 = new Intent();
            intent3.setAction("android.intent.action.SEND_MULTIPLE");
            intent3.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
            intent3.setExtrasClassLoader(RoomMediaMessage.class.getClassLoader());
            intent3.setType(ResourceUtils.MIME_TYPE_ALL_CONTENT);
            intent2.putExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS, intent3);
        }
        startActivity(intent2);
    }
}
