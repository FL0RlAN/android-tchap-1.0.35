package org.piwik.sdk.tools;

public class PropertySource {
    public String getHttpAgent() {
        return getSystemProperty("http.agent");
    }

    public String getJVMVersion() {
        return getSystemProperty("java.vm.version");
    }

    public String getSystemProperty(String str) {
        return System.getProperty(str);
    }
}
