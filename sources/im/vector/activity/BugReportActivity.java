package im.vector.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnTextChanged;
import fr.gouv.tchap.a.R;
import im.vector.VectorApp;
import im.vector.util.BugReporter;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 A2\u00020\u0001:\u0001AB\u0005¢\u0006\u0002\u0010\u0002J\b\u00100\u001a\u000201H\u0016J\b\u00102\u001a\u000201H\u0016J\b\u00103\u001a\u000204H\u0016J\u0010\u00105\u001a\u0002062\u0006\u00107\u001a\u000208H\u0016J\u0010\u00109\u001a\u0002062\u0006\u0010:\u001a\u00020;H\u0016J\r\u0010<\u001a\u000204H\u0001¢\u0006\u0002\b=J\b\u0010>\u001a\u000204H\u0002J\r\u0010?\u001a\u000204H\u0001¢\u0006\u0002\b@R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\n8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001e\u0010\u0012\u001a\u00020\n8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001e\u0010\u0015\u001a\u00020\u00168\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001e\u0010\u001b\u001a\u00020\u001c8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001e\u0010!\u001a\u00020\"8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001e\u0010'\u001a\u00020(8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u001e\u0010-\u001a\u00020\u00168\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0018\"\u0004\b/\u0010\u001a¨\u0006B"}, d2 = {"Lim/vector/activity/BugReportActivity;", "Lim/vector/activity/MXCActionBarActivity;", "()V", "mBugReportText", "Landroid/widget/EditText;", "getMBugReportText", "()Landroid/widget/EditText;", "setMBugReportText", "(Landroid/widget/EditText;)V", "mIncludeCrashLogsButton", "Landroid/widget/CheckBox;", "getMIncludeCrashLogsButton", "()Landroid/widget/CheckBox;", "setMIncludeCrashLogsButton", "(Landroid/widget/CheckBox;)V", "mIncludeLogsButton", "getMIncludeLogsButton", "setMIncludeLogsButton", "mIncludeScreenShotButton", "getMIncludeScreenShotButton", "setMIncludeScreenShotButton", "mMaskView", "Landroid/view/View;", "getMMaskView", "()Landroid/view/View;", "setMMaskView", "(Landroid/view/View;)V", "mProgressBar", "Landroid/widget/ProgressBar;", "getMProgressBar", "()Landroid/widget/ProgressBar;", "setMProgressBar", "(Landroid/widget/ProgressBar;)V", "mProgressTextView", "Landroid/widget/TextView;", "getMProgressTextView", "()Landroid/widget/TextView;", "setMProgressTextView", "(Landroid/widget/TextView;)V", "mScreenShotPreview", "Landroid/widget/ImageView;", "getMScreenShotPreview", "()Landroid/widget/ImageView;", "setMScreenShotPreview", "(Landroid/widget/ImageView;)V", "mScrollView", "getMScrollView", "setMScrollView", "getLayoutRes", "", "getMenuRes", "initUiAndData", "", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onPrepareOptionsMenu", "menu", "Landroid/view/Menu;", "onSendScreenshotChanged", "onSendScreenshotChanged$vector_appAgentWithoutvoipWithpinningMatrixorg", "sendBugReport", "textChanged", "textChanged$vector_appAgentWithoutvoipWithpinningMatrixorg", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: BugReportActivity.kt */
public final class BugReportActivity extends MXCActionBarActivity {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final String LOG_TAG = BugReportActivity.class.getSimpleName();
    private HashMap _$_findViewCache;
    @BindView(2131296340)
    public EditText mBugReportText;
    @BindView(2131296337)
    public CheckBox mIncludeCrashLogsButton;
    @BindView(2131296338)
    public CheckBox mIncludeLogsButton;
    @BindView(2131296339)
    public CheckBox mIncludeScreenShotButton;
    @BindView(2131296341)
    public View mMaskView;
    @BindView(2131296343)
    public ProgressBar mProgressBar;
    @BindView(2131296342)
    public TextView mProgressTextView;
    @BindView(2131296344)
    public ImageView mScreenShotPreview;
    @BindView(2131296345)
    public View mScrollView;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lim/vector/activity/BugReportActivity$Companion;", "", "()V", "LOG_TAG", "", "kotlin.jvm.PlatformType", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: BugReportActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public int getLayoutRes() {
        return R.layout.activity_bug_report;
    }

    public int getMenuRes() {
        return R.menu.bug_report;
    }

    public final EditText getMBugReportText() {
        EditText editText = this.mBugReportText;
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBugReportText");
        }
        return editText;
    }

    public final void setMBugReportText(EditText editText) {
        Intrinsics.checkParameterIsNotNull(editText, "<set-?>");
        this.mBugReportText = editText;
    }

    public final CheckBox getMIncludeLogsButton() {
        CheckBox checkBox = this.mIncludeLogsButton;
        if (checkBox == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mIncludeLogsButton");
        }
        return checkBox;
    }

    public final void setMIncludeLogsButton(CheckBox checkBox) {
        Intrinsics.checkParameterIsNotNull(checkBox, "<set-?>");
        this.mIncludeLogsButton = checkBox;
    }

    public final CheckBox getMIncludeCrashLogsButton() {
        CheckBox checkBox = this.mIncludeCrashLogsButton;
        if (checkBox == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mIncludeCrashLogsButton");
        }
        return checkBox;
    }

    public final void setMIncludeCrashLogsButton(CheckBox checkBox) {
        Intrinsics.checkParameterIsNotNull(checkBox, "<set-?>");
        this.mIncludeCrashLogsButton = checkBox;
    }

    public final CheckBox getMIncludeScreenShotButton() {
        CheckBox checkBox = this.mIncludeScreenShotButton;
        if (checkBox == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mIncludeScreenShotButton");
        }
        return checkBox;
    }

    public final void setMIncludeScreenShotButton(CheckBox checkBox) {
        Intrinsics.checkParameterIsNotNull(checkBox, "<set-?>");
        this.mIncludeScreenShotButton = checkBox;
    }

    public final ImageView getMScreenShotPreview() {
        ImageView imageView = this.mScreenShotPreview;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mScreenShotPreview");
        }
        return imageView;
    }

    public final void setMScreenShotPreview(ImageView imageView) {
        Intrinsics.checkParameterIsNotNull(imageView, "<set-?>");
        this.mScreenShotPreview = imageView;
    }

    public final ProgressBar getMProgressBar() {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mProgressBar");
        }
        return progressBar;
    }

    public final void setMProgressBar(ProgressBar progressBar) {
        Intrinsics.checkParameterIsNotNull(progressBar, "<set-?>");
        this.mProgressBar = progressBar;
    }

    public final TextView getMProgressTextView() {
        TextView textView = this.mProgressTextView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mProgressTextView");
        }
        return textView;
    }

    public final void setMProgressTextView(TextView textView) {
        Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
        this.mProgressTextView = textView;
    }

    public final View getMScrollView() {
        View view = this.mScrollView;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mScrollView");
        }
        return view;
    }

    public final void setMScrollView(View view) {
        Intrinsics.checkParameterIsNotNull(view, "<set-?>");
        this.mScrollView = view;
    }

    public final View getMMaskView() {
        View view = this.mMaskView;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMaskView");
        }
        return view;
    }

    public final void setMMaskView(View view) {
        Intrinsics.checkParameterIsNotNull(view, "<set-?>");
        this.mMaskView = view;
    }

    public void initUiAndData() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        String str = "mScreenShotPreview";
        if (BugReporter.getScreenshot() != null) {
            ImageView imageView = this.mScreenShotPreview;
            if (imageView == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            imageView.setImageBitmap(BugReporter.getScreenshot());
            return;
        }
        ImageView imageView2 = this.mScreenShotPreview;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        imageView2.setVisibility(8);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0047, code lost:
        if ((r1.getVisibility() == 0) == false) goto L_0x004b;
     */
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        MenuItem findItem = menu.findItem(R.id.ic_action_send_bug_report);
        if (findItem != null) {
            EditText editText = this.mBugReportText;
            if (editText == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mBugReportText");
            }
            String obj = editText.getText().toString();
            if (obj != null) {
                boolean z = true;
                if (StringsKt.trim((CharSequence) obj).toString().length() > 10) {
                    View view = this.mMaskView;
                    if (view == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("mMaskView");
                    }
                }
                z = false;
                findItem.setEnabled(z);
                Drawable icon = findItem.getIcon();
                Intrinsics.checkExpressionValueIsNotNull(icon, "it.icon");
                icon.setAlpha(z ? 255 : 100);
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intrinsics.checkParameterIsNotNull(menuItem, "item");
        if (menuItem.getItemId() != R.id.ic_action_send_bug_report) {
            return super.onOptionsItemSelected(menuItem);
        }
        sendBugReport();
        return true;
    }

    private final void sendBugReport() {
        View view = this.mScrollView;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mScrollView");
        }
        view.setAlpha(0.3f);
        View view2 = this.mMaskView;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMaskView");
        }
        view2.setVisibility(0);
        invalidateOptionsMenu();
        TextView textView = this.mProgressTextView;
        String str = "mProgressTextView";
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView.setVisibility(0);
        TextView textView2 = this.mProgressTextView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView2.setText(getString(R.string.send_bug_report_progress, new Object[]{"0"}));
        ProgressBar progressBar = this.mProgressBar;
        String str2 = "mProgressBar";
        if (progressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str2);
        }
        progressBar.setVisibility(0);
        ProgressBar progressBar2 = this.mProgressBar;
        if (progressBar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str2);
        }
        progressBar2.setProgress(0);
        Context instance = VectorApp.getInstance();
        CheckBox checkBox = this.mIncludeLogsButton;
        if (checkBox == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mIncludeLogsButton");
        }
        boolean isChecked = checkBox.isChecked();
        CheckBox checkBox2 = this.mIncludeCrashLogsButton;
        if (checkBox2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mIncludeCrashLogsButton");
        }
        boolean isChecked2 = checkBox2.isChecked();
        CheckBox checkBox3 = this.mIncludeScreenShotButton;
        if (checkBox3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mIncludeScreenShotButton");
        }
        boolean isChecked3 = checkBox3.isChecked();
        EditText editText = this.mBugReportText;
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mBugReportText");
        }
        BugReporter.sendBugReport(instance, isChecked, isChecked2, isChecked3, editText.getText().toString(), new BugReportActivity$sendBugReport$1(this));
    }

    @OnTextChanged({2131296340})
    public final void textChanged$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        invalidateOptionsMenu();
    }

    @OnCheckedChanged({2131296339})
    public final void onSendScreenshotChanged$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        ImageView imageView = this.mScreenShotPreview;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mScreenShotPreview");
        }
        View view = imageView;
        CheckBox checkBox = this.mIncludeScreenShotButton;
        if (checkBox == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mIncludeScreenShotButton");
        }
        int i = 0;
        if (!(checkBox.isChecked() && BugReporter.getScreenshot() != null)) {
            i = 8;
        }
        view.setVisibility(i);
    }
}
