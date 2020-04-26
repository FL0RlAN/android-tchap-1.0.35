package im.vector.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import im.vector.preference.BingRulePreference;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.BingRulesManager.onBingRuleUpdateListener;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$8 implements OnPreferenceClickListener {
    final /* synthetic */ Preference $preference;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$8(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, Preference preference) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$preference = preference;
    }

    public final boolean onPreferenceClick(Preference preference) {
        new Builder(this.this$0.getActivity()).setSingleChoiceItems(((BingRulePreference) this.$preference).getBingRuleStatuses(), ((BingRulePreference) this.$preference).getRuleStatusIndex(), (OnClickListener) new OnClickListener(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$onCreate$8 this$0;

            {
                this.this$0 = r1;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                BingRule createRule = ((BingRulePreference) this.this$0.$preference).createRule(i);
                dialogInterface.cancel();
                if (createRule != null) {
                    this.this$0.this$0.displayLoadingView();
                    MXDataHandler dataHandler = VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0.this$0).getDataHandler();
                    Intrinsics.checkExpressionValueIsNotNull(dataHandler, "mSession.dataHandler");
                    dataHandler.getBingRulesManager().updateRule(((BingRulePreference) this.this$0.$preference).getRule(), createRule, new onBingRuleUpdateListener(this) {
                        final /* synthetic */ AnonymousClass1 this$0;

                        {
                            this.this$0 = r1;
                        }

                        private final void onDone() {
                            this.this$0.this$0.this$0.refreshDisplay();
                            this.this$0.this$0.this$0.hideLoadingView();
                        }

                        public void onBingRuleUpdateSuccess() {
                            onDone();
                        }

                        public void onBingRuleUpdateFailure(String str) {
                            Intrinsics.checkParameterIsNotNull(str, "errorMessage");
                            Activity activity = this.this$0.this$0.this$0.getActivity();
                            if (activity != null) {
                                Toast makeText = Toast.makeText(activity, str, 0);
                                makeText.show();
                                Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
                            }
                            onDone();
                        }
                    });
                }
            }
        }).show();
        return true;
    }
}
