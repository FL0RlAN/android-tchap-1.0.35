package im.vector.util;

import android.content.Context;
import android.util.Log;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t\u001a\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a\u000e\u0010\u000b\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t\u001a7\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052%\u0010\r\u001a!\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0004\u0012\u0004\u0012\u00020\u00030\u000ej\u0002`\u0011H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000*@\u0010\u0012\"\u001d\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0004\u0012\u0004\u0012\u00020\u00030\u000e2\u001d\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0004\u0012\u0004\u0012\u00020\u00030\u000e¨\u0006\u0013"}, d2 = {"LOG_TAG", "", "deleteAction", "", "file", "Ljava/io/File;", "deleteAllFiles", "", "context", "Landroid/content/Context;", "logAction", "lsFiles", "recursiveActionOnFile", "action", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lim/vector/util/ActionOnFile;", "ActionOnFile", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: FileUtils.kt */
public final class FileUtilsKt {
    private static final String LOG_TAG = "FileUtils";

    public static final void deleteAllFiles(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        String str = LOG_TAG;
        Log.v(str, "Delete cache dir:");
        File cacheDir = context.getCacheDir();
        Intrinsics.checkExpressionValueIsNotNull(cacheDir, "context.cacheDir");
        recursiveActionOnFile(cacheDir, FileUtilsKt$deleteAllFiles$1.INSTANCE);
        Log.v(str, "Delete files dir:");
        File filesDir = context.getFilesDir();
        Intrinsics.checkExpressionValueIsNotNull(filesDir, "context.filesDir");
        recursiveActionOnFile(filesDir, FileUtilsKt$deleteAllFiles$2.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static final boolean deleteAction(File file) {
        if (!file.exists()) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("deleteFile: ");
        sb.append(file);
        Log.v(LOG_TAG, sb.toString());
        return file.delete();
    }

    public static final void lsFiles(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        String str = LOG_TAG;
        Log.v(str, "Content of cache dir:");
        File cacheDir = context.getCacheDir();
        Intrinsics.checkExpressionValueIsNotNull(cacheDir, "context.cacheDir");
        recursiveActionOnFile(cacheDir, FileUtilsKt$lsFiles$1.INSTANCE);
        Log.v(str, "Content of files dir:");
        File filesDir = context.getFilesDir();
        Intrinsics.checkExpressionValueIsNotNull(filesDir, "context.filesDir");
        recursiveActionOnFile(filesDir, FileUtilsKt$lsFiles$2.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static final boolean logAction(File file) {
        boolean isDirectory = file.isDirectory();
        String str = LOG_TAG;
        if (isDirectory) {
            Log.d(str, file.toString());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(file.toString());
            sb.append(" ");
            sb.append(file.length());
            sb.append(" bytes");
            Log.d(str, sb.toString());
        }
        return true;
    }

    private static final boolean recursiveActionOnFile(File file, Function1<? super File, Boolean> function1) {
        if (file.isDirectory()) {
            String[] list = file.list();
            Intrinsics.checkExpressionValueIsNotNull(list, "file.list()");
            for (String file2 : list) {
                if (!recursiveActionOnFile(new File(file, file2), function1)) {
                    return false;
                }
            }
        }
        return ((Boolean) function1.invoke(file)).booleanValue();
    }
}
