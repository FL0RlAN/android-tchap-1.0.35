package im.vector.fragments;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.rest.DeviceInfo;
import org.matrix.androidsdk.crypto.model.rest.DevicesListResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0014\u0010\u0007\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\u0014\u0010\f\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016¨\u0006\r"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$refreshDevicesList$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/crypto/model/rest/DevicesListResponse;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$refreshDevicesList$1 implements ApiCallback<DevicesListResponse> {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$refreshDevicesList$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public void onSuccess(DevicesListResponse devicesListResponse) {
        Intrinsics.checkParameterIsNotNull(devicesListResponse, "info");
        if (devicesListResponse.devices.isEmpty()) {
            this.this$0.removeDevicesPreference();
            return;
        }
        VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment = this.this$0;
        List<DeviceInfo> list = devicesListResponse.devices;
        Intrinsics.checkExpressionValueIsNotNull(list, "info.devices");
        vectorSettingsPreferencesFragment.buildDevicesSettings(list);
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.this$0.removeDevicesPreference();
        this.this$0.onCommonDone(exc.getMessage());
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        this.this$0.removeDevicesPreference();
        this.this$0.onCommonDone(matrixError.getMessage());
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.this$0.removeDevicesPreference();
        this.this$0.onCommonDone(exc.getMessage());
    }
}
