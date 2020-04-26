package io.realm.sync;

public enum Subscription$State {
    ERROR(Byte.valueOf(-1)),
    PENDING(Byte.valueOf(0)),
    ACTIVE(Byte.valueOf(1)),
    INVALIDATED(null);
    
    private final Byte nativeValue;

    private Subscription$State(Byte b) {
        this.nativeValue = b;
    }

    public Byte getValue() {
        return this.nativeValue;
    }
}
