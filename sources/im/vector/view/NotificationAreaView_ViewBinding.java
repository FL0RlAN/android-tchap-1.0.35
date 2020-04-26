package im.vector.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public final class NotificationAreaView_ViewBinding implements Unbinder {
    private NotificationAreaView target;

    public NotificationAreaView_ViewBinding(NotificationAreaView notificationAreaView) {
        this(notificationAreaView, notificationAreaView);
    }

    public NotificationAreaView_ViewBinding(NotificationAreaView notificationAreaView, View view) {
        this.target = notificationAreaView;
        notificationAreaView.imageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.room_notification_icon, "field 'imageView'", ImageView.class);
        notificationAreaView.messageView = (TextView) Utils.findRequiredViewAsType(view, R.id.room_notification_message, "field 'messageView'", TextView.class);
    }

    public void unbind() {
        NotificationAreaView notificationAreaView = this.target;
        if (notificationAreaView != null) {
            this.target = null;
            notificationAreaView.imageView = null;
            notificationAreaView.messageView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
