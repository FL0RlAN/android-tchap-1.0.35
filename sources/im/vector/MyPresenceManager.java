package im.vector;

import android.content.Context;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.User;

public class MyPresenceManager {
    private static final Map<MXSession, MyPresenceManager> instances = new HashMap();
    private static final String[] orderedPresenceArray = {User.PRESENCE_ONLINE, User.PRESENCE_UNAVAILABLE, User.PRESENCE_OFFLINE};
    /* access modifiers changed from: private */
    public static final Map<String, Integer> presenceOrderMap = new HashMap();
    /* access modifiers changed from: private */
    public String latestAdvertisedPresence = "";
    /* access modifiers changed from: private */
    public MyUser myUser;

    static {
        int i = 0;
        while (true) {
            String[] strArr = orderedPresenceArray;
            if (i < strArr.length) {
                presenceOrderMap.put(strArr[i], Integer.valueOf(i));
                i++;
            } else {
                return;
            }
        }
    }

    private MyPresenceManager(Context context, MXSession mXSession) {
        this.myUser = mXSession.getMyUser();
        this.myUser.addEventListener(new MXEventListener() {
            public void onPresenceUpdate(Event event, User user) {
                MyPresenceManager.this.myUser.presence = user.presence;
                if (!user.presence.equals(MyPresenceManager.this.latestAdvertisedPresence)) {
                    Integer num = (Integer) MyPresenceManager.presenceOrderMap.get(user.presence);
                    if (num != null && num.intValue() > ((Integer) MyPresenceManager.presenceOrderMap.get(MyPresenceManager.this.latestAdvertisedPresence)).intValue()) {
                        MyPresenceManager myPresenceManager = MyPresenceManager.this;
                        myPresenceManager.advertisePresence(myPresenceManager.latestAdvertisedPresence);
                    }
                }
            }
        });
    }

    private static MyPresenceManager createInstance(Context context, MXSession mXSession) {
        MyPresenceManager myPresenceManager = new MyPresenceManager(context, mXSession);
        instances.put(mXSession, myPresenceManager);
        return myPresenceManager;
    }

    public static synchronized MyPresenceManager getInstance(Context context, MXSession mXSession) {
        MyPresenceManager myPresenceManager;
        synchronized (MyPresenceManager.class) {
            if (!instances.containsKey(mXSession)) {
                createInstance(context, mXSession);
            }
            myPresenceManager = (MyPresenceManager) instances.get(mXSession);
        }
        return myPresenceManager;
    }

    public static synchronized void createPresenceManager(Context context, Collection<MXSession> collection) {
        synchronized (MyPresenceManager.class) {
            for (MXSession mXSession : collection) {
                if (!instances.containsKey(mXSession)) {
                    createInstance(context, mXSession);
                }
            }
        }
    }

    public static synchronized void remove(MXSession mXSession) {
        synchronized (MyPresenceManager.class) {
            instances.remove(mXSession);
        }
    }

    /* access modifiers changed from: private */
    public void advertisePresence(String str) {
        if (!this.latestAdvertisedPresence.equals(str)) {
            this.latestAdvertisedPresence = str;
        }
    }

    private static void advertiseAll(String str) {
        for (MyPresenceManager advertisePresence : instances.values()) {
            advertisePresence.advertisePresence(str);
        }
    }

    public static void advertiseAllOnline() {
        advertiseAll(User.PRESENCE_ONLINE);
    }

    public static void advertiseAllUnavailable() {
        advertiseAll(User.PRESENCE_UNAVAILABLE);
    }

    public void advertiseOffline() {
        advertisePresence(User.PRESENCE_OFFLINE);
    }
}
