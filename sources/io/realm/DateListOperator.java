package io.realm;

import io.realm.internal.OsList;
import java.util.Date;
import java.util.Locale;
import javax.annotation.Nullable;

/* compiled from: RealmList */
final class DateListOperator extends ManagedListOperator<Date> {
    public boolean forRealmModel() {
        return false;
    }

    DateListOperator(BaseRealm baseRealm, OsList osList, Class<Date> cls) {
        super(baseRealm, osList, cls);
    }

    @Nullable
    public Date get(int i) {
        return (Date) this.osList.getValue((long) i);
    }

    /* access modifiers changed from: protected */
    public void checkValidValue(@Nullable Object obj) {
        if (obj != null && !(obj instanceof Date)) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Unacceptable value type. Acceptable: %1$s, actual: %2$s .", new Object[]{"java.util.Date", obj.getClass().getName()}));
        }
    }

    public void appendValue(Object obj) {
        this.osList.addDate((Date) obj);
    }

    public void insertValue(int i, Object obj) {
        this.osList.insertDate((long) i, (Date) obj);
    }

    /* access modifiers changed from: protected */
    public void setValue(int i, Object obj) {
        this.osList.setDate((long) i, (Date) obj);
    }
}
