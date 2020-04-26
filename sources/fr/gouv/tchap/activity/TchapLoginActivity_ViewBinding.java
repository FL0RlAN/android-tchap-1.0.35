package fr.gouv.tchap.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorAppCompatActivity_ViewBinding;

public class TchapLoginActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private TchapLoginActivity target;
    private View view7f090111;
    private View view7f090116;
    private View view7f09011a;
    private View view7f09011b;
    private View view7f09011d;
    private View view7f09011f;

    public TchapLoginActivity_ViewBinding(TchapLoginActivity tchapLoginActivity) {
        this(tchapLoginActivity, tchapLoginActivity.getWindow().getDecorView());
    }

    public TchapLoginActivity_ViewBinding(final TchapLoginActivity tchapLoginActivity, View view) {
        super(tchapLoginActivity, view);
        this.target = tchapLoginActivity;
        tchapLoginActivity.screenWelcome = Utils.findRequiredView(view, R.id.fragment_tchap_first_welcome, "field 'screenWelcome'");
        tchapLoginActivity.screenLogin = Utils.findRequiredView(view, R.id.fragment_tchap_first_login, "field 'screenLogin'");
        tchapLoginActivity.screenRegister = Utils.findRequiredView(view, R.id.fragment_tchap_first_register, "field 'screenRegister'");
        tchapLoginActivity.screenForgottenPassword = Utils.findRequiredView(view, R.id.fragment_tchap_first_forgotten_password, "field 'screenForgottenPassword'");
        tchapLoginActivity.screenMessageButton = Utils.findRequiredView(view, R.id.fragment_tchap_first_message_button, "field 'screenMessageButton'");
        tchapLoginActivity.screenRegisterWaitForEmail = Utils.findRequiredView(view, R.id.fragment_tchap_first_register_wait_for_email, "field 'screenRegisterWaitForEmail'");
        tchapLoginActivity.screenRegisterWaitForEmailEmailTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.fragment_tchap_register_wait_for_email_email, "field 'screenRegisterWaitForEmailEmailTextView'", TextView.class);
        tchapLoginActivity.messageNotice = (TextView) Utils.findRequiredViewAsType(view, R.id.fragment_tchap_first_message_button_notice, "field 'messageNotice'", TextView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.fragment_tchap_first_message_button_submit, "field 'messageButton' and method 'messageSubmit'");
        tchapLoginActivity.messageButton = (Button) Utils.castView(findRequiredView, R.id.fragment_tchap_first_message_button_submit, "field 'messageButton'", Button.class);
        this.view7f090116 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapLoginActivity.messageSubmit();
            }
        });
        tchapLoginActivity.mForgotEmailTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.fragment_tchap_first_forget_password_email, "field 'mForgotEmailTextView'", TextView.class);
        tchapLoginActivity.mForgotPassword1TextView = (EditText) Utils.findRequiredViewAsType(view, R.id.fragment_tchap_first_forget_password_new_password, "field 'mForgotPassword1TextView'", EditText.class);
        tchapLoginActivity.mForgotPassword2TextView = (EditText) Utils.findRequiredViewAsType(view, R.id.fragment_tchap_first_forget_password_new_password_confirm, "field 'mForgotPassword2TextView'", EditText.class);
        View findRequiredView2 = Utils.findRequiredView(view, R.id.fragment_tchap_first_forget_password_submit, "method 'onForgotPasswordClick'");
        this.view7f090111 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapLoginActivity.onForgotPasswordClick();
            }
        });
        View findRequiredView3 = Utils.findRequiredView(view, R.id.fragment_tchap_first_welcome_login_button, "method 'onLoginClick'");
        this.view7f09011a = findRequiredView3;
        findRequiredView3.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapLoginActivity.onLoginClick();
            }
        });
        View findRequiredView4 = Utils.findRequiredView(view, R.id.fragment_tchap_first_welcome_register_button, "method 'onRegisterClick'");
        this.view7f09011b = findRequiredView4;
        findRequiredView4.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapLoginActivity.onRegisterClick();
            }
        });
        View findRequiredView5 = Utils.findRequiredView(view, R.id.fragment_tchap_register_wait_for_email_login_button, "method 'goToLoginScreen'");
        this.view7f09011f = findRequiredView5;
        findRequiredView5.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapLoginActivity.goToLoginScreen();
            }
        });
        View findRequiredView6 = Utils.findRequiredView(view, R.id.fragment_tchap_register_wait_for_email_back, "method 'onEmailNotReceived'");
        this.view7f09011d = findRequiredView6;
        findRequiredView6.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapLoginActivity.onEmailNotReceived();
            }
        });
    }

    public void unbind() {
        TchapLoginActivity tchapLoginActivity = this.target;
        if (tchapLoginActivity != null) {
            this.target = null;
            tchapLoginActivity.screenWelcome = null;
            tchapLoginActivity.screenLogin = null;
            tchapLoginActivity.screenRegister = null;
            tchapLoginActivity.screenForgottenPassword = null;
            tchapLoginActivity.screenMessageButton = null;
            tchapLoginActivity.screenRegisterWaitForEmail = null;
            tchapLoginActivity.screenRegisterWaitForEmailEmailTextView = null;
            tchapLoginActivity.messageNotice = null;
            tchapLoginActivity.messageButton = null;
            tchapLoginActivity.mForgotEmailTextView = null;
            tchapLoginActivity.mForgotPassword1TextView = null;
            tchapLoginActivity.mForgotPassword2TextView = null;
            this.view7f090116.setOnClickListener(null);
            this.view7f090116 = null;
            this.view7f090111.setOnClickListener(null);
            this.view7f090111 = null;
            this.view7f09011a.setOnClickListener(null);
            this.view7f09011a = null;
            this.view7f09011b.setOnClickListener(null);
            this.view7f09011b = null;
            this.view7f09011f.setOnClickListener(null);
            this.view7f09011f = null;
            this.view7f09011d.setOnClickListener(null);
            this.view7f09011d = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
