package org.matrix.androidsdk.rest.model.group;

import java.io.Serializable;
import java.util.List;

public class GroupSummaryUsersSection implements Serializable {
    public Integer totalUserCountEstimate;
    public List<String> users;
}
