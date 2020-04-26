package org.matrix.androidsdk.rest.model.sync;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class AccountData implements Serializable {
    @SerializedName("events")
    public List<AccountDataElement> accountDataElements;
}
