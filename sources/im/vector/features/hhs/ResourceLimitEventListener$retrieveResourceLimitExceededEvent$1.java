package im.vector.features.hhs;

import androidx.core.app.NotificationCompat;
import com.google.gson.JsonElement;
import im.vector.features.hhs.LimitResourceState.Exceeded;
import im.vector.features.hhs.ResourceLimitEventListener.Callback;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.ServerNoticeUsageLimitContent;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"im/vector/features/hhs/ResourceLimitEventListener$retrieveResourceLimitExceededEvent$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/rest/model/Event;", "onSuccess", "", "event", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ResourceLimitEventListener.kt */
public final class ResourceLimitEventListener$retrieveResourceLimitExceededEvent$1 extends SimpleApiCallback<Event> {
    final /* synthetic */ String $eventId;
    final /* synthetic */ String $roomId;
    final /* synthetic */ ResourceLimitEventListener this$0;

    ResourceLimitEventListener$retrieveResourceLimitExceededEvent$1(ResourceLimitEventListener resourceLimitEventListener, String str, String str2) {
        this.this$0 = resourceLimitEventListener;
        this.$roomId = str;
        this.$eventId = str2;
    }

    public void onSuccess(Event event) {
        Intrinsics.checkParameterIsNotNull(event, NotificationCompat.CATEGORY_EVENT);
        ServerNoticeUsageLimitContent serverNoticeUsageLimitContent = (ServerNoticeUsageLimitContent) JsonUtils.toClass((JsonElement) event.getContentAsJsonObject(), ServerNoticeUsageLimitContent.class);
        Intrinsics.checkExpressionValueIsNotNull(serverNoticeUsageLimitContent, BingRule.KIND_CONTENT);
        if (serverNoticeUsageLimitContent.isServerNoticeUsageLimit()) {
            MatrixError matrixError = new MatrixError(MatrixError.RESOURCE_LIMIT_EXCEEDED, "");
            matrixError.limitType = serverNoticeUsageLimitContent.limit;
            matrixError.adminUri = serverNoticeUsageLimitContent.adminUri;
            this.this$0.limitResourceState = new Exceeded(this.$roomId, this.$eventId, matrixError);
            Callback access$getCallback$p = this.this$0.callback;
            if (access$getCallback$p != null) {
                access$getCallback$p.onResourceLimitStateChanged();
            }
        }
    }
}
