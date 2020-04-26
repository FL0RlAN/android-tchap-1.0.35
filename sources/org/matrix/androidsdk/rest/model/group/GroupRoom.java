package org.matrix.androidsdk.rest.model.group;

import android.text.TextUtils;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

public class GroupRoom extends PublicRoom {
    public String getDisplayName() {
        if (!TextUtils.isEmpty(this.name)) {
            return this.name;
        }
        if (!TextUtils.isEmpty(this.canonicalAlias)) {
            return this.canonicalAlias;
        }
        return this.roomId;
    }
}
