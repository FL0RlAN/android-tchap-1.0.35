package im.vector.util;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Icon;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.view.GravityCompat;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.DinumUtilsKt;
import im.vector.Matrix;
import im.vector.activity.VectorRoomActivity;
import im.vector.adapters.AdapterUtils;
import im.vector.ui.themes.ThemeUtils;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.BingRulesManager;
import org.matrix.androidsdk.core.BingRulesManager.RoomNotificationState;
import org.matrix.androidsdk.core.EventDisplay;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomAccountData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;

public class RoomUtils {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = RoomUtils.class.getSimpleName();
    private Room room;
    private MXSession session;

    public interface HistoricalRoomActionListener {
        void onForgotRoom(Room room);
    }

    public interface MoreActionListener {
        void addHomeScreenShortcut(MXSession mXSession, String str);

        void moveToConversations(MXSession mXSession, String str);

        void moveToFavorites(MXSession mXSession, String str);

        void moveToLowPriority(MXSession mXSession, String str);

        void onForgetRoom(MXSession mXSession, String str);

        void onLeaveRoom(MXSession mXSession, String str);

        void onToggleDirectChat(MXSession mXSession, String str);

        void onUpdateRoomNotificationsState(MXSession mXSession, String str, RoomNotificationState roomNotificationState);
    }

    public static Comparator<Room> getRoomsDateComparator(final MXSession mXSession, final boolean z) {
        return new Comparator<Room>() {
            private Comparator<RoomSummary> mRoomSummaryComparator;
            private final Map<String, RoomSummary> mSummaryByRoomIdMap = new HashMap();

            private Comparator<RoomSummary> getSummaryComparator() {
                if (this.mRoomSummaryComparator == null) {
                    this.mRoomSummaryComparator = RoomUtils.getRoomSummaryComparator(z);
                }
                return this.mRoomSummaryComparator;
            }

            private RoomSummary getSummary(String str) {
                if (TextUtils.isEmpty(str)) {
                    return null;
                }
                RoomSummary roomSummary = (RoomSummary) this.mSummaryByRoomIdMap.get(str);
                if (roomSummary == null) {
                    roomSummary = mXSession.getDataHandler().getStore().getSummary(str);
                    if (roomSummary != null) {
                        this.mSummaryByRoomIdMap.put(str, roomSummary);
                    }
                }
                return roomSummary;
            }

            public int compare(Room room, Room room2) {
                return getSummaryComparator().compare(getSummary(room.getRoomId()), getSummary(room2.getRoomId()));
            }
        };
    }

    public static Comparator<Room> getNotifCountRoomsComparator(final MXSession mXSession, final boolean z, final boolean z2) {
        return new Comparator<Room>() {
            private Comparator<RoomSummary> mRoomSummaryComparator;
            private final Map<String, RoomSummary> mSummaryByRoomIdMap = new HashMap();

            private Comparator<RoomSummary> getSummaryComparator() {
                if (this.mRoomSummaryComparator == null) {
                    this.mRoomSummaryComparator = RoomUtils.getNotifCountRoomSummaryComparator(mXSession.getDataHandler().getBingRulesManager(), z, z2);
                }
                return this.mRoomSummaryComparator;
            }

            private RoomSummary getSummary(String str) {
                if (TextUtils.isEmpty(str)) {
                    return null;
                }
                RoomSummary roomSummary = (RoomSummary) this.mSummaryByRoomIdMap.get(str);
                if (roomSummary == null) {
                    roomSummary = mXSession.getDataHandler().getStore().getSummary(str);
                    if (roomSummary != null) {
                        this.mSummaryByRoomIdMap.put(str, roomSummary);
                    }
                }
                return roomSummary;
            }

            public int compare(Room room, Room room2) {
                return getSummaryComparator().compare(getSummary(room.getRoomId()), getSummary(room2.getRoomId()));
            }
        };
    }

    public static Comparator<Room> getHistoricalRoomsComparator(final MXSession mXSession, final boolean z) {
        return new Comparator<Room>() {
            public int compare(Room room, Room room2) {
                return RoomUtils.getRoomSummaryComparator(z).compare(mXSession.getDataHandler().getStore(room.getRoomId()).getSummary(room.getRoomId()), mXSession.getDataHandler().getStore(room2.getRoomId()).getSummary(room2.getRoomId()));
            }
        };
    }

    /* access modifiers changed from: private */
    public static Comparator<RoomSummary> getRoomSummaryComparator(final boolean z) {
        return new Comparator<RoomSummary>() {
            /* JADX WARNING: Removed duplicated region for block: B:15:0x0038  */
            /* JADX WARNING: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
            public int compare(RoomSummary roomSummary, RoomSummary roomSummary2) {
                int i = -1;
                if (!(roomSummary == null || roomSummary.getLatestReceivedEvent() == null)) {
                    if (!(roomSummary2 == null || roomSummary2.getLatestReceivedEvent() == null)) {
                        long originServerTs = roomSummary2.getLatestReceivedEvent().getOriginServerTs() - roomSummary.getLatestReceivedEvent().getOriginServerTs();
                        if (originServerTs <= 0) {
                            if (originServerTs >= 0) {
                                i = 0;
                            }
                        }
                    }
                    return !z ? -i : i;
                }
                i = 1;
                if (!z) {
                }
            }
        };
    }

    /* access modifiers changed from: private */
    public static Comparator<RoomSummary> getNotifCountRoomSummaryComparator(final BingRulesManager bingRulesManager, final boolean z, final boolean z2) {
        return new Comparator<RoomSummary>() {
            public int compare(RoomSummary roomSummary, RoomSummary roomSummary2) {
                int i;
                int i2;
                int i3;
                int i4;
                int i5;
                int i6;
                if (roomSummary != null) {
                    i3 = roomSummary.getHighlightCount();
                    i2 = roomSummary.getNotificationCount();
                    i = roomSummary.getUnreadEventsCount();
                    if (bingRulesManager.isRoomMentionOnly(roomSummary.getRoomId())) {
                        i2 = i3;
                    }
                } else {
                    i3 = 0;
                    i2 = 0;
                    i = 0;
                }
                if (roomSummary2 != null) {
                    i6 = roomSummary2.getHighlightCount();
                    i5 = roomSummary2.getNotificationCount();
                    i4 = roomSummary2.getUnreadEventsCount();
                    if (bingRulesManager.isRoomMentionOnly(roomSummary2.getRoomId())) {
                        i5 = i6;
                    }
                } else {
                    i6 = 0;
                    i5 = 0;
                    i4 = 0;
                }
                if (!(roomSummary == null || roomSummary.getLatestReceivedEvent() == null)) {
                    if (roomSummary2 == null || roomSummary2.getLatestReceivedEvent() == null) {
                        return -1;
                    }
                    if (!z || i6 <= 0 || i3 != 0) {
                        if (z && i6 == 0 && i3 > 0) {
                            return -1;
                        }
                        if (!z || i5 <= 0 || i2 != 0) {
                            if (z && i5 == 0 && i2 > 0) {
                                return -1;
                            }
                            if (!z2 || i4 <= 0 || i != 0) {
                                if (z2 && i4 == 0 && i > 0) {
                                    return -1;
                                }
                                long originServerTs = roomSummary2.getLatestReceivedEvent().getOriginServerTs() - roomSummary.getLatestReceivedEvent().getOriginServerTs();
                                if (originServerTs <= 0) {
                                    if (originServerTs < 0) {
                                        return -1;
                                    }
                                    return 0;
                                }
                            }
                        }
                    }
                }
                return 1;
            }
        };
    }

    public static String getRoomTimestamp(Context context, Event event) {
        String tsToString = AdapterUtils.tsToString(context, event.getOriginServerTs(), false);
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.today));
        sb.append(" ");
        String sb2 = sb.toString();
        return tsToString.startsWith(sb2) ? tsToString.substring(sb2.length()) : tsToString;
    }

    public static CharSequence getRoomMessageToDisplay(Context context, MXSession mXSession, RoomSummary roomSummary) {
        String str;
        String str2;
        CharSequence charSequence = null;
        if (roomSummary == null) {
            return null;
        }
        Event latestReceivedEvent = roomSummary.getLatestReceivedEvent();
        if (latestReceivedEvent != null) {
            Room room2 = mXSession.getDataHandler().getStore().getRoom(roomSummary.getRoomId());
            if (room2 != null) {
                if (System.currentTimeMillis() - latestReceivedEvent.getOriginServerTs() <= TimeUnit.DAYS.toMillis((long) DinumUtilsKt.getRoomRetention(room2))) {
                    EventDisplay eventDisplay = new EventDisplay(context);
                    eventDisplay.setPrependMessagesWithAuthor(false);
                    charSequence = eventDisplay.getTextualDisplay(Integer.valueOf(ThemeUtils.INSTANCE.getColor(context, R.attr.vctr_room_notification_text_color)), latestReceivedEvent, roomSummary.getLatestRoomState());
                }
            }
        }
        if (!roomSummary.isInvited() || roomSummary.getInviterUserId() == null) {
            return charSequence;
        }
        RoomState latestRoomState = roomSummary.getLatestRoomState();
        String inviterUserId = roomSummary.getInviterUserId();
        String userId = roomSummary.getUserId();
        if (latestRoomState != null) {
            str = latestRoomState.getMemberName(inviterUserId);
            str2 = latestRoomState.getMemberName(userId);
        } else {
            str = getMemberDisplayNameFromUserId(context, roomSummary.getUserId(), inviterUserId);
            str2 = getMemberDisplayNameFromUserId(context, roomSummary.getUserId(), userId);
        }
        if (TextUtils.equals(mXSession.getMyUserId(), roomSummary.getUserId())) {
            return context.getString(R.string.notice_room_invite_you, new Object[]{str});
        }
        return context.getString(R.string.notice_room_invite, new Object[]{str, str2});
    }

    private static String getMemberDisplayNameFromUserId(Context context, String str, String str2) {
        if (!(str == null || str2 == null)) {
            MXSession mXSession = Matrix.getMXSession(context, str);
            if (mXSession != null && mXSession.isAlive()) {
                User user = mXSession.getDataHandler().getStore().getUser(str2);
                return (user == null || TextUtils.isEmpty(user.displayname)) ? str2 : user.displayname;
            }
        }
        return null;
    }

    public static void displayPopupMenu(Context context, MXSession mXSession, Room room2, View view, boolean z, boolean z2, MoreActionListener moreActionListener) {
        if (moreActionListener != null) {
            displayPopupMenu(context, mXSession, room2, view, null, z, z2, moreActionListener, null);
        }
    }

    public static void displayTchapPopupMenu(Context context, MXSession mXSession, Room room2, View view, View view2, boolean z, boolean z2, MoreActionListener moreActionListener) {
        if (moreActionListener != null) {
            displayPopupMenu(context, mXSession, room2, view, view2, z, z2, moreActionListener, null);
        }
    }

    public static void displayHistoricalRoomMenu(Context context, MXSession mXSession, Room room2, View view, HistoricalRoomActionListener historicalRoomActionListener) {
        if (historicalRoomActionListener != null) {
            displayPopupMenu(context, mXSession, room2, view, null, false, false, null, historicalRoomActionListener);
        }
    }

    private static void displayPopupMenu(Context context, MXSession mXSession, final Room room2, View view, View view2, boolean z, boolean z2, MoreActionListener moreActionListener, HistoricalRoomActionListener historicalRoomActionListener) {
        PopupMenu popupMenu;
        Context context2 = context;
        View view3 = view;
        final HistoricalRoomActionListener historicalRoomActionListener2 = historicalRoomActionListener;
        if (room2 != null) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.PopMenuStyle);
            if (VERSION.SDK_INT >= 19) {
                popupMenu = new PopupMenu(contextThemeWrapper, view, GravityCompat.END);
            } else {
                popupMenu = new PopupMenu(contextThemeWrapper, view);
            }
            PopupMenu popupMenu2 = popupMenu;
            popupMenu2.getMenuInflater().inflate(R.menu.vector_home_room_settings, popupMenu2.getMenu());
            ThemeUtils.INSTANCE.tintMenuIcons(popupMenu2.getMenu(), ThemeUtils.INSTANCE.getColor(context, R.attr.vctr_settings_icon_tint_color));
            if (room2.isLeft()) {
                popupMenu2.getMenu().setGroupVisible(R.id.active_room_actions, false);
                popupMenu2.getMenu().setGroupVisible(R.id.add_shortcut_actions, false);
                popupMenu2.getMenu().setGroupVisible(R.id.historical_room_actions, true);
                if (historicalRoomActionListener2 != null) {
                    popupMenu2.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.action_forget_room) {
                                historicalRoomActionListener2.onForgotRoom(room2);
                            }
                            return true;
                        }
                    });
                }
            } else {
                popupMenu2.getMenu().setGroupVisible(R.id.active_room_actions, true);
                popupMenu2.getMenu().setGroupVisible(R.id.add_shortcut_actions, false);
                popupMenu2.getMenu().setGroupVisible(R.id.historical_room_actions, false);
                if (!z) {
                    popupMenu2.getMenu().findItem(R.id.ic_action_select_pin).setIcon(null);
                }
                RoomNotificationState roomNotificationState = mXSession.getDataHandler().getBingRulesManager().getRoomNotificationState(room2.getRoomId());
                if (RoomNotificationState.ALL_MESSAGES_NOISY != roomNotificationState) {
                    popupMenu2.getMenu().findItem(R.id.ic_action_notifications_noisy).setIcon(null);
                }
                if (RoomNotificationState.ALL_MESSAGES != roomNotificationState) {
                    popupMenu2.getMenu().findItem(R.id.ic_action_notifications_all_message).setIcon(null);
                }
                if (RoomNotificationState.MENTIONS_ONLY != roomNotificationState) {
                    popupMenu2.getMenu().findItem(R.id.ic_action_notifications_mention_only).setIcon(null);
                }
                if (RoomNotificationState.MUTE != roomNotificationState) {
                    popupMenu2.getMenu().findItem(R.id.ic_action_notifications_mute).setIcon(null);
                }
                RoomMember member = room2.getMember(mXSession.getMyUserId());
                final boolean z3 = member != null && member.kickedOrBanned();
                if (z3) {
                    MenuItem findItem = popupMenu2.getMenu().findItem(R.id.ic_action_select_remove);
                    if (findItem != null) {
                        findItem.setTitle(R.string.forget_room);
                    }
                }
                if (moreActionListener != null) {
                    final boolean z4 = z;
                    final MoreActionListener moreActionListener2 = moreActionListener;
                    final MXSession mXSession2 = mXSession;
                    final Room room3 = room2;
                    final View view4 = view2;
                    AnonymousClass7 r0 = new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            if (itemId != R.id.ic_action_add_homescreen_shortcut) {
                                switch (itemId) {
                                    case R.id.ic_action_notifications_all_message /*2131296598*/:
                                        moreActionListener2.onUpdateRoomNotificationsState(mXSession2, room3.getRoomId(), RoomNotificationState.ALL_MESSAGES);
                                        View view = view4;
                                        if (view != null) {
                                            view.setVisibility(8);
                                            break;
                                        }
                                        break;
                                    case R.id.ic_action_notifications_mention_only /*2131296599*/:
                                        moreActionListener2.onUpdateRoomNotificationsState(mXSession2, room3.getRoomId(), RoomNotificationState.MENTIONS_ONLY);
                                        View view2 = view4;
                                        if (view2 != null) {
                                            view2.setVisibility(8);
                                            break;
                                        }
                                        break;
                                    case R.id.ic_action_notifications_mute /*2131296600*/:
                                        moreActionListener2.onUpdateRoomNotificationsState(mXSession2, room3.getRoomId(), RoomNotificationState.MUTE);
                                        View view3 = view4;
                                        if (view3 != null) {
                                            view3.setVisibility(0);
                                            break;
                                        }
                                        break;
                                    case R.id.ic_action_notifications_noisy /*2131296601*/:
                                        moreActionListener2.onUpdateRoomNotificationsState(mXSession2, room3.getRoomId(), RoomNotificationState.ALL_MESSAGES_NOISY);
                                        View view4 = view4;
                                        if (view4 != null) {
                                            view4.setVisibility(8);
                                            break;
                                        }
                                        break;
                                    default:
                                        switch (itemId) {
                                            case R.id.ic_action_select_pin /*2131296612*/:
                                                if (!z4) {
                                                    moreActionListener2.moveToFavorites(mXSession2, room3.getRoomId());
                                                    break;
                                                } else {
                                                    moreActionListener2.moveToConversations(mXSession2, room3.getRoomId());
                                                    break;
                                                }
                                            case R.id.ic_action_select_remove /*2131296613*/:
                                                if (!z3) {
                                                    moreActionListener2.onLeaveRoom(mXSession2, room3.getRoomId());
                                                    break;
                                                } else {
                                                    moreActionListener2.onForgetRoom(mXSession2, room3.getRoomId());
                                                    break;
                                                }
                                        }
                                }
                            } else {
                                moreActionListener2.addHomeScreenShortcut(mXSession2, room3.getRoomId());
                            }
                            return false;
                        }
                    };
                    popupMenu2.setOnMenuItemClickListener(r0);
                }
            }
            try {
                Field[] declaredFields = popupMenu2.getClass().getDeclaredFields();
                int length = declaredFields.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Field field = declaredFields[i];
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object obj = field.get(popupMenu2);
                        Class.forName(obj.getClass().getName()).getMethod("setForceShowIcon", new Class[]{Boolean.TYPE}).invoke(obj, new Object[]{Boolean.valueOf(true)});
                        break;
                    }
                    i++;
                }
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## displayPopupMenu() : failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
            popupMenu2.show();
        }
    }

    public static void showLeaveRoomDialog(final Context context, final MXSession mXSession, Room room2, final OnClickListener onClickListener) {
        final PowerLevels powerLevels = room2.getState().getPowerLevels();
        if (powerLevels != null) {
            if (((float) powerLevels.getUserPowerLevel(mXSession.getMyUserId())) >= 100.0f) {
                room2.getJoinedMembersAsync(new ApiCallback<List<RoomMember>>() {
                    private void onDone(boolean z) {
                        String str;
                        if (z) {
                            str = context.getString(R.string.tchap_room_admin_leave_prompt_msg);
                        } else {
                            str = context.getString(R.string.room_participants_leave_prompt_msg);
                        }
                        new Builder(context).setTitle((int) R.string.room_participants_leave_prompt_title).setMessage((CharSequence) str).setPositiveButton((int) R.string.leave, onClickListener).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                    }

                    public void onSuccess(List<RoomMember> list) {
                        boolean z;
                        Iterator it = list.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                z = true;
                                break;
                            }
                            String userId = ((RoomMember) it.next()).getUserId();
                            if (!TextUtils.equals(userId, mXSession.getMyUserId()) && ((float) powerLevels.getUserPowerLevel(userId)) >= 100.0f) {
                                z = false;
                                break;
                            }
                        }
                        onDone(z);
                    }

                    public void onNetworkError(Exception exc) {
                        String access$200 = RoomUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("showLeaveRoomDialog onNetworkError ");
                        sb.append(exc.getLocalizedMessage());
                        Log.e(access$200, sb.toString(), exc);
                        onDone(true);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        String access$200 = RoomUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("showLeaveRoomDialog onMatrixError ");
                        sb.append(matrixError.getLocalizedMessage());
                        Log.e(access$200, sb.toString());
                        onDone(true);
                    }

                    public void onUnexpectedError(Exception exc) {
                        String access$200 = RoomUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("showLeaveRoomDialog onUnexpectedError ");
                        sb.append(exc.getLocalizedMessage());
                        Log.e(access$200, sb.toString(), exc);
                        onDone(true);
                    }
                });
                return;
            }
        }
        new Builder(context).setTitle((int) R.string.room_participants_leave_prompt_title).setMessage((int) R.string.room_participants_leave_prompt_msg).setPositiveButton((int) R.string.leave, onClickListener).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0062  */
    public static void addHomeScreenShortcut(Context context, MXSession mXSession, String str) {
        Bitmap bitmap;
        if (VERSION.SDK_INT >= 26) {
            ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(ShortcutManager.class);
            if (shortcutManager.isRequestPinShortcutSupported()) {
                Room room2 = mXSession.getDataHandler().getRoom(str);
                if (room2 != null) {
                    String roomDisplayName = DinsicUtils.getRoomDisplayName(context, room2);
                    String roomAvatarUrl = DinsicUtils.getRoomAvatarUrl(room2);
                    if (!TextUtils.isEmpty(roomAvatarUrl)) {
                        File thumbnailCacheFile = mXSession.getMediaCache().thumbnailCacheFile(roomAvatarUrl, context.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size));
                        if (thumbnailCacheFile != null) {
                            Options options = new Options();
                            options.inPreferredConfig = Config.ARGB_8888;
                            try {
                                bitmap = BitmapFactory.decodeFile(thumbnailCacheFile.getPath(), options);
                            } catch (OutOfMemoryError e) {
                                Log.e(LOG_TAG, "decodeFile failed with an oom", e);
                            }
                            if (bitmap == null) {
                                bitmap = VectorUtils.getAvatar(context, VectorUtils.getAvatarColor(str), roomDisplayName, true);
                            }
                            Icon createWithBitmap = Icon.createWithBitmap(bitmap);
                            Intent intent = new Intent(context, VectorRoomActivity.class);
                            intent.setFlags(268468224);
                            intent.setAction("android.intent.action.VIEW");
                            intent.putExtra("EXTRA_ROOM_ID", str);
                            shortcutManager.requestPinShortcut(new ShortcutInfo.Builder(context, str).setShortLabel(roomDisplayName).setIcon(createWithBitmap).setIntent(intent).build(), null);
                        }
                    }
                    bitmap = null;
                    if (bitmap == null) {
                    }
                    Icon createWithBitmap2 = Icon.createWithBitmap(bitmap);
                    Intent intent2 = new Intent(context, VectorRoomActivity.class);
                    intent2.setFlags(268468224);
                    intent2.setAction("android.intent.action.VIEW");
                    intent2.putExtra("EXTRA_ROOM_ID", str);
                    shortcutManager.requestPinShortcut(new ShortcutInfo.Builder(context, str).setShortLabel(roomDisplayName).setIcon(createWithBitmap2).setIntent(intent2).build(), null);
                }
            }
        }
    }

    public static void updateRoomTag(MXSession mXSession, String str, Double d, String str2, ApiCallback<Void> apiCallback) {
        Room room2 = mXSession.getDataHandler().getRoom(str);
        if (room2 != null) {
            String str3 = null;
            RoomAccountData accountData = room2.getAccountData();
            if (accountData != null && accountData.hasTags()) {
                str3 = (String) accountData.getKeys().iterator().next();
            }
            if (d == null) {
                d = Double.valueOf(0.0d);
                if (str2 != null) {
                    d = mXSession.tagOrderToBeAtIndex(0, Integer.MAX_VALUE, str2);
                }
            }
            room2.replaceTag(str3, str2, d, apiCallback);
        }
    }

    public static void toggleDirectChat(MXSession mXSession, String str, ApiCallback<Void> apiCallback) {
        if (mXSession.getDataHandler().getRoom(str) != null) {
            mXSession.toggleDirectChatRoom(str, null, apiCallback);
        }
    }

    public static boolean isDirectChat(MXSession mXSession, String str) {
        return str != null && mXSession.getDataHandler().getDirectChatRoomIdsList().contains(str);
    }

    public static List<Room> getFilteredRooms(Context context, List<Room> list, CharSequence charSequence) {
        String trim = charSequence != null ? charSequence.toString().trim() : null;
        if (TextUtils.isEmpty(trim)) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        Pattern compile = Pattern.compile(Pattern.quote(trim), 2);
        for (Room room2 : list) {
            if (compile.matcher(DinsicUtils.getRoomDisplayName(context, room2)).find()) {
                arrayList.add(room2);
            }
        }
        return arrayList;
    }

    public static String formatUnreadMessagesCounter(int i) {
        if (i <= 0) {
            return null;
        }
        if (i <= 999) {
            return String.valueOf(i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(i / 1000);
        sb.append(".");
        sb.append((i % 1000) / 100);
        sb.append("K");
        return sb.toString();
    }
}
