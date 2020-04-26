package fr.gouv.tchap.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.adapters.AbsAdapter;
import im.vector.adapters.AbsAdapter.MoreRoomActionListener;
import im.vector.adapters.AbsAdapter.RoomInvitationListener;
import im.vector.adapters.AdapterSection;
import im.vector.adapters.RoomViewHolder;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.data.Room;

public class TchapRoomAdapter extends AbsAdapter {
    private static final String LOG_TAG = TchapRoomAdapter.class.getSimpleName();
    /* access modifiers changed from: private */
    public final OnSelectItemListener mListener;
    private final AdapterSection<Room> mRoomsSection;

    public interface OnSelectItemListener {
        void onSelectItem(Room room, int i);
    }

    class PublicRoomViewHolder extends ViewHolder {
        @BindView(2131296870)
        ImageView vPublicRoomAvatar;
        @BindView(2131296872)
        TextView vPublicRoomsMemberCountTextView;
        @BindView(2131296874)
        TextView vRoomTopic;

        private PublicRoomViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }

    public class PublicRoomViewHolder_ViewBinding implements Unbinder {
        private PublicRoomViewHolder target;

        public PublicRoomViewHolder_ViewBinding(PublicRoomViewHolder publicRoomViewHolder, View view) {
            this.target = publicRoomViewHolder;
            publicRoomViewHolder.vPublicRoomAvatar = (ImageView) Utils.findRequiredViewAsType(view, R.id.public_room_avatar, "field 'vPublicRoomAvatar'", ImageView.class);
            publicRoomViewHolder.vRoomTopic = (TextView) Utils.findRequiredViewAsType(view, R.id.public_room_topic, "field 'vRoomTopic'", TextView.class);
            publicRoomViewHolder.vPublicRoomsMemberCountTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.public_room_members_count, "field 'vPublicRoomsMemberCountTextView'", TextView.class);
        }

        public void unbind() {
            PublicRoomViewHolder publicRoomViewHolder = this.target;
            if (publicRoomViewHolder != null) {
                this.target = null;
                publicRoomViewHolder.vPublicRoomAvatar = null;
                publicRoomViewHolder.vRoomTopic = null;
                publicRoomViewHolder.vPublicRoomsMemberCountTextView = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    public TchapRoomAdapter(Context context, OnSelectItemListener onSelectItemListener, RoomInvitationListener roomInvitationListener, MoreRoomActionListener moreRoomActionListener) {
        super(context, roomInvitationListener, moreRoomActionListener);
        this.mListener = onSelectItemListener;
        AnonymousClass1 r0 = new AdapterSection<Room>(context, context.getString(R.string.rooms_header), -1, R.layout.adapter_item_room_view, -2, -1, new ArrayList(), DinsicUtils.getRoomsComparator(this.mSession, false)) {
            public int getContentViewType(int i) {
                int i2 = i - 1;
                if (i2 < getFilteredItems().size()) {
                    return ((Room) getFilteredItems().get(i2)).isDirect() ? -7 : -4;
                }
                return -1;
            }
        };
        this.mRoomsSection = r0;
        this.mRoomsSection.setEmptyViewPlaceholder(context.getString(R.string.no_room_placeholder), context.getString(R.string.no_result_placeholder));
        addSection(this.mRoomsSection);
    }

    /* access modifiers changed from: protected */
    public ViewHolder createSubViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == -7) {
            return new RoomViewHolder(from.inflate(R.layout.adapter_item_direct_room_view, viewGroup, false));
        }
        if (i != -4) {
            return null;
        }
        return new RoomViewHolder(from.inflate(R.layout.adapter_item_room_view, viewGroup, false));
    }

    /* access modifiers changed from: protected */
    public void populateViewHolder(int i, ViewHolder viewHolder, int i2) {
        if (i == -7 || i == -4) {
            RoomViewHolder roomViewHolder = (RoomViewHolder) viewHolder;
            final Room room = (Room) getItemForPosition(i2);
            roomViewHolder.populateViews(this.mContext, this.mSession, room, false, false, this.mMoreRoomActionListener);
            roomViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    TchapRoomAdapter.this.mListener.onSelectItem(room, -1);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public int applyFilter(String str) {
        return filterRoomSection(this.mRoomsSection, str) + 0;
    }

    public void setRooms(List<Room> list) {
        this.mRoomsSection.setItems(list, this.mCurrentFilterPattern);
        if (!TextUtils.isEmpty(this.mCurrentFilterPattern)) {
            filterRoomSection(this.mRoomsSection, String.valueOf(this.mCurrentFilterPattern));
        }
        updateSections();
    }
}
