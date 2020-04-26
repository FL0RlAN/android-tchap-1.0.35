package org.matrix.androidsdk.rest.model.sync;

import java.util.Collections;
import java.util.List;
import org.matrix.androidsdk.crypto.model.rest.DeviceInfo;
import org.matrix.androidsdk.data.comparator.Comparators;

public class DeviceInfoUtil {
    public static void sortByLastSeen(List<DeviceInfo> list) {
        if (list != null) {
            Collections.sort(list, Comparators.descComparator);
        }
    }
}
