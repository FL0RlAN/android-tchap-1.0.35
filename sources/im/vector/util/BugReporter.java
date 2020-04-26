package im.vector.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import fr.gouv.tchap.a.R;
import im.vector.BuildConfig;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.activity.BugReportActivity;
import im.vector.extensions.BasicExtensionsKt;
import im.vector.settings.VectorLocale;
import im.vector.util.BugReporterMultipartBody.Builder;
import im.vector.util.BugReporterMultipartBody.WriteListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.cli.HelpFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.bingrules.Condition;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

public class BugReporter {
    private static final int BUFFER_SIZE = 52428800;
    private static final String BUG_REPORT_URL_SUFFIX = "/bugreports/submit";
    private static final String CRASH_FILENAME = "crash.log";
    private static final String[] LOGCAT_CMD_DEBUG;
    private static final String[] LOGCAT_CMD_ERROR;
    private static final String LOG_CAT_ERROR_FILENAME = "logcatError.log";
    private static final String LOG_CAT_FILENAME = "logcat.log";
    private static final String LOG_CAT_SCREENSHOT_FILENAME = "screenshot.png";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = BugReporter.class.getSimpleName();
    /* access modifiers changed from: private */
    public static Call mBugReportCall = null;
    /* access modifiers changed from: private */
    public static boolean mIsCancelled = false;
    /* access modifiers changed from: private */
    public static final OkHttpClient mOkHttpClient = new OkHttpClient();
    /* access modifiers changed from: private */
    public static Bitmap mScreenshot = null;

    public interface IMXBugReportListener {
        void onProgress(int i);

        void onUploadCancelled();

        void onUploadFailed(String str);

        void onUploadSucceed();
    }

    static {
        String str = "logcat";
        String str2 = "-d";
        String str3 = "-v";
        String str4 = "threadtime";
        LOGCAT_CMD_ERROR = new String[]{str, str2, str3, str4, "AndroidRuntime:E libcommunicator:V DEBUG:V *:S"};
        LOGCAT_CMD_DEBUG = new String[]{str, str2, str3, str4, "*:*"};
    }

    public static void sendBugReport(Context context, boolean z, boolean z2, boolean z3, String str, IMXBugReportListener iMXBugReportListener) {
        final String str2 = str;
        final Context context2 = context;
        final boolean z4 = z;
        final boolean z5 = z2;
        final boolean z6 = z3;
        final IMXBugReportListener iMXBugReportListener2 = iMXBugReportListener;
        AnonymousClass1 r0 = new AsyncTask<Void, Integer, String>() {
            final List<File> mBugReportFiles = new ArrayList();

            /* JADX WARNING: type inference failed for: r11v0 */
            /* JADX WARNING: type inference failed for: r0v5, types: [java.io.InputStream] */
            /* JADX WARNING: type inference failed for: r11v2, types: [java.io.InputStream] */
            /* JADX WARNING: type inference failed for: r2v14 */
            /* JADX WARNING: type inference failed for: r11v3, types: [java.lang.String] */
            /* JADX WARNING: type inference failed for: r0v9 */
            /* JADX WARNING: type inference failed for: r2v15 */
            /* JADX WARNING: type inference failed for: r0v11, types: [java.io.InputStream] */
            /* JADX WARNING: type inference failed for: r11v4, types: [java.lang.String] */
            /* JADX WARNING: type inference failed for: r2v17 */
            /* JADX WARNING: type inference failed for: r11v5 */
            /* JADX WARNING: type inference failed for: r2v18 */
            /* JADX WARNING: type inference failed for: r2v20, types: [java.lang.String] */
            /* JADX WARNING: type inference failed for: r2v21 */
            /* JADX WARNING: type inference failed for: r11v6 */
            /* JADX WARNING: type inference failed for: r1v32, types: [java.lang.String] */
            /* JADX WARNING: type inference failed for: r11v7 */
            /* JADX WARNING: type inference failed for: r2v22, types: [java.lang.String] */
            /* JADX WARNING: type inference failed for: r11v10 */
            /* JADX WARNING: type inference failed for: r0v18 */
            /* JADX WARNING: type inference failed for: r2v39 */
            /* JADX WARNING: type inference failed for: r2v40 */
            /* JADX WARNING: type inference failed for: r2v41 */
            /* JADX WARNING: type inference failed for: r2v42 */
            /* access modifiers changed from: protected */
            /* JADX WARNING: Code restructure failed: missing block: B:106:0x03c8, code lost:
                r1 = th;
                r0 = r0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:107:0x03cb, code lost:
                r1 = e;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:108:0x03cc, code lost:
                r2 = 0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:123:?, code lost:
                r11.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:124:0x041a, code lost:
                r0 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:125:0x041b, code lost:
                r1 = im.vector.util.BugReporter.access$600();
                r3 = new java.lang.StringBuilder();
                r3.append(r14);
                r3.append(r0.getMessage());
                org.matrix.androidsdk.core.Log.e(r1, r3.toString(), r0);
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v17
  assigns: []
  uses: []
  mth insns count: 378
            	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:30)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
            	at jadx.core.ProcessClass.process(ProcessClass.java:35)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
             */
            /* JADX WARNING: Removed duplicated region for block: B:106:0x03c8 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:88:0x0370] */
            /* JADX WARNING: Removed duplicated region for block: B:122:0x0416 A[SYNTHETIC, Splitter:B:122:0x0416] */
            /* JADX WARNING: Removed duplicated region for block: B:128:0x0439 A[SYNTHETIC, Splitter:B:128:0x0439] */
            /* JADX WARNING: Removed duplicated region for block: B:143:? A[RETURN, SYNTHETIC] */
            /* JADX WARNING: Removed duplicated region for block: B:79:0x0347  */
            /* JADX WARNING: Unknown variable types count: 11 */
            public String doInBackground(Void... voidArr) {
                String str;
                String str2;
                String str3;
                String str4;
                String str5;
                String str6;
                Response response;
                ? r11;
                ? r2;
                ? sb;
                ? r22;
                String str7 = "## sendBugReport() : failed to close the error stream ";
                String str8 = "label";
                String str9 = str2;
                String access$000 = BugReporter.getCrashDescription(context2);
                if (access$000 != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str9);
                    sb2.append("\n\n\n\n--------------------------------- crash call stack ---------------------------------\n");
                    String sb3 = sb2.toString();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(sb3);
                    sb4.append(access$000);
                    str9 = sb4.toString();
                }
                ArrayList arrayList = new ArrayList();
                if (z4) {
                    for (File file : Log.addLogFiles(new ArrayList())) {
                        if (!BugReporter.mIsCancelled) {
                            File access$200 = BugReporter.compressFile(file);
                            if (access$200 != null) {
                                arrayList.add(access$200);
                            }
                        }
                    }
                }
                if (!BugReporter.mIsCancelled && (z5 || z4)) {
                    File access$300 = BugReporter.saveLogCat(context2, false);
                    if (access$300 != null) {
                        if (arrayList.size() == 0) {
                            arrayList.add(access$300);
                        } else {
                            arrayList.add(0, access$300);
                        }
                    }
                    File access$400 = BugReporter.getCrashFile(context2);
                    if (access$400.exists()) {
                        File access$2002 = BugReporter.compressFile(access$400);
                        if (access$2002 != null) {
                            if (arrayList.size() == 0) {
                                arrayList.add(access$2002);
                            } else {
                                arrayList.add(0, access$2002);
                            }
                        }
                    }
                }
                MXSession defaultSession = Matrix.getInstance(context2).getDefaultSession();
                String str10 = BugReporter.BUG_REPORT_URL_SUFFIX;
                String str11 = "undefined";
                if (defaultSession != null) {
                    str11 = defaultSession.getMyUserId();
                    str3 = defaultSession.getCredentials().deviceId;
                    str2 = defaultSession.getVersion(true);
                    str = defaultSession.getCryptoVersion(context2, true);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(defaultSession.getHomeServerConfig().getHomeserverUri());
                    sb5.append(str10);
                    str4 = sb5.toString();
                } else {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(context2.getString(R.string.server_url_prefix));
                    sb6.append(context2.getString(R.string.bug_report_default_host));
                    sb6.append(str10);
                    str4 = sb6.toString();
                    str3 = str11;
                    str2 = str3;
                    str = str2;
                }
                ? r112 = 0;
                if (BugReporter.mIsCancelled) {
                    return null;
                }
                String str12 = "branch_name";
                String str13 = "lazy_loading";
                Builder addFormDataPart = new Builder().addFormDataPart("text", str9).addFormDataPart(BuildConfig.FLAVOR_base, "tchap-android").addFormDataPart("user_agent", RestClient.getUserAgent()).addFormDataPart("user_id", str11).addFormDataPart("device_id", str3).addFormDataPart(TermsResponse.VERSION, Matrix.getInstance(context2).getVersion(true, false)).addFormDataPart(str12, context2.getString(R.string.git_branch_name)).addFormDataPart("matrix_sdk_version", str2).addFormDataPart("olm_version", str).addFormDataPart(Condition.KIND_DEVICE, Build.MODEL.trim()).addFormDataPart(str13, BasicExtensionsKt.toOnOff(PreferencesManager.useLazyLoading(context2)));
                StringBuilder sb7 = new StringBuilder();
                sb7.append(VERSION.RELEASE);
                sb7.append(" (API ");
                sb7.append(VERSION.SDK_INT);
                sb7.append(") ");
                sb7.append(VERSION.INCREMENTAL);
                sb7.append(HelpFormatter.DEFAULT_OPT_PREFIX);
                sb7.append(VERSION.CODENAME);
                String str14 = "locale";
                String str15 = "app_language";
                String str16 = "default_app_language";
                Builder addFormDataPart2 = addFormDataPart.addFormDataPart("os", sb7.toString()).addFormDataPart(str14, Locale.getDefault().toString()).addFormDataPart(str15, VectorLocale.INSTANCE.getApplicationLocale().toString()).addFormDataPart(str16, SystemUtilsKt.getDeviceLocale(context2).toString());
                String string = context2.getString(R.string.build_number);
                if (!TextUtils.isEmpty(string) && !string.equals("0")) {
                    addFormDataPart2.addFormDataPart("build_number", string);
                }
                Iterator it = arrayList.iterator();
                while (true) {
                    str5 = "application/octet-stream";
                    if (!it.hasNext()) {
                        break;
                    }
                    File file2 = (File) it.next();
                    addFormDataPart2.addFormDataPart("compressed-log", file2.getName(), RequestBody.create(MediaType.parse(str5), file2));
                }
                this.mBugReportFiles.addAll(arrayList);
                if (z6) {
                    Bitmap access$500 = BugReporter.mScreenshot;
                    if (access$500 != null) {
                        File file3 = new File(context2.getCacheDir().getAbsolutePath(), BugReporter.LOG_CAT_SCREENSHOT_FILENAME);
                        if (file3.exists()) {
                            file3.delete();
                        }
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file3);
                            access$500.compress(CompressFormat.PNG, 100, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            addFormDataPart2.addFormDataPart("file", file3.getName(), RequestBody.create(MediaType.parse(str5), file3));
                        } catch (Exception e) {
                            String access$600 = BugReporter.LOG_TAG;
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("## saveLogCat() : fail to write logcat");
                            sb8.append(e.toString());
                            Log.e(access$600, sb8.toString(), e);
                        }
                    }
                }
                BugReporter.mScreenshot = null;
                try {
                    addFormDataPart2.addFormDataPart(str8, context2.getPackageManager().getPackageInfo(context2.getPackageName(), 0).versionName);
                } catch (Exception e2) {
                    String access$6002 = BugReporter.LOG_TAG;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("## sendBugReport() : cannot retrieve the appname ");
                    sb9.append(e2.getMessage());
                    Log.e(access$6002, sb9.toString(), e2);
                }
                addFormDataPart2.addFormDataPart(str8, BuildConfig.FLAVOR_DESCRIPTION);
                addFormDataPart2.addFormDataPart(str8, context2.getString(R.string.git_branch_name));
                if (BugReporter.getCrashFile(context2).exists()) {
                    addFormDataPart2.addFormDataPart(str8, "crash");
                    BugReporter.deleteCrashFile(context2);
                }
                addFormDataPart2.addFormDataPart(str8, "dinsic");
                BugReporterMultipartBody build = addFormDataPart2.build();
                build.setWriteListener(new WriteListener() {
                    public void onWrite(long j, long j2) {
                        int i = -1 != j2 ? j > j2 ? 100 : (int) ((j * 100) / j2) : 0;
                        if (BugReporter.mIsCancelled && BugReporter.mBugReportCall != null) {
                            BugReporter.mBugReportCall.cancel();
                        }
                        String access$600 = BugReporter.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## onWrite() : ");
                        sb.append(i);
                        sb.append("%");
                        Log.d(access$600, sb.toString());
                        AnonymousClass1.this.publishProgress(new Integer[]{Integer.valueOf(i)});
                    }
                });
                Request build2 = new Request.Builder().url(str4).post(build).build();
                int i = 500;
                try {
                    BugReporter.mBugReportCall = BugReporter.mOkHttpClient.newCall(build2);
                    response = BugReporter.mBugReportCall.execute();
                    try {
                        i = response.code();
                        str6 = null;
                    } catch (Exception e3) {
                        e = e3;
                        String access$6003 = BugReporter.LOG_TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("response ");
                        sb10.append(e.getMessage());
                        Log.e(access$6003, sb10.toString(), e);
                        str6 = e.getLocalizedMessage();
                        if (i == 200) {
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    response = null;
                    String access$60032 = BugReporter.LOG_TAG;
                    StringBuilder sb102 = new StringBuilder();
                    sb102.append("response ");
                    sb102.append(e.getMessage());
                    Log.e(access$60032, sb102.toString(), e);
                    str6 = e.getLocalizedMessage();
                    if (i == 200) {
                    }
                }
                if (i == 200) {
                    return null;
                }
                String str17 = "Failed with error ";
                if (str6 != null) {
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(str17);
                    sb11.append(str6);
                    return sb11.toString();
                } else if (response == null || response.body() == null) {
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append(str17);
                    sb12.append(i);
                    return sb12.toString();
                } else {
                    try {
                        ? byteStream = response.body().byteStream();
                        if (byteStream != 0) {
                            try {
                                StringBuilder sb13 = new StringBuilder();
                                while (true) {
                                    int read = byteStream.read();
                                    if (read == -1) {
                                        break;
                                    }
                                    sb13.append((char) read);
                                }
                                sb = sb13.toString();
                                byteStream.close();
                                try {
                                    r22 = new JSONObject(sb).getString("error");
                                } catch (JSONException e5) {
                                    String access$6004 = BugReporter.LOG_TAG;
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("doInBackground ; Json conversion failed ");
                                    sb14.append(e5.getMessage());
                                    Log.e(access$6004, sb14.toString(), e5);
                                    r22 = sb;
                                }
                                if (r22 == 0) {
                                    StringBuilder sb15 = new StringBuilder();
                                    sb15.append(str17);
                                    sb15.append(i);
                                    r112 = sb15.toString();
                                } else {
                                    r112 = r22;
                                }
                            } catch (Exception e6) {
                                e = e6;
                                ? r23 = sb;
                                r11 = byteStream;
                                r2 = r23;
                                try {
                                    String access$6005 = BugReporter.LOG_TAG;
                                    StringBuilder sb16 = new StringBuilder();
                                    sb16.append("## sendBugReport() : failed to parse error ");
                                    sb16.append(e.getMessage());
                                    Log.e(access$6005, sb16.toString(), e);
                                    if (r11 != 0) {
                                    }
                                    return r2;
                                } catch (Throwable th) {
                                    th = th;
                                    ? r0 = r11;
                                    if (r0 != 0) {
                                        try {
                                            r0.close();
                                        } catch (Exception e7) {
                                            String access$6006 = BugReporter.LOG_TAG;
                                            StringBuilder sb17 = new StringBuilder();
                                            sb17.append(str7);
                                            sb17.append(e7.getMessage());
                                            Log.e(access$6006, sb17.toString(), e7);
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Throwable th2) {
                            }
                        }
                        if (byteStream == 0) {
                            return r112;
                        }
                        try {
                            byteStream.close();
                            return r112;
                        } catch (Exception e8) {
                            String access$6007 = BugReporter.LOG_TAG;
                            StringBuilder sb18 = new StringBuilder();
                            sb18.append(str7);
                            sb18.append(e8.getMessage());
                            Log.e(access$6007, sb18.toString(), e8);
                            return r112;
                        }
                    } catch (Exception e9) {
                        e = e9;
                        r2 = 0;
                        r11 = r112;
                        String access$60052 = BugReporter.LOG_TAG;
                        StringBuilder sb162 = new StringBuilder();
                        sb162.append("## sendBugReport() : failed to parse error ");
                        sb162.append(e.getMessage());
                        Log.e(access$60052, sb162.toString(), e);
                        if (r11 != 0) {
                        }
                        return r2;
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void onProgressUpdate(Integer... numArr) {
                super.onProgressUpdate(numArr);
                IMXBugReportListener iMXBugReportListener = iMXBugReportListener2;
                if (iMXBugReportListener != null) {
                    int i = 0;
                    if (numArr != null) {
                        try {
                            i = numArr[0].intValue();
                        } catch (Exception e) {
                            String access$600 = BugReporter.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## onProgress() : failed ");
                            sb.append(e.getMessage());
                            Log.e(access$600, sb.toString(), e);
                            return;
                        }
                    }
                    iMXBugReportListener.onProgress(i);
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String str) {
                BugReporter.mBugReportCall = null;
                for (File delete : this.mBugReportFiles) {
                    delete.delete();
                }
                if (iMXBugReportListener2 != null) {
                    try {
                        if (BugReporter.mIsCancelled) {
                            iMXBugReportListener2.onUploadCancelled();
                        } else if (str == null) {
                            iMXBugReportListener2.onUploadSucceed();
                        } else {
                            iMXBugReportListener2.onUploadFailed(str);
                        }
                    } catch (Exception e) {
                        String access$600 = BugReporter.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## onPostExecute() : failed ");
                        sb.append(e.getMessage());
                        Log.e(access$600, sb.toString(), e);
                    }
                }
            }
        };
        r0.execute(new Void[0]);
    }

    public static Bitmap getScreenshot() {
        return mScreenshot;
    }

    public static void sendBugReport() {
        mScreenshot = takeScreenshot();
        Activity currentActivity = VectorApp.getCurrentActivity();
        if (currentActivity == null) {
            sendBugReport(VectorApp.getInstance().getApplicationContext(), true, true, true, "", null);
        } else {
            currentActivity.startActivity(new Intent(currentActivity, BugReportActivity.class));
        }
    }

    /* access modifiers changed from: private */
    public static File getCrashFile(Context context) {
        return new File(context.getCacheDir().getAbsolutePath(), CRASH_FILENAME);
    }

    public static void deleteCrashFile(Context context) {
        File crashFile = getCrashFile(context);
        if (crashFile.exists()) {
            crashFile.delete();
        }
    }

    public static void saveCrashReport(Context context, String str) {
        File crashFile = getCrashFile(context);
        if (crashFile.exists()) {
            crashFile.delete();
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(crashFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write(str);
                outputStreamWriter.close();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## saveCrashReport() : fail to write ");
                sb.append(e.toString());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public static String getCrashDescription(Context context) {
        File crashFile = getCrashFile(context);
        String str = null;
        if (!crashFile.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(crashFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] cArr = new char[fileInputStream.available()];
            str = String.valueOf(cArr, 0, inputStreamReader.read(cArr, 0, fileInputStream.available()));
            inputStreamReader.close();
            fileInputStream.close();
            return str;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getCrashDescription() : fail to read ");
            sb.append(e.toString());
            Log.e(str2, sb.toString(), e);
            return str;
        }
    }

    private static Bitmap takeScreenshot() {
        if (VectorApp.getCurrentActivity() == null) {
            return null;
        }
        View findViewById = VectorApp.getCurrentActivity().findViewById(16908290);
        String str = ". Cannot take screenshot.";
        if (findViewById == null) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot find content view on ");
            sb.append(VectorApp.getCurrentActivity());
            sb.append(str);
            Log.e(str2, sb.toString());
            return null;
        }
        View rootView = findViewById.getRootView();
        if (rootView == null) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot find root view on ");
            sb2.append(VectorApp.getCurrentActivity());
            sb2.append(str);
            Log.e(str3, sb2.toString());
            return null;
        }
        rootView.setDrawingCacheEnabled(false);
        rootView.setDrawingCacheEnabled(true);
        try {
            return rootView.getDrawingCache();
        } catch (OutOfMemoryError e) {
            String str4 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Cannot get drawing cache for ");
            sb3.append(VectorApp.getCurrentActivity());
            sb3.append(" OOM.");
            Log.e(str4, sb3.toString(), e);
            return null;
        } catch (Exception e2) {
            String str5 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Cannot get snapshot of screen: ");
            sb4.append(e2);
            Log.e(str5, sb4.toString(), e2);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static File saveLogCat(Context context, boolean z) {
        String str = "## saveLogCat() : fail to write logcat";
        File file = new File(context.getCacheDir().getAbsolutePath(), z ? LOG_CAT_ERROR_FILENAME : LOG_CAT_FILENAME);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            getLogCatError(outputStreamWriter, z);
            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return compressFile(file);
        } catch (OutOfMemoryError e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(e.toString());
            Log.e(str2, sb.toString(), e);
            return null;
        } catch (Exception e2) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(e2.toString());
            Log.e(str3, sb2.toString(), e2);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0076 A[SYNTHETIC, Splitter:B:28:0x0076] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0086 A[SYNTHETIC, Splitter:B:34:0x0086] */
    private static void getLogCatError(OutputStreamWriter outputStreamWriter, boolean z) {
        String str;
        StringBuilder sb;
        String str2 = "getLog fails with ";
        try {
            Process exec = Runtime.getRuntime().exec(z ? LOGCAT_CMD_ERROR : LOGCAT_CMD_DEBUG);
            BufferedReader bufferedReader = null;
            try {
                String property = System.getProperty("line.separator");
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(exec.getInputStream()), BUFFER_SIZE);
                while (true) {
                    try {
                        String readLine = bufferedReader2.readLine();
                        if (readLine != null) {
                            outputStreamWriter.append(readLine);
                            outputStreamWriter.append(property);
                        } else {
                            try {
                                break;
                            } catch (IOException e) {
                                e = e;
                                str = LOG_TAG;
                                sb = new StringBuilder();
                            }
                        }
                    } catch (IOException e2) {
                        e = e2;
                        bufferedReader = bufferedReader2;
                        try {
                            String str3 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str2);
                            sb2.append(e.getLocalizedMessage());
                            Log.e(str3, sb2.toString(), e);
                            if (bufferedReader != null) {
                            }
                        } catch (Throwable th) {
                            th = th;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e3) {
                                    String str4 = LOG_TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(str2);
                                    sb3.append(e3.getLocalizedMessage());
                                    Log.e(str4, sb3.toString(), e3);
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader = bufferedReader2;
                        if (bufferedReader != null) {
                        }
                        throw th;
                    }
                }
                bufferedReader2.close();
            } catch (IOException e4) {
                e = e4;
                String str32 = LOG_TAG;
                StringBuilder sb22 = new StringBuilder();
                sb22.append(str2);
                sb22.append(e.getLocalizedMessage());
                Log.e(str32, sb22.toString(), e);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                        e = e5;
                        str = LOG_TAG;
                        sb = new StringBuilder();
                    }
                }
            }
            sb.append(str2);
            sb.append(e.getLocalizedMessage());
            Log.e(str, sb.toString(), e);
        } catch (IOException unused) {
        }
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r5v2, types: [java.util.zip.GZIPOutputStream] */
    /* JADX WARNING: type inference failed for: r6v1 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r6v2, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r5v4, types: [java.util.zip.GZIPOutputStream] */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r6v3 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r6v4, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r5v7, types: [java.util.zip.GZIPOutputStream] */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r6v5 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r5v11 */
    /* JADX WARNING: type inference failed for: r5v12 */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: type inference failed for: r5v14 */
    /* JADX WARNING: type inference failed for: r5v15, types: [java.util.zip.GZIPOutputStream] */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: type inference failed for: r6v8 */
    /* JADX WARNING: type inference failed for: r6v9 */
    /* JADX WARNING: type inference failed for: r6v10, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r6v11 */
    /* JADX WARNING: type inference failed for: r5v16 */
    /* JADX WARNING: type inference failed for: r6v12 */
    /* JADX WARNING: type inference failed for: r6v13 */
    /* JADX WARNING: type inference failed for: r5v17 */
    /* JADX WARNING: type inference failed for: r5v18 */
    /* JADX WARNING: type inference failed for: r5v19 */
    /* JADX WARNING: type inference failed for: r6v14 */
    /* JADX WARNING: type inference failed for: r6v15 */
    /* JADX WARNING: type inference failed for: r5v20 */
    /* JADX WARNING: type inference failed for: r5v21 */
    /* JADX WARNING: type inference failed for: r5v22 */
    /* JADX WARNING: type inference failed for: r5v23 */
    /* JADX WARNING: type inference failed for: r5v24 */
    /* JADX WARNING: type inference failed for: r5v25 */
    /* JADX WARNING: type inference failed for: r5v26 */
    /* JADX WARNING: type inference failed for: r5v27 */
    /* JADX WARNING: type inference failed for: r5v28 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r6v17 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r6v1
  assigns: []
  uses: []
  mth insns count: 163
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00f6 A[SYNTHETIC, Splitter:B:45:0x00f6] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00fe A[Catch:{ Exception -> 0x00fa }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0103 A[Catch:{ Exception -> 0x00fa }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x012d A[SYNTHETIC, Splitter:B:60:0x012d] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0135 A[Catch:{ Exception -> 0x0131 }] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x013a A[Catch:{ Exception -> 0x0131 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x015a A[SYNTHETIC, Splitter:B:73:0x015a] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0162 A[Catch:{ Exception -> 0x015e }] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0167 A[Catch:{ Exception -> 0x015e }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:57:0x0113=Splitter:B:57:0x0113, B:42:0x00dc=Splitter:B:42:0x00dc} */
    /* JADX WARNING: Unknown variable types count: 16 */
    public static File compressFile(File file) {
        ? r6;
        ? r5;
        FileOutputStream fileOutputStream;
        ? r62;
        ? r52;
        StringBuilder sb;
        String str;
        ? r63;
        ? r53;
        ? r54;
        ? r55;
        ? r64;
        ? r56;
        ? r57;
        ? r58;
        ? r59;
        ? r510;
        ? gZIPOutputStream;
        String str2 = "## compressFile() failed ";
        String str3 = "## compressFile() failed to close inputStream ";
        String str4 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## compressFile() : compress ");
        sb2.append(file.getName());
        Log.d(str4, sb2.toString());
        String parent = file.getParent();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(file.getName());
        sb3.append(".gz");
        File file2 = new File(parent, sb3.toString());
        if (file2.exists()) {
            file2.delete();
        }
        try {
            fileOutputStream = new FileOutputStream(file2);
            try {
                gZIPOutputStream = new GZIPOutputStream(fileOutputStream);
            } catch (Exception e) {
                e = e;
                r55 = 0;
                r63 = r54;
                r53 = r54;
                r62 = r63;
                r52 = r53;
                String str5 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str2);
                sb4.append(e.getMessage());
                Log.e(str5, sb4.toString(), e);
                r62 = r63;
                r52 = r53;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e2) {
                        e = e2;
                        str = LOG_TAG;
                        sb = new StringBuilder();
                        sb.append(str3);
                        sb.append(e.getMessage());
                        Log.e(str, sb.toString(), e);
                        return null;
                    }
                }
                if (r53 != 0) {
                    r53.close();
                }
                if (r63 != 0) {
                    r63.close();
                }
                return null;
            } catch (OutOfMemoryError e3) {
                e = e3;
                r58 = 0;
                r64 = r57;
                r56 = r57;
                try {
                    r62 = r64;
                    r52 = r56;
                    String str6 = LOG_TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str2);
                    sb5.append(e.getMessage());
                    Log.e(str6, sb5.toString(), e);
                    r62 = r64;
                    r52 = r56;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e4) {
                            e = e4;
                            str = LOG_TAG;
                            sb = new StringBuilder();
                            sb.append(str3);
                            sb.append(e.getMessage());
                            Log.e(str, sb.toString(), e);
                            return null;
                        }
                    }
                    if (r56 != 0) {
                        r56.close();
                    }
                    if (r64 != 0) {
                        r64.close();
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    r6 = r62;
                    r5 = r52;
                    if (fileOutputStream != null) {
                    }
                    if (r5 != 0) {
                    }
                    if (r6 != 0) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                r510 = 0;
                r6 = r59;
                r5 = r59;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e5) {
                        String str7 = LOG_TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str3);
                        sb6.append(e5.getMessage());
                        Log.e(str7, sb6.toString(), e5);
                        throw th;
                    }
                }
                if (r5 != 0) {
                    r5.close();
                }
                if (r6 != 0) {
                    r6.close();
                }
                throw th;
            }
            try {
                ? fileInputStream = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[2048];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        gZIPOutputStream.write(bArr, 0, read);
                    }
                    gZIPOutputStream.close();
                    fileInputStream.close();
                    String str8 = LOG_TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("## compressFile() : ");
                    sb7.append(file.length());
                    sb7.append(" compressed to ");
                    sb7.append(file2.length());
                    sb7.append(" bytes");
                    Log.d(str8, sb7.toString());
                    try {
                        fileOutputStream.close();
                        gZIPOutputStream.close();
                        fileInputStream.close();
                    } catch (Exception e6) {
                        String str9 = LOG_TAG;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str3);
                        sb8.append(e6.getMessage());
                        Log.e(str9, sb8.toString(), e6);
                    }
                    return file2;
                } catch (Exception e7) {
                    e = e7;
                    r53 = gZIPOutputStream;
                    r63 = fileInputStream;
                    r62 = r63;
                    r52 = r53;
                    String str52 = LOG_TAG;
                    StringBuilder sb42 = new StringBuilder();
                    sb42.append(str2);
                    sb42.append(e.getMessage());
                    Log.e(str52, sb42.toString(), e);
                    r62 = r63;
                    r52 = r53;
                    if (fileOutputStream != null) {
                    }
                    if (r53 != 0) {
                    }
                    if (r63 != 0) {
                    }
                    return null;
                } catch (OutOfMemoryError e8) {
                    e = e8;
                    r56 = gZIPOutputStream;
                    r64 = fileInputStream;
                    r62 = r64;
                    r52 = r56;
                    String str62 = LOG_TAG;
                    StringBuilder sb52 = new StringBuilder();
                    sb52.append(str2);
                    sb52.append(e.getMessage());
                    Log.e(str62, sb52.toString(), e);
                    r62 = r64;
                    r52 = r56;
                    if (fileOutputStream != null) {
                    }
                    if (r56 != 0) {
                    }
                    if (r64 != 0) {
                    }
                    return null;
                }
            } catch (Exception e9) {
                e = e9;
                r63 = 0;
                r53 = gZIPOutputStream;
                r62 = r63;
                r52 = r53;
                String str522 = LOG_TAG;
                StringBuilder sb422 = new StringBuilder();
                sb422.append(str2);
                sb422.append(e.getMessage());
                Log.e(str522, sb422.toString(), e);
                r62 = r63;
                r52 = r53;
                if (fileOutputStream != null) {
                }
                if (r53 != 0) {
                }
                if (r63 != 0) {
                }
                return null;
            } catch (OutOfMemoryError e10) {
                e = e10;
                r64 = 0;
                r56 = gZIPOutputStream;
                r62 = r64;
                r52 = r56;
                String str622 = LOG_TAG;
                StringBuilder sb522 = new StringBuilder();
                sb522.append(str2);
                sb522.append(e.getMessage());
                Log.e(str622, sb522.toString(), e);
                r62 = r64;
                r52 = r56;
                if (fileOutputStream != null) {
                }
                if (r56 != 0) {
                }
                if (r64 != 0) {
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                r6 = 0;
                r5 = gZIPOutputStream;
                if (fileOutputStream != null) {
                }
                if (r5 != 0) {
                }
                if (r6 != 0) {
                }
                throw th;
            }
        } catch (Exception e11) {
            e = e11;
            fileOutputStream = null;
            r55 = 0;
            r63 = r54;
            r53 = r54;
            r62 = r63;
            r52 = r53;
            String str5222 = LOG_TAG;
            StringBuilder sb4222 = new StringBuilder();
            sb4222.append(str2);
            sb4222.append(e.getMessage());
            Log.e(str5222, sb4222.toString(), e);
            r62 = r63;
            r52 = r53;
            if (fileOutputStream != null) {
            }
            if (r53 != 0) {
            }
            if (r63 != 0) {
            }
            return null;
        } catch (OutOfMemoryError e12) {
            e = e12;
            fileOutputStream = null;
            r58 = 0;
            r64 = r57;
            r56 = r57;
            r62 = r64;
            r52 = r56;
            String str6222 = LOG_TAG;
            StringBuilder sb5222 = new StringBuilder();
            sb5222.append(str2);
            sb5222.append(e.getMessage());
            Log.e(str6222, sb5222.toString(), e);
            r62 = r64;
            r52 = r56;
            if (fileOutputStream != null) {
            }
            if (r56 != 0) {
            }
            if (r64 != 0) {
            }
            return null;
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream = null;
            r510 = 0;
            r6 = r59;
            r5 = r59;
            if (fileOutputStream != null) {
            }
            if (r5 != 0) {
            }
            if (r6 != 0) {
            }
            throw th;
        }
    }
}
