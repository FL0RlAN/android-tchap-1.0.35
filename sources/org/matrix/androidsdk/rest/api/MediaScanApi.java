package org.matrix.androidsdk.rest.api;

import org.matrix.androidsdk.rest.model.EncryptedMediaScanBody;
import org.matrix.androidsdk.rest.model.EncryptedMediaScanEncryptedBody;
import org.matrix.androidsdk.rest.model.MediaScanPublicKeyResult;
import org.matrix.androidsdk.rest.model.MediaScanResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MediaScanApi {
    @GET("public_key")
    Call<MediaScanPublicKeyResult> getServerPublicKey();

    @POST("scan_encrypted")
    Call<MediaScanResult> scanEncrypted(@Body EncryptedMediaScanBody encryptedMediaScanBody);

    @POST("scan_encrypted")
    Call<MediaScanResult> scanEncrypted(@Body EncryptedMediaScanEncryptedBody encryptedMediaScanEncryptedBody);

    @GET("scan/{domain}/{mediaId}")
    Call<MediaScanResult> scanUnencrypted(@Path("domain") String str, @Path("mediaId") String str2);
}
