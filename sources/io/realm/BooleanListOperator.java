package io.realm;

import io.realm.internal.OsList;
import java.util.Locale;
import javax.annotation.Nullable;

/* compiled from: RealmList */
final class BooleanListOperator extends ManagedListOperator<Boolean> {
    public boolean forRealmModel() {
        return false;
    }

    BooleanListOperator(BaseRealm baseRealm, OsList osList, Class<Boolean> cls) {
        super(baseRealm, osList, cls);
    }

    @Nullable
    public Boolean get(int i) {
        return (Boolean) this.osList.getValue((long) i);
    }

    /* access modifiers changed from: protected */
    public void checkValidValue(@Nullable Object obj) {
        if (obj != null && !(obj instanceof Boolean)) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Unacceptable value type. Acceptable: %1$s, actual: %2$s .", new Object[]{"java.lang.Boolean", obj.getClass().getName()}));
        }
    }

    public void appendValue(Object obj) {
        this.osList.addBoolean(((Boolean) obj).booleanValue());
    }

    public void insertValue(int i, Object obj) {
        this.osList.insertBoolean((long) i, ((Boolean) obj).booleanValue());
    }

    /* access modifiers changed from: protected */
    public void setValue(int i, Object obj) {
        this.osList.setBoolean((long) i, ((Boolean) obj).booleanValue());
    }
}
