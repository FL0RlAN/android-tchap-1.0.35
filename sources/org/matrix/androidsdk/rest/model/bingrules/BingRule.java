package org.matrix.androidsdk.rest.model.bingrules;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;

public class BingRule {
    public static final String ACTION_COALESCE = "coalesce";
    public static final String ACTION_DONT_NOTIFY = "dont_notify";
    public static final String ACTION_NOTIFY = "notify";
    public static final String ACTION_PARAMETER_SET_TWEAK = "set_tweak";
    public static final String ACTION_PARAMETER_VALUE = "value";
    public static final String ACTION_SET_TWEAK_HIGHLIGHT_VALUE = "highlight";
    public static final String ACTION_SET_TWEAK_SOUND_VALUE = "sound";
    public static final String ACTION_VALUE_DEFAULT = "default";
    public static final String ACTION_VALUE_RING = "ring";
    public static final String KIND_CONTENT = "content";
    public static final String KIND_OVERRIDE = "override";
    public static final String KIND_ROOM = "room";
    public static final String KIND_SENDER = "sender";
    public static final String KIND_UNDERRIDE = "underride";
    private static final String LOG_TAG = BingRule.class.getSimpleName();
    public static final String RULE_ID_ALL_OTHER_MESSAGES_ROOMS = ".m.rule.message";
    public static final String RULE_ID_CALL = ".m.rule.call";
    public static final String RULE_ID_CONTAIN_DISPLAY_NAME = ".m.rule.contains_display_name";
    public static final String RULE_ID_CONTAIN_USER_NAME = ".m.rule.contains_user_name";
    public static final String RULE_ID_DISABLE_ALL = ".m.rule.master";
    public static final String RULE_ID_FALLBACK = ".m.rule.fallback";
    public static final String RULE_ID_INVITE_ME = ".m.rule.invite_for_me";
    public static final String RULE_ID_ONE_TO_ONE_ROOM = ".m.rule.room_one_to_one";
    public static final String RULE_ID_PEOPLE_JOIN_LEAVE = ".m.rule.member_event";
    public static final String RULE_ID_SUPPRESS_BOTS_NOTIFICATIONS = ".m.rule.suppress_notices";
    public List<Object> actions;
    public List<Condition> conditions;
    @SerializedName("default")
    public boolean isDefault;
    @SerializedName("enabled")
    public boolean isEnabled;
    public String kind;
    public String ruleId;

    public BingRule(boolean z) {
        this.ruleId = null;
        this.conditions = null;
        this.actions = null;
        this.isDefault = false;
        this.isEnabled = true;
        this.kind = null;
        this.isDefault = z;
    }

    public BingRule() {
        this.ruleId = null;
        this.conditions = null;
        this.actions = null;
        this.isDefault = false;
        this.isEnabled = true;
        this.kind = null;
        this.isDefault = false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BingRule{ruleId='");
        sb.append(this.ruleId);
        sb.append('\'');
        sb.append(", conditions=");
        sb.append(this.conditions);
        sb.append(", actions=");
        sb.append(this.actions);
        sb.append(", isDefault=");
        sb.append(this.isDefault);
        sb.append(", isEnabled=");
        sb.append(this.isEnabled);
        sb.append(", kind='");
        sb.append(this.kind);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }

    public JsonElement toJsonElement() {
        JsonObject asJsonObject = JsonUtils.getGson(false).toJsonTree(this).getAsJsonObject();
        if (this.conditions != null) {
            asJsonObject.add("conditions", JsonUtils.getGson(false).toJsonTree(this.conditions));
        }
        return asJsonObject;
    }

    public BingRule(String str, String str2, Boolean bool, Boolean bool2, boolean z) {
        this.ruleId = null;
        this.conditions = null;
        this.actions = null;
        this.isDefault = false;
        this.isEnabled = true;
        this.kind = null;
        this.ruleId = str2;
        this.isEnabled = true;
        this.isDefault = false;
        this.kind = str;
        this.conditions = null;
        this.actions = new ArrayList();
        if (bool != null) {
            setNotify(bool.booleanValue());
        }
        if (bool2 != null) {
            setHighlight(bool2.booleanValue());
        }
        if (z) {
            setNotificationSound();
        }
    }

    public BingRule(BingRule bingRule) {
        this.ruleId = null;
        this.conditions = null;
        this.actions = null;
        this.isDefault = false;
        this.isEnabled = true;
        this.kind = null;
        this.ruleId = bingRule.ruleId;
        List<Condition> list = bingRule.conditions;
        if (list != null) {
            this.conditions = new ArrayList(list);
        }
        List<Object> list2 = bingRule.actions;
        if (list2 != null) {
            this.actions = new ArrayList(list2);
        }
        this.isDefault = bingRule.isDefault;
        this.isEnabled = bingRule.isEnabled;
        this.kind = bingRule.kind;
    }

    public void addCondition(Condition condition) {
        if (this.conditions == null) {
            this.conditions = new ArrayList();
        }
        this.conditions.add(condition);
    }

    public Map<String, Object> getActionMap(String str) {
        if (this.actions != null && !TextUtils.isEmpty(str)) {
            for (Object next : this.actions) {
                if (next instanceof Map) {
                    try {
                        Map<String, Object> map = (Map) next;
                        if (TextUtils.equals((String) map.get(ACTION_PARAMETER_SET_TWEAK), str)) {
                            return map;
                        }
                    } catch (Exception e) {
                        String str2 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## getActionMap() : ");
                        sb.append(e.getMessage());
                        Log.e(str2, sb.toString(), e);
                    }
                }
            }
        }
        return null;
    }

    public static boolean isDefaultNotificationSound(String str) {
        return ACTION_VALUE_DEFAULT.equals(str);
    }

    public static boolean isCallRingNotificationSound(String str) {
        return ACTION_VALUE_RING.equals(str);
    }

    public String getNotificationSound() {
        Map actionMap = getActionMap(ACTION_SET_TWEAK_SOUND_VALUE);
        if (actionMap != null) {
            String str = ACTION_PARAMETER_VALUE;
            if (actionMap.containsKey(str)) {
                return (String) actionMap.get(str);
            }
        }
        return null;
    }

    public void setNotificationSound() {
        setNotificationSound(ACTION_VALUE_DEFAULT);
    }

    public void setNotificationSound(String str) {
        removeNotificationSound();
        if (!TextUtils.isEmpty(str)) {
            HashMap hashMap = new HashMap();
            hashMap.put(ACTION_PARAMETER_SET_TWEAK, ACTION_SET_TWEAK_SOUND_VALUE);
            hashMap.put(ACTION_PARAMETER_VALUE, str);
            this.actions.add(hashMap);
        }
    }

    public void removeNotificationSound() {
        Map actionMap = getActionMap(ACTION_SET_TWEAK_SOUND_VALUE);
        if (actionMap != null) {
            this.actions.remove(actionMap);
        }
    }

    public void setHighlight(boolean z) {
        String str = ACTION_SET_TWEAK_HIGHLIGHT_VALUE;
        Map actionMap = getActionMap(str);
        if (actionMap == null) {
            actionMap = new HashMap();
            actionMap.put(ACTION_PARAMETER_SET_TWEAK, str);
            this.actions.add(actionMap);
        }
        String str2 = ACTION_PARAMETER_VALUE;
        if (z) {
            actionMap.remove(str2);
        } else {
            actionMap.put(str2, Boolean.valueOf(false));
        }
    }

    public boolean shouldHighlight() {
        Map actionMap = getActionMap(ACTION_SET_TWEAK_HIGHLIGHT_VALUE);
        if (actionMap == null) {
            return false;
        }
        String str = ACTION_PARAMETER_VALUE;
        if (!actionMap.containsKey(str)) {
            return true;
        }
        Object obj = actionMap.get(str);
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof String) {
            return TextUtils.equals((String) obj, "true");
        }
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## shouldHighlight() : unexpected type ");
        sb.append(obj);
        Log.e(str2, sb.toString());
        return true;
    }

    public void setNotify(boolean z) {
        String str = ACTION_DONT_NOTIFY;
        String str2 = ACTION_NOTIFY;
        if (z) {
            this.actions.remove(str);
            if (!this.actions.contains(str2)) {
                this.actions.add(str2);
                return;
            }
            return;
        }
        this.actions.remove(str2);
        if (!this.actions.contains(str)) {
            this.actions.add(str);
        }
    }

    public boolean shouldNotify() {
        return this.actions.contains(ACTION_NOTIFY);
    }

    public boolean shouldNotNotify() {
        return this.actions.contains(ACTION_DONT_NOTIFY);
    }
}
