package org.matrix.androidsdk.data;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;
import org.matrix.androidsdk.rest.model.filter.RoomEventFilter;

public class DataRetriever {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = DataRetriever.class.getSimpleName();
    /* access modifiers changed from: private */
    public final Map<String, String> mPendingBackwardRequestTokenByRoomId = new HashMap();
    /* access modifiers changed from: private */
    public final Map<String, String> mPendingForwardRequestTokenByRoomId = new HashMap();
    /* access modifiers changed from: private */
    public final Map<String, String> mPendingRemoteRequestTokenByRoomId = new HashMap();
    private RoomsRestClient mRestClient;

    public RoomsRestClient getRoomsRestClient() {
        return this.mRestClient;
    }

    public void setRoomsRestClient(RoomsRestClient roomsRestClient) {
        this.mRestClient = roomsRestClient;
    }

    public Collection<Event> getCachedRoomMessages(IMXStore iMXStore, String str) {
        return iMXStore.getRoomMessages(str);
    }

    public void cancelHistoryRequests(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## cancelHistoryRequests() : roomId ");
        sb.append(str);
        Log.d(str2, sb.toString());
        clearPendingToken(this.mPendingForwardRequestTokenByRoomId, str);
        clearPendingToken(this.mPendingBackwardRequestTokenByRoomId, str);
    }

    public void cancelRemoteHistoryRequest(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## cancelRemoteHistoryRequest() : roomId ");
        sb.append(str);
        Log.d(str2, sb.toString());
        clearPendingToken(this.mPendingRemoteRequestTokenByRoomId, str);
    }

    public void getEvent(IMXStore iMXStore, String str, String str2, ApiCallback<Event> apiCallback) {
        Event event = iMXStore.getEvent(str2, str);
        if (event == null) {
            this.mRestClient.getEvent(str, str2, apiCallback);
        } else {
            apiCallback.onSuccess(event);
        }
    }

    public void backPaginate(IMXStore iMXStore, String str, String str2, int i, RoomEventFilter roomEventFilter, ApiCallback<TokensChunkEvents> apiCallback) {
        String str3 = str;
        String str4 = str2;
        if (TextUtils.equals(str4, Event.PAGINATE_BACK_TOKEN_END)) {
            final Handler handler = new Handler(Looper.getMainLooper());
            final ApiCallback<TokensChunkEvents> apiCallback2 = apiCallback;
            handler.post(new Runnable() {
                public void run() {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            apiCallback2.onSuccess(new TokensChunkEvents());
                        }
                    }, 0);
                }
            });
            return;
        }
        ApiCallback<TokensChunkEvents> apiCallback3 = apiCallback;
        String str5 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## backPaginate() : starts for roomId ");
        sb.append(str);
        Log.d(str5, sb.toString());
        TokensChunkEvents earlierMessages = iMXStore.getEarlierMessages(str, str2, i);
        putPendingToken(this.mPendingBackwardRequestTokenByRoomId, str, str4);
        if (earlierMessages != null) {
            final Handler handler2 = new Handler(Looper.getMainLooper());
            String str6 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## backPaginate() : some data has been retrieved into the local storage (");
            sb2.append(earlierMessages.chunk.size());
            sb2.append(" events)");
            Log.d(str6, sb2.toString());
            final String str7 = str;
            final String str8 = str2;
            final ApiCallback<TokensChunkEvents> apiCallback4 = apiCallback;
            final TokensChunkEvents tokensChunkEvents = earlierMessages;
            AnonymousClass2 r0 = new Runnable() {
                public void run() {
                    handler2.postDelayed(new Runnable() {
                        public void run() {
                            String access$100 = DataRetriever.this.getPendingToken(DataRetriever.this.mPendingBackwardRequestTokenByRoomId, str7);
                            String access$200 = DataRetriever.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## backPaginate() : local store roomId ");
                            sb.append(str7);
                            sb.append(" token ");
                            sb.append(str8);
                            sb.append(" vs ");
                            sb.append(access$100);
                            Log.d(access$200, sb.toString());
                            if (TextUtils.equals(access$100, str8)) {
                                DataRetriever.this.clearPendingToken(DataRetriever.this.mPendingBackwardRequestTokenByRoomId, str7);
                                apiCallback4.onSuccess(tokensChunkEvents);
                            }
                        }
                    }, 0);
                }
            };
            new Thread(r0).start();
        } else {
            Log.d(LOG_TAG, "## backPaginate() : trigger a remote request");
            RoomsRestClient roomsRestClient = this.mRestClient;
            Direction direction = Direction.BACKWARDS;
            final String str9 = str;
            final String str10 = str2;
            final IMXStore iMXStore2 = iMXStore;
            final ApiCallback<TokensChunkEvents> apiCallback5 = apiCallback;
            AnonymousClass3 r02 = new SimpleApiCallback<TokensChunkEvents>(apiCallback) {
                public void onSuccess(TokensChunkEvents tokensChunkEvents) {
                    DataRetriever dataRetriever = DataRetriever.this;
                    String access$100 = dataRetriever.getPendingToken(dataRetriever.mPendingBackwardRequestTokenByRoomId, str9);
                    String access$200 = DataRetriever.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## backPaginate() succeeds : roomId ");
                    sb.append(str9);
                    String str = " token ";
                    sb.append(str);
                    sb.append(str10);
                    sb.append(" vs ");
                    sb.append(access$100);
                    Log.d(access$200, sb.toString());
                    if (TextUtils.equals(access$100, str10)) {
                        DataRetriever dataRetriever2 = DataRetriever.this;
                        dataRetriever2.clearPendingToken(dataRetriever2.mPendingBackwardRequestTokenByRoomId, str9);
                        Event oldestEvent = iMXStore2.getOldestEvent(str9);
                        if (tokensChunkEvents.chunk.size() != 0) {
                            ((Event) tokensChunkEvents.chunk.get(0)).mToken = tokensChunkEvents.start;
                            if (tokensChunkEvents.end == null) {
                                tokensChunkEvents.end = Event.PAGINATE_BACK_TOKEN_END;
                            }
                            ((Event) tokensChunkEvents.chunk.get(tokensChunkEvents.chunk.size() - 1)).mToken = tokensChunkEvents.end;
                            Event event = (Event) tokensChunkEvents.chunk.get(0);
                            if (!(oldestEvent == null || event == null || !TextUtils.equals(oldestEvent.eventId, event.eventId))) {
                                tokensChunkEvents.chunk.remove(0);
                            }
                            iMXStore2.storeRoomEvents(str9, tokensChunkEvents, Direction.BACKWARDS);
                        }
                        String access$2002 = DataRetriever.LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("## backPaginate() succeed : roomId ");
                        sb2.append(str9);
                        sb2.append(str);
                        sb2.append(str10);
                        sb2.append(" got ");
                        sb2.append(tokensChunkEvents.chunk.size());
                        Log.d(access$2002, sb2.toString());
                        apiCallback5.onSuccess(tokensChunkEvents);
                    }
                }

                private void logErrorMessage(String str, String str2) {
                    String access$200 = DataRetriever.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## backPaginate() failed : roomId ");
                    sb.append(str9);
                    sb.append(" token ");
                    sb.append(str10);
                    sb.append(" expected ");
                    sb.append(str);
                    sb.append(" with ");
                    sb.append(str2);
                    Log.e(access$200, sb.toString());
                }

                public void onNetworkError(Exception exc) {
                    DataRetriever dataRetriever = DataRetriever.this;
                    String access$100 = dataRetriever.getPendingToken(dataRetriever.mPendingBackwardRequestTokenByRoomId, str9);
                    logErrorMessage(access$100, exc.getMessage());
                    if (TextUtils.equals(str10, access$100)) {
                        DataRetriever dataRetriever2 = DataRetriever.this;
                        dataRetriever2.clearPendingToken(dataRetriever2.mPendingBackwardRequestTokenByRoomId, str9);
                        apiCallback5.onNetworkError(exc);
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    DataRetriever dataRetriever = DataRetriever.this;
                    String access$100 = dataRetriever.getPendingToken(dataRetriever.mPendingBackwardRequestTokenByRoomId, str9);
                    logErrorMessage(access$100, matrixError.getMessage());
                    if (TextUtils.equals(str10, access$100)) {
                        DataRetriever dataRetriever2 = DataRetriever.this;
                        dataRetriever2.clearPendingToken(dataRetriever2.mPendingBackwardRequestTokenByRoomId, str9);
                        apiCallback5.onMatrixError(matrixError);
                    }
                }

                public void onUnexpectedError(Exception exc) {
                    DataRetriever dataRetriever = DataRetriever.this;
                    String access$100 = dataRetriever.getPendingToken(dataRetriever.mPendingBackwardRequestTokenByRoomId, str9);
                    logErrorMessage(access$100, exc.getMessage());
                    if (TextUtils.equals(str10, access$100)) {
                        DataRetriever dataRetriever2 = DataRetriever.this;
                        dataRetriever2.clearPendingToken(dataRetriever2.mPendingBackwardRequestTokenByRoomId, str9);
                        apiCallback5.onUnexpectedError(exc);
                    }
                }
            };
            roomsRestClient.getRoomMessagesFrom(str, str2, direction, i, roomEventFilter, r02);
        }
    }

    private void forwardPaginate(IMXStore iMXStore, String str, String str2, RoomEventFilter roomEventFilter, ApiCallback<TokensChunkEvents> apiCallback) {
        String str3 = str;
        String str4 = str2;
        putPendingToken(this.mPendingForwardRequestTokenByRoomId, str3, str4);
        RoomsRestClient roomsRestClient = this.mRestClient;
        Direction direction = Direction.FORWARDS;
        final String str5 = str;
        final String str6 = str2;
        final IMXStore iMXStore2 = iMXStore;
        final ApiCallback<TokensChunkEvents> apiCallback2 = apiCallback;
        AnonymousClass4 r0 = new SimpleApiCallback<TokensChunkEvents>(apiCallback) {
            public void onSuccess(TokensChunkEvents tokensChunkEvents) {
                DataRetriever dataRetriever = DataRetriever.this;
                if (TextUtils.equals(dataRetriever.getPendingToken(dataRetriever.mPendingForwardRequestTokenByRoomId, str5), str6)) {
                    DataRetriever dataRetriever2 = DataRetriever.this;
                    dataRetriever2.clearPendingToken(dataRetriever2.mPendingForwardRequestTokenByRoomId, str5);
                    iMXStore2.storeRoomEvents(str5, tokensChunkEvents, Direction.FORWARDS);
                    apiCallback2.onSuccess(tokensChunkEvents);
                }
            }
        };
        roomsRestClient.getRoomMessagesFrom(str3, str4, direction, 30, roomEventFilter, r0);
    }

    public void paginate(IMXStore iMXStore, String str, String str2, Direction direction, RoomEventFilter roomEventFilter, ApiCallback<TokensChunkEvents> apiCallback) {
        if (direction == Direction.BACKWARDS) {
            backPaginate(iMXStore, str, str2, 30, roomEventFilter, apiCallback);
        } else {
            forwardPaginate(iMXStore, str, str2, roomEventFilter, apiCallback);
        }
    }

    public void requestServerRoomHistory(String str, String str2, int i, RoomEventFilter roomEventFilter, ApiCallback<TokensChunkEvents> apiCallback) {
        String str3 = str;
        String str4 = str2;
        putPendingToken(this.mPendingRemoteRequestTokenByRoomId, str, str4);
        RoomsRestClient roomsRestClient = this.mRestClient;
        Direction direction = Direction.BACKWARDS;
        final String str5 = str;
        final String str6 = str2;
        final ApiCallback<TokensChunkEvents> apiCallback2 = apiCallback;
        AnonymousClass5 r0 = new SimpleApiCallback<TokensChunkEvents>(apiCallback) {
            public void onSuccess(TokensChunkEvents tokensChunkEvents) {
                DataRetriever dataRetriever = DataRetriever.this;
                if (TextUtils.equals(dataRetriever.getPendingToken(dataRetriever.mPendingRemoteRequestTokenByRoomId, str5), str6)) {
                    if (tokensChunkEvents.chunk.size() != 0) {
                        ((Event) tokensChunkEvents.chunk.get(0)).mToken = tokensChunkEvents.start;
                        ((Event) tokensChunkEvents.chunk.get(tokensChunkEvents.chunk.size() - 1)).mToken = tokensChunkEvents.end;
                    }
                    DataRetriever dataRetriever2 = DataRetriever.this;
                    dataRetriever2.clearPendingToken(dataRetriever2.mPendingRemoteRequestTokenByRoomId, str5);
                    apiCallback2.onSuccess(tokensChunkEvents);
                }
            }
        };
        roomsRestClient.getRoomMessagesFrom(str3, str4, direction, i, roomEventFilter, r0);
    }

    /* access modifiers changed from: private */
    public void clearPendingToken(Map<String, String> map, String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## clearPendingToken() : roomId ");
        sb.append(str);
        Log.d(str2, sb.toString());
        if (str != null) {
            synchronized (map) {
                map.remove(str);
            }
        }
    }

    /* access modifiers changed from: private */
    public String getPendingToken(Map<String, String> map, String str) {
        String str2;
        synchronized (map) {
            if (map.containsKey(str)) {
                str2 = (String) map.get(str);
                if (TextUtils.isEmpty(str2)) {
                    str2 = null;
                }
            } else {
                str2 = "Not a valid token";
            }
        }
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## getPendingToken() : roomId ");
        sb.append(str);
        sb.append(" token ");
        sb.append(str2);
        Log.d(str3, sb.toString());
        return str2;
    }

    private void putPendingToken(Map<String, String> map, String str, String str2) {
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## putPendingToken() : roomId ");
        sb.append(str);
        sb.append(" token ");
        sb.append(str2);
        Log.d(str3, sb.toString());
        synchronized (map) {
            if (str2 == null) {
                map.put(str, "");
            } else {
                map.put(str, str2);
            }
        }
    }
}
