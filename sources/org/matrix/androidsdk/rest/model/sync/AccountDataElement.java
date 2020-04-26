package org.matrix.androidsdk.rest.model.sync;

import java.io.Serializable;
import java.util.Map;

public class AccountDataElement implements Serializable {
    public static final String ACCOUNT_DATA_ACCEPTED_TERMS = "m.accepted_terms";
    public static final String ACCOUNT_DATA_KEY_ACCEPTED_TERMS = "accepted";
    public static final String ACCOUNT_DATA_KEY_IGNORED_USERS = "ignored_users";
    public static final String ACCOUNT_DATA_KEY_URL_PREVIEW_DISABLE = "disable";
    public static final String ACCOUNT_DATA_TYPE_DIRECT_MESSAGES = "m.direct";
    public static final String ACCOUNT_DATA_TYPE_IGNORED_USER_LIST = "m.ignored_user_list";
    public static final String ACCOUNT_DATA_TYPE_PREVIEW_URLS = "org.matrix.preview_urls";
    public static final String ACCOUNT_DATA_TYPE_PUSH_RULES = "m.push_rules";
    public static final String ACCOUNT_DATA_TYPE_WIDGETS = "m.widgets";
    public Map<String, Object> content;
    public String type;
}
