package im.vector.analytics.e2e;

import java.util.TimerTask;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"kotlin/concurrent/TimersKt$timerTask$1", "Ljava/util/TimerTask;", "run", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 13})
/* compiled from: Timer.kt */
public final class DecryptionFailureTracker$$special$$inlined$timer$1 extends TimerTask {
    final /* synthetic */ DecryptionFailureTracker this$0;

    public DecryptionFailureTracker$$special$$inlined$timer$1(DecryptionFailureTracker decryptionFailureTracker) {
        this.this$0 = decryptionFailureTracker;
    }

    public void run() {
        TimerTask timerTask = this;
        this.this$0.checkFailures();
    }
}
