package org.apache.commons.cli;

class Util {
    Util() {
    }

    static String stripLeadingHyphens(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith(HelpFormatter.DEFAULT_LONG_OPT_PREFIX)) {
            return str.substring(2, str.length());
        }
        if (str.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
            str = str.substring(1, str.length());
        }
        return str;
    }

    static String stripLeadingAndTrailingQuotes(String str) {
        String str2 = "\"";
        if (str.startsWith(str2)) {
            str = str.substring(1, str.length());
        }
        return str.endsWith(str2) ? str.substring(0, str.length() - 1) : str;
    }
}
