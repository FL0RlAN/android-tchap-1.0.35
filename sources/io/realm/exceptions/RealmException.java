package io.realm.exceptions;

public final class RealmException extends RuntimeException {
    public RealmException(String str) {
        super(str);
    }

    public RealmException(String str, Throwable th) {
        super(str, th);
    }
}
