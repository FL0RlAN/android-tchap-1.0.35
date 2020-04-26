package im.vector.adapters;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.settings.VectorLocale;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.pid.RoomThirdPartyInvite;

public class VectorRoomDetailsMembersAdapter extends BaseExpandableListAdapter {
    /* access modifiers changed from: private */
    public final String LOG_TAG = VectorRoomDetailsMembersAdapter.class.getSimpleName();
    private final int mChildLayoutResourceId;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public List<String> mDisplayNamesList = new ArrayList();
    /* access modifiers changed from: private */
    public int mGroupIndexInvitedMembers = -1;
    /* access modifiers changed from: private */
    public int mGroupIndexPresentMembers = -1;
    private final int mGroupLayoutResourceId;
    private boolean mIsMultiSelectionMode;
    private final LayoutInflater mLayoutInflater;
    /* access modifiers changed from: private */
    public OnParticipantsListener mOnParticipantsListener;
    /* access modifiers changed from: private */
    public final Room mRoom;
    /* access modifiers changed from: private */
    public List<List<ParticipantAdapterItem>> mRoomMembersListByGroupPosition;
    /* access modifiers changed from: private */
    public String mSearchPattern = "";
    private List<String> mSelectedUserIds = new ArrayList();
    /* access modifiers changed from: private */
    public final MXSession mSession;
    /* access modifiers changed from: private */
    public View mSwipingCellView;

    private static class ChildMemberViewHolder {
        final View mDeleteActionsView;
        final View mHiddenListActionsView;
        final ImageView mMemberAvatarBadgeImageView;
        final ImageView mMemberAvatarImageView;
        final TextView mMemberDomainTextView;
        final TextView mMemberNameTextView;
        final ImageView mMemberOnlineStatusImageView;
        final RelativeLayout mSwipeCellLayout;

        ChildMemberViewHolder(View view) {
            this.mMemberAvatarImageView = (ImageView) view.findViewById(R.id.filtered_list_avatar);
            this.mMemberAvatarBadgeImageView = (ImageView) view.findViewById(R.id.filtered_list_avatar_badge);
            this.mMemberOnlineStatusImageView = (ImageView) view.findViewById(R.id.filtered_list_online_status);
            this.mMemberNameTextView = (TextView) view.findViewById(R.id.filtered_list_name);
            this.mMemberDomainTextView = (TextView) view.findViewById(R.id.filtered_list_domain);
            this.mHiddenListActionsView = view.findViewById(R.id.filtered_list_actions);
            this.mSwipeCellLayout = (RelativeLayout) view.findViewById(R.id.filtered_list_cell);
            this.mDeleteActionsView = view.findViewById(R.id.filtered_list_delete_action);
        }
    }

    private static class GroupViewHolder {
        final ImageView mExpanderLogoImageView;
        final TextView mTitleTxtView;

        GroupViewHolder(View view) {
            this.mTitleTxtView = (TextView) view.findViewById(R.id.heading);
            this.mExpanderLogoImageView = (ImageView) view.findViewById(R.id.heading_image);
        }
    }

    public interface OnParticipantsListener {
        void onClick(ParticipantAdapterItem participantAdapterItem);

        void onGroupCollapsedNotif(int i);

        void onGroupExpandedNotif(int i);

        void onLeaveClick();

        void onRemoveClick(ParticipantAdapterItem participantAdapterItem);

        void onSelectUserId(String str);
    }

    public interface OnRoomMembersSearchListener {
        void onSearchEnd(int i, boolean z, String str);
    }

    public long getChildId(int i, int i2) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    public VectorRoomDetailsMembersAdapter(Context context, int i, int i2, MXSession mXSession, String str, MXMediaCache mXMediaCache) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mChildLayoutResourceId = i;
        this.mGroupLayoutResourceId = i2;
        this.mSession = mXSession;
        this.mRoom = this.mSession.getDataHandler().getRoom(str);
        this.mIsMultiSelectionMode = false;
    }

    public void setSearchedPattern(String str, OnRoomMembersSearchListener onRoomMembersSearchListener, boolean z) {
        if (TextUtils.isEmpty(str)) {
            this.mSearchPattern = null;
            updateRoomMembersDataModel(onRoomMembersSearchListener);
        } else if (!str.trim().equals(this.mSearchPattern) || z) {
            this.mSearchPattern = str.trim().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
            updateRoomMembersDataModel(onRoomMembersSearchListener);
        } else if (onRoomMembersSearchListener != null) {
            onRoomMembersSearchListener.onSearchEnd(getItemsCount(), false, null);
        }
    }

    public int getItemsCount() {
        return getChildrenCount(this.mGroupIndexInvitedMembers) + getChildrenCount(this.mGroupIndexPresentMembers);
    }

    public void setOnParticipantsListener(OnParticipantsListener onParticipantsListener) {
        this.mOnParticipantsListener = onParticipantsListener;
    }

    public List<String> getSelectedUserIds() {
        return this.mSelectedUserIds;
    }

    public void setMultiSelectionMode(boolean z) {
        this.mIsMultiSelectionMode = z;
        this.mSelectedUserIds = new ArrayList();
    }

    /* access modifiers changed from: private */
    public boolean isSearchModeEnabled() {
        return !TextUtils.isEmpty(this.mSearchPattern);
    }

    /* access modifiers changed from: private */
    public int alphaComparator(String str, String str2) {
        if (str == null) {
            return -1;
        }
        if (str2 == null) {
            return 1;
        }
        return String.CASE_INSENSITIVE_ORDER.compare(str, str2);
    }

    public void updateRoomMembersDataModel(final OnRoomMembersSearchListener onRoomMembersSearchListener) {
        if (!this.mSession.isAlive()) {
            Log.e(this.LOG_TAG, "updateRoomMembersDataModel the session is not anymore valid");
            return;
        }
        final Handler handler = new Handler();
        final String str = this.mSearchPattern;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                final boolean access$000 = VectorRoomDetailsMembersAdapter.this.isSearchModeEnabled();
                final ArrayList arrayList = new ArrayList();
                final ArrayList arrayList2 = new ArrayList();
                final ArrayList arrayList3 = new ArrayList();
                ArrayList arrayList4 = new ArrayList();
                final ArrayList arrayList5 = new ArrayList();
                final ArrayList<RoomMember> arrayList6 = new ArrayList<>();
                final String[] strArr = new String[1];
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                VectorRoomDetailsMembersAdapter.this.mRoom.getActiveMembersAsync(new ApiCallback<List<RoomMember>>() {
                    public void onSuccess(List<RoomMember> list) {
                        arrayList6.addAll(list);
                        countDownLatch.countDown();
                    }

                    public void onNetworkError(Exception exc) {
                        strArr[0] = exc.getLocalizedMessage();
                        countDownLatch.countDown();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        strArr[0] = matrixError.getLocalizedMessage();
                        countDownLatch.countDown();
                    }

                    public void onUnexpectedError(Exception exc) {
                        strArr[0] = exc.getLocalizedMessage();
                        countDownLatch.countDown();
                    }
                });
                try {
                    countDownLatch.await(30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Log.e(VectorRoomDetailsMembersAdapter.this.LOG_TAG, "updateRoomMembersDataModel, latch interrupted", e);
                    strArr[0] = VectorRoomDetailsMembersAdapter.this.mContext.getString(R.string.unknown_error);
                    e.printStackTrace();
                }
                if (strArr[0] == null) {
                    String myUserId = VectorRoomDetailsMembersAdapter.this.mSession.getMyUserId();
                    final PowerLevels powerLevels = VectorRoomDetailsMembersAdapter.this.mRoom.getState().getPowerLevels();
                    for (RoomMember roomMember : arrayList6) {
                        ParticipantAdapterItem participantAdapterItem = new ParticipantAdapterItem(roomMember);
                        if (!access$000 || participantAdapterItem.contains(VectorRoomDetailsMembersAdapter.this.mSearchPattern)) {
                            if (roomMember.getUserId().equals(myUserId)) {
                                arrayList.add(participantAdapterItem);
                            } else {
                                if ("invite".equals(roomMember.membership)) {
                                    arrayList5.add(participantAdapterItem);
                                } else {
                                    arrayList4.add(participantAdapterItem);
                                }
                            }
                            if (!TextUtils.isEmpty(participantAdapterItem.mDisplayName)) {
                                arrayList3.add(participantAdapterItem.mDisplayName);
                            }
                        }
                    }
                    for (RoomThirdPartyInvite roomThirdPartyInvite : VectorRoomDetailsMembersAdapter.this.mRoom.getState().thirdPartyInvites()) {
                        if (VectorRoomDetailsMembersAdapter.this.mRoom.getState().memberWithThirdPartyInviteToken(roomThirdPartyInvite.token) == null && roomThirdPartyInvite.display_name != null) {
                            ParticipantAdapterItem participantAdapterItem2 = new ParticipantAdapterItem(roomThirdPartyInvite);
                            if (!access$000 || participantAdapterItem2.contains(VectorRoomDetailsMembersAdapter.this.mSearchPattern)) {
                                arrayList5.add(participantAdapterItem2);
                            }
                        }
                    }
                    final MXDataHandler dataHandler = VectorRoomDetailsMembersAdapter.this.mSession.getDataHandler();
                    AnonymousClass2 r9 = new Comparator<ParticipantAdapterItem>() {
                        private final Map<String, User> usersMap = new HashMap();

                        private User getUser(String str) {
                            if (str == null) {
                                return null;
                            }
                            User user = (User) this.usersMap.get(str);
                            if (user != null) {
                                return user;
                            }
                            User user2 = dataHandler.getUser(str);
                            if (user2 == null) {
                                return user2;
                            }
                            User deepCopy = user2.deepCopy();
                            this.usersMap.put(str, deepCopy);
                            return deepCopy;
                        }

                        public int compare(ParticipantAdapterItem participantAdapterItem, ParticipantAdapterItem participantAdapterItem2) {
                            int i;
                            User user = getUser(participantAdapterItem.mUserId);
                            User user2 = getUser(participantAdapterItem2.mUserId);
                            String comparisonDisplayName = participantAdapterItem.getComparisonDisplayName();
                            String comparisonDisplayName2 = participantAdapterItem2.getComparisonDisplayName();
                            int i2 = 0;
                            boolean booleanValue = (user == null || user.currently_active == null) ? false : user.currently_active.booleanValue();
                            boolean booleanValue2 = (user2 == null || user2.currently_active == null) ? false : user2.currently_active.booleanValue();
                            if (powerLevels != null) {
                                i = (user == null || user.user_id == null) ? 0 : powerLevels.getUserPowerLevel(user.user_id);
                                if (!(user2 == null || user2.user_id == null)) {
                                    i2 = powerLevels.getUserPowerLevel(user2.user_id);
                                }
                            } else {
                                i = 0;
                            }
                            if (user == null && user2 == null) {
                                return VectorRoomDetailsMembersAdapter.this.alphaComparator(comparisonDisplayName, comparisonDisplayName2);
                            }
                            int i3 = 1;
                            if (user != null && user2 == null) {
                                return 1;
                            }
                            if (user == null && user2 != null) {
                                return -1;
                            }
                            if (!booleanValue || !booleanValue2) {
                                if (booleanValue && !booleanValue2) {
                                    return -1;
                                }
                                if (!booleanValue && booleanValue2) {
                                    return 1;
                                }
                                long absoluteLastActiveAgo = user != null ? user.getAbsoluteLastActiveAgo() : 0;
                                long absoluteLastActiveAgo2 = user2 != null ? user2.getAbsoluteLastActiveAgo() : 0;
                                long j = absoluteLastActiveAgo - absoluteLastActiveAgo2;
                                if (j == 0) {
                                    return VectorRoomDetailsMembersAdapter.this.alphaComparator(comparisonDisplayName, comparisonDisplayName2);
                                }
                                if (0 == absoluteLastActiveAgo) {
                                    return 1;
                                }
                                if (0 == absoluteLastActiveAgo2) {
                                    return -1;
                                }
                                if (j <= 0) {
                                    i3 = -1;
                                }
                                return i3;
                            } else if (i == i2) {
                                return VectorRoomDetailsMembersAdapter.this.alphaComparator(comparisonDisplayName, comparisonDisplayName2);
                            } else {
                                if (i2 - i <= 0) {
                                    i3 = -1;
                                }
                                return i3;
                            }
                        }
                    };
                    try {
                        Collections.sort(arrayList4, r9);
                        arrayList.addAll(arrayList4);
                        Handler handler = handler;
                        final AnonymousClass2 r7 = r9;
                        AnonymousClass4 r1 = new Runnable() {
                            public void run() {
                                if (TextUtils.equals(VectorRoomDetailsMembersAdapter.this.mSearchPattern, str)) {
                                    VectorRoomDetailsMembersAdapter.this.mDisplayNamesList = arrayList3;
                                    VectorRoomDetailsMembersAdapter.this.mRoomMembersListByGroupPosition = arrayList2;
                                    VectorRoomDetailsMembersAdapter.this.mGroupIndexPresentMembers = -1;
                                    VectorRoomDetailsMembersAdapter.this.mGroupIndexPresentMembers = -1;
                                    int i = 0;
                                    if (arrayList.size() != 0) {
                                        arrayList2.add(arrayList);
                                        VectorRoomDetailsMembersAdapter.this.mGroupIndexPresentMembers = 0;
                                        i = 1;
                                    }
                                    if (arrayList5.size() != 0) {
                                        Collections.sort(arrayList5, r7);
                                        arrayList2.add(arrayList5);
                                        VectorRoomDetailsMembersAdapter.this.mGroupIndexInvitedMembers = i;
                                    }
                                    if (onRoomMembersSearchListener != null) {
                                        try {
                                            onRoomMembersSearchListener.onSearchEnd(VectorRoomDetailsMembersAdapter.this.getItemsCount(), access$000, null);
                                        } catch (Exception e) {
                                            String access$200 = VectorRoomDetailsMembersAdapter.this.LOG_TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("## updateRoomMembersDataModel() : onSearchEnd fails ");
                                            sb.append(e.getMessage());
                                            Log.e(access$200, sb.toString(), e);
                                        }
                                    }
                                    VectorRoomDetailsMembersAdapter.this.notifyDataSetChanged();
                                }
                            }
                        };
                        handler.post(r1);
                    } catch (Exception e2) {
                        String access$200 = VectorRoomDetailsMembersAdapter.this.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## updateRoomMembersDataModel failed while sorting ");
                        sb.append(e2.getMessage());
                        Log.e(access$200, sb.toString(), e2);
                        if (TextUtils.equals(str, VectorRoomDetailsMembersAdapter.this.mSearchPattern)) {
                            handler.post(new Runnable() {
                                public void run() {
                                    VectorRoomDetailsMembersAdapter.this.updateRoomMembersDataModel(onRoomMembersSearchListener);
                                }
                            });
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            if (onRoomMembersSearchListener != null) {
                                try {
                                    onRoomMembersSearchListener.onSearchEnd(VectorRoomDetailsMembersAdapter.this.getItemsCount(), access$000, strArr[0]);
                                } catch (Exception e) {
                                    String access$200 = VectorRoomDetailsMembersAdapter.this.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## updateRoomMembersDataModel() : onSearchEnd fails ");
                                    sb.append(e.getMessage());
                                    Log.e(access$200, sb.toString(), e);
                                }
                            }
                        }
                    });
                }
            }
        });
        thread.setPriority(1);
        thread.start();
    }

    public List<String> getUserIdsList() {
        ArrayList arrayList = new ArrayList();
        int i = this.mGroupIndexPresentMembers;
        if (i >= 0) {
            int size = ((List) this.mRoomMembersListByGroupPosition.get(i)).size();
            for (int i2 = 1; i2 < size; i2++) {
                ParticipantAdapterItem participantAdapterItem = (ParticipantAdapterItem) ((List) this.mRoomMembersListByGroupPosition.get(this.mGroupIndexPresentMembers)).get(i2);
                if (participantAdapterItem.mUserId != null) {
                    arrayList.add(participantAdapterItem.mUserId);
                }
            }
        }
        return arrayList;
    }

    private String getGroupTitle(int i) {
        if (this.mGroupIndexInvitedMembers == i) {
            return this.mContext.getString(R.string.room_details_people_invited_group_name);
        }
        return this.mGroupIndexPresentMembers == i ? this.mContext.getString(R.string.room_details_people_present_group_name) : "??";
    }

    public void onGroupCollapsed(int i) {
        super.onGroupCollapsed(i);
        OnParticipantsListener onParticipantsListener = this.mOnParticipantsListener;
        if (onParticipantsListener != null) {
            onParticipantsListener.onGroupCollapsedNotif(i);
        }
    }

    public void onGroupExpanded(int i) {
        super.onGroupExpanded(i);
        OnParticipantsListener onParticipantsListener = this.mOnParticipantsListener;
        if (onParticipantsListener != null) {
            onParticipantsListener.onGroupExpandedNotif(i);
        }
    }

    public int getGroupCount() {
        List<List<ParticipantAdapterItem>> list = this.mRoomMembersListByGroupPosition;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public int getChildrenCount(int i) {
        try {
            if (this.mRoomMembersListByGroupPosition == null || -1 == i) {
                return 0;
            }
            return ((List) this.mRoomMembersListByGroupPosition.get(i)).size();
        } catch (Exception e) {
            String str = this.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getChildrenCount(): Exception Msg=");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return 0;
        }
    }

    public Object getGroup(int i) {
        return getGroupTitle(i);
    }

    public Object getChild(int i, int i2) {
        List<List<ParticipantAdapterItem>> list = this.mRoomMembersListByGroupPosition;
        if (list != null) {
            return ((List) list.get(i)).get(i2);
        }
        return null;
    }

    public long getGroupId(int i) {
        return (long) getGroupTitle(i).hashCode();
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mGroupLayoutResourceId, null);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.mTitleTxtView.setText(getGroupTitle(i));
        groupViewHolder.mExpanderLogoImageView.setImageResource(z ? R.drawable.ic_material_expand_more_black : R.drawable.ic_material_expand_less_black);
        return view;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:68:0x01b0, code lost:
        if (r3 >= r14) goto L_0x01b9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01b6, code lost:
        if (r9.mRoom == null) goto L_0x01b2;
     */
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        final ChildMemberViewHolder childMemberViewHolder;
        PowerLevels powerLevels;
        boolean isSearchModeEnabled = isSearchModeEnabled();
        boolean z2 = false;
        final boolean z3 = i2 == 0 && this.mGroupIndexPresentMembers == i;
        final ParticipantAdapterItem participantAdapterItem = (ParticipantAdapterItem) ((List) this.mRoomMembersListByGroupPosition.get(i)).get(i2);
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mChildLayoutResourceId, viewGroup, false);
            childMemberViewHolder = new ChildMemberViewHolder(view);
            view.setTag(childMemberViewHolder);
        } else {
            childMemberViewHolder = (ChildMemberViewHolder) view.getTag();
        }
        if (!this.mSession.isAlive()) {
            Log.e(this.LOG_TAG, "getChildView : the session is not anymore valid");
            return view;
        }
        if (participantAdapterItem.getAvatarBitmap() != null) {
            childMemberViewHolder.mMemberAvatarImageView.setImageBitmap(participantAdapterItem.getAvatarBitmap());
        } else if (TextUtils.isEmpty(participantAdapterItem.mUserId)) {
            VectorUtils.loadUserAvatar(this.mContext, this.mSession, childMemberViewHolder.mMemberAvatarImageView, participantAdapterItem.mAvatarUrl, participantAdapterItem.mDisplayName, participantAdapterItem.mDisplayName);
        } else {
            if (TextUtils.equals(participantAdapterItem.mUserId, participantAdapterItem.mDisplayName) || TextUtils.isEmpty(participantAdapterItem.mAvatarUrl)) {
                User user = this.mSession.getDataHandler().getStore().getUser(participantAdapterItem.mUserId);
                if (user != null) {
                    if (TextUtils.equals(participantAdapterItem.mUserId, participantAdapterItem.mDisplayName) && !TextUtils.isEmpty(user.displayname)) {
                        participantAdapterItem.mDisplayName = user.displayname;
                    }
                    if (participantAdapterItem.mAvatarUrl == null) {
                        participantAdapterItem.mAvatarUrl = user.avatar_url;
                    }
                }
            }
            VectorUtils.loadUserAvatar(this.mContext, this.mSession, childMemberViewHolder.mMemberAvatarImageView, participantAdapterItem.mAvatarUrl, participantAdapterItem.mUserId, participantAdapterItem.mDisplayName);
        }
        String str = participantAdapterItem.mDisplayName;
        if (!TextUtils.isEmpty(str)) {
            int indexOf = this.mDisplayNamesList.indexOf(str);
            if (indexOf >= 0 && indexOf == this.mDisplayNamesList.lastIndexOf(str)) {
                indexOf = -1;
            }
            if (indexOf >= 0 && !TextUtils.isEmpty(participantAdapterItem.mUserId)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" (");
                sb.append(participantAdapterItem.mUserId);
                sb.append(")");
                str = sb.toString();
            }
        }
        childMemberViewHolder.mMemberNameTextView.setPadding(0, 22, 0, 0);
        childMemberViewHolder.mMemberDomainTextView.setPadding(0, 30, 0, 0);
        childMemberViewHolder.mMemberNameTextView.setText(DinsicUtils.getNameFromDisplayName(str));
        childMemberViewHolder.mMemberDomainTextView.setText(DinsicUtils.getDomainFromDisplayName(str));
        int i3 = 8;
        childMemberViewHolder.mMemberAvatarBadgeImageView.setVisibility(8);
        Room room = this.mRoom;
        if (room != null) {
            powerLevels = room.getState().getPowerLevels();
            if (powerLevels != null) {
                if (((float) powerLevels.getUserPowerLevel(participantAdapterItem.mUserId)) >= 100.0f) {
                    childMemberViewHolder.mMemberAvatarBadgeImageView.setVisibility(0);
                    childMemberViewHolder.mMemberAvatarBadgeImageView.setImageResource(R.drawable.admin_icon);
                } else if (((float) powerLevels.getUserPowerLevel(participantAdapterItem.mUserId)) >= 50.0f) {
                    childMemberViewHolder.mMemberAvatarBadgeImageView.setVisibility(0);
                    childMemberViewHolder.mMemberAvatarBadgeImageView.setImageResource(R.drawable.mod_icon);
                }
            }
        } else {
            powerLevels = null;
        }
        ImageView imageView = childMemberViewHolder.mMemberOnlineStatusImageView;
        if (VectorUtils.isUserOnline(this.mContext, this.mSession, participantAdapterItem.mUserId, null)) {
            i3 = 0;
        }
        imageView.setVisibility(i3);
        childMemberViewHolder.mDeleteActionsView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener != null) {
                    try {
                        if (z3) {
                            VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener.onLeaveClick();
                        } else {
                            VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener.onRemoveClick(participantAdapterItem);
                        }
                    } catch (Exception e) {
                        String access$200 = VectorRoomDetailsMembersAdapter.this.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## Delete action listener: Exception Msg=");
                        sb.append(e.getMessage());
                        Log.e(access$200, sb.toString(), e);
                    }
                }
            }
        });
        View view2 = this.mSwipingCellView;
        if (view2 != null) {
            view2.setTranslationX(0.0f);
            this.mSwipingCellView = null;
        }
        childMemberViewHolder.mSwipeCellLayout.setTranslationX(0.0f);
        if (powerLevels != null) {
            int userPowerLevel = powerLevels.getUserPowerLevel(this.mSession.getCredentials().userId);
            int userPowerLevel2 = powerLevels.getUserPowerLevel(participantAdapterItem.mUserId);
            int i4 = powerLevels.kick;
            if (!z3) {
                if (userPowerLevel > userPowerLevel2) {
                }
            }
            childMemberViewHolder.mSwipeCellLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener != null && !TextUtils.isEmpty(participantAdapterItem.mUserId)) {
                        String str = participantAdapterItem.mUserId;
                        if (Patterns.EMAIL_ADDRESS.matcher(str).matches() || (str.startsWith("@") && str.indexOf(":") > 1)) {
                            VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener.onClick(participantAdapterItem);
                        } else {
                            Toast.makeText(VectorRoomDetailsMembersAdapter.this.mContext, R.string.malformed_id, 1).show();
                        }
                    }
                }
            });
            childMemberViewHolder.mSwipeCellLayout.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    return true;
                }
            });
            if (!isSearchModeEnabled || z2 || (participantAdapterItem.mRoomMember == null && participantAdapterItem.mRoomThirdPartyInvite == null)) {
                childMemberViewHolder.mSwipeCellLayout.setOnTouchListener(null);
            } else {
                childMemberViewHolder.mSwipeCellLayout.setOnTouchListener(new OnTouchListener() {
                    private float mStartX = 0.0f;

                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        int width = childMemberViewHolder.mHiddenListActionsView.getWidth();
                        int action = motionEvent.getAction();
                        if (action != 0) {
                            if (action != 1) {
                                if (action == 2) {
                                    childMemberViewHolder.mSwipeCellLayout.setTranslationX(Math.max(Math.min((motionEvent.getX() + view.getTranslationX()) - this.mStartX, 0.0f), (float) (-width)));
                                } else if (action != 3) {
                                    return false;
                                }
                            }
                            float x = motionEvent.getX() + view.getTranslationX();
                            if (Math.abs(x - this.mStartX) >= 10.0f) {
                                float f = (float) (-width);
                                if ((-Math.max(Math.min(x - this.mStartX, 0.0f), f)) > ((float) (width / 2))) {
                                    childMemberViewHolder.mSwipeCellLayout.setTranslationX(f);
                                } else {
                                    childMemberViewHolder.mSwipeCellLayout.setTranslationX(0.0f);
                                    VectorRoomDetailsMembersAdapter.this.mSwipingCellView = null;
                                }
                            } else if (motionEvent.getAction() != 1 || VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener == null || TextUtils.isEmpty(participantAdapterItem.mUserId)) {
                                return false;
                            } else {
                                VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener.onClick(participantAdapterItem);
                                return false;
                            }
                        } else {
                            if (VectorRoomDetailsMembersAdapter.this.mSwipingCellView == null) {
                                VectorRoomDetailsMembersAdapter.this.mSwipingCellView = childMemberViewHolder.mSwipeCellLayout;
                            }
                            this.mStartX = motionEvent.getX();
                        }
                        return true;
                    }
                });
            }
            childMemberViewHolder.mSwipeCellLayout.setBackgroundColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_riot_primary_background_color));
            return view;
        }
        z2 = true;
        childMemberViewHolder.mSwipeCellLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener != null && !TextUtils.isEmpty(participantAdapterItem.mUserId)) {
                    String str = participantAdapterItem.mUserId;
                    if (Patterns.EMAIL_ADDRESS.matcher(str).matches() || (str.startsWith("@") && str.indexOf(":") > 1)) {
                        VectorRoomDetailsMembersAdapter.this.mOnParticipantsListener.onClick(participantAdapterItem);
                    } else {
                        Toast.makeText(VectorRoomDetailsMembersAdapter.this.mContext, R.string.malformed_id, 1).show();
                    }
                }
            }
        });
        childMemberViewHolder.mSwipeCellLayout.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                return true;
            }
        });
        if (!isSearchModeEnabled) {
        }
        childMemberViewHolder.mSwipeCellLayout.setOnTouchListener(null);
        childMemberViewHolder.mSwipeCellLayout.setBackgroundColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_riot_primary_background_color));
        return view;
    }
}
