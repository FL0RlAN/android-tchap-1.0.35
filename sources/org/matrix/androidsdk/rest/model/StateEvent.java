package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StateEvent {
    public String algorithm;
    public List<String> aliases;
    @SerializedName("alias")
    public String canonicalAlias;
    public List<String> groups;
    @SerializedName("guest_access")
    public String guestAccess;
    @SerializedName("history_visibility")
    public String historyVisibility;
    @SerializedName("join_rule")
    public String joinRule;
    public String name;
    public String topic;
    public String url;
}
