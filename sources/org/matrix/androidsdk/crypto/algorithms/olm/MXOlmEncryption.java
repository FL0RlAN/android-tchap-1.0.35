package org.matrix.androidsdk.crypto.algorithms.olm;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.crypto.algorithms.IMXEncrypting;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmSessionResult;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;

public class MXOlmEncryption implements IMXEncrypting {
    /* access modifiers changed from: private */
    public MXCryptoImpl mCrypto;
    /* access modifiers changed from: private */
    public String mRoomId;
    private CryptoSession mSession;

    public void initWithMatrixSession(CryptoSession cryptoSession, MXCryptoImpl mXCryptoImpl, String str) {
        this.mSession = cryptoSession;
        this.mCrypto = mXCryptoImpl;
        this.mRoomId = str;
    }

    /* access modifiers changed from: private */
    public List<MXDeviceInfo> getUserDevices(String str) {
        ArrayList arrayList;
        Map userDevices = this.mCrypto.getCryptoStore().getUserDevices(str);
        if (userDevices != null) {
            arrayList = new ArrayList(userDevices.values());
        } else {
            arrayList = new ArrayList();
        }
        return arrayList;
    }

    public void encryptEventContent(JsonElement jsonElement, String str, List<String> list, ApiCallback<JsonElement> apiCallback) {
        final List<String> list2 = list;
        final String str2 = str;
        final JsonElement jsonElement2 = jsonElement;
        final ApiCallback<JsonElement> apiCallback2 = apiCallback;
        AnonymousClass1 r0 = new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                ArrayList arrayList = new ArrayList();
                for (String access$000 : list2) {
                    List<MXDeviceInfo> access$0002 = MXOlmEncryption.this.getUserDevices(access$000);
                    if (access$0002 != null) {
                        for (MXDeviceInfo mXDeviceInfo : access$0002) {
                            if (!TextUtils.equals(mXDeviceInfo.identityKey(), MXOlmEncryption.this.mCrypto.getOlmDevice().getDeviceCurve25519Key()) && !mXDeviceInfo.isBlocked()) {
                                arrayList.add(mXDeviceInfo);
                            }
                        }
                    }
                }
                HashMap hashMap = new HashMap();
                hashMap.put("room_id", MXOlmEncryption.this.mRoomId);
                hashMap.put(PasswordLoginParams.IDENTIFIER_KEY_TYPE, str2);
                hashMap.put(BingRule.KIND_CONTENT, jsonElement2);
                MXOlmEncryption.this.mCrypto.encryptMessage(hashMap, arrayList);
                apiCallback2.onSuccess(JsonUtility.getGson(false).toJsonTree(hashMap));
            }
        };
        ensureSession(list, r0);
    }

    private void ensureSession(final List<String> list, final ApiCallback<Void> apiCallback) {
        this.mCrypto.getDeviceList().downloadKeys(list, false, new SimpleApiCallback<MXUsersDevicesMap<MXDeviceInfo>>(apiCallback) {
            public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                MXOlmEncryption.this.mCrypto.ensureOlmSessionsForUsers(list, new SimpleApiCallback<MXUsersDevicesMap<MXOlmSessionResult>>(apiCallback) {
                    public void onSuccess(MXUsersDevicesMap<MXOlmSessionResult> mXUsersDevicesMap) {
                        if (apiCallback != null) {
                            apiCallback.onSuccess(null);
                        }
                    }
                });
            }
        });
    }
}
