package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class Versions {
    @SerializedName("versions")
    public List<String> supportedVersions;
    @SerializedName("unstable_features")
    public Map<String, Boolean> unstableFeatures;
}
