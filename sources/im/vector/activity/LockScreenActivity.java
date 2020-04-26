package im.vector.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.notifications.NotificationUtils;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.Lock;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.Message;

public class LockScreenActivity extends VectorAppCompatActivity {
    private static final String EXTRA_MATRIX_ID = "extra_matrix_id";
    public static final String EXTRA_MESSAGE_BODY = "extra_chat_body";
    public static final String EXTRA_ROOM_ID = "extra_room_id";
    public static final String EXTRA_SENDER_NAME = "extra_sender_name";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = LockScreenActivity.class.getSimpleName();
    private static LockScreenActivity mLockScreenActivity = null;
    private LinearLayout mMainLayout;

    public int getLayoutRes() {
        return R.layout.activity_lock_screen;
    }

    public static boolean isDisplayingALockScreenActivity() {
        return mLockScreenActivity != null;
    }

    public ActivityOtherThemes getOtherThemes() {
        return Lock.INSTANCE;
    }

    public void doBeforeSetContentView() {
        Window window = getWindow();
        window.addFlags(524288);
        window.addFlags(2048);
        window.addFlags(4194304);
        window.addFlags(2097152);
        requestWindowFeature(1);
    }

    public void initUiAndData() {
        LockScreenActivity lockScreenActivity = mLockScreenActivity;
        if (lockScreenActivity != null) {
            lockScreenActivity.finish();
        }
        mLockScreenActivity = this;
        NotificationUtils.INSTANCE.cancelAllNotifications(this);
        Intent intent = getIntent();
        String str = EXTRA_ROOM_ID;
        if (!intent.hasExtra(str)) {
            finish();
            return;
        }
        String str2 = EXTRA_SENDER_NAME;
        if (!intent.hasExtra(str2)) {
            finish();
            return;
        }
        final String stringExtra = intent.getStringExtra(str);
        String str3 = null;
        String str4 = EXTRA_MATRIX_ID;
        if (intent.hasExtra(str4)) {
            str3 = intent.getStringExtra(str4);
        }
        final MXSession session = Matrix.getInstance(getApplicationContext()).getSession(str3);
        final Room room = session.getDataHandler().getRoom(stringExtra);
        String roomDisplayName = DinsicUtils.getRoomDisplayName(this, room);
        setTitle(roomDisplayName);
        ((TextView) findViewById(R.id.lock_screen_sender)).setText(getString(R.string.generic_label, new Object[]{intent.getStringExtra(str2)}));
        ((TextView) findViewById(R.id.lock_screen_body)).setText(intent.getStringExtra(EXTRA_MESSAGE_BODY));
        ((TextView) findViewById(R.id.lock_screen_room_name)).setText(roomDisplayName);
        final ImageButton imageButton = (ImageButton) findViewById(R.id.lock_screen_sendbutton);
        final EditText editText = (EditText) findViewById(R.id.lock_screen_edittext);
        imageButton.setEnabled(false);
        imageButton.setAlpha(0.5f);
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    imageButton.setEnabled(false);
                    imageButton.setAlpha(0.5f);
                    return;
                }
                imageButton.setEnabled(true);
                imageButton.setAlpha(1.0f);
            }
        });
        final EditText editText2 = editText;
        AnonymousClass2 r4 = new OnClickListener() {
            public void onClick(View view) {
                Log.d(LockScreenActivity.LOG_TAG, "Send a message ...");
                String obj = editText2.getText().toString();
                Message message = new Message();
                message.msgtype = Message.MSGTYPE_TEXT;
                message.body = obj;
                Event event = new Event(message, session.getCredentials().userId, stringExtra);
                room.storeOutgoingEvent(event);
                room.sendEvent(event, new ApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        Log.d(LockScreenActivity.LOG_TAG, "Send message : onSuccess ");
                    }

                    public void onNetworkError(Exception exc) {
                        String access$000 = LockScreenActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Send message : onNetworkError ");
                        sb.append(exc.getMessage());
                        Log.d(access$000, sb.toString(), exc);
                        Toast.makeText(LockScreenActivity.this, exc.getLocalizedMessage(), 0).show();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        String access$000 = LockScreenActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Send message : onMatrixError ");
                        sb.append(matrixError.getMessage());
                        Log.d(access$000, sb.toString());
                        if (matrixError instanceof MXCryptoError) {
                            Toast.makeText(LockScreenActivity.this, ((MXCryptoError) matrixError).getDetailedErrorDescription(), 0).show();
                        } else {
                            Toast.makeText(LockScreenActivity.this, matrixError.getLocalizedMessage(), 0).show();
                        }
                    }

                    public void onUnexpectedError(Exception exc) {
                        String access$000 = LockScreenActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Send message : onUnexpectedError ");
                        sb.append(exc.getMessage());
                        Log.d(access$000, sb.toString(), exc);
                        Toast.makeText(LockScreenActivity.this, exc.getLocalizedMessage(), 0).show();
                    }
                });
                LockScreenActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        LockScreenActivity.this.finish();
                    }
                });
            }
        };
        imageButton.setOnClickListener(r4);
        this.mMainLayout = (LinearLayout) findViewById(R.id.lock_main_layout);
    }

    private void refreshMainLayout() {
        LinearLayout linearLayout = this.mMainLayout;
        if (linearLayout != null) {
            LayoutParams layoutParams = linearLayout.getLayoutParams();
            layoutParams.width = (int) (((float) getResources().getDisplayMetrics().widthPixels) * 0.8f);
            this.mMainLayout.setLayoutParams(layoutParams);
        }
    }

    public void onResume() {
        super.onResume();
        refreshMainLayout();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        refreshMainLayout();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this == mLockScreenActivity) {
            mLockScreenActivity = null;
        }
    }
}
