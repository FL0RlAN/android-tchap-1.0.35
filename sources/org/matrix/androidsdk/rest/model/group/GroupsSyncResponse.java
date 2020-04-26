package org.matrix.androidsdk.rest.model.group;

import java.util.Map;

public class GroupsSyncResponse {
    public Map<String, InvitedGroupSync> invite;
    public Map<String, Object> join;
    public Map<String, Object> leave;
}
