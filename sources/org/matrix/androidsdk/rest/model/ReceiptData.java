package org.matrix.androidsdk.rest.model;

import java.io.Serializable;
import org.matrix.androidsdk.core.interfaces.DatedObject;

public class ReceiptData implements Serializable, DatedObject {
    public String eventId;
    public long originServerTs;
    public String userId;

    public ReceiptData(String str, String str2, long j) {
        this.userId = str;
        this.eventId = str2;
        this.originServerTs = j;
    }

    public long getDate() {
        return this.originServerTs;
    }
}
