package org.piwik.sdk.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.LinkedBlockingDeque;
import timber.log.Timber;

public class EventCache {
    private static final String TAG = "PIWIK:EventCache";
    private final EventDiskCache mDiskCache;
    private final LinkedBlockingDeque<Event> mQueue = new LinkedBlockingDeque<>();

    public EventCache(EventDiskCache eventDiskCache) {
        this.mDiskCache = eventDiskCache;
    }

    public void add(Event event) {
        this.mQueue.add(event);
    }

    public void drainTo(List<Event> list) {
        this.mQueue.drainTo(list);
    }

    public void clear() {
        this.mDiskCache.uncache();
        this.mQueue.clear();
    }

    public boolean isEmpty() {
        return this.mQueue.isEmpty() && this.mDiskCache.isEmpty();
    }

    public boolean updateState(boolean z) {
        String str = TAG;
        if (z) {
            List uncache = this.mDiskCache.uncache();
            ListIterator listIterator = uncache.listIterator(uncache.size());
            while (listIterator.hasPrevious()) {
                this.mQueue.offerFirst(listIterator.previous());
            }
            Timber.tag(str).d("Switched state to ONLINE, uncached %d events from disk.", Integer.valueOf(uncache.size()));
        } else if (!this.mQueue.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            this.mQueue.drainTo(arrayList);
            this.mDiskCache.cache(arrayList);
            Timber.tag(str).d("Switched state to OFFLINE, caching %d events to disk.", Integer.valueOf(arrayList.size()));
        }
        if (!z || this.mQueue.isEmpty()) {
            return false;
        }
        return true;
    }

    public void requeue(List<Event> list) {
        for (Event offerFirst : list) {
            this.mQueue.offerFirst(offerFirst);
        }
    }
}
