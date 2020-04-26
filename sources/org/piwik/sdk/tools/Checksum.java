package org.piwik.sdk.tools;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Checksum {
    private static final String HEXES = "0123456789ABCDEF";

    public static String getHex(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            int i = (b & 240) >> 4;
            String str = HEXES;
            sb.append(str.charAt(i));
            sb.append(str.charAt(b & 15));
        }
        return sb.toString();
    }

    public static String getMD5Checksum(String str) throws Exception {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(str.getBytes());
        return getHex(instance.digest());
    }

    public static String getMD5Checksum(File file) throws Exception {
        int read;
        if (!file.isFile()) {
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[1024];
        MessageDigest instance = MessageDigest.getInstance("MD5");
        do {
            read = fileInputStream.read(bArr);
            if (read > 0) {
                instance.update(bArr, 0, read);
            }
        } while (read != -1);
        fileInputStream.close();
        return getHex(instance.digest());
    }
}
