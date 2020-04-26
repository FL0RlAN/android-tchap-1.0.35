package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;

public class ServerNoticeUsageLimitContent {
    private static final String EVENT_TYPE_SERVER_NOTICE_USAGE_LIMIT = "m.server_notice.usage_limit_reached";
    @SerializedName("admin_contact")
    public String adminUri;
    public String limit;
    @SerializedName("server_notice_type")
    public String type;

    public boolean isServerNoticeUsageLimit() {
        return EVENT_TYPE_SERVER_NOTICE_USAGE_LIMIT.equals(this.type);
    }
}
