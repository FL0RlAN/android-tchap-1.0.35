package im.vector.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import im.vector.Matrix;
import im.vector.activity.VectorGroupDetailsActivity;
import org.matrix.androidsdk.MXSession;

public abstract class GroupDetailsBaseFragment extends VectorBaseFragment {
    private static final String CURRENT_FILTER = "CURRENT_FILTER";
    private static final String LOG_TAG = GroupDetailsBaseFragment.class.getSimpleName();
    protected VectorGroupDetailsActivity mActivity;
    protected MXSession mSession;

    /* access modifiers changed from: protected */
    public abstract void initViews();

    public abstract void refreshViews();

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mSession = Matrix.getInstance(getContext()).getDefaultSession();
        this.mActivity = (VectorGroupDetailsActivity) getActivity();
        initViews();
    }

    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    public void onResume() {
        super.onResume();
        VectorGroupDetailsActivity vectorGroupDetailsActivity = this.mActivity;
        if (vectorGroupDetailsActivity != null) {
            View currentFocus = vectorGroupDetailsActivity.getCurrentFocus();
            if (currentFocus != null) {
                ((InputMethodManager) this.mActivity.getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }
}
