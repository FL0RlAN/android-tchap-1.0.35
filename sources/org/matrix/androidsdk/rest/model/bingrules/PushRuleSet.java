package org.matrix.androidsdk.rest.model.bingrules;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public class PushRuleSet {
    public List<ContentRule> content = new ArrayList();
    public List<BingRule> override = new ArrayList();
    public List<BingRule> room = new ArrayList();
    public List<BingRule> sender = new ArrayList();
    public List<BingRule> underride = new ArrayList();

    private BingRule findRule(List<BingRule> list, String str) {
        for (BingRule bingRule : list) {
            if (TextUtils.equals(str, bingRule.ruleId)) {
                return bingRule;
            }
        }
        return null;
    }

    private List<BingRule> getBingRulesList(String str) {
        if (BingRule.KIND_OVERRIDE.equals(str)) {
            return this.override;
        }
        if (BingRule.KIND_ROOM.equals(str)) {
            return this.room;
        }
        if (BingRule.KIND_SENDER.equals(str)) {
            return this.sender;
        }
        if (BingRule.KIND_UNDERRIDE.equals(str)) {
            return this.underride;
        }
        return null;
    }

    public void addAtTop(BingRule bingRule) {
        if (TextUtils.equals(BingRule.KIND_CONTENT, bingRule.kind)) {
            List<ContentRule> list = this.content;
            if (list != null && (bingRule instanceof ContentRule)) {
                list.add(0, (ContentRule) bingRule);
                return;
            }
            return;
        }
        List bingRulesList = getBingRulesList(bingRule.kind);
        if (bingRulesList != null) {
            bingRulesList.add(0, bingRule);
        }
    }

    public boolean remove(BingRule bingRule) {
        if (BingRule.KIND_CONTENT.equals(bingRule.kind)) {
            List<ContentRule> list = this.content;
            if (list != null) {
                return list.remove(bingRule);
            }
        } else {
            List bingRulesList = getBingRulesList(bingRule.kind);
            if (bingRulesList != null) {
                return bingRulesList.remove(bingRule);
            }
        }
        return false;
    }

    private BingRule findContentRule(List<ContentRule> list, String str) {
        for (BingRule bingRule : list) {
            if (TextUtils.equals(str, bingRule.ruleId)) {
                return bingRule;
            }
        }
        return null;
    }

    public BingRule findDefaultRule(String str) {
        if (str == null) {
            return null;
        }
        if (TextUtils.equals(BingRule.RULE_ID_CONTAIN_USER_NAME, str)) {
            return findContentRule(this.content, str);
        }
        BingRule findRule = findRule(this.override, str);
        return findRule == null ? findRule(this.underride, str) : findRule;
    }

    public List<BingRule> getContentRules() {
        ArrayList arrayList = new ArrayList();
        List<ContentRule> list = this.content;
        if (list != null) {
            for (BingRule bingRule : list) {
                if (!bingRule.ruleId.startsWith(".m.")) {
                    arrayList.add(bingRule);
                }
            }
        }
        return arrayList;
    }

    public List<BingRule> getRoomRules() {
        List<BingRule> list = this.room;
        return list == null ? new ArrayList() : list;
    }

    public List<BingRule> getSenderRules() {
        List<BingRule> list = this.sender;
        return list == null ? new ArrayList() : list;
    }
}
