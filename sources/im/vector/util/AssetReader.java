package im.vector.util;

import android.content.Context;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010\t\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005R*\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005`\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lim/vector/util/AssetReader;", "", "()V", "cache", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "clearCache", "", "readAssetFile", "context", "Landroid/content/Context;", "assetFilename", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: AssetReader.kt */
public final class AssetReader {
    public static final AssetReader INSTANCE = new AssetReader();
    private static final HashMap<String, String> cache = new HashMap<>();

    private AssetReader() {
    }

    public final String readAssetFile(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "assetFilename");
        if (cache.containsKey(str)) {
            return (String) cache.get(str);
        }
        String str2 = null;
        try {
            InputStream open = context.getAssets().open(str);
            char[] cArr = new char[1024];
            StringBuilder sb = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(open, "UTF-8");
            while (true) {
                int read = inputStreamReader.read(cArr, 0, cArr.length);
                if (read < 0) {
                    break;
                }
                sb.append(cArr, 0, read);
            }
            str2 = sb.toString();
            cache.put(str, str2);
            inputStreamReader.close();
            open.close();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## readAssetFile() failed : ");
            sb2.append(e.getMessage());
            Log.e("AssetReader", sb2.toString(), e);
        }
        return str2;
    }

    public final void clearCache() {
        cache.clear();
    }
}
