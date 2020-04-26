package org.matrix.androidsdk.rest.model.bingrules;

import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;

public class RoomMemberCountCondition extends Condition {
    private static final String LOG_TAG = RoomMemberCountCondition.class.getSimpleName();
    private static final String[] PREFIX_ARR = {"==", "<=", ">=", "<", ">", ""};
    private String comparisonPrefix;
    public String is;
    private int limit;
    private boolean parseError;

    public RoomMemberCountCondition() {
        this.comparisonPrefix = null;
        this.parseError = false;
        this.kind = Condition.KIND_ROOM_MEMBER_COUNT;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RoomMemberCountCondition{is='");
        sb.append(this.is);
        sb.append("'}'");
        return sb.toString();
    }

    public boolean isSatisfied(Room room) {
        boolean z = false;
        if (room == null || this.parseError) {
            return false;
        }
        if (this.comparisonPrefix == null) {
            parseIsField();
            if (this.parseError) {
                return false;
            }
        }
        int numberOfJoinedMembers = room.getNumberOfJoinedMembers();
        if (!"==".equals(this.comparisonPrefix)) {
            if (!"".equals(this.comparisonPrefix)) {
                if ("<".equals(this.comparisonPrefix)) {
                    if (numberOfJoinedMembers < this.limit) {
                        z = true;
                    }
                    return z;
                }
                if (">".equals(this.comparisonPrefix)) {
                    if (numberOfJoinedMembers > this.limit) {
                        z = true;
                    }
                    return z;
                }
                if ("<=".equals(this.comparisonPrefix)) {
                    if (numberOfJoinedMembers <= this.limit) {
                        z = true;
                    }
                    return z;
                }
                if (">=".equals(this.comparisonPrefix) && numberOfJoinedMembers >= this.limit) {
                    z = true;
                }
                return z;
            }
        }
        if (numberOfJoinedMembers == this.limit) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void parseIsField() {
        String[] strArr = PREFIX_ARR;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String str = strArr[i];
            if (this.is.startsWith(str)) {
                this.comparisonPrefix = str;
                break;
            }
            i++;
        }
        String str2 = this.comparisonPrefix;
        if (str2 == null) {
            this.parseError = true;
        } else {
            try {
                this.limit = Integer.parseInt(this.is.substring(str2.length()));
            } catch (NumberFormatException unused) {
                this.parseError = true;
            }
        }
        if (this.parseError) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("parsing error : ");
            sb.append(this.is);
            Log.e(str3, sb.toString());
        }
    }
}
