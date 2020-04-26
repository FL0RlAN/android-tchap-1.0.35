package im.vector.activity;

import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import im.vector.VectorApp;
import im.vector.util.BugReporter.IMXBugReportListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\u0012\u0010\u0007\u001a\u00020\u00032\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010\n\u001a\u00020\u0003H\u0016¨\u0006\u000b"}, d2 = {"im/vector/activity/BugReportActivity$sendBugReport$1", "Lim/vector/util/BugReporter$IMXBugReportListener;", "onProgress", "", "progress", "", "onUploadCancelled", "onUploadFailed", "reason", "", "onUploadSucceed", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: BugReportActivity.kt */
public final class BugReportActivity$sendBugReport$1 implements IMXBugReportListener {
    final /* synthetic */ BugReportActivity this$0;

    BugReportActivity$sendBugReport$1(BugReportActivity bugReportActivity) {
        this.this$0 = bugReportActivity;
    }

    public void onUploadFailed(String str) {
        try {
            if (VectorApp.getInstance() != null && !TextUtils.isEmpty(str)) {
                Toast.makeText(VectorApp.getInstance(), VectorApp.getInstance().getString(R.string.send_bug_report_failed, new Object[]{str}), 1).show();
            }
        } catch (Exception e) {
            String access$getLOG_TAG$cp = BugReportActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onUploadFailed() : failed to display the toast ");
            sb.append(e.getMessage());
            Log.e(access$getLOG_TAG$cp, sb.toString(), e);
        }
        this.this$0.getMMaskView().setVisibility(8);
        this.this$0.getMProgressBar().setVisibility(8);
        this.this$0.getMProgressTextView().setVisibility(8);
        this.this$0.getMScrollView().setAlpha(1.0f);
        this.this$0.invalidateOptionsMenu();
    }

    public void onUploadCancelled() {
        onUploadFailed(null);
    }

    public void onProgress(int i) {
        if (i > 100) {
            Log.e(BugReportActivity.LOG_TAG, "## onProgress() : progress > 100");
            i = 100;
        } else if (i < 0) {
            Log.e(BugReportActivity.LOG_TAG, "## onProgress() : progress < 0");
            i = 0;
        }
        this.this$0.getMProgressBar().setProgress(i);
        TextView mProgressTextView = this.this$0.getMProgressTextView();
        BugReportActivity bugReportActivity = this.this$0;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(i));
        sb.append("");
        mProgressTextView.setText(bugReportActivity.getString(R.string.send_bug_report_progress, new Object[]{sb.toString()}));
    }

    public void onUploadSucceed() {
        try {
            VectorApp instance = VectorApp.getInstance();
            if (instance != null) {
                Toast makeText = Toast.makeText(instance, R.string.send_bug_report_sent, 1);
                makeText.show();
                Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            }
        } catch (Exception e) {
            String access$getLOG_TAG$cp = BugReportActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onUploadSucceed() : failed to dismiss the toast ");
            sb.append(e.getMessage());
            Log.e(access$getLOG_TAG$cp, sb.toString(), e);
        }
        try {
            this.this$0.finish();
        } catch (Exception e2) {
            String access$getLOG_TAG$cp2 = BugReportActivity.LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## onUploadSucceed() : failed to dismiss the dialog ");
            sb2.append(e2.getMessage());
            Log.e(access$getLOG_TAG$cp2, sb2.toString(), e2);
        }
    }
}
