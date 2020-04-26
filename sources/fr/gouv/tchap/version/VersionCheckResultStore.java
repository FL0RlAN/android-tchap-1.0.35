package fr.gouv.tchap.version;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import fr.gouv.tchap.version.VersionCheckResult.Ok;
import fr.gouv.tchap.version.VersionCheckResult.ShowUpgradeScreen;
import fr.gouv.tchap.version.VersionCheckResult.Unknown;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lfr/gouv/tchap/version/VersionCheckResultStore;", "", "()V", "VERSION_CHECK_RESULT_STORE_CAN_OPEN_APP", "", "VERSION_CHECK_RESULT_STORE_DISPLAY_ONLY_ONCE", "VERSION_CHECK_RESULT_STORE_FOR_VERSION_CODE", "VERSION_CHECK_RESULT_STORE_MESSAGE", "VERSION_CHECK_RESULT_STORE_TYPE", "read", "Lfr/gouv/tchap/version/VersionCheckResult;", "context", "Landroid/content/Context;", "write", "", "versionCheckResult", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VersionCheckResultStore.kt */
public final class VersionCheckResultStore {
    public static final VersionCheckResultStore INSTANCE = new VersionCheckResultStore();
    private static final String VERSION_CHECK_RESULT_STORE_CAN_OPEN_APP = "VERSION_CHECK_RESULT_STORE_CAN_OPEN_APP";
    private static final String VERSION_CHECK_RESULT_STORE_DISPLAY_ONLY_ONCE = "VERSION_CHECK_RESULT_STORE_DISPLAY_ONLY_ONCE";
    private static final String VERSION_CHECK_RESULT_STORE_FOR_VERSION_CODE = "VERSION_CHECK_RESULT_STORE_FOR_VERSION_CODE";
    private static final String VERSION_CHECK_RESULT_STORE_MESSAGE = "VERSION_CHECK_RESULT_STORE_MESSAGE";
    private static final String VERSION_CHECK_RESULT_STORE_TYPE = "VERSION_CHECK_RESULT_STORE_TYPE";

    private VersionCheckResultStore() {
    }

    public final VersionCheckResult read(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String str = "PreferenceManager.getDef…ltSharedPreferences(this)";
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, str);
        int i = defaultSharedPreferences.getInt(VERSION_CHECK_RESULT_STORE_TYPE, 0);
        if (i == 1) {
            return Ok.INSTANCE;
        }
        if (i != 2) {
            return Unknown.INSTANCE;
        }
        SharedPreferences defaultSharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences2, str);
        String str2 = "";
        String string = defaultSharedPreferences2.getString(VERSION_CHECK_RESULT_STORE_MESSAGE, str2);
        if (string == null) {
            string = str2;
        }
        SharedPreferences defaultSharedPreferences3 = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences3, str);
        int i2 = defaultSharedPreferences3.getInt(VERSION_CHECK_RESULT_STORE_FOR_VERSION_CODE, 0);
        SharedPreferences defaultSharedPreferences4 = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences4, str);
        boolean z = defaultSharedPreferences4.getBoolean(VERSION_CHECK_RESULT_STORE_DISPLAY_ONLY_ONCE, false);
        SharedPreferences defaultSharedPreferences5 = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences5, str);
        return new ShowUpgradeScreen(string, i2, z, defaultSharedPreferences5.getBoolean(VERSION_CHECK_RESULT_STORE_CAN_OPEN_APP, false));
    }

    public final void write(Context context, VersionCheckResult versionCheckResult) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(versionCheckResult, "versionCheckResult");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…ltSharedPreferences(this)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        boolean z = versionCheckResult instanceof Ok;
        String str = VERSION_CHECK_RESULT_STORE_CAN_OPEN_APP;
        String str2 = VERSION_CHECK_RESULT_STORE_DISPLAY_ONLY_ONCE;
        String str3 = VERSION_CHECK_RESULT_STORE_FOR_VERSION_CODE;
        String str4 = VERSION_CHECK_RESULT_STORE_MESSAGE;
        String str5 = VERSION_CHECK_RESULT_STORE_TYPE;
        if (z) {
            edit.putInt(str5, 1);
            edit.remove(str4);
            edit.remove(str3);
            edit.remove(str2);
            edit.remove(str);
        } else if (versionCheckResult instanceof ShowUpgradeScreen) {
            edit.putInt(str5, 2);
            ShowUpgradeScreen showUpgradeScreen = (ShowUpgradeScreen) versionCheckResult;
            edit.putString(str4, showUpgradeScreen.getMessage());
            edit.putInt(str3, showUpgradeScreen.getForVersionCode());
            edit.putBoolean(str2, showUpgradeScreen.getDisplayOnlyOnce());
            edit.putBoolean(str, showUpgradeScreen.getCanOpenApp());
        } else {
            edit.putInt(str5, 0);
            edit.remove(str4);
            edit.remove(str3);
            edit.remove(str2);
            edit.remove(str);
        }
        edit.apply();
    }
}
