package im.vector.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesKt;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.HexagonMaskView;
import im.vector.adapters.AbsAdapter.MoreRoomActionListener;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.RoomUtils;
import im.vector.util.VectorUtils;
import java.util.Set;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.BingRulesManager.RoomNotificationState;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.RoomTag;
import org.matrix.androidsdk.data.store.IMXStore;

public class RoomViewHolder extends ViewHolder {
    private static final String LOG_TAG = RoomViewHolder.class.getSimpleName();
    @BindView(2131296923)
    ImageView vRoomAvatar;
    @BindView(2131296926)
    HexagonMaskView vRoomAvatarHexagon;
    @BindView(2131296949)
    TextView vRoomDomain;
    @BindView(2131296925)
    View vRoomEncryptedIcon;
    @BindView(2131296950)
    TextView vRoomLastMessage;
    @BindView(2131296951)
    View vRoomMoreActionAnchor;
    @BindView(2131296952)
    View vRoomMoreActionClickArea;
    @BindView(2131296954)
    TextView vRoomName;
    @BindView(2131296955)
    TextView vRoomNameServer;
    @BindView(2131296837)
    ImageView vRoomNotificationMute;
    @BindView(2131296963)
    View vRoomPinFavorite;
    @BindView(2131296977)
    TextView vRoomTimestamp;
    @BindView(2131296976)
    TextView vRoomUnreadCount;
    @BindView(2131297022)
    TextView vSenderDisplayName;

    public RoomViewHolder(View view) {
        super(view);
        ButterKnife.bind((Object) this, view);
    }

    /* JADX WARNING: Removed duplicated region for block: B:109:0x0278  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x02a7  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x02ad  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0170  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01bf  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01c8  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0230  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0232  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x023a  */
    public void populateViews(Context context, MXSession mXSession, final Room room, boolean z, boolean z2, final MoreRoomActionListener moreRoomActionListener) {
        TextView textView;
        View view;
        if (room == null) {
            Log.e(LOG_TAG, "## populateViews() : null room");
        } else if (mXSession == null) {
            Log.e(LOG_TAG, "## populateViews() : null session");
        } else if (mXSession.getDataHandler() == null) {
            Log.e(LOG_TAG, "## populateViews() : null dataHandler");
        } else {
            IMXStore store = mXSession.getDataHandler().getStore(room.getRoomId());
            if (store == null) {
                Log.e(LOG_TAG, "## populateViews() : null Store");
                return;
            }
            RoomSummary summary = store.getSummary(room.getRoomId());
            if (summary == null) {
                Log.e(LOG_TAG, "## populateViews() : null roomSummary");
                return;
            }
            int unreadEventsCount = summary.getUnreadEventsCount();
            int color = ThemeUtils.INSTANCE.getColor(context, R.attr.vctr_default_icon_tint_color);
            int color2 = ContextCompat.getColor(context, R.color.vector_fuchsia_color);
            int color3 = ContextCompat.getColor(context, R.color.vector_silver_color);
            int highlightCount = summary.getHighlightCount();
            int notificationCount = summary.getNotificationCount();
            if (room.getDataHandler() != null && room.getDataHandler().getBingRulesManager().isRoomMentionOnly(room.getRoomId())) {
                notificationCount = highlightCount;
            }
            if (z2 || highlightCount != 0) {
                color = color2;
            } else if (notificationCount == 0) {
                color = unreadEventsCount != 0 ? color3 : 0;
            }
            boolean z3 = true;
            if (z2 || notificationCount > 0) {
                this.vRoomUnreadCount.setText(z2 ? "!" : RoomUtils.formatUnreadMessagesCounter(notificationCount));
                this.vRoomUnreadCount.setTypeface(null, 1);
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(0);
                gradientDrawable.setCornerRadius(100.0f);
                gradientDrawable.setColor(color);
                this.vRoomUnreadCount.setBackground(gradientDrawable);
                this.vRoomUnreadCount.setVisibility(0);
            } else {
                this.vRoomUnreadCount.setVisibility(8);
            }
            String roomDisplayName = DinsicUtils.getRoomDisplayName(context, room);
            String nameFromDisplayName = DinsicUtils.getNameFromDisplayName(roomDisplayName);
            TextView textView2 = this.vRoomDomain;
            if (textView2 != null) {
                textView2.setText(DinsicUtils.getDomainFromDisplayName(roomDisplayName));
            }
            if (!(this.vSenderDisplayName == null || summary.getLatestReceivedEvent() == null)) {
                this.vSenderDisplayName.setText(DinsicUtils.getNameFromDisplayName(mXSession.getDataHandler().getUser(summary.getLatestReceivedEvent().getSender()).displayname));
                this.vSenderDisplayName.setVisibility(0);
            }
            if (this.vRoomAvatarHexagon != null) {
                ImageView imageView = this.vRoomAvatar;
                if (imageView != null) {
                    if (z) {
                        imageView.setVisibility(0);
                        this.vRoomAvatarHexagon.setVisibility(8);
                        VectorUtils.loadRoomAvatar(context, mXSession, this.vRoomAvatar, room);
                    } else {
                        imageView.setVisibility(8);
                        this.vRoomAvatarHexagon.setVisibility(0);
                        VectorUtils.loadRoomAvatar(context, mXSession, (ImageView) this.vRoomAvatarHexagon, room);
                    }
                    if (this.vRoomNameServer != null) {
                        this.vRoomName.setText(nameFromDisplayName);
                    } else if (MXPatterns.isRoomAlias(nameFromDisplayName)) {
                        String str = ":";
                        String[] split = nameFromDisplayName.split(str);
                        StringBuilder sb = new StringBuilder();
                        sb.append(split[0]);
                        sb.append(str);
                        String sb2 = sb.toString();
                        String str2 = split[1];
                        this.vRoomName.setLines(1);
                        this.vRoomName.setText(sb2);
                        this.vRoomNameServer.setText(str2);
                        this.vRoomNameServer.setVisibility(0);
                        this.vRoomNameServer.setTypeface(null, unreadEventsCount != 0 ? 1 : 0);
                    } else {
                        this.vRoomName.setLines(2);
                        this.vRoomNameServer.setVisibility(8);
                        this.vRoomName.setText(nameFromDisplayName);
                    }
                    if (this.vRoomLastMessage != null) {
                        CharSequence roomMessageToDisplay = RoomUtils.getRoomMessageToDisplay(context, mXSession, summary);
                        if (roomMessageToDisplay != null) {
                            this.vRoomLastMessage.setText(roomMessageToDisplay);
                            this.vRoomLastMessage.setVisibility(0);
                            if (notificationCount > 0) {
                                this.vRoomLastMessage.setTypeface(null, 1);
                                this.vRoomLastMessage.setTextColor(ContextCompat.getColor(context, R.color.tchap_primary_text_color));
                            } else {
                                this.vRoomLastMessage.setTypeface(null, 0);
                                this.vRoomLastMessage.setTextColor(ContextCompat.getColor(context, R.color.tchap_third_text_color));
                            }
                            if (this.vSenderDisplayName != null && roomMessageToDisplay.toString().startsWith(this.vSenderDisplayName.getText().toString())) {
                                this.vSenderDisplayName.setVisibility(8);
                            }
                        } else {
                            this.vRoomLastMessage.setVisibility(8);
                            TextView textView3 = this.vSenderDisplayName;
                            if (textView3 != null) {
                                textView3.setVisibility(8);
                            }
                        }
                    }
                    this.vRoomEncryptedIcon.setVisibility(!room.isEncrypted() ? 0 : 4);
                    textView = this.vRoomTimestamp;
                    if (textView != null) {
                        textView.setText(RoomUtils.getRoomTimestamp(context, summary.getLatestReceivedEvent()));
                        TextView textView4 = this.vRoomTimestamp;
                        TextView textView5 = this.vRoomLastMessage;
                        textView4.setVisibility(textView5 != null ? textView5.getVisibility() : 0);
                    }
                    view = this.vRoomMoreActionClickArea;
                    if (!(view == null || this.vRoomMoreActionAnchor == null)) {
                        view.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                MoreRoomActionListener moreRoomActionListener = moreRoomActionListener;
                                if (moreRoomActionListener != null) {
                                    moreRoomActionListener.onTchapMoreActionClick(RoomViewHolder.this.vRoomMoreActionAnchor, room, RoomViewHolder.this.vRoomNotificationMute);
                                }
                            }
                        });
                    }
                    RoomNotificationState roomNotificationState = mXSession.getDataHandler().getBingRulesManager().getRoomNotificationState(room.getRoomId());
                    if (this.vRoomNotificationMute != null) {
                        if (roomNotificationState.equals(RoomNotificationState.MUTE)) {
                            this.vRoomNotificationMute.setVisibility(0);
                        } else {
                            this.vRoomNotificationMute.setVisibility(8);
                        }
                    }
                    if (!(room == null || this.vRoomPinFavorite == null)) {
                        Set keys = room.getAccountData().getKeys();
                        if (keys == null || !keys.contains(RoomTag.ROOM_TAG_FAVOURITE)) {
                            z3 = false;
                        }
                        if (!z3) {
                            this.vRoomPinFavorite.setVisibility(0);
                        } else {
                            this.vRoomPinFavorite.setVisibility(4);
                        }
                    }
                }
            }
            HexagonMaskView hexagonMaskView = this.vRoomAvatarHexagon;
            if (hexagonMaskView != null) {
                VectorUtils.loadRoomAvatar(context, mXSession, (ImageView) hexagonMaskView, room);
                if (TextUtils.equals(DinsicUtils.getRoomAccessRule(room), RoomAccessRulesKt.RESTRICTED)) {
                    this.vRoomAvatarHexagon.setBorderSettings(ContextCompat.getColor(context, R.color.restricted_room_avatar_border_color), 3);
                } else {
                    this.vRoomAvatarHexagon.setBorderSettings(ContextCompat.getColor(context, R.color.unrestricted_room_avatar_border_color), 10);
                }
            } else {
                ImageView imageView2 = this.vRoomAvatar;
                if (imageView2 != null) {
                    VectorUtils.loadRoomAvatar(context, mXSession, imageView2, room);
                }
            }
            if (this.vRoomNameServer != null) {
            }
            if (this.vRoomLastMessage != null) {
            }
            this.vRoomEncryptedIcon.setVisibility(!room.isEncrypted() ? 0 : 4);
            textView = this.vRoomTimestamp;
            if (textView != null) {
            }
            view = this.vRoomMoreActionClickArea;
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MoreRoomActionListener moreRoomActionListener = moreRoomActionListener;
                    if (moreRoomActionListener != null) {
                        moreRoomActionListener.onTchapMoreActionClick(RoomViewHolder.this.vRoomMoreActionAnchor, room, RoomViewHolder.this.vRoomNotificationMute);
                    }
                }
            });
            RoomNotificationState roomNotificationState2 = mXSession.getDataHandler().getBingRulesManager().getRoomNotificationState(room.getRoomId());
            if (this.vRoomNotificationMute != null) {
            }
            Set keys2 = room.getAccountData().getKeys();
            z3 = false;
            if (!z3) {
            }
        }
    }
}
