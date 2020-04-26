package org.matrix.androidsdk.crypto.model.rest;

import org.matrix.androidsdk.core.interfaces.DatedObject;

public class DeviceInfo implements DatedObject {
    public String device_id;
    public String display_name;
    public String last_seen_ip;
    public long last_seen_ts = 0;
    public String user_id;

    public long getDate() {
        return this.last_seen_ts;
    }
}
