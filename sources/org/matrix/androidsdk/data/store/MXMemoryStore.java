package org.matrix.androidsdk.data.store;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomAccountData;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.comparator.Comparators;
import org.matrix.androidsdk.data.metrics.MetricsListener;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.Event.SentState;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.group.Group;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;
import org.matrix.androidsdk.rest.model.sync.AccountData;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;

public class MXMemoryStore implements IMXStore {
    private static final String LOG_TAG = MXMemoryStore.class.getSimpleName();
    protected static final Object mRoomEventsLock = new Object();
    private static Context mSharedContext = null;
    protected AccountData mAccountData;
    protected Context mContext;
    protected Credentials mCredentials;
    protected String mEventStreamToken = null;
    protected Map<String, Group> mGroups;
    protected final List<IMXStoreListener> mListeners = new ArrayList();
    protected MXFileStoreMetaData mMetadata = null;
    protected MetricsListener mMetricsListener;
    protected Map<String, Map<String, ReceiptData>> mReceiptsByRoomId;
    protected final Object mReceiptsByRoomIdLock = new Object();
    protected Map<String, RoomAccountData> mRoomAccountData;
    protected Map<String, LinkedHashMap<String, Event>> mRoomEvents;
    protected Map<String, RoomSummary> mRoomSummaries;
    protected Map<String, String> mRoomTokens;
    protected Map<String, Room> mRooms;
    private final Map<String, Event> mTemporaryEventsList = new HashMap();
    protected long mUserAvatarUrlTs;
    protected long mUserDisplayNameTs;
    protected Map<String, User> mUsers;

    public boolean areReceiptsReady() {
        return true;
    }

    public void close() {
    }

    public void commit() {
    }

    public long diskUsage() {
        return 0;
    }

    public void flushGroup(Group group) {
    }

    public void flushRoomEvents(String str) {
    }

    public void flushSummaries() {
    }

    public void flushSummary(RoomSummary roomSummary) {
    }

    public long getPreloadTime() {
        return 0;
    }

    public boolean isCorrupted() {
        return false;
    }

    public boolean isPermanent() {
        return false;
    }

    public boolean isReady() {
        return true;
    }

    public void open() {
    }

    public void storeLiveStateForRoom(String str) {
    }

    public void storeRoomStateEvent(String str, Event event) {
    }

    /* access modifiers changed from: protected */
    public void initCommon() {
        this.mRooms = new ConcurrentHashMap();
        this.mUsers = new ConcurrentHashMap();
        this.mRoomEvents = new ConcurrentHashMap();
        this.mRoomTokens = new ConcurrentHashMap();
        this.mRoomSummaries = new ConcurrentHashMap();
        this.mReceiptsByRoomId = new ConcurrentHashMap();
        this.mRoomAccountData = new ConcurrentHashMap();
        this.mGroups = new ConcurrentHashMap();
        this.mEventStreamToken = null;
    }

    public MXMemoryStore() {
        initCommon();
    }

    /* access modifiers changed from: protected */
    public void setContext(Context context) {
        if (mSharedContext == null) {
            if (context != null) {
                mSharedContext = context.getApplicationContext();
            } else {
                throw new RuntimeException("MXMemoryStore : context cannot be null");
            }
        }
        this.mContext = mSharedContext;
    }

    public MXMemoryStore(Credentials credentials, Context context) {
        initCommon();
        setContext(context);
        this.mCredentials = credentials;
        this.mMetadata = new MXFileStoreMetaData();
    }

    public Context getContext() {
        return this.mContext;
    }

    public void clear() {
        initCommon();
    }

    public void setCorrupted(String str) {
        dispatchOnStoreCorrupted(this.mCredentials.userId, str);
    }

    public String getEventStreamToken() {
        return this.mEventStreamToken;
    }

    public void setEventStreamToken(String str) {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            mXFileStoreMetaData.mEventStreamToken = str;
        }
        this.mEventStreamToken = str;
    }

    public void addMXStoreListener(IMXStoreListener iMXStoreListener) {
        synchronized (this) {
            if (iMXStoreListener != null) {
                if (this.mListeners.indexOf(iMXStoreListener) < 0) {
                    this.mListeners.add(iMXStoreListener);
                }
            }
        }
    }

    public void removeMXStoreListener(IMXStoreListener iMXStoreListener) {
        synchronized (this) {
            if (iMXStoreListener != null) {
                this.mListeners.remove(iMXStoreListener);
            }
        }
    }

    public String displayName() {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            return mXFileStoreMetaData.mUserDisplayName;
        }
        return null;
    }

    public boolean setDisplayName(String str, long j) {
        boolean z;
        synchronized (LOG_TAG) {
            if (this.mMetadata != null) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## setDisplayName() : from ");
                sb.append(this.mMetadata.mUserDisplayName);
                sb.append(" to ");
                sb.append(str);
                sb.append(" ts ");
                sb.append(j);
                Log.d(str2, sb.toString());
            }
            z = this.mMetadata != null && !TextUtils.equals(this.mMetadata.mUserDisplayName, str) && this.mUserDisplayNameTs < j && j != 0 && j <= System.currentTimeMillis();
            if (z) {
                this.mMetadata.mUserDisplayName = str != null ? str.trim() : null;
                this.mUserDisplayNameTs = j;
                User user = getUser(this.mMetadata.mUserId);
                if (user != null) {
                    user.displayname = this.mMetadata.mUserDisplayName;
                }
                Log.d(LOG_TAG, "## setDisplayName() : updated");
                commit();
            }
        }
        return z;
    }

    public String avatarURL() {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            return mXFileStoreMetaData.mUserAvatarUrl;
        }
        return null;
    }

    public boolean setAvatarURL(String str, long j) {
        boolean z;
        synchronized (LOG_TAG) {
            if (this.mMetadata != null) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## setAvatarURL() : from ");
                sb.append(this.mMetadata.mUserAvatarUrl);
                sb.append(" to ");
                sb.append(str);
                sb.append(" ts ");
                sb.append(j);
                Log.d(str2, sb.toString());
            }
            z = this.mMetadata != null && !TextUtils.equals(this.mMetadata.mUserAvatarUrl, str) && this.mUserAvatarUrlTs < j && j != 0 && j <= System.currentTimeMillis();
            if (z) {
                this.mMetadata.mUserAvatarUrl = str;
                this.mUserAvatarUrlTs = j;
                User user = getUser(this.mMetadata.mUserId);
                if (user != null) {
                    user.setAvatarUrl(str);
                }
                Log.d(LOG_TAG, "## setAvatarURL() : updated");
                commit();
            }
        }
        return z;
    }

    public List<ThirdPartyIdentifier> thirdPartyIdentifiers() {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            return mXFileStoreMetaData.mThirdPartyIdentifiers;
        }
        return new ArrayList();
    }

    public void setThirdPartyIdentifiers(List<ThirdPartyIdentifier> list) {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            mXFileStoreMetaData.mThirdPartyIdentifiers = list;
            Log.d(LOG_TAG, "setThirdPartyIdentifiers : commit");
            commit();
        }
    }

    public List<String> getIgnoredUserIdsList() {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            return mXFileStoreMetaData.mIgnoredUsers;
        }
        return new ArrayList();
    }

    public void setIgnoredUserIdsList(List<String> list) {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            mXFileStoreMetaData.mIgnoredUsers = list;
            Log.d(LOG_TAG, "setIgnoredUserIdsList : commit");
            commit();
        }
    }

    public Map<String, List<String>> getDirectChatRoomsDict() {
        return this.mMetadata.mDirectChatRoomsMap;
    }

    public void setDirectChatRoomsDict(Map<String, List<String>> map) {
        MXFileStoreMetaData mXFileStoreMetaData = this.mMetadata;
        if (mXFileStoreMetaData != null) {
            mXFileStoreMetaData.mDirectChatRoomsMap = map;
            Log.d(LOG_TAG, "setDirectChatRoomsDict : commit");
            commit();
        }
    }

    public Collection<Room> getRooms() {
        return new ArrayList(this.mRooms.values());
    }

    public Collection<User> getUsers() {
        ArrayList arrayList;
        synchronized (this.mUsers) {
            arrayList = new ArrayList(this.mUsers.values());
        }
        return arrayList;
    }

    public Room getRoom(String str) {
        if (str != null) {
            return (Room) this.mRooms.get(str);
        }
        return null;
    }

    public User getUser(String str) {
        User user;
        if (str == null) {
            return null;
        }
        synchronized (this.mUsers) {
            user = (User) this.mUsers.get(str);
        }
        return user;
    }

    public void storeUser(User user) {
        if (user != null && user.user_id != null) {
            try {
                synchronized (this.mUsers) {
                    this.mUsers.put(user.user_id, user);
                }
            } catch (OutOfMemoryError e) {
                dispatchOOM(e);
            }
        }
    }

    public void updateUserWithRoomMemberEvent(RoomMember roomMember) {
        boolean z;
        String str = "## updateUserWithRoomMemberEvent() failed ";
        if (roomMember != null) {
            try {
                User user = getUser(roomMember.getUserId());
                if (user == null) {
                    user = new User();
                    user.user_id = roomMember.getUserId();
                    user.setRetrievedFromRoomMember();
                    storeUser(user);
                }
                if (TextUtils.equals(roomMember.membership, "join")) {
                    if (TextUtils.equals(user.displayname, roomMember.displayname)) {
                        if (TextUtils.equals(user.getAvatarUrl(), roomMember.getAvatarUrl())) {
                            z = false;
                            if (z && user.getLatestPresenceTs() < roomMember.getOriginServerTs()) {
                                user.displayname = roomMember.displayname;
                                user.setAvatarUrl(roomMember.getAvatarUrl());
                                user.setLatestPresenceTs(roomMember.getOriginServerTs());
                                user.setRetrievedFromRoomMember();
                                return;
                            }
                        }
                    }
                    z = true;
                    if (z) {
                    }
                }
            } catch (OutOfMemoryError e) {
                dispatchOOM(e);
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            } catch (Exception e2) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(e2.getMessage());
                Log.e(str3, sb2.toString(), e2);
            }
        }
    }

    public void storeRoom(Room room) {
        if (room != null && room.getRoomId() != null) {
            this.mRooms.put(room.getRoomId(), room);
            if (!this.mRoomTokens.containsKey(room.getRoomId())) {
                storeBackToken(room.getRoomId(), "");
            }
        }
    }

    public Event getOldestEvent(String str) {
        Event event = null;
        if (str != null) {
            synchronized (mRoomEventsLock) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
                if (linkedHashMap != null) {
                    Iterator it = linkedHashMap.values().iterator();
                    if (it.hasNext()) {
                        event = (Event) it.next();
                    }
                }
            }
        }
        return event;
    }

    public Event getLatestEvent(String str) {
        Event event = null;
        if (str != null) {
            synchronized (mRoomEventsLock) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
                if (linkedHashMap != null) {
                    Iterator it = linkedHashMap.values().iterator();
                    if (it.hasNext()) {
                        while (it.hasNext()) {
                            event = (Event) it.next();
                        }
                    }
                }
            }
        }
        return event;
    }

    public int eventsCountAfter(String str, String str2) {
        return eventsAfter(str, str2, this.mCredentials.userId, null).size();
    }

    public void storeLiveRoomEvent(Event event) {
        if (event != null) {
            try {
                if (!(event.roomId == null || event.eventId == null)) {
                    synchronized (mRoomEventsLock) {
                        LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(event.roomId);
                        if (linkedHashMap == null) {
                            linkedHashMap = new LinkedHashMap();
                            this.mRoomEvents.put(event.roomId, linkedHashMap);
                        } else if (!linkedHashMap.containsKey(event.eventId)) {
                            if (!event.isDummyEvent() && this.mTemporaryEventsList.size() > 0) {
                                Object obj = null;
                                Iterator it = this.mTemporaryEventsList.keySet().iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    String str = (String) it.next();
                                    if (TextUtils.equals(((Event) this.mTemporaryEventsList.get(str)).eventId, event.eventId)) {
                                        obj = str;
                                        break;
                                    }
                                }
                                if (obj != null) {
                                    linkedHashMap.remove(obj);
                                    this.mTemporaryEventsList.remove(obj);
                                }
                            }
                        } else {
                            return;
                        }
                        linkedHashMap.put(event.eventId, event);
                        if (event.isDummyEvent()) {
                            this.mTemporaryEventsList.put(event.eventId, event);
                        }
                    }
                }
            } catch (OutOfMemoryError e) {
                dispatchOOM(e);
            }
        }
    }

    public boolean doesEventExist(String str, String str2) {
        boolean z = false;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            synchronized (mRoomEventsLock) {
                if (this.mRoomEvents.containsKey(str2) && ((LinkedHashMap) this.mRoomEvents.get(str2)).containsKey(str)) {
                    z = true;
                }
            }
        }
        return z;
    }

    public Event getEvent(String str, String str2) {
        Event event = null;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            synchronized (mRoomEventsLock) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str2);
                if (linkedHashMap != null) {
                    event = (Event) linkedHashMap.get(str);
                }
            }
        }
        return event;
    }

    public void deleteEvent(Event event) {
        if (event != null && event.roomId != null && event.eventId != null) {
            synchronized (mRoomEventsLock) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(event.roomId);
                if (linkedHashMap != null) {
                    linkedHashMap.remove(event.eventId);
                }
            }
        }
    }

    public void deleteRoom(String str) {
        if (str != null) {
            deleteRoomData(str);
            synchronized (mRoomEventsLock) {
                this.mRooms.remove(str);
            }
        }
    }

    public void deleteRoomData(String str) {
        if (str != null) {
            synchronized (mRoomEventsLock) {
                this.mRoomEvents.remove(str);
                this.mRoomTokens.remove(str);
                this.mRoomSummaries.remove(str);
                this.mRoomAccountData.remove(str);
                this.mReceiptsByRoomId.remove(str);
            }
        }
    }

    public void deleteAllRoomMessages(String str, boolean z) {
        if (str != null) {
            synchronized (mRoomEventsLock) {
                if (z) {
                    LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
                    if (linkedHashMap != null) {
                        for (Event event : new ArrayList(linkedHashMap.values())) {
                            if (event.mSentState == SentState.SENT && event.eventId != null) {
                                linkedHashMap.remove(event.eventId);
                            }
                        }
                    }
                } else {
                    this.mRoomEvents.remove(str);
                }
                this.mRoomSummaries.remove(str);
            }
        }
    }

    public void storeRoomEvents(String str, TokensChunkEvents tokensChunkEvents, Direction direction) {
        if (str != null) {
            try {
                synchronized (mRoomEventsLock) {
                    LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
                    if (linkedHashMap == null) {
                        linkedHashMap = new LinkedHashMap();
                        this.mRoomEvents.put(str, linkedHashMap);
                    }
                    if (direction == Direction.FORWARDS) {
                        this.mRoomTokens.put(str, tokensChunkEvents.start);
                        for (Event event : tokensChunkEvents.chunk) {
                            linkedHashMap.put(event.eventId, event);
                        }
                    } else {
                        Collection<Event> values = linkedHashMap.values();
                        if (linkedHashMap.size() == 0) {
                            for (int size = tokensChunkEvents.chunk.size() - 1; size >= 0; size--) {
                                Event event2 = (Event) tokensChunkEvents.chunk.get(size);
                                linkedHashMap.put(event2.eventId, event2);
                            }
                            this.mRoomTokens.put(str, tokensChunkEvents.start);
                        } else {
                            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                            for (int size2 = tokensChunkEvents.chunk.size() - 1; size2 >= 0; size2--) {
                                Event event3 = (Event) tokensChunkEvents.chunk.get(size2);
                                linkedHashMap2.put(event3.eventId, event3);
                            }
                            for (Event event4 : values) {
                                linkedHashMap2.put(event4.eventId, event4);
                            }
                            this.mRoomEvents.put(str, linkedHashMap2);
                        }
                    }
                }
            } catch (OutOfMemoryError e) {
                dispatchOOM(e);
            }
        }
    }

    public void storeBackToken(String str, String str2) {
        if (str != null && str2 != null) {
            this.mRoomTokens.put(str, str2);
        }
    }

    public void storeSummary(RoomSummary roomSummary) {
        if (roomSummary != null) {
            try {
                if (roomSummary.getRoomId() != null) {
                    this.mRoomSummaries.put(roomSummary.getRoomId(), roomSummary);
                }
            } catch (OutOfMemoryError e) {
                dispatchOOM(e);
            }
        }
    }

    public void storeAccountData(AccountData accountData) {
        if (this.mAccountData == null) {
            this.mAccountData = accountData;
            return;
        }
        for (AccountDataElement accountDataElement : accountData.accountDataElements) {
            String str = accountDataElement.type;
            Iterator it = this.mAccountData.accountDataElements.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                AccountDataElement accountDataElement2 = (AccountDataElement) it.next();
                if (accountDataElement2.type.equals(str)) {
                    this.mAccountData.accountDataElements.remove(accountDataElement2);
                    break;
                }
            }
            this.mAccountData.accountDataElements.add(accountDataElement);
        }
    }

    public AccountDataElement getAccountDataElement(String str) {
        AccountData accountData = this.mAccountData;
        if (!(accountData == null || accountData.accountDataElements == null)) {
            for (AccountDataElement accountDataElement : this.mAccountData.accountDataElements) {
                if (str.equals(accountDataElement.type)) {
                    return accountDataElement;
                }
            }
        }
        return null;
    }

    public void storeRoomAccountData(String str, RoomAccountData roomAccountData) {
        if (str != null) {
            try {
                if (((Room) this.mRooms.get(str)) != null && roomAccountData != null) {
                    this.mRoomAccountData.put(str, roomAccountData);
                }
            } catch (OutOfMemoryError e) {
                dispatchOOM(e);
            }
        }
    }

    public void getRoomStateEvents(String str, final ApiCallback<List<Event>> apiCallback) {
        final ArrayList arrayList = new ArrayList();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                apiCallback.onSuccess(arrayList);
            }
        });
    }

    public Collection<Event> getRoomMessages(String str) {
        ArrayList arrayList = null;
        if (str == null) {
            return null;
        }
        synchronized (mRoomEventsLock) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
            if (linkedHashMap != null) {
                arrayList = new ArrayList(linkedHashMap.values());
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
        r1 = new java.util.ArrayList();
        java.util.Collections.reverse(r3);
        r2 = new org.matrix.androidsdk.rest.model.TokensChunkEvents();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0040, code lost:
        if (r9 != null) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        if (r3.size() > r10) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0048, code lost:
        r1 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004a, code lost:
        if (r9 == null) goto L_0x0067;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004c, code lost:
        r5 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0051, code lost:
        if (r5 >= r3.size()) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005f, code lost:
        if (android.text.TextUtils.equals(r9, ((org.matrix.androidsdk.rest.model.Event) r3.get(r5)).mToken) != false) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0061, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0064, code lost:
        r9 = r5 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0067, code lost:
        r9 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x006c, code lost:
        if (r9 >= r3.size()) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0072, code lost:
        if (r9 >= r3.size()) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0074, code lost:
        r5 = (org.matrix.androidsdk.rest.model.Event) r3.get(r9);
        r1.add(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0081, code lost:
        if (r1.size() < r10) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0085, code lost:
        if (r5.mToken == null) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0088, code lost:
        r9 = r9 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x008f, code lost:
        if (r1.size() != 0) goto L_0x0092;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0091, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0092, code lost:
        r2.chunk = r1;
        r10 = (org.matrix.androidsdk.rest.model.Event) r1.get(r1.size() - 1);
        r2.start = ((org.matrix.androidsdk.rest.model.Event) r1.get(0)).mToken;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ac, code lost:
        if (r10.mToken != null) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00ba, code lost:
        if (android.text.TextUtils.isEmpty((java.lang.CharSequence) r7.mRoomTokens.get(r8)) != false) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00bc, code lost:
        r10.mToken = (java.lang.String) r7.mRoomTokens.get(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00c6, code lost:
        r2.end = r10.mToken;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00ca, code lost:
        return r2;
     */
    public TokensChunkEvents getEarlierMessages(String str, String str2, int i) {
        if (str == null) {
            return null;
        }
        synchronized (mRoomEventsLock) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
            if (linkedHashMap != null) {
                if (linkedHashMap.size() != 0) {
                    if (TextUtils.equals((CharSequence) this.mRoomTokens.get(str), str2)) {
                        return null;
                    }
                    ArrayList arrayList = new ArrayList(linkedHashMap.values());
                }
            }
            return null;
        }
    }

    public Collection<RoomSummary> getSummaries() {
        ArrayList arrayList = new ArrayList();
        for (String str : this.mRoomSummaries.keySet()) {
            Room room = (Room) this.mRooms.get(str);
            String str2 = "## getSummaries() : a summary exists for the roomId ";
            if (room == null) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str);
                sb.append(" but it does not exist in the room list");
                Log.e(str3, sb.toString());
            } else if (room.isJoined() || room.isInvited()) {
                arrayList.add(this.mRoomSummaries.get(str));
            } else {
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(str);
                sb2.append(" but the user is not anymore a member");
                Log.e(str4, sb2.toString());
            }
        }
        return arrayList;
    }

    public RoomSummary getSummary(String str) {
        if (str == null) {
            return null;
        }
        if (((Room) this.mRooms.get(str)) != null) {
            return (RoomSummary) this.mRoomSummaries.get(str);
        }
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## getSummary() : a summary exists for the roomId ");
        sb.append(str);
        sb.append(" but it does not exist in the room list");
        Log.e(str2, sb.toString());
        return null;
    }

    public List<Event> getLatestUnsentEvents(String str) {
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        synchronized (mRoomEventsLock) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
            if (linkedHashMap != null && linkedHashMap.size() > 0) {
                ArrayList arrayList2 = new ArrayList(linkedHashMap.values());
                for (int size = linkedHashMap.size() - 1; size >= 0; size--) {
                    Event event = (Event) arrayList2.get(size);
                    if (event.mSentState == SentState.WAITING_RETRY) {
                        arrayList.add(event);
                    }
                }
                Collections.reverse(arrayList);
            }
        }
        return arrayList;
    }

    public List<Event> getUndeliveredEvents(String str) {
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        synchronized (mRoomEventsLock) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
            if (linkedHashMap != null && linkedHashMap.size() > 0) {
                ArrayList arrayList2 = new ArrayList(linkedHashMap.values());
                for (int i = 0; i < linkedHashMap.size(); i++) {
                    Event event = (Event) arrayList2.get(i);
                    if (event.isUndelivered()) {
                        arrayList.add(event);
                    }
                }
            }
        }
        return arrayList;
    }

    public List<Event> getUnknownDeviceEvents(String str) {
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        synchronized (mRoomEventsLock) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
            if (linkedHashMap != null && linkedHashMap.size() > 0) {
                ArrayList arrayList2 = new ArrayList(linkedHashMap.values());
                for (int i = 0; i < linkedHashMap.size(); i++) {
                    Event event = (Event) arrayList2.get(i);
                    if (event.isUnknownDevice()) {
                        arrayList.add(event);
                    }
                }
            }
        }
        return arrayList;
    }

    public List<ReceiptData> getEventReceipts(String str, String str2, boolean z, boolean z2) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mReceiptsByRoomIdLock) {
            if (this.mReceiptsByRoomId.containsKey(str)) {
                String str3 = this.mCredentials.userId;
                Map map = (Map) this.mReceiptsByRoomId.get(str);
                ArrayList<String> arrayList2 = new ArrayList<>(map.keySet());
                if (str2 == null) {
                    arrayList.addAll(map.values());
                } else {
                    for (String str4 : arrayList2) {
                        if (map.containsKey(str4) && (!z || !TextUtils.equals(str3, str4))) {
                            ReceiptData receiptData = (ReceiptData) map.get(str4);
                            if (TextUtils.equals(receiptData.eventId, str2)) {
                                arrayList.add(receiptData);
                            }
                        }
                    }
                }
            }
        }
        if (z2 && arrayList.size() > 0) {
            Collections.sort(arrayList, Comparators.descComparator);
        }
        return arrayList;
    }

    public boolean storeReceipt(ReceiptData receiptData, String str) {
        Map map;
        try {
            if (!TextUtils.isEmpty(str)) {
                if (receiptData != null) {
                    synchronized (this.mReceiptsByRoomIdLock) {
                        if (!this.mReceiptsByRoomId.containsKey(str)) {
                            map = new HashMap();
                            this.mReceiptsByRoomId.put(str, map);
                        } else {
                            map = (Map) this.mReceiptsByRoomId.get(str);
                        }
                    }
                    ReceiptData receiptData2 = null;
                    if (map.containsKey(receiptData.userId)) {
                        receiptData2 = (ReceiptData) map.get(receiptData.userId);
                    }
                    if (receiptData2 == null) {
                        map.put(receiptData.userId, receiptData);
                        return true;
                    } else if (TextUtils.equals(receiptData.eventId, receiptData2.eventId) || receiptData.originServerTs < receiptData2.originServerTs) {
                        return false;
                    } else {
                        if (TextUtils.equals(receiptData.userId, this.mCredentials.userId)) {
                            synchronized (this.mReceiptsByRoomIdLock) {
                                LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
                                if (linkedHashMap != null && linkedHashMap.containsKey(receiptData.eventId)) {
                                    ArrayList arrayList = new ArrayList(linkedHashMap.keySet());
                                    int indexOf = arrayList.indexOf(receiptData2.eventId);
                                    int indexOf2 = arrayList.indexOf(receiptData.eventId);
                                    if (indexOf >= indexOf2) {
                                        String str2 = LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("## storeReceipt() : the read message is already read (cur pos ");
                                        sb.append(indexOf);
                                        sb.append(" receipt event pos ");
                                        sb.append(indexOf2);
                                        sb.append(")");
                                        Log.d(str2, sb.toString());
                                        return false;
                                    }
                                }
                            }
                        }
                        map.put(receiptData.userId, receiptData);
                        return true;
                    }
                }
            }
            return false;
        } catch (OutOfMemoryError e) {
            dispatchOOM(e);
        }
    }

    public ReceiptData getReceipt(String str, String str2) {
        ReceiptData receiptData = null;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            synchronized (this.mReceiptsByRoomIdLock) {
                if (this.mReceiptsByRoomId.containsKey(str)) {
                    receiptData = (ReceiptData) ((Map) this.mReceiptsByRoomId.get(str)).get(str2);
                }
            }
        }
        return receiptData;
    }

    private List<Event> eventsAfter(String str, String str2, String str3, List<String> list) {
        ArrayList arrayList = new ArrayList();
        if (str != null) {
            synchronized (mRoomEventsLock) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
                if (linkedHashMap != null) {
                    ArrayList arrayList2 = new ArrayList(linkedHashMap.values());
                    for (int size = arrayList2.size() - 1; size >= 0; size--) {
                        Event event = (Event) arrayList2.get(size);
                        if (str2 != null && TextUtils.equals(event.eventId, str2)) {
                            break;
                        }
                        if ((list == null || list.indexOf(event.getType()) >= 0) && !TextUtils.equals(event.getSender(), str3)) {
                            arrayList.add(event);
                        }
                    }
                    int i = 0;
                    while (i < arrayList.size()) {
                        Event event2 = (Event) arrayList.get(i);
                        if (TextUtils.equals(event2.getSender(), this.mCredentials.userId) || TextUtils.equals(event2.getType(), "m.room.member")) {
                            arrayList.remove(i);
                            i--;
                        }
                        i++;
                    }
                    Collections.reverse(arrayList);
                }
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0058, code lost:
        if (r3.containsKey(r8) != false) goto L_0x005a;
     */
    public boolean isEventRead(String str, String str2, String str3) {
        boolean z = false;
        if (!(str == null || str2 == null)) {
            synchronized (this.mReceiptsByRoomIdLock) {
                synchronized (mRoomEventsLock) {
                    if (this.mReceiptsByRoomId.containsKey(str) && this.mRoomEvents.containsKey(str)) {
                        Map map = (Map) this.mReceiptsByRoomId.get(str);
                        LinkedHashMap linkedHashMap = (LinkedHashMap) this.mRoomEvents.get(str);
                        if (linkedHashMap.containsKey(str3) && map.containsKey(str2)) {
                            ReceiptData receiptData = (ReceiptData) map.get(str2);
                            ArrayList arrayList = new ArrayList(linkedHashMap.keySet());
                            if (arrayList.indexOf(str3) <= arrayList.indexOf(receiptData.eventId)) {
                            }
                        }
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    public List<Event> unreadEvents(String str, List<String> list) {
        List<Event> list2;
        synchronized (this.mReceiptsByRoomIdLock) {
            if (this.mReceiptsByRoomId.containsKey(str)) {
                Map map = (Map) this.mReceiptsByRoomId.get(str);
                if (map.containsKey(this.mCredentials.userId)) {
                    list2 = eventsAfter(str, ((ReceiptData) map.get(this.mCredentials.userId)).eventId, this.mCredentials.userId, list);
                }
            }
            list2 = null;
        }
        return list2 == null ? new ArrayList() : list2;
    }

    private List<IMXStoreListener> getListeners() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.mListeners);
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public void dispatchPostProcess(String str) {
        for (IMXStoreListener postProcess : getListeners()) {
            postProcess.postProcess(str);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchOnStoreReady(String str) {
        for (IMXStoreListener onStoreReady : getListeners()) {
            onStoreReady.onStoreReady(str);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchOnStoreCorrupted(String str, String str2) {
        for (IMXStoreListener onStoreCorrupted : getListeners()) {
            onStoreCorrupted.onStoreCorrupted(str, str2);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchOOM(OutOfMemoryError outOfMemoryError) {
        for (IMXStoreListener onStoreOOM : getListeners()) {
            onStoreOOM.onStoreOOM(this.mCredentials.userId, outOfMemoryError.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchOnReadReceiptsLoaded(String str) {
        for (IMXStoreListener onReadReceiptsLoaded : getListeners()) {
            onReadReceiptsLoaded.onReadReceiptsLoaded(str);
        }
    }

    public Map<String, Long> getStats() {
        return new HashMap();
    }

    public void post(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void storeGroup(Group group) {
        if (group != null && !TextUtils.isEmpty(group.getGroupId())) {
            synchronized (this.mGroups) {
                this.mGroups.put(group.getGroupId(), group);
            }
        }
    }

    public void deleteGroup(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.mGroups) {
                this.mGroups.remove(str);
            }
        }
    }

    public Group getGroup(String str) {
        Group group;
        synchronized (this.mGroups) {
            if (str != null) {
                try {
                    group = (Group) this.mGroups.get(str);
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                group = null;
            }
        }
        return group;
    }

    public Collection<Group> getGroups() {
        Collection<Group> values;
        synchronized (this.mGroups) {
            values = this.mGroups.values();
        }
        return values;
    }

    public void setURLPreviewEnabled(boolean z) {
        this.mMetadata.mIsUrlPreviewEnabled = z;
    }

    public boolean isURLPreviewEnabled() {
        return this.mMetadata.mIsUrlPreviewEnabled;
    }

    public void setRoomsWithoutURLPreview(Set<String> set) {
        this.mMetadata.mRoomsListWithoutURLPrevew = set;
    }

    public void setUserWidgets(Map<String, Object> map) {
        this.mMetadata.mUserWidgets = map;
    }

    public Map<String, Object> getUserWidgets() {
        return this.mMetadata.mUserWidgets;
    }

    public Set<String> getRoomsWithoutURLPreviews() {
        return this.mMetadata.mRoomsListWithoutURLPrevew != null ? this.mMetadata.mRoomsListWithoutURLPrevew : new HashSet();
    }

    public void addFilter(String str, String str2) {
        this.mMetadata.mKnownFilters.put(str, str2);
    }

    public Map<String, String> getFilters() {
        return new HashMap(this.mMetadata.mKnownFilters);
    }

    public void setAntivirusServerPublicKey(String str) {
        this.mMetadata.mAntivirusServerPublicKey = str;
    }

    public String getAntivirusServerPublicKey() {
        return this.mMetadata.mAntivirusServerPublicKey;
    }

    public void setMetricsListener(MetricsListener metricsListener) {
        this.mMetricsListener = metricsListener;
    }
}
