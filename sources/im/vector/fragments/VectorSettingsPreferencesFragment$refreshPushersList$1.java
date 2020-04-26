package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.Preference;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import im.vector.preference.VectorCustomActionEditTextPreference.OnPreferenceLongClickListener;
import im.vector.push.PushManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Pusher;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceLongClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$refreshPushersList$1 implements OnPreferenceLongClickListener {
    final /* synthetic */ PushManager $pushManager;
    final /* synthetic */ Pusher $pusher;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$refreshPushersList$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, PushManager pushManager, Pusher pusher) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$pushManager = pushManager;
        this.$pusher = pusher;
    }

    public final boolean onPreferenceLongClick(Preference preference) {
        new Builder(this.this$0.getActivity()).setTitle((int) R.string.dialog_title_confirmation).setMessage((int) R.string.settings_delete_notification_targets_confirmation).setPositiveButton((int) R.string.remove, (OnClickListener) new OnClickListener(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$refreshPushersList$1 this$0;

            {
                this.this$0 = r1;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                this.this$0.this$0.displayLoadingView();
                this.this$0.$pushManager.unregister(VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0.this$0), this.this$0.$pusher, new ApiCallback<Void>(this) {
                    final /* synthetic */ AnonymousClass1 this$0;

                    {
                        this.this$0 = r1;
                    }

                    public void onSuccess(Void voidR) {
                        this.this$0.this$0.this$0.refreshPushersList();
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
        }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
        return true;
    }
}
