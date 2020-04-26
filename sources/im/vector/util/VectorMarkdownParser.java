package im.vector.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.core.Log;

public class VectorMarkdownParser extends WebView {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorMarkdownParser.class.getSimpleName();
    private static final int MAX_DELAY_TO_WAIT_FOR_WEBVIEW_RESPONSE_MILLIS = 300;
    private boolean mIsInitialised;
    private final MarkDownWebAppInterface mMarkDownWebAppInterface;

    public interface IVectorMarkdownParserListener {
        void onMarkdownParsed(String str, String str2);
    }

    private class MarkDownWebAppInterface {
        /* access modifiers changed from: private */
        public IVectorMarkdownParserListener mListener;
        /* access modifiers changed from: private */
        public String mTextToParse;
        private Timer mWatchdogTimer;

        private MarkDownWebAppInterface() {
        }

        public void initParams(String str, IVectorMarkdownParserListener iVectorMarkdownParserListener) {
            this.mTextToParse = str;
            this.mListener = iVectorMarkdownParserListener;
        }

        public void start() {
            Log.d(VectorMarkdownParser.LOG_TAG, "## start() : Markdown starts");
            try {
                this.mWatchdogTimer = new Timer();
                this.mWatchdogTimer.schedule(new TimerTask() {
                    public void run() {
                        if (MarkDownWebAppInterface.this.mListener != null) {
                            Log.d(VectorMarkdownParser.LOG_TAG, "## start() : delay expires");
                            try {
                                MarkDownWebAppInterface.this.mListener.onMarkdownParsed(MarkDownWebAppInterface.this.mTextToParse, MarkDownWebAppInterface.this.mTextToParse);
                            } catch (Exception e) {
                                String access$100 = VectorMarkdownParser.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## onMarkdownParsed() ");
                                sb.append(e.getMessage());
                                Log.e(access$100, sb.toString(), e);
                            }
                        }
                        MarkDownWebAppInterface.this.done();
                    }
                }, 300);
            } catch (Throwable th) {
                String access$100 = VectorMarkdownParser.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## start() : failed to starts ");
                sb.append(th.getMessage());
                Log.e(access$100, sb.toString(), th);
            }
        }

        public void cancel() {
            Log.d(VectorMarkdownParser.LOG_TAG, "## cancel()");
            done();
        }

        /* access modifiers changed from: private */
        public void done() {
            Timer timer = this.mWatchdogTimer;
            if (timer != null) {
                timer.cancel();
                this.mWatchdogTimer = null;
            }
            this.mListener = null;
        }

        @JavascriptInterface
        public void wOnParse(String str) {
            String trim = str.trim();
            String str2 = "<p>";
            if (trim.startsWith(str2) && trim.lastIndexOf(str2) == 0 && trim.endsWith("</p>")) {
                trim = trim.substring(3, trim.length() - 4);
            }
            if (this.mListener != null) {
                Log.d(VectorMarkdownParser.LOG_TAG, "## wOnParse() : parse done");
                try {
                    this.mListener.onMarkdownParsed(this.mTextToParse, trim);
                } catch (Exception e) {
                    String access$100 = VectorMarkdownParser.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onMarkdownParsed() ");
                    sb.append(e.getMessage());
                    Log.e(access$100, sb.toString(), e);
                }
                done();
                return;
            }
            Log.d(VectorMarkdownParser.LOG_TAG, "## wOnParse() : parse required too much time");
        }
    }

    public VectorMarkdownParser(Context context) {
        this(context, null);
    }

    public VectorMarkdownParser(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VectorMarkdownParser(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsInitialised = false;
        this.mMarkDownWebAppInterface = new MarkDownWebAppInterface();
        initialize();
    }

    private void initialize() {
        try {
            loadUrl("file:///android_asset/html/markdown.html");
            getSettings().setJavaScriptEnabled(true);
            addJavascriptInterface(this.mMarkDownWebAppInterface, "Android");
            getSettings().setAllowUniversalAccessFromFileURLs(true);
            this.mIsInitialised = true;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## initialize() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void markdownToHtml(String str, IVectorMarkdownParserListener iVectorMarkdownParserListener) {
        if (iVectorMarkdownParserListener != null) {
            String trim = str != null ? str.trim() : str;
            if (!this.mIsInitialised || TextUtils.isEmpty(trim) || !PreferencesManager.isMarkdownEnabled(getContext())) {
                iVectorMarkdownParserListener.onMarkdownParsed(str, trim);
                return;
            }
            this.mMarkDownWebAppInterface.initParams(str, iVectorMarkdownParserListener);
            try {
                this.mMarkDownWebAppInterface.start();
                if (VERSION.SDK_INT < 19) {
                    loadUrl(String.format("javascript:convertToHtml('%s')", new Object[]{escapeText(str)}));
                } else {
                    evaluateJavascript(String.format("convertToHtml('%s')", new Object[]{escapeText(str)}), null);
                }
            } catch (Exception e) {
                this.mMarkDownWebAppInterface.cancel();
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## markdownToHtml() : failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
                iVectorMarkdownParserListener.onMarkdownParsed(str, trim);
            }
        }
    }

    private static String escapeText(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\\\n").replace("'", "\\'").replace("\r", "");
    }
}
