package im.vector.activity;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class NotificationPrivacyActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private NotificationPrivacyActivity target;
    private View view7f090285;
    private View view7f090286;

    public NotificationPrivacyActivity_ViewBinding(NotificationPrivacyActivity notificationPrivacyActivity) {
        this(notificationPrivacyActivity, notificationPrivacyActivity.getWindow().getDecorView());
    }

    public NotificationPrivacyActivity_ViewBinding(final NotificationPrivacyActivity notificationPrivacyActivity, View view) {
        super(notificationPrivacyActivity, view);
        this.target = notificationPrivacyActivity;
        notificationPrivacyActivity.tvNeedPermission = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_apps_needs_permission, "field 'tvNeedPermission'", TextView.class);
        notificationPrivacyActivity.tvNoPermission = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_apps_no_permission, "field 'tvNoPermission'", TextView.class);
        notificationPrivacyActivity.rbPrivacyNormal = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rb_normal_notification_privacy, "field 'rbPrivacyNormal'", RadioButton.class);
        notificationPrivacyActivity.rbPrivacyLowDetail = (RadioButton) Utils.findRequiredViewAsType(view, R.id.rb_notification_low_detail, "field 'rbPrivacyLowDetail'", RadioButton.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.rly_normal_notification_privacy, "method 'onNormalClick'");
        this.view7f090286 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                notificationPrivacyActivity.onNormalClick();
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.rly_low_detail_notifications, "method 'onLowDetailClick'");
        this.view7f090285 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                notificationPrivacyActivity.onLowDetailClick();
            }
        });
    }

    public void unbind() {
        NotificationPrivacyActivity notificationPrivacyActivity = this.target;
        if (notificationPrivacyActivity != null) {
            this.target = null;
            notificationPrivacyActivity.tvNeedPermission = null;
            notificationPrivacyActivity.tvNoPermission = null;
            notificationPrivacyActivity.rbPrivacyNormal = null;
            notificationPrivacyActivity.rbPrivacyLowDetail = null;
            this.view7f090286.setOnClickListener(null);
            this.view7f090286 = null;
            this.view7f090285.setOnClickListener(null);
            this.view7f090285 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
