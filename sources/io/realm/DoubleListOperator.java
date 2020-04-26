package io.realm;

import io.realm.internal.OsList;
import java.util.Locale;
import javax.annotation.Nullable;

/* compiled from: RealmList */
final class DoubleListOperator extends ManagedListOperator<Double> {
    public boolean forRealmModel() {
        return false;
    }

    DoubleListOperator(BaseRealm baseRealm, OsList osList, Class<Double> cls) {
        super(baseRealm, osList, cls);
    }

    @Nullable
    public Double get(int i) {
        return (Double) this.osList.getValue((long) i);
    }

    /* access modifiers changed from: protected */
    public void checkValidValue(@Nullable Object obj) {
        if (obj != null && !(obj instanceof Number)) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Unacceptable value type. Acceptable: %1$s, actual: %2$s .", new Object[]{"java.lang.Number", obj.getClass().getName()}));
        }
    }

    public void appendValue(Object obj) {
        this.osList.addDouble(((Number) obj).doubleValue());
    }

    public void insertValue(int i, Object obj) {
        this.osList.insertDouble((long) i, ((Number) obj).doubleValue());
    }

    /* access modifiers changed from: protected */
    public void setValue(int i, Object obj) {
        this.osList.setDouble((long) i, ((Number) obj).doubleValue());
    }
}
