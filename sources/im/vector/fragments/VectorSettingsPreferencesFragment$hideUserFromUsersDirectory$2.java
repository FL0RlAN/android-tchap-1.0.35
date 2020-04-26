package im.vector.fragments;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0014\u0010\u0007\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0014\u0010\f\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016¨\u0006\r"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$hideUserFromUsersDirectory$2", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$hideUserFromUsersDirectory$2 implements ApiCallback<Void> {
    final /* synthetic */ boolean $hidden;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$hideUserFromUsersDirectory$2(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, boolean z) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$hidden = z;
    }

    public void onSuccess(Void voidR) {
        this.this$0.hideLoadingView();
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        this.this$0.getHideFromUsersDirectoryPreference().setChecked(!this.$hidden);
        this.this$0.hideLoadingView();
        Activity activity = this.this$0.getActivity();
        if (activity != null) {
            Context context = activity;
            String localizedMessage = matrixError.getLocalizedMessage();
            Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
            Toast makeText = Toast.makeText(context, localizedMessage, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.this$0.getHideFromUsersDirectoryPreference().setChecked(!this.$hidden);
        this.this$0.hideLoadingView();
        Activity activity = this.this$0.getActivity();
        if (activity != null) {
            Context context = activity;
            String localizedMessage = exc.getLocalizedMessage();
            Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
            Toast makeText = Toast.makeText(context, localizedMessage, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.this$0.getHideFromUsersDirectoryPreference().setChecked(!this.$hidden);
        this.this$0.hideLoadingView();
        Activity activity = this.this$0.getActivity();
        if (activity != null) {
            Context context = activity;
            String localizedMessage = exc.getLocalizedMessage();
            Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
            Toast makeText = Toast.makeText(context, localizedMessage, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }
}
