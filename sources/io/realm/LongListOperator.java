package io.realm;

import io.realm.internal.OsList;
import java.util.Locale;
import javax.annotation.Nullable;

/* compiled from: RealmList */
final class LongListOperator<T> extends ManagedListOperator<T> {
    public boolean forRealmModel() {
        return false;
    }

    LongListOperator(BaseRealm baseRealm, OsList osList, Class<T> cls) {
        super(baseRealm, osList, cls);
    }

    @Nullable
    public T get(int i) {
        T t = (Long) this.osList.getValue((long) i);
        if (t == null) {
            return null;
        }
        if (this.clazz == Long.class) {
            return t;
        }
        if (this.clazz == Integer.class) {
            return this.clazz.cast(Integer.valueOf(t.intValue()));
        }
        if (this.clazz == Short.class) {
            return this.clazz.cast(Short.valueOf(t.shortValue()));
        }
        if (this.clazz == Byte.class) {
            return this.clazz.cast(Byte.valueOf(t.byteValue()));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected element type: ");
        sb.append(this.clazz.getName());
        throw new IllegalStateException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void checkValidValue(@Nullable Object obj) {
        if (obj != null && !(obj instanceof Number)) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Unacceptable value type. Acceptable: %1$s, actual: %2$s .", new Object[]{"java.lang.Long, java.lang.Integer, java.lang.Short, java.lang.Byte", obj.getClass().getName()}));
        }
    }

    public void appendValue(Object obj) {
        this.osList.addLong(((Number) obj).longValue());
    }

    public void insertValue(int i, Object obj) {
        this.osList.insertLong((long) i, ((Number) obj).longValue());
    }

    /* access modifiers changed from: protected */
    public void setValue(int i, Object obj) {
        this.osList.setLong((long) i, ((Number) obj).longValue());
    }
}
