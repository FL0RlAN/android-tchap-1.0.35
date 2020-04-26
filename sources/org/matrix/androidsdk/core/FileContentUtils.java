package org.matrix.androidsdk.core;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.StatFs;
import android.system.Os;
import android.text.format.Formatter;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.lang.reflect.Field;

public class FileContentUtils {
    private static final String LOG_TAG = FileContentUtils.class.getSimpleName();

    public static String getMimeType(String str) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(str.substring(str.lastIndexOf(46) + 1).toLowerCase());
    }

    public static boolean deleteDirectory(File file) {
        boolean z;
        boolean z2;
        boolean z3 = false;
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                z = true;
                for (int i = 0; i < listFiles.length; i++) {
                    if (listFiles[i].isDirectory()) {
                        z2 = deleteDirectory(listFiles[i]);
                    } else {
                        z2 = listFiles[i].delete();
                    }
                    z &= z2;
                }
                if (z && file.delete()) {
                    z3 = true;
                }
                return z3;
            }
        }
        z = true;
        z3 = true;
        return z3;
    }

    public static long getDirectorySize(Context context, File file, int i) {
        long j;
        StatFs statFs = new StatFs(file.getAbsolutePath());
        if (VERSION.SDK_INT >= 18) {
            j = statFs.getBlockSizeLong();
        } else {
            j = (long) statFs.getBlockSize();
        }
        if (j < 0) {
            j = 1;
        }
        return getDirectorySize(context, file, i, j);
    }

    public static long getDirectorySize(Context context, File file, int i, long j) {
        long j2;
        File[] listFiles = file.listFiles();
        long j3 = 0;
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (!file2.isDirectory()) {
                    j2 = ((file2.length() / j) + 1) * j;
                } else {
                    j2 = getDirectorySize(context, file2, i - 1);
                }
                j3 += j2;
            }
        }
        if (i > 0) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getDirectorySize() ");
            sb.append(file.getPath());
            sb.append(" ");
            sb.append(Formatter.formatFileSize(context, j3));
            Log.d(str, sb.toString());
        }
        return j3;
    }

    public static long getLastAccessTime(File file) {
        long lastModified = file.lastModified();
        try {
            if (VERSION.SDK_INT >= 21) {
                return Os.lstat(file.getAbsolutePath()).st_atime;
            }
            Field declaredField = Class.forName("libcore.io.Libcore").getDeclaredField("os");
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }
            Object obj = declaredField.get(null);
            Object invoke = obj.getClass().getMethod("lstat", new Class[]{String.class}).invoke(obj, new Object[]{file.getAbsolutePath()});
            Field declaredField2 = invoke.getClass().getDeclaredField("st_atime");
            if (!declaredField2.isAccessible()) {
                declaredField2.setAccessible(true);
            }
            return declaredField2.getLong(invoke);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getLastAccessTime() failed ");
            sb.append(e.getMessage());
            sb.append(" for file ");
            sb.append(file);
            Log.e(str, sb.toString(), e);
            return lastModified;
        }
    }
}
