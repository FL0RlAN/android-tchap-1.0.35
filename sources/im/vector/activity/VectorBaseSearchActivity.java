package im.vector.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBar.LayoutParams;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.Search;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.core.Log;

public abstract class VectorBaseSearchActivity extends MXCActionBarActivity {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorBaseSearchActivity.class.getSimpleName();
    private static final int SPEECH_REQUEST_CODE = 1234;
    private ActionBar mActionBar;
    private MenuItem mClearEditTextMenuItem;
    private MenuItem mMicroMenuItem;
    EditText mPatternToSearchEditText;

    public interface IVectorSearchActivity {
        void refreshSearch();
    }

    public int getMenuRes() {
        return R.menu.vector_searches;
    }

    /* access modifiers changed from: 0000 */
    public void onPatternUpdate(boolean z) {
    }

    public ActivityOtherThemes getOtherThemes() {
        return Search.INSTANCE;
    }

    public void initUiAndData() {
        this.mActionBar = getSupportActionBar();
        View customizeActionBar = customizeActionBar();
        this.mPatternToSearchEditText = (EditText) customizeActionBar.findViewById(R.id.room_action_bar_edit_text);
        customizeActionBar.postDelayed(new Runnable() {
            public void run() {
                VectorBaseSearchActivity.this.mPatternToSearchEditText.requestFocus();
                ((InputMethodManager) VectorBaseSearchActivity.this.getSystemService("input_method")).showSoftInput(VectorBaseSearchActivity.this.mPatternToSearchEditText, 0);
            }
        }, 100);
        this.mPatternToSearchEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                VectorBaseSearchActivity.this.refreshMenuEntries();
                final String obj = VectorBaseSearchActivity.this.mPatternToSearchEditText.getText().toString();
                try {
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            VectorBaseSearchActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (TextUtils.equals(VectorBaseSearchActivity.this.mPatternToSearchEditText.getText().toString(), obj)) {
                                        VectorBaseSearchActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                VectorBaseSearchActivity.this.onPatternUpdate(true);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }, 100);
                } catch (Throwable th) {
                    String access$100 = VectorBaseSearchActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## failed to start the timer ");
                    sb.append(th.getMessage());
                    Log.e(access$100, sb.toString(), th);
                    VectorBaseSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (TextUtils.equals(VectorBaseSearchActivity.this.mPatternToSearchEditText.getText().toString(), obj)) {
                                VectorBaseSearchActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        VectorBaseSearchActivity.this.onPatternUpdate(true);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        this.mPatternToSearchEditText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 3 && (keyEvent == null || keyEvent.getKeyCode() != 66 || keyEvent.getAction() != 0)) {
                    return false;
                }
                VectorBaseSearchActivity.this.onPatternUpdate(false);
                return true;
            }
        });
        this.mPatternToSearchEditText.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.mPatternToSearchEditText.getApplicationWindowToken(), 0);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            public void run() {
                VectorBaseSearchActivity.this.onPatternUpdate(false);
            }
        });
    }

    private View customizeActionBar() {
        this.mActionBar.setDisplayShowCustomEnabled(true);
        this.mActionBar.setDisplayOptions(22);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        View inflate = getLayoutInflater().inflate(R.layout.vector_search_action_bar, null);
        this.mActionBar.setCustomView(inflate, layoutParams);
        return inflate;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (CommonActivityUtils.shouldRestartApp(this)) {
            return false;
        }
        this.mMicroMenuItem = menu.findItem(R.id.ic_action_speak_to_search);
        this.mClearEditTextMenuItem = menu.findItem(R.id.ic_action_clear_search);
        refreshMenuEntries();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.ic_action_clear_search) {
            this.mPatternToSearchEditText.setText("");
            onPatternUpdate(false);
            return true;
        } else if (itemId != R.id.ic_action_speak_to_search) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == SPEECH_REQUEST_CODE && i2 == -1) {
            final ArrayList stringArrayListExtra = intent.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (stringArrayListExtra.size() == 1) {
                this.mPatternToSearchEditText.setText((CharSequence) stringArrayListExtra.get(0));
                onPatternUpdate(false);
            } else if (stringArrayListExtra.size() > 1) {
                new Builder(this).setItems((CharSequence[]) (String[]) stringArrayListExtra.toArray(new String[stringArrayListExtra.size()]), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorBaseSearchActivity.this.mPatternToSearchEditText.setText((CharSequence) stringArrayListExtra.get(i));
                        VectorBaseSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                VectorBaseSearchActivity.this.onPatternUpdate(false);
                            }
                        });
                    }
                }).show();
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    private boolean supportSpeechRecognizer() {
        List queryIntentActivities = getPackageManager().queryIntentActivities(new Intent("android.speech.action.RECOGNIZE_SPEECH"), 0);
        if (queryIntentActivities == null || queryIntentActivities.size() <= 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void refreshMenuEntries() {
        boolean z = true;
        boolean z2 = !TextUtils.isEmpty(this.mPatternToSearchEditText.getText());
        MenuItem menuItem = this.mMicroMenuItem;
        if (menuItem != null) {
            if (z2 || !supportSpeechRecognizer()) {
                z = false;
            }
            menuItem.setVisible(z);
        }
        MenuItem menuItem2 = this.mClearEditTextMenuItem;
        if (menuItem2 != null) {
            menuItem2.setVisible(z2);
        }
    }
}
