package im.vector.view;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class HomeSectionView_ViewBinding implements Unbinder {
    private HomeSectionView target;

    public HomeSectionView_ViewBinding(HomeSectionView homeSectionView) {
        this(homeSectionView, homeSectionView);
    }

    public HomeSectionView_ViewBinding(HomeSectionView homeSectionView, View view) {
        this.target = homeSectionView;
        homeSectionView.mHeader = (TextView) Utils.findRequiredViewAsType(view, R.id.section_header, "field 'mHeader'", TextView.class);
        homeSectionView.mBadge = (TextView) Utils.findRequiredViewAsType(view, R.id.section_badge, "field 'mBadge'", TextView.class);
        homeSectionView.mRecyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.section_recycler_view, "field 'mRecyclerView'", RecyclerView.class);
        homeSectionView.mPlaceHolder = (TextView) Utils.findRequiredViewAsType(view, R.id.section_placeholder, "field 'mPlaceHolder'", TextView.class);
    }

    public void unbind() {
        HomeSectionView homeSectionView = this.target;
        if (homeSectionView != null) {
            this.target = null;
            homeSectionView.mHeader = null;
            homeSectionView.mBadge = null;
            homeSectionView.mRecyclerView = null;
            homeSectionView.mPlaceHolder = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
