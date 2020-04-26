package org.matrix.androidsdk.data.store;

import android.content.Context;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.crypto.interfaces.CryptoStore;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomAccountData;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.metrics.MetricsListener;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.group.Group;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;
import org.matrix.androidsdk.rest.model.sync.AccountData;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;

public interface IMXStore extends CryptoStore {
    void addFilter(String str, String str2);

    void addMXStoreListener(IMXStoreListener iMXStoreListener);

    boolean areReceiptsReady();

    String avatarURL();

    void clear();

    void close();

    void commit();

    void deleteAllRoomMessages(String str, boolean z);

    void deleteEvent(Event event);

    void deleteGroup(String str);

    void deleteRoom(String str);

    void deleteRoomData(String str);

    long diskUsage();

    String displayName();

    boolean doesEventExist(String str, String str2);

    int eventsCountAfter(String str, String str2);

    void flushGroup(Group group);

    void flushRoomEvents(String str);

    void flushSummaries();

    void flushSummary(RoomSummary roomSummary);

    AccountDataElement getAccountDataElement(String str);

    String getAntivirusServerPublicKey();

    Context getContext();

    Map<String, List<String>> getDirectChatRoomsDict();

    TokensChunkEvents getEarlierMessages(String str, String str2, int i);

    Event getEvent(String str, String str2);

    List<ReceiptData> getEventReceipts(String str, String str2, boolean z, boolean z2);

    String getEventStreamToken();

    Map<String, String> getFilters();

    Group getGroup(String str);

    Collection<Group> getGroups();

    List<String> getIgnoredUserIdsList();

    Event getLatestEvent(String str);

    List<Event> getLatestUnsentEvents(String str);

    Event getOldestEvent(String str);

    long getPreloadTime();

    ReceiptData getReceipt(String str, String str2);

    Room getRoom(String str);

    Collection<Event> getRoomMessages(String str);

    void getRoomStateEvents(String str, ApiCallback<List<Event>> apiCallback);

    Collection<Room> getRooms();

    Set<String> getRoomsWithoutURLPreviews();

    Map<String, Long> getStats();

    Collection<RoomSummary> getSummaries();

    RoomSummary getSummary(String str);

    List<Event> getUndeliveredEvents(String str);

    List<Event> getUnknownDeviceEvents(String str);

    User getUser(String str);

    Map<String, Object> getUserWidgets();

    Collection<User> getUsers();

    boolean isCorrupted();

    boolean isEventRead(String str, String str2, String str3);

    boolean isPermanent();

    boolean isReady();

    boolean isURLPreviewEnabled();

    void open();

    void post(Runnable runnable);

    void removeMXStoreListener(IMXStoreListener iMXStoreListener);

    void setAntivirusServerPublicKey(String str);

    boolean setAvatarURL(String str, long j);

    void setCorrupted(String str);

    void setDirectChatRoomsDict(Map<String, List<String>> map);

    boolean setDisplayName(String str, long j);

    void setEventStreamToken(String str);

    void setIgnoredUserIdsList(List<String> list);

    void setMetricsListener(MetricsListener metricsListener);

    void setRoomsWithoutURLPreview(Set<String> set);

    void setThirdPartyIdentifiers(List<ThirdPartyIdentifier> list);

    void setURLPreviewEnabled(boolean z);

    void setUserWidgets(Map<String, Object> map);

    void storeAccountData(AccountData accountData);

    void storeBackToken(String str, String str2);

    void storeGroup(Group group);

    void storeLiveRoomEvent(Event event);

    void storeLiveStateForRoom(String str);

    boolean storeReceipt(ReceiptData receiptData, String str);

    void storeRoom(Room room);

    void storeRoomAccountData(String str, RoomAccountData roomAccountData);

    void storeRoomEvents(String str, TokensChunkEvents tokensChunkEvents, Direction direction);

    void storeRoomStateEvent(String str, Event event);

    void storeSummary(RoomSummary roomSummary);

    void storeUser(User user);

    List<ThirdPartyIdentifier> thirdPartyIdentifiers();

    List<Event> unreadEvents(String str, List<String> list);

    void updateUserWithRoomMemberEvent(RoomMember roomMember);
}
