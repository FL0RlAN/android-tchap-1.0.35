package fr.gouv.tchap.sdk.rest.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TchapBulkLookupParams {
    @SerializedName("id_server")
    public String idServer;
    public List<List<String>> threepids;
}
