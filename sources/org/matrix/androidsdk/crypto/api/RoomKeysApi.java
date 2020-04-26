package org.matrix.androidsdk.crypto.api;

import kotlin.Metadata;
import org.matrix.androidsdk.crypto.model.keys.CreateKeysBackupVersionBody;
import org.matrix.androidsdk.crypto.model.keys.KeyBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysVersion;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;
import org.matrix.androidsdk.crypto.model.keys.RoomKeysBackupData;
import org.matrix.androidsdk.crypto.model.rest.keys.BackupKeysResult;
import org.matrix.androidsdk.crypto.model.rest.keys.UpdateKeysBackupVersionBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H'J\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\t\u001a\u00020\nH'J,\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\r\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH'J\"\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH'J\u0018\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\t\u001a\u00020\nH'J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003H'J\u0018\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\t\u001a\u00020\nH'J,\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\r\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH'J\"\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\nH'J\u0018\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u00032\b\b\u0001\u0010\t\u001a\u00020\nH'J6\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\r\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u001b\u001a\u00020\u0014H'J,\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\f\u001a\u00020\n2\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u001d\u001a\u00020\u0016H'J\"\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u001f\u001a\u00020\u0018H'J\"\u0010 \u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010!\u001a\u00020\"H'¨\u0006#"}, d2 = {"Lorg/matrix/androidsdk/crypto/api/RoomKeysApi;", "", "createKeysBackupVersion", "Lretrofit2/Call;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersion;", "createKeysBackupVersionBody", "Lorg/matrix/androidsdk/crypto/model/keys/CreateKeysBackupVersionBody;", "deleteBackup", "Ljava/lang/Void;", "version", "", "deleteRoomSessionData", "roomId", "sessionId", "deleteRoomSessionsData", "deleteSessionsData", "getKeysBackupLastVersion", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersionResult;", "getKeysBackupVersion", "getRoomSessionData", "Lorg/matrix/androidsdk/crypto/model/keys/KeyBackupData;", "getRoomSessionsData", "Lorg/matrix/androidsdk/crypto/model/keys/RoomKeysBackupData;", "getSessionsData", "Lorg/matrix/androidsdk/crypto/model/keys/KeysBackupData;", "storeRoomSessionData", "Lorg/matrix/androidsdk/crypto/model/rest/keys/BackupKeysResult;", "keyBackupData", "storeRoomSessionsData", "roomKeysBackupData", "storeSessionsData", "keysBackupData", "updateKeysBackupVersion", "keysBackupVersionBody", "Lorg/matrix/androidsdk/crypto/model/rest/keys/UpdateKeysBackupVersionBody;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RoomKeysApi.kt */
public interface RoomKeysApi {
    @POST("room_keys/version")
    Call<KeysVersion> createKeysBackupVersion(@Body CreateKeysBackupVersionBody createKeysBackupVersionBody);

    @DELETE("room_keys/version/{version}")
    Call<Void> deleteBackup(@Path("version") String str);

    @DELETE("room_keys/keys/{roomId}/{sessionId}")
    Call<Void> deleteRoomSessionData(@Path("roomId") String str, @Path("sessionId") String str2, @Query("version") String str3);

    @DELETE("room_keys/keys/{roomId}")
    Call<Void> deleteRoomSessionsData(@Path("roomId") String str, @Query("version") String str2);

    @DELETE("room_keys/keys")
    Call<Void> deleteSessionsData(@Query("version") String str);

    @GET("room_keys/version")
    Call<KeysVersionResult> getKeysBackupLastVersion();

    @GET("room_keys/version/{version}")
    Call<KeysVersionResult> getKeysBackupVersion(@Path("version") String str);

    @GET("room_keys/keys/{roomId}/{sessionId}")
    Call<KeyBackupData> getRoomSessionData(@Path("roomId") String str, @Path("sessionId") String str2, @Query("version") String str3);

    @GET("room_keys/keys/{roomId}")
    Call<RoomKeysBackupData> getRoomSessionsData(@Path("roomId") String str, @Query("version") String str2);

    @GET("room_keys/keys")
    Call<KeysBackupData> getSessionsData(@Query("version") String str);

    @PUT("room_keys/keys/{roomId}/{sessionId}")
    Call<BackupKeysResult> storeRoomSessionData(@Path("roomId") String str, @Path("sessionId") String str2, @Query("version") String str3, @Body KeyBackupData keyBackupData);

    @PUT("room_keys/keys/{roomId}")
    Call<BackupKeysResult> storeRoomSessionsData(@Path("roomId") String str, @Query("version") String str2, @Body RoomKeysBackupData roomKeysBackupData);

    @PUT("room_keys/keys")
    Call<BackupKeysResult> storeSessionsData(@Query("version") String str, @Body KeysBackupData keysBackupData);

    @PUT("room_keys/version/{version}")
    Call<Void> updateKeysBackupVersion(@Path("version") String str, @Body UpdateKeysBackupVersionBody updateKeysBackupVersionBody);
}
