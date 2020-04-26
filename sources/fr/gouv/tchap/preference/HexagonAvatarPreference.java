package fr.gouv.tchap.preference;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.HexagonMaskView;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.MyUser;

public class HexagonAvatarPreference extends EditTextPreference {
    HexagonMaskView mAvatarView;
    private ProgressBar mLoadingProgressBar;
    MXSession mSession;

    /* access modifiers changed from: protected */
    public void showDialog(Bundle bundle) {
    }

    public HexagonAvatarPreference(Context context) {
        super(context);
    }

    public HexagonAvatarPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public HexagonAvatarPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public View onCreateView(ViewGroup viewGroup) {
        setWidgetLayoutResource(R.layout.tchap_settings_hexagon_avatar);
        View onCreateView = super.onCreateView(viewGroup);
        this.mAvatarView = (HexagonMaskView) onCreateView.findViewById(R.id.avatar_img);
        this.mLoadingProgressBar = (ProgressBar) onCreateView.findViewById(R.id.avatar_update_progress_bar);
        refreshAvatar();
        return onCreateView;
    }

    public void refreshAvatar() {
        if (this.mAvatarView != null) {
            MXSession mXSession = this.mSession;
            if (mXSession != null) {
                MyUser myUser = mXSession.getMyUser();
                VectorUtils.loadUserAvatar(getContext(), this.mSession, this.mAvatarView, myUser.getAvatarUrl(), myUser.user_id, myUser.displayname);
            }
        }
    }

    public void setSession(MXSession mXSession) {
        this.mSession = mXSession;
        refreshAvatar();
    }
}
