package im.vector.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import fr.gouv.tchap.activity.TchapLoginActivity;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorGroupDetailsActivity;
import im.vector.activity.VectorHomeActivity;
import im.vector.activity.VectorMemberDetailsActivity;
import im.vector.activity.VectorRoomActivity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;

public class VectorUniversalLinkReceiver extends BroadcastReceiver {
    public static final String BROADCAST_ACTION_UNIVERSAL_LINK = "im.vector.receiver.UNIVERSAL_LINK";
    public static final String BROADCAST_ACTION_UNIVERSAL_LINK_RESUME = "im.vector.receiver.UNIVERSAL_LINK_RESUME";
    public static final String EXTRA_UNIVERSAL_LINK_SENDER_ID = "EXTRA_UNIVERSAL_LINK_SENDER_ID";
    public static final String EXTRA_UNIVERSAL_LINK_URI = "EXTRA_UNIVERSAL_LINK_URI";
    public static final String HOME_SENDER_ID = VectorHomeActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorUniversalLinkReceiver.class.getSimpleName();
    private static final List<String> sSupportedVectorHosts = Arrays.asList(new String[]{"www.tchap.gouv.fr", "www.beta.tchap.gouv.fr"});
    private static final List<String> sSupportedVectorLinkPaths = Arrays.asList(new String[]{"/"});
    /* access modifiers changed from: private */
    public Map<String, String> mParameters;
    private MXSession mSession;

    public void onReceive(Context context, Intent intent) {
        Uri uri;
        Log.d(LOG_TAG, "## onReceive() IN");
        this.mSession = Matrix.getInstance(context).getDefaultSession();
        MXSession mXSession = this.mSession;
        String str = EXTRA_UNIVERSAL_LINK_URI;
        if (mXSession == null) {
            Log.e(LOG_TAG, "## onReceive() Warning - Unable to proceed URL link: Session is null");
            Intent intent2 = new Intent(context, TchapLoginActivity.class);
            intent2.putExtra(str, intent.getData());
            intent2.addFlags(335577088);
            context.startActivity(intent2);
            return;
        }
        if (intent != null) {
            String action = intent.getAction();
            String dataString = intent.getDataString();
            boolean isAlive = this.mSession.isAlive();
            boolean isInitialSyncComplete = this.mSession.getDataHandler().isInitialSyncComplete();
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onReceive() uri getDataString=");
            sb.append(dataString);
            sb.append("isSessionActive=");
            sb.append(isAlive);
            sb.append(" isLoginStepDone=");
            sb.append(isInitialSyncComplete);
            Log.d(str2, sb.toString());
            if (TextUtils.equals(action, BROADCAST_ACTION_UNIVERSAL_LINK)) {
                Log.d(LOG_TAG, "## onReceive() action = BROADCAST_ACTION_UNIVERSAL_LINK");
                uri = intent.getData();
            } else if (TextUtils.equals(action, BROADCAST_ACTION_UNIVERSAL_LINK_RESUME)) {
                Log.d(LOG_TAG, "## onReceive() action = BROADCAST_ACTION_UNIVERSAL_LINK_RESUME");
                uri = (Uri) intent.getParcelableExtra(str);
            } else {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## onReceive() Unknown action received (");
                sb2.append(action);
                sb2.append(") - unable to proceed URL link");
                Log.e(str3, sb2.toString());
            }
            if (uri != null) {
                String str4 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## onCreate() intentUri - host=");
                sb3.append(uri.getHost());
                sb3.append(" path=");
                sb3.append(uri.getPath());
                sb3.append(" queryParams=");
                sb3.append(uri.getQuery());
                Log.d(str4, sb3.toString());
                Map<String, String> parseUniversalLink = parseUniversalLink(uri);
                if (parseUniversalLink != null) {
                    if (!isAlive) {
                        Log.w(LOG_TAG, "## onReceive() Warning: Session is not alive");
                    }
                    if (!isInitialSyncComplete) {
                        Log.w(LOG_TAG, "## onReceive() Warning: Session is not complete - start Login Activity");
                        Intent intent3 = new Intent(context, TchapLoginActivity.class);
                        intent3.putExtra(str, intent.getData());
                        intent3.setFlags(268435456);
                        context.startActivity(intent3);
                    } else {
                        this.mParameters = parseUniversalLink;
                        if (this.mParameters.containsKey(PermalinkUtils.ULINK_ROOM_ID_OR_ALIAS_KEY)) {
                            manageRoomOnActivity(context);
                        } else if (this.mParameters.containsKey(PermalinkUtils.ULINK_MATRIX_USER_ID_KEY)) {
                            manageMemberDetailsActivity(context);
                        } else if (this.mParameters.containsKey(PermalinkUtils.ULINK_GROUP_ID_KEY)) {
                            manageGroupDetailsActivity(context);
                        } else {
                            Log.e(LOG_TAG, "## onReceive() : nothing to do");
                        }
                    }
                } else {
                    String str5 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## onReceive() Path not supported: ");
                    sb4.append(uri.getPath());
                    Log.e(str5, sb4.toString());
                }
            }
        }
    }

    private void manageMemberDetailsActivity(Context context) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## manageMemberDetailsActivity() : open ");
        Map<String, String> map = this.mParameters;
        String str2 = PermalinkUtils.ULINK_MATRIX_USER_ID_KEY;
        sb.append((String) map.get(str2));
        sb.append(" page");
        Log.d(str, sb.toString());
        Activity currentActivity = VectorApp.getCurrentActivity();
        if (currentActivity != null) {
            Intent intent = new Intent(currentActivity, VectorMemberDetailsActivity.class);
            intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, (String) this.mParameters.get(str2));
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
            currentActivity.startActivity(intent);
            return;
        }
        Intent intent2 = new Intent(context, VectorHomeActivity.class);
        intent2.setFlags(872415232);
        intent2.putExtra(VectorHomeActivity.EXTRA_MEMBER_ID, (String) this.mParameters.get(str2));
        context.startActivity(intent2);
    }

    /* access modifiers changed from: private */
    public void manageRoomOnActivity(final Context context) {
        Activity currentActivity = VectorApp.getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    VectorUniversalLinkReceiver.this.manageRoom(context);
                }
            });
            return;
        }
        Intent intent = new Intent(context, VectorHomeActivity.class);
        intent.setFlags(872415232);
        intent.putExtra(VectorHomeActivity.EXTRA_WAITING_VIEW_STATUS, true);
        context.startActivity(intent);
        try {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    timer.cancel();
                    VectorUniversalLinkReceiver.this.manageRoomOnActivity(context);
                }
            }, 200);
        } catch (Throwable th) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## manageRoomOnActivity timer creation failed ");
            sb.append(th.getMessage());
            Log.e(str, sb.toString(), th);
            manageRoomOnActivity(context);
        }
    }

    private void manageGroupDetailsActivity(Context context) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## manageMemberDetailsActivity() : open the group");
        Map<String, String> map = this.mParameters;
        String str2 = PermalinkUtils.ULINK_GROUP_ID_KEY;
        sb.append((String) map.get(str2));
        Log.d(str, sb.toString());
        Activity currentActivity = VectorApp.getCurrentActivity();
        if (currentActivity != null) {
            Intent intent = new Intent(currentActivity, VectorGroupDetailsActivity.class);
            intent.putExtra(VectorGroupDetailsActivity.EXTRA_GROUP_ID, (String) this.mParameters.get(str2));
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
            currentActivity.startActivity(intent);
            return;
        }
        Intent intent2 = new Intent(context, VectorHomeActivity.class);
        intent2.setFlags(872415232);
        intent2.putExtra(VectorHomeActivity.EXTRA_GROUP_ID, (String) this.mParameters.get(str2));
        context.startActivity(intent2);
    }

    /* access modifiers changed from: private */
    public void manageRoom(Context context) {
        manageRoom(context, null);
    }

    /* access modifiers changed from: private */
    public void manageRoom(final Context context, String str) {
        final String str2 = (String) this.mParameters.get(PermalinkUtils.ULINK_ROOM_ID_OR_ALIAS_KEY);
        Log.d(LOG_TAG, "manageRoom roomIdOrAlias");
        if (!TextUtils.isEmpty(str2)) {
            if (str2.startsWith("!")) {
                RoomPreviewData roomPreviewData = new RoomPreviewData(this.mSession, str2, (String) this.mParameters.get(PermalinkUtils.ULINK_EVENT_ID_KEY), str, this.mParameters);
                Room room = this.mSession.getDataHandler().getRoom(str2, false);
                if (room == null || room.isInvited()) {
                    CommonActivityUtils.previewRoom(VectorApp.getCurrentActivity(), this.mSession, str2, roomPreviewData, null);
                } else {
                    openRoomActivity(context);
                }
            } else {
                Log.d(LOG_TAG, "manageRoom : it is a room Alias");
                Intent intent = new Intent(context, VectorHomeActivity.class);
                intent.setFlags(872415232);
                intent.putExtra(VectorHomeActivity.EXTRA_WAITING_VIEW_STATUS, true);
                context.startActivity(intent);
                this.mSession.getDataHandler().roomIdByAlias(str2, new ApiCallback<String>() {
                    public void onSuccess(String str) {
                        String access$200 = VectorUniversalLinkReceiver.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("manageRoom : retrieve the room ID ");
                        sb.append(str);
                        Log.d(access$200, sb.toString());
                        if (!TextUtils.isEmpty(str)) {
                            VectorUniversalLinkReceiver.this.mParameters.put(PermalinkUtils.ULINK_ROOM_ID_OR_ALIAS_KEY, str);
                            VectorUniversalLinkReceiver.this.manageRoom(context, str2);
                        }
                    }

                    private void onError(String str) {
                        Toast.makeText(context, str, 0).show();
                        VectorUniversalLinkReceiver.this.stopHomeActivitySpinner(context);
                    }

                    public void onNetworkError(Exception exc) {
                        onError(exc.getLocalizedMessage());
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        onError(matrixError.getLocalizedMessage());
                    }

                    public void onUnexpectedError(Exception exc) {
                        onError(exc.getLocalizedMessage());
                    }
                });
            }
        }
    }

    private void openRoomActivity(Context context) {
        HashMap hashMap = new HashMap();
        hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
        hashMap.put("EXTRA_ROOM_ID", this.mParameters.get(PermalinkUtils.ULINK_ROOM_ID_OR_ALIAS_KEY));
        Map<String, String> map = this.mParameters;
        String str = PermalinkUtils.ULINK_EVENT_ID_KEY;
        if (map.containsKey(str)) {
            hashMap.put(VectorRoomActivity.EXTRA_EVENT_ID, this.mParameters.get(str));
        }
        Intent intent = new Intent(context, VectorHomeActivity.class);
        intent.setFlags(872415232);
        intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, hashMap);
        context.startActivity(intent);
    }

    public static Map<String, String> parseUniversalLink(Uri uri) {
        return PermalinkUtils.parseUniversalLink(uri, sSupportedVectorHosts, sSupportedVectorLinkPaths);
    }

    /* access modifiers changed from: private */
    public void stopHomeActivitySpinner(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(VectorHomeActivity.BROADCAST_ACTION_STOP_WAITING_VIEW));
    }
}
