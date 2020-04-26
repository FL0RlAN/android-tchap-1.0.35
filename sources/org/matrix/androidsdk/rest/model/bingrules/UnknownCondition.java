package org.matrix.androidsdk.rest.model.bingrules;

import org.matrix.androidsdk.rest.model.Event;

public class UnknownCondition extends Condition {
    public boolean isSatisfied(Event event) {
        return false;
    }

    public String toString() {
        return "UnknownCondition";
    }

    public UnknownCondition() {
        this.kind = Condition.KIND_UNKNOWN;
    }
}
