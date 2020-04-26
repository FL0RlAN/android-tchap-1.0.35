package org.matrix.androidsdk.crypto.data;

import com.google.gson.JsonElement;
import java.io.Serializable;

public class MXEncryptEventContentResult implements Serializable {
    public final JsonElement mEventContent;
    public final String mEventType;

    public MXEncryptEventContentResult(JsonElement jsonElement, String str) {
        this.mEventContent = jsonElement;
        this.mEventType = str;
    }
}
