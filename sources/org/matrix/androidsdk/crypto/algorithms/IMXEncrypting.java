package org.matrix.androidsdk.crypto.algorithms;

import com.google.gson.JsonElement;
import java.util.List;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;

public interface IMXEncrypting {
    void encryptEventContent(JsonElement jsonElement, String str, List<String> list, ApiCallback<JsonElement> apiCallback);

    void initWithMatrixSession(CryptoSession cryptoSession, MXCryptoImpl mXCryptoImpl, String str);
}
