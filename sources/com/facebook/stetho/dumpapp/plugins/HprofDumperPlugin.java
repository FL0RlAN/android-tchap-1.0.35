package com.facebook.stetho.dumpapp.plugins;

import android.content.Context;
import android.os.Debug;
import com.facebook.stetho.common.Util;
import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumpUsageException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import org.apache.commons.cli.HelpFormatter;

public class HprofDumperPlugin implements DumperPlugin {
    private static final String NAME = "hprof";
    private final Context mContext;

    public String getName() {
        return NAME;
    }

    public HprofDumperPlugin(Context context) {
        this.mContext = context;
    }

    public void dump(DumperContext dumperContext) throws DumpException {
        PrintStream stdout = dumperContext.getStdout();
        Iterator it = dumperContext.getArgsAsList().iterator();
        String str = it.hasNext() ? (String) it.next() : null;
        if (str == null) {
            usage(stdout);
        } else if (HelpFormatter.DEFAULT_OPT_PREFIX.equals(str)) {
            handlePipeOutput(stdout);
        } else {
            File file = new File(str);
            if (!file.isAbsolute()) {
                file = this.mContext.getFileStreamPath(str);
            }
            writeHprof(file);
            StringBuilder sb = new StringBuilder();
            sb.append("Wrote to ");
            sb.append(file);
            stdout.println(sb.toString());
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0029 */
    private void handlePipeOutput(OutputStream outputStream) throws DumpException {
        boolean exists;
        File fileStreamPath = this.mContext.getFileStreamPath("hprof-dump.hprof");
        try {
            writeHprof(fileStreamPath);
            FileInputStream fileInputStream = new FileInputStream(fileStreamPath);
            try {
                Util.copy(fileInputStream, outputStream, new byte[2048]);
                fileInputStream.close();
                if (!exists) {
                    return;
                }
                return;
            } catch (IOException ) {
            } catch (Throwable th) {
                fileInputStream.close();
                throw th;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Failure copying ");
            sb.append(fileStreamPath);
            sb.append(" to dumper output");
            throw new DumpException(sb.toString());
        } finally {
            if (fileStreamPath.exists()) {
                fileStreamPath.delete();
            }
        }
    }

    private void writeHprof(File file) throws DumpException {
        try {
            truncateAndDeleteFile(file);
            Debug.dumpHprofData(file.getAbsolutePath());
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failure writing to ");
            sb.append(file);
            sb.append(": ");
            sb.append(e.getMessage());
            throw new DumpException(sb.toString());
        }
    }

    private static void truncateAndDeleteFile(File file) throws IOException {
        new FileOutputStream(file).close();
        if (!file.delete()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to delete ");
            sb.append(file);
            throw new IOException(sb.toString());
        }
    }

    private void usage(PrintStream printStream) throws DumpUsageException {
        printStream.println("Usage: dumpapp hprof [ path ]");
        printStream.println("Dump HPROF memory usage data from the running application.");
        printStream.println();
        printStream.println("Where path can be any of:");
        printStream.println("  -           Output directly to stdout");
        printStream.println("  <path>      Full path to a writable file on the device");
        printStream.println("  <filename>  Relative filename that will be stored in the app internal storage");
        throw new DumpUsageException("Missing path");
    }
}
