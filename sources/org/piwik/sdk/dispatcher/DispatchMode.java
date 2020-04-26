package org.piwik.sdk.dispatcher;

public enum DispatchMode {
    ALWAYS("always"),
    WIFI_ONLY("wifi_only");
    
    private final String key;

    private DispatchMode(String str) {
        this.key = str;
    }

    public String toString() {
        return this.key;
    }

    public static DispatchMode fromString(String str) {
        DispatchMode[] values;
        for (DispatchMode dispatchMode : values()) {
            if (dispatchMode.key.equals(str)) {
                return dispatchMode;
            }
        }
        return null;
    }
}
