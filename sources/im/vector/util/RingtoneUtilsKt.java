package im.vector.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0007\u001a\u0016\u0010\u000b\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r\u001a\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\u0003¨\u0006\u000f"}, d2 = {"getCallRingtone", "Landroid/media/Ringtone;", "context", "Landroid/content/Context;", "getCallRingtoneName", "", "getCallRingtoneUri", "Landroid/net/Uri;", "setCallRingtoneUri", "", "ringtoneUri", "setUseRiotDefaultRingtone", "useRiotDefault", "", "useRiotDefaultRingtone", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: RingtoneUtils.kt */
public final class RingtoneUtilsKt {
    public static final Uri getCallRingtoneUri(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        String string = PreferenceManager.getDefaultSharedPreferences(context).getString(PreferencesManager.SETTINGS_CALL_RINGTONE_URI_PREFERENCE_KEY, null);
        if (string != null) {
            Uri parse = Uri.parse(string);
            Intrinsics.checkExpressionValueIsNotNull(parse, "Uri.parse(it)");
            return parse;
        }
        Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, 1);
        Intrinsics.checkExpressionValueIsNotNull(actualDefaultRingtoneUri, "RingtoneManager.getActua…oneManager.TYPE_RINGTONE)");
        return actualDefaultRingtoneUri;
    }

    public static final Ringtone getCallRingtone(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Ringtone ringtone = RingtoneManager.getRingtone(context, getCallRingtoneUri(context));
        Intrinsics.checkExpressionValueIsNotNull(ringtone, "RingtoneManager.getRingt…CallRingtoneUri(context))");
        return ringtone;
    }

    public static final String getCallRingtoneName(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return getCallRingtone(context).getTitle(context);
    }

    public static final void setCallRingtoneUri(Context context, Uri uri) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(uri, "ringtoneUri");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…haredPreferences(context)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putString(PreferencesManager.SETTINGS_CALL_RINGTONE_URI_PREFERENCE_KEY, uri.toString());
        edit.apply();
    }

    public static final boolean useRiotDefaultRingtone(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PreferencesManager.SETTINGS_CALL_RINGTONE_USE_RIOT_PREFERENCE_KEY, true);
    }

    public static final void setUseRiotDefaultRingtone(Context context, boolean z) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…haredPreferences(context)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putBoolean(PreferencesManager.SETTINGS_CALL_RINGTONE_USE_RIOT_PREFERENCE_KEY, z);
        edit.apply();
    }
}
