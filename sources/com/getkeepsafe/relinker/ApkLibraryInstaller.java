package com.getkeepsafe.relinker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import com.getkeepsafe.relinker.ReLinker.LibraryInstaller;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkLibraryInstaller implements LibraryInstaller {
    private static final int COPY_BUFFER_SIZE = 4096;
    private static final int MAX_TRIES = 5;

    private static class ZipFileInZipEntry {
        public ZipEntry zipEntry;
        public ZipFile zipFile;

        public ZipFileInZipEntry(ZipFile zipFile2, ZipEntry zipEntry2) {
            this.zipFile = zipFile2;
            this.zipEntry = zipEntry2;
        }
    }

    private String[] sourceDirectories(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (VERSION.SDK_INT < 21 || applicationInfo.splitSourceDirs == null || applicationInfo.splitSourceDirs.length == 0) {
            return new String[]{applicationInfo.sourceDir};
        }
        String[] strArr = new String[(applicationInfo.splitSourceDirs.length + 1)];
        strArr[0] = applicationInfo.sourceDir;
        System.arraycopy(applicationInfo.splitSourceDirs, 0, strArr, 1, applicationInfo.splitSourceDirs.length);
        return strArr;
    }

    private ZipFileInZipEntry findAPKWithLibrary(Context context, String[] strArr, String str, ReLinkerInstance reLinkerInstance) {
        int i;
        String[] strArr2 = strArr;
        String[] sourceDirectories = sourceDirectories(context);
        int length = sourceDirectories.length;
        char c = 0;
        ZipFile zipFile = null;
        int i2 = 0;
        while (i2 < length) {
            String str2 = sourceDirectories[i2];
            int i3 = 0;
            while (true) {
                int i4 = i3 + 1;
                i = 5;
                if (i3 >= 5) {
                    break;
                }
                try {
                    zipFile = new ZipFile(new File(str2), 1);
                    break;
                } catch (IOException unused) {
                    i3 = i4;
                }
            }
            if (zipFile != null) {
                int i5 = 0;
                while (true) {
                    int i6 = i5 + 1;
                    if (i5 >= i) {
                        break;
                    }
                    int length2 = strArr2.length;
                    int i7 = 0;
                    while (i7 < length2) {
                        String str3 = strArr2[i7];
                        StringBuilder sb = new StringBuilder();
                        sb.append("lib");
                        sb.append(File.separatorChar);
                        sb.append(str3);
                        sb.append(File.separatorChar);
                        sb.append(str);
                        String sb2 = sb.toString();
                        Object[] objArr = new Object[2];
                        objArr[c] = sb2;
                        objArr[1] = str2;
                        reLinkerInstance.log("Looking for %s in APK %s...", objArr);
                        ZipEntry entry = zipFile.getEntry(sb2);
                        if (entry != null) {
                            return new ZipFileInZipEntry(zipFile, entry);
                        }
                        i7++;
                        c = 0;
                    }
                    String str4 = str;
                    ReLinkerInstance reLinkerInstance2 = reLinkerInstance;
                    i5 = i6;
                    i = 5;
                }
            }
            String str5 = str;
            ReLinkerInstance reLinkerInstance3 = reLinkerInstance;
            i2++;
            c = 0;
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:71:0x00a5 A[SYNTHETIC, Splitter:B:71:0x00a5] */
    public void installLibrary(Context context, String[] strArr, String str, File file, ReLinkerInstance reLinkerInstance) {
        ZipFileInZipEntry zipFileInZipEntry;
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            zipFileInZipEntry = findAPKWithLibrary(context, strArr, str, reLinkerInstance);
            if (zipFileInZipEntry != null) {
                int i = 0;
                while (true) {
                    int i2 = i + 1;
                    if (i < 5) {
                        try {
                            reLinkerInstance.log("Found %s! Extracting...", str);
                            try {
                                if (file.exists() || file.createNewFile()) {
                                    inputStream = zipFileInZipEntry.zipFile.getInputStream(zipFileInZipEntry.zipEntry);
                                    try {
                                        fileOutputStream = new FileOutputStream(file);
                                    } catch (FileNotFoundException unused) {
                                        fileOutputStream = null;
                                        closeSilently(inputStream);
                                        closeSilently(fileOutputStream);
                                        i = i2;
                                    } catch (IOException unused2) {
                                        fileOutputStream = null;
                                        closeSilently(inputStream);
                                        closeSilently(fileOutputStream);
                                        i = i2;
                                    } catch (Throwable th) {
                                        th = th;
                                        closeSilently(inputStream);
                                        closeSilently(fileOutputStream2);
                                        throw th;
                                    }
                                    try {
                                        long copy = copy(inputStream, fileOutputStream);
                                        fileOutputStream.getFD().sync();
                                        if (copy != file.length()) {
                                            closeSilently(inputStream);
                                            closeSilently(fileOutputStream);
                                            i = i2;
                                        } else {
                                            closeSilently(inputStream);
                                            closeSilently(fileOutputStream);
                                            file.setReadable(true, false);
                                            file.setExecutable(true, false);
                                            file.setWritable(true);
                                            if (zipFileInZipEntry != null) {
                                                try {
                                                    if (zipFileInZipEntry.zipFile != null) {
                                                        zipFileInZipEntry.zipFile.close();
                                                    }
                                                } catch (IOException unused3) {
                                                }
                                            }
                                            return;
                                        }
                                    } catch (FileNotFoundException unused4) {
                                        closeSilently(inputStream);
                                        closeSilently(fileOutputStream);
                                        i = i2;
                                    } catch (IOException unused5) {
                                        closeSilently(inputStream);
                                        closeSilently(fileOutputStream);
                                        i = i2;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        fileOutputStream2 = fileOutputStream;
                                        closeSilently(inputStream);
                                        closeSilently(fileOutputStream2);
                                        throw th;
                                    }
                                } else {
                                    i = i2;
                                }
                            } catch (IOException unused6) {
                            }
                        } catch (FileNotFoundException unused7) {
                            inputStream = null;
                            fileOutputStream = null;
                            closeSilently(inputStream);
                            closeSilently(fileOutputStream);
                            i = i2;
                        } catch (IOException unused8) {
                            inputStream = null;
                            fileOutputStream = null;
                            closeSilently(inputStream);
                            closeSilently(fileOutputStream);
                            i = i2;
                        } catch (Throwable th3) {
                            th = th3;
                            if (zipFileInZipEntry != null) {
                            }
                            throw th;
                        }
                    } else {
                        reLinkerInstance.log("FATAL! Couldn't extract the library from the APK!");
                        if (zipFileInZipEntry != null) {
                            try {
                                if (zipFileInZipEntry.zipFile != null) {
                                    zipFileInZipEntry.zipFile.close();
                                }
                            } catch (IOException unused9) {
                            }
                        }
                        return;
                    }
                }
            } else {
                throw new MissingLibraryException(str);
            }
        } catch (Throwable th4) {
            th = th4;
            zipFileInZipEntry = null;
            if (zipFileInZipEntry != null) {
                try {
                    if (zipFileInZipEntry.zipFile != null) {
                        zipFileInZipEntry.zipFile.close();
                    }
                } catch (IOException unused10) {
                }
            }
            throw th;
        }
    }

    private long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                outputStream.flush();
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    private void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
