package im.vector.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import im.vector.VectorApp;
import im.vector.util.PreferencesManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.listeners.MXEventListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016¨\u0006\b"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$mEventsListener$1", "Lorg/matrix/androidsdk/listeners/MXEventListener;", "onAccountDataUpdated", "", "onAccountInfoUpdate", "myUser", "Lorg/matrix/androidsdk/data/MyUser;", "onBingRulesUpdate", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$mEventsListener$1 extends MXEventListener {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$mEventsListener$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public void onBingRulesUpdate() {
        this.this$0.refreshPreferences();
        this.this$0.refreshDisplay();
    }

    public void onAccountInfoUpdate(MyUser myUser) {
        Intrinsics.checkParameterIsNotNull(myUser, "myUser");
        VectorApp instance = VectorApp.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(instance, "VectorApp.getInstance()");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(instance.getApplicationContext());
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…nce().applicationContext)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putString(PreferencesManager.SETTINGS_DISPLAY_NAME_PREFERENCE_KEY, myUser.displayname);
        edit.apply();
        this.this$0.refreshDisplay();
    }

    public void onAccountDataUpdated() {
        this.this$0.refreshUsersDirectoryVisibility();
    }
}
