package fr.gouv.tchap.activity;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.activity.MXCActionBarActivity;
import im.vector.util.VectorUtils;

public abstract class TchapContactActionBarActivity extends MXCActionBarActivity {
    protected TextView mActionBarCustomTitle;
    protected TextView mActionBarCustomTopic;
    protected ImageView mAvatar;

    public void initUiAndData() {
        this.mActionBarCustomTitle = (TextView) findViewById(R.id.room_action_bar_title);
        this.mActionBarCustomTopic = (TextView) findViewById(R.id.room_action_bar_topic);
        this.mAvatar = (ImageView) findViewById(R.id.big_avatar_img);
    }

    /* access modifiers changed from: protected */
    public void setTitle(String str) {
        TextView textView = this.mActionBarCustomTitle;
        if (textView != null) {
            textView.setText(str);
        }
    }

    /* access modifiers changed from: protected */
    public void setTopic(String str) {
        TextView textView = this.mActionBarCustomTopic;
        if (textView != null) {
            textView.setText(str);
        }
    }

    /* access modifiers changed from: protected */
    public void setAvatar() {
        if (this.mRoom != null) {
            VectorUtils.loadRoomAvatar((Context) this, this.mSession, this.mAvatar, this.mRoom);
        }
    }
}
