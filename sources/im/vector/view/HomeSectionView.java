package im.vector.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Filter.FilterListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.gouv.tchap.a.R;
import im.vector.adapters.AbsAdapter.MoreRoomActionListener;
import im.vector.adapters.AbsAdapter.RoomInvitationListener;
import im.vector.adapters.HomeRoomAdapter;
import im.vector.adapters.HomeRoomAdapter.OnSelectRoomListener;
import im.vector.fragments.AbsHomeFragment.OnFilterListener;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.RoomUtils;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;

public class HomeSectionView extends RelativeLayout {
    private static final String LOG_TAG = HomeSectionView.class.getSimpleName();
    private HomeRoomAdapter mAdapter;
    @BindView(2131297011)
    TextView mBadge;
    private String mCurrentFilter;
    @BindView(2131297012)
    TextView mHeader;
    private boolean mHideIfEmpty;
    private String mNoItemPlaceholder;
    private String mNoResultPlaceholder;
    @BindView(2131297015)
    TextView mPlaceHolder;
    @BindView(2131297016)
    RecyclerView mRecyclerView;

    public HomeSectionView(Context context) {
        super(context);
        setup();
    }

    public HomeSectionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setup();
    }

    public HomeSectionView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setup();
    }

    private HomeSectionView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setup();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAdapter = null;
    }

    private void setup() {
        inflate(getContext(), R.layout.home_section_view, this);
        ButterKnife.bind((View) this);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadius(100.0f);
        gradientDrawable.setColor(ThemeUtils.INSTANCE.getColor(getContext(), R.attr.vctr_activity_bottom_gradient_color));
        this.mBadge.setBackground(gradientDrawable);
        this.mHeader.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (HomeSectionView.this.mRecyclerView != null) {
                    HomeSectionView.this.mRecyclerView.stopScroll();
                    HomeSectionView.this.mRecyclerView.scrollToPosition(0);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onDataUpdated() {
        HomeRoomAdapter homeRoomAdapter = this.mAdapter;
        if (homeRoomAdapter != null) {
            try {
                boolean isEmpty = homeRoomAdapter.isEmpty();
                boolean hasNoResult = this.mAdapter.hasNoResult();
                int badgeCount = this.mAdapter.getBadgeCount();
                int i = 8;
                setVisibility((!this.mHideIfEmpty || !isEmpty) ? 0 : 8);
                this.mBadge.setText(RoomUtils.formatUnreadMessagesCounter(badgeCount));
                this.mBadge.setVisibility(badgeCount == 0 ? 8 : 0);
                this.mRecyclerView.setVisibility(hasNoResult ? 8 : 0);
                TextView textView = this.mPlaceHolder;
                if (hasNoResult) {
                    i = 0;
                }
                textView.setVisibility(i);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onDataUpdated() failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    public void setTitle(int i) {
        this.mHeader.setText(i);
    }

    public void setPlaceholders(String str, String str2) {
        this.mNoItemPlaceholder = str;
        this.mNoResultPlaceholder = str2;
        this.mPlaceHolder.setText(TextUtils.isEmpty(this.mCurrentFilter) ? this.mNoItemPlaceholder : this.mNoResultPlaceholder);
    }

    public void setHideIfEmpty(boolean z) {
        int i;
        this.mHideIfEmpty = z;
        if (this.mHideIfEmpty) {
            HomeRoomAdapter homeRoomAdapter = this.mAdapter;
            if (homeRoomAdapter == null || homeRoomAdapter.isEmpty()) {
                i = 8;
                setVisibility(i);
            }
        }
        i = 0;
        setVisibility(i);
    }

    public void setupRoomRecyclerView(LayoutManager layoutManager, int i, boolean z, OnSelectRoomListener onSelectRoomListener, RoomInvitationListener roomInvitationListener, MoreRoomActionListener moreRoomActionListener) {
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setNestedScrollingEnabled(z);
        HomeRoomAdapter homeRoomAdapter = new HomeRoomAdapter(getContext(), i, onSelectRoomListener, roomInvitationListener, moreRoomActionListener);
        this.mAdapter = homeRoomAdapter;
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                super.onChanged();
                HomeSectionView.this.onDataUpdated();
            }
        });
    }

    public void onFilter(final String str, final OnFilterListener onFilterListener) {
        this.mAdapter.getFilter().filter(str, new FilterListener() {
            public void onFilterComplete(int i) {
                OnFilterListener onFilterListener = onFilterListener;
                if (onFilterListener != null) {
                    onFilterListener.onFilterDone(i);
                }
                HomeSectionView.this.setCurrentFilter(str);
                HomeSectionView.this.mRecyclerView.getLayoutManager().scrollToPosition(0);
                HomeSectionView.this.onDataUpdated();
            }
        });
    }

    public void setCurrentFilter(String str) {
        HomeRoomAdapter homeRoomAdapter = this.mAdapter;
        if (homeRoomAdapter != null) {
            this.mCurrentFilter = str;
            homeRoomAdapter.onFilterDone(this.mCurrentFilter);
            this.mPlaceHolder.setText(TextUtils.isEmpty(this.mCurrentFilter) ? this.mNoItemPlaceholder : this.mNoResultPlaceholder);
        }
    }

    public void setRooms(List<Room> list) {
        HomeRoomAdapter homeRoomAdapter = this.mAdapter;
        if (homeRoomAdapter != null) {
            homeRoomAdapter.setRooms(list);
        }
    }

    public void scrollToPosition(int i) {
        this.mRecyclerView.scrollToPosition(i);
    }
}
