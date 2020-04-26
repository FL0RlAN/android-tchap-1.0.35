package org.matrix.androidsdk.data.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;

public class MXFileStoreMetaData implements Serializable {
    public String mAccessToken = null;
    public String mAntivirusServerPublicKey;
    public Map<String, List<String>> mDirectChatRoomsMap = null;
    public boolean mEndToEndDeviceAnnounced = false;
    public String mEventStreamToken = null;
    public List<String> mIgnoredUsers = new ArrayList();
    public boolean mIsUrlPreviewEnabled = false;
    public Map<String, String> mKnownFilters = new HashMap();
    public Set<String> mRoomsListWithoutURLPrevew = new HashSet();
    public List<ThirdPartyIdentifier> mThirdPartyIdentifiers = null;
    public String mUserAvatarUrl = null;
    public String mUserDisplayName = null;
    public String mUserId = null;
    public Map<String, Object> mUserWidgets = new HashMap();
    public int mVersion = -1;

    public MXFileStoreMetaData deepCopy() {
        MXFileStoreMetaData mXFileStoreMetaData = new MXFileStoreMetaData();
        mXFileStoreMetaData.mUserId = this.mUserId;
        mXFileStoreMetaData.mAccessToken = this.mAccessToken;
        mXFileStoreMetaData.mEventStreamToken = this.mEventStreamToken;
        mXFileStoreMetaData.mVersion = this.mVersion;
        mXFileStoreMetaData.mUserDisplayName = this.mUserDisplayName;
        String str = mXFileStoreMetaData.mUserDisplayName;
        if (str != null) {
            str.trim();
        }
        mXFileStoreMetaData.mUserAvatarUrl = this.mUserAvatarUrl;
        mXFileStoreMetaData.mThirdPartyIdentifiers = this.mThirdPartyIdentifiers;
        mXFileStoreMetaData.mIgnoredUsers = this.mIgnoredUsers;
        mXFileStoreMetaData.mDirectChatRoomsMap = this.mDirectChatRoomsMap;
        mXFileStoreMetaData.mEndToEndDeviceAnnounced = this.mEndToEndDeviceAnnounced;
        mXFileStoreMetaData.mAntivirusServerPublicKey = this.mAntivirusServerPublicKey;
        mXFileStoreMetaData.mIsUrlPreviewEnabled = this.mIsUrlPreviewEnabled;
        mXFileStoreMetaData.mUserWidgets = this.mUserWidgets;
        mXFileStoreMetaData.mRoomsListWithoutURLPrevew = this.mRoomsListWithoutURLPrevew;
        mXFileStoreMetaData.mKnownFilters = new HashMap(this.mKnownFilters);
        return mXFileStoreMetaData;
    }
}
