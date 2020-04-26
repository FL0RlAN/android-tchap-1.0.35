package org.matrix.androidsdk.rest.client;

import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2CallbackWrapper;
import org.matrix.androidsdk.rest.api.PushRulesApi;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.bingrules.PushRulesResponse;

public class PushRulesRestClient extends RestClient<PushRulesApi> {
    public PushRulesRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, PushRulesApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(false));
    }

    public void getAllRules(ApiCallback<PushRulesResponse> apiCallback) {
        ((PushRulesApi) this.mApi).getAllRules().enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }

    public void updateEnableRuleStatus(String str, String str2, boolean z, ApiCallback<Void> apiCallback) {
        ((PushRulesApi) this.mApi).updateEnableRuleStatus(str, str2, Boolean.valueOf(z)).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }

    public void updateRuleActions(String str, String str2, Object obj, ApiCallback<Void> apiCallback) {
        ((PushRulesApi) this.mApi).updateRuleActions(str, str2, obj).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }

    public void deleteRule(String str, String str2, ApiCallback<Void> apiCallback) {
        ((PushRulesApi) this.mApi).deleteRule(str, str2).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }

    public void addRule(BingRule bingRule, ApiCallback<Void> apiCallback) {
        ((PushRulesApi) this.mApi).addRule(bingRule.kind, bingRule.ruleId, bingRule.toJsonElement()).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }
}
