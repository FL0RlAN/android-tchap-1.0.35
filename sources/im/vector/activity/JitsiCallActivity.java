package im.vector.activity;

import fr.gouv.tchap.a.R;

public class JitsiCallActivity extends VectorAppCompatActivity {
    public static final String EXTRA_ENABLE_VIDEO = "EXTRA_ENABLE_VIDEO";
    public static final String EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID";
    private static final String LOG_TAG = JitsiCallActivity.class.getSimpleName();

    public int getLayoutRes() {
        return R.layout.activity_jitsi_call;
    }
}
