package fr.gouv.tchap.version;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.rest.client.TchapConfigRestClient;
import fr.gouv.tchap.sdk.rest.model.MinClientVersion;
import fr.gouv.tchap.sdk.rest.model.MinVersion;
import fr.gouv.tchap.sdk.rest.model.TchapClientConfig;
import fr.gouv.tchap.version.VersionCheckResult.Ok;
import fr.gouv.tchap.version.VersionCheckResult.ShowUpgradeScreen;
import fr.gouv.tchap.version.VersionCheckResult.Unknown;
import java.util.Calendar;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0013H\u0002J\u001c\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0013J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0016\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\tJ\u000e\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u001e\u0010\u001a\u001a\u00020\u001b*\u00020\u001c2\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0001\u0010\u001d\u001a\u00020\tH\u0002J\u0014\u0010\u001e\u001a\u00020\u000b*\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX.¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lfr/gouv/tchap/version/VersionChecker;", "", "()V", "DO_NOT_SHOW_UPGRADE_BEFORE_KEY", "", "LAST_VERSION_CHECK_TS_KEY", "LAST_VERSION_INFO_DISPLAYED_VERSION_CODE", "LOG_TAG", "MIN_DELAY_BETWEEN_TWO_REQUEST_MILLIS", "", "lastVersionCheckResult", "Lfr/gouv/tchap/version/VersionCheckResult;", "restClient", "Lfr/gouv/tchap/sdk/rest/client/TchapConfigRestClient;", "answer", "", "context", "Landroid/content/Context;", "callback", "Lorg/matrix/androidsdk/core/callback/SuccessCallback;", "checkVersion", "getCurrentDayMillis", "", "onUpgradeScreenDisplayed", "forVersionCode", "showLater", "toShowUpgradeScreen", "Lfr/gouv/tchap/version/VersionCheckResult$ShowUpgradeScreen;", "Lfr/gouv/tchap/sdk/rest/model/MinVersion;", "defaultMessageResId", "toVersionCheckResult", "Lfr/gouv/tchap/sdk/rest/model/TchapClientConfig;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VersionChecker.kt */
public final class VersionChecker {
    private static final String DO_NOT_SHOW_UPGRADE_BEFORE_KEY = "DO_NOT_SHOW_UPGRADE_BEFORE_KEY";
    public static final VersionChecker INSTANCE = new VersionChecker();
    private static final String LAST_VERSION_CHECK_TS_KEY = "LAST_VERSION_CHECK_TS_KEY";
    private static final String LAST_VERSION_INFO_DISPLAYED_VERSION_CODE = "LAST_VERSION_INFO_DISPLAYED_CODE";
    private static final String LOG_TAG = "VersionChecker";
    private static final int MIN_DELAY_BETWEEN_TWO_REQUEST_MILLIS = 86400000;
    /* access modifiers changed from: private */
    public static VersionCheckResult lastVersionCheckResult;
    private static final TchapConfigRestClient restClient = new TchapConfigRestClient();

    private VersionChecker() {
    }

    public static final /* synthetic */ VersionCheckResult access$getLastVersionCheckResult$p(VersionChecker versionChecker) {
        VersionCheckResult versionCheckResult = lastVersionCheckResult;
        if (versionCheckResult == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lastVersionCheckResult");
        }
        return versionCheckResult;
    }

    /* access modifiers changed from: private */
    public final void answer(Context context, SuccessCallback<VersionCheckResult> successCallback) {
        VersionCheckResult versionCheckResult = lastVersionCheckResult;
        if (versionCheckResult == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lastVersionCheckResult");
        }
        if ((versionCheckResult instanceof Ok) || (versionCheckResult instanceof Unknown)) {
            successCallback.onSuccess(versionCheckResult);
        } else if (versionCheckResult instanceof ShowUpgradeScreen) {
            ShowUpgradeScreen showUpgradeScreen = (ShowUpgradeScreen) versionCheckResult;
            String str = "PreferenceManager.getDef…ltSharedPreferences(this)";
            if (showUpgradeScreen.getDisplayOnlyOnce()) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, str);
                if (defaultSharedPreferences.getInt(LAST_VERSION_INFO_DISPLAYED_VERSION_CODE, 0) == showUpgradeScreen.getForVersionCode()) {
                    successCallback.onSuccess(Ok.INSTANCE);
                    return;
                }
            }
            if (showUpgradeScreen.getCanOpenApp()) {
                long currentDayMillis = getCurrentDayMillis();
                SharedPreferences defaultSharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(context);
                Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences2, str);
                if (currentDayMillis < defaultSharedPreferences2.getLong(DO_NOT_SHOW_UPGRADE_BEFORE_KEY, 0)) {
                    successCallback.onSuccess(Ok.INSTANCE);
                    return;
                }
            }
            successCallback.onSuccess(versionCheckResult);
        }
    }

    private final long getCurrentDayMillis() {
        Calendar instance = Calendar.getInstance();
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        instance.set(11, 5);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Calendar.getInstance()\n …DAY, 5)\n                }");
        return instance.getTimeInMillis();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    public final VersionCheckResult toVersionCheckResult(TchapClientConfig tchapClientConfig, Context context) {
        int i;
        VersionCheckResult versionCheckResult;
        int i2;
        MinClientVersion minClientVersion = tchapClientConfig.minimumClientVersion;
        ShowUpgradeScreen showUpgradeScreen = null;
        MinVersion minVersion = minClientVersion != null ? minClientVersion.criticalMinVersion : null;
        MinClientVersion minClientVersion2 = tchapClientConfig.minimumClientVersion;
        MinVersion minVersion2 = minClientVersion2 != null ? minClientVersion2.mandatoryMinVersion : null;
        MinClientVersion minClientVersion3 = tchapClientConfig.minimumClientVersion;
        MinVersion minVersion3 = minClientVersion3 != null ? minClientVersion3.infoMinVersion : null;
        int i3 = 0;
        if (minVersion != null) {
            Integer num = minVersion.minVersionCode;
            if (num != null) {
                i = num.intValue();
                if (i <= 58) {
                    if (minVersion != null) {
                        showUpgradeScreen = toShowUpgradeScreen(minVersion, context, R.string.an_update_is_available_critical);
                    }
                    versionCheckResult = showUpgradeScreen;
                } else {
                    if (minVersion2 != null) {
                        Integer num2 = minVersion2.minVersionCode;
                        if (num2 != null) {
                            i2 = num2.intValue();
                            if (i2 <= 58) {
                                if (minVersion2 != null) {
                                    showUpgradeScreen = toShowUpgradeScreen(minVersion2, context, R.string.an_update_is_available_mandatory);
                                }
                                versionCheckResult = showUpgradeScreen;
                            } else {
                                if (minVersion3 != null) {
                                    Integer num3 = minVersion3.minVersionCode;
                                    if (num3 != null) {
                                        i3 = num3.intValue();
                                    }
                                }
                                if (i3 > 58) {
                                    if (minVersion3 != null) {
                                        showUpgradeScreen = toShowUpgradeScreen(minVersion3, context, R.string.an_update_is_available_info);
                                    }
                                    versionCheckResult = showUpgradeScreen;
                                } else {
                                    versionCheckResult = Ok.INSTANCE;
                                }
                            }
                        }
                    }
                    i2 = 0;
                    if (i2 <= 58) {
                    }
                }
                return versionCheckResult == null ? versionCheckResult : Unknown.INSTANCE;
            }
        }
        i = 0;
        if (i <= 58) {
        }
        if (versionCheckResult == null) {
        }
    }

    private final ShowUpgradeScreen toShowUpgradeScreen(MinVersion minVersion, Context context, int i) {
        Map<String, String> map = minVersion.message;
        String str = map != null ? (String) map.get(context.getString(R.string.resources_language)) : null;
        CharSequence charSequence = str;
        int i2 = 0;
        if (!(!(charSequence == null || charSequence.length() == 0))) {
            str = null;
        }
        if (str == null) {
            Map<String, String> map2 = minVersion.message;
            str = map2 != null ? (String) map2.get(BingRule.ACTION_VALUE_DEFAULT) : null;
            CharSequence charSequence2 = str;
            if (!(!(charSequence2 == null || charSequence2.length() == 0))) {
                str = null;
            }
        }
        if (str == null) {
            str = context.getString(i);
            Intrinsics.checkExpressionValueIsNotNull(str, "context.getString(defaultMessageResId)");
        }
        Integer num = minVersion.minVersionCode;
        if (num != null) {
            i2 = num.intValue();
        }
        return new ShowUpgradeScreen(str, i2, Intrinsics.areEqual((Object) minVersion.displayOnlyOnce, (Object) Boolean.valueOf(true)), Intrinsics.areEqual((Object) minVersion.allowOpeningApp, (Object) Boolean.valueOf(true)));
    }

    public final void checkVersion(Context context, SuccessCallback<VersionCheckResult> successCallback) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(successCallback, "callback");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…ltSharedPreferences(this)");
        long j = defaultSharedPreferences.getLong(LAST_VERSION_CHECK_TS_KEY, 0);
        long currentDayMillis = getCurrentDayMillis();
        lastVersionCheckResult = VersionCheckResultStore.INSTANCE.read(context);
        VersionCheckResult versionCheckResult = lastVersionCheckResult;
        if (versionCheckResult == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lastVersionCheckResult");
        }
        if ((versionCheckResult instanceof Unknown) || currentDayMillis >= j + ((long) MIN_DELAY_BETWEEN_TWO_REQUEST_MILLIS)) {
            restClient.getClientConfig(new VersionChecker$checkVersion$1(context, currentDayMillis, successCallback));
        } else {
            answer(context, successCallback);
        }
    }

    public final void showLater(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…ltSharedPreferences(this)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putLong(DO_NOT_SHOW_UPGRADE_BEFORE_KEY, INSTANCE.getCurrentDayMillis() + ((long) MIN_DELAY_BETWEEN_TWO_REQUEST_MILLIS));
        edit.apply();
    }

    public final void onUpgradeScreenDisplayed(Context context, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…ltSharedPreferences(this)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putInt(LAST_VERSION_INFO_DISPLAYED_VERSION_CODE, i);
        edit.apply();
    }
}
