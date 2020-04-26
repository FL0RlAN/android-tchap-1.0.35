package im.vector.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.gson.JsonObject;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.settings.VectorLocale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;

public class WidgetsManager {
    private static final String LOG_TAG = WidgetsManager.class.getSimpleName();
    private static final String SCALAR_TOKEN_PREFERENCE_KEY = "SCALAR_TOKEN_PREFERENCE_KEY";
    public static final String WIDGET_EVENT_TYPE = "im.vector.modular.widgets";
    private static final String WIDGET_TYPE_JITSI = "jitsi";
    public static final String WIDGET_USER_EVENT_TYPE = "m.widgets";
    private static final Set<onWidgetUpdateListener> mListeners = new HashSet();
    private static final WidgetsManager mSharedInstance = new WidgetsManager();
    /* access modifiers changed from: private */
    public final Map<String, ApiCallback<Widget>> mPendingWidgetCreationCallbacks = new HashMap();

    public class WidgetError extends MatrixError {
        public static final String WIDGET_CREATION_FAILED_ERROR_CODE = "WIDGET_CREATION_FAILED_ERROR_CODE";
        public static final String WIDGET_NOT_ENOUGH_POWER_ERROR_CODE = "WIDGET_NOT_ENOUGH_POWER_ERROR_CODE";

        public WidgetError(String str, String str2) {
            this.errcode = str;
            this.error = str2;
        }
    }

    public interface onWidgetUpdateListener {
        void onWidgetUpdate(Widget widget);
    }

    public static WidgetsManager getSharedInstance() {
        return mSharedInstance;
    }

    public List<Widget> getActiveWidgets(MXSession mXSession, Room room) {
        return getActiveWidgets(mXSession, room, null, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x006e  */
    private List<Widget> getActiveWidgets(MXSession mXSession, Room room, Set<String> set, Set<String> set2) {
        Object obj;
        String str = PasswordLoginParams.IDENTIFIER_KEY_TYPE;
        List<Event> stateEvents = room.getState().getStateEvents(new HashSet(Arrays.asList(new String[]{WIDGET_EVENT_TYPE})));
        HashMap hashMap = new HashMap();
        Collections.sort(stateEvents, new Comparator<Event>() {
            public int compare(Event event, Event event2) {
                long originServerTs = event.getOriginServerTs() - event2.getOriginServerTs();
                if (originServerTs < 0) {
                    return 1;
                }
                return originServerTs > 0 ? -1 : 0;
            }
        });
        for (Event event : stateEvents) {
            Widget widget = null;
            if (!(set == null && set2 == null)) {
                try {
                    JsonObject contentAsJsonObject = event.getContentAsJsonObject();
                    if (contentAsJsonObject.has(str)) {
                        obj = contentAsJsonObject.get(str).getAsString();
                        if (obj != null) {
                            if (set != null) {
                                if (!set.contains(obj)) {
                                }
                            }
                            if (set2 != null && set2.contains(obj)) {
                            }
                        }
                    }
                } catch (Exception e) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## getWidgets() failed : ");
                    sb.append(e.getMessage());
                    Log.e(str2, sb.toString(), e);
                }
                obj = null;
                if (obj != null) {
                }
            }
            if (event.stateKey != null && !hashMap.containsKey(event.stateKey)) {
                try {
                    if (event.roomId == null) {
                        String str3 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("## getWidgets() : set the room id to the event ");
                        sb2.append(event.eventId);
                        Log.e(str3, sb2.toString());
                        event.roomId = room.getRoomId();
                    }
                    widget = new Widget(mXSession, event);
                } catch (Exception e2) {
                    String str4 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## getWidgets() : widget creation failed ");
                    sb3.append(e2.getMessage());
                    Log.e(str4, sb3.toString(), e2);
                }
                if (widget != null) {
                    hashMap.put(widget.getWidgetId(), widget);
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        for (Widget widget2 : hashMap.values()) {
            if (widget2.isActive()) {
                arrayList.add(widget2);
            }
        }
        return arrayList;
    }

    public List<Widget> getActiveJitsiWidgets(MXSession mXSession, Room room) {
        return getActiveWidgets(mXSession, room, new HashSet(Arrays.asList(new String[]{WIDGET_TYPE_JITSI})), null);
    }

    public List<Widget> getActiveWebviewWidgets(MXSession mXSession, Room room) {
        return getActiveWidgets(mXSession, room, null, new HashSet(Arrays.asList(new String[]{WIDGET_TYPE_JITSI})));
    }

    public WidgetError checkWidgetPermission(MXSession mXSession, Room room) {
        if (room == null || room.getState() == null || room.getState().getPowerLevels() == null || room.getState().getPowerLevels().getUserPowerLevel(mXSession.getMyUserId()) >= room.getState().getPowerLevels().state_default) {
            return null;
        }
        return new WidgetError(WidgetError.WIDGET_NOT_ENOUGH_POWER_ERROR_CODE, VectorApp.getInstance().getString(R.string.widget_no_power_to_manage));
    }

    private void createWidget(MXSession mXSession, Room room, String str, Map<String, Object> map, final ApiCallback<Widget> apiCallback) {
        WidgetError checkWidgetPermission = checkWidgetPermission(mXSession, room);
        if (checkWidgetPermission != null) {
            if (apiCallback != null) {
                apiCallback.onMatrixError(checkWidgetPermission);
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(mXSession.getMyUserId());
        sb.append("_");
        sb.append(str);
        final String sb2 = sb.toString();
        if (apiCallback != null) {
            this.mPendingWidgetCreationCallbacks.put(sb2, apiCallback);
        }
        mXSession.getRoomsApiClient().sendStateEvent(room.getRoomId(), WIDGET_EVENT_TYPE, str, map, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
            }

            public void onNetworkError(Exception exc) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
                WidgetsManager.this.mPendingWidgetCreationCallbacks.remove(sb2);
            }

            public void onMatrixError(MatrixError matrixError) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
                WidgetsManager.this.mPendingWidgetCreationCallbacks.remove(sb2);
            }

            public void onUnexpectedError(Exception exc) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
                WidgetsManager.this.mPendingWidgetCreationCallbacks.remove(sb2);
            }
        });
    }

    public void createJitsiWidget(MXSession mXSession, Room room, boolean z, ApiCallback<Widget> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("jitsi_");
        sb.append(mXSession.getMyUserId());
        sb.append("_");
        sb.append(System.currentTimeMillis());
        String sb2 = sb.toString();
        String uuid = UUID.randomUUID().toString();
        if (uuid.length() > 8) {
            uuid = uuid.substring(0, 7);
        }
        String roomId = room.getRoomId();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(roomId.substring(1, roomId.indexOf(":") - 1));
        sb3.append(uuid.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()));
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("https://scalar.vector.im/api/widgets/jitsi.html?confId=");
        sb5.append(sb4);
        sb5.append("&isAudioConf=");
        sb5.append(z ? "false" : "true");
        sb5.append("&displayName=$matrix_display_name&avatarUrl=$matrix_avatar_url&email=$matrix_user_id");
        String sb6 = sb5.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("url", sb6);
        hashMap.put(PasswordLoginParams.IDENTIFIER_KEY_TYPE, WIDGET_TYPE_JITSI);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("widgetSessionId", uuid);
        hashMap.put("data", hashMap2);
        createWidget(mXSession, room, sb2, hashMap, apiCallback);
    }

    public void closeWidget(MXSession mXSession, Room room, String str, ApiCallback<Void> apiCallback) {
        if (!(mXSession == null || room == null || str == null)) {
            WidgetError checkWidgetPermission = checkWidgetPermission(mXSession, room);
            if (checkWidgetPermission != null) {
                if (apiCallback != null) {
                    apiCallback.onMatrixError(checkWidgetPermission);
                }
                return;
            }
            mXSession.getRoomsApiClient().sendStateEvent(room.getRoomId(), WIDGET_EVENT_TYPE, str, new HashMap(), apiCallback);
        }
    }

    public static void addListener(onWidgetUpdateListener onwidgetupdatelistener) {
        if (onwidgetupdatelistener != null) {
            synchronized (mListeners) {
                mListeners.add(onwidgetupdatelistener);
            }
        }
    }

    public static void removeListener(onWidgetUpdateListener onwidgetupdatelistener) {
        if (onwidgetupdatelistener != null) {
            synchronized (mListeners) {
                mListeners.remove(onwidgetupdatelistener);
            }
        }
    }

    private void onWidgetUpdate(Widget widget) {
        synchronized (mListeners) {
            for (onWidgetUpdateListener onWidgetUpdate : mListeners) {
                try {
                    onWidgetUpdate.onWidgetUpdate(widget);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onWidgetUpdate failed: ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
        }
    }

    public void onLiveEvent(MXSession mXSession, Event event) {
        if (TextUtils.equals(WIDGET_EVENT_TYPE, event.getType())) {
            String str = event.stateKey;
            StringBuilder sb = new StringBuilder();
            sb.append(mXSession.getMyUserId());
            sb.append("_");
            sb.append(str);
            String sb2 = sb.toString();
            String str2 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## onLiveEvent() : New widget detected: ");
            sb3.append(str);
            sb3.append(" in room ");
            sb3.append(event.roomId);
            Log.d(str2, sb3.toString());
            Widget widget = null;
            try {
                widget = new Widget(mXSession, event);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("## onLiveEvent () : widget creation failed ");
                sb4.append(e.getMessage());
                Log.e(str3, sb4.toString(), e);
            }
            if (widget != null) {
                if (this.mPendingWidgetCreationCallbacks.containsKey(sb2)) {
                    try {
                        ((ApiCallback) this.mPendingWidgetCreationCallbacks.get(sb2)).onSuccess(widget);
                    } catch (Exception e2) {
                        String str4 = LOG_TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("## onLiveEvent() : get(callbackKey).onSuccess failed ");
                        sb5.append(e2.getMessage());
                        Log.e(str4, sb5.toString(), e2);
                    }
                }
                onWidgetUpdate(widget);
            } else {
                String str5 = LOG_TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("## onLiveEvent() : Cannot decode new widget - event: ");
                sb6.append(event);
                Log.e(str5, sb6.toString());
                if (this.mPendingWidgetCreationCallbacks.containsKey(sb2)) {
                    try {
                        ((ApiCallback) this.mPendingWidgetCreationCallbacks.get(sb2)).onMatrixError(new WidgetError(WidgetError.WIDGET_CREATION_FAILED_ERROR_CODE, VectorApp.getInstance().getString(R.string.widget_creation_failure)));
                    } catch (Exception e3) {
                        String str6 = LOG_TAG;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("## onLiveEvent() : get(callbackKey).onMatrixError failed ");
                        sb7.append(e3.getMessage());
                        Log.e(str6, sb7.toString(), e3);
                    }
                }
            }
            this.mPendingWidgetCreationCallbacks.remove(sb2);
        }
    }

    public static void getFormattedWidgetUrl(Context context, final Widget widget, final ApiCallback<String> apiCallback) {
        getScalarToken(context, Matrix.getInstance(context).getSession(widget.getSessionId()), new ApiCallback<String>() {
            public void onSuccess(String str) {
                if (str == null) {
                    apiCallback.onSuccess(widget.getUrl());
                    return;
                }
                ApiCallback apiCallback = apiCallback;
                StringBuilder sb = new StringBuilder();
                sb.append(widget.getUrl());
                sb.append("&scalar_token=");
                sb.append(str);
                apiCallback.onSuccess(sb.toString());
            }

            public void onNetworkError(Exception exc) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            }

            public void onUnexpectedError(Exception exc) {
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
            }
        });
    }

    public static void getScalarToken(final Context context, MXSession mXSession, final ApiCallback<String> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append(SCALAR_TOKEN_PREFERENCE_KEY);
        sb.append(mXSession.getMyUserId());
        final String sb2 = sb.toString();
        final String string = PreferenceManager.getDefaultSharedPreferences(context).getString(sb2, null);
        if (string != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    String str = string;
                    if (str != null) {
                        apiCallback.onSuccess(str);
                    }
                }
            });
        } else {
            mXSession.openIdToken(new ApiCallback<Map<Object, Object>>() {
                public void onSuccess(Map<Object, Object> map) {
                    new WidgetsRestClient(context).register(map, new ApiCallback<Map<String, String>>() {
                        public void onSuccess(Map<String, String> map) {
                            String str = (String) map.get("scalar_token");
                            if (str != null) {
                                PreferenceManager.getDefaultSharedPreferences(context).edit().putString(sb2, str).apply();
                            }
                            if (apiCallback != null) {
                                apiCallback.onSuccess(str);
                            }
                        }

                        public void onNetworkError(Exception exc) {
                            if (apiCallback != null) {
                                apiCallback.onNetworkError(exc);
                            }
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            if (apiCallback != null) {
                                apiCallback.onMatrixError(matrixError);
                            }
                        }

                        public void onUnexpectedError(Exception exc) {
                            if (apiCallback != null) {
                                apiCallback.onUnexpectedError(exc);
                            }
                        }
                    });
                }

                public void onNetworkError(Exception exc) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onNetworkError(exc);
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onMatrixError(matrixError);
                    }
                }

                public void onUnexpectedError(Exception exc) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(exc);
                    }
                }
            });
        }
    }

    public static void clearScalarToken(Context context, MXSession mXSession) {
        StringBuilder sb = new StringBuilder();
        sb.append(SCALAR_TOKEN_PREFERENCE_KEY);
        sb.append(mXSession.getMyUserId());
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(sb.toString()).apply();
    }
}
