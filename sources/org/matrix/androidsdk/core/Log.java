package org.matrix.androidsdk.core;

import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.matrix.androidsdk.BuildConfig;

public class Log {
    /* access modifiers changed from: private */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int LOG_ROTATION_COUNT = 15;
    private static final int LOG_SIZE_BYTES = 52428800;
    private static final String LOG_TAG = "Log";
    private static File sCacheDirectory = null;
    private static FileHandler sFileHandler = null;
    private static String sFileName = "matrix";
    private static final Logger sLogger = Logger.getLogger(BuildConfig.APPLICATION_ID);
    public static boolean sShouldLogDebug = false;

    public enum EventTag {
        NAVIGATION,
        USER,
        NOTICE,
        BACKGROUND
    }

    public static final class LogFormatter extends Formatter {
        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);
        private static boolean mIsTimeZoneSet = false;

        public String format(LogRecord logRecord) {
            if (!mIsTimeZoneSet) {
                DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
                mIsTimeZoneSet = true;
            }
            Throwable thrown = logRecord.getThrown();
            if (thrown != null) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                stringWriter.write(logRecord.getMessage());
                stringWriter.write(Log.LINE_SEPARATOR);
                thrown.printStackTrace(printWriter);
                printWriter.flush();
                return stringWriter.toString();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(DATE_FORMAT.format(new Date(logRecord.getMillis())));
            sb.append("Z ");
            sb.append(logRecord.getMessage());
            sb.append(Log.LINE_SEPARATOR);
            return sb.toString();
        }
    }

    public static void init(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                sFileName = str;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(sCacheDirectory.getAbsolutePath());
            sb.append("/");
            sb.append(sFileName);
            sb.append(".%g.txt");
            sFileHandler = new FileHandler(sb.toString(), LOG_SIZE_BYTES, 15);
            sFileHandler.setFormatter(new LogFormatter());
            sLogger.setUseParentHandlers(false);
            sLogger.setLevel(Level.ALL);
            sLogger.addHandler(sFileHandler);
        } catch (IOException unused) {
        }
    }

    public static void setLogDirectory(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        sCacheDirectory = file;
    }

    public static File getLogDirectory() {
        return sCacheDirectory;
    }

    public static List<File> addLogFiles(List<File> list) {
        try {
            if (sFileHandler != null) {
                sFileHandler.flush();
                String absolutePath = sCacheDirectory.getAbsolutePath();
                for (int i = 0; i <= 15; i++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append("/");
                    sb.append(sFileName);
                    sb.append(".");
                    sb.append(i);
                    sb.append(".txt");
                    File file = new File(sb.toString());
                    if (file.exists()) {
                        list.add(file);
                    }
                }
            }
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## addLogFiles() failed : ");
            sb2.append(e.getMessage());
            e(LOG_TAG, sb2.toString(), e);
        }
        return list;
    }

    private static void logToFile(String str, String str2, String str3) {
        if (sCacheDirectory != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(Thread.currentThread().getId());
            sb.append(" ");
            sb.append(str);
            sb.append("/");
            sb.append(str2);
            sb.append(": ");
            sb.append(str3);
            sLogger.info(sb.toString());
        }
    }

    private static void logToFile(Throwable th) {
        if (sCacheDirectory != null && th != null) {
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            sLogger.info(stringWriter.toString());
        }
    }

    public static void event(EventTag eventTag, String str) {
        android.util.Log.v(eventTag.name(), str);
        logToFile("EVENT", eventTag.name(), str);
    }

    public static void con(String str, String str2) {
        android.util.Log.v(str, str2);
        logToFile("CON", str, str2);
    }

    public static void v(String str, String str2) {
        android.util.Log.v(str, str2);
        logToFile("V", str, str2);
    }

    public static void v(String str, String str2, Throwable th) {
        android.util.Log.v(str, str2, th);
        logToFile("V", str, str2);
        logToFile(th);
    }

    public static void d(String str, String str2) {
        if (sShouldLogDebug) {
            android.util.Log.d(str, str2);
            logToFile("D", str, str2);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (sShouldLogDebug) {
            android.util.Log.d(str, str2, th);
            logToFile("D", str, str2);
            logToFile(th);
        }
    }

    public static void i(String str, String str2) {
        android.util.Log.i(str, str2);
        logToFile("I", str, str2);
    }

    public static void i(String str, String str2, Throwable th) {
        android.util.Log.i(str, str2, th);
        logToFile("I", str, str2);
        logToFile(th);
    }

    public static void w(String str, String str2) {
        android.util.Log.w(str, str2);
        logToFile("W", str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        android.util.Log.w(str, str2, th);
        logToFile("W", str, str2);
        logToFile(th);
    }

    public static void e(String str, String str2) {
        android.util.Log.e(str, str2);
        logToFile("E", str, str2);
    }

    public static void e(String str, String str2, Throwable th) {
        android.util.Log.e(str, str2, th);
        logToFile("E", str, str2);
        logToFile(th);
    }

    public static void wtf(String str, String str2) {
        android.util.Log.wtf(str, str2);
        logToFile("WTF", str, str2);
    }

    public static void wtf(String str, Throwable th) {
        android.util.Log.wtf(str, th);
        logToFile("WTF", str, th.getMessage());
        logToFile(th);
    }

    public static void wtf(String str, String str2, Throwable th) {
        android.util.Log.wtf(str, str2, th);
        logToFile("WTF", str, str2);
        logToFile(th);
    }
}
