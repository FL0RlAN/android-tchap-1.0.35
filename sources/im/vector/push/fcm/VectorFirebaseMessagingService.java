package im.vector.push.fcm;

import android.os.Handler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonParser;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.activity.CommonActivityUtils;
import im.vector.push.PushManager;
import im.vector.services.EventStreamService;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;

public class VectorFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = VectorFirebaseMessagingService.class.getSimpleName();
    private Boolean mCheckLaunched = Boolean.valueOf(false);
    private Handler mUIHandler = null;

    private Event parseEvent(Map<String, String> map) {
        String str = BingRule.KIND_CONTENT;
        if (map != null) {
            String str2 = "room_id";
            if (map.containsKey(str2)) {
                String str3 = "event_id";
                if (map.containsKey(str3)) {
                    try {
                        Event event = new Event();
                        event.eventId = (String) map.get(str3);
                        event.sender = (String) map.get(BingRule.KIND_SENDER);
                        event.roomId = (String) map.get(str2);
                        event.setType((String) map.get(PasswordLoginParams.IDENTIFIER_KEY_TYPE));
                        if (map.containsKey(str)) {
                            event.updateContent(new JsonParser().parse((String) map.get(str)).getAsJsonObject());
                        }
                        return event;
                    } catch (Exception e) {
                        String str4 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("buildEvent fails ");
                        sb.append(e.getLocalizedMessage());
                        Log.e(str4, sb.toString(), e);
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x007c A[Catch:{ Exception -> 0x019f }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0084 A[Catch:{ Exception -> 0x019f }] */
    public void onMessageReceivedInternal(Map<String, String> map) {
        String str;
        PushManager pushManager;
        String str2 = "event_id";
        String str3 = "room_id";
        String str4 = "unread";
        int i = 0;
        String str5 = null;
        if (map != null) {
            try {
                if (map.containsKey(str4)) {
                    if (map.containsKey(str4)) {
                        i = Integer.parseInt((String) map.get(str4));
                    }
                    str = map.containsKey(str3) ? (String) map.get(str3) : null;
                    if (map.containsKey(str2)) {
                        str5 = (String) map.get(str2);
                    }
                    String str6 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onMessageReceivedInternal() : roomId ");
                    sb.append(str);
                    sb.append(" eventId ");
                    sb.append(str5);
                    sb.append(" unread ");
                    sb.append(i);
                    Log.d(str6, sb.toString());
                    CommonActivityUtils.updateBadgeCount(getApplicationContext(), i);
                    pushManager = Matrix.getInstance(getApplicationContext()).getPushManager();
                    if (pushManager.areDeviceNotificationsAllowed()) {
                        Log.d(LOG_TAG, "## onMessageReceivedInternal() : the notifications are disabled");
                        return;
                    } else if (pushManager.isBackgroundSyncAllowed() || !VectorApp.isAppInBackground()) {
                        if (!this.mCheckLaunched.booleanValue() && Matrix.getInstance(getApplicationContext()).getDefaultSession() != null) {
                            CommonActivityUtils.startEventStreamService(this);
                            this.mCheckLaunched = Boolean.valueOf(true);
                        }
                        if (!(str5 == null || str == null)) {
                            try {
                                List<MXSession> sessions = Matrix.getInstance(getApplicationContext()).getSessions();
                                if (sessions != null && !sessions.isEmpty()) {
                                    for (MXSession mXSession : sessions) {
                                        if (mXSession.getDataHandler().getStore().isReady() && mXSession.getDataHandler().getStore().getEvent(str5, str) != null) {
                                            String str7 = LOG_TAG;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("## onMessageReceivedInternal() : ignore the event ");
                                            sb2.append(str5);
                                            sb2.append(" in room ");
                                            sb2.append(str);
                                            sb2.append(" because it is already known");
                                            Log.e(str7, sb2.toString());
                                            return;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                String str8 = LOG_TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("## onMessageReceivedInternal() : failed to check if the event was already defined ");
                                sb3.append(e.getMessage());
                                Log.e(str8, sb3.toString(), e);
                            }
                        }
                        CommonActivityUtils.catchupEventStream(this);
                        return;
                    } else {
                        EventStreamService instance = EventStreamService.getInstance();
                        Event parseEvent = parseEvent(map);
                        String str9 = (String) map.get("room_name");
                        if (str9 == null && str != null) {
                            MXSession defaultSession = Matrix.getInstance(getApplicationContext()).getDefaultSession();
                            if (defaultSession != null && defaultSession.getDataHandler().getStore().isReady()) {
                                Room room = defaultSession.getDataHandler().getStore().getRoom(str);
                                if (room != null) {
                                    str9 = DinsicUtils.getRoomDisplayName(this, room);
                                }
                            }
                        }
                        String str10 = LOG_TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("## onMessageReceivedInternal() : the background sync is disabled with eventStreamService ");
                        sb4.append(instance);
                        Log.d(str10, sb4.toString());
                        EventStreamService.onStaticNotifiedEvent(getApplicationContext(), parseEvent, str9, (String) map.get("sender_display_name"), i);
                        return;
                    }
                }
            } catch (Exception e2) {
                String str11 = LOG_TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("## onMessageReceivedInternal() failed : ");
                sb5.append(e2.getMessage());
                Log.d(str11, sb5.toString(), e2);
            }
        }
        str = null;
        String str62 = LOG_TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("## onMessageReceivedInternal() : roomId ");
        sb6.append(str);
        sb6.append(" eventId ");
        sb6.append(str5);
        sb6.append(" unread ");
        sb6.append(i);
        Log.d(str62, sb6.toString());
        CommonActivityUtils.updateBadgeCount(getApplicationContext(), i);
        pushManager = Matrix.getInstance(getApplicationContext()).getPushManager();
        if (pushManager.areDeviceNotificationsAllowed()) {
        }
    }

    public void onNewToken(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onNewToken: ");
        sb.append(str);
        Log.d(str2, sb.toString());
        FcmHelper.storeFcmToken(this, str);
        Matrix.getInstance(this).getPushManager().resetFCMRegistration(str);
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onMessageReceived() from FCM with priority ");
        sb.append(remoteMessage.getPriority());
        sb.append(" from ");
        sb.append(remoteMessage.getFrom());
        Log.d(str, sb.toString());
        if (EventStreamService.getInstance() == null) {
            CommonActivityUtils.startEventStreamService(this);
        }
        final Map data = remoteMessage.getData();
        if (this.mUIHandler == null) {
            this.mUIHandler = new Handler(VectorApp.getInstance().getMainLooper());
        }
        this.mUIHandler.post(new Runnable() {
            public void run() {
                VectorFirebaseMessagingService.this.onMessageReceivedInternal(data);
            }
        });
    }

    public void onDeletedMessages() {
        Log.d(LOG_TAG, "## onDeletedMessages()");
    }
}
