package im.vector.fragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCrypto;
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult;
import org.matrix.androidsdk.data.RoomMediaMessage;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$importKeys$2 implements OnClickListener {
    final /* synthetic */ Context $appContext;
    final /* synthetic */ AlertDialog $importDialog;
    final /* synthetic */ TextInputEditText $passPhraseEditText;
    final /* synthetic */ RoomMediaMessage $sharedDataItem;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$importKeys$2(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, TextInputEditText textInputEditText, Context context, RoomMediaMessage roomMediaMessage, AlertDialog alertDialog) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$passPhraseEditText = textInputEditText;
        this.$appContext = context;
        this.$sharedDataItem = roomMediaMessage;
        this.$importDialog = alertDialog;
    }

    public final void onClick(View view) {
        TextInputEditText textInputEditText = this.$passPhraseEditText;
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText, "passPhraseEditText");
        String valueOf = String.valueOf(textInputEditText.getText());
        Context context = this.$appContext;
        RoomMediaMessage roomMediaMessage = this.$sharedDataItem;
        Intrinsics.checkExpressionValueIsNotNull(roomMediaMessage, "sharedDataItem");
        Resource openResource = ResourceUtils.openResource(context, roomMediaMessage.getUri(), this.$sharedDataItem.getMimeType(this.$appContext));
        try {
            byte[] bArr = new byte[openResource.mContentStream.available()];
            openResource.mContentStream.read(bArr);
            openResource.mContentStream.close();
            this.this$0.displayLoadingView();
            MXCrypto crypto = VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0).getCrypto();
            if (crypto != null) {
                crypto.importRoomKeys(bArr, valueOf, null, new ApiCallback<ImportRoomKeysResult>(this) {
                    final /* synthetic */ VectorSettingsPreferencesFragment$importKeys$2 this$0;

                    {
                        this.this$0 = r1;
                    }

                    public void onSuccess(ImportRoomKeysResult importRoomKeysResult) {
                        this.this$0.this$0.hideLoadingView();
                    }

                    public void onNetworkError(Exception exc) {
                        Intrinsics.checkParameterIsNotNull(exc, "e");
                        Context context = this.this$0.$appContext;
                        Intrinsics.checkExpressionValueIsNotNull(context, "appContext");
                        String localizedMessage = exc.getLocalizedMessage();
                        Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
                        Toast makeText = Toast.makeText(context, localizedMessage, 0);
                        makeText.show();
                        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
                        this.this$0.this$0.hideLoadingView();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        Intrinsics.checkParameterIsNotNull(matrixError, "e");
                        Context context = this.this$0.$appContext;
                        Intrinsics.checkExpressionValueIsNotNull(context, "appContext");
                        String localizedMessage = matrixError.getLocalizedMessage();
                        Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
                        Toast makeText = Toast.makeText(context, localizedMessage, 0);
                        makeText.show();
                        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
                        this.this$0.this$0.hideLoadingView();
                    }

                    public void onUnexpectedError(Exception exc) {
                        Intrinsics.checkParameterIsNotNull(exc, "e");
                        Context context = this.this$0.$appContext;
                        Intrinsics.checkExpressionValueIsNotNull(context, "appContext");
                        String localizedMessage = exc.getLocalizedMessage();
                        Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
                        Toast makeText = Toast.makeText(context, localizedMessage, 0);
                        makeText.show();
                        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
                        this.this$0.this$0.hideLoadingView();
                    }
                });
            }
            this.$importDialog.dismiss();
        } catch (Exception e) {
            try {
                openResource.mContentStream.close();
            } catch (Exception e2) {
                String access$getLOG_TAG$cp = VectorSettingsPreferencesFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## importKeys() : ");
                sb.append(e2.getMessage());
                Log.e(access$getLOG_TAG$cp, sb.toString(), e2);
            }
            Context context2 = this.$appContext;
            Intrinsics.checkExpressionValueIsNotNull(context2, "appContext");
            String localizedMessage = e.getLocalizedMessage();
            Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
            Toast makeText = Toast.makeText(context2, localizedMessage, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }
}
