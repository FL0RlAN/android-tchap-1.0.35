package fr.gouv.tchap.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputEditText;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.HexagonMaskView;
import im.vector.activity.VectorAppCompatActivity_ViewBinding;

public class TchapRoomCreationActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private TchapRoomCreationActivity target;
    private View view7f0900e0;
    private TextWatcher view7f0900e0TextWatcher;
    private View view7f090284;
    private View view7f09032a;
    private View view7f09032b;
    private View view7f09032c;

    public TchapRoomCreationActivity_ViewBinding(TchapRoomCreationActivity tchapRoomCreationActivity) {
        this(tchapRoomCreationActivity, tchapRoomCreationActivity.getWindow().getDecorView());
    }

    public TchapRoomCreationActivity_ViewBinding(final TchapRoomCreationActivity tchapRoomCreationActivity, View view) {
        super(tchapRoomCreationActivity, view);
        this.target = tchapRoomCreationActivity;
        tchapRoomCreationActivity.hexagonMaskView = (HexagonMaskView) Utils.findRequiredViewAsType(view, R.id.hexagon_mask_view, "field 'hexagonMaskView'", HexagonMaskView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.rly_hexagon_avatar, "field 'hexagonAvatar' and method 'addRoomAvatar'");
        tchapRoomCreationActivity.hexagonAvatar = findRequiredView;
        this.view7f090284 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapRoomCreationActivity.addRoomAvatar();
            }
        });
        tchapRoomCreationActivity.addAvatarText = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_add_avatar_image, "field 'addAvatarText'", TextView.class);
        View findRequiredView2 = Utils.findRequiredView(view, R.id.et_room_name, "field 'etRoomName' and method 'onTextChanged'");
        tchapRoomCreationActivity.etRoomName = (TextInputEditText) Utils.castView(findRequiredView2, R.id.et_room_name, "field 'etRoomName'", TextInputEditText.class);
        this.view7f0900e0 = findRequiredView2;
        this.view7f0900e0TextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                tchapRoomCreationActivity.onTextChanged(charSequence);
            }
        };
        ((TextView) findRequiredView2).addTextChangedListener(this.view7f0900e0TextWatcher);
        tchapRoomCreationActivity.roomMessageRetentionText = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_room_msg_retention, "field 'roomMessageRetentionText'", TextView.class);
        View findRequiredView3 = Utils.findRequiredView(view, R.id.switch_external_access_room, "field 'externalAccessRoomSwitch' and method 'setRoomExternalAccess'");
        tchapRoomCreationActivity.externalAccessRoomSwitch = (Switch) Utils.castView(findRequiredView3, R.id.switch_external_access_room, "field 'externalAccessRoomSwitch'", Switch.class);
        this.view7f09032b = findRequiredView3;
        ((CompoundButton) findRequiredView3).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                tchapRoomCreationActivity.setRoomExternalAccess();
            }
        });
        View findRequiredView4 = Utils.findRequiredView(view, R.id.switch_public_private_room, "field 'publicPrivateRoomSwitch' and method 'setRoomPrivacy'");
        tchapRoomCreationActivity.publicPrivateRoomSwitch = (Switch) Utils.castView(findRequiredView4, R.id.switch_public_private_room, "field 'publicPrivateRoomSwitch'", Switch.class);
        this.view7f09032c = findRequiredView4;
        ((CompoundButton) findRequiredView4).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                tchapRoomCreationActivity.setRoomPrivacy();
            }
        });
        View findRequiredView5 = Utils.findRequiredView(view, R.id.switch_disable_federation, "field 'disableFederationSwitch' and method 'setRoomFederation'");
        tchapRoomCreationActivity.disableFederationSwitch = (Switch) Utils.castView(findRequiredView5, R.id.switch_disable_federation, "field 'disableFederationSwitch'", Switch.class);
        this.view7f09032a = findRequiredView5;
        ((CompoundButton) findRequiredView5).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                tchapRoomCreationActivity.setRoomFederation();
            }
        });
        tchapRoomCreationActivity.tvPublicPrivateRoomDescription = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_public_private_room_description, "field 'tvPublicPrivateRoomDescription'", TextView.class);
    }

    public void unbind() {
        TchapRoomCreationActivity tchapRoomCreationActivity = this.target;
        if (tchapRoomCreationActivity != null) {
            this.target = null;
            tchapRoomCreationActivity.hexagonMaskView = null;
            tchapRoomCreationActivity.hexagonAvatar = null;
            tchapRoomCreationActivity.addAvatarText = null;
            tchapRoomCreationActivity.etRoomName = null;
            tchapRoomCreationActivity.roomMessageRetentionText = null;
            tchapRoomCreationActivity.externalAccessRoomSwitch = null;
            tchapRoomCreationActivity.publicPrivateRoomSwitch = null;
            tchapRoomCreationActivity.disableFederationSwitch = null;
            tchapRoomCreationActivity.tvPublicPrivateRoomDescription = null;
            this.view7f090284.setOnClickListener(null);
            this.view7f090284 = null;
            ((TextView) this.view7f0900e0).removeTextChangedListener(this.view7f0900e0TextWatcher);
            this.view7f0900e0TextWatcher = null;
            this.view7f0900e0 = null;
            ((CompoundButton) this.view7f09032b).setOnCheckedChangeListener(null);
            this.view7f09032b = null;
            ((CompoundButton) this.view7f09032c).setOnCheckedChangeListener(null);
            this.view7f09032c = null;
            ((CompoundButton) this.view7f09032a).setOnCheckedChangeListener(null);
            this.view7f09032a = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
