package im.vector.fragments.base;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.DialogFragment;
import java.util.HashMap;
import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u0000 \u000f*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0001\u000fB\u0005¢\u0006\u0002\u0010\u0003J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J\b\u0010\u000e\u001a\u00020\u000bH\u0016R\u001e\u0010\u0004\u001a\u0004\u0018\u00018\u0000X\u000e¢\u0006\u0010\n\u0002\u0010\t\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\u0010"}, d2 = {"Lim/vector/fragments/base/VectorBaseDialogFragment;", "LISTENER", "Landroidx/fragment/app/DialogFragment;", "()V", "listener", "getListener", "()Ljava/lang/Object;", "setListener", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "onAttach", "", "context", "Landroid/content/Context;", "onDetach", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorBaseDialogFragment.kt */
public abstract class VectorBaseDialogFragment<LISTENER> extends DialogFragment {
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = VectorBaseDialogFragment.class.getSimpleName();
    private HashMap _$_findViewCache;
    private LISTENER listener;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lim/vector/fragments/base/VectorBaseDialogFragment$Companion;", "", "()V", "LOG_TAG", "", "kotlin.jvm.PlatformType", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorBaseDialogFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
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

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    /* access modifiers changed from: protected */
    public final LISTENER getListener() {
        return this.listener;
    }

    /* access modifiers changed from: protected */
    public final void setListener(LISTENER listener2) {
        this.listener = listener2;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.listener = context;
        } catch (ClassCastException unused) {
            Log.w(LOG_TAG, "Parent Activity should implement the LISTENER interface");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
