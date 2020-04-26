package io.realm.internal;

import android.content.Context;
import com.getkeepsafe.relinker.ReLinker;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Locale;

public class RealmCore {
    private static final String BINARIES_PATH;
    private static final String FILE_SEP = File.separator;
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    private static final String PATH_SEP = File.pathSeparator;
    private static boolean libraryIsLoaded = false;

    static {
        StringBuilder sb = new StringBuilder();
        String str = "lib";
        sb.append(str);
        sb.append(PATH_SEP);
        sb.append("..");
        sb.append(FILE_SEP);
        sb.append(str);
        BINARIES_PATH = sb.toString();
    }

    public static boolean osIsWindows() {
        return System.getProperty("os.name").toLowerCase(Locale.getDefault()).contains("win");
    }

    public static synchronized void loadLibrary(Context context) {
        synchronized (RealmCore.class) {
            if (!libraryIsLoaded) {
                ReLinker.loadLibrary(context, "realm-jni", "5.12.0");
                libraryIsLoaded = true;
            }
        }
    }

    private static String loadLibraryWindows() {
        try {
            addNativeLibraryPath(BINARIES_PATH);
            resetLibraryPath();
        } catch (Throwable unused) {
        }
        String loadCorrectLibrary = loadCorrectLibrary("realm_jni32d", "realm_jni64d");
        if (loadCorrectLibrary != null) {
            System.out.println("!!! Realm debug version loaded. !!!\n");
        } else {
            loadCorrectLibrary = loadCorrectLibrary("realm_jni32", "realm_jni64");
            if (loadCorrectLibrary == null) {
                PrintStream printStream = System.err;
                StringBuilder sb = new StringBuilder();
                sb.append("Searched java.library.path=");
                sb.append(System.getProperty(JAVA_LIBRARY_PATH));
                printStream.println(sb.toString());
                throw new RuntimeException("Couldn't load the Realm JNI library 'realm_jni32.dll or realm_jni64.dll'. Please include the directory to the library in java.library.path.");
            }
        }
        return loadCorrectLibrary;
    }

    private static String loadCorrectLibrary(String... strArr) {
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String str = strArr[i];
            try {
                System.loadLibrary(str);
                return str;
            } catch (Throwable unused) {
                i++;
            }
        }
        return null;
    }

    public static void addNativeLibraryPath(String str) {
        String str2 = JAVA_LIBRARY_PATH;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(System.getProperty(str2));
            sb.append(PATH_SEP);
            sb.append(str);
            sb.append(PATH_SEP);
            System.setProperty(str2, sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("Cannot set the library path!", e);
        }
    }

    private static void resetLibraryPath() {
        try {
            Field declaredField = ClassLoader.class.getDeclaredField("sys_paths");
            declaredField.setAccessible(true);
            declaredField.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Cannot reset the library path!", e);
        }
    }
}
