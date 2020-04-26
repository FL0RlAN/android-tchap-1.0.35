package fr.gouv.tchap.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.HexagonMaskView;
import im.vector.adapters.AbsAdapter;
import im.vector.adapters.AbsAdapter.HeaderViewHolder;
import im.vector.adapters.AdapterSection;
import im.vector.adapters.PublicRoomsAdapterSection;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

public class TchapPublicRoomAdapter extends AbsAdapter {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TchapPublicRoomAdapter.class.getSimpleName();
    private static final int TYPE_HEADER_PUBLIC_ROOM = 0;
    private static final int TYPE_PUBLIC_ROOM = 1;
    private static final Comparator<PublicRoom> mComparator = new Comparator<PublicRoom>() {
        public int compare(PublicRoom publicRoom, PublicRoom publicRoom2) {
            return publicRoom2.numJoinedMembers - publicRoom.numJoinedMembers;
        }
    };
    /* access modifiers changed from: private */
    public final OnSelectItemListener mListener;
    private final PublicRoomsAdapterSection mPublicRoomsSection;

    public interface OnSelectItemListener {
        void onSelectItem(Room room, int i);

        void onSelectItem(PublicRoom publicRoom);
    }

    class PublicRoomViewHolder extends ViewHolder {
        @BindView(2131296870)
        HexagonMaskView vPublicRoomAvatar;
        @BindView(2131296871)
        TextView vPublicRoomDomain;
        @BindView(2131296873)
        TextView vPublicRoomName;
        @BindView(2131296872)
        TextView vPublicRoomsMemberCountTextView;
        @BindView(2131296874)
        TextView vRoomTopic;

        private PublicRoomViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }

        /* access modifiers changed from: private */
        public void populateViews(final PublicRoom publicRoom) {
            if (publicRoom == null) {
                Log.e(TchapPublicRoomAdapter.LOG_TAG, "## populateViews() : null publicRoom");
                return;
            }
            String publicRoomDisplayName = !TextUtils.isEmpty(publicRoom.name) ? publicRoom.name : VectorUtils.getPublicRoomDisplayName(publicRoom);
            VectorUtils.loadUserAvatar(TchapPublicRoomAdapter.this.mContext, TchapPublicRoomAdapter.this.mSession, this.vPublicRoomAvatar, publicRoom.avatarUrl, publicRoom.roomId, publicRoomDisplayName);
            this.vPublicRoomAvatar.setBorderSettings(ContextCompat.getColor(TchapPublicRoomAdapter.this.mContext, R.color.restricted_room_avatar_border_color), 3);
            this.vRoomTopic.setText(publicRoom.topic);
            this.vPublicRoomName.setText(publicRoomDisplayName);
            if (publicRoom.roomId != null) {
                this.vPublicRoomDomain.setText(DinsicUtils.getHomeServerDisplayNameFromMXIdentifier(publicRoom.roomId));
            }
            this.vPublicRoomsMemberCountTextView.setText(TchapPublicRoomAdapter.this.mContext.getResources().getQuantityString(R.plurals.public_room_nb_users, publicRoom.numJoinedMembers, new Object[]{Integer.valueOf(publicRoom.numJoinedMembers)}));
            this.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    TchapPublicRoomAdapter.this.mListener.onSelectItem(publicRoom);
                }
            });
        }
    }

    public class PublicRoomViewHolder_ViewBinding implements Unbinder {
        private PublicRoomViewHolder target;

        public PublicRoomViewHolder_ViewBinding(PublicRoomViewHolder publicRoomViewHolder, View view) {
            this.target = publicRoomViewHolder;
            publicRoomViewHolder.vPublicRoomAvatar = (HexagonMaskView) Utils.findRequiredViewAsType(view, R.id.public_room_avatar, "field 'vPublicRoomAvatar'", HexagonMaskView.class);
            publicRoomViewHolder.vPublicRoomName = (TextView) Utils.findRequiredViewAsType(view, R.id.public_room_name, "field 'vPublicRoomName'", TextView.class);
            publicRoomViewHolder.vRoomTopic = (TextView) Utils.findRequiredViewAsType(view, R.id.public_room_topic, "field 'vRoomTopic'", TextView.class);
            publicRoomViewHolder.vPublicRoomsMemberCountTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.public_room_members_count, "field 'vPublicRoomsMemberCountTextView'", TextView.class);
            publicRoomViewHolder.vPublicRoomDomain = (TextView) Utils.findRequiredViewAsType(view, R.id.public_room_domain, "field 'vPublicRoomDomain'", TextView.class);
        }

        public void unbind() {
            PublicRoomViewHolder publicRoomViewHolder = this.target;
            if (publicRoomViewHolder != null) {
                this.target = null;
                publicRoomViewHolder.vPublicRoomAvatar = null;
                publicRoomViewHolder.vPublicRoomName = null;
                publicRoomViewHolder.vRoomTopic = null;
                publicRoomViewHolder.vPublicRoomsMemberCountTextView = null;
                publicRoomViewHolder.vPublicRoomDomain = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    /* access modifiers changed from: protected */
    public int applyFilter(String str) {
        return 0;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    }

    public TchapPublicRoomAdapter(Context context, OnSelectItemListener onSelectItemListener) {
        super(context);
        this.mListener = onSelectItemListener;
        PublicRoomsAdapterSection publicRoomsAdapterSection = new PublicRoomsAdapterSection(context, context.getString(R.string.rooms_directory_header), -1, R.layout.adapter_item_public_room_view, 0, 1, new ArrayList(), null);
        this.mPublicRoomsSection = publicRoomsAdapterSection;
        this.mPublicRoomsSection.setEmptyViewPlaceholder(context.getString(R.string.no_public_room_placeholder), context.getString(R.string.no_result_placeholder));
        if (!DinsicUtils.isExternalTchapSession(this.mSession)) {
            addSection(this.mPublicRoomsSection);
        }
    }

    /* access modifiers changed from: protected */
    public ViewHolder createSubViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            return new HeaderViewHolder(from.inflate(R.layout.adapter_section_header_public_room, viewGroup, false));
        }
        if (i != 1) {
            return null;
        }
        return new PublicRoomViewHolder(from.inflate(R.layout.adapter_item_public_room_view, viewGroup, false));
    }

    /* access modifiers changed from: protected */
    public void populateViewHolder(int i, ViewHolder viewHolder, int i2) {
        if (i == 0) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            for (Pair pair : getSectionsArray()) {
                if (((Integer) pair.first).intValue() == i2) {
                    headerViewHolder.populateViews((AdapterSection) pair.second);
                    return;
                }
            }
        } else if (i == 1) {
            ((PublicRoomViewHolder) viewHolder).populateViews((PublicRoom) getItemForPosition(i2));
        }
    }

    public void setPublicRooms(List<PublicRoom> list) {
        this.mPublicRoomsSection.setItems(list, this.mCurrentFilterPattern);
        updateSections();
    }

    public void setEstimatedPublicRoomsCount(int i) {
        this.mPublicRoomsSection.setEstimatedPublicRoomsCount(i);
    }

    public void setNoMorePublicRooms(boolean z) {
        this.mPublicRoomsSection.setHasMoreResults(z);
    }

    public void addPublicRooms(List<PublicRoom> list) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mPublicRoomsSection.getItems());
        arrayList.addAll(list);
        Collections.sort(arrayList, mComparator);
        this.mPublicRoomsSection.setItems(arrayList, this.mCurrentFilterPattern);
        updateSections();
    }
}
