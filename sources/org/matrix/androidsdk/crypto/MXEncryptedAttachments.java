package org.matrix.androidsdk.crypto;

import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileKey;

public class MXEncryptedAttachments implements Serializable {
    private static final String CIPHER_ALGORITHM = "AES/CTR/NoPadding";
    private static final int CRYPTO_BUFFER_SIZE = 32768;
    private static final String LOG_TAG = MXEncryptedAttachments.class.getSimpleName();
    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";
    private static final String SECRET_KEY_SPEC_ALGORITHM = "AES";

    public static class EncryptionResult {
        public EncryptedFileInfo mEncryptedFileInfo;
        public InputStream mEncryptedStream;
    }

    public static EncryptionResult encryptAttachment(InputStream inputStream, String str) {
        String str2 = "";
        String str3 = "## encryptAttachment failed ";
        long currentTimeMillis = System.currentTimeMillis();
        SecureRandom secureRandom = new SecureRandom();
        byte[] bArr = new byte[16];
        Arrays.fill(bArr, 0);
        byte[] bArr2 = new byte[8];
        secureRandom.nextBytes(bArr2);
        System.arraycopy(bArr2, 0, bArr, 0, bArr2.length);
        byte[] bArr3 = new byte[32];
        secureRandom.nextBytes(bArr3);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
            instance.init(1, new SecretKeySpec(bArr3, SECRET_KEY_SPEC_ALGORITHM), new IvParameterSpec(bArr));
            MessageDigest instance2 = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
            byte[] bArr4 = new byte[32768];
            while (true) {
                int read = inputStream.read(bArr4);
                if (-1 != read) {
                    byte[] update = instance.update(bArr4, 0, read);
                    instance2.update(update, 0, update.length);
                    byteArrayOutputStream.write(update);
                } else {
                    byte[] doFinal = instance.doFinal();
                    instance2.update(doFinal, 0, doFinal.length);
                    byteArrayOutputStream.write(doFinal);
                    EncryptionResult encryptionResult = new EncryptionResult();
                    encryptionResult.mEncryptedFileInfo = new EncryptedFileInfo();
                    encryptionResult.mEncryptedFileInfo.key = new EncryptedFileKey();
                    encryptionResult.mEncryptedFileInfo.mimetype = str;
                    encryptionResult.mEncryptedFileInfo.key.alg = "A256CTR";
                    encryptionResult.mEncryptedFileInfo.key.ext = Boolean.valueOf(true);
                    encryptionResult.mEncryptedFileInfo.key.key_ops = Arrays.asList(new String[]{"encrypt", "decrypt"});
                    encryptionResult.mEncryptedFileInfo.key.kty = "oct";
                    encryptionResult.mEncryptedFileInfo.key.k = base64ToBase64Url(Base64.encodeToString(bArr3, 0));
                    encryptionResult.mEncryptedFileInfo.iv = Base64.encodeToString(bArr, 0).replace("\n", str2).replace("=", str2);
                    encryptionResult.mEncryptedFileInfo.v = "v2";
                    encryptionResult.mEncryptedFileInfo.hashes = new HashMap();
                    encryptionResult.mEncryptedFileInfo.hashes.put("sha256", base64ToUnpaddedBase64(Base64.encodeToString(instance2.digest(), 0)));
                    encryptionResult.mEncryptedStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.close();
                    String str4 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Encrypt in ");
                    sb.append(System.currentTimeMillis() - currentTimeMillis);
                    sb.append(" ms");
                    Log.d(str4, sb.toString());
                    return encryptionResult;
                }
            }
        } catch (OutOfMemoryError e) {
            String str5 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str3);
            sb2.append(e.getMessage());
            Log.e(str5, sb2.toString(), e);
            try {
                byteArrayOutputStream.close();
            } catch (Exception e2) {
                Log.e(LOG_TAG, "## encryptAttachment() : fail to close outStream", e2);
            }
            return null;
        } catch (Exception e3) {
            String str6 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(e3.getMessage());
            Log.e(str6, sb3.toString(), e3);
            byteArrayOutputStream.close();
            return null;
        }
    }

    public static InputStream decryptAttachment(InputStream inputStream, EncryptedFileInfo encryptedFileInfo) {
        String str = "## decryptAttachment() :  failed ";
        if (inputStream == null || encryptedFileInfo == null) {
            Log.e(LOG_TAG, "## decryptAttachment() : null parameters");
            return null;
        }
        if (!(TextUtils.isEmpty(encryptedFileInfo.iv) || encryptedFileInfo.key == null || encryptedFileInfo.hashes == null)) {
            String str2 = "sha256";
            if (encryptedFileInfo.hashes.containsKey(str2)) {
                if (!TextUtils.equals(encryptedFileInfo.key.alg, "A256CTR") || !TextUtils.equals(encryptedFileInfo.key.kty, "oct") || TextUtils.isEmpty(encryptedFileInfo.key.k)) {
                    Log.e(LOG_TAG, "## decryptAttachment() : invalid key fields");
                    return null;
                }
                try {
                    if (inputStream.available() == 0) {
                        return new ByteArrayInputStream(new byte[0]);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Fail to retrieve the file size", e);
                }
                long currentTimeMillis = System.currentTimeMillis();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    byte[] decode = Base64.decode(base64UrlToBase64(encryptedFileInfo.key.k), 0);
                    byte[] decode2 = Base64.decode(encryptedFileInfo.iv, 0);
                    Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
                    instance.init(2, new SecretKeySpec(decode, SECRET_KEY_SPEC_ALGORITHM), new IvParameterSpec(decode2));
                    MessageDigest instance2 = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
                    byte[] bArr = new byte[32768];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (-1 == read) {
                            break;
                        }
                        instance2.update(bArr, 0, read);
                        byteArrayOutputStream.write(instance.update(bArr, 0, read));
                    }
                    byteArrayOutputStream.write(instance.doFinal());
                    if (!TextUtils.equals((CharSequence) encryptedFileInfo.hashes.get(str2), base64ToUnpaddedBase64(Base64.encodeToString(instance2.digest(), 0)))) {
                        Log.e(LOG_TAG, "## decryptAttachment() :  Digest value mismatch");
                        byteArrayOutputStream.close();
                        return null;
                    }
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.close();
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Decrypt in ");
                    sb.append(System.currentTimeMillis() - currentTimeMillis);
                    sb.append(" ms");
                    Log.d(str3, sb.toString());
                    return byteArrayInputStream;
                } catch (OutOfMemoryError e2) {
                    String str4 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(e2.getMessage());
                    Log.e(str4, sb2.toString(), e2);
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e3) {
                        Log.e(LOG_TAG, "## decryptAttachment() :  fail to close the file", e3);
                    }
                    return null;
                } catch (Exception e4) {
                    String str5 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(e4.getMessage());
                    Log.e(str5, sb3.toString(), e4);
                    byteArrayOutputStream.close();
                    return null;
                }
            }
        }
        Log.e(LOG_TAG, "## decryptAttachment() : some fields are not defined");
        return null;
    }

    private static String base64UrlToBase64(String str) {
        return str != null ? str.replaceAll(HelpFormatter.DEFAULT_OPT_PREFIX, "+").replaceAll("_", "/") : str;
    }

    private static String base64ToBase64Url(String str) {
        if (str == null) {
            return str;
        }
        String str2 = "";
        return str.replaceAll("\n", str2).replaceAll("\\+", HelpFormatter.DEFAULT_OPT_PREFIX).replaceAll("/", "_").replaceAll("=", str2);
    }

    private static String base64ToUnpaddedBase64(String str) {
        if (str == null) {
            return str;
        }
        String str2 = "";
        return str.replaceAll("\n", str2).replaceAll("=", str2);
    }
}
