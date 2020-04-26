package im.vector.activity;

import fr.gouv.tchap.a.R;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;

public class LoggingOutActivity extends MXCActionBarActivity {
    public int getLayoutRes() {
        return R.layout.vector_activity_splash;
    }

    public void initUiAndData() {
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }
}
