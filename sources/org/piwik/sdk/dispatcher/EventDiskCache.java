package org.piwik.sdk.dispatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import org.piwik.sdk.Tracker;
import timber.log.Timber;

public class EventDiskCache {
    private static final String CACHE_DIR_NAME = "piwik_cache";
    private static final String TAG = "PIWIK:EventDiskCache";
    private static final String VERSION = "1";
    private final File mCacheDir;
    private long mCurrentSize = 0;
    private boolean mDelayedClear;
    private final LinkedBlockingQueue<File> mEventContainer = new LinkedBlockingQueue<>();
    private final long mMaxAge;
    private final long mMaxSize;

    public EventDiskCache(Tracker tracker) {
        this.mDelayedClear = false;
        this.mMaxAge = tracker.getOfflineCacheAge();
        this.mMaxSize = tracker.getOfflineCacheSize();
        this.mCacheDir = new File(new File(tracker.getPiwik().getContext().getCacheDir(), CACHE_DIR_NAME), tracker.getAPIUrl().getHost());
        File[] listFiles = this.mCacheDir.listFiles();
        if (listFiles != null) {
            Arrays.sort(listFiles);
            for (File file : listFiles) {
                this.mCurrentSize += file.length();
                this.mEventContainer.add(file);
            }
        } else if (!this.mCacheDir.mkdirs()) {
            Timber.tag(TAG).e("Failed to make disk-cache dir %s", this.mCacheDir);
        }
    }

    private void checkCacheLimits() {
        long j;
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = this.mMaxAge;
        String str = "Failed to delete cache container %s";
        String str2 = "Deleted cache container %s";
        long j3 = 0;
        String str3 = TAG;
        if (j2 < 0) {
            Timber.tag(str3).d("Caching is disabled.", new Object[0]);
            while (!this.mEventContainer.isEmpty()) {
                File file = (File) this.mEventContainer.poll();
                if (file.delete()) {
                    Timber.tag(str3).e(str2, file.getPath());
                }
            }
        } else if (j2 > 0) {
            Iterator it = this.mEventContainer.iterator();
            while (it.hasNext()) {
                File file2 = (File) it.next();
                try {
                    j = Long.valueOf(file2.getName().split("_")[1]).longValue();
                } catch (Exception e) {
                    Timber.tag(str3).e(e, null, new Object[0]);
                    j = j3;
                }
                if (j >= System.currentTimeMillis() - this.mMaxAge) {
                    break;
                }
                if (file2.delete()) {
                    Timber.tag(str3).e(str2, file2.getPath());
                } else {
                    Timber.tag(str3).e(str, file2.getPath());
                }
                it.remove();
                j3 = 0;
            }
        }
        if (this.mMaxSize != 0) {
            Iterator it2 = this.mEventContainer.iterator();
            while (it2.hasNext() && this.mCurrentSize > this.mMaxSize) {
                File file3 = (File) it2.next();
                this.mCurrentSize -= file3.length();
                it2.remove();
                if (file3.delete()) {
                    Timber.tag(str3).e(str2, file3.getPath());
                } else {
                    Timber.tag(str3).e(str, file3.getPath());
                }
            }
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        Timber.tag(str3).d("Cache check took %dms", Long.valueOf(currentTimeMillis2 - currentTimeMillis));
    }

    private boolean isCachingEnabled() {
        return this.mMaxAge >= 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0054, code lost:
        return;
     */
    public synchronized void cache(List<Event> list) {
        if (isCachingEnabled()) {
            if (!list.isEmpty()) {
                checkCacheLimits();
                long currentTimeMillis = System.currentTimeMillis();
                File writeEventFile = writeEventFile(list);
                if (writeEventFile != null) {
                    this.mEventContainer.add(writeEventFile);
                    this.mCurrentSize += writeEventFile.length();
                }
                Timber.tag(TAG).d("Caching of %d events took %dms (%s)", Integer.valueOf(list.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis), writeEventFile);
            }
        }
    }

    public synchronized List<Event> uncache() {
        ArrayList arrayList = new ArrayList();
        if (!isCachingEnabled()) {
            return arrayList;
        }
        checkCacheLimits();
        long currentTimeMillis = System.currentTimeMillis();
        while (!this.mEventContainer.isEmpty()) {
            File file = (File) this.mEventContainer.poll();
            if (file != null) {
                arrayList.addAll(readEventFile(file));
                if (!file.delete()) {
                    Timber.tag(TAG).e("Failed to delete cache container %s", file.getPath());
                }
            }
        }
        Timber.tag(TAG).d("Uncaching of %d events took %dms", Integer.valueOf(arrayList.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return arrayList;
    }

    public synchronized boolean isEmpty() {
        if (!this.mDelayedClear) {
            checkCacheLimits();
            this.mDelayedClear = true;
        }
        return this.mEventContainer.isEmpty();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x009a, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x009c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x009d, code lost:
        r3 = r0;
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00b3, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00b4, code lost:
        timber.log.Timber.tag(r2).e(r0, null, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00e2, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00e3, code lost:
        timber.log.Timber.tag(r2).e(r0, null, new java.lang.Object[0]);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009c A[ExcHandler: all (r0v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:4:0x0012] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00af A[SYNTHETIC, Splitter:B:50:0x00af] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00de A[SYNTHETIC, Splitter:B:57:0x00de] */
    private List<Event> readEventFile(File file) {
        InputStream inputStream;
        String str = TAG;
        ArrayList arrayList = new ArrayList();
        if (!file.exists()) {
            return arrayList;
        }
        try {
            inputStream = new FileInputStream(file);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                if (!VERSION.equals(bufferedReader.readLine())) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Timber.tag(str).e(e, null, new Object[0]);
                    }
                    return arrayList;
                }
                long currentTimeMillis = System.currentTimeMillis() - this.mMaxAge;
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        int indexOf = readLine.indexOf(" ");
                        if (indexOf != -1) {
                            try {
                                long parseLong = Long.parseLong(readLine.substring(0, indexOf));
                                if (this.mMaxAge <= 0 || parseLong >= currentTimeMillis) {
                                    arrayList.add(new Event(parseLong, readLine.substring(indexOf + 1)));
                                }
                            } catch (Exception e2) {
                                Timber.tag(str).e(e2, null, new Object[0]);
                            }
                        }
                    } else {
                        try {
                            break;
                        } catch (IOException e3) {
                            Timber.tag(str).e(e3, null, new Object[0]);
                        }
                    }
                }
                inputStream.close();
                Timber.tag(str).d("Restored %d events from %s", Integer.valueOf(arrayList.size()), file.getPath());
                return arrayList;
            } catch (IOException e4) {
                e = e4;
            }
        } catch (IOException e5) {
            e = e5;
            File file2 = file;
            inputStream = null;
            try {
                Timber.tag(str).e(e, null, new Object[0]);
                if (inputStream != null) {
                }
                Timber.tag(str).d("Restored %d events from %s", Integer.valueOf(arrayList.size()), file.getPath());
                return arrayList;
            } catch (Throwable th) {
                Throwable th2 = th;
                if (inputStream != null) {
                }
                throw th2;
            }
        } catch (Throwable th3) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00da A[SYNTHETIC, Splitter:B:37:0x00da] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ec A[SYNTHETIC, Splitter:B:43:0x00ec] */
    private File writeEventFile(List<Event> list) {
        FileWriter fileWriter;
        Throwable th;
        String str = "\n";
        String str2 = TAG;
        if (list.isEmpty()) {
            return null;
        }
        File file = this.mCacheDir;
        StringBuilder sb = new StringBuilder();
        sb.append("events_");
        sb.append(((Event) list.get(list.size() - 1)).getTimeStamp());
        File file2 = new File(file, sb.toString());
        try {
            fileWriter = new FileWriter(file2);
            try {
                fileWriter.append(VERSION).append(str);
                long currentTimeMillis = System.currentTimeMillis() - this.mMaxAge;
                boolean z = false;
                for (Event event : list) {
                    if (this.mMaxAge <= 0 || event.getTimeStamp() >= currentTimeMillis) {
                        fileWriter.append(String.valueOf(event.getTimeStamp())).append(" ").append(event.getEncodedQuery()).append(str);
                        z = true;
                    }
                }
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    Timber.tag(str2).e(e, null, new Object[0]);
                }
                Timber.tag(str2).d("Saved %d events to %s", Integer.valueOf(list.size()), file2.getPath());
                if (z) {
                    return file2;
                }
                file2.delete();
                return null;
            } catch (IOException e2) {
                e = e2;
                try {
                    Timber.tag(str2).e(e, null, new Object[0]);
                    file2.deleteOnExit();
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e3) {
                            Timber.tag(str2).e(e3, null, new Object[0]);
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e4) {
                            Timber.tag(str2).e(e4, null, new Object[0]);
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e5) {
            e = e5;
            fileWriter = null;
            Timber.tag(str2).e(e, null, new Object[0]);
            file2.deleteOnExit();
            if (fileWriter != null) {
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileWriter = null;
            if (fileWriter != null) {
            }
            throw th;
        }
    }
}
