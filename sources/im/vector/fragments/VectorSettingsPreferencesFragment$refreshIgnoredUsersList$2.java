package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$refreshIgnoredUsersList$2 implements OnPreferenceClickListener {
    final /* synthetic */ String $displayName;
    final /* synthetic */ String $userId;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$refreshIgnoredUsersList$2(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, String str, String str2) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$displayName = str;
        this.$userId = str2;
    }

    public final boolean onPreferenceClick(Preference preference) {
        new Builder(this.this$0.getActivity()).setMessage((CharSequence) this.this$0.getString(R.string.settings_unignore_user, new Object[]{this.$displayName})).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$refreshIgnoredUsersList$2 this$0;

            {
                this.this$0 = r1;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                this.this$0.this$0.displayLoadingView();
                ArrayList arrayList = new ArrayList();
                arrayList.add(this.this$0.$userId);
                VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0.this$0).unIgnoreUsers(arrayList, new ApiCallback<Void>(this) {
                    final /* synthetic */ AnonymousClass1 this$0;

                    {
                        this.this$0 = r1;
                    }

                    public void onSuccess(Void voidR) {
                        this.this$0.this$0.this$0.onCommonDone(null);
                    }

                    public void onNetworkError(Exception exc) {
                        Intrinsics.checkParameterIsNotNull(exc, "e");
                        this.this$0.this$0.this$0.onCommonDone(exc.getLocalizedMessage());
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        Intrinsics.checkParameterIsNotNull(matrixError, "e");
                        this.this$0.this$0.this$0.onCommonDone(matrixError.getLocalizedMessage());
                    }

                    public void onUnexpectedError(Exception exc) {
                        Intrinsics.checkParameterIsNotNull(exc, "e");
                        this.this$0.this$0.this$0.onCommonDone(exc.getLocalizedMessage());
                    }
                });
            }
        }).setNegativeButton((int) R.string.no, (OnClickListener) null).show();
        return false;
    }
}
