package org.matrix.androidsdk.rest.client;

import android.text.TextUtils;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.RestClient.EndPointServer;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.HttpError;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2CallbackWrapper;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedBodyFileInfo;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.api.MediaScanApi;
import org.matrix.androidsdk.rest.model.EncryptedMediaScanBody;
import org.matrix.androidsdk.rest.model.EncryptedMediaScanEncryptedBody;
import org.matrix.androidsdk.rest.model.MediaScanError;
import org.matrix.androidsdk.rest.model.MediaScanPublicKeyResult;
import org.matrix.androidsdk.rest.model.MediaScanResult;
import org.matrix.olm.OlmException;
import org.matrix.olm.OlmPkEncryption;
import org.matrix.olm.OlmPkMessage;
import retrofit2.Call;

public class MediaScanRestClient extends RestClient<MediaScanApi> {
    /* access modifiers changed from: private */
    public IMXStore mMxStore;

    public MediaScanRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        super(homeServerConnectionConfig2, MediaScanApi.class, RestClient.URI_API_PREFIX_PATH_MEDIA_PROXY_UNSTABLE, JsonUtils.getGson(false), EndPointServer.ANTIVIRUS_SERVER);
    }

    public void setMxStore(IMXStore iMXStore) {
        this.mMxStore = iMXStore;
    }

    public void getServerPublicKey(final ApiCallback<String> apiCallback) {
        IMXStore iMXStore = this.mMxStore;
        if (iMXStore == null) {
            apiCallback.onUnexpectedError(new Exception("MxStore not configured"));
            return;
        }
        String antivirusServerPublicKey = iMXStore.getAntivirusServerPublicKey();
        if (antivirusServerPublicKey != null) {
            apiCallback.onSuccess(antivirusServerPublicKey);
        } else {
            ((MediaScanApi) this.mApi).getServerPublicKey().enqueue(new DefaultRetrofit2CallbackWrapper(new SimpleApiCallback<MediaScanPublicKeyResult>(apiCallback) {
                public void onSuccess(MediaScanPublicKeyResult mediaScanPublicKeyResult) {
                    MediaScanRestClient.this.mMxStore.setAntivirusServerPublicKey(mediaScanPublicKeyResult.mCurve25519PublicKey);
                    if (mediaScanPublicKeyResult.mCurve25519PublicKey != null) {
                        apiCallback.onSuccess(mediaScanPublicKeyResult.mCurve25519PublicKey);
                    } else {
                        apiCallback.onUnexpectedError(new Exception("Unable to get server public key from Json"));
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    if (matrixError.mStatus.intValue() == 404) {
                        String str = "";
                        MediaScanRestClient.this.mMxStore.setAntivirusServerPublicKey(str);
                        apiCallback.onSuccess(str);
                        return;
                    }
                    super.onMatrixError(matrixError);
                }
            }));
        }
    }

    public void resetServerPublicKey() {
        IMXStore iMXStore = this.mMxStore;
        if (iMXStore != null) {
            iMXStore.setAntivirusServerPublicKey(null);
        }
    }

    public void scanUnencryptedFile(String str, String str2, ApiCallback<MediaScanResult> apiCallback) {
        ((MediaScanApi) this.mApi).scanUnencrypted(str, str2).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }

    public void scanEncryptedFile(final EncryptedMediaScanBody encryptedMediaScanBody, final ApiCallback<MediaScanResult> apiCallback) {
        getServerPublicKey(new SimpleApiCallback<String>(apiCallback) {
            public void onSuccess(String str) {
                Call call;
                if (!TextUtils.isEmpty(str)) {
                    try {
                        OlmPkEncryption olmPkEncryption = new OlmPkEncryption();
                        olmPkEncryption.setRecipientKey(str);
                        OlmPkMessage encrypt = olmPkEncryption.encrypt(JsonUtility.getCanonicalizedJsonString(encryptedMediaScanBody));
                        EncryptedMediaScanEncryptedBody encryptedMediaScanEncryptedBody = new EncryptedMediaScanEncryptedBody();
                        encryptedMediaScanEncryptedBody.encryptedBodyFileInfo = new EncryptedBodyFileInfo(encrypt);
                        call = ((MediaScanApi) MediaScanRestClient.this.mApi).scanEncrypted(encryptedMediaScanEncryptedBody);
                    } catch (OlmException e) {
                        apiCallback.onUnexpectedError(e);
                        call = null;
                    }
                } else {
                    call = ((MediaScanApi) MediaScanRestClient.this.mApi).scanEncrypted(encryptedMediaScanBody);
                }
                if (call != null) {
                    call.enqueue(new DefaultRetrofit2CallbackWrapper(new SimpleApiCallback<MediaScanResult>(apiCallback) {
                        public void onSuccess(MediaScanResult mediaScanResult) {
                            apiCallback.onSuccess(mediaScanResult);
                        }

                        public void onNetworkError(Exception exc) {
                            MediaScanError mediaScanError;
                            if (exc instanceof HttpException) {
                                HttpError httpError = ((HttpException) exc).getHttpError();
                                if (httpError.getHttpCode() == 403) {
                                    try {
                                        mediaScanError = (MediaScanError) JsonUtils.getGson(false).fromJson(httpError.getErrorBody(), MediaScanError.class);
                                    } catch (Exception unused) {
                                        mediaScanError = null;
                                    }
                                    if (mediaScanError != null) {
                                        if (MediaScanError.MCS_BAD_DECRYPTION.equals(mediaScanError.reason)) {
                                            MediaScanRestClient.this.resetServerPublicKey();
                                        }
                                    }
                                }
                            }
                            super.onNetworkError(exc);
                        }
                    }));
                }
            }
        });
    }
}
