package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.activity.util.RequestCodesKt;
import im.vector.push.PushManager.NotificationPrivacy;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.util.SystemUtilsKt;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

public class NotificationPrivacyActivity extends VectorAppCompatActivity {
    private static final String LOG_TAG = NotificationPrivacyActivity.class.getSimpleName();
    @BindView(2131296879)
    RadioButton rbPrivacyLowDetail;
    @BindView(2131296878)
    RadioButton rbPrivacyNormal;
    @BindView(2131297127)
    TextView tvNeedPermission;
    @BindView(2131297128)
    TextView tvNoPermission;

    /* renamed from: im.vector.activity.NotificationPrivacyActivity$2 reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$im$vector$push$PushManager$NotificationPrivacy = new int[NotificationPrivacy.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            $SwitchMap$im$vector$push$PushManager$NotificationPrivacy[NotificationPrivacy.REDUCED.ordinal()] = 1;
            $SwitchMap$im$vector$push$PushManager$NotificationPrivacy[NotificationPrivacy.LOW_DETAIL.ordinal()] = 2;
            try {
                $SwitchMap$im$vector$push$PushManager$NotificationPrivacy[NotificationPrivacy.NORMAL.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public int getLayoutRes() {
        return R.layout.activity_notification_privacy;
    }

    public int getTitleRes() {
        return R.string.settings_notification_privacy;
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        configureToolbar();
        setWaitingView(findViewById(R.id.waiting_view));
        if (VERSION.SDK_INT >= 23) {
            this.tvNeedPermission.setVisibility(0);
            this.tvNoPermission.setVisibility(0);
            return;
        }
        this.tvNeedPermission.setVisibility(8);
        this.tvNoPermission.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        refreshNotificationPrivacy();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 14000) {
            doSetNotificationPrivacy(NotificationPrivacy.NORMAL);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296902})
    public void onNormalClick() {
        updateNotificationPrivacy(NotificationPrivacy.NORMAL);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296901})
    public void onLowDetailClick() {
        updateNotificationPrivacy(NotificationPrivacy.LOW_DETAIL);
    }

    private void updateNotificationPrivacy(NotificationPrivacy notificationPrivacy) {
        if (notificationPrivacy != NotificationPrivacy.NORMAL || SystemUtilsKt.isIgnoringBatteryOptimizations(this)) {
            doSetNotificationPrivacy(notificationPrivacy);
        } else {
            SystemUtilsKt.requestDisablingBatteryOptimization(this, RequestCodesKt.BATTERY_OPTIMIZATION_REQUEST_CODE);
        }
    }

    /* access modifiers changed from: private */
    public void refreshNotificationPrivacy() {
        NotificationPrivacy notificationPrivacy = Matrix.getInstance(this).getPushManager().getNotificationPrivacy();
        boolean z = true;
        this.rbPrivacyNormal.setChecked(notificationPrivacy == NotificationPrivacy.NORMAL);
        RadioButton radioButton = this.rbPrivacyLowDetail;
        if (notificationPrivacy != NotificationPrivacy.LOW_DETAIL) {
            z = false;
        }
        radioButton.setChecked(z);
    }

    private void doSetNotificationPrivacy(NotificationPrivacy notificationPrivacy) {
        showWaitingView();
        Matrix.getInstance(this).getPushManager().setNotificationPrivacy(notificationPrivacy, new SimpleApiCallback<Void>(this) {
            public void onSuccess(Void voidR) {
                NotificationPrivacyActivity.this.hideWaitingView();
                NotificationPrivacyActivity.this.refreshNotificationPrivacy();
            }

            public void onNetworkError(Exception exc) {
                NotificationPrivacyActivity.this.hideWaitingView();
                super.onNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                NotificationPrivacyActivity.this.hideWaitingView();
                super.onMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                NotificationPrivacyActivity.this.hideWaitingView();
                super.onUnexpectedError(exc);
            }
        });
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, NotificationPrivacyActivity.class);
    }

    public static String getNotificationPrivacyString(Context context, NotificationPrivacy notificationPrivacy) {
        int i = AnonymousClass2.$SwitchMap$im$vector$push$PushManager$NotificationPrivacy[notificationPrivacy.ordinal()];
        int i2 = i != 1 ? i != 2 ? R.string.settings_notification_privacy_normal : R.string.settings_notification_privacy_low_detail : R.string.settings_notification_privacy_reduced;
        return context.getString(i2);
    }
}
