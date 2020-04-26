package im.vector.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.format.Formatter;
import com.bumptech.glide.Glide;
import fr.gouv.tchap.media.MediaScanManager;
import im.vector.activity.VectorAppCompatActivity;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.db.MXMediaCache;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceClick", "im/vector/fragments/VectorSettingsPreferencesFragment$onCreate$13$2"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$6 implements OnPreferenceClickListener {
    final /* synthetic */ Preference $it;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$6(Preference preference, VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.$it = preference;
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public final boolean onPreferenceClick(Preference preference) {
        this.this$0.displayLoadingView();
        AnonymousClass1 r6 = new AsyncTask<Void, Void, Void>(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$6 this$0;

            {
                this.this$0 = r1;
            }

            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                Intrinsics.checkParameterIsNotNull(voidArr, "params");
                VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0.this$0).getMediaCache().clear();
                Glide.get(this.this$0.this$0.getActivity()).clearDiskCache();
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                this.this$0.this$0.hideLoadingView();
                MXMediaCache.getCachesSize(this.this$0.this$0.getActivity(), new SimpleApiCallback<Long>(this) {
                    final /* synthetic */ AnonymousClass1 this$0;

                    {
                        this.this$0 = r1;
                    }

                    public /* bridge */ /* synthetic */ void onSuccess(Object obj) {
                        onSuccess(((Number) obj).longValue());
                    }

                    public void onSuccess(long j) {
                        Preference preference = this.this$0.this$0.$it;
                        Intrinsics.checkExpressionValueIsNotNull(preference, "it");
                        preference.setSummary(Formatter.formatFileSize(this.this$0.this$0.this$0.getActivity(), j));
                    }
                });
            }
        };
        try {
            r6.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            if (this.this$0.getActivity() instanceof VectorAppCompatActivity) {
                Activity activity = this.this$0.getActivity();
                if (activity != null) {
                    new MediaScanManager(VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0).getMediaScanRestClient(), ((VectorAppCompatActivity) activity).getRealm()).clearAntiVirusScanResults();
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type im.vector.activity.VectorAppCompatActivity");
                }
            }
        } catch (Exception e) {
            String access$getLOG_TAG$cp = VectorSettingsPreferencesFragment.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## mSession.getMediaCache().clear() failed ");
            sb.append(e.getMessage());
            Log.e(access$getLOG_TAG$cp, sb.toString(), e);
            r6.cancel(true);
            this.this$0.hideLoadingView();
        }
        return false;
    }
}
