package im.vector.preference;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import fr.gouv.tchap.a.R;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public class BingRulePreference extends VectorCustomActionEditTextPreference {
    private static final int NOTIFICATION_NOISY_INDEX = 2;
    private static final int NOTIFICATION_OFF_INDEX = 0;
    private static final int NOTIFICATION_ON_INDEX = 1;
    private BingRule mBingRule;
    private CharSequence[] mRuleStatuses = null;

    public BingRulePreference(Context context) {
        super(context);
    }

    public BingRulePreference(Context context, int i) {
        super(context);
    }

    public BingRulePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BingRulePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setBingRule(BingRule bingRule) {
        this.mBingRule = bingRule;
        refreshSummary();
    }

    public BingRule getRule() {
        return this.mBingRule;
    }

    public void refreshSummary() {
        setSummary(getBingRuleStatuses()[getRuleStatusIndex()]);
    }

    public int getRuleStatusIndex() {
        BingRule bingRule = this.mBingRule;
        if (bingRule != null) {
            if (TextUtils.equals(bingRule.ruleId, BingRule.RULE_ID_SUPPRESS_BOTS_NOTIFICATIONS)) {
                if (this.mBingRule.shouldNotNotify()) {
                    if (this.mBingRule.isEnabled) {
                        return 0;
                    }
                    return 1;
                } else if (this.mBingRule.shouldNotify()) {
                    return 2;
                }
            }
            if (!this.mBingRule.isEnabled || this.mBingRule.shouldNotNotify()) {
                return 0;
            }
            return this.mBingRule.getNotificationSound() != null ? 2 : 1;
        }
        return 0;
    }

    public CharSequence[] getBingRuleStatuses() {
        if (this.mRuleStatuses == null) {
            this.mRuleStatuses = new CharSequence[]{getContext().getString(R.string.notification_off), getContext().getString(R.string.notification_on), getContext().getString(R.string.notification_noisy)};
        }
        return this.mRuleStatuses;
    }

    public BingRule createRule(int i) {
        BingRule bingRule;
        if (this.mBingRule == null || i == getRuleStatusIndex()) {
            bingRule = null;
        } else {
            bingRule = new BingRule(this.mBingRule);
            String str = bingRule.ruleId;
            String str2 = BingRule.RULE_ID_SUPPRESS_BOTS_NOTIFICATIONS;
            boolean equals = TextUtils.equals(str, str2);
            String str3 = BingRule.ACTION_VALUE_DEFAULT;
            boolean z = false;
            if (equals) {
                if (i == 0) {
                    bingRule.isEnabled = true;
                    bingRule.setNotify(false);
                } else if (1 == i) {
                    bingRule.isEnabled = false;
                    bingRule.setNotify(false);
                } else if (2 == i) {
                    bingRule.isEnabled = true;
                    bingRule.setNotify(true);
                    bingRule.setNotificationSound(str3);
                }
                return bingRule;
            }
            String str4 = BingRule.KIND_UNDERRIDE;
            if (i != 0) {
                bingRule.isEnabled = true;
                bingRule.setNotify(true);
                if (!TextUtils.equals(this.mBingRule.kind, str4) && !TextUtils.equals(bingRule.ruleId, BingRule.RULE_ID_INVITE_ME) && 2 == i) {
                    z = true;
                }
                bingRule.setHighlight(z);
                if (2 == i) {
                    if (TextUtils.equals(bingRule.ruleId, BingRule.RULE_ID_CALL)) {
                        str3 = BingRule.ACTION_VALUE_RING;
                    }
                    bingRule.setNotificationSound(str3);
                } else {
                    bingRule.removeNotificationSound();
                }
            } else if (TextUtils.equals(this.mBingRule.kind, str4) || TextUtils.equals(bingRule.ruleId, str2)) {
                bingRule.setNotify(false);
            } else {
                bingRule.isEnabled = false;
            }
        }
        return bingRule;
    }
}
