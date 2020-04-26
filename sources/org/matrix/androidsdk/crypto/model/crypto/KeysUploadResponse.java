package org.matrix.androidsdk.crypto.model.crypto;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class KeysUploadResponse {
    @SerializedName("one_time_key_counts")
    public Map<String, Integer> oneTimeKeyCounts;

    public int oneTimeKeyCountsForAlgorithm(String str) {
        if (this.oneTimeKeyCounts != null && !TextUtils.isEmpty(str)) {
            Integer num = (Integer) this.oneTimeKeyCounts.get(str);
            if (num != null) {
                return num.intValue();
            }
        }
        return 0;
    }

    public boolean hasOneTimeKeyCountsForAlgorithm(String str) {
        Map<String, Integer> map = this.oneTimeKeyCounts;
        return (map == null || str == null || !map.containsKey(str)) ? false : true;
    }
}
