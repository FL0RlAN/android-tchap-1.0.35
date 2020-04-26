package org.piwik.sdk.dispatcher;

import com.facebook.stetho.server.http.HttpHeaders;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;
import org.piwik.sdk.TrackMe;
import org.piwik.sdk.tools.Connectivity;
import org.piwik.sdk.tools.Connectivity.Type;
import timber.log.Timber;

public class Dispatcher {
    static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    static final long DEFAULT_DISPATCH_INTERVAL = 120000;
    private static final String LOGGER_TAG = "PIWIK:Dispatcher";
    private final Connectivity mConnectivity;
    private boolean mDispatchGzipped = false;
    /* access modifiers changed from: private */
    public volatile long mDispatchInterval = DEFAULT_DISPATCH_INTERVAL;
    private DispatchMode mDispatchMode = DispatchMode.ALWAYS;
    private List<Packet> mDryRunTarget = null;
    /* access modifiers changed from: private */
    public final EventCache mEventCache;
    private Runnable mLoop = new Runnable() {
        public void run() {
            boolean z;
            while (Dispatcher.this.mRunning) {
                try {
                    Dispatcher.this.mSleepToken.tryAcquire(Dispatcher.this.mDispatchInterval, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Timber.tag(Dispatcher.LOGGER_TAG).e(e);
                }
                if (Dispatcher.this.mEventCache.updateState(Dispatcher.this.isConnected())) {
                    ArrayList arrayList = new ArrayList();
                    Dispatcher.this.mEventCache.drainTo(arrayList);
                    Timber.tag(Dispatcher.LOGGER_TAG).d("Drained %s events.", Integer.valueOf(arrayList.size()));
                    Iterator it = Dispatcher.this.mPacketFactory.buildPackets(arrayList).iterator();
                    int i = 0;
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Packet packet = (Packet) it.next();
                        try {
                            z = Dispatcher.this.dispatch(packet);
                        } catch (IOException e2) {
                            Timber.tag(Dispatcher.LOGGER_TAG).d(e2);
                            z = false;
                        }
                        if (!z) {
                            Timber.tag(Dispatcher.LOGGER_TAG).d("Unsuccesful assuming OFFLINE, requeuing events.", new Object[0]);
                            Dispatcher.this.mEventCache.updateState(false);
                            Dispatcher.this.mEventCache.requeue(arrayList.subList(i, arrayList.size()));
                            break;
                        }
                        i += packet.getEventCount();
                    }
                    Timber.tag(Dispatcher.LOGGER_TAG).d("Dispatched %d events.", Integer.valueOf(i));
                }
                synchronized (Dispatcher.this.mThreadControl) {
                    if (!Dispatcher.this.mEventCache.isEmpty()) {
                        if (Dispatcher.this.mDispatchInterval < 0) {
                        }
                    }
                    Dispatcher.this.mRunning = false;
                    return;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public final PacketFactory mPacketFactory;
    /* access modifiers changed from: private */
    public volatile boolean mRunning = false;
    /* access modifiers changed from: private */
    public final Semaphore mSleepToken = new Semaphore(0);
    /* access modifiers changed from: private */
    public final Object mThreadControl = new Object();
    private volatile int mTimeOut = DEFAULT_CONNECTION_TIMEOUT;

    /* renamed from: org.piwik.sdk.dispatcher.Dispatcher$2 reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$org$piwik$sdk$dispatcher$DispatchMode = new int[DispatchMode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            $SwitchMap$org$piwik$sdk$dispatcher$DispatchMode[DispatchMode.ALWAYS.ordinal()] = 1;
            $SwitchMap$org$piwik$sdk$dispatcher$DispatchMode[DispatchMode.WIFI_ONLY.ordinal()] = 2;
        }
    }

    private boolean checkResponseCode(int i) {
        return i == 204 || i == 200;
    }

    public Dispatcher(EventCache eventCache, Connectivity connectivity, PacketFactory packetFactory) {
        this.mConnectivity = connectivity;
        this.mEventCache = eventCache;
        this.mPacketFactory = packetFactory;
    }

    public int getConnectionTimeOut() {
        return this.mTimeOut;
    }

    public void setConnectionTimeOut(int i) {
        this.mTimeOut = i;
    }

    public void setDispatchInterval(long j) {
        this.mDispatchInterval = j;
        if (this.mDispatchInterval != -1) {
            launch();
        }
    }

    public long getDispatchInterval() {
        return this.mDispatchInterval;
    }

    public void setDispatchGzipped(boolean z) {
        this.mDispatchGzipped = z;
    }

    public boolean getDispatchGzipped() {
        return this.mDispatchGzipped;
    }

    public void setDispatchMode(DispatchMode dispatchMode) {
        this.mDispatchMode = dispatchMode;
    }

    public DispatchMode getDispatchMode() {
        return this.mDispatchMode;
    }

    private boolean launch() {
        synchronized (this.mThreadControl) {
            if (this.mRunning) {
                return false;
            }
            this.mRunning = true;
            Thread thread = new Thread(this.mLoop);
            thread.setPriority(1);
            thread.start();
            return true;
        }
    }

    public boolean forceDispatch() {
        if (launch()) {
            return true;
        }
        this.mSleepToken.release();
        return false;
    }

    public void clear() {
        this.mEventCache.clear();
        if (this.mRunning) {
            forceDispatch();
        }
    }

    public void submit(TrackMe trackMe) {
        this.mEventCache.add(new Event(trackMe.toMap()));
        if (this.mDispatchInterval != -1) {
            launch();
        }
    }

    /* access modifiers changed from: private */
    public boolean isConnected() {
        boolean z = false;
        if (!this.mConnectivity.isConnected()) {
            return false;
        }
        int i = AnonymousClass2.$SwitchMap$org$piwik$sdk$dispatcher$DispatchMode[this.mDispatchMode.ordinal()];
        if (i == 1) {
            return true;
        }
        if (i != 2) {
            return false;
        }
        if (this.mConnectivity.getType() == Type.WIFI) {
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x008a A[Catch:{ all -> 0x0084, all -> 0x00d1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ab A[Catch:{ all -> 0x0084, all -> 0x00d1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00d7  */
    public boolean dispatch(Packet packet) throws IOException {
        HttpURLConnection httpURLConnection;
        GZIPOutputStream gZIPOutputStream;
        List<Packet> list = this.mDryRunTarget;
        String str = LOGGER_TAG;
        if (list != null) {
            list.add(packet);
            Timber.tag(str).d("DryRun, stored HttpRequest, now %s.", Integer.valueOf(this.mDryRunTarget.size()));
            return true;
        }
        BufferedWriter bufferedWriter = null;
        try {
            httpURLConnection = (HttpURLConnection) packet.openConnection();
            try {
                httpURLConnection.setConnectTimeout(this.mTimeOut);
                httpURLConnection.setReadTimeout(this.mTimeOut);
                if (packet.getPostData() != null) {
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpURLConnection.setRequestProperty("charset", "utf-8");
                    String jSONObject = packet.getPostData().toString();
                    if (this.mDispatchGzipped) {
                        httpURLConnection.addRequestProperty("Content-Encoding", "gzip");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                            gZIPOutputStream.write(jSONObject.getBytes(Charset.forName("UTF8")));
                            gZIPOutputStream.close();
                            httpURLConnection.getOutputStream().write(byteArrayOutputStream.toByteArray());
                        } catch (Throwable th) {
                            th = th;
                            gZIPOutputStream = null;
                            if (gZIPOutputStream != null) {
                                gZIPOutputStream.close();
                            }
                            throw th;
                        }
                    } else {
                        try {
                            BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                            try {
                                bufferedWriter2.write(jSONObject);
                                bufferedWriter2.close();
                            } catch (Throwable th2) {
                                th = th2;
                                bufferedWriter = bufferedWriter2;
                                if (bufferedWriter != null) {
                                    bufferedWriter.close();
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            if (bufferedWriter != null) {
                            }
                            throw th;
                        }
                    }
                } else {
                    httpURLConnection.setDoOutput(false);
                }
                int responseCode = httpURLConnection.getResponseCode();
                Timber.tag(str).d("status code %s", Integer.valueOf(responseCode));
                boolean checkResponseCode = checkResponseCode(responseCode);
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return checkResponseCode;
            } catch (Throwable th4) {
                th = th4;
                if (httpURLConnection != null) {
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            httpURLConnection = null;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            throw th;
        }
    }

    public void setDryRunTarget(List<Packet> list) {
        this.mDryRunTarget = list;
    }

    public List<Packet> getDryRunTarget() {
        return this.mDryRunTarget;
    }
}
