package org.matrix.androidsdk.listeners;

import java.util.List;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public interface IMXEventListener {
    void onAccountDataUpdated();

    void onAccountInfoUpdate(MyUser myUser);

    void onBingEvent(Event event, RoomState roomState, BingRule bingRule);

    void onBingRulesUpdate();

    void onCryptoSyncComplete();

    void onDirectMessageChatRoomsListUpdate();

    void onEventDecrypted(String str, String str2);

    void onEventSent(Event event, String str);

    void onEventSentStateUpdated(Event event);

    void onGroupInvitedUsersListUpdate(String str);

    void onGroupProfileUpdate(String str);

    void onGroupRoomsListUpdate(String str);

    void onGroupUsersListUpdate(String str);

    void onIgnoredUsersListUpdate();

    void onInitialSyncComplete(String str);

    void onJoinGroup(String str);

    void onJoinRoom(String str);

    void onLeaveGroup(String str);

    void onLeaveRoom(String str);

    void onLiveEvent(Event event, RoomState roomState);

    void onLiveEventsChunkProcessed(String str, String str2);

    void onNewGroupInvitation(String str);

    void onNewRoom(String str);

    void onNotificationCountUpdate(String str);

    void onPresenceUpdate(Event event, User user);

    void onReadMarkerEvent(String str);

    void onReceiptEvent(String str, List<String> list);

    void onRoomFlush(String str);

    void onRoomInternalUpdate(String str);

    void onRoomKick(String str);

    void onRoomTagEvent(String str);

    void onStoreReady();

    void onSyncError(MatrixError matrixError);

    void onToDeviceEvent(Event event);
}
