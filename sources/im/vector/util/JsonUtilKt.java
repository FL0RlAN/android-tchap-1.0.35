package im.vector.util;

import com.google.gson.Gson;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\u001a$\u0010\u0000\u001a\u001c\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0001j\n\u0012\u0004\u0012\u00020\u0003\u0018\u0001`\u0004*\u00020\u0003¨\u0006\u0005"}, d2 = {"toJsonMap", "", "", "", "Lim/vector/types/JsonDict;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: JsonUtil.kt */
public final class JsonUtilKt {
    public static final Map<String, Object> toJsonMap(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "receiver$0");
        Gson gson = JsonUtils.getGson(false);
        Map map = null;
        try {
            return (Map) gson.fromJson(gson.toJson(obj), new JsonUtilKt$toJsonMap$1().getType());
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## Any.toJsonMap() failed ");
            sb.append(e.getMessage());
            Log.e("TAG", sb.toString(), e);
            return map;
        }
    }
}
