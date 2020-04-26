package org.piwik.sdk.tools;

import android.app.Activity;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public class ActivityHelper {
    public static String getBreadcrumbs(Activity activity) {
        ArrayList arrayList = new ArrayList();
        while (activity != null) {
            arrayList.add(activity.getTitle().toString());
            activity = activity.getParent();
        }
        return joinSlash(arrayList);
    }

    public static String joinSlash(List<String> list) {
        return (list == null || list.size() <= 0) ? "" : TextUtils.join("/", list);
    }

    public static String breadcrumbsToPath(String str) {
        return str.replaceAll("\\s", "");
    }
}
