package im.vector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.VectorUtils;
import im.vector.view.VectorCircularImageView;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;

public class VectorMemberDetailsAdapter extends BaseExpandableListAdapter {
    /* access modifiers changed from: private */
    public IEnablingActions mActionListener;
    private List<List<AdapterMemberActionItems>> mActionsList = new ArrayList();
    private List<AdapterMemberActionItems> mAdminActionsList = new ArrayList();
    private int mAdminGroupPosition = -1;
    private List<AdapterMemberActionItems> mCallActionsList = new ArrayList();
    private int mCallGroupPosition = -1;
    private final Context mContext;
    private int mDevicesGroupPosition = -1;
    private List<AdapterMemberActionItems> mDevicesList = new ArrayList();
    private int mDirectCallsGroupPosition = -1;
    private List<AdapterMemberActionItems> mDirectCallsList = new ArrayList();
    private final int mHeaderLayoutResourceId;
    private final LayoutInflater mLayoutInflater;
    private final int mRowItemLayoutResourceId;
    private final MXSession mSession;
    private List<AdapterMemberActionItems> mUncategorizedActionsList = new ArrayList();
    private int mUncategorizedGroupPosition = -1;

    public static class AdapterMemberActionItems {
        public final String mActionDescText;
        public final int mActionType;
        public final int mIconResourceId;
        public final Room mRoom;

        public AdapterMemberActionItems(int i, String str, int i2) {
            this.mIconResourceId = i;
            this.mActionDescText = str;
            this.mActionType = i2;
            this.mRoom = null;
        }

        public AdapterMemberActionItems(Room room) {
            this.mIconResourceId = -1;
            this.mActionDescText = null;
            this.mActionType = -1;
            this.mRoom = room;
        }
    }

    public interface IEnablingActions {
        void performItemAction(int i);

        void selectRoom(Room room);
    }

    private static class MemberDetailsViewHolder {
        final TextView mActionDescTextView;
        final ImageView mActionImageView;
        final View mRoomAvatarLayout;
        final VectorCircularImageView mVectorCircularImageView;

        MemberDetailsViewHolder(View view) {
            this.mActionImageView = (ImageView) view.findViewById(R.id.adapter_member_details_icon);
            this.mActionDescTextView = (TextView) view.findViewById(R.id.adapter_member_details_action_text);
            this.mVectorCircularImageView = (VectorCircularImageView) view.findViewById(R.id.room_avatar_image_view);
            this.mRoomAvatarLayout = view.findViewById(R.id.room_avatar_layout);
        }
    }

    public Object getChild(int i, int i2) {
        return null;
    }

    public long getChildId(int i, int i2) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    public VectorMemberDetailsAdapter(Context context, MXSession mXSession, int i, int i2) {
        this.mContext = context;
        this.mSession = mXSession;
        this.mRowItemLayoutResourceId = i;
        this.mHeaderLayoutResourceId = i2;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
    }

    public void setActionListener(IEnablingActions iEnablingActions) {
        this.mActionListener = iEnablingActions;
    }

    public void setUncategorizedActionsList(List<AdapterMemberActionItems> list) {
        this.mUncategorizedActionsList = list;
    }

    public void setAdminActionsList(List<AdapterMemberActionItems> list) {
        this.mAdminActionsList = list;
    }

    public void setCallActionsList(List<AdapterMemberActionItems> list) {
        this.mCallActionsList = list;
    }

    public void setDirectCallsActionsList(List<AdapterMemberActionItems> list) {
        this.mDirectCallsList = list;
    }

    public void setDevicesActionsList(List<AdapterMemberActionItems> list) {
        this.mDevicesList = list;
    }

    public void notifyDataSetChanged() {
        this.mActionsList = new ArrayList();
        this.mUncategorizedGroupPosition = -1;
        this.mAdminGroupPosition = -1;
        this.mCallGroupPosition = -1;
        this.mDirectCallsGroupPosition = -1;
        this.mDevicesGroupPosition = -1;
        int i = 0;
        if (this.mUncategorizedActionsList.size() != 0) {
            this.mActionsList.add(this.mUncategorizedActionsList);
            this.mUncategorizedGroupPosition = 0;
            i = 1;
        }
        if (this.mAdminActionsList.size() != 0) {
            this.mActionsList.add(this.mAdminActionsList);
            this.mAdminGroupPosition = i;
            i++;
        }
        if (this.mCallActionsList.size() != 0) {
            this.mActionsList.add(this.mCallActionsList);
            this.mCallGroupPosition = i;
            i++;
        }
        if (this.mDevicesList.size() != 0) {
            this.mActionsList.add(this.mDevicesList);
            this.mDevicesGroupPosition = i;
            i++;
        }
        if (this.mDirectCallsList.size() != 0) {
            this.mActionsList.add(this.mDirectCallsList);
            this.mDirectCallsGroupPosition = i;
        }
        super.notifyDataSetChanged();
    }

    public int getGroupCount() {
        return this.mActionsList.size();
    }

    private String getGroupTitle(int i) {
        if (i == this.mAdminGroupPosition) {
            return this.mContext.getString(R.string.room_participants_header_admin_tools);
        }
        if (i == this.mCallGroupPosition) {
            return this.mContext.getString(R.string.room_participants_header_call);
        }
        if (i == this.mDirectCallsGroupPosition) {
            return this.mContext.getString(R.string.room_participants_header_direct_chats);
        }
        return i == this.mDevicesGroupPosition ? this.mContext.getString(R.string.room_participants_header_devices) : "???";
    }

    public Object getGroup(int i) {
        return getGroupTitle(i);
    }

    public long getGroupId(int i) {
        return (long) getGroupTitle(i).hashCode();
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mHeaderLayoutResourceId, null);
        }
        ((TextView) view.findViewById(R.id.heading)).setText(getGroupTitle(i));
        int i2 = 8;
        view.findViewById(R.id.heading_image).setVisibility(8);
        View findViewById = view.findViewById(R.id.heading_layout);
        if (i != this.mUncategorizedGroupPosition) {
            i2 = 0;
        }
        findViewById.setVisibility(i2);
        return view;
    }

    public int getChildrenCount(int i) {
        if (i < this.mActionsList.size()) {
            return ((List) this.mActionsList.get(i)).size();
        }
        return 0;
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        MemberDetailsViewHolder memberDetailsViewHolder;
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mRowItemLayoutResourceId, viewGroup, false);
            memberDetailsViewHolder = new MemberDetailsViewHolder(view);
            view.setTag(memberDetailsViewHolder);
        } else {
            memberDetailsViewHolder = (MemberDetailsViewHolder) view.getTag();
        }
        if (i < this.mActionsList.size() && i2 < ((List) this.mActionsList.get(i)).size()) {
            final AdapterMemberActionItems adapterMemberActionItems = (AdapterMemberActionItems) ((List) this.mActionsList.get(i)).get(i2);
            if (adapterMemberActionItems.mRoom != null) {
                memberDetailsViewHolder.mActionDescTextView.setTextColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_riot_primary_text_color));
                memberDetailsViewHolder.mActionDescTextView.setText(DinsicUtils.getRoomDisplayName(this.mContext, adapterMemberActionItems.mRoom));
                memberDetailsViewHolder.mActionImageView.setVisibility(8);
                memberDetailsViewHolder.mRoomAvatarLayout.setVisibility(0);
                VectorUtils.loadRoomAvatar(this.mContext, this.mSession, (ImageView) memberDetailsViewHolder.mVectorCircularImageView, adapterMemberActionItems.mRoom);
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (VectorMemberDetailsAdapter.this.mActionListener != null) {
                            VectorMemberDetailsAdapter.this.mActionListener.selectRoom(adapterMemberActionItems.mRoom);
                        }
                    }
                });
            } else {
                memberDetailsViewHolder.mActionDescTextView.setText(adapterMemberActionItems.mActionDescText);
                memberDetailsViewHolder.mActionImageView.setVisibility(0);
                memberDetailsViewHolder.mRoomAvatarLayout.setVisibility(8);
                memberDetailsViewHolder.mActionImageView.setImageResource(adapterMemberActionItems.mIconResourceId);
                if (adapterMemberActionItems.mIconResourceId != R.drawable.ic_remove_circle_outline_red) {
                    memberDetailsViewHolder.mActionImageView.setImageDrawable(ThemeUtils.INSTANCE.tintDrawable(this.mContext, memberDetailsViewHolder.mActionImageView.getDrawable(), R.attr.vctr_settings_icon_tint_color));
                }
                int color = ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_riot_primary_text_color);
                if (2 == adapterMemberActionItems.mActionType) {
                    color = ContextCompat.getColor(this.mContext, R.color.vector_fuchsia_color);
                }
                memberDetailsViewHolder.mActionDescTextView.setTextColor(color);
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (VectorMemberDetailsAdapter.this.mActionListener != null) {
                            VectorMemberDetailsAdapter.this.mActionListener.performItemAction(adapterMemberActionItems.mActionType);
                        }
                    }
                });
            }
        }
        return view;
    }
}
