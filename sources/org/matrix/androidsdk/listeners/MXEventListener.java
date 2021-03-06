package org.matrix.androidsdk.listeners;

import java.util.List;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public class MXEventListener implements IMXEventListener {
    public void onAccountDataUpdated() {
    }

    public void onAccountInfoUpdate(MyUser myUser) {
    }

    public void onBingEvent(Event event, RoomState roomState, BingRule bingRule) {
    }

    public void onBingRulesUpdate() {
    }

    public void onCryptoSyncComplete() {
    }

    public void onDirectMessageChatRoomsListUpdate() {
    }

    public void onEventDecrypted(String str, String str2) {
    }

    public void onEventSent(Event event, String str) {
    }

    public void onEventSentStateUpdated(Event event) {
    }

    public void onGroupInvitedUsersListUpdate(String str) {
    }

    public void onGroupProfileUpdate(String str) {
    }

    public void onGroupRoomsListUpdate(String str) {
    }

    public void onGroupUsersListUpdate(String str) {
    }

    public void onIgnoredUsersListUpdate() {
    }

    public void onInitialSyncComplete(String str) {
    }

    public void onJoinGroup(String str) {
    }

    public void onJoinRoom(String str) {
    }

    public void onLeaveGroup(String str) {
    }

    public void onLeaveRoom(String str) {
    }

    public void onLiveEvent(Event event, RoomState roomState) {
    }

    public void onLiveEventsChunkProcessed(String str, String str2) {
    }

    public void onNewGroupInvitation(String str) {
    }

    public void onNewRoom(String str) {
    }

    public void onNotificationCountUpdate(String str) {
    }

    public void onPresenceUpdate(Event event, User user) {
    }

    public void onReadMarkerEvent(String str) {
    }

    public void onReceiptEvent(String str, List<String> list) {
    }

    public void onRoomFlush(String str) {
    }

    public void onRoomInternalUpdate(String str) {
    }

    public void onRoomKick(String str) {
    }

    public void onRoomTagEvent(String str) {
    }

    public void onStoreReady() {
    }

    public void onSyncError(MatrixError matrixError) {
    }

    public void onToDeviceEvent(Event event) {
    }
}
