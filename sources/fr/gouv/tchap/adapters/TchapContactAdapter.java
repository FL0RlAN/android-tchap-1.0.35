package fr.gouv.tchap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Pair;
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
import im.vector.adapters.AbsAdapter.HeaderViewHolder;
import im.vector.adapters.AbsAdapter.MoreRoomActionListener;
import im.vector.adapters.AbsAdapter.RoomInvitationListener;
import im.vector.adapters.AdapterSection;
import im.vector.adapters.KnownContactsAdapterSection;
import im.vector.adapters.ParticipantAdapterItem;
import im.vector.adapters.RoomViewHolder;
import im.vector.contacts.ContactsManager;
import im.vector.settings.VectorLocale;
import im.vector.util.RoomUtils;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.User;

public class TchapContactAdapter extends AbsAdapter {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TchapContactAdapter.class.getSimpleName();
    private static final int TYPE_CONTACT = 1;
    private static final int TYPE_HEADER_LOCAL_CONTACTS = 0;
    private final AdapterSection<Room> mDirectChatsSection;
    private final KnownContactsAdapterSection mKnownContactsSection;
    /* access modifiers changed from: private */
    public final OnSelectItemListener mListener;
    private final AdapterSection<ParticipantAdapterItem> mLocalContactsSection;
    private final String mNoContactAccessPlaceholder;
    private final String mNoResultPlaceholder;

    class ContactViewHolder extends ViewHolder {
        @BindView(2131296396)
        ImageView vContactAvatar;
        @BindView(2131296400)
        TextView vContactDomain;
        @BindView(2131296401)
        TextView vContactName;
        @BindView(2131296402)
        ImageView vContactStatus;

        private ContactViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }

        /* access modifiers changed from: private */
        public void populateViews(final ParticipantAdapterItem participantAdapterItem, int i) {
            if (participantAdapterItem == null) {
                Log.e(TchapContactAdapter.LOG_TAG, "## populateViews() : null participant");
            } else if (i >= TchapContactAdapter.this.getItemCount()) {
                String access$300 = TchapContactAdapter.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## populateViews() : position out of bound ");
                sb.append(i);
                sb.append(" / ");
                sb.append(TchapContactAdapter.this.getItemCount());
                Log.e(access$300, sb.toString());
            } else {
                if (participantAdapterItem.isMatrixUser()) {
                    this.vContactAvatar.clearColorFilter();
                    String domainFromDisplayName = DinsicUtils.getDomainFromDisplayName(participantAdapterItem.mDisplayName);
                    this.vContactDomain.setVisibility(0);
                    this.vContactDomain.setText(domainFromDisplayName);
                    this.vContactName.setTypeface(null, 1);
                    this.vContactName.setText(DinsicUtils.getNameFromDisplayName(participantAdapterItem.mDisplayName));
                } else {
                    this.vContactAvatar.setColorFilter(Color.argb(155, 185, 185, 185));
                    this.vContactDomain.setVisibility(8);
                    this.vContactName.setTypeface(null, 2);
                    this.vContactName.setText(participantAdapterItem.mDisplayName);
                }
                participantAdapterItem.displayAvatar(TchapContactAdapter.this.mSession, this.vContactAvatar);
                if (MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER.matcher(participantAdapterItem.mUserId).matches()) {
                    loadContactPresence(this.vContactStatus, participantAdapterItem, i);
                } else {
                    this.vContactStatus.setVisibility(8);
                }
                this.itemView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        TchapContactAdapter.this.mListener.onSelectItem(participantAdapterItem, -1);
                    }
                });
            }
        }

        private void loadContactPresence(final ImageView imageView, final ParticipantAdapterItem participantAdapterItem, final int i) {
            imageView.setVisibility(VectorUtils.isUserOnline(TchapContactAdapter.this.mContext, TchapContactAdapter.this.mSession, participantAdapterItem.mUserId, new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    ImageView imageView = imageView;
                    if (imageView != null) {
                        imageView.setVisibility(VectorUtils.isUserOnline(TchapContactAdapter.this.mContext, TchapContactAdapter.this.mSession, participantAdapterItem.mUserId, null) ? 0 : 8);
                        TchapContactAdapter.this.notifyItemChanged(i);
                    }
                }
            }) ? 0 : 8);
        }
    }

    public class ContactViewHolder_ViewBinding implements Unbinder {
        private ContactViewHolder target;

        public ContactViewHolder_ViewBinding(ContactViewHolder contactViewHolder, View view) {
            this.target = contactViewHolder;
            contactViewHolder.vContactAvatar = (ImageView) Utils.findRequiredViewAsType(view, R.id.contact_avatar, "field 'vContactAvatar'", ImageView.class);
            contactViewHolder.vContactStatus = (ImageView) Utils.findRequiredViewAsType(view, R.id.contact_status, "field 'vContactStatus'", ImageView.class);
            contactViewHolder.vContactName = (TextView) Utils.findRequiredViewAsType(view, R.id.contact_name, "field 'vContactName'", TextView.class);
            contactViewHolder.vContactDomain = (TextView) Utils.findRequiredViewAsType(view, R.id.contact_domain, "field 'vContactDomain'", TextView.class);
        }

        public void unbind() {
            ContactViewHolder contactViewHolder = this.target;
            if (contactViewHolder != null) {
                this.target = null;
                contactViewHolder.vContactAvatar = null;
                contactViewHolder.vContactStatus = null;
                contactViewHolder.vContactName = null;
                contactViewHolder.vContactDomain = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    public interface OnSelectItemListener {
        void onSelectItem(ParticipantAdapterItem participantAdapterItem, int i);

        void onSelectItem(Room room, int i);
    }

    public TchapContactAdapter(Context context, OnSelectItemListener onSelectItemListener, RoomInvitationListener roomInvitationListener, MoreRoomActionListener moreRoomActionListener) {
        super(context, roomInvitationListener, moreRoomActionListener);
        this.mListener = onSelectItemListener;
        this.mNoContactAccessPlaceholder = context.getString(R.string.no_contact_access_placeholder);
        this.mNoResultPlaceholder = context.getString(R.string.no_result_placeholder);
        AdapterSection adapterSection = new AdapterSection(context, context.getString(R.string.direct_chats_header), -1, R.layout.adapter_item_room_view, -2, -4, new ArrayList(), RoomUtils.getRoomsDateComparator(this.mSession, false));
        this.mDirectChatsSection = adapterSection;
        this.mDirectChatsSection.setEmptyViewPlaceholder(context.getString(R.string.no_conversation_placeholder), context.getString(R.string.no_result_placeholder));
        AdapterSection adapterSection2 = new AdapterSection(context, context.getString(R.string.local_address_book_header), R.layout.adapter_local_contacts_sticky_header_subview, R.layout.adapter_item_contact_view, 0, 1, new ArrayList(), ParticipantAdapterItem.alphaComparator);
        this.mLocalContactsSection = adapterSection2;
        this.mLocalContactsSection.setEmptyViewPlaceholder(!ContactsManager.getInstance().isContactBookAccessAllowed() ? this.mNoContactAccessPlaceholder : this.mNoResultPlaceholder);
        KnownContactsAdapterSection knownContactsAdapterSection = new KnownContactsAdapterSection(context, context.getString(R.string.user_directory_header), -1, R.layout.adapter_item_contact_view, -2, 1, new ArrayList(), null);
        this.mKnownContactsSection = knownContactsAdapterSection;
        this.mKnownContactsSection.setEmptyViewPlaceholder(null, context.getString(R.string.no_result_placeholder));
        this.mKnownContactsSection.setIsHiddenWhenNoFilter(true);
        addSection(this.mLocalContactsSection);
        addSection(this.mKnownContactsSection);
    }

    /* access modifiers changed from: protected */
    public ViewHolder createSubViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            View inflate = from.inflate(R.layout.adapter_section_header_local, viewGroup, false);
            inflate.setBackgroundColor(-65281);
            return new HeaderViewHolder(inflate);
        } else if (i == -4) {
            return new RoomViewHolder(from.inflate(R.layout.adapter_item_room_view, viewGroup, false));
        } else {
            if (i != 1) {
                return null;
            }
            return new ContactViewHolder(from.inflate(R.layout.adapter_item_contact_view, viewGroup, false));
        }
    }

    /* access modifiers changed from: protected */
    public void populateViewHolder(int i, ViewHolder viewHolder, int i2) {
        if (i == -4) {
            RoomViewHolder roomViewHolder = (RoomViewHolder) viewHolder;
            final Room room = (Room) getItemForPosition(i2);
            roomViewHolder.populateViews(this.mContext, this.mSession, room, true, false, this.mMoreRoomActionListener);
            roomViewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    TchapContactAdapter.this.mListener.onSelectItem(room, -1);
                }
            });
        } else if (i == 0) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            for (Pair pair : getSectionsArray()) {
                if (((Integer) pair.first).intValue() == i2) {
                    headerViewHolder.populateViews((AdapterSection) pair.second);
                    return;
                }
            }
        } else if (i == 1) {
            ((ContactViewHolder) viewHolder).populateViews((ParticipantAdapterItem) getItemForPosition(i2), i2);
        }
    }

    /* access modifiers changed from: protected */
    public int applyFilter(String str) {
        int filterRoomSection = filterRoomSection(this.mDirectChatsSection, str) + 0 + filterLocalContacts(str);
        return TextUtils.isEmpty(str) ? filterRoomSection + filterKnownContacts(str) : filterRoomSection;
    }

    public void setRooms(List<Room> list) {
        this.mDirectChatsSection.setItems(list, this.mCurrentFilterPattern);
        if (!TextUtils.isEmpty(this.mCurrentFilterPattern)) {
            filterRoomSection(this.mDirectChatsSection, String.valueOf(this.mCurrentFilterPattern));
        }
        updateSections();
    }

    public void setLocalContacts(List<ParticipantAdapterItem> list) {
        this.mLocalContactsSection.setEmptyViewPlaceholder(!ContactsManager.getInstance().isContactBookAccessAllowed() ? this.mNoContactAccessPlaceholder : this.mNoResultPlaceholder);
        if (DinsicUtils.isExternalTchapSession(this.mSession)) {
            this.mLocalContactsSection.setItems(removeExternalTchapUsers(list), this.mCurrentFilterPattern);
        } else {
            this.mLocalContactsSection.setItems(list, this.mCurrentFilterPattern);
        }
        if (!TextUtils.isEmpty(this.mCurrentFilterPattern)) {
            filterLocalContacts(String.valueOf(this.mCurrentFilterPattern));
        }
        updateSections();
    }

    public void setKnownContacts(List<ParticipantAdapterItem> list) {
        if (DinsicUtils.isExternalTchapSession(this.mSession)) {
            this.mKnownContactsSection.setItems(removeExternalTchapUsers(list), this.mCurrentFilterPattern);
        } else {
            this.mKnownContactsSection.setItems(list, this.mCurrentFilterPattern);
        }
        if (!TextUtils.isEmpty(this.mCurrentFilterPattern)) {
            filterKnownContacts(String.valueOf(this.mCurrentFilterPattern));
        } else {
            filterKnownContacts(null);
        }
        updateSections();
    }

    public void setFilteredKnownContacts(List<ParticipantAdapterItem> list, String str) {
        Collections.sort(list, ParticipantAdapterItem.getComparator(this.mSession));
        this.mKnownContactsSection.setFilteredItems(list, str);
        updateSections();
    }

    public void setKnownContactsLimited(boolean z) {
        this.mKnownContactsSection.setIsLimited(z);
    }

    public void setKnownContactsExtraTitle(String str) {
        this.mKnownContactsSection.setCustomHeaderExtra(str);
    }

    public void updateKnownContact(User user) {
        int sectionHeaderPosition = getSectionHeaderPosition(this.mKnownContactsSection) + 1;
        List filteredItems = this.mKnownContactsSection.getFilteredItems();
        for (int i = 0; i < filteredItems.size(); i++) {
            if (TextUtils.equals(user.user_id, ((ParticipantAdapterItem) filteredItems.get(i)).mUserId)) {
                notifyItemChanged(sectionHeaderPosition + i);
            }
        }
    }

    private List<ParticipantAdapterItem> removeExternalTchapUsers(List<ParticipantAdapterItem> list) {
        ArrayList arrayList = new ArrayList();
        for (ParticipantAdapterItem participantAdapterItem : list) {
            if (!DinsicUtils.isExternalTchapUser(participantAdapterItem.mUserId)) {
                arrayList.add(participantAdapterItem);
            }
        }
        return arrayList;
    }

    private int filterLocalContacts(String str) {
        if (!TextUtils.isEmpty(str)) {
            ArrayList arrayList = new ArrayList();
            String trim = str.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).trim();
            for (ParticipantAdapterItem participantAdapterItem : new ArrayList(this.mLocalContactsSection.getItems())) {
                if (participantAdapterItem.startsWith(trim)) {
                    arrayList.add(participantAdapterItem);
                }
            }
            this.mLocalContactsSection.setFilteredItems(arrayList, str);
        } else {
            this.mLocalContactsSection.resetFilter();
        }
        return this.mLocalContactsSection.getFilteredItems().size();
    }

    public void filterAccountKnownContacts(String str) {
        filterKnownContacts(str);
        updateSections();
    }

    private int filterKnownContacts(String str) {
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str)) {
            String lowerCase = str.trim().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
            for (ParticipantAdapterItem participantAdapterItem : new ArrayList(this.mKnownContactsSection.getItems())) {
                if (participantAdapterItem.startsWith(lowerCase)) {
                    arrayList.add(participantAdapterItem);
                }
            }
        }
        Collections.sort(arrayList, ParticipantAdapterItem.getComparator(this.mSession));
        this.mKnownContactsSection.setFilteredItems(arrayList, str);
        setKnownContactsLimited(false);
        setKnownContactsExtraTitle(null);
        return arrayList.size();
    }

    public void removeDirectChat(String str) {
        if (this.mDirectChatsSection.removeItem(this.mSession.getDataHandler().getRoom(str))) {
            updateSections();
        }
    }
}
