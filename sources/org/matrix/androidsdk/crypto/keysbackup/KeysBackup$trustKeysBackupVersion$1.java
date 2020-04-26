package org.matrix.androidsdk.crypto.keysbackup;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;
import org.matrix.androidsdk.crypto.model.rest.keys.UpdateKeysBackupVersionBody;
import org.matrix.androidsdk.crypto.rest.RoomKeysRestClient;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$trustKeysBackupVersion$1 implements Runnable {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ KeysVersionResult $keysBackupVersion;
    final /* synthetic */ boolean $trust;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$trustKeysBackupVersion$1(KeysBackup keysBackup, KeysVersionResult keysVersionResult, ApiCallback apiCallback, boolean z) {
        this.this$0 = keysBackup;
        this.$keysBackupVersion = keysVersionResult;
        this.$callback = apiCallback;
        this.$trust = z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0049, code lost:
        if (r1 != null) goto L_0x0053;
     */
    public final void run() {
        Map map;
        String str = this.this$0.mCrypto.getMyDevice().userId;
        MegolmBackupAuthData access$getMegolmBackupAuthData = this.this$0.getMegolmBackupAuthData(this.$keysBackupVersion);
        if (access$getMegolmBackupAuthData == null) {
            Log.w(KeysBackup.LOG_TAG, "trustKeyBackupVersion:trust: Key backup is missing required data");
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$trustKeysBackupVersion$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    this.this$0.$callback.onUnexpectedError(new IllegalArgumentException("Missing element"));
                }
            });
            return;
        }
        Map signatures = access$getMegolmBackupAuthData.getSignatures();
        if (signatures == null) {
            Intrinsics.throwNpe();
        }
        Map map2 = (Map) signatures.get(str);
        if (map2 != null) {
            map = MapsKt.toMutableMap(map2);
        }
        map = new HashMap();
        if (this.$trust) {
            Map map3 = (Map) this.this$0.mCrypto.signObject(JsonUtility.getCanonicalizedJsonString(access$getMegolmBackupAuthData.signalableJSONDictionary())).get(str);
            if (map3 != null) {
                for (Entry entry : map3.entrySet()) {
                    Object key = entry.getKey();
                    Intrinsics.checkExpressionValueIsNotNull(key, "entry.key");
                    Object value = entry.getValue();
                    Intrinsics.checkExpressionValueIsNotNull(value, "entry.value");
                    map.put(key, value);
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("ed25519:");
            sb.append(this.this$0.mCrypto.getMyDevice().deviceId);
            map.remove(sb.toString());
        }
        String version = this.$keysBackupVersion.getVersion();
        if (version == null) {
            Intrinsics.throwNpe();
        }
        final UpdateKeysBackupVersionBody updateKeysBackupVersionBody = new UpdateKeysBackupVersionBody(version);
        updateKeysBackupVersionBody.setAlgorithm(this.$keysBackupVersion.getAlgorithm());
        MegolmBackupAuthData copy$default = MegolmBackupAuthData.copy$default(access$getMegolmBackupAuthData, null, null, null, null, 15, null);
        Map signatures2 = copy$default.getSignatures();
        if (signatures2 == null) {
            Intrinsics.throwNpe();
        }
        Map mutableMap = MapsKt.toMutableMap(signatures2);
        Intrinsics.checkExpressionValueIsNotNull(str, "myUserId");
        mutableMap.put(str, map);
        copy$default.setSignatures(mutableMap);
        updateKeysBackupVersionBody.setAuthData(JsonUtility.getBasicGson().toJsonTree(copy$default));
        RoomKeysRestClient access$getMRoomKeysRestClient$p = this.this$0.mRoomKeysRestClient;
        String version2 = this.$keysBackupVersion.getVersion();
        if (version2 == null) {
            Intrinsics.throwNpe();
        }
        access$getMRoomKeysRestClient$p.updateKeysBackupVersion(version2, updateKeysBackupVersionBody, new ApiCallback<Void>(this) {
            final /* synthetic */ KeysBackup$trustKeysBackupVersion$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onSuccess(Void voidR) {
                KeysVersionResult keysVersionResult = new KeysVersionResult();
                keysVersionResult.setVersion(this.this$0.$keysBackupVersion.getVersion());
                keysVersionResult.setAlgorithm(this.this$0.$keysBackupVersion.getAlgorithm());
                keysVersionResult.setCount(this.this$0.$keysBackupVersion.getCount());
                keysVersionResult.setHash(this.this$0.$keysBackupVersion.getHash());
                keysVersionResult.setAuthData(updateKeysBackupVersionBody.getAuthData());
                this.this$0.this$0.checkAndStartWithKeysBackupVersion(keysVersionResult);
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$trustKeysBackupVersion$1$3$onSuccess$1(this));
            }

            public void onUnexpectedError(Exception exc) {
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$trustKeysBackupVersion$1$3$onUnexpectedError$1(this, exc));
            }

            public void onNetworkError(Exception exc) {
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$trustKeysBackupVersion$1$3$onNetworkError$1(this, exc));
            }

            public void onMatrixError(MatrixError matrixError) {
                this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$trustKeysBackupVersion$1$3$onMatrixError$1(this, matrixError));
            }
        });
    }
}
