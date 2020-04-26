package im.vector.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.TchapLoginActivity;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.MyPresenceManager;
import im.vector.VectorApp;
import im.vector.adapters.VectorRoomsSelectionAdapter;
import im.vector.contacts.ContactsManager;
import im.vector.contacts.PIDsRetriever;
import im.vector.extensions.MatrixSdkExtensionsKt;
import im.vector.fragments.VectorUnknownDevicesFragment.IUnknownDevicesSendAnywayListener;
import im.vector.push.PushManager;
import im.vector.services.EventStreamService;
import im.vector.services.EventStreamService.StreamAction;
import im.vector.util.PreferencesManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.leolin.shortcutbadger.ShortcutBadger;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.db.MXMediaCache;

public class CommonActivityUtils {
    public static final boolean GROUP_IS_COLLAPSED = false;
    public static final boolean GROUP_IS_EXPANDED = true;
    public static final String KEY_GROUPS_EXPANDED_STATE = "KEY_GROUPS_EXPANDED_STATE";
    public static final String KEY_SEARCH_PATTERN = "KEY_SEARCH_PATTERN";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = CommonActivityUtils.class.getSimpleName();
    private static final String LOW_MEMORY_LOG_TAG = "Memory usage";
    private static final String RESTART_IN_PROGRESS_KEY = "RESTART_IN_PROGRESS_KEY";
    private static final int ROOM_SIZE_ONE_TO_ONE = 2;
    private static final String TAG_FRAGMENT_UNKNOWN_DEVICES_DIALOG_DIALOG = "ActionBarActivity.TAG_FRAGMENT_UNKNOWN_DEVICES_DIALOG_DIALOG";
    public static final boolean UTILS_DISPLAY_PROGRESS_BAR = true;
    public static final boolean UTILS_HIDE_PROGRESS_BAR = false;
    public static final float UTILS_POWER_LEVEL_ADMIN = 100.0f;
    public static final float UTILS_POWER_LEVEL_MODERATOR = 50.0f;
    private static int mBadgeValue = 0;

    public static void logout(Context context, List<MXSession> list, boolean z, ApiCallback<Void> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## logout() : from ");
        sb.append(context);
        sb.append(", ");
        sb.append(list.size());
        sb.append(" session(s), clearCredentials ");
        sb.append(z);
        Log.d(str, sb.toString());
        logout(context, list.iterator(), z, apiCallback);
    }

    /* access modifiers changed from: private */
    public static void logout(final Context context, final Iterator<MXSession> it, final boolean z, final ApiCallback<Void> apiCallback) {
        if (!it.hasNext()) {
            if (apiCallback != null) {
                apiCallback.onSuccess(null);
            }
            return;
        }
        MXSession mXSession = (MXSession) it.next();
        if (mXSession.isAlive()) {
            EventStreamService instance = EventStreamService.getInstance();
            if (instance != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(mXSession.getMyUserId());
                instance.stopAccounts(arrayList);
            }
            MyPresenceManager.getInstance(context, mXSession).advertiseOffline();
            MyPresenceManager.remove(mXSession);
            EventStreamService.removeNotification();
            Matrix.getInstance(context).getPushManager().unregister(mXSession, null);
            Matrix.getInstance(context).clearSession(context, mXSession, z, new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    CommonActivityUtils.logout(context, it, z, apiCallback);
                }
            });
        }
    }

    public static boolean shouldRestartApp(Context context) {
        EventStreamService instance = EventStreamService.getInstance();
        if (!Matrix.hasValidSessions()) {
            Log.e(LOG_TAG, "shouldRestartApp : the client has no valid session");
        }
        if (instance == null) {
            Log.e(LOG_TAG, "eventStreamService is null : restart the event stream");
            startEventStreamService(context);
        }
        return !Matrix.hasValidSessions();
    }

    public static boolean isGoingToSplash(Activity activity) {
        return isGoingToSplash(activity, null, null);
    }

    public static boolean isGoingToSplash(Activity activity, String str, String str2) {
        if (Matrix.hasValidSessions()) {
            for (MXSession mXSession : Matrix.getInstance(activity).getSessions()) {
                if (mXSession.isAlive() && !mXSession.getDataHandler().getStore().isReady()) {
                    Intent intent = new Intent(activity, SplashActivity.class);
                    if (!(str == null || str2 == null)) {
                        intent.putExtra("EXTRA_MATRIX_ID", str);
                        intent.putExtra("EXTRA_ROOM_ID", str2);
                    }
                    activity.startActivity(intent);
                    activity.finish();
                    return true;
                }
            }
        }
        return false;
    }

    public static void onApplicationStarted(Activity activity) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean(RESTART_IN_PROGRESS_KEY, false).apply();
    }

    public static void restartApp(Activity activity) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String str = RESTART_IN_PROGRESS_KEY;
        if (!defaultSharedPreferences.getBoolean(str, false)) {
            Toast.makeText(activity, "Restart the application (low memory)", 0).show();
            Log.e(LOG_TAG, "Kill the application");
            defaultSharedPreferences.edit().putBoolean(str, true).apply();
            ((AlarmManager) activity.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(1, System.currentTimeMillis() + 50, PendingIntent.getActivity(activity, 314159, new Intent(activity, TchapLoginActivity.class), 268435456));
            System.exit(0);
            return;
        }
        Log.e(LOG_TAG, "The application is restarting, please wait !!");
        activity.finish();
    }

    public static void logout(Activity activity) {
        logout(activity, true);
    }

    public static void logout(Activity activity, final boolean z) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## logout() : from ");
        sb.append(activity);
        sb.append(" goToLoginPage ");
        sb.append(z);
        Log.d(str, sb.toString());
        final Context applicationContext = activity == 0 ? VectorApp.getInstance().getApplicationContext() : activity;
        EventStreamService.removeNotification();
        stopEventStream(applicationContext);
        try {
            ShortcutBadger.setBadge(applicationContext, 0);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## logout(): Exception Msg=");
            sb2.append(e.getMessage());
            Log.d(str2, sb2.toString(), e);
        }
        for (MXSession mXSession : Matrix.getMXSessions(applicationContext)) {
            MyPresenceManager.getInstance(applicationContext, mXSession).advertiseOffline();
            MyPresenceManager.remove(mXSession);
        }
        PreferencesManager.clearPreferences(applicationContext);
        Matrix.getInstance(applicationContext).getPushManager().resetFCMRegistration();
        if (z) {
            Matrix.getInstance(applicationContext).getPushManager().clearPreferences();
            if (activity != 0) {
                activity.startActivity(new Intent(activity, LoggingOutActivity.class));
                activity.finish();
            } else {
                Intent intent = new Intent(applicationContext, LoggingOutActivity.class);
                intent.setFlags(268468224);
                applicationContext.startActivity(intent);
            }
        }
        Matrix.getInstance(applicationContext).clearSessions(applicationContext, true, new SimpleApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                Log.d(CommonActivityUtils.LOG_TAG, "## logout() : clearSessions succeeded");
                Matrix.getInstance(applicationContext).getLoginStorage().clear();
                Matrix.getInstance(applicationContext).clearTmpStoresList();
                PIDsRetriever.getInstance().reset();
                ContactsManager.getInstance().reset();
                MXMediaCache.clearThumbnailsCache(applicationContext);
                if (z) {
                    Activity currentActivity = VectorApp.getCurrentActivity();
                    if (currentActivity != null) {
                        currentActivity.startActivity(new Intent(currentActivity, TchapLoginActivity.class));
                        currentActivity.finish();
                        return;
                    }
                    Intent intent = new Intent(applicationContext, TchapLoginActivity.class);
                    intent.setFlags(268468224);
                    applicationContext.startActivity(intent);
                }
            }
        });
        Log.d(LOG_TAG, "## logout() : clearSessions in progress");
    }

    public static void deactivateAccount(final Context context, final MXSession mXSession, String str, boolean z, final ApiCallback<Void> apiCallback) {
        Matrix.getInstance(context).deactivateSession(context, mXSession, str, z, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                EventStreamService.removeNotification();
                CommonActivityUtils.stopEventStream(context);
                try {
                    ShortcutBadger.setBadge(context, 0);
                } catch (Exception e) {
                    String access$100 = CommonActivityUtils.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## logout(): Exception Msg=");
                    sb.append(e.getMessage());
                    Log.d(access$100, sb.toString(), e);
                }
                MyPresenceManager.getInstance(context, mXSession).advertiseOffline();
                MyPresenceManager.remove(mXSession);
                PreferencesManager.clearPreferences(context);
                Matrix.getInstance(context).getPushManager().resetFCMRegistration();
                Matrix.getInstance(context).getPushManager().clearPreferences();
                Matrix.getInstance(context).getLoginStorage().clear();
                Matrix.getInstance(context).clearTmpStoresList();
                PIDsRetriever.getInstance().reset();
                ContactsManager.getInstance().reset();
                MXMediaCache.clearThumbnailsCache(context);
                apiCallback.onSuccess(voidR);
            }
        });
    }

    public static void startLoginActivityNewTask(Activity activity) {
        Intent intent = new Intent(activity, TchapLoginActivity.class);
        intent.setFlags(268468224);
        activity.startActivity(intent);
    }

    private static boolean isUserLogout(Context context) {
        return context == null || Matrix.getInstance(context.getApplicationContext()).getDefaultSession() == null;
    }

    private static void sendEventStreamAction(Context context, StreamAction streamAction) {
        Context applicationContext = context.getApplicationContext();
        if (!isUserLogout(applicationContext)) {
            Intent intent = new Intent(applicationContext, EventStreamService.class);
            if (streamAction != StreamAction.CATCHUP || !EventStreamService.isStopped()) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sendEventStreamAction ");
                sb.append(streamAction);
                Log.d(str, sb.toString());
                intent.putExtra(EventStreamService.EXTRA_STREAM_ACTION, streamAction.ordinal());
            } else {
                Log.d(LOG_TAG, "sendEventStreamAction : auto restart");
                String str2 = EventStreamService.EXTRA_AUTO_RESTART_ACTION;
                intent.putExtra(str2, str2);
            }
            applicationContext.startService(intent);
            return;
        }
        String str3 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## sendEventStreamAction(): \"");
        sb2.append(streamAction);
        sb2.append("\" action not sent - user logged out");
        Log.d(str3, sb2.toString());
    }

    /* access modifiers changed from: private */
    public static void stopEventStream(Context context) {
        Log.d(LOG_TAG, "stopEventStream");
        sendEventStreamAction(context, StreamAction.STOP);
    }

    public static void pauseEventStream(Context context) {
        Log.d(LOG_TAG, "pauseEventStream");
        sendEventStreamAction(context, StreamAction.PAUSE);
    }

    public static void resumeEventStream(Context context) {
        Log.d(LOG_TAG, "resumeEventStream");
        sendEventStreamAction(context, StreamAction.RESUME);
    }

    public static void catchupEventStream(Context context) {
        if (VectorApp.isAppInBackground()) {
            Log.d(LOG_TAG, "catchupEventStream");
            sendEventStreamAction(context, StreamAction.CATCHUP);
        }
    }

    public static void onPushUpdate(Context context) {
        Log.d(LOG_TAG, "onPushUpdate");
        sendEventStreamAction(context, StreamAction.PUSH_STATUS_UPDATE);
    }

    public static void startEventStreamService(Context context) {
        if (EventStreamService.isStopped()) {
            ArrayList arrayList = new ArrayList();
            List<MXSession> sessions = Matrix.getInstance(context.getApplicationContext()).getSessions();
            if (sessions != null && sessions.size() > 0) {
                PushManager pushManager = Matrix.getInstance(context).getPushManager();
                Log.e(LOG_TAG, "## startEventStreamService() : restart EventStreamService");
                for (MXSession mXSession : sessions) {
                    if (!(mXSession.getDataHandler() == null || mXSession.getDataHandler().getStore() == null)) {
                        if (!mXSession.getDataHandler().getStore().isReady()) {
                            String str = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## startEventStreamService() : the session ");
                            sb.append(mXSession.getMyUserId());
                            sb.append(" is not opened");
                            Log.e(str, sb.toString());
                            mXSession.getDataHandler().getStore().open();
                        } else {
                            String str2 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## startEventStreamService() : check if the crypto of the session ");
                            sb2.append(mXSession.getMyUserId());
                            Log.e(str2, sb2.toString());
                            mXSession.checkCrypto();
                        }
                        mXSession.setSyncDelay(pushManager.isBackgroundSyncAllowed() ? pushManager.getBackgroundSyncDelay() : 0);
                        mXSession.setSyncTimeout(pushManager.getBackgroundSyncTimeOut());
                        arrayList.add(mXSession.getCredentials().userId);
                    }
                }
                if (arrayList.size() > 0) {
                    Intent intent = new Intent(context, EventStreamService.class);
                    intent.putExtra(EventStreamService.EXTRA_MATRIX_IDS, (String[]) arrayList.toArray(new String[arrayList.size()]));
                    intent.putExtra(EventStreamService.EXTRA_STREAM_ACTION, StreamAction.START.ordinal());
                    ContextCompat.startForegroundService(context, intent);
                }
            }
            if (EventStreamService.getInstance() != null) {
                EventStreamService.getInstance().refreshForegroundNotification();
            }
        }
    }

    public static void previewRoom(Activity activity, RoomPreviewData roomPreviewData) {
        if (activity != null && roomPreviewData != null) {
            VectorRoomActivity.sRoomPreviewData = roomPreviewData;
            Intent intent = new Intent(activity, VectorRoomActivity.class);
            intent.putExtra("EXTRA_ROOM_ID", roomPreviewData.getRoomId());
            intent.putExtra(VectorRoomActivity.EXTRA_ROOM_PREVIEW_ID, roomPreviewData.getRoomId());
            intent.putExtra(VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER, true);
            activity.startActivity(intent);
        }
    }

    public static Intent buildIntentPreviewRoom(String str, String str2, Context context, Class<?> cls) {
        String str3 = null;
        if (context == null || str2 == null || str == null) {
            return null;
        }
        MXSession session = Matrix.getInstance(context).getSession(str);
        if (session == null) {
            session = Matrix.getInstance(context).getDefaultSession();
        }
        if (session == null || !session.isAlive()) {
            return null;
        }
        Room room = session.getDataHandler().getRoom(str2);
        if (!(room == null || room.getState() == null)) {
            str3 = room.getState().getCanonicalAlias();
        }
        Intent intent = new Intent(context, cls);
        intent.putExtra("EXTRA_ROOM_ID", str2);
        intent.putExtra(VectorRoomActivity.EXTRA_ROOM_PREVIEW_ID, str2);
        intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", str);
        intent.putExtra(VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER, true);
        intent.putExtra(VectorRoomActivity.EXTRA_ROOM_PREVIEW_ROOM_ALIAS, str3);
        return intent;
    }

    public static void previewRoom(Activity activity, MXSession mXSession, String str, String str2, ApiCallback<Void> apiCallback) {
        RoomPreviewData roomPreviewData = new RoomPreviewData(mXSession, str, null, str2, null);
        previewRoom(activity, mXSession, str, roomPreviewData, apiCallback);
    }

    public static void previewRoom(final Activity activity, MXSession mXSession, String str, final RoomPreviewData roomPreviewData, final ApiCallback<Void> apiCallback) {
        Room room = mXSession.getDataHandler().getRoom(str, false);
        if (room != null && room.isInvited()) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("previewRoom : the user is invited -> display the preview ");
            sb.append(VectorApp.getCurrentActivity());
            Log.d(str2, sb.toString());
            previewRoom(activity, roomPreviewData);
            if (apiCallback != null) {
                apiCallback.onSuccess(null);
            }
        } else if (room == null || !room.isJoined()) {
            Log.d(LOG_TAG, "previewRoom : display the preview");
            roomPreviewData.fetchPreviewData(new ApiCallback<Void>() {
                private void onDone() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(null);
                    }
                    CommonActivityUtils.previewRoom(activity, roomPreviewData);
                }

                public void onSuccess(Void voidR) {
                    onDone();
                }

                public void onNetworkError(Exception exc) {
                    onDone();
                }

                public void onMatrixError(MatrixError matrixError) {
                    onDone();
                }

                public void onUnexpectedError(Exception exc) {
                    onDone();
                }
            });
        } else {
            Log.d(LOG_TAG, "previewRoom : the user joined the room -> open the room");
            HashMap hashMap = new HashMap();
            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", mXSession.getMyUserId());
            hashMap.put("EXTRA_ROOM_ID", str);
            goToRoomPage(activity, mXSession, hashMap);
            if (apiCallback != null) {
                apiCallback.onSuccess(null);
            }
        }
    }

    public static void goToRoomPage(final Activity activity, final MXSession mXSession, final Map<String, Object> map) {
        if (mXSession == null) {
            mXSession = Matrix.getMXSession(activity, (String) map.get("MXCActionBarActivity.EXTRA_MATRIX_ID"));
        }
        if (mXSession != null && mXSession.isAlive()) {
            Room room = mXSession.getDataHandler().getRoom((String) map.get("EXTRA_ROOM_ID"));
            if (room == null || !room.isLeaving()) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (!(activity instanceof VectorHomeActivity)) {
                            Log.d(CommonActivityUtils.LOG_TAG, "## goToRoomPage(): start VectorHomeActivity..");
                            Intent intent = new Intent(activity, VectorHomeActivity.class);
                            intent.setFlags(603979776);
                            intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, (Serializable) map);
                            activity.startActivity(intent);
                            return;
                        }
                        Log.d(CommonActivityUtils.LOG_TAG, "## goToRoomPage(): already in VectorHomeActivity..");
                        Intent intent2 = new Intent(activity, VectorRoomActivity.class);
                        for (String str : map.keySet()) {
                            Object obj = map.get(str);
                            if (obj instanceof String) {
                                intent2.putExtra(str, (String) obj);
                            } else if (obj instanceof Boolean) {
                                intent2.putExtra(str, (Boolean) obj);
                            } else if (obj instanceof Parcelable) {
                                intent2.putExtra(str, (Parcelable) obj);
                            } else if (obj instanceof Serializable) {
                                intent2.putExtra(str, (Serializable) obj);
                            }
                        }
                        Map map = map;
                        String str2 = VectorRoomActivity.EXTRA_DEFAULT_NAME;
                        if (map.get(str2) == null) {
                            Room room = mXSession.getDataHandler().getRoom((String) map.get("EXTRA_ROOM_ID"));
                            if (room != null && room.isInvited()) {
                                String roomDisplayName = DinsicUtils.getRoomDisplayName(activity, room);
                                if (roomDisplayName != null) {
                                    intent2.putExtra(str2, roomDisplayName);
                                }
                            }
                        }
                        activity.startActivity(intent2);
                    }
                });
            }
        }
    }

    public static void setToggleDirectMessageRoom(MXSession mXSession, String str, String str2, final ApiCallback<Void> apiCallback) {
        if (mXSession == null || TextUtils.isEmpty(str)) {
            String str3 = "## setToggleDirectMessageRoom(): failure - invalid input parameters";
            Log.e(LOG_TAG, str3);
            apiCallback.onUnexpectedError(new Exception(str3));
            return;
        }
        mXSession.toggleDirectChatRoom(str, str2, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }
        });
    }

    public static void sendFilesTo(Activity activity, Intent intent) {
        if (Matrix.getMXSessions(activity).size() == 1) {
            sendFilesTo(activity, intent, Matrix.getMXSession(activity, null));
        } else {
            boolean z = activity instanceof FragmentActivity;
        }
    }

    private static void sendFilesTo(final Activity activity, final Intent intent, final MXSession mXSession) {
        if (mXSession != null && mXSession.isAlive() && !activity.isFinishing()) {
            final ArrayList arrayList = new ArrayList(mXSession.getDataHandler().getStore().getSummaries());
            int i = 0;
            while (i < arrayList.size()) {
                Room room = mXSession.getDataHandler().getRoom(((RoomSummary) arrayList.get(i)).getRoomId());
                if (room == null || room.isInvited() || room.isConferenceUserRoom()) {
                    arrayList.remove(i);
                    i--;
                }
                i++;
            }
            Collections.sort(arrayList, new Comparator<RoomSummary>() {
                public int compare(RoomSummary roomSummary, RoomSummary roomSummary2) {
                    if (roomSummary == null || roomSummary.getLatestReceivedEvent() == null) {
                        return 1;
                    }
                    if (roomSummary2 == null || roomSummary2.getLatestReceivedEvent() == null || roomSummary.getLatestReceivedEvent().getOriginServerTs() > roomSummary2.getLatestReceivedEvent().getOriginServerTs()) {
                        return -1;
                    }
                    if (roomSummary.getLatestReceivedEvent().getOriginServerTs() < roomSummary2.getLatestReceivedEvent().getOriginServerTs()) {
                        return 1;
                    }
                    return 0;
                }
            });
            VectorRoomsSelectionAdapter vectorRoomsSelectionAdapter = new VectorRoomsSelectionAdapter(activity, R.layout.adapter_item_vector_recent_room, mXSession);
            vectorRoomsSelectionAdapter.addAll(arrayList);
            new Builder(activity).setTitle((int) R.string.send_files_in).setNegativeButton((int) R.string.cancel, (OnClickListener) null).setAdapter(vectorRoomsSelectionAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, final int i) {
                    dialogInterface.dismiss();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            RoomSummary roomSummary = (RoomSummary) arrayList.get(i);
                            HashMap hashMap = new HashMap();
                            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", mXSession.getMyUserId());
                            hashMap.put("EXTRA_ROOM_ID", roomSummary.getRoomId());
                            hashMap.put(VectorRoomActivity.EXTRA_ROOM_INTENT, intent);
                            CommonActivityUtils.goToRoomPage(activity, mXSession, hashMap);
                        }
                    });
                }
            }).show();
        }
    }

    private static void saveFileInto(final File file, final String str, final String str2, final ApiCallback<String> apiCallback) {
        if (file == null || str == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onNetworkError(new Exception("Null parameters"));
                    }
                }
            });
            return;
        }
        AnonymousClass10 r0 = new AsyncTask<Void, Void, Pair<String, Exception>>() {
            /* access modifiers changed from: protected */
            /* JADX WARNING: Removed duplicated region for block: B:47:0x00f4 A[SYNTHETIC, Splitter:B:47:0x00f4] */
            /* JADX WARNING: Removed duplicated region for block: B:52:0x00fc A[Catch:{ Exception -> 0x00f8 }] */
            /* JADX WARNING: Removed duplicated region for block: B:57:0x0125 A[SYNTHETIC, Splitter:B:57:0x0125] */
            /* JADX WARNING: Removed duplicated region for block: B:62:0x012d A[Catch:{ Exception -> 0x0129 }] */
            public Pair<String, Exception> doInBackground(Void... voidArr) {
                FileOutputStream fileOutputStream;
                FileInputStream fileInputStream;
                String str = "## saveFileInto(): Exception Msg=";
                String str2 = str2;
                String str3 = "";
                String str4 = ".";
                if (str2 == null) {
                    int lastIndexOf = file.getName().lastIndexOf(str4);
                    String substring = lastIndexOf > 0 ? file.getName().substring(lastIndexOf) : str3;
                    StringBuilder sb = new StringBuilder();
                    sb.append("vector_");
                    sb.append(System.currentTimeMillis());
                    sb.append(substring);
                    str2 = sb.toString();
                }
                File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(str);
                if (externalStoragePublicDirectory != null) {
                    externalStoragePublicDirectory.mkdirs();
                }
                File file = new File(externalStoragePublicDirectory, str2);
                if (file.exists()) {
                    int lastIndexOf2 = str2.lastIndexOf(str4);
                    if (lastIndexOf2 > 0) {
                        String substring2 = str2.substring(0, lastIndexOf2);
                        str3 = str2.substring(lastIndexOf2);
                        str2 = substring2;
                    }
                    int i = 1;
                    while (file.exists()) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append("(");
                        sb2.append(i);
                        sb2.append(")");
                        sb2.append(str3);
                        file = new File(externalStoragePublicDirectory, sb2.toString());
                        i++;
                    }
                }
                try {
                    file.createNewFile();
                    fileInputStream = new FileInputStream(file);
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            byte[] bArr = new byte[10240];
                            while (true) {
                                int read = fileInputStream.read(bArr);
                                if (read != -1) {
                                    fileOutputStream.write(bArr, 0, read);
                                } else {
                                    Pair<String, Exception> pair = new Pair<>(file.getAbsolutePath(), null);
                                    try {
                                        fileInputStream.close();
                                        fileOutputStream.close();
                                        return pair;
                                    } catch (Exception e) {
                                        String access$100 = CommonActivityUtils.LOG_TAG;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append(str);
                                        sb3.append(e.getMessage());
                                        Log.e(access$100, sb3.toString(), e);
                                        return new Pair<>(null, e);
                                    }
                                }
                            }
                        } catch (Exception e2) {
                            e = e2;
                            try {
                                Pair pair2 = new Pair(null, e);
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Exception e3) {
                                        String access$1002 = CommonActivityUtils.LOG_TAG;
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str);
                                        sb4.append(e3.getMessage());
                                        Log.e(access$1002, sb4.toString(), e3);
                                        return new Pair<>(null, e3);
                                    }
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                return pair2;
                            } catch (Throwable th) {
                                th = th;
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Exception e4) {
                                        String access$1003 = CommonActivityUtils.LOG_TAG;
                                        StringBuilder sb5 = new StringBuilder();
                                        sb5.append(str);
                                        sb5.append(e4.getMessage());
                                        Log.e(access$1003, sb5.toString(), e4);
                                        new Pair(null, e4);
                                        throw th;
                                    }
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                throw th;
                            }
                        }
                    } catch (Exception e5) {
                        e = e5;
                        fileOutputStream = null;
                        Pair pair22 = new Pair(null, e);
                        if (fileInputStream != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        return pair22;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = null;
                        if (fileInputStream != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        throw th;
                    }
                } catch (Exception e6) {
                    e = e6;
                    fileInputStream = null;
                    fileOutputStream = null;
                    Pair pair222 = new Pair(null, e);
                    if (fileInputStream != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    return pair222;
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = null;
                    fileOutputStream = null;
                    if (fileInputStream != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Pair<String, Exception> pair) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback == null) {
                    return;
                }
                if (pair == null) {
                    apiCallback.onUnexpectedError(new Exception("Null parameters"));
                } else if (pair.first != null) {
                    apiCallback.onSuccess(pair.first);
                } else {
                    apiCallback.onUnexpectedError((Exception) pair.second);
                }
            }
        };
        try {
            r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## saveFileInto() failed ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
            r0.cancel(true);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(e);
                    }
                }
            });
        }
    }

    public static void saveMediaIntoDownloads(final Context context, File file, String str, final String str2, final ApiCallback<String> apiCallback) {
        saveFileInto(file, Environment.DIRECTORY_DOWNLOADS, str, new ApiCallback<String>() {
            public void onSuccess(String str) {
                if (str != null) {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService("download");
                    try {
                        File file = new File(str);
                        downloadManager.addCompletedDownload(file.getName(), file.getName(), true, str2, file.getAbsolutePath(), file.length(), true);
                    } catch (Exception e) {
                        String access$100 = CommonActivityUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## saveMediaIntoDownloads(): Exception Msg=");
                        sb.append(e.getMessage());
                        Log.e(access$100, sb.toString(), e);
                    }
                }
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(str);
                }
            }

            public void onNetworkError(Exception exc) {
                Toast.makeText(context, exc.getLocalizedMessage(), 1).show();
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                Toast.makeText(context, matrixError.getLocalizedMessage(), 1).show();
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            }

            public void onUnexpectedError(Exception exc) {
                Toast.makeText(context, exc.getLocalizedMessage(), 1).show();
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
            }
        });
    }

    public static void updateBadgeCount(Context context, int i) {
        try {
            mBadgeValue = i;
            ShortcutBadger.setBadge(context, i);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## updateBadgeCount(): Exception Msg=");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public static void specificUpdateBadgeUnreadCount(MXSession mXSession, Context context) {
        if (context == null || mXSession == null) {
            Log.w(LOG_TAG, "## specificUpdateBadgeUnreadCount(): invalid input null values");
            return;
        }
        MXDataHandler dataHandler = mXSession.getDataHandler();
        if (dataHandler == null) {
            Log.w(LOG_TAG, "## specificUpdateBadgeUnreadCount(): invalid DataHandler instance");
        } else if (mXSession.isAlive()) {
            PushManager pushManager = Matrix.getInstance(context).getPushManager();
            boolean z = true;
            boolean z2 = !Matrix.getInstance(context).isConnected();
            if (pushManager == null || (pushManager.useFcm() && pushManager.hasRegistrationToken())) {
                z = false;
            }
            if (z2 || z) {
                updateBadgeCount(context, dataHandler);
            }
        }
    }

    private static void updateBadgeCount(Context context, MXDataHandler mXDataHandler) {
        if (context == null || mXDataHandler == null) {
            Log.w(LOG_TAG, "## updateBadgeCount(): invalid input null values");
        } else if (mXDataHandler.getStore() == null) {
            Log.w(LOG_TAG, "## updateBadgeCount(): invalid store instance");
        } else {
            ArrayList<Room> arrayList = new ArrayList<>(mXDataHandler.getStore().getRooms());
            int i = 0;
            for (Room notificationCount : arrayList) {
                if (notificationCount.getNotificationCount() > 0) {
                    i++;
                }
            }
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## updateBadgeCount(): badge update count=");
            sb.append(i);
            Log.d(str, sb.toString());
            updateBadgeCount(context, i);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0109  */
    public static boolean displayMemoryInformation(Activity activity, String str) {
        long j;
        long j2;
        long j3 = 0;
        try {
            Runtime runtime = Runtime.getRuntime();
            j2 = runtime.freeMemory();
            try {
                j3 = runtime.totalMemory();
                j = j3 - j2;
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
                j = -1;
                String str2 = "---------------------------------------------------";
                String str3 = LOW_MEMORY_LOG_TAG;
                Log.e(str3, str2);
                StringBuilder sb = new StringBuilder();
                sb.append("----------- ");
                sb.append(str);
                sb.append(" -----------------");
                Log.e(str3, sb.toString());
                Log.e(str3, str2);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("usedSize   ");
                sb2.append(j / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
                String str4 = " MB";
                sb2.append(str4);
                Log.e(str3, sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("freeSize   ");
                sb3.append(j2 / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
                sb3.append(str4);
                Log.e(str3, sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append("totalSize  ");
                sb4.append(j3 / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
                sb4.append(str4);
                Log.e(str3, sb4.toString());
                Log.e(str3, str2);
                if (activity == null) {
                }
            }
        } catch (Exception e2) {
            e = e2;
            j2 = 0;
            e.printStackTrace();
            j = -1;
            String str22 = "---------------------------------------------------";
            String str32 = LOW_MEMORY_LOG_TAG;
            Log.e(str32, str22);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("----------- ");
            sb5.append(str);
            sb5.append(" -----------------");
            Log.e(str32, sb5.toString());
            Log.e(str32, str22);
            StringBuilder sb22 = new StringBuilder();
            sb22.append("usedSize   ");
            sb22.append(j / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
            String str42 = " MB";
            sb22.append(str42);
            Log.e(str32, sb22.toString());
            StringBuilder sb32 = new StringBuilder();
            sb32.append("freeSize   ");
            sb32.append(j2 / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
            sb32.append(str42);
            Log.e(str32, sb32.toString());
            StringBuilder sb42 = new StringBuilder();
            sb42.append("totalSize  ");
            sb42.append(j3 / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
            sb42.append(str42);
            Log.e(str32, sb42.toString());
            Log.e(str32, str22);
            if (activity == null) {
            }
        }
        String str222 = "---------------------------------------------------";
        String str322 = LOW_MEMORY_LOG_TAG;
        Log.e(str322, str222);
        StringBuilder sb52 = new StringBuilder();
        sb52.append("----------- ");
        sb52.append(str);
        sb52.append(" -----------------");
        Log.e(str322, sb52.toString());
        Log.e(str322, str222);
        StringBuilder sb222 = new StringBuilder();
        sb222.append("usedSize   ");
        sb222.append(j / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
        String str422 = " MB";
        sb222.append(str422);
        Log.e(str322, sb222.toString());
        StringBuilder sb322 = new StringBuilder();
        sb322.append("freeSize   ");
        sb322.append(j2 / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
        sb322.append(str422);
        Log.e(str322, sb322.toString());
        StringBuilder sb422 = new StringBuilder();
        sb422.append("totalSize  ");
        sb422.append(j3 / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
        sb422.append(str422);
        Log.e(str322, sb422.toString());
        Log.e(str322, str222);
        if (activity == null) {
            return false;
        }
        MemoryInfo memoryInfo = new MemoryInfo();
        ((ActivityManager) activity.getSystemService("activity")).getMemoryInfo(memoryInfo);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("availMem   ");
        sb6.append(memoryInfo.availMem / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
        sb6.append(str422);
        Log.e(str322, sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append("totalMem   ");
        sb7.append(memoryInfo.totalMem / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
        sb7.append(str422);
        Log.e(str322, sb7.toString());
        StringBuilder sb8 = new StringBuilder();
        sb8.append("threshold  ");
        sb8.append(memoryInfo.threshold / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED);
        sb8.append(str422);
        Log.e(str322, sb8.toString());
        StringBuilder sb9 = new StringBuilder();
        sb9.append("lowMemory  ");
        sb9.append(memoryInfo.lowMemory);
        Log.e(str322, sb9.toString());
        Log.e(str322, str222);
        return memoryInfo.lowMemory;
    }

    public static void onLowMemory(Activity activity) {
        boolean isAppInBackground = VectorApp.isAppInBackground();
        String str = LOW_MEMORY_LOG_TAG;
        if (!isAppInBackground) {
            String simpleName = activity != null ? activity.getClass().getSimpleName() : "NotAvailable";
            StringBuilder sb = new StringBuilder();
            sb.append("Active application : onLowMemory from ");
            sb.append(simpleName);
            Log.e(str, sb.toString());
            if (!displayMemoryInformation(activity, "onLowMemory test")) {
                Log.e(str, "Wait to be concerned");
            } else if (shouldRestartApp(activity)) {
                Log.e(str, "restart");
                restartApp(activity);
            } else {
                Log.e(str, "clear the application cache");
                Matrix.getInstance(activity).reloadSessions(activity);
            }
        } else {
            Log.e(str, "background application : onLowMemory ");
        }
        displayMemoryInformation(activity, "onLowMemory global");
    }

    public static void onTrimMemory(Activity activity, int i) {
        String simpleName = activity != null ? activity.getClass().getSimpleName() : "NotAvailable";
        StringBuilder sb = new StringBuilder();
        sb.append("Active application : onTrimMemory from ");
        sb.append(simpleName);
        sb.append(" level=");
        sb.append(i);
        Log.e(LOW_MEMORY_LOG_TAG, sb.toString());
        displayMemoryInformation(activity, "onTrimMemory");
    }

    public static <T> void displayDeviceVerificationDialog(final MXDeviceInfo mXDeviceInfo, final String str, final MXSession mXSession, Activity activity, final ApiCallback<Void> apiCallback) {
        if (mXDeviceInfo == null || str == null || mXSession == null) {
            Log.e(LOG_TAG, "## displayDeviceVerificationDialog(): invalid input parameters");
            return;
        }
        Builder builder = new Builder(activity);
        View inflate = activity.getLayoutInflater().inflate(R.layout.dialog_device_verify, null);
        ((TextView) inflate.findViewById(R.id.encrypted_device_info_device_name)).setText(mXDeviceInfo.displayName());
        ((TextView) inflate.findViewById(R.id.encrypted_device_info_device_id)).setText(mXDeviceInfo.deviceId);
        ((TextView) inflate.findViewById(R.id.encrypted_device_info_device_key)).setText(MatrixSdkExtensionsKt.getFingerprintHumanReadable(mXDeviceInfo));
        builder.setTitle((int) R.string.encryption_information_verify_device).setView(inflate).setPositiveButton((int) R.string.encryption_information_verify, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                mXSession.getCrypto().setDeviceVerification(1, mXDeviceInfo.deviceId, str, apiCallback);
            }
        }).setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }
        }).show();
    }

    public static void exportKeys(final MXSession mXSession, String str, final ApiCallback<String> apiCallback) {
        final VectorApp instance = VectorApp.getInstance();
        if (mXSession.getCrypto() == null) {
            if (apiCallback != null) {
                apiCallback.onMatrixError(new MatrixError("EMPTY", "No crypto"));
            }
            return;
        }
        mXSession.getCrypto().exportRoomKeys(str, new SimpleApiCallback<byte[]>(apiCallback) {
            public void onSuccess(byte[] bArr) {
                String str = "text/plain";
                try {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                    MXMediaCache mediaCache = mXSession.getMediaCache();
                    StringBuilder sb = new StringBuilder();
                    sb.append("tchap-");
                    sb.append(System.currentTimeMillis());
                    sb.append(".txt");
                    String saveMedia = mediaCache.saveMedia(byteArrayInputStream, sb.toString(), str);
                    byteArrayInputStream.close();
                    CommonActivityUtils.saveMediaIntoDownloads(instance, new File(Uri.parse(saveMedia).getPath()), "tchap-keys.txt", str, new SimpleApiCallback<String>(apiCallback) {
                        public void onSuccess(String str) {
                            if (apiCallback != null) {
                                apiCallback.onSuccess(str);
                            }
                        }
                    });
                } catch (Exception e) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(e);
                    }
                }
            }
        });
    }

    public static void displayUnknownDevicesDialog(MXSession mXSession, FragmentActivity fragmentActivity, MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap, boolean z, final IUnknownDevicesSendAnywayListener iUnknownDevicesSendAnywayListener) {
        if (!fragmentActivity.isFinishing() && mXUsersDevicesMap != null && mXUsersDevicesMap.getMap().size() != 0) {
            ArrayList arrayList = new ArrayList();
            for (String str : mXUsersDevicesMap.getUserIds()) {
                for (String object : mXUsersDevicesMap.getUserDeviceIds(str)) {
                    arrayList.add(mXUsersDevicesMap.getObject(object, str));
                }
            }
            if (arrayList.size() > 0) {
                mXSession.getCrypto().setDevicesKnown(arrayList, new ApiCallback<Void>() {
                    private void onDone() {
                        IUnknownDevicesSendAnywayListener iUnknownDevicesSendAnywayListener = iUnknownDevicesSendAnywayListener;
                        if (iUnknownDevicesSendAnywayListener != null) {
                            iUnknownDevicesSendAnywayListener.onSendAnyway();
                        }
                    }

                    public void onSuccess(Void voidR) {
                        onDone();
                    }

                    public void onNetworkError(Exception exc) {
                        onDone();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        onDone();
                    }

                    public void onUnexpectedError(Exception exc) {
                        onDone();
                    }
                });
            }
        }
    }
}
