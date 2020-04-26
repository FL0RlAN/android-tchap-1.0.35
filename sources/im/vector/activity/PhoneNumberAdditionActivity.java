package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.util.PhoneNumberUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.pid.ThreePid;

public class PhoneNumberAdditionActivity extends VectorAppCompatActivity implements OnEditorActionListener, TextWatcher, OnClickListener {
    private static final String EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID";
    private static final String LOG_TAG = PhoneNumberAdditionActivity.class.getSimpleName();
    private static final int REQUEST_COUNTRY = 1245;
    private static final int REQUEST_VERIFICATION = 6789;
    private TextInputEditText mCountry;
    private TextInputLayout mCountryLayout;
    private PhoneNumber mCurrentPhoneNumber;
    private String mCurrentPhonePrefix;
    private String mCurrentRegionCode;
    private boolean mIsSubmittingPhone;
    private TextInputEditText mPhoneNumber;
    private TextInputLayout mPhoneNumberLayout;
    /* access modifiers changed from: private */
    public MXSession mSession;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public int getLayoutRes() {
        return R.layout.activity_phone_number_addition;
    }

    public int getMenuRes() {
        return R.menu.menu_phone_number_addition;
    }

    public int getTitleRes() {
        return R.string.settings_add_phone_number;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static Intent getIntent(Context context, String str) {
        Intent intent = new Intent(context, PhoneNumberAdditionActivity.class);
        intent.putExtra("EXTRA_MATRIX_ID", str);
        return intent;
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.mCountry = (TextInputEditText) findViewById(R.id.phone_number_country_value);
        this.mCountryLayout = (TextInputLayout) findViewById(R.id.phone_number_country);
        this.mPhoneNumber = (TextInputEditText) findViewById(R.id.phone_number_value);
        this.mPhoneNumberLayout = (TextInputLayout) findViewById(R.id.phone_number);
        setWaitingView(findViewById(R.id.loading_view));
        this.mSession = Matrix.getInstance(this).getSession(getIntent().getStringExtra("EXTRA_MATRIX_ID"));
        MXSession mXSession = this.mSession;
        if (mXSession == null || !mXSession.isAlive()) {
            finish();
        } else {
            initViews();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mIsSubmittingPhone = false;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_add_phone_number) {
            return super.onOptionsItemSelected(menuItem);
        }
        submitPhoneNumber();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i != REQUEST_COUNTRY) {
            if (i == REQUEST_VERIFICATION) {
                setResult(-1, new Intent());
                finish();
            }
        } else if (intent != null) {
            String str = CountryPickerActivity.EXTRA_OUT_COUNTRY_CODE;
            if (intent.hasExtra(str)) {
                this.mCountryLayout.setError(null);
                this.mCountryLayout.setErrorEnabled(false);
                if (TextUtils.isEmpty(this.mPhoneNumber.getText())) {
                    setCountryCode(intent.getStringExtra(str));
                    initPhoneWithPrefix();
                    return;
                }
                String obj = this.mPhoneNumber.getText().toString();
                String str2 = this.mCurrentPhonePrefix;
                if (str2 != null && obj.startsWith(str2)) {
                    obj = obj.substring(this.mCurrentPhonePrefix.length());
                }
                setCountryCode(intent.getStringExtra(str));
                if (TextUtils.isEmpty(obj)) {
                    initPhoneWithPrefix();
                } else {
                    formatPhoneNumber(obj);
                }
            }
        }
    }

    private void initViews() {
        setCountryCode(PhoneNumberUtils.getCountryCode(this));
        initPhoneWithPrefix();
        this.mCountry.setOnClickListener(this);
        this.mPhoneNumber.setOnEditorActionListener(this);
        this.mPhoneNumber.addTextChangedListener(this);
    }

    private void setCountryCode(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals(this.mCurrentRegionCode)) {
            this.mCurrentRegionCode = str;
            this.mCountry.setText(PhoneNumberUtils.getHumanCountryCode(this.mCurrentRegionCode));
            int countryCodeForRegion = PhoneNumberUtil.getInstance().getCountryCodeForRegion(this.mCurrentRegionCode);
            if (countryCodeForRegion > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("+");
                sb.append(countryCodeForRegion);
                this.mCurrentPhonePrefix = sb.toString();
            }
        }
    }

    private void initPhoneWithPrefix() {
        if (!TextUtils.isEmpty(this.mCurrentPhonePrefix)) {
            this.mPhoneNumber.setText(this.mCurrentPhonePrefix);
            TextInputEditText textInputEditText = this.mPhoneNumber;
            textInputEditText.setSelection(textInputEditText.getText().length());
        }
    }

    private void formatPhoneNumber(String str) {
        if (!TextUtils.isEmpty(this.mCurrentRegionCode)) {
            try {
                this.mCurrentPhoneNumber = PhoneNumberUtil.getInstance().parse(str.trim(), this.mCurrentRegionCode);
                String format = PhoneNumberUtil.getInstance().format(this.mCurrentPhoneNumber, PhoneNumberFormat.INTERNATIONAL);
                if (!TextUtils.equals(format, this.mPhoneNumber.getText())) {
                    this.mPhoneNumber.setText(format);
                    this.mPhoneNumber.setSelection(this.mPhoneNumber.getText().length());
                }
            } catch (NumberParseException unused) {
                this.mCurrentPhoneNumber = null;
            }
        }
    }

    private void submitPhoneNumber() {
        if (this.mCurrentRegionCode == null) {
            this.mCountryLayout.setErrorEnabled(true);
            this.mCountryLayout.setError(getString(R.string.settings_phone_number_country_error));
        } else if (this.mCurrentPhoneNumber == null || !PhoneNumberUtil.getInstance().isValidNumberForRegion(this.mCurrentPhoneNumber, this.mCurrentRegionCode)) {
            this.mPhoneNumberLayout.setErrorEnabled(true);
            this.mPhoneNumberLayout.setError(getString(R.string.settings_phone_number_error));
        } else {
            addPhoneNumber(this.mCurrentPhoneNumber);
        }
    }

    private void addPhoneNumber(PhoneNumber phoneNumber) {
        if (!this.mIsSubmittingPhone) {
            this.mIsSubmittingPhone = true;
            showWaitingView();
            final ThreePid threePid = new ThreePid(PhoneNumberUtils.getE164format(phoneNumber), PhoneNumberUtil.getInstance().getRegionCodeForCountryCode(phoneNumber.getCountryCode()), ThreePid.MEDIUM_MSISDN);
            this.mSession.getMyUser().requestPhoneNumberValidationToken(threePid, new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    PhoneNumberAdditionActivity.this.hideWaitingView();
                    PhoneNumberAdditionActivity phoneNumberAdditionActivity = PhoneNumberAdditionActivity.this;
                    PhoneNumberAdditionActivity.this.startActivityForResult(PhoneNumberVerificationActivity.getIntent(phoneNumberAdditionActivity, phoneNumberAdditionActivity.mSession.getCredentials().userId, threePid), PhoneNumberAdditionActivity.REQUEST_VERIFICATION);
                }

                public void onNetworkError(Exception exc) {
                    PhoneNumberAdditionActivity.this.onSubmitPhoneError(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    if (TextUtils.equals(MatrixError.THREEPID_IN_USE, matrixError.errcode)) {
                        PhoneNumberAdditionActivity phoneNumberAdditionActivity = PhoneNumberAdditionActivity.this;
                        phoneNumberAdditionActivity.onSubmitPhoneError(phoneNumberAdditionActivity.getString(R.string.account_phone_number_already_used_error));
                        return;
                    }
                    PhoneNumberAdditionActivity.this.onSubmitPhoneError(matrixError.getLocalizedMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    PhoneNumberAdditionActivity.this.onSubmitPhoneError(exc.getLocalizedMessage());
                }
            });
            return;
        }
        Log.e(LOG_TAG, "Already submitting");
    }

    /* access modifiers changed from: private */
    public void onSubmitPhoneError(String str) {
        this.mIsSubmittingPhone = false;
        hideWaitingView();
        Toast.makeText(this, str, 0).show();
    }

    public void onClick(View view) {
        startActivityForResult(CountryPickerActivity.getIntent(this, true), REQUEST_COUNTRY);
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6 || isFinishing()) {
            return false;
        }
        submitPhoneNumber();
        return true;
    }

    public void afterTextChanged(Editable editable) {
        formatPhoneNumber(editable.toString());
        if (this.mPhoneNumberLayout.getError() != null) {
            this.mPhoneNumberLayout.setError(null);
            this.mPhoneNumberLayout.setErrorEnabled(false);
        }
    }
}
