package org.matrix.androidsdk.data.comparator;

import java.util.Comparator;
import org.matrix.androidsdk.core.interfaces.DatedObject;

public class Comparators {
    public static final Comparator<DatedObject> ascComparator = new Comparator<DatedObject>() {
        public int compare(DatedObject datedObject, DatedObject datedObject2) {
            return (int) (datedObject.getDate() - datedObject2.getDate());
        }
    };
    public static final Comparator<DatedObject> descComparator = new Comparator<DatedObject>() {
        public int compare(DatedObject datedObject, DatedObject datedObject2) {
            return (int) (datedObject2.getDate() - datedObject.getDate());
        }
    };
}
