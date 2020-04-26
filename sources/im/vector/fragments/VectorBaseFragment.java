package im.vector.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import im.vector.activity.VectorAppCompatActivity;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.Log.EventTag;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\b\u0010\u000f\u001a\u00020\fH\u0017J\b\u0010\u0010\u001a\u00020\fH\u0016J\b\u0010\u0011\u001a\u00020\fH\u0017J\u001a\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0017R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0017"}, d2 = {"Lim/vector/fragments/VectorBaseFragment;", "Landroidx/fragment/app/Fragment;", "()V", "mUnBinder", "Lbutterknife/Unbinder;", "vectorActivity", "Lim/vector/activity/VectorAppCompatActivity;", "getVectorActivity", "()Lim/vector/activity/VectorAppCompatActivity;", "setVectorActivity", "(Lim/vector/activity/VectorAppCompatActivity;)V", "onAttach", "", "context", "Landroid/content/Context;", "onDestroyView", "onDetach", "onResume", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorBaseFragment.kt */
public class VectorBaseFragment extends Fragment {
    private HashMap _$_findViewCache;
    private Unbinder mUnBinder;
    private VectorAppCompatActivity vectorActivity;

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
        if (view == null) {
            View view2 = getView();
            if (view2 == null) {
                return null;
            }
            view = view2.findViewById(i);
            this._$_findViewCache.put(Integer.valueOf(i), view);
        }
        return view;
    }

    /* access modifiers changed from: protected */
    public final VectorAppCompatActivity getVectorActivity() {
        return this.vectorActivity;
    }

    /* access modifiers changed from: protected */
    public final void setVectorActivity(VectorAppCompatActivity vectorAppCompatActivity) {
        this.vectorActivity = vectorAppCompatActivity;
    }

    public void onResume() {
        super.onResume();
        EventTag eventTag = EventTag.NAVIGATION;
        StringBuilder sb = new StringBuilder();
        sb.append("onResume Fragment ");
        sb.append(getClass().getSimpleName());
        Log.event(eventTag, sb.toString());
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        this.mUnBinder = ButterKnife.bind((Object) this, view);
    }

    public void onDestroyView() {
        super.onDestroyView();
        Unbinder unbinder = this.mUnBinder;
        if (unbinder != null) {
            unbinder.unbind();
        }
        this.mUnBinder = null;
        _$_clearFindViewByIdCache();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            this.vectorActivity = (VectorAppCompatActivity) context;
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type im.vector.activity.VectorAppCompatActivity");
    }

    public void onDetach() {
        super.onDetach();
        this.vectorActivity = null;
    }
}
