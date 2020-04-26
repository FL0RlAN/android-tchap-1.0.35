package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import fr.gouv.tchap.a.R;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0016J&\u0010\b\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u000fH\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\"\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u000f2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0014J\u0010\u0010\u001b\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J&\u0010\u001e\u001a\u00020\u00152\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X.¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lim/vector/activity/StickerPickerActivity;", "Lim/vector/activity/AbstractWidgetActivity;", "()V", "mWidgetId", "", "mWidgetUrl", "buildInterfaceUrl", "scalarToken", "dealsWithWidgetRequest", "", "eventData", "", "", "Lim/vector/types/JsonDict;", "getLayoutRes", "", "getMenuRes", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "getTitleRes", "initUiAndData", "", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "sendSticker", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: StickerPickerActivity.kt */
public final class StickerPickerActivity extends AbstractWidgetActivity {
    public static final Companion Companion = new Companion(null);
    private static final String EXTRA_OUT_CONTENT = "EXTRA_OUT_CONTENT";
    private static final String EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID";
    private static final String EXTRA_WIDGET_URL = "EXTRA_WIDGET_URL";
    private static final String LOG_TAG = StickerPickerActivity.class.getSimpleName();
    public static final String WIDGET_NAME = "m.stickerpicker";
    private HashMap _$_findViewCache;
    private String mWidgetId;
    private String mWidgetUrl;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J.\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0004J\u000e\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lim/vector/activity/StickerPickerActivity$Companion;", "", "()V", "EXTRA_OUT_CONTENT", "", "EXTRA_WIDGET_ID", "EXTRA_WIDGET_URL", "LOG_TAG", "kotlin.jvm.PlatformType", "WIDGET_NAME", "getIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "matrixId", "roomId", "widgetUrl", "widgetId", "getResultContent", "intent", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: StickerPickerActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Intent getIntent(Context context, String str, String str2, String str3, String str4) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, "matrixId");
            Intrinsics.checkParameterIsNotNull(str2, CryptoRoomEntityFields.ROOM_ID);
            Intrinsics.checkParameterIsNotNull(str3, "widgetUrl");
            Intrinsics.checkParameterIsNotNull(str4, "widgetId");
            Intent intent = new Intent(context, StickerPickerActivity.class);
            intent.putExtra("EXTRA_MATRIX_ID", str);
            intent.putExtra("EXTRA_ROOM_ID", str2);
            intent.putExtra(StickerPickerActivity.EXTRA_WIDGET_URL, str3);
            intent.putExtra("EXTRA_WIDGET_ID", str4);
            return intent;
        }

        public final String getResultContent(Intent intent) {
            Intrinsics.checkParameterIsNotNull(intent, "intent");
            String stringExtra = intent.getStringExtra(StickerPickerActivity.EXTRA_OUT_CONTENT);
            Intrinsics.checkExpressionValueIsNotNull(stringExtra, "intent.getStringExtra(EXTRA_OUT_CONTENT)");
            return stringExtra;
        }
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public int getLayoutRes() {
        return R.layout.activity_choose_sticker;
    }

    public int getMenuRes() {
        return R.menu.vector_choose_sticker;
    }

    public int getTitleRes() {
        return R.string.title_activity_choose_sticker;
    }

    public NoActionBar getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        String stringExtra = getIntent().getStringExtra(EXTRA_WIDGET_URL);
        Intrinsics.checkExpressionValueIsNotNull(stringExtra, "intent.getStringExtra(EXTRA_WIDGET_URL)");
        this.mWidgetUrl = stringExtra;
        String stringExtra2 = getIntent().getStringExtra("EXTRA_WIDGET_ID");
        Intrinsics.checkExpressionValueIsNotNull(stringExtra2, "intent.getStringExtra(EXTRA_WIDGET_ID)");
        this.mWidgetId = stringExtra2;
        configureToolbar();
        super.initUiAndData();
    }

    public String buildInterfaceUrl(String str) {
        String str2 = "utf-8";
        Intrinsics.checkParameterIsNotNull(str, "scalarToken");
        try {
            StringBuilder sb = new StringBuilder();
            String str3 = this.mWidgetUrl;
            if (str3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mWidgetUrl");
            }
            sb.append(str3);
            sb.append("?");
            sb.append("scalar_token=");
            sb.append(URLEncoder.encode(str, str2));
            sb.append("&room_id=");
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sb.append(URLEncoder.encode(mRoom.getRoomId(), str2));
            sb.append("&widgetId=");
            String str4 = this.mWidgetId;
            if (str4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mWidgetId");
            }
            sb.append(URLEncoder.encode(str4, str2));
            return sb.toString();
        } catch (Exception e) {
            String str5 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## buildInterfaceUrl() failed ");
            sb2.append(e.getMessage());
            Log.e(str5, sb2.toString(), e);
            return null;
        }
    }

    public boolean dealsWithWidgetRequest(Map<String, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        String str = (String) map.get("action");
        if (str == null || str.hashCode() != 1525570748 || !str.equals(Event.EVENT_TYPE_STICKER)) {
            return super.dealsWithWidgetRequest(map);
        }
        sendSticker(map);
        Unit unit = Unit.INSTANCE;
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 13000) {
            getMWebView().reload();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intrinsics.checkParameterIsNotNull(menuItem, "item");
        if (menuItem.getItemId() != R.id.menu_settings) {
            return super.onOptionsItemSelected(menuItem);
        }
        String str = this.mWidgetId;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWidgetId");
        }
        openIntegrationManager(str, "type_m.stickerpicker");
        return true;
    }

    private final void sendSticker(Map<String, ? extends Object> map) {
        Log.d(LOG_TAG, "Received request send sticker");
        Object obj = map.get("data");
        String str = "getString(R.string.widge…ration_missing_parameter)";
        if (obj == null) {
            String string = getString(R.string.widget_integration_missing_parameter);
            Intrinsics.checkExpressionValueIsNotNull(string, str);
            sendError(string, map);
            return;
        }
        Object obj2 = ((Map) obj).get(BingRule.KIND_CONTENT);
        if (obj2 == null) {
            String string2 = getString(R.string.widget_integration_missing_parameter);
            Intrinsics.checkExpressionValueIsNotNull(string2, str);
            sendError(string2, map);
            return;
        }
        String json = JsonUtils.getBasicGson().toJson(obj2);
        sendSuccess(map);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_OUT_CONTENT, json);
        setResult(-1, intent);
        finish();
    }
}
