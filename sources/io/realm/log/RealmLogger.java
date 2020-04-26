package io.realm.log;

import javax.annotation.Nullable;

public interface RealmLogger {
    void log(int i, String str, @Nullable Throwable th, @Nullable String str2);
}
