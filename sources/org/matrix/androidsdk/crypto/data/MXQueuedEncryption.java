package org.matrix.androidsdk.crypto.data;

import com.google.gson.JsonElement;
import org.matrix.androidsdk.core.callback.ApiCallback;

public class MXQueuedEncryption {
    public ApiCallback<JsonElement> mApiCallback;
    public JsonElement mEventContent;
    public String mEventType;
}
