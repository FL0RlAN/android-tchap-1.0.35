package im.vector.activity;

import com.google.gson.JsonElement;
import fr.gouv.tchap.a.R;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.Event;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0014\u0010\u0007\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0014\u0010\f\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016¨\u0006\r"}, d2 = {"im/vector/activity/IntegrationManagerActivity$getMembershipState$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/rest/model/Event;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "event", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: IntegrationManagerActivity.kt */
public final class IntegrationManagerActivity$getMembershipState$1 implements ApiCallback<Event> {
    final /* synthetic */ Map $eventData;
    final /* synthetic */ String $userId;
    final /* synthetic */ IntegrationManagerActivity this$0;

    IntegrationManagerActivity$getMembershipState$1(IntegrationManagerActivity integrationManagerActivity, String str, Map map) {
        this.this$0 = integrationManagerActivity;
        this.$userId = str;
        this.$eventData = map;
    }

    public void onSuccess(Event event) {
        String access$getLOG_TAG$cp = IntegrationManagerActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("membership_state of ");
        sb.append(this.$userId);
        sb.append(" in room ");
        Room mRoom = this.this$0.getMRoom();
        if (mRoom == null) {
            Intrinsics.throwNpe();
        }
        sb.append(mRoom.getRoomId());
        sb.append(" returns ");
        sb.append(event);
        Log.d(access$getLOG_TAG$cp, sb.toString());
        if (event != null) {
            IntegrationManagerActivity integrationManagerActivity = this.this$0;
            JsonElement content = event.getContent();
            Intrinsics.checkExpressionValueIsNotNull(content, "event.content");
            integrationManagerActivity.sendObjectAsJsonMap(content, this.$eventData);
            return;
        }
        this.this$0.sendObjectResponse(null, this.$eventData);
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        String access$getLOG_TAG$cp = IntegrationManagerActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("membership_state of ");
        sb.append(this.$userId);
        sb.append(" in room ");
        Room mRoom = this.this$0.getMRoom();
        if (mRoom == null) {
            Intrinsics.throwNpe();
        }
        sb.append(mRoom.getRoomId());
        sb.append(" failed ");
        sb.append(exc.getMessage());
        Log.e(access$getLOG_TAG$cp, sb.toString(), exc);
        IntegrationManagerActivity integrationManagerActivity = this.this$0;
        String string = integrationManagerActivity.getString(R.string.widget_integration_failed_to_send_request);
        Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…n_failed_to_send_request)");
        integrationManagerActivity.sendError(string, this.$eventData);
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        String access$getLOG_TAG$cp = IntegrationManagerActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("membership_state of ");
        sb.append(this.$userId);
        sb.append(" in room ");
        Room mRoom = this.this$0.getMRoom();
        if (mRoom == null) {
            Intrinsics.throwNpe();
        }
        sb.append(mRoom.getRoomId());
        sb.append(" failed ");
        sb.append(matrixError.getMessage());
        Log.e(access$getLOG_TAG$cp, sb.toString());
        IntegrationManagerActivity integrationManagerActivity = this.this$0;
        String string = integrationManagerActivity.getString(R.string.widget_integration_failed_to_send_request);
        Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…n_failed_to_send_request)");
        integrationManagerActivity.sendError(string, this.$eventData);
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        String access$getLOG_TAG$cp = IntegrationManagerActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("membership_state of ");
        sb.append(this.$userId);
        sb.append(" in room ");
        Room mRoom = this.this$0.getMRoom();
        if (mRoom == null) {
            Intrinsics.throwNpe();
        }
        sb.append(mRoom.getRoomId());
        sb.append(" failed ");
        sb.append(exc.getMessage());
        Log.e(access$getLOG_TAG$cp, sb.toString(), exc);
        IntegrationManagerActivity integrationManagerActivity = this.this$0;
        String string = integrationManagerActivity.getString(R.string.widget_integration_failed_to_send_request);
        Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…n_failed_to_send_request)");
        integrationManagerActivity.sendError(string, this.$eventData);
    }
}
