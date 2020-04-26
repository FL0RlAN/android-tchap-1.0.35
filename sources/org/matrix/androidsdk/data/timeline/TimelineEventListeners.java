package org.matrix.androidsdk.data.timeline;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.data.timeline.EventTimeline.Listener;
import org.matrix.androidsdk.rest.model.Event;

class TimelineEventListeners {
    private static final String LOG_TAG = TimelineEventListeners.class.getSimpleName();
    private final List<Listener> mListeners = new ArrayList();

    TimelineEventListeners() {
    }

    public void add(Listener listener) {
        if (listener != null) {
            synchronized (this) {
                if (!this.mListeners.contains(listener)) {
                    this.mListeners.add(listener);
                }
            }
        }
    }

    public void remove(Listener listener) {
        if (listener != null) {
            synchronized (this) {
                this.mListeners.remove(listener);
            }
        }
    }

    public void onEvent(final Event event, final Direction direction, final RoomState roomState) {
        ArrayList<Listener> arrayList;
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            synchronized (this) {
                arrayList = new ArrayList<>(this.mListeners);
            }
            for (Listener listener : arrayList) {
                try {
                    listener.onEvent(event, direction, roomState);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("EventTimeline.onEvent ");
                    sb.append(listener);
                    sb.append(" crashes ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                TimelineEventListeners.this.onEvent(event, direction, roomState);
            }
        });
    }
}
