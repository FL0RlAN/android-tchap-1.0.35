package im.vector.adapters;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import im.vector.Matrix;
import im.vector.adapters.AbsAdapter.GroupInvitationListener;
import im.vector.adapters.AbsAdapter.MoreGroupActionListener;
import im.vector.adapters.AbsAdapter.MoreRoomActionListener;
import im.vector.adapters.AbsAdapter.RoomInvitationListener;
import org.matrix.androidsdk.MXSession;

public abstract class AbsFilterableAdapter<T extends ViewHolder> extends Adapter<T> implements Filterable {
    /* access modifiers changed from: protected */
    public final Context mContext;
    protected CharSequence mCurrentFilterPattern;
    private final Filter mFilter = createFilter();
    protected GroupInvitationListener mGroupInvitationListener;
    protected MoreGroupActionListener mMoreGroupActionListener;
    protected MoreRoomActionListener mMoreRoomActionListener;
    protected RoomInvitationListener mRoomInvitationListener;
    /* access modifiers changed from: protected */
    public final MXSession mSession;

    /* access modifiers changed from: protected */
    public abstract Filter createFilter();

    AbsFilterableAdapter(Context context) {
        this.mContext = context;
        this.mSession = Matrix.getInstance(context).getDefaultSession();
    }

    AbsFilterableAdapter(Context context, RoomInvitationListener roomInvitationListener, MoreRoomActionListener moreRoomActionListener) {
        this.mContext = context;
        this.mRoomInvitationListener = roomInvitationListener;
        this.mMoreRoomActionListener = moreRoomActionListener;
        this.mSession = Matrix.getInstance(context).getDefaultSession();
    }

    AbsFilterableAdapter(Context context, GroupInvitationListener groupInvitationListener, MoreGroupActionListener moreGroupActionListener) {
        this.mContext = context;
        this.mGroupInvitationListener = groupInvitationListener;
        this.mMoreGroupActionListener = moreGroupActionListener;
        this.mSession = Matrix.getInstance(context).getDefaultSession();
    }

    public Filter getFilter() {
        return this.mFilter;
    }

    public void onFilterDone(CharSequence charSequence) {
        this.mCurrentFilterPattern = charSequence;
    }
}
