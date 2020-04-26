package fr.gouv.tchap.version;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;
import butterknife.OnClick;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.TchapLoginActivity;
import fr.gouv.tchap.config.TchapConfiguration;
import fr.gouv.tchap.version.VersionCheckResult.Ok;
import fr.gouv.tchap.version.VersionCheckResult.ShowUpgradeScreen;
import fr.gouv.tchap.version.VersionCheckResult.Unknown;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.util.ExternalApplicationsUtilKt;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.SuccessCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0005¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u0006H\u0014J\u0010\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J\b\u0010\u0010\u001a\u00020\u0006H\u0007J\b\u0010\u0011\u001a\u00020\u0006H\u0007J\u0010\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0013H\u0002¨\u0006\u0014"}, d2 = {"Lfr/gouv/tchap/version/TchapVersionCheckActivity;", "Lim/vector/activity/VectorAppCompatActivity;", "Lorg/matrix/androidsdk/core/callback/SuccessCallback;", "Lfr/gouv/tchap/version/VersionCheckResult;", "()V", "finish", "", "startNext", "", "getLayoutRes", "", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "onResume", "onSuccess", "versionCheckResult", "openApp", "openStore", "updateUi", "Lfr/gouv/tchap/version/VersionCheckResult$ShowUpgradeScreen;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: TchapVersionCheckActivity.kt */
public final class TchapVersionCheckActivity extends VectorAppCompatActivity implements SuccessCallback<VersionCheckResult> {
    private HashMap _$_findViewCache;

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public int getLayoutRes() {
        return R.layout.activity_check_version;
    }

    public NoActionBar getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        VersionChecker.INSTANCE.checkVersion(this, this);
    }

    public void onSuccess(VersionCheckResult versionCheckResult) {
        Intrinsics.checkParameterIsNotNull(versionCheckResult, "versionCheckResult");
        if (versionCheckResult instanceof Ok) {
            finish(true);
        } else if (versionCheckResult instanceof ShowUpgradeScreen) {
            updateUi((ShowUpgradeScreen) versionCheckResult);
        } else if (versionCheckResult instanceof Unknown) {
            finish(true);
        }
    }

    private final void updateUi(ShowUpgradeScreen showUpgradeScreen) {
        VersionChecker.INSTANCE.onUpgradeScreenDisplayed(this, showUpgradeScreen.getForVersionCode());
        ProgressBar progressBar = (ProgressBar) _$_findCachedViewById(im.vector.R.id.checkVersionProgress);
        Intrinsics.checkExpressionValueIsNotNull(progressBar, "checkVersionProgress");
        int i = 8;
        progressBar.setVisibility(8);
        Group group = (Group) _$_findCachedViewById(im.vector.R.id.checkVersionAllViews);
        Intrinsics.checkExpressionValueIsNotNull(group, "checkVersionAllViews");
        group.setVisibility(0);
        TextView textView = (TextView) _$_findCachedViewById(im.vector.R.id.checkVersionText);
        Intrinsics.checkExpressionValueIsNotNull(textView, "checkVersionText");
        textView.setText(showUpgradeScreen.getMessage());
        AppCompatButton appCompatButton = (AppCompatButton) _$_findCachedViewById(im.vector.R.id.checkVersionOpen);
        Intrinsics.checkExpressionValueIsNotNull(appCompatButton, "checkVersionOpen");
        appCompatButton.setVisibility(showUpgradeScreen.getCanOpenApp() ? 0 : 8);
        ((AppCompatButton) _$_findCachedViewById(im.vector.R.id.checkVersionOpen)).setText(showUpgradeScreen.getDisplayOnlyOnce() ? R.string.an_update_is_available_ignore : R.string.an_update_is_available_later);
        AppCompatButton appCompatButton2 = (AppCompatButton) _$_findCachedViewById(im.vector.R.id.checkVersionUpgrade);
        Intrinsics.checkExpressionValueIsNotNull(appCompatButton2, "checkVersionUpgrade");
        View view = appCompatButton2;
        if (TchapConfiguration.INSTANCE.getPackageWhiteList().contains(getPackageName())) {
            i = 0;
        }
        view.setVisibility(i);
    }

    @OnClick({2131296378})
    public final void openStore() {
        Activity activity = this;
        String packageName = getPackageName();
        Intrinsics.checkExpressionValueIsNotNull(packageName, "packageName");
        ExternalApplicationsUtilKt.openPlayStore(activity, packageName);
    }

    @OnClick({2131296375})
    public final void openApp() {
        VersionChecker.INSTANCE.showLater(this);
        finish(true);
    }

    private final void finish(boolean z) {
        finish();
        if (z) {
            startActivity(new Intent(this, TchapLoginActivity.class));
        }
    }
}
