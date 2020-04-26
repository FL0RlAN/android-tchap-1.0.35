package org.matrix.androidsdk.rest.model.bingrules;

import org.matrix.androidsdk.core.EventUtils;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.Message;

public class ContainsDisplayNameCondition extends Condition {
    public ContainsDisplayNameCondition() {
        this.kind = Condition.KIND_CONTAINS_DISPLAY_NAME;
    }

    public boolean isSatisfied(Event event, String str) {
        if (Event.EVENT_TYPE_MESSAGE.equals(event.getType())) {
            Message message = JsonUtils.toMessage(event.getContent());
            if (message != null) {
                return EventUtils.caseInsensitiveFind(str, message.body);
            }
        }
        return false;
    }
}
