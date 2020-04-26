package org.matrix.androidsdk.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.security.KeyPairGeneratorSpec.Builder;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import androidx.preference.PreferenceManager;
import com.google.android.gms.stats.CodePackage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Calendar;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import org.matrix.androidsdk.core.model.SecretKeyAndVersion;

public class CompatUtil {
    private static final String AES_GCM_CIPHER_TYPE = "AES/GCM/NoPadding";
    private static final int AES_GCM_IV_LENGTH = 12;
    private static final int AES_GCM_KEY_SIZE_IN_BITS = 128;
    private static final String AES_LOCAL_PROTECTION_KEY_ALIAS = "aes_local_protection";
    private static final String AES_WRAPPED_PROTECTION_KEY_SHARED_PREFERENCE = "aes_wrapped_local_protection";
    private static final String ANDROID_KEY_STORE_PROVIDER = "AndroidKeyStore";
    private static final String RSA_WRAP_CIPHER_TYPE = "RSA/NONE/PKCS1Padding";
    private static final String RSA_WRAP_LOCAL_PROTECTION_KEY_ALIAS = "rsa_wrap_local_protection";
    private static final String SHARED_KEY_ANDROID_VERSION_WHEN_KEY_HAS_BEEN_GENERATED = "android_version_when_key_has_been_generated";
    private static final String TAG = CompatUtil.class.getSimpleName();
    private static SecureRandom sPrng;
    private static SecretKeyAndVersion sSecretKeyAndVersion;

    public static GZIPOutputStream createGzipOutputStream(OutputStream outputStream) throws IOException {
        if (VERSION.SDK_INT == 19) {
            return new GZIPOutputStream(outputStream, false);
        }
        return new GZIPOutputStream(outputStream);
    }

    private static synchronized SecretKeyAndVersion getAesGcmLocalProtectionKey(Context context) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, UnrecoverableKeyException {
        SecretKeyAndVersion secretKeyAndVersion;
        SecretKey secretKey;
        synchronized (CompatUtil.class) {
            if (sSecretKeyAndVersion == null) {
                KeyStore instance = KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER);
                instance.load(null);
                Log.i(TAG, "Loading local protection key");
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                int i = defaultSharedPreferences.getInt(SHARED_KEY_ANDROID_VERSION_WHEN_KEY_HAS_BEEN_GENERATED, VERSION.SDK_INT);
                if (VERSION.SDK_INT < 23) {
                    SecretKey readKeyApiL = readKeyApiL(defaultSharedPreferences, instance);
                    if (readKeyApiL == null) {
                        Log.i(TAG, "Generating RSA key pair with keystore");
                        KeyPairGenerator instance2 = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE_PROVIDER);
                        Calendar instance3 = Calendar.getInstance();
                        Calendar instance4 = Calendar.getInstance();
                        instance4.add(1, 10);
                        instance2.initialize(new Builder(context).setAlgorithmParameterSpec(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)).setAlias(RSA_WRAP_LOCAL_PROTECTION_KEY_ALIAS).setSubject(new X500Principal("CN=matrix-android-sdk")).setStartDate(instance3.getTime()).setEndDate(instance4.getTime()).setSerialNumber(BigInteger.ONE).build());
                        KeyPair generateKeyPair = instance2.generateKeyPair();
                        Log.i(TAG, "Generating wrapped AES key");
                        byte[] bArr = new byte[16];
                        getPrng().nextBytes(bArr);
                        SecretKey secretKeySpec = new SecretKeySpec(bArr, "AES");
                        Cipher instance5 = Cipher.getInstance(RSA_WRAP_CIPHER_TYPE);
                        instance5.init(3, generateKeyPair.getPublic());
                        defaultSharedPreferences.edit().putString(AES_WRAPPED_PROTECTION_KEY_SHARED_PREFERENCE, Base64.encodeToString(instance5.wrap(secretKeySpec), 0)).putInt(SHARED_KEY_ANDROID_VERSION_WHEN_KEY_HAS_BEEN_GENERATED, VERSION.SDK_INT).apply();
                        secretKey = secretKeySpec;
                    } else {
                        secretKey = readKeyApiL;
                    }
                } else if (instance.containsAlias(AES_LOCAL_PROTECTION_KEY_ALIAS)) {
                    Log.i(TAG, "AES local protection key found in keystore");
                    secretKey = (SecretKey) instance.getKey(AES_LOCAL_PROTECTION_KEY_ALIAS, null);
                } else {
                    secretKey = readKeyApiL(defaultSharedPreferences, instance);
                    if (secretKey == null) {
                        Log.i(TAG, "Generating AES key with keystore");
                        KeyGenerator instance6 = KeyGenerator.getInstance("AES", ANDROID_KEY_STORE_PROVIDER);
                        instance6.init(new KeyGenParameterSpec.Builder(AES_LOCAL_PROTECTION_KEY_ALIAS, 3).setBlockModes(new String[]{CodePackage.GCM}).setKeySize(128).setEncryptionPaddings(new String[]{"NoPadding"}).build());
                        secretKey = instance6.generateKey();
                        defaultSharedPreferences.edit().putInt(SHARED_KEY_ANDROID_VERSION_WHEN_KEY_HAS_BEEN_GENERATED, VERSION.SDK_INT).apply();
                    }
                }
                sSecretKeyAndVersion = new SecretKeyAndVersion(secretKey, i);
            }
            secretKeyAndVersion = sSecretKeyAndVersion;
        }
        return secretKeyAndVersion;
    }

    private static SecretKey readKeyApiL(SharedPreferences sharedPreferences, KeyStore keyStore) throws KeyStoreException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnrecoverableKeyException {
        String string = sharedPreferences.getString(AES_WRAPPED_PROTECTION_KEY_SHARED_PREFERENCE, null);
        if (string != null) {
            String str = RSA_WRAP_LOCAL_PROTECTION_KEY_ALIAS;
            if (keyStore.containsAlias(str)) {
                Log.i(TAG, "RSA + wrapped AES local protection keys found in keystore");
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(str, null);
                byte[] decode = Base64.decode(string, 0);
                Cipher instance = Cipher.getInstance(RSA_WRAP_CIPHER_TYPE);
                instance.init(4, privateKey);
                return (SecretKey) instance.unwrap(decode, "AES", 3);
            }
        }
        return null;
    }

    private static SecureRandom getPrng() {
        if (sPrng == null) {
            sPrng = new SecureRandom();
        }
        return sPrng;
    }

    public static OutputStream createCipherOutputStream(OutputStream outputStream, Context context) throws IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchProviderException, KeyStoreException, IllegalBlockSizeException {
        byte[] bArr;
        if (VERSION.SDK_INT < 19) {
            return outputStream;
        }
        SecretKeyAndVersion aesGcmLocalProtectionKey = getAesGcmLocalProtectionKey(context);
        if (aesGcmLocalProtectionKey == null || aesGcmLocalProtectionKey.getSecretKey() == null) {
            throw new KeyStoreException();
        }
        Cipher instance = Cipher.getInstance(AES_GCM_CIPHER_TYPE);
        if (aesGcmLocalProtectionKey.getAndroidVersionWhenTheKeyHasBeenGenerated() >= 23) {
            instance.init(1, aesGcmLocalProtectionKey.getSecretKey());
            bArr = instance.getIV();
        } else {
            byte[] bArr2 = new byte[12];
            getPrng().nextBytes(bArr2);
            instance.init(1, aesGcmLocalProtectionKey.getSecretKey(), new IvParameterSpec(bArr2));
            bArr = bArr2;
        }
        if (bArr.length != 12) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid IV length ");
            sb.append(bArr.length);
            Log.e(str, sb.toString());
            return null;
        }
        outputStream.write(bArr.length);
        outputStream.write(bArr);
        return new CipherOutputStream(outputStream, instance);
    }

    public static InputStream createCipherInputStream(InputStream inputStream, Context context) throws NoSuchPaddingException, NoSuchAlgorithmException, CertificateException, InvalidKeyException, KeyStoreException, UnrecoverableKeyException, IllegalBlockSizeException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException {
        AlgorithmParameterSpec algorithmParameterSpec;
        if (VERSION.SDK_INT < 19) {
            return inputStream;
        }
        int read = inputStream.read();
        if (read != 12) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid IV length ");
            sb.append(read);
            Log.e(str, sb.toString());
            return null;
        }
        byte[] bArr = new byte[12];
        inputStream.read(bArr);
        Cipher instance = Cipher.getInstance(AES_GCM_CIPHER_TYPE);
        SecretKeyAndVersion aesGcmLocalProtectionKey = getAesGcmLocalProtectionKey(context);
        if (aesGcmLocalProtectionKey == null || aesGcmLocalProtectionKey.getSecretKey() == null) {
            throw new KeyStoreException();
        }
        if (aesGcmLocalProtectionKey.getAndroidVersionWhenTheKeyHasBeenGenerated() >= 23) {
            algorithmParameterSpec = new GCMParameterSpec(128, bArr);
        } else {
            algorithmParameterSpec = new IvParameterSpec(bArr);
        }
        instance.init(2, aesGcmLocalProtectionKey.getSecretKey(), algorithmParameterSpec);
        return new CipherInputStream(inputStream, instance);
    }
}
