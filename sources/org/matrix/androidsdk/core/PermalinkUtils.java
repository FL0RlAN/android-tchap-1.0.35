package org.matrix.androidsdk.core;

import android.net.Uri;
import android.text.TextUtils;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;

public class PermalinkUtils {
    private static final String LOG_TAG = PermalinkUtils.class.getSimpleName();
    private static final String MATRIX_TO_URL_BASE = "https://matrix.to/#/";
    public static final String ULINK_EVENT_ID_KEY = "ULINK_EVENT_ID_KEY";
    public static final String ULINK_GROUP_ID_KEY = "ULINK_GROUP_ID_KEY";
    public static final String ULINK_MATRIX_USER_ID_KEY = "ULINK_MATRIX_USER_ID_KEY";
    public static final String ULINK_ROOM_ID_OR_ALIAS_KEY = "ULINK_ROOM_ID_OR_ALIAS_KEY";

    public static String createPermalink(Event event) {
        if (event == null) {
            return null;
        }
        return createPermalink(event.roomId, event.eventId);
    }

    public static String createPermalink(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(MATRIX_TO_URL_BASE);
        sb.append(escape(str));
        return sb.toString();
    }

    public static String createPermalink(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(MATRIX_TO_URL_BASE);
        sb.append(escape(str));
        sb.append("/");
        sb.append(escape(str2));
        return sb.toString();
    }

    public static String getLinkedId(String str) {
        if (str != null && str.startsWith(MATRIX_TO_URL_BASE)) {
            return str.substring(20);
        }
        return null;
    }

    private static String escape(String str) {
        return str.replaceAll("/", "%2F").replaceAll("\\+", "%2B");
    }

    /* JADX WARNING: Removed duplicated region for block: B:87:0x01a7  */
    public static Map<String, String> parseUniversalLink(Uri uri, List<String> list, List<String> list2) {
        Map<String, String> map;
        String str;
        if (uri != null) {
            try {
                if (!TextUtils.isEmpty(uri.getPath())) {
                    if (list.contains(uri.getHost()) || TextUtils.equals(uri.getHost(), "matrix.to")) {
                        boolean contains = list.contains(uri.getHost());
                        if (!contains || list2.contains(uri.getPath())) {
                            String encodedFragment = uri.getEncodedFragment();
                            if (encodedFragment != null) {
                                String[] split = encodedFragment.substring(1).split("/", 3);
                                int i = 0;
                                while (true) {
                                    str = "UTF-8";
                                    if (i >= split.length) {
                                        break;
                                    }
                                    split[i] = URLDecoder.decode(split[i], str);
                                    i++;
                                }
                                String str2 = BingRule.KIND_ROOM;
                                if (!contains) {
                                    ArrayList arrayList = new ArrayList(Arrays.asList(split));
                                    arrayList.add(0, str2);
                                    split = (String[]) arrayList.toArray(new String[arrayList.size()]);
                                }
                                if (split.length < 2) {
                                    Log.e(LOG_TAG, "## parseUniversalLink : too short");
                                    return null;
                                } else if (TextUtils.equals(split[0], str2) || TextUtils.equals(split[0], PasswordLoginParams.IDENTIFIER_KEY_USER)) {
                                    map = new HashMap<>();
                                    try {
                                        String str3 = split[1];
                                        boolean isUserId = MXPatterns.isUserId(str3);
                                        String str4 = ULINK_ROOM_ID_OR_ALIAS_KEY;
                                        if (!isUserId) {
                                            if (!MXPatterns.isRoomAlias(str3)) {
                                                if (!MXPatterns.isRoomId(str3)) {
                                                    if (MXPatterns.isGroupId(str3)) {
                                                        map.put(ULINK_GROUP_ID_KEY, str3);
                                                    }
                                                }
                                            }
                                            map.put(str4, str3);
                                        } else if (split.length > 2) {
                                            Log.e(LOG_TAG, "## parseUniversalLink : universal link to member id is too long");
                                            return null;
                                        } else {
                                            map.put(ULINK_MATRIX_USER_ID_KEY, str3);
                                        }
                                        if (split.length > 2) {
                                            if (MXPatterns.isEventId(split[2])) {
                                                map.put(ULINK_EVENT_ID_KEY, split[2]);
                                            } else {
                                                Uri parse = Uri.parse(uri.toString().replace("#/room/", "room/"));
                                                map.put(str4, parse.getLastPathSegment());
                                                for (String str5 : parse.getQueryParameterNames()) {
                                                    try {
                                                        map.put(str5, URLDecoder.decode(parse.getQueryParameter(str5), str));
                                                    } catch (Exception e) {
                                                        String str6 = LOG_TAG;
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append("## parseUniversalLink : URLDecoder.decode ");
                                                        sb.append(e.getMessage());
                                                        Log.e(str6, sb.toString(), e);
                                                        return null;
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e2) {
                                        e = e2;
                                        String str7 = LOG_TAG;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("## parseUniversalLink : crashes ");
                                        sb2.append(e.getLocalizedMessage());
                                        Log.e(str7, sb2.toString(), e);
                                        if (map != null) {
                                        }
                                        return map;
                                    }
                                    if (map != null || !map.isEmpty()) {
                                        return map;
                                    }
                                    Log.e(LOG_TAG, "## parseUniversalLink : empty dictionary");
                                    return null;
                                } else {
                                    String str8 = LOG_TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("## parseUniversalLink : not supported ");
                                    sb3.append(split[0]);
                                    Log.e(str8, sb3.toString());
                                    return null;
                                }
                            } else {
                                Log.e(LOG_TAG, "## parseUniversalLink : cannot extract path");
                                return null;
                            }
                        } else {
                            Log.e(LOG_TAG, "## parseUniversalLink : not supported");
                            return null;
                        }
                    } else {
                        String str9 = LOG_TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("## parseUniversalLink : unsupported host ");
                        sb4.append(uri.getHost());
                        Log.e(str9, sb4.toString());
                        return null;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                map = null;
                String str72 = LOG_TAG;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("## parseUniversalLink : crashes ");
                sb22.append(e.getLocalizedMessage());
                Log.e(str72, sb22.toString(), e);
                if (map != null) {
                }
                return map;
            }
        }
        Log.e(LOG_TAG, "## parseUniversalLink : null");
        return null;
    }
}
