package im.vector.widgets;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import java.io.Serializable;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;

public class WidgetContent implements Serializable {
    private static final String LOG_TAG = "WidgetContent";
    public String creatorUserId;
    public Map<String, Object> data;
    public String id;
    public String name;
    public String type;
    public String url;

    public String getHumanName() {
        String str = " widget";
        if (!TextUtils.isEmpty(this.name)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name);
            sb.append(str);
            return sb.toString();
        } else if (TextUtils.isEmpty(this.type)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Widget ");
            sb2.append(this.id);
            return sb2.toString();
        } else if (this.type.contains("widget")) {
            return this.type;
        } else {
            if (this.id != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(this.type);
                sb3.append(" ");
                sb3.append(this.id);
                return sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(this.type);
            sb4.append(str);
            return sb4.toString();
        }
    }

    public static WidgetContent toWidgetContent(JsonElement jsonElement) {
        try {
            return (WidgetContent) JsonUtils.getGson(false).fromJson(jsonElement, WidgetContent.class);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## toWidgetContent() : failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString(), e);
            return new WidgetContent();
        }
    }
}
