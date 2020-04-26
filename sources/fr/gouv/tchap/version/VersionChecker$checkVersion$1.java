package fr.gouv.tchap.version;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import fr.gouv.tchap.sdk.rest.model.TchapClientConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0014\u0010\u0007\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\u0014\u0010\f\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016¨\u0006\r"}, d2 = {"fr/gouv/tchap/version/VersionChecker$checkVersion$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lfr/gouv/tchap/sdk/rest/model/TchapClientConfig;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VersionChecker.kt */
public final class VersionChecker$checkVersion$1 implements ApiCallback<TchapClientConfig> {
    final /* synthetic */ SuccessCallback $callback;
    final /* synthetic */ Context $context;
    final /* synthetic */ long $currentDay;

    VersionChecker$checkVersion$1(Context context, long j, SuccessCallback successCallback) {
        this.$context = context;
        this.$currentDay = j;
        this.$callback = successCallback;
    }

    public void onSuccess(TchapClientConfig tchapClientConfig) {
        Intrinsics.checkParameterIsNotNull(tchapClientConfig, "info");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.$context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…ltSharedPreferences(this)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putLong("LAST_VERSION_CHECK_TS_KEY", this.$currentDay);
        edit.apply();
        VersionChecker.lastVersionCheckResult = VersionChecker.INSTANCE.toVersionCheckResult(tchapClientConfig, this.$context);
        VersionCheckResultStore.INSTANCE.write(this.$context, VersionChecker.access$getLastVersionCheckResult$p(VersionChecker.INSTANCE));
        VersionChecker.INSTANCE.answer(this.$context, this.$callback);
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        Log.e("VersionChecker", "error", exc);
        VersionChecker.INSTANCE.answer(this.$context, this.$callback);
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        StringBuilder sb = new StringBuilder();
        sb.append("error ");
        sb.append(matrixError);
        Log.e("VersionChecker", sb.toString());
        VersionChecker.INSTANCE.answer(this.$context, this.$callback);
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        Log.e("VersionChecker", "error", exc);
        VersionChecker.INSTANCE.answer(this.$context, this.$callback);
    }
}
