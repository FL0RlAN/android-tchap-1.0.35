package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;

public class UserIdAndReason {
    public String reason;
    @SerializedName("user_id")
    public String userId;
}
