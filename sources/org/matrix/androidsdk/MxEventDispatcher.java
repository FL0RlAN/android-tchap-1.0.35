package org.matrix.androidsdk;

import android.os.Looper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXOsHandler;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoEventListener;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.listeners.IMXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

class MxEventDispatcher {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MxEventDispatcher.class.getSimpleName();
    private CryptoEventListener mCryptoEventsListener = null;
    private final Set<IMXEventListener> mEventListeners = new HashSet();
    private final MXOsHandler mUiHandler = new MXOsHandler(Looper.getMainLooper());

    MxEventDispatcher() {
    }

    public void setCryptoEventsListener(CryptoEventListener cryptoEventListener) {
        this.mCryptoEventsListener = cryptoEventListener;
    }

    public void addListener(IMXEventListener iMXEventListener) {
        this.mEventListeners.add(iMXEventListener);
    }

    public void removeListener(IMXEventListener iMXEventListener) {
        this.mEventListeners.remove(iMXEventListener);
    }

    public void clearListeners() {
        this.mEventListeners.clear();
    }

    public void dispatchOnStoreReady() {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onStoreReady : listenersSnapshot) {
                    try {
                        onStoreReady.onStoreReady();
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onStoreReady ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnAccountInfoUpdate(final MyUser myUser) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onAccountInfoUpdate : listenersSnapshot) {
                    try {
                        onAccountInfoUpdate.onAccountInfoUpdate(myUser);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onAccountInfoUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnPresenceUpdate(final Event event, final User user) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onPresenceUpdate : listenersSnapshot) {
                    try {
                        onPresenceUpdate.onPresenceUpdate(event, user);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onPresenceUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnLiveEvent(final Event event, final RoomState roomState) {
        CryptoEventListener cryptoEventListener = this.mCryptoEventsListener;
        if (cryptoEventListener != null) {
            cryptoEventListener.onLiveEvent(event, roomState);
        }
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onLiveEvent : listenersSnapshot) {
                    try {
                        onLiveEvent.onLiveEvent(event, roomState);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onLiveEvent ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnLiveEventsChunkProcessed(final String str, final String str2) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onLiveEventsChunkProcessed : listenersSnapshot) {
                    try {
                        onLiveEventsChunkProcessed.onLiveEventsChunkProcessed(str, str2);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onLiveEventsChunkProcessed ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnBingEvent(Event event, RoomState roomState, BingRule bingRule, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            MXOsHandler mXOsHandler = this.mUiHandler;
            final Event event2 = event;
            final RoomState roomState2 = roomState;
            final BingRule bingRule2 = bingRule;
            AnonymousClass6 r0 = new Runnable() {
                public void run() {
                    for (IMXEventListener onBingEvent : listenersSnapshot) {
                        try {
                            onBingEvent.onBingEvent(event2, roomState2, bingRule2);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onBingEvent ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            };
            mXOsHandler.post(r0);
        }
    }

    public void dispatchOnEventSentStateUpdated(final Event event, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onEventSentStateUpdated : listenersSnapshot) {
                        try {
                            onEventSentStateUpdated.onEventSentStateUpdated(event);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onEventSentStateUpdated ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnEventSent(final Event event, final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onEventSent : listenersSnapshot) {
                        try {
                            onEventSent.onEventSent(event, str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onEventSent ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnBingRulesUpdate() {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onBingRulesUpdate : listenersSnapshot) {
                    try {
                        onBingRulesUpdate.onBingRulesUpdate();
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onBingRulesUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnInitialSyncComplete(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onInitialSyncComplete : listenersSnapshot) {
                    try {
                        onInitialSyncComplete.onInitialSyncComplete(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onInitialSyncComplete ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnCryptoSyncComplete() {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onCryptoSyncComplete : listenersSnapshot) {
                    try {
                        onCryptoSyncComplete.onCryptoSyncComplete();
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("OnCryptoSyncComplete ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnSyncError(final MatrixError matrixError) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onSyncError : listenersSnapshot) {
                    try {
                        onSyncError.onSyncError(matrixError);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onSyncError ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnNewRoom(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onNewRoom : listenersSnapshot) {
                        try {
                            onNewRoom.onNewRoom(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onNewRoom ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnJoinRoom(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onJoinRoom : listenersSnapshot) {
                        try {
                            onJoinRoom.onJoinRoom(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onJoinRoom ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnRoomInternalUpdate(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onRoomInternalUpdate : listenersSnapshot) {
                        try {
                            onRoomInternalUpdate.onRoomInternalUpdate(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onRoomInternalUpdate ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnLeaveRoom(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onLeaveRoom : listenersSnapshot) {
                        try {
                            onLeaveRoom.onLeaveRoom(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onLeaveRoom ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnRoomKick(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onRoomKick : listenersSnapshot) {
                        try {
                            onRoomKick.onRoomKick(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onRoomKick ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnReceiptEvent(final String str, final List<String> list, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onReceiptEvent : listenersSnapshot) {
                        try {
                            onReceiptEvent.onReceiptEvent(str, list);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onReceiptEvent ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnRoomTagEvent(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onRoomTagEvent : listenersSnapshot) {
                        try {
                            onRoomTagEvent.onRoomTagEvent(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onRoomTagEvent ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnReadMarkerEvent(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onReadMarkerEvent : listenersSnapshot) {
                        try {
                            onReadMarkerEvent.onReadMarkerEvent(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onReadMarkerEvent ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnRoomFlush(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onRoomFlush : listenersSnapshot) {
                        try {
                            onRoomFlush.onRoomFlush(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onRoomFlush ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnIgnoredUsersListUpdate() {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onIgnoredUsersListUpdate : listenersSnapshot) {
                    try {
                        onIgnoredUsersListUpdate.onIgnoredUsersListUpdate();
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onIgnoredUsersListUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnToDeviceEvent(final Event event, boolean z) {
        CryptoEventListener cryptoEventListener = this.mCryptoEventsListener;
        if (cryptoEventListener != null) {
            cryptoEventListener.onToDeviceEvent(event);
        }
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onToDeviceEvent : listenersSnapshot) {
                        try {
                            onToDeviceEvent.onToDeviceEvent(event);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("OnToDeviceEvent ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnDirectMessageChatRoomsListUpdate() {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onDirectMessageChatRoomsListUpdate : listenersSnapshot) {
                    try {
                        onDirectMessageChatRoomsListUpdate.onDirectMessageChatRoomsListUpdate();
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onDirectMessageChatRoomsListUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnEventDecrypted(final CryptoEvent cryptoEvent) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onEventDecrypted : listenersSnapshot) {
                    try {
                        onEventDecrypted.onEventDecrypted(cryptoEvent.getRoomId(), cryptoEvent.getEventId());
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onDecryptedEvent ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnNewGroupInvitation(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onNewGroupInvitation : listenersSnapshot) {
                    try {
                        onNewGroupInvitation.onNewGroupInvitation(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onNewGroupInvitation ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnJoinGroup(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onJoinGroup : listenersSnapshot) {
                    try {
                        onJoinGroup.onJoinGroup(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onJoinGroup ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnLeaveGroup(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onLeaveGroup : listenersSnapshot) {
                    try {
                        onLeaveGroup.onLeaveGroup(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onLeaveGroup ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnGroupProfileUpdate(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onGroupProfileUpdate : listenersSnapshot) {
                    try {
                        onGroupProfileUpdate.onGroupProfileUpdate(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onGroupProfileUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnGroupRoomsListUpdate(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onGroupRoomsListUpdate : listenersSnapshot) {
                    try {
                        onGroupRoomsListUpdate.onGroupRoomsListUpdate(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onGroupRoomsListUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnGroupUsersListUpdate(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onGroupUsersListUpdate : listenersSnapshot) {
                    try {
                        onGroupUsersListUpdate.onGroupUsersListUpdate(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onGroupUsersListUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnGroupInvitedUsersListUpdate(final String str) {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onGroupInvitedUsersListUpdate : listenersSnapshot) {
                    try {
                        onGroupInvitedUsersListUpdate.onGroupInvitedUsersListUpdate(str);
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onGroupInvitedUsersListUpdate ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void dispatchOnNotificationCountUpdate(final String str, boolean z) {
        if (!z) {
            final List listenersSnapshot = getListenersSnapshot();
            this.mUiHandler.post(new Runnable() {
                public void run() {
                    for (IMXEventListener onNotificationCountUpdate : listenersSnapshot) {
                        try {
                            onNotificationCountUpdate.onNotificationCountUpdate(str);
                        } catch (Exception e) {
                            String access$000 = MxEventDispatcher.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("onNotificationCountUpdate ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                }
            });
        }
    }

    public void dispatchOnAccountDataUpdate() {
        final List listenersSnapshot = getListenersSnapshot();
        this.mUiHandler.post(new Runnable() {
            public void run() {
                for (IMXEventListener onAccountDataUpdated : listenersSnapshot) {
                    try {
                        onAccountDataUpdated.onAccountDataUpdated();
                    } catch (Exception e) {
                        String access$000 = MxEventDispatcher.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onAccountDataUpdated ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    private List<IMXEventListener> getListenersSnapshot() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.mEventListeners);
        }
        return arrayList;
    }
}
