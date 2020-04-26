package org.matrix.androidsdk.core;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MXPatterns {
    private static final String APP_BASE_REGEX = "https://[A-Z0-9.-]+\\.[A-Z]{2,}/[A-Z]{3,}/#/room/";
    private static final String DOMAIN_REGEX = ":[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String LINK_TO_APP_ROOM_ALIAS_REGEXP = "https://[A-Z0-9.-]+\\.[A-Z]{2,}/[A-Z]{3,}/#/room/#[A-Z0-9._%#@=+-]+:[A-Z0-9.-]+(:[0-9]{2,5})?/\\$[A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String LINK_TO_APP_ROOM_ID_REGEXP = "https://[A-Z0-9.-]+\\.[A-Z]{2,}/[A-Z]{3,}/#/room/![A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?/\\$[A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String LINK_TO_ROOM_ALIAS_REGEXP = "https://matrix\\.to/#/#[A-Z0-9._%#@=+-]+:[A-Z0-9.-]+(:[0-9]{2,5})?/\\$[A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String LINK_TO_ROOM_ID_REGEXP = "https://matrix\\.to/#/![A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?/\\$[A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String MATRIX_EVENT_IDENTIFIER_REGEX = "\\$[A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String MATRIX_EVENT_IDENTIFIER_V3_REGEX = "\\$[A-Z0-9/+]+";
    private static final String MATRIX_EVENT_IDENTIFIER_V4_REGEX = "\\$[A-Z0-9\\_\\-]+";
    private static final String MATRIX_GROUP_IDENTIFIER_REGEX = "\\+[A-Z0-9=_\\-./]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    public static final List<Pattern> MATRIX_PATTERNS = Arrays.asList(new Pattern[]{PATTERN_CONTAIN_MATRIX_TO_PERMALINK_ROOM_ID, PATTERN_CONTAIN_MATRIX_TO_PERMALINK_ROOM_ALIAS, PATTERN_CONTAIN_APP_LINK_PERMALINK_ROOM_ID, PATTERN_CONTAIN_APP_LINK_PERMALINK_ROOM_ALIAS, PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER, PATTERN_CONTAIN_MATRIX_ALIAS, PATTERN_CONTAIN_MATRIX_ROOM_IDENTIFIER, PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER, PATTERN_CONTAIN_MATRIX_GROUP_IDENTIFIER});
    private static final String MATRIX_ROOM_ALIAS_REGEX = "#[A-Z0-9._%#@=+-]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String MATRIX_ROOM_IDENTIFIER_REGEX = "![A-Z0-9]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    private static final String MATRIX_USER_IDENTIFIER_REGEX = "@[A-Z0-9\\x21-\\x39\\x3B-\\x7F]+:[A-Z0-9.-]+(:[0-9]{2,5})?";
    public static final Pattern PATTERN_CONTAIN_APP_LINK_PERMALINK_ROOM_ALIAS = Pattern.compile(LINK_TO_APP_ROOM_ALIAS_REGEXP, 2);
    public static final Pattern PATTERN_CONTAIN_APP_LINK_PERMALINK_ROOM_ID = Pattern.compile(LINK_TO_APP_ROOM_ID_REGEXP, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_ALIAS = Pattern.compile(MATRIX_ROOM_ALIAS_REGEX, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER = Pattern.compile(MATRIX_EVENT_IDENTIFIER_REGEX, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER_V3 = Pattern.compile(MATRIX_EVENT_IDENTIFIER_V3_REGEX, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER_V4 = Pattern.compile(MATRIX_EVENT_IDENTIFIER_V4_REGEX, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_GROUP_IDENTIFIER = Pattern.compile(MATRIX_GROUP_IDENTIFIER_REGEX, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_ROOM_IDENTIFIER = Pattern.compile(MATRIX_ROOM_IDENTIFIER_REGEX, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_TO_PERMALINK_ROOM_ALIAS = Pattern.compile(LINK_TO_ROOM_ALIAS_REGEXP, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_TO_PERMALINK_ROOM_ID = Pattern.compile(LINK_TO_ROOM_ID_REGEXP, 2);
    public static final Pattern PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER = Pattern.compile(MATRIX_USER_IDENTIFIER_REGEX, 2);
    private static final String PERMALINK_BASE_REGEX = "https://matrix\\.to/#/";
    private static final String SEP_REGEX = "/";

    private MXPatterns() {
    }

    public static boolean isUserId(String str) {
        return str != null && PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER.matcher(str).matches();
    }

    public static boolean isRoomId(String str) {
        return str != null && PATTERN_CONTAIN_MATRIX_ROOM_IDENTIFIER.matcher(str).matches();
    }

    public static boolean isRoomAlias(String str) {
        return str != null && PATTERN_CONTAIN_MATRIX_ALIAS.matcher(str).matches();
    }

    public static boolean isEventId(String str) {
        return str != null && (PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER.matcher(str).matches() || PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER_V3.matcher(str).matches() || PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER_V4.matcher(str).matches());
    }

    public static boolean isGroupId(String str) {
        return str != null && PATTERN_CONTAIN_MATRIX_GROUP_IDENTIFIER.matcher(str).matches();
    }

    public static String extractServerNameFromId(String str) {
        if (str == null) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf(":");
        if (lastIndexOf == -1) {
            return null;
        }
        return str.substring(lastIndexOf + 1);
    }
}
