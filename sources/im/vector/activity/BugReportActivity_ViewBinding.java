package im.vector.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public final class BugReportActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private BugReportActivity target;
    private View view7f090053;
    private View view7f090054;
    private TextWatcher view7f090054TextWatcher;

    public BugReportActivity_ViewBinding(BugReportActivity bugReportActivity) {
        this(bugReportActivity, bugReportActivity.getWindow().getDecorView());
    }

    public BugReportActivity_ViewBinding(final BugReportActivity bugReportActivity, View view) {
        super(bugReportActivity, view);
        this.target = bugReportActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.bug_report_edit_text, "field 'mBugReportText' and method 'textChanged$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        bugReportActivity.mBugReportText = (EditText) Utils.castView(findRequiredView, R.id.bug_report_edit_text, "field 'mBugReportText'", EditText.class);
        this.view7f090054 = findRequiredView;
        this.view7f090054TextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                bugReportActivity.textChanged$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        };
        ((TextView) findRequiredView).addTextChangedListener(this.view7f090054TextWatcher);
        bugReportActivity.mIncludeLogsButton = (CheckBox) Utils.findRequiredViewAsType(view, R.id.bug_report_button_include_logs, "field 'mIncludeLogsButton'", CheckBox.class);
        bugReportActivity.mIncludeCrashLogsButton = (CheckBox) Utils.findRequiredViewAsType(view, R.id.bug_report_button_include_crash_logs, "field 'mIncludeCrashLogsButton'", CheckBox.class);
        View findRequiredView2 = Utils.findRequiredView(view, R.id.bug_report_button_include_screenshot, "field 'mIncludeScreenShotButton' and method 'onSendScreenshotChanged$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        bugReportActivity.mIncludeScreenShotButton = (CheckBox) Utils.castView(findRequiredView2, R.id.bug_report_button_include_screenshot, "field 'mIncludeScreenShotButton'", CheckBox.class);
        this.view7f090053 = findRequiredView2;
        ((CompoundButton) findRequiredView2).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                bugReportActivity.onSendScreenshotChanged$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
        bugReportActivity.mScreenShotPreview = (ImageView) Utils.findRequiredViewAsType(view, R.id.bug_report_screenshot_preview, "field 'mScreenShotPreview'", ImageView.class);
        bugReportActivity.mProgressBar = (ProgressBar) Utils.findRequiredViewAsType(view, R.id.bug_report_progress_view, "field 'mProgressBar'", ProgressBar.class);
        bugReportActivity.mProgressTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.bug_report_progress_text_view, "field 'mProgressTextView'", TextView.class);
        bugReportActivity.mScrollView = Utils.findRequiredView(view, R.id.bug_report_scrollview, "field 'mScrollView'");
        bugReportActivity.mMaskView = Utils.findRequiredView(view, R.id.bug_report_mask_view, "field 'mMaskView'");
    }

    public void unbind() {
        BugReportActivity bugReportActivity = this.target;
        if (bugReportActivity != null) {
            this.target = null;
            bugReportActivity.mBugReportText = null;
            bugReportActivity.mIncludeLogsButton = null;
            bugReportActivity.mIncludeCrashLogsButton = null;
            bugReportActivity.mIncludeScreenShotButton = null;
            bugReportActivity.mScreenShotPreview = null;
            bugReportActivity.mProgressBar = null;
            bugReportActivity.mProgressTextView = null;
            bugReportActivity.mScrollView = null;
            bugReportActivity.mMaskView = null;
            ((TextView) this.view7f090054).removeTextChangedListener(this.view7f090054TextWatcher);
            this.view7f090054TextWatcher = null;
            this.view7f090054 = null;
            ((CompoundButton) this.view7f090053).setOnCheckedChangeListener(null);
            this.view7f090053 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
