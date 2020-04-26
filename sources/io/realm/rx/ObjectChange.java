package io.realm.rx;

import io.realm.ObjectChangeSet;
import io.realm.RealmModel;
import javax.annotation.Nullable;

public class ObjectChange<E extends RealmModel> {
    private final ObjectChangeSet changeset;
    private final E object;

    public ObjectChange(E e, @Nullable ObjectChangeSet objectChangeSet) {
        this.object = e;
        this.changeset = objectChangeSet;
    }

    public E getObject() {
        return this.object;
    }

    @Nullable
    public ObjectChangeSet getChangeset() {
        return this.changeset;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ObjectChange objectChange = (ObjectChange) obj;
        if (!this.object.equals(objectChange.object)) {
            return false;
        }
        ObjectChangeSet objectChangeSet = this.changeset;
        ObjectChangeSet objectChangeSet2 = objectChange.changeset;
        if (objectChangeSet != null) {
            z = objectChangeSet.equals(objectChangeSet2);
        } else if (objectChangeSet2 != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int hashCode = this.object.hashCode() * 31;
        ObjectChangeSet objectChangeSet = this.changeset;
        return hashCode + (objectChangeSet != null ? objectChangeSet.hashCode() : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ObjectChange{object=");
        sb.append(this.object);
        sb.append(", changeset=");
        sb.append(this.changeset);
        sb.append('}');
        return sb.toString();
    }
}
