package im.vector.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.push.PushManager;
import im.vector.push.PushManager.NotificationPrivacy;
import im.vector.repositories.ServerUrlsRepository;
import im.vector.ui.themes.ThemeUtils;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.matrix.androidsdk.core.Log;

public class PreferencesManager {
    private static final String DID_ASK_TO_IGNORE_BATTERY_OPTIMIZATIONS_KEY = "DID_ASK_TO_IGNORE_BATTERY_OPTIMIZATIONS_KEY";
    private static final String DID_ASK_TO_USE_ANALYTICS_TRACKING_KEY = "DID_ASK_TO_USE_ANALYTICS_TRACKING_KEY";
    private static final String LOG_TAG = PreferencesManager.class.getSimpleName();
    private static final int MEDIA_SAVING_1_MONTH = 2;
    private static final int MEDIA_SAVING_1_WEEK = 1;
    private static final int MEDIA_SAVING_3_DAYS = 0;
    private static final int MEDIA_SAVING_FOREVER = 3;
    private static final String SETTINGS_12_24_TIMESTAMPS_KEY = "SETTINGS_12_24_TIMESTAMPS_KEY";
    private static final String SETTINGS_ALWAYS_SHOW_TIMESTAMPS_KEY = "SETTINGS_ALWAYS_SHOW_TIMESTAMPS_KEY";
    public static final String SETTINGS_APP_TERM_CONDITIONS_PREFERENCE_KEY = "SETTINGS_APP_TERM_CONDITIONS_PREFERENCE_KEY";
    public static final String SETTINGS_BACKGROUND_SYNC_DIVIDER_PREFERENCE_KEY = "SETTINGS_BACKGROUND_SYNC_DIVIDER_PREFERENCE_KEY";
    public static final String SETTINGS_BACKGROUND_SYNC_PREFERENCE_KEY = "SETTINGS_BACKGROUND_SYNC_PREFERENCE_KEY";
    public static final String SETTINGS_CALL_INVITATIONS_PREFERENCE_KEY = "SETTINGS_CALL_INVITATIONS_PREFERENCE_KEY_2";
    public static final String SETTINGS_CALL_RINGTONE_URI_PREFERENCE_KEY = "SETTINGS_CALL_RINGTONE_URI_PREFERENCE_KEY";
    public static final String SETTINGS_CALL_RINGTONE_USE_RIOT_PREFERENCE_KEY = "SETTINGS_CALL_RINGTONE_USE_RIOT_PREFERENCE_KEY";
    public static final String SETTINGS_CHANGE_PASSWORD_PREFERENCE_KEY = "SETTINGS_CHANGE_PASSWORD_PREFERENCE_KEY";
    public static final String SETTINGS_CLEAR_CACHE_PREFERENCE_KEY = "SETTINGS_CLEAR_CACHE_PREFERENCE_KEY";
    public static final String SETTINGS_CLEAR_MEDIA_CACHE_PREFERENCE_KEY = "SETTINGS_CLEAR_MEDIA_CACHE_PREFERENCE_KEY";
    public static final String SETTINGS_CONTACTS_PHONEBOOK_COUNTRY_PREFERENCE_KEY = "SETTINGS_CONTACTS_PHONEBOOK_COUNTRY_PREFERENCE_KEY";
    public static final String SETTINGS_CONTACT_PREFERENCE_KEYS = "SETTINGS_CONTACT_PREFERENCE_KEYS";
    public static final String SETTINGS_CONTAINING_MY_DISPLAY_NAME_PREFERENCE_KEY = "SETTINGS_CONTAINING_MY_DISPLAY_NAME_PREFERENCE_KEY_2";
    public static final String SETTINGS_CONTAINING_MY_USER_NAME_PREFERENCE_KEY = "SETTINGS_CONTAINING_MY_USER_NAME_PREFERENCE_KEY_2";
    public static final String SETTINGS_COPYRIGHT_PREFERENCE_KEY = "SETTINGS_COPYRIGHT_PREFERENCE_KEY";
    public static final String SETTINGS_CRYPTOGRAPHY_DIVIDER_PREFERENCE_KEY = "SETTINGS_CRYPTOGRAPHY_DIVIDER_PREFERENCE_KEY";
    public static final String SETTINGS_CRYPTOGRAPHY_PREFERENCE_KEY = "SETTINGS_CRYPTOGRAPHY_PREFERENCE_KEY";
    public static final String SETTINGS_DATA_SAVE_MODE_PREFERENCE_KEY = "SETTINGS_DATA_SAVE_MODE_PREFERENCE_KEY";
    public static final String SETTINGS_DEACTIVATE_ACCOUNT_KEY = "SETTINGS_DEACTIVATE_ACCOUNT_KEY";
    public static final String SETTINGS_DETECT_ACCESSIBILITY_SERVICE_KEY = "SETTINGS_DETECT_ACCESSIBILITY_SERVICE_KEY";
    public static final String SETTINGS_DETECT_NOTIFICATION_LISTENER_KEY = "SETTINGS_DETECT_NOTIFICATION_LISTENER_KEY";
    public static final String SETTINGS_DEVICES_DIVIDER_PREFERENCE_KEY = "SETTINGS_DEVICES_DIVIDER_PREFERENCE_KEY";
    public static final String SETTINGS_DEVICES_LIST_PREFERENCE_KEY = "SETTINGS_DEVICES_LIST_PREFERENCE_KEY";
    private static final String SETTINGS_DISPLAY_ALL_EVENTS_KEY = "SETTINGS_DISPLAY_ALL_EVENTS_KEY";
    public static final String SETTINGS_DISPLAY_NAME_PREFERENCE_KEY = "SETTINGS_DISPLAY_NAME_PREFERENCE_KEY";
    public static final String SETTINGS_ENABLE_ALL_NOTIF_PREFERENCE_KEY = "SETTINGS_ENABLE_ALL_NOTIF_PREFERENCE_KEY";
    public static final String SETTINGS_ENABLE_BACKGROUND_SYNC_PREFERENCE_KEY = "SETTINGS_ENABLE_BACKGROUND_SYNC_PREFERENCE_KEY";
    private static final String SETTINGS_ENABLE_MARKDOWN_KEY = "SETTINGS_ENABLE_MARKDOWN_KEY";
    private static final String SETTINGS_ENABLE_SEND_VOICE_FEATURE_PREFERENCE_KEY = "SETTINGS_ENABLE_SEND_VOICE_FEATURE_PREFERENCE_KEY";
    public static final String SETTINGS_ENABLE_THIS_DEVICE_PREFERENCE_KEY = "SETTINGS_ENABLE_THIS_DEVICE_PREFERENCE_KEY";
    public static final String SETTINGS_ENCRYPTION_EXPORT_E2E_ROOM_KEYS_PREFERENCE_KEY = "SETTINGS_ENCRYPTION_EXPORT_E2E_ROOM_KEYS_PREFERENCE_KEY";
    public static final String SETTINGS_ENCRYPTION_IMPORT_E2E_ROOM_KEYS_PREFERENCE_KEY = "SETTINGS_ENCRYPTION_IMPORT_E2E_ROOM_KEYS_PREFERENCE_KEY";
    public static final String SETTINGS_ENCRYPTION_INFORMATION_DEVICE_ID_PREFERENCE_KEY = "SETTINGS_ENCRYPTION_INFORMATION_DEVICE_ID_PREFERENCE_KEY";
    public static final String SETTINGS_ENCRYPTION_INFORMATION_DEVICE_KEY_PREFERENCE_KEY = "SETTINGS_ENCRYPTION_INFORMATION_DEVICE_KEY_PREFERENCE_KEY";
    public static final String SETTINGS_ENCRYPTION_INFORMATION_DEVICE_NAME_PREFERENCE_KEY = "SETTINGS_ENCRYPTION_INFORMATION_DEVICE_NAME_PREFERENCE_KEY";
    public static final String SETTINGS_ENCRYPTION_NEVER_SENT_TO_PREFERENCE_KEY = "SETTINGS_ENCRYPTION_NEVER_SENT_TO_PREFERENCE_KEY";
    public static final String SETTINGS_GROUPS_FLAIR_KEY = "SETTINGS_GROUPS_FLAIR_KEY";
    public static final String SETTINGS_HIDE_FROM_USERS_DIRECTORY_KEY = "SETTINGS_HIDE_FROM_USERS_DIRECTORY_KEY";
    public static final String SETTINGS_HOME_SERVER_PREFERENCE_KEY = "SETTINGS_HOME_SERVER_PREFERENCE_KEY";
    public static final String SETTINGS_IDENTITY_SERVER_PREFERENCE_KEY = "SETTINGS_IDENTITY_SERVER_PREFERENCE_KEY";
    public static final String SETTINGS_IGNORED_USERS_PREFERENCE_KEY = "SETTINGS_IGNORED_USERS_PREFERENCE_KEY";
    public static final String SETTINGS_IGNORE_USERS_DIVIDER_PREFERENCE_KEY = "SETTINGS_IGNORE_USERS_DIVIDER_PREFERENCE_KEY";
    public static final String SETTINGS_INTERFACE_LANGUAGE_PREFERENCE_KEY = "SETTINGS_INTERFACE_LANGUAGE_PREFERENCE_KEY";
    public static final String SETTINGS_INTERFACE_TEXT_SIZE_KEY = "SETTINGS_INTERFACE_TEXT_SIZE_KEY";
    public static final String SETTINGS_INVITED_TO_ROOM_PREFERENCE_KEY = "SETTINGS_INVITED_TO_ROOM_PREFERENCE_KEY_2";
    public static final String SETTINGS_LABS_PREFERENCE_KEY = "SETTINGS_LABS_PREFERENCE_KEY";
    public static final String SETTINGS_LAZY_LOADING_PREFERENCE_KEY = "SETTINGS_LAZY_LOADING_PREFERENCE_KEY";
    public static final String SETTINGS_LOGGED_IN_PREFERENCE_KEY = "SETTINGS_LOGGED_IN_PREFERENCE_KEY";
    public static final String SETTINGS_MEDIA_SAVING_PERIOD_KEY = "SETTINGS_MEDIA_SAVING_PERIOD_KEY";
    private static final String SETTINGS_MEDIA_SAVING_PERIOD_SELECTED_KEY = "SETTINGS_MEDIA_SAVING_PERIOD_SELECTED_KEY";
    public static final String SETTINGS_MESSAGES_IN_GROUP_CHAT_PREFERENCE_KEY = "SETTINGS_MESSAGES_IN_GROUP_CHAT_PREFERENCE_KEY_2";
    public static final String SETTINGS_MESSAGES_IN_ONE_TO_ONE_PREFERENCE_KEY = "SETTINGS_MESSAGES_IN_ONE_TO_ONE_PREFERENCE_KEY_2";
    public static final String SETTINGS_MESSAGES_SENT_BY_BOT_PREFERENCE_KEY = "SETTINGS_MESSAGES_SENT_BY_BOT_PREFERENCE_KEY_2";
    public static final String SETTINGS_NOTIFICATIONS_KEY = "SETTINGS_NOTIFICATIONS_KEY";
    public static final String SETTINGS_NOTIFICATIONS_TARGETS_PREFERENCE_KEY = "SETTINGS_NOTIFICATIONS_TARGETS_PREFERENCE_KEY";
    public static final String SETTINGS_NOTIFICATIONS_TARGET_DIVIDER_PREFERENCE_KEY = "SETTINGS_NOTIFICATIONS_TARGET_DIVIDER_PREFERENCE_KEY";
    public static final String SETTINGS_NOTIFICATION_PRIVACY_PREFERENCE_KEY = "SETTINGS_NOTIFICATION_PRIVACY_PREFERENCE_KEY";
    public static final String SETTINGS_NOTIFICATION_RINGTONE_PREFERENCE_KEY = "SETTINGS_NOTIFICATION_RINGTONE_PREFERENCE_KEY";
    public static final String SETTINGS_NOTIFICATION_RINGTONE_SELECTION_PREFERENCE_KEY = "SETTINGS_NOTIFICATION_RINGTONE_SELECTION_PREFERENCE_KEY";
    public static final String SETTINGS_OLM_VERSION_PREFERENCE_KEY = "SETTINGS_OLM_VERSION_PREFERENCE_KEY";
    private static final String SETTINGS_PIN_MISSED_NOTIFICATIONS_PREFERENCE_KEY = "SETTINGS_PIN_MISSED_NOTIFICATIONS_PREFERENCE_KEY";
    private static final String SETTINGS_PIN_UNREAD_MESSAGES_PREFERENCE_KEY = "SETTINGS_PIN_UNREAD_MESSAGES_PREFERENCE_KEY";
    private static final String SETTINGS_PREVIEW_MEDIA_BEFORE_SENDING_KEY = "SETTINGS_PREVIEW_MEDIA_BEFORE_SENDING_KEY";
    public static final String SETTINGS_PRIVACY_POLICY_PREFERENCE_KEY = "SETTINGS_PRIVACY_POLICY_PREFERENCE_KEY";
    public static final String SETTINGS_PROFILE_PICTURE_PREFERENCE_KEY = "SETTINGS_PROFILE_PICTURE_PREFERENCE_KEY";
    public static final String SETTINGS_ROOM_SETTINGS_LABS_END_TO_END_IS_ACTIVE_PREFERENCE_KEY = "SETTINGS_ROOM_SETTINGS_LABS_END_TO_END_IS_ACTIVE_PREFERENCE_KEY";
    public static final String SETTINGS_ROOM_SETTINGS_LABS_END_TO_END_PREFERENCE_KEY = "SETTINGS_ROOM_SETTINGS_LABS_END_TO_END_PREFERENCE_KEY";
    private static final String SETTINGS_SEND_TYPING_NOTIF_KEY = "SETTINGS_SEND_TYPING_NOTIF_KEY";
    public static final String SETTINGS_SET_SYNC_DELAY_PREFERENCE_KEY = "SETTINGS_SET_SYNC_DELAY_PREFERENCE_KEY";
    private static final String SETTINGS_SHOW_AVATAR_DISPLAY_NAME_CHANGES_MESSAGES_KEY = "SETTINGS_SHOW_AVATAR_DISPLAY_NAME_CHANGES_MESSAGES_KEY";
    public static final String SETTINGS_SHOW_JOIN_LEAVE_MESSAGES_KEY = "SETTINGS_SHOW_JOIN_LEAVE_MESSAGES_KEY";
    private static final String SETTINGS_SHOW_READ_RECEIPTS_KEY = "SETTINGS_SHOW_READ_RECEIPTS_KEY";
    public static final String SETTINGS_SHOW_URL_PREVIEW_KEY = "SETTINGS_SHOW_URL_PREVIEW_KEY";
    public static final String SETTINGS_START_ON_BOOT_PREFERENCE_KEY = "SETTINGS_START_ON_BOOT_PREFERENCE_KEY";
    public static final String SETTINGS_THIRD_PARTY_NOTICES_PREFERENCE_KEY = "SETTINGS_THIRD_PARTY_NOTICES_PREFERENCE_KEY";
    public static final String SETTINGS_TURN_SCREEN_ON_PREFERENCE_KEY = "SETTINGS_TURN_SCREEN_ON_PREFERENCE_KEY";
    public static final String SETTINGS_USER_REFUSED_LAZY_LOADING_PREFERENCE_KEY = "SETTINGS_USER_REFUSED_LAZY_LOADING_PREFERENCE_KEY";
    public static final String SETTINGS_USER_SETTINGS_PREFERENCE_KEY = "SETTINGS_USER_SETTINGS_PREFERENCE_KEY";
    public static final String SETTINGS_USE_ANALYTICS_KEY = "SETTINGS_USE_ANALYTICS_KEY";
    private static final String SETTINGS_USE_JITSI_CONF_PREFERENCE_KEY = "SETTINGS_USE_JITSI_CONF_PREFERENCE_KEY";
    private static final String SETTINGS_USE_NATIVE_CAMERA_PREFERENCE_KEY = "SETTINGS_USE_NATIVE_CAMERA_PREFERENCE_KEY";
    public static final String SETTINGS_USE_RAGE_SHAKE_KEY = "SETTINGS_USE_RAGE_SHAKE_KEY";
    public static final String SETTINGS_VERSION_PREFERENCE_KEY = "SETTINGS_VERSION_PREFERENCE_KEY";
    private static final String SETTINGS_VIBRATE_ON_MENTION_KEY = "SETTINGS_VIBRATE_ON_MENTION_KEY";
    public static final String VERSION_BUILD = "VERSION_BUILD";
    private static final List<String> mKeysToKeepAfterLogout;

    public static boolean useAnalytics(Context context) {
        return false;
    }

    static {
        String str = SETTINGS_SET_SYNC_DELAY_PREFERENCE_KEY;
        mKeysToKeepAfterLogout = Arrays.asList(new String[]{SETTINGS_SEND_TYPING_NOTIF_KEY, SETTINGS_ALWAYS_SHOW_TIMESTAMPS_KEY, SETTINGS_12_24_TIMESTAMPS_KEY, SETTINGS_SHOW_READ_RECEIPTS_KEY, SETTINGS_SHOW_JOIN_LEAVE_MESSAGES_KEY, SETTINGS_SHOW_AVATAR_DISPLAY_NAME_CHANGES_MESSAGES_KEY, SETTINGS_MEDIA_SAVING_PERIOD_KEY, SETTINGS_MEDIA_SAVING_PERIOD_SELECTED_KEY, SETTINGS_PREVIEW_MEDIA_BEFORE_SENDING_KEY, SETTINGS_PIN_UNREAD_MESSAGES_PREFERENCE_KEY, SETTINGS_PIN_MISSED_NOTIFICATIONS_PREFERENCE_KEY, SETTINGS_DATA_SAVE_MODE_PREFERENCE_KEY, SETTINGS_START_ON_BOOT_PREFERENCE_KEY, SETTINGS_INTERFACE_TEXT_SIZE_KEY, SETTINGS_USE_JITSI_CONF_PREFERENCE_KEY, SETTINGS_NOTIFICATION_RINGTONE_PREFERENCE_KEY, SETTINGS_NOTIFICATION_RINGTONE_SELECTION_PREFERENCE_KEY, str, SETTINGS_ROOM_SETTINGS_LABS_END_TO_END_PREFERENCE_KEY, SETTINGS_CONTACTS_PHONEBOOK_COUNTRY_PREFERENCE_KEY, SETTINGS_INTERFACE_LANGUAGE_PREFERENCE_KEY, SETTINGS_BACKGROUND_SYNC_PREFERENCE_KEY, SETTINGS_ENABLE_BACKGROUND_SYNC_PREFERENCE_KEY, str, SETTINGS_USE_RAGE_SHAKE_KEY});
    }

    public static void clearPreferences(Context context) {
        HashSet hashSet = new HashSet(mKeysToKeepAfterLogout);
        hashSet.add(ServerUrlsRepository.HOME_SERVER_URL_PREF);
        hashSet.add(ServerUrlsRepository.IDENTITY_SERVER_URL_PREF);
        hashSet.add(ThemeUtils.APPLICATION_THEME_KEY);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = defaultSharedPreferences.edit();
        Set<String> keySet = defaultSharedPreferences.getAll().keySet();
        keySet.removeAll(hashSet);
        for (String remove : keySet) {
            edit.remove(remove);
        }
        edit.apply();
    }

    public static boolean detectNotificationListener(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_DETECT_NOTIFICATION_LISTENER_KEY, true);
    }

    public static void putDetectNotificationListener(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_DETECT_NOTIFICATION_LISTENER_KEY, z).apply();
    }

    public static boolean detectAccessibilityService(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_DETECT_ACCESSIBILITY_SERVICE_KEY, true);
    }

    public static void putDetectAccessibilityService(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_DETECT_ACCESSIBILITY_SERVICE_KEY, z).apply();
    }

    public static boolean didAskUserToIgnoreBatteryOptimizations(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DID_ASK_TO_IGNORE_BATTERY_OPTIMIZATIONS_KEY, false);
    }

    public static void setDidAskUserToIgnoreBatteryOptimizations(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(DID_ASK_TO_IGNORE_BATTERY_OPTIMIZATIONS_KEY, true).apply();
    }

    public static void resetDidAskUserToIgnoreBatteryOptimizations(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(DID_ASK_TO_IGNORE_BATTERY_OPTIMIZATIONS_KEY, false).apply();
    }

    public static boolean displayTimeIn12hFormat(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_12_24_TIMESTAMPS_KEY, false);
    }

    public static boolean showJoinLeaveMessages(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_SHOW_JOIN_LEAVE_MESSAGES_KEY, false);
    }

    public static boolean showAvatarDisplayNameChangeMessages(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_SHOW_AVATAR_DISPLAY_NAME_CHANGES_MESSAGES_KEY, false);
    }

    public static boolean isSendVoiceFeatureEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_ENABLE_SEND_VOICE_FEATURE_PREFERENCE_KEY, false);
    }

    public static void setNotificationRingTone(Context context, Uri uri) {
        String str;
        Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (uri != null) {
            str = uri.toString();
            if (str.startsWith("file://")) {
                return;
            }
        } else {
            str = "";
        }
        edit.putString(SETTINGS_NOTIFICATION_RINGTONE_PREFERENCE_KEY, str);
        edit.apply();
    }

    public static Uri getNotificationRingTone(Context context) {
        Uri uri = null;
        String string = PreferenceManager.getDefaultSharedPreferences(context).getString(SETTINGS_NOTIFICATION_RINGTONE_PREFERENCE_KEY, null);
        if (TextUtils.equals(string, "")) {
            return null;
        }
        if (string != null && !string.startsWith("file://")) {
            try {
                uri = Uri.parse(string);
            } catch (Exception e) {
                Log.e(LOG_TAG, "## getNotificationRingTone() : Uri.parse failed", e);
            }
        }
        if (uri == null) {
            uri = RingtoneManager.getDefaultUri(2);
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## getNotificationRingTone() returns ");
        sb.append(uri);
        Log.d(str, sb.toString());
        return uri;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0074  */
    public static String getNotificationRingToneName(Context context) {
        String str;
        String str2 = ".";
        String str3 = "_data";
        Uri notificationRingTone = getNotificationRingTone(context);
        Cursor cursor = null;
        if (notificationRingTone == null) {
            return null;
        }
        try {
            Cursor query = context.getContentResolver().query(notificationRingTone, new String[]{str3}, null, null, null);
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow(str3);
                query.moveToFirst();
                String name = new File(query.getString(columnIndexOrThrow)).getName();
                if (name.contains(str2)) {
                    name = name.substring(0, name.lastIndexOf(str2));
                }
                if (query != null) {
                    query.close();
                }
                str = name;
            } catch (Exception e) {
                e = e;
                cursor = query;
                str = null;
                try {
                    String str4 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## getNotificationRingToneName() failed() : ");
                    sb.append(e.getMessage());
                    Log.e(str4, sb.toString(), e);
                    if (cursor != null) {
                    }
                    return str;
                } catch (Throwable th) {
                    th = th;
                    if (cursor != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            str = null;
            String str42 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## getNotificationRingToneName() failed() : ");
            sb2.append(e.getMessage());
            Log.e(str42, sb2.toString(), e);
            if (cursor != null) {
                cursor.close();
            }
            return str;
        }
        return str;
    }

    public static void setUseLazyLoading(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_LAZY_LOADING_PREFERENCE_KEY, z).apply();
    }

    public static boolean useLazyLoading(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_LAZY_LOADING_PREFERENCE_KEY, false);
    }

    public static void setUserRefuseLazyLoading(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_USER_REFUSED_LAZY_LOADING_PREFERENCE_KEY, true).apply();
    }

    public static boolean hasUserRefusedLazyLoading(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_USER_REFUSED_LAZY_LOADING_PREFERENCE_KEY, false);
    }

    public static boolean useDataSaveMode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_DATA_SAVE_MODE_PREFERENCE_KEY, false);
    }

    public static boolean useJitsiConfCall(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_USE_JITSI_CONF_PREFERENCE_KEY, false);
    }

    public static boolean autoStartOnBoot(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_START_ON_BOOT_PREFERENCE_KEY, true);
    }

    public static void setAutoStartOnBoot(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_START_ON_BOOT_PREFERENCE_KEY, z).apply();
    }

    public static int getSelectedMediasSavingPeriod(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(SETTINGS_MEDIA_SAVING_PERIOD_SELECTED_KEY, 1);
    }

    public static void setSelectedMediasSavingPeriod(Context context, int i) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(SETTINGS_MEDIA_SAVING_PERIOD_SELECTED_KEY, i).apply();
    }

    public static long getMinMediasLastAccessTime(Context context) {
        long currentTimeMillis;
        long j;
        int selectedMediasSavingPeriod = getSelectedMediasSavingPeriod(context);
        if (selectedMediasSavingPeriod == 0) {
            currentTimeMillis = System.currentTimeMillis() / 1000;
            j = 259200;
        } else if (selectedMediasSavingPeriod == 1) {
            currentTimeMillis = System.currentTimeMillis() / 1000;
            j = 604800;
        } else if (selectedMediasSavingPeriod != 2) {
            if (selectedMediasSavingPeriod != 3) {
            }
            return 0;
        } else {
            currentTimeMillis = System.currentTimeMillis() / 1000;
            j = 2592000;
        }
        return currentTimeMillis - j;
    }

    public static String getSelectedMediasSavingPeriodString(Context context) {
        int selectedMediasSavingPeriod = getSelectedMediasSavingPeriod(context);
        if (selectedMediasSavingPeriod == 0) {
            return context.getString(R.string.media_saving_period_3_days);
        }
        if (selectedMediasSavingPeriod == 1) {
            return context.getString(R.string.media_saving_period_1_week);
        }
        if (selectedMediasSavingPeriod == 2) {
            return context.getString(R.string.media_saving_period_1_month);
        }
        if (selectedMediasSavingPeriod != 3) {
            return "?";
        }
        return context.getString(R.string.media_saving_period_forever);
    }

    public static void fixMigrationIssues(Context context) {
        PushManager pushManager = Matrix.getInstance(context).getPushManager();
        if (pushManager.getNotificationPrivacy() == NotificationPrivacy.REDUCED) {
            pushManager.setNotificationPrivacy(NotificationPrivacy.LOW_DETAIL, null);
            resetDidAskUserToIgnoreBatteryOptimizations(context);
        }
    }

    public static boolean isMarkdownEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_ENABLE_MARKDOWN_KEY, true);
    }

    public static void setMarkdownEnabled(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_ENABLE_MARKDOWN_KEY, z).apply();
    }

    public static boolean showReadReceipts(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_SHOW_READ_RECEIPTS_KEY, true);
    }

    public static boolean alwaysShowTimeStamps(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_ALWAYS_SHOW_TIMESTAMPS_KEY, false);
    }

    public static boolean sendTypingNotifs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_SEND_TYPING_NOTIF_KEY, true);
    }

    public static boolean pinMissedNotifications(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_PIN_MISSED_NOTIFICATIONS_PREFERENCE_KEY, true);
    }

    public static boolean pinUnreadMessages(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_PIN_UNREAD_MESSAGES_PREFERENCE_KEY, true);
    }

    public static boolean vibrateWhenMentioning(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_VIBRATE_ON_MENTION_KEY, false);
    }

    public static boolean didAskToUseAnalytics(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DID_ASK_TO_USE_ANALYTICS_TRACKING_KEY, false);
    }

    public static void setDidAskToUseAnalytics(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(DID_ASK_TO_USE_ANALYTICS_TRACKING_KEY, true).apply();
    }

    public static void setUseAnalytics(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_USE_ANALYTICS_KEY, z).apply();
    }

    public static boolean previewMediaWhenSending(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_PREVIEW_MEDIA_BEFORE_SENDING_KEY, false);
    }

    public static boolean useRageshake(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_USE_RAGE_SHAKE_KEY, true);
    }

    public static void setUseRageshake(Context context, boolean z) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTINGS_USE_RAGE_SHAKE_KEY, z).apply();
    }

    public static boolean displayAllEvents(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_DISPLAY_ALL_EVENTS_KEY, false);
    }
}
