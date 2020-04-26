package im.vector.util;

import java.io.Serializable;
import org.matrix.androidsdk.MXSession;

public class RoomDirectoryData implements Serializable {
    private final String mAvatarUrl;
    private final String mDisplayName;
    private final String mHomeServer;
    private final boolean mIncludeAllNetworks;
    private final String mThirdPartyInstanceId;

    public static RoomDirectoryData createIncludingAllNetworks(String str, String str2) {
        RoomDirectoryData roomDirectoryData = new RoomDirectoryData(str, str2, null, null, true);
        return roomDirectoryData;
    }

    public static RoomDirectoryData getDefault(MXSession mXSession) {
        RoomDirectoryData roomDirectoryData = new RoomDirectoryData(null, mXSession.getMyUserId().substring(mXSession.getMyUserId().indexOf(":") + 1), null, null, false);
        return roomDirectoryData;
    }

    public RoomDirectoryData(String str, String str2, String str3, String str4, boolean z) {
        this.mHomeServer = str;
        this.mDisplayName = str2;
        this.mAvatarUrl = str3;
        this.mThirdPartyInstanceId = str4;
        this.mIncludeAllNetworks = z;
    }

    public String getHomeServer() {
        return this.mHomeServer;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public String getAvatarUrl() {
        return this.mAvatarUrl;
    }

    public String getThirdPartyInstanceId() {
        return this.mThirdPartyInstanceId;
    }

    public boolean isIncludedAllNetworks() {
        return this.mIncludeAllNetworks;
    }
}
