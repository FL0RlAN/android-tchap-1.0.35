package org.matrix.androidsdk.rest.model;

public class MediaScanError {
    public static final String MCS_BAD_DECRYPTION = "MCS_BAD_DECRYPTION";
    public static final String MCS_MALFORMED_JSON = "MCS_MALFORMED_JSON";
    public static final String MCS_MEDIA_FAILED_TO_DECRYPT = "MCS_MEDIA_FAILED_TO_DECRYPT";
    public static final String MCS_MEDIA_NOT_CLEAN = "MCS_MEDIA_NOT_CLEAN";
    public static final String MCS_MEDIA_REQUEST_FAILED = "MCS_MEDIA_REQUEST_FAILED";
    public String info;
    public String reason;
}
