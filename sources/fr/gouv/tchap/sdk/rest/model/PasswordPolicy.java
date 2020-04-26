package fr.gouv.tchap.sdk.rest.model;

import com.google.gson.annotations.SerializedName;

public class PasswordPolicy {
    @SerializedName("m.require_digit")
    public boolean isDigitRequired = false;
    @SerializedName("m.require_lowercase")
    public boolean isLowercaseRequired = false;
    @SerializedName("m.require_symbol")
    public boolean isSymbolRequired = false;
    @SerializedName("m.require_uppercase")
    public boolean isUppercaseRequired = false;
    @SerializedName("m.minimum_length")
    public int minLength = 8;
}
