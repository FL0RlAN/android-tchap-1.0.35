package im.vector.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.fragments.VectorSettingsPreferencesFragment;
import im.vector.util.PermissionsToolsKt;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J+\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u00062\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016¢\u0006\u0002\u0010\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lim/vector/activity/VectorSettingsActivity;", "Lim/vector/activity/MXCActionBarActivity;", "()V", "vectorSettingsPreferencesFragment", "Lim/vector/fragments/VectorSettingsPreferencesFragment;", "getLayoutRes", "", "getTitleRes", "initUiAndData", "", "onRequestPermissionsResult", "requestCode", "permissions", "", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsActivity.kt */
public final class VectorSettingsActivity extends MXCActionBarActivity {
    public static final Companion Companion = new Companion(null);
    private static final String FRAGMENT_TAG = "VectorSettingsPreferencesFragment";
    private HashMap _$_findViewCache;
    private VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lim/vector/activity/VectorSettingsActivity$Companion;", "", "()V", "FRAGMENT_TAG", "", "getIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "userId", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorSettingsActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final Intent getIntent(Context context, String str) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, "userId");
            Intent intent = new Intent(context, VectorSettingsActivity.class);
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", str);
            return intent;
        }
    }

    @JvmStatic
    public static final Intent getIntent(Context context, String str) {
        return Companion.getIntent(context, str);
    }

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
        return R.layout.activity_vector_settings;
    }

    public int getTitleRes() {
        return R.string.title_activity_settings;
    }

    public void initUiAndData() {
        MXSession session = getSession(getIntent());
        if (session == null) {
            Matrix instance = Matrix.getInstance(this);
            Intrinsics.checkExpressionValueIsNotNull(instance, "Matrix.getInstance(this)");
            session = instance.getDefaultSession();
        }
        if (session == null) {
            finish();
            return;
        }
        boolean isFirstCreation = isFirstCreation();
        String str = FRAGMENT_TAG;
        if (isFirstCreation) {
            im.vector.fragments.VectorSettingsPreferencesFragment.Companion companion = VectorSettingsPreferencesFragment.Companion;
            String myUserId = session.getMyUserId();
            Intrinsics.checkExpressionValueIsNotNull(myUserId, "session.myUserId");
            this.vectorSettingsPreferencesFragment = companion.newInstance(myUserId);
            FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
            VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment2 = this.vectorSettingsPreferencesFragment;
            if (vectorSettingsPreferencesFragment2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("vectorSettingsPreferencesFragment");
            }
            beginTransaction.replace(R.id.vector_settings_page, vectorSettingsPreferencesFragment2, str).commit();
        } else {
            Fragment findFragmentByTag = getFragmentManager().findFragmentByTag(str);
            if (findFragmentByTag != null) {
                this.vectorSettingsPreferencesFragment = (VectorSettingsPreferencesFragment) findFragmentByTag;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type im.vector.fragments.VectorSettingsPreferencesFragment");
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "permissions");
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        if (PermissionsToolsKt.allGranted(iArr) && i == 573) {
            VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment2 = this.vectorSettingsPreferencesFragment;
            if (vectorSettingsPreferencesFragment2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("vectorSettingsPreferencesFragment");
            }
            vectorSettingsPreferencesFragment2.exportKeys();
        }
    }
}
