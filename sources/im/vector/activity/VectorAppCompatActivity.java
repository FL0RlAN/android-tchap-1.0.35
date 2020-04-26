package im.vector.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewKt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.gouv.tchap.a.R;
import im.vector.VectorApp;
import im.vector.activity.interfaces.Restorable;
import im.vector.dialogs.ConsentNotGivenHelper;
import im.vector.receiver.DebugReceiver;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.Default;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.AssetReader;
import io.realm.Realm;
import java.util.HashMap;
import java.util.HashSet;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.Log.EventTag;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u0013H\u0004J\b\u0010)\u001a\u00020*H\u0004J\u0010\u0010+\u001a\u00020*2\u0006\u0010,\u001a\u00020-H\u0014J\b\u0010.\u001a\u00020*H\u0004J\b\u0010/\u001a\u00020'H\u0016J\b\u00100\u001a\u00020*H\u0016J\b\u00101\u001a\u000202H'J\b\u00103\u001a\u000202H\u0017J\b\u00104\u001a\u000202H\u0017J\b\u00105\u001a\u000206H\u0016J\b\u00107\u001a\u00020\u0016H\u0004J\b\u00108\u001a\u000202H\u0017J\u0006\u00109\u001a\u00020*J\b\u0010:\u001a\u00020*H\u0016J\b\u0010;\u001a\u00020'H\u0004J\u0006\u0010<\u001a\u00020'J\u0012\u0010=\u001a\u00020*2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0004J\u000e\u0010>\u001a\u00020'2\u0006\u0010?\u001a\u00020@J\b\u0010A\u001a\u00020*H\u0014J\b\u0010B\u001a\u00020*H\u0017J\u0010\u0010C\u001a\u00020'2\u0006\u0010D\u001a\u00020EH\u0016J\b\u0010F\u001a\u00020*H\u0014J\b\u0010G\u001a\u00020*H\u0015J\u0010\u0010H\u001a\u00020*2\u0006\u0010I\u001a\u00020\u0016H\u0015J\u0010\u0010J\u001a\u00020*2\u0006\u0010K\u001a\u00020'H\u0016J\b\u0010L\u001a\u00020*H\u0002J\u0006\u0010M\u001a\u00020*R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX.¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\u00130\u0012j\b\u0012\u0004\u0012\u00020\u0013`\u0014X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u00020\u00188\u0004@\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u000e¢\u0006\u0002\n\u0000R(\u0010!\u001a\u0004\u0018\u00010 2\b\u0010\u001f\u001a\u0004\u0018\u00010 @FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%¨\u0006N"}, d2 = {"Lim/vector/activity/VectorAppCompatActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "consentNotGivenHelper", "Lim/vector/dialogs/ConsentNotGivenHelper;", "getConsentNotGivenHelper", "()Lim/vector/dialogs/ConsentNotGivenHelper;", "consentNotGivenHelper$delegate", "Lkotlin/Lazy;", "debugReceiver", "Lim/vector/receiver/DebugReceiver;", "realm", "Lio/realm/Realm;", "getRealm", "()Lio/realm/Realm;", "setRealm", "(Lio/realm/Realm;)V", "restorables", "Ljava/util/HashSet;", "Lim/vector/activity/interfaces/Restorable;", "Lkotlin/collections/HashSet;", "savedInstanceState", "Landroid/os/Bundle;", "toolbar", "Landroidx/appcompat/widget/Toolbar;", "getToolbar", "()Landroidx/appcompat/widget/Toolbar;", "setToolbar", "(Landroidx/appcompat/widget/Toolbar;)V", "unBinder", "Lbutterknife/Unbinder;", "value", "Landroid/view/View;", "waitingView", "getWaitingView", "()Landroid/view/View;", "setWaitingView", "(Landroid/view/View;)V", "addToRestorables", "", "restorable", "applyScreenshotSecurity", "", "attachBaseContext", "base", "Landroid/content/Context;", "configureToolbar", "displayInFullscreen", "doBeforeSetContentView", "getLayoutRes", "", "getMenuRes", "getMenuTint", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes;", "getSavedInstanceState", "getTitleRes", "hideWaitingView", "initUiAndData", "isFirstCreation", "isWaitingViewVisible", "onCreate", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "onDestroy", "onLowMemory", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onPause", "onResume", "onSaveInstanceState", "outState", "onWindowFocusChanged", "hasFocus", "setFullScreen", "showWaitingView", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorAppCompatActivity.kt */
public abstract class VectorAppCompatActivity extends AppCompatActivity {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(VectorAppCompatActivity.class), "consentNotGivenHelper", "getConsentNotGivenHelper()Lim/vector/dialogs/ConsentNotGivenHelper;"))};
    private HashMap _$_findViewCache;
    private final Lazy consentNotGivenHelper$delegate = LazyKt.lazy(new VectorAppCompatActivity$consentNotGivenHelper$2(this));
    private DebugReceiver debugReceiver;
    public Realm realm;
    private final HashSet<Restorable> restorables = new HashSet<>();
    /* access modifiers changed from: private */
    public Bundle savedInstanceState;
    @BindView(2131297117)
    protected Toolbar toolbar;
    private Unbinder unBinder;
    private View waitingView;

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

    public boolean displayInFullscreen() {
        return false;
    }

    public void doBeforeSetContentView() {
    }

    public final ConsentNotGivenHelper getConsentNotGivenHelper() {
        Lazy lazy = this.consentNotGivenHelper$delegate;
        KProperty kProperty = $$delegatedProperties[0];
        return (ConsentNotGivenHelper) lazy.getValue();
    }

    public abstract int getLayoutRes();

    public int getMenuRes() {
        return -1;
    }

    public int getMenuTint() {
        return R.attr.vctr_icon_tint_on_dark_action_bar_color;
    }

    public int getTitleRes() {
        return -1;
    }

    public void initUiAndData() {
    }

    public final Realm getRealm() {
        Realm realm2 = this.realm;
        if (realm2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realm");
        }
        return realm2;
    }

    public final void setRealm(Realm realm2) {
        Intrinsics.checkParameterIsNotNull(realm2, "<set-?>");
        this.realm = realm2;
    }

    /* access modifiers changed from: protected */
    public final Toolbar getToolbar() {
        Toolbar toolbar2 = this.toolbar;
        if (toolbar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("toolbar");
        }
        return toolbar2;
    }

    /* access modifiers changed from: protected */
    public final void setToolbar(Toolbar toolbar2) {
        Intrinsics.checkParameterIsNotNull(toolbar2, "<set-?>");
        this.toolbar = toolbar2;
    }

    public void onLowMemory() {
        super.onLowMemory();
        AssetReader.INSTANCE.clearCache();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "base");
        super.attachBaseContext(VectorApp.getLocalisedContext(context));
    }

    /* access modifiers changed from: protected */
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Realm defaultInstance = Realm.getDefaultInstance();
        Intrinsics.checkExpressionValueIsNotNull(defaultInstance, "Realm.getDefaultInstance()");
        this.realm = defaultInstance;
        Activity activity = this;
        ThemeUtils.INSTANCE.setActivityTheme(activity, getOtherThemes());
        doBeforeSetContentView();
        setContentView(getLayoutRes());
        this.unBinder = ButterKnife.bind(activity);
        this.savedInstanceState = bundle;
        initUiAndData();
        int titleRes = getTitleRes();
        if (titleRes != -1) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(titleRes);
            } else {
                setTitle(titleRes);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Realm realm2 = this.realm;
        if (realm2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realm");
        }
        realm2.close();
        Unbinder unbinder = this.unBinder;
        if (unbinder != null) {
            unbinder.unbind();
        }
        this.unBinder = null;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (displayInFullscreen()) {
            setFullScreen();
        }
        EventTag eventTag = EventTag.NAVIGATION;
        StringBuilder sb = new StringBuilder();
        sb.append("onResume Activity ");
        sb.append(getClass().getSimpleName());
        Log.event(eventTag, sb.toString());
        DebugReceiver.Companion.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        DebugReceiver debugReceiver2 = this.debugReceiver;
        if (debugReceiver2 != null) {
            unregisterReceiver(debugReceiver2);
            this.debugReceiver = null;
        }
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z && displayInFullscreen()) {
            setFullScreen();
        }
    }

    public final boolean onCreateOptionsMenu(Menu menu) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        int menuRes = getMenuRes();
        if (menuRes == -1) {
            return super.onCreateOptionsMenu(menu);
        }
        getMenuInflater().inflate(menuRes, menu);
        ThemeUtils.INSTANCE.tintMenuIcons(menu, ThemeUtils.INSTANCE.getColor(this, getMenuTint()));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intrinsics.checkParameterIsNotNull(menuItem, "item");
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        setResult(0);
        finish();
        return true;
    }

    public ActivityOtherThemes getOtherThemes() {
        return Default.INSTANCE;
    }

    public final View getWaitingView() {
        return this.waitingView;
    }

    public final void setWaitingView(View view) {
        this.waitingView = view;
        if (view != null) {
            view.setClickable(true);
        }
    }

    public final boolean isWaitingViewVisible() {
        View view = this.waitingView;
        if (view == null) {
            return false;
        }
        return view.getVisibility() == 0;
    }

    public final void showWaitingView() {
        View view = this.waitingView;
        if (view != null) {
            ViewKt.setVisible(view, true);
        }
    }

    public final void hideWaitingView() {
        View view = this.waitingView;
        if (view != null) {
            ViewKt.setVisible(view, false);
        }
    }

    /* access modifiers changed from: protected */
    public final Bundle getSavedInstanceState() {
        Bundle bundle = this.savedInstanceState;
        if (bundle == null) {
            Intrinsics.throwNpe();
        }
        return bundle;
    }

    /* access modifiers changed from: protected */
    public final boolean isFirstCreation() {
        return this.savedInstanceState == null;
    }

    /* access modifiers changed from: protected */
    public final void configureToolbar() {
        Toolbar toolbar2 = this.toolbar;
        if (toolbar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("toolbar");
        }
        setSupportActionBar(toolbar2);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /* access modifiers changed from: protected */
    public final void applyScreenshotSecurity() {
        getWindow().addFlags(8192);
    }

    private final void setFullScreen() {
        Window window = getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window, "window");
        View decorView = window.getDecorView();
        Intrinsics.checkExpressionValueIsNotNull(decorView, "window.decorView");
        decorView.setSystemUiVisibility(5894);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(bundle, "outState");
        super.onSaveInstanceState(bundle);
        for (Restorable saveState : this.restorables) {
            saveState.saveState(bundle);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean addToRestorables(Restorable restorable) {
        Intrinsics.checkParameterIsNotNull(restorable, "restorable");
        return this.restorables.add(restorable);
    }
}
