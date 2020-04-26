package io.realm;

import io.realm.internal.OsList;
import java.util.Locale;
import javax.annotation.Nullable;

/* compiled from: RealmList */
final class StringListOperator extends ManagedListOperator<String> {
    public boolean forRealmModel() {
        return false;
    }

    StringListOperator(BaseRealm baseRealm, OsList osList, Class<String> cls) {
        super(baseRealm, osList, cls);
    }

    @Nullable
    public String get(int i) {
        return (String) this.osList.getValue((long) i);
    }

    /* access modifiers changed from: protected */
    public void checkValidValue(@Nullable Object obj) {
        if (obj != null && !(obj instanceof String)) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Unacceptable value type. Acceptable: %1$s, actual: %2$s .", new Object[]{"java.lang.String", obj.getClass().getName()}));
        }
    }

    public void appendValue(Object obj) {
        this.osList.addString((String) obj);
    }

    public void insertValue(int i, Object obj) {
        this.osList.insertString((long) i, (String) obj);
    }

    /* access modifiers changed from: protected */
    public void setValue(int i, Object obj) {
        this.osList.setString((long) i, (String) obj);
    }
}
