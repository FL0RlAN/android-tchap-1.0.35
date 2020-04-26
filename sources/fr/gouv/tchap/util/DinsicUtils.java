package fr.gouv.tchap.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.FragmentActivity;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.rest.client.TchapThirdPidRestClient;
import fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesKt;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.activity.VectorRoomActivity;
import im.vector.adapters.ParticipantAdapterItem;
import im.vector.contacts.Contact;
import im.vector.contacts.ContactsManager;
import im.vector.util.RoomUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomEmailInvitation;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.data.RoomTag;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.CreateRoomParams;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomCreateContent;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;

public class DinsicUtils {
    private static final String LOG_TAG = "DinsicUtils";

    public static String capitalize(String str) {
        StringBuilder sb = new StringBuilder();
        if (!str.isEmpty()) {
            sb.append(str.substring(0, 1).toUpperCase());
            if (str.length() > 1) {
                sb.append(str.substring(1));
            }
        }
        return sb.toString();
    }

    public static String getDeviceName() {
        String trim = Build.MANUFACTURER.trim();
        String trim2 = Build.MODEL.trim();
        if (trim2.startsWith(trim)) {
            return capitalize(trim2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(capitalize(trim));
        sb.append(" ");
        sb.append(trim2);
        return sb.toString();
    }

    public static String getHomeServerNameFromMXIdentifier(String str) {
        String str2 = ":";
        if (str.contains(str2)) {
            return str.substring(str.indexOf(str2) + 1);
        }
        return null;
    }

    public static String getHomeServerDisplayNameFromMXIdentifier(String str) {
        String homeServerNameFromMXIdentifier = getHomeServerNameFromMXIdentifier(str);
        if (homeServerNameFromMXIdentifier == null) {
            return null;
        }
        if (homeServerNameFromMXIdentifier.contains("tchap.gouv.fr")) {
            String[] split = homeServerNameFromMXIdentifier.split("\\.");
            if (split.length >= 4) {
                homeServerNameFromMXIdentifier = split[split.length - 4];
            }
        }
        return capitalize(homeServerNameFromMXIdentifier);
    }

    public static String getNameFromDisplayName(String str) {
        return (str == null || !str.contains("[")) ? str : str.split("\\[")[0].trim();
    }

    public static String getDomainFromDisplayName(String str) {
        if (str == null || !str.contains("[")) {
            return null;
        }
        String str2 = str.split("\\[")[1];
        if (str2.contains("]")) {
            return str2.split("\\]")[0].trim();
        }
        return null;
    }

    public static String computeDisplayNameFromUserId(String str) {
        if (str != null && MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER.matcher(str).matches()) {
            String substring = str.substring(1, str.indexOf(":"));
            boolean isExternalTchapUser = isExternalTchapUser(str);
            String str2 = HelpFormatter.DEFAULT_OPT_PREFIX;
            if (!isExternalTchapUser) {
                int lastIndexOf = substring.lastIndexOf(str2);
                if (-1 != lastIndexOf) {
                    String[] split = substring.substring(0, lastIndexOf).split("\\.");
                    StringBuilder sb = new StringBuilder();
                    int length = split.length;
                    for (int i = 0; i < length; i++) {
                        String str3 = split[i];
                        if (!str3.isEmpty()) {
                            if (sb.length() > 0) {
                                sb.append(" ");
                            }
                            if (str3.contains(str2)) {
                                String[] split2 = str3.split(str2);
                                for (int i2 = 0; i2 < split2.length - 1; i2++) {
                                    String str4 = split2[i2];
                                    if (!str4.isEmpty()) {
                                        sb.append(str4.substring(0, 1).toUpperCase());
                                        if (str4.length() > 1) {
                                            sb.append(str4.substring(1));
                                        }
                                        sb.append(str2);
                                    }
                                }
                                str3 = split2[split2.length - 1];
                            }
                            if (!str3.isEmpty()) {
                                sb.append(str3.substring(0, 1).toUpperCase());
                                if (str3.length() > 1) {
                                    sb.append(str3.substring(1));
                                }
                            }
                        }
                    }
                    return sb.toString();
                }
            } else if (substring.split(str2).length == 2) {
                return substring.replace(str2, "@");
            } else {
                return substring;
            }
        }
        return null;
    }

    public static boolean isExternalTchapSession(MXSession mXSession) {
        String host = mXSession.getHomeServerConfig().getHomeserverUri().getHost();
        return host.contains(".e.") || host.contains(".externe.");
    }

    public static boolean isExternalTchapUser(String str) {
        String homeServerNameFromMXIdentifier = getHomeServerNameFromMXIdentifier(str);
        if (homeServerNameFromMXIdentifier != null) {
            return isExternalTchapServer(homeServerNameFromMXIdentifier);
        }
        return true;
    }

    public static boolean isExternalTchapServer(String str) {
        return str.startsWith("e.") || str.startsWith("agent.externe.");
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    public static void editContactForm(Context context, Activity activity, String str, Contact contact) {
        boolean z;
        String str2 = "contact_id";
        String str3 = "lookup";
        LayoutInflater.from(context);
        try {
            z = true;
            Cursor query = context.getContentResolver().query(Data.CONTENT_URI, new String[]{"display_name", str3, str2}, "mimetype = ? AND contact_id = ?", new String[]{"vnd.android.cursor.item/name", contact.getContactId()}, null);
            if (query != null && query.moveToNext()) {
                String string = query.getString(query.getColumnIndex(str2));
                Uri lookupUri = Contacts.getLookupUri((long) Integer.parseInt(string), query.getString(query.getColumnIndex(str3)));
                Intent intent = new Intent("android.intent.action.EDIT");
                intent.setDataAndType(lookupUri, "vnd.android.cursor.item/contact");
                intent.putExtra("finishActivityOnSaveCompleted", true);
                activity.startActivity(intent);
                if (z) {
                    Builder builder = new Builder(activity);
                    builder.setMessage((CharSequence) str);
                    builder.setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) null);
                    builder.create().show();
                    return;
                }
                return;
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## editContactForm(): Exception - Msg=");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
        }
        z = false;
        if (z) {
        }
    }

    public static void editContact(final FragmentActivity fragmentActivity, final Context context, final ParticipantAdapterItem participantAdapterItem) {
        if (ContactsManager.getInstance().isContactBookAccessAllowed()) {
            Builder builder = new Builder(fragmentActivity);
            builder.setMessage((CharSequence) fragmentActivity.getString(R.string.people_invalid_warning_msg));
            builder.setNegativeButton((int) R.string.cancel, (OnClickListener) null).setPositiveButton((int) R.string.action_edit_contact_form, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Context context = context;
                    FragmentActivity fragmentActivity = fragmentActivity;
                    DinsicUtils.editContactForm(context, fragmentActivity, fragmentActivity.getString(R.string.people_edit_contact_warning_msg), participantAdapterItem.mContact);
                }
            });
            builder.create().show();
            return;
        }
        Builder builder2 = new Builder(fragmentActivity);
        builder2.setMessage((CharSequence) fragmentActivity.getString(R.string.people_invalid_warning_msg));
        builder2.setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder2.create().show();
    }

    public static boolean participantAlreadyAdded(List<ParticipantAdapterItem> list, ParticipantAdapterItem participantAdapterItem) {
        Iterator it = list.iterator();
        boolean z = false;
        for (boolean z2 = !it.hasNext(); !z2; z2 = z || !it.hasNext()) {
            ParticipantAdapterItem participantAdapterItem2 = (ParticipantAdapterItem) it.next();
            if (participantAdapterItem2 != null && participantAdapterItem2.mIsValid) {
                z = participantAdapterItem2.mUserId.equals(participantAdapterItem.mUserId);
            }
        }
        return z;
    }

    public static boolean removeParticipantIfExist(List<ParticipantAdapterItem> list, ParticipantAdapterItem participantAdapterItem) {
        Iterator it = list.iterator();
        boolean z = false;
        for (boolean z2 = !it.hasNext(); !z2; z2 = z || !it.hasNext()) {
            ParticipantAdapterItem participantAdapterItem2 = (ParticipantAdapterItem) it.next();
            if (participantAdapterItem2 != null && participantAdapterItem2.mIsValid) {
                boolean equals = participantAdapterItem2.mUserId.equals(participantAdapterItem.mUserId);
                if (equals) {
                    it.remove();
                }
                z = equals;
            }
        }
        return z;
    }

    public static void alertSimpleMsg(FragmentActivity fragmentActivity, String str) {
        Builder builder = new Builder(fragmentActivity);
        builder.setMessage((CharSequence) str);
        builder.setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public static boolean openDirectChat(final VectorAppCompatActivity vectorAppCompatActivity, String str, final MXSession mXSession, boolean z) {
        Room isDirectChatRoomAlreadyExist = isDirectChatRoomAlreadyExist(str, mXSession, true);
        AnonymousClass4 r2 = new ApiCallback<String>() {
            public void onSuccess(String str) {
                vectorAppCompatActivity.hideWaitingView();
                HashMap hashMap = new HashMap();
                hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", mXSession.getMyUserId());
                hashMap.put("EXTRA_ROOM_ID", str);
                hashMap.put(VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER, Boolean.valueOf(true));
                Log.d(DinsicUtils.LOG_TAG, "## prepareDirectChatCallBack: onSuccess - start goToRoomPage");
                CommonActivityUtils.goToRoomPage(vectorAppCompatActivity, mXSession, hashMap);
            }

            private void onError(final String str) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (str != null) {
                            Toast.makeText(vectorAppCompatActivity, str, 1).show();
                        }
                        vectorAppCompatActivity.hideWaitingView();
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        };
        if (isDirectChatRoomAlreadyExist == null) {
            if (z) {
                if (!isExternalTchapSession(mXSession)) {
                    vectorAppCompatActivity.showWaitingView();
                    createDirectChat(mXSession, str, r2);
                    return true;
                }
                alertSimpleMsg(vectorAppCompatActivity, vectorAppCompatActivity.getString(R.string.room_creation_forbidden));
            }
            return false;
        } else if (isDirectChatRoomAlreadyExist.isInvited()) {
            vectorAppCompatActivity.showWaitingView();
            mXSession.joinRoom(isDirectChatRoomAlreadyExist.getRoomId(), r2);
            return true;
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", str);
            hashMap.put("EXTRA_ROOM_ID", isDirectChatRoomAlreadyExist.getRoomId());
            CommonActivityUtils.goToRoomPage(vectorAppCompatActivity, mXSession, hashMap);
            return true;
        }
    }

    public static Room isDirectChatRoomAlreadyExist(String str, MXSession mXSession, boolean z) {
        String str2 = LOG_TAG;
        if (mXSession != null) {
            IMXStore store = mXSession.getDataHandler().getStore();
            if (store.getDirectChatRoomsDict() != null) {
                HashMap hashMap = new HashMap(store.getDirectChatRoomsDict());
                if (hashMap.containsKey(str)) {
                    ArrayList arrayList = new ArrayList((Collection) hashMap.get(str));
                    if (!arrayList.isEmpty()) {
                        Iterator it = arrayList.iterator();
                        Room room = null;
                        Room room2 = null;
                        while (it.hasNext()) {
                            String str3 = (String) it.next();
                            Room room3 = mXSession.getDataHandler().getRoom(str3, false);
                            if (room3 != null && room3.isReady() && !room3.isLeaving()) {
                                boolean isInvited = room3.isInvited();
                                if (z || !isInvited) {
                                    String str4 = "## isDirectChatRoomAlreadyExist(): for user: ";
                                    if (!MXPatterns.isUserId(str)) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(str4);
                                        sb.append(str);
                                        sb.append(" room id: ");
                                        sb.append(str3);
                                        Log.d(str2, sb.toString());
                                        return room3;
                                    }
                                    RoomMember member = room3.getMember(str);
                                    if (member == null) {
                                        continue;
                                    } else {
                                        if (member.membership.equals("join")) {
                                            if (!isInvited) {
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append(str4);
                                                sb2.append(str);
                                                sb2.append(" (join) room id: ");
                                                sb2.append(str3);
                                                Log.d(str2, sb2.toString());
                                                return room3;
                                            }
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append("## isDirectChatRoomAlreadyExist(): set candidate (invite-join) room id: ");
                                            sb3.append(str3);
                                            Log.d(str2, sb3.toString());
                                        } else if (member.membership.equals("invite")) {
                                            if (room == null) {
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append("## isDirectChatRoomAlreadyExist(): set candidate (join-invite) room id: ");
                                                sb4.append(str3);
                                                Log.d(str2, sb4.toString());
                                            }
                                        } else if (member.membership.equals("leave")) {
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append("## isDirectChatRoomAlreadyExist(): set candidate (join-left) room id: ");
                                            sb5.append(str3);
                                            Log.d(str2, sb5.toString());
                                            room2 = room3;
                                        }
                                        room = room3;
                                    }
                                }
                            }
                        }
                        String str5 = "## isDirectChatRoomAlreadyExist(): user: ";
                        if (room != null) {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str5);
                            sb6.append(str);
                            sb6.append(" (invite) room id: ");
                            sb6.append(room.getRoomId());
                            Log.d(str2, sb6.toString());
                            return room;
                        } else if (room2 != null) {
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append(str5);
                            sb7.append(str);
                            sb7.append(" (leave) room id: ");
                            sb7.append(room2.getRoomId());
                            Log.d(str2, sb7.toString());
                            return room2;
                        }
                    }
                }
            }
        }
        StringBuilder sb8 = new StringBuilder();
        sb8.append("## isDirectChatRoomAlreadyExist(): for user=");
        sb8.append(str);
        sb8.append(" no found room");
        Log.d(str2, sb8.toString());
        return null;
    }

    public static void startDirectChat(final VectorAppCompatActivity vectorAppCompatActivity, final MXSession mXSession, final ParticipantAdapterItem participantAdapterItem) {
        if (!participantAdapterItem.mIsValid) {
            editContact(vectorAppCompatActivity, vectorAppCompatActivity, participantAdapterItem);
        } else if (MXPatterns.isUserId(participantAdapterItem.mUserId)) {
            User user = mXSession.getDataHandler().getUser(participantAdapterItem.mUserId);
            if (user == null) {
                user = new User();
                user.user_id = participantAdapterItem.mUserId;
                user.avatar_url = participantAdapterItem.mAvatarUrl;
                user.displayname = participantAdapterItem.mDisplayName;
            } else {
                if (user.displayname == null) {
                    user.displayname = participantAdapterItem.mDisplayName;
                }
                if (user.avatar_url == null) {
                    user.avatar_url = participantAdapterItem.mAvatarUrl;
                }
            }
            startDirectChat(vectorAppCompatActivity, mXSession, user);
        } else {
            String string = vectorAppCompatActivity.getResources().getString(R.string.room_invite_people);
            if (openDirectChat(vectorAppCompatActivity, participantAdapterItem.mUserId, mXSession, false)) {
                return;
            }
            if (isExternalTchapSession(mXSession)) {
                alertSimpleMsg(vectorAppCompatActivity, vectorAppCompatActivity.getResources().getString(R.string.room_creation_forbidden));
                return;
            }
            Builder builder = new Builder(vectorAppCompatActivity);
            builder.setMessage((CharSequence) string);
            builder.setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    DinsicUtils.openDirectChat(vectorAppCompatActivity, participantAdapterItem.mUserId, mXSession, true);
                }
            }).setNegativeButton((int) R.string.cancel, (OnClickListener) null);
            builder.create().show();
        }
    }

    public static void startDirectChat(VectorAppCompatActivity vectorAppCompatActivity, MXSession mXSession, User user) {
        if (!openDirectChat(vectorAppCompatActivity, user.user_id, mXSession, false)) {
            HashMap hashMap = new HashMap();
            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", mXSession.getMyUserId());
            if (user.displayname == null) {
                user.displayname = computeDisplayNameFromUserId(user.user_id);
            }
            hashMap.put(VectorRoomActivity.EXTRA_TCHAP_USER, user);
            CommonActivityUtils.goToRoomPage(vectorAppCompatActivity, mXSession, hashMap);
        }
    }

    public static boolean createDirectChat(MXSession mXSession, String str, ApiCallback<String> apiCallback) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CreateRoomParams createRoomParams = new CreateRoomParams();
        createRoomParams.setDirectMessage();
        createRoomParams.addParticipantIds(mXSession.getHomeServerConfig(), Arrays.asList(new String[]{str}));
        Event event = new Event();
        event.type = RoomAccessRulesKt.EVENT_TYPE_STATE_ROOM_ACCESS_RULES;
        HashMap hashMap = new HashMap();
        hashMap.put(RoomAccessRulesKt.STATE_EVENT_CONTENT_KEY_RULE, RoomAccessRulesKt.DIRECT);
        event.updateContent(JsonUtils.getGson(false).toJsonTree(hashMap));
        event.stateKey = "";
        createRoomParams.initialStates = Arrays.asList(new Event[]{event});
        mXSession.createRoom(createRoomParams, apiCallback);
        return true;
    }

    public static void joinRoom(RoomPreviewData roomPreviewData, ApiCallback<Void> apiCallback) {
        Room room = roomPreviewData.getSession().getDataHandler().getRoom(roomPreviewData.getRoomId());
        RoomEmailInvitation roomEmailInvitation = roomPreviewData.getRoomEmailInvitation();
        room.joinWithThirdPartySigned(roomPreviewData.getRoomIdOrAlias(), roomEmailInvitation != null ? roomEmailInvitation.signUrl : null, apiCallback);
    }

    public static void onNewJoinedRoom(final Activity activity, RoomPreviewData roomPreviewData) {
        final MXSession session = roomPreviewData.getSession();
        Room room = session.getDataHandler().getRoom(roomPreviewData.getRoomId());
        if (room != null) {
            String myUserId = session.getMyUserId();
            final HashMap hashMap = new HashMap();
            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", myUserId);
            hashMap.put("EXTRA_ROOM_ID", roomPreviewData.getRoomId());
            if (roomPreviewData.getEventId() != null) {
                hashMap.put(VectorRoomActivity.EXTRA_EVENT_ID, roomPreviewData.getEventId());
            }
            if (!getRoomAccessRule(room).equals(RoomAccessRulesKt.DIRECT) || room.isDirect()) {
                CommonActivityUtils.goToRoomPage(activity, session, hashMap);
                return;
            }
            Log.d(LOG_TAG, "## onNewJoinedRoom(): this new joined room is direct");
            CommonActivityUtils.setToggleDirectMessageRoom(session, roomPreviewData.getRoomId(), null, new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    CommonActivityUtils.goToRoomPage(activity, session, hashMap);
                }
            });
        }
    }

    public static Comparator<Room> getRoomsComparator(final MXSession mXSession, final boolean z) {
        return new Comparator<Room>() {
            private Comparator<Room> mRoomsDateComparator;

            private Comparator<Room> getRoomsDateComparator() {
                if (this.mRoomsDateComparator == null) {
                    this.mRoomsDateComparator = RoomUtils.getRoomsDateComparator(mXSession, z);
                }
                return this.mRoomsDateComparator;
            }

            public int compare(Room room, Room room2) {
                Set keys = room.getAccountData().getKeys();
                String str = RoomTag.ROOM_TAG_FAVOURITE;
                boolean z = false;
                boolean z2 = keys != null && keys.contains(str);
                Set keys2 = room2.getAccountData().getKeys();
                if (keys2 != null && keys2.contains(str)) {
                    z = true;
                }
                int i = -1;
                if (z2 && !z) {
                    if (z) {
                        i = 1;
                    }
                    return i;
                } else if (z2 || !z) {
                    return getRoomsDateComparator().compare(room, room2);
                } else {
                    if (!z) {
                        i = 1;
                    }
                    return i;
                }
            }
        };
    }

    public static String getRoomDisplayName(Context context, Room room) {
        String roomDisplayName = room.getRoomDisplayName(context, null);
        if (!TextUtils.isEmpty(roomDisplayName)) {
            return MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER.matcher(roomDisplayName).matches() ? computeDisplayNameFromUserId(roomDisplayName) : roomDisplayName;
        }
        String string = context.getString(R.string.room_displayname_empty_room);
        if (!room.isDirect()) {
            return string;
        }
        for (RoomMember roomMember : room.getState().getDisplayableLoadedMembers()) {
            if (!TextUtils.equals(roomMember.getUserId(), room.getDataHandler().getUserId())) {
                return roomMember.getName();
            }
        }
        return string;
    }

    public static boolean isFederatedRoom(Room room) {
        RoomCreateContent roomCreateContent = room.getState().getRoomCreateContent();
        if (roomCreateContent == null || roomCreateContent.isFederated == null) {
            return true;
        }
        return roomCreateContent.isFederated.booleanValue();
    }

    public static String getRoomAccessRule(Room room) {
        String str;
        List stateEvents = room.getState().getStateEvents(new HashSet(Arrays.asList(new String[]{RoomAccessRulesKt.EVENT_TYPE_STATE_ROOM_ACCESS_RULES})));
        boolean isEmpty = stateEvents.isEmpty();
        String str2 = LOG_TAG;
        if (isEmpty) {
            Log.d(str2, "## getRoomAccessRule(): no rule is defined");
            str = null;
        } else {
            Event event = (Event) stateEvents.get(stateEvents.size() - 1);
            for (int i = 0; i < stateEvents.size() - 1; i++) {
                Event event2 = (Event) stateEvents.get(i);
                if (event.originServerTs < event2.originServerTs) {
                    event = event2;
                }
            }
            str = RoomAccessRulesKt.getRule(event);
            StringBuilder sb = new StringBuilder();
            sb.append("## getRoomAccessRule(): the rule ");
            sb.append(str);
            sb.append(" is defined");
            Log.d(str2, sb.toString());
        }
        if (str == null) {
            return room.isDirect() ? RoomAccessRulesKt.DIRECT : RoomAccessRulesKt.RESTRICTED;
        }
        return str;
    }

    public static void setRoomAccessRule(MXSession mXSession, Room room, String str, ApiCallback<Void> apiCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put(RoomAccessRulesKt.STATE_EVENT_CONTENT_KEY_RULE, str);
        mXSession.getRoomsApiClient().sendStateEvent(room.getRoomId(), RoomAccessRulesKt.EVENT_TYPE_STATE_ROOM_ACCESS_RULES, "", hashMap, apiCallback);
    }

    public static String getRoomAvatarUrl(Room room) {
        if (room.isDirect()) {
            return room.getAvatarUrl();
        }
        return room.getState().getAvatarUrl();
    }

    public static List<ParticipantAdapterItem> getContactsFromDirectChats(final MXSession mXSession) {
        ArrayList arrayList = new ArrayList();
        if (mXSession == null || mXSession.getDataHandler() == null) {
            Log.e(LOG_TAG, "## getContactsFromDirectChats() : null session");
            return arrayList;
        }
        IMXStore store = mXSession.getDataHandler().getStore();
        if (store.getDirectChatRoomsDict() != null) {
            for (final String str : new ArrayList(store.getDirectChatRoomsDict().keySet())) {
                if (MXPatterns.isUserId(str)) {
                    if (!str.equals(mXSession.getMyUserId())) {
                        List list = (List) store.getDirectChatRoomsDict().get(str);
                        if (list != null && !list.isEmpty()) {
                            Iterator it = list.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                Room room = store.getRoom((String) it.next());
                                if (room != null) {
                                    RoomMember member = room.getMember(str);
                                    if (member != null && ((TextUtils.equals(member.membership, "join") || TextUtils.equals(member.membership, "invite")) && !TextUtils.isEmpty(member.displayname))) {
                                        Contact contact = new Contact("null");
                                        contact.setDisplayName(member.displayname);
                                        ParticipantAdapterItem participantAdapterItem = new ParticipantAdapterItem(contact);
                                        participantAdapterItem.mUserId = str;
                                        arrayList.add(participantAdapterItem);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else if (Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                    new TchapThirdPidRestClient(mXSession.getHomeServerConfig()).lookup(str, "email", new ApiCallback<String>() {
                        public void onSuccess(String str) {
                            Log.i(DinsicUtils.LOG_TAG, "## getContactsFromDirectChats: lookup success");
                            if (!TextUtils.isEmpty(str)) {
                                DinsicUtils.updateDirectChatRoomsOnDiscoveredUser(mXSession, str, str);
                            }
                        }

                        private void onError(String str) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("## getContactsFromDirectChats: lookup failed ");
                            sb.append(str);
                            Log.e(DinsicUtils.LOG_TAG, sb.toString());
                        }

                        public void onNetworkError(Exception exc) {
                            onError(exc.getMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            onError(matrixError.getMessage());
                        }

                        public void onUnexpectedError(Exception exc) {
                            onError(exc.getMessage());
                        }
                    });
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public static void updateDirectChatRoomsOnDiscoveredUser(final MXSession mXSession, String str, final String str2) {
        IMXStore store = mXSession.getDataHandler().getStore();
        boolean containsKey = store.getDirectChatRoomsDict().containsKey(str);
        String str3 = LOG_TAG;
        if (!containsKey) {
            Log.v(str3, "## updateDirectChatRoomsOnDiscoveredUser() direct chat map has been already updated");
            return;
        }
        for (final String str4 : new ArrayList((Collection) store.getDirectChatRoomsDict().get(str))) {
            StringBuilder sb = new StringBuilder();
            sb.append("## updateDirectChatRoomsOnDiscoveredUser() ");
            sb.append(str4);
            Log.i(str3, sb.toString());
            mXSession.toggleDirectChatRoom(str4, null, new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    mXSession.toggleDirectChatRoom(str4, str2, new ApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            Log.i(DinsicUtils.LOG_TAG, "## updateDirectChatRoomsOnDiscoveredUser() succeeded to update direct chat map ");
                        }

                        private void onFails(String str) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("## updateDirectChatRoomsOnDiscoveredUser() failed to update direct chat map ");
                            sb.append(str);
                            Log.e(DinsicUtils.LOG_TAG, sb.toString());
                        }

                        public void onNetworkError(Exception exc) {
                            onFails(exc.getLocalizedMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            onFails(matrixError.getLocalizedMessage());
                        }

                        public void onUnexpectedError(Exception exc) {
                            onFails(exc.getLocalizedMessage());
                        }
                    });
                }

                private void onFails(String str) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## updateDirectChatRoomsOnDiscoveredUser() failed to update direct chat map ");
                    sb.append(str);
                    Log.e(DinsicUtils.LOG_TAG, sb.toString());
                }

                public void onNetworkError(Exception exc) {
                    onFails(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    onFails(matrixError.getLocalizedMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onFails(exc.getLocalizedMessage());
                }
            });
        }
    }
}
