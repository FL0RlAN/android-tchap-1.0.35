package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import androidx.core.app.NotificationCompat;
import fr.gouv.tchap.a.R;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.util.JsonUtilKt;
import im.vector.widgets.Widget;
import im.vector.widgets.WidgetsManager;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 !2\u00020\u0001:\u0001!B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0016J&\u0010\b\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u000e\u001a\u00020\u000f2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u0010\u001a\u00020\u000f2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u0011\u001a\u00020\u000f2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0016J&\u0010\u0012\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u0013\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0016J&\u0010\u0016\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u0017\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0016J&\u0010\u001a\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J\b\u0010\u001b\u001a\u00020\tH\u0017J&\u0010\u001c\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u001d\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u001e\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010\u001f\u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002J&\u0010 \u001a\u00020\t2\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lim/vector/activity/IntegrationManagerActivity;", "Lim/vector/activity/AbstractWidgetActivity;", "()V", "mScreenId", "", "mWidgetId", "buildInterfaceUrl", "scalarToken", "canSendEvent", "", "eventData", "", "", "Lim/vector/types/JsonDict;", "checkRoomId", "", "checkUserId", "dealsWithWidgetRequest", "getBotOptions", "getJoinRules", "getLayoutRes", "", "getMembershipCount", "getMembershipState", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "getWidgets", "initUiAndData", "inviteUser", "setBotOptions", "setBotPower", "setPlumbingState", "setWidget", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: IntegrationManagerActivity.kt */
public final class IntegrationManagerActivity extends AbstractWidgetActivity {
    public static final Companion Companion = new Companion(null);
    public static final String EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID";
    public static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    private static final String EXTRA_SCREEN_ID = "EXTRA_SCREEN_ID";
    public static final String EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = IntegrationManagerActivity.class.getSimpleName();
    private HashMap _$_findViewCache;
    private String mScreenId;
    private String mWidgetId;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J6\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00042\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \t*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lim/vector/activity/IntegrationManagerActivity$Companion;", "", "()V", "EXTRA_MATRIX_ID", "", "EXTRA_ROOM_ID", "EXTRA_SCREEN_ID", "EXTRA_WIDGET_ID", "LOG_TAG", "kotlin.jvm.PlatformType", "getIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "matrixId", "roomId", "widgetId", "screenId", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: IntegrationManagerActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ Intent getIntent$default(Companion companion, Context context, String str, String str2, String str3, String str4, int i, Object obj) {
            if ((i & 8) != 0) {
                str3 = null;
            }
            String str5 = str3;
            if ((i & 16) != 0) {
                str4 = null;
            }
            return companion.getIntent(context, str, str2, str5, str4);
        }

        public final Intent getIntent(Context context, String str, String str2, String str3, String str4) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, "matrixId");
            Intrinsics.checkParameterIsNotNull(str2, CryptoRoomEntityFields.ROOM_ID);
            Intent intent = new Intent(context, IntegrationManagerActivity.class);
            intent.putExtra("EXTRA_MATRIX_ID", str);
            intent.putExtra("EXTRA_ROOM_ID", str2);
            intent.putExtra("EXTRA_WIDGET_ID", str3);
            intent.putExtra(IntegrationManagerActivity.EXTRA_SCREEN_ID, str4);
            return intent;
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
        return R.layout.activity_integration_manager;
    }

    public NoActionBar getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        this.mWidgetId = getIntent().getStringExtra("EXTRA_WIDGET_ID");
        this.mScreenId = getIntent().getStringExtra(EXTRA_SCREEN_ID);
        setWaitingView(findViewById(R.id.integration_progress_layout));
        showWaitingView();
        super.initUiAndData();
    }

    public String buildInterfaceUrl(String str) {
        String str2 = "utf-8";
        Intrinsics.checkParameterIsNotNull(str, "scalarToken");
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.integrations_ui_url));
            sb.append("?");
            sb.append("scalar_token=");
            sb.append(URLEncoder.encode(str, str2));
            sb.append("&");
            sb.append("room_id=");
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sb.append(URLEncoder.encode(mRoom.getRoomId(), str2));
            String sb2 = sb.toString();
            if (this.mWidgetId != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append("&integ_id=");
                sb3.append(URLEncoder.encode(this.mWidgetId, str2));
                sb2 = sb3.toString();
            }
            if (this.mScreenId != null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(sb2);
                sb4.append("&screen=");
                sb4.append(URLEncoder.encode(this.mScreenId, str2));
                sb2 = sb4.toString();
            }
            return sb2;
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("## buildInterfaceUrl() failed ");
            sb5.append(e.getMessage());
            Log.e(str3, sb5.toString(), e);
            return null;
        }
    }

    public boolean dealsWithWidgetRequest(Map<String, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        String str = (String) map.get("action");
        if (str != null) {
            switch (str.hashCode()) {
                case -1330055448:
                    if (str.equals("membership_state")) {
                        getMembershipState(map);
                        Unit unit = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case -1209460717:
                    if (str.equals("close_scalar")) {
                        finish();
                        Unit unit2 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case -1183699191:
                    if (str.equals("invite")) {
                        inviteUser(map);
                        Unit unit3 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case -1166615312:
                    if (str.equals("set_bot_power")) {
                        setBotPower(map);
                        Unit unit4 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case -1065531199:
                    if (str.equals("set_widget")) {
                        setWidget(map);
                        Unit unit5 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case -992375863:
                    if (str.equals("set_bot_options")) {
                        setBotOptions(map);
                        Unit unit6 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case -248847901:
                    if (str.equals("set_plumbing_state")) {
                        setPlumbingState(map);
                        Unit unit7 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case 122629423:
                    if (str.equals("get_membership_count")) {
                        getMembershipCount(map);
                        Unit unit8 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case 1072392116:
                    if (str.equals("join_rules_state")) {
                        getJoinRules(map);
                        Unit unit9 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case 1416851142:
                    if (str.equals("get_widgets")) {
                        getWidgets(map);
                        Unit unit10 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case 1551692838:
                    if (str.equals("bot_options")) {
                        getBotOptions(map);
                        Unit unit11 = Unit.INSTANCE;
                        return true;
                    }
                    break;
                case 2016326386:
                    if (str.equals("can_send_event")) {
                        canSendEvent(map);
                        Unit unit12 = Unit.INSTANCE;
                        return true;
                    }
                    break;
            }
        }
        return super.dealsWithWidgetRequest(map);
    }

    private final void inviteUser(Map<String, ? extends Object> map) {
        if (!checkRoomId(map) && !checkUserId(map)) {
            Object obj = map.get("user_id");
            if (obj != null) {
                String str = (String) obj;
                StringBuilder sb = new StringBuilder();
                sb.append("Received request to invite ");
                sb.append(str);
                sb.append(" into room ");
                Room mRoom = getMRoom();
                if (mRoom == null) {
                    Intrinsics.throwNpe();
                }
                sb.append(mRoom.getRoomId());
                String sb2 = sb.toString();
                Log.d(LOG_TAG, sb2);
                Room mRoom2 = getMRoom();
                if (mRoom2 == null) {
                    Intrinsics.throwNpe();
                }
                RoomMember member = mRoom2.getMember(str);
                if (member == null || !TextUtils.equals(member.membership, "join")) {
                    Room mRoom3 = getMRoom();
                    if (mRoom3 == null) {
                        Intrinsics.throwNpe();
                    }
                    mRoom3.invite(str, (ApiCallback<Void>) new WidgetApiCallback<Void>(this, map, sb2));
                } else {
                    sendSuccess(map);
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
    }

    private final void setWidget(Map<String, ? extends Object> map) {
        Boolean bool = (Boolean) map.get("userWidget");
        Boolean valueOf = Boolean.valueOf(true);
        if (Intrinsics.areEqual((Object) bool, (Object) valueOf)) {
            Log.d(LOG_TAG, "Received request to set widget for user");
        } else if (!checkRoomId(map)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Received request to set widget in room ");
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sb.append(mRoom.getRoomId());
            Log.d(str, sb.toString());
        } else {
            return;
        }
        String str2 = (String) map.get("widget_id");
        String str3 = PasswordLoginParams.IDENTIFIER_KEY_TYPE;
        String str4 = (String) map.get(str3);
        String str5 = "url";
        String str6 = (String) map.get(str5);
        String str7 = TermsResponse.NAME;
        String str8 = (String) map.get(str7);
        String str9 = "data";
        Map map2 = (Map) map.get(str9);
        String str10 = "getString(R.string.widge…gration_unable_to_create)";
        if (str2 == null) {
            String string = getString(R.string.widget_integration_unable_to_create);
            Intrinsics.checkExpressionValueIsNotNull(string, str10);
            sendError(string, map);
            return;
        }
        HashMap hashMap = new HashMap();
        if (str6 != null) {
            if (str4 == null) {
                String string2 = getString(R.string.widget_integration_unable_to_create);
                Intrinsics.checkExpressionValueIsNotNull(string2, str10);
                sendError(string2, map);
                return;
            }
            Map map3 = hashMap;
            map3.put(str3, str4);
            map3.put(str5, str6);
            if (str8 != null) {
                map3.put(str7, str8);
            }
            if (map2 != null) {
                map3.put(str9, map2);
            }
        }
        boolean areEqual = Intrinsics.areEqual((Object) bool, (Object) valueOf);
        String str11 = "## setWidget()";
        if (areEqual) {
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
            hashMap3.put(BingRule.KIND_CONTENT, hashMap);
            hashMap3.put("state_key", str2);
            hashMap3.put("id", str2);
            MXSession mSession = getMSession();
            if (mSession == null) {
                Intrinsics.throwNpe();
            }
            hashMap3.put(BingRule.KIND_SENDER, mSession.getMyUserId());
            hashMap3.put(str3, "m.widget");
            hashMap2.put(str2, hashMap3);
            MXSession mSession2 = getMSession();
            if (mSession2 == null) {
                Intrinsics.throwNpe();
            }
            mSession2.addUserWidget(hashMap2, new WidgetApiCallback(this, map, str11));
        } else {
            MXSession mSession3 = getMSession();
            if (mSession3 == null) {
                Intrinsics.throwNpe();
            }
            RoomsRestClient roomsApiClient = mSession3.getRoomsApiClient();
            Room mRoom2 = getMRoom();
            if (mRoom2 == null) {
                Intrinsics.throwNpe();
            }
            roomsApiClient.sendStateEvent(mRoom2.getRoomId(), WidgetsManager.WIDGET_EVENT_TYPE, str2, hashMap, new WidgetApiCallback(this, map, str11));
        }
    }

    private final void getWidgets(Map<String, ? extends Object> map) {
        if (!checkRoomId(map)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Received request to get widget in room ");
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sb.append(mRoom.getRoomId());
            Log.d(str, sb.toString());
            List<Widget> activeWidgets = WidgetsManager.getSharedInstance().getActiveWidgets(getMSession(), getMRoom());
            ArrayList arrayList = new ArrayList();
            for (Widget widget : activeWidgets) {
                Intrinsics.checkExpressionValueIsNotNull(widget, "widget");
                Event widgetEvent = widget.getWidgetEvent();
                Intrinsics.checkExpressionValueIsNotNull(widgetEvent, "widget.widgetEvent");
                Map jsonMap = JsonUtilKt.toJsonMap(widgetEvent);
                if (jsonMap != null) {
                    arrayList.add(jsonMap);
                }
            }
            MXSession mSession = getMSession();
            if (mSession == null) {
                Intrinsics.throwNpe();
            }
            Map userWidgets = mSession.getUserWidgets();
            Intrinsics.checkExpressionValueIsNotNull(userWidgets, "mSession!!.userWidgets");
            for (Entry value : userWidgets.entrySet()) {
                Object value2 = value.getValue();
                if (value2 != null) {
                    arrayList.add((Map) value2);
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type im.vector.types.JsonDict<kotlin.Any> /* = kotlin.collections.Map<kotlin.String, kotlin.Any> */");
                }
            }
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## getWidgets() returns ");
            sb2.append(arrayList);
            Log.d(str2, sb2.toString());
            sendObjectResponse(arrayList, map);
        }
    }

    private final void canSendEvent(Map<String, ? extends Object> map) {
        if (!checkRoomId(map)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Received request canSendEvent in room ");
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sb.append(mRoom.getRoomId());
            Log.d(str, sb.toString());
            Room mRoom2 = getMRoom();
            if (mRoom2 == null) {
                Intrinsics.throwNpe();
            }
            if (!mRoom2.isJoined()) {
                String string = getString(R.string.widget_integration_must_be_in_room);
                Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…egration_must_be_in_room)");
                sendError(string, map);
                return;
            }
            Object obj = map.get("event_type");
            if (obj != null) {
                String str2 = (String) obj;
                Object obj2 = map.get("is_state");
                if (obj2 != null) {
                    boolean booleanValue = ((Boolean) obj2).booleanValue();
                    String str3 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## canSendEvent() : eventType ");
                    sb2.append(str2);
                    sb2.append(" isState ");
                    sb2.append(booleanValue);
                    Log.d(str3, sb2.toString());
                    Room mRoom3 = getMRoom();
                    if (mRoom3 == null) {
                        Intrinsics.throwNpe();
                    }
                    RoomState state = mRoom3.getState();
                    Intrinsics.checkExpressionValueIsNotNull(state, "mRoom!!.state");
                    PowerLevels powerLevels = state.getPowerLevels();
                    if (powerLevels == null) {
                        Intrinsics.throwNpe();
                    }
                    MXSession mSession = getMSession();
                    if (mSession == null) {
                        Intrinsics.throwNpe();
                    }
                    int userPowerLevel = powerLevels.getUserPowerLevel(mSession.getMyUserId());
                    boolean z = false;
                    if (!booleanValue ? userPowerLevel >= powerLevels.minimumPowerLevelForSendingEventAsMessage(str2) : userPowerLevel >= powerLevels.minimumPowerLevelForSendingEventAsStateEvent(str2)) {
                        z = true;
                    }
                    if (z) {
                        Log.d(LOG_TAG, "## canSendEvent() returns true");
                        sendBoolResponse(true, map);
                    } else {
                        Log.d(LOG_TAG, "## canSendEvent() returns widget_integration_no_permission_in_room");
                        String string2 = getString(R.string.widget_integration_no_permission_in_room);
                        Intrinsics.checkExpressionValueIsNotNull(string2, "getString(R.string.widge…on_no_permission_in_room)");
                        sendError(string2, map);
                    }
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
    }

    private final void getMembershipState(Map<String, ? extends Object> map) {
        if (!checkRoomId(map) && !checkUserId(map)) {
            Object obj = map.get("user_id");
            if (obj != null) {
                String str = (String) obj;
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("membership_state of ");
                sb.append(str);
                sb.append(" in room ");
                Room mRoom = getMRoom();
                if (mRoom == null) {
                    Intrinsics.throwNpe();
                }
                sb.append(mRoom.getRoomId());
                sb.append(" requested");
                Log.d(str2, sb.toString());
                Room mRoom2 = getMRoom();
                if (mRoom2 == null) {
                    Intrinsics.throwNpe();
                }
                mRoom2.getMemberEvent(str, new IntegrationManagerActivity$getMembershipState$1(this, str, map));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
    }

    private final void getJoinRules(Map<String, ? extends Object> map) {
        if (!checkRoomId(map)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Received request join rules  in room ");
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sb.append(mRoom.getRoomId());
            Log.d(str, sb.toString());
            Room mRoom2 = getMRoom();
            if (mRoom2 == null) {
                Intrinsics.throwNpe();
            }
            List stateEvents = mRoom2.getState().getStateEvents(new HashSet(Arrays.asList(new String[]{Event.EVENT_TYPE_STATE_ROOM_JOIN_RULES})));
            if (stateEvents.size() > 0) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Received request join rules returns ");
                sb2.append((Event) stateEvents.get(stateEvents.size() - 1));
                Log.d(str2, sb2.toString());
                Object obj = stateEvents.get(stateEvents.size() - 1);
                Intrinsics.checkExpressionValueIsNotNull(obj, "joinedEvents[joinedEvents.size - 1]");
                sendObjectAsJsonMap(obj, map);
            } else {
                Log.e(LOG_TAG, "Received request join rules failed widget_integration_failed_to_send_request");
                String string = getString(R.string.widget_integration_failed_to_send_request);
                Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…n_failed_to_send_request)");
                sendError(string, map);
            }
        }
    }

    private final void setPlumbingState(Map<String, ? extends Object> map) {
        if (!checkRoomId(map)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Received request to set plumbing state to status ");
            String str = NotificationCompat.CATEGORY_STATUS;
            sb.append(map.get(str));
            sb.append(" in room ");
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sb.append(mRoom.getRoomId());
            sb.append(" requested");
            String sb2 = sb.toString();
            Log.d(LOG_TAG, sb2);
            Object obj = map.get(str);
            if (obj != null) {
                String str2 = (String) obj;
                Map hashMap = new HashMap();
                hashMap.put(str, str2);
                MXSession mSession = getMSession();
                if (mSession == null) {
                    Intrinsics.throwNpe();
                }
                RoomsRestClient roomsApiClient = mSession.getRoomsApiClient();
                Room mRoom2 = getMRoom();
                if (mRoom2 == null) {
                    Intrinsics.throwNpe();
                }
                roomsApiClient.sendStateEvent(mRoom2.getRoomId(), Event.EVENT_TYPE_ROOM_PLUMBING, null, hashMap, new WidgetApiCallback(this, map, sb2));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
    }

    private final void getBotOptions(Map<String, ? extends Object> map) {
        if (!checkRoomId(map) && !checkUserId(map)) {
            Object obj = map.get("user_id");
            if (obj != null) {
                String str = (String) obj;
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                String str3 = "Received request to get options for bot ";
                sb.append(str3);
                sb.append(str);
                sb.append(" in room ");
                Room mRoom = getMRoom();
                if (mRoom == null) {
                    Intrinsics.throwNpe();
                }
                sb.append(mRoom.getRoomId());
                sb.append(" requested");
                Log.d(str2, sb.toString());
                Room mRoom2 = getMRoom();
                if (mRoom2 == null) {
                    Intrinsics.throwNpe();
                }
                List<Event> stateEvents = mRoom2.getState().getStateEvents(new HashSet(Arrays.asList(new String[]{Event.EVENT_TYPE_ROOM_BOT_OPTIONS})));
                Event event = null;
                StringBuilder sb2 = new StringBuilder();
                sb2.append('_');
                sb2.append(str);
                String sb3 = sb2.toString();
                for (Event event2 : stateEvents) {
                    if (TextUtils.equals(event2.stateKey, sb3) && (event == null || event2.getAge() > event.getAge())) {
                        event = event2;
                    }
                }
                if (event != null) {
                    String str4 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str3);
                    sb4.append(str);
                    sb4.append(" returns ");
                    sb4.append(event);
                    Log.d(str4, sb4.toString());
                    sendObjectAsJsonMap(event, map);
                } else {
                    String str5 = LOG_TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str3);
                    sb5.append(str);
                    sb5.append(" returns null");
                    Log.d(str5, sb5.toString());
                    sendObjectResponse(null, map);
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
    }

    private final void setBotOptions(Map<String, ? extends Object> map) {
        if (!checkRoomId(map) && !checkUserId(map)) {
            Object obj = map.get("user_id");
            if (obj != null) {
                String str = (String) obj;
                StringBuilder sb = new StringBuilder();
                sb.append("Received request to set options for bot ");
                sb.append(str);
                sb.append(" in room ");
                Room mRoom = getMRoom();
                if (mRoom == null) {
                    Intrinsics.throwNpe();
                }
                sb.append(mRoom.getRoomId());
                String sb2 = sb.toString();
                Log.d(LOG_TAG, sb2);
                Object obj2 = map.get(BingRule.KIND_CONTENT);
                if (obj2 != null) {
                    Map map2 = (Map) obj2;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append('_');
                    sb3.append(str);
                    String sb4 = sb3.toString();
                    MXSession mSession = getMSession();
                    if (mSession == null) {
                        Intrinsics.throwNpe();
                    }
                    RoomsRestClient roomsApiClient = mSession.getRoomsApiClient();
                    Room mRoom2 = getMRoom();
                    if (mRoom2 == null) {
                        Intrinsics.throwNpe();
                    }
                    roomsApiClient.sendStateEvent(mRoom2.getRoomId(), Event.EVENT_TYPE_ROOM_BOT_OPTIONS, sb4, map2, new WidgetApiCallback(this, map, sb2));
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type im.vector.types.JsonDict<kotlin.Any> /* = kotlin.collections.Map<kotlin.String, kotlin.Any> */");
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
    }

    private final void setBotPower(Map<String, ? extends Object> map) {
        if (!checkRoomId(map) && !checkUserId(map)) {
            Object obj = map.get("user_id");
            if (obj != null) {
                String str = (String) obj;
                StringBuilder sb = new StringBuilder();
                sb.append("Received request to set power level to ");
                String str2 = "level";
                sb.append(map.get(str2));
                sb.append(" for bot ");
                sb.append(str);
                sb.append(" in room ");
                Room mRoom = getMRoom();
                if (mRoom == null) {
                    Intrinsics.throwNpe();
                }
                sb.append(mRoom.getRoomId());
                String sb2 = sb.toString();
                Log.d(LOG_TAG, sb2);
                Object obj2 = map.get(str2);
                if (obj2 != null) {
                    int intValue = ((Integer) obj2).intValue();
                    if (intValue >= 0) {
                        Room mRoom2 = getMRoom();
                        if (mRoom2 == null) {
                            Intrinsics.throwNpe();
                        }
                        mRoom2.updateUserPowerLevels(str, intValue, new WidgetApiCallback(this, map, sb2));
                    } else {
                        Log.e(LOG_TAG, "## setBotPower() : Power level must be positive integer.");
                        String string = getString(R.string.widget_integration_positive_power_level);
                        Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…ion_positive_power_level)");
                        sendError(string, map);
                    }
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Int");
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
    }

    private final void getMembershipCount(Map<String, ? extends Object> map) {
        if (!checkRoomId(map)) {
            Room mRoom = getMRoom();
            if (mRoom == null) {
                Intrinsics.throwNpe();
            }
            sendIntegerResponse(mRoom.getNumberOfJoinedMembers(), map);
        }
    }

    private final boolean checkRoomId(Map<String, ? extends Object> map) {
        String str = (String) map.get("room_id");
        if (str == null) {
            String string = getString(R.string.widget_integration_missing_room_id);
            Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…egration_missing_room_id)");
            sendError(string, map);
            return true;
        }
        CharSequence charSequence = str;
        Room mRoom = getMRoom();
        if (mRoom == null) {
            Intrinsics.throwNpe();
        }
        if (TextUtils.equals(charSequence, mRoom.getRoomId())) {
            return false;
        }
        String string2 = getString(R.string.widget_integration_room_not_visible);
        Intrinsics.checkExpressionValueIsNotNull(string2, "getString(R.string.widge…gration_room_not_visible)");
        sendError(string2, map);
        return true;
    }

    private final boolean checkUserId(Map<String, ? extends Object> map) {
        if (((String) map.get("user_id")) != null) {
            return false;
        }
        String string = getString(R.string.widget_integration_missing_user_id);
        Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…egration_missing_user_id)");
        sendError(string, map);
        return true;
    }
}
