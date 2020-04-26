package im.vector.notifications;

import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public class NotifiedEvent {
    public final BingRule mBingRule;
    public final String mEventId;
    public final long mOriginServerTs;
    public final String mRoomId;

    public NotifiedEvent(String str, String str2, BingRule bingRule, long j) {
        this.mRoomId = str;
        this.mEventId = str2;
        this.mBingRule = bingRule;
        this.mOriginServerTs = j;
    }
}
