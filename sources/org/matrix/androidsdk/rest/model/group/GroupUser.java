package org.matrix.androidsdk.rest.model.group;

import java.io.Serializable;

public class GroupUser implements Serializable {
    public String avatarUrl;
    public String displayname;
    public Boolean isPrivileged;
    public Boolean isPublic;
    public String userId;

    public String getDisplayname() {
        String str = this.displayname;
        return str != null ? str : this.userId;
    }
}
