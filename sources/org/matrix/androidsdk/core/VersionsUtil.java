package org.matrix.androidsdk.core;

import java.util.Map;
import org.matrix.androidsdk.rest.model.Versions;

public class VersionsUtil {
    private static final String FEATURE_LAZY_LOAD_MEMBERS = "m.lazy_load_members";

    public static boolean supportLazyLoadMembers(Versions versions) {
        if (!(versions == null || versions.unstableFeatures == null)) {
            Map<String, Boolean> map = versions.unstableFeatures;
            String str = FEATURE_LAZY_LOAD_MEMBERS;
            if (map.containsKey(str) && ((Boolean) versions.unstableFeatures.get(str)).booleanValue()) {
                return true;
            }
        }
        return false;
    }
}
