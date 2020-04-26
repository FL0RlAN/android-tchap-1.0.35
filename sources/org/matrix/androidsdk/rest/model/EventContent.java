package org.matrix.androidsdk.rest.model;

import java.io.Serializable;
import org.matrix.androidsdk.crypto.interfaces.CryptoEventContent;
import org.matrix.androidsdk.rest.model.pid.RoomThirdPartyInvite;

public class EventContent implements Serializable, CryptoEventContent {
    public String algorithm;
    public String avatar_url;
    public String displayname;
    public String membership;
    public RoomThirdPartyInvite third_party_invite;

    public String getAlgorithm() {
        return this.algorithm;
    }
}
