package im.vector.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u0004J\u0016\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u0004J\u001e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004J\u001e\u0010\u0016\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lim/vector/repositories/ServerUrlsRepository;", "", "()V", "DEFAULT_REFERRER_HOME_SERVER_URL_PREF", "", "DEFAULT_REFERRER_IDENTITY_SERVER_URL_PREF", "HOME_SERVER_URL_PREF", "IDENTITY_SERVER_URL_PREF", "getDefaultHomeServerUrl", "context", "Landroid/content/Context;", "getDefaultIdentityServerUrl", "getLastHomeServerUrl", "getLastIdentityServerUrl", "isDefaultHomeServerUrl", "", "url", "isDefaultIdentityServerUrl", "saveServerUrls", "", "homeServerUrl", "identityServerUrl", "setDefaultUrlsFromReferrer", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ServerUrlsRepository.kt */
public final class ServerUrlsRepository {
    private static final String DEFAULT_REFERRER_HOME_SERVER_URL_PREF = "default_referrer_home_server_url";
    private static final String DEFAULT_REFERRER_IDENTITY_SERVER_URL_PREF = "default_referrer_identity_server_url";
    public static final String HOME_SERVER_URL_PREF = "home_server_url";
    public static final String IDENTITY_SERVER_URL_PREF = "identity_server_url";
    public static final ServerUrlsRepository INSTANCE = new ServerUrlsRepository();

    public final String getDefaultHomeServerUrl(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return "";
    }

    public final String getDefaultIdentityServerUrl(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return "";
    }

    private ServerUrlsRepository() {
    }

    public final void setDefaultUrlsFromReferrer(Context context, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "homeServerUrl");
        Intrinsics.checkParameterIsNotNull(str2, "identityServerUrl");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…haredPreferences(context)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        if (!TextUtils.isEmpty(str)) {
            edit.putString(DEFAULT_REFERRER_HOME_SERVER_URL_PREF, str);
        }
        if (!TextUtils.isEmpty(str2)) {
            edit.putString(DEFAULT_REFERRER_IDENTITY_SERVER_URL_PREF, str2);
        }
        edit.apply();
    }

    public final void saveServerUrls(Context context, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "homeServerUrl");
        Intrinsics.checkParameterIsNotNull(str2, "identityServerUrl");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…haredPreferences(context)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putString(HOME_SERVER_URL_PREF, str);
        edit.putString(IDENTITY_SERVER_URL_PREF, str2);
        edit.apply();
    }

    public final String getLastHomeServerUrl(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = defaultSharedPreferences.getString(HOME_SERVER_URL_PREF, defaultSharedPreferences.getString(DEFAULT_REFERRER_HOME_SERVER_URL_PREF, getDefaultHomeServerUrl(context)));
        Intrinsics.checkExpressionValueIsNotNull(string, "prefs.getString(HOME_SER…tHomeServerUrl(context)))");
        return string;
    }

    public final String getLastIdentityServerUrl(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = defaultSharedPreferences.getString(IDENTITY_SERVER_URL_PREF, defaultSharedPreferences.getString(DEFAULT_REFERRER_IDENTITY_SERVER_URL_PREF, getDefaultIdentityServerUrl(context)));
        Intrinsics.checkExpressionValueIsNotNull(string, "prefs.getString(IDENTITY…ntityServerUrl(context)))");
        return string;
    }

    public final boolean isDefaultHomeServerUrl(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "url");
        return Intrinsics.areEqual((Object) str, (Object) getDefaultHomeServerUrl(context));
    }

    public final boolean isDefaultIdentityServerUrl(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "url");
        return Intrinsics.areEqual((Object) str, (Object) getDefaultIdentityServerUrl(context));
    }
}
