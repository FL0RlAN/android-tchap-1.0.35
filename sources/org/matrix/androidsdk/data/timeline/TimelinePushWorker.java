package org.matrix.androidsdk.data.timeline;

import com.google.gson.JsonObject;
import kotlin.jvm.internal.LongCompanionObject;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.BingRulesManager;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

class TimelinePushWorker {
    private static final String LOG_TAG = TimelinePushWorker.class.getSimpleName();
    private final MXDataHandler mDataHandler;

    TimelinePushWorker(MXDataHandler mXDataHandler) {
        this.mDataHandler = mXDataHandler;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00f7  */
    public void triggerPush(RoomState roomState, Event event) {
        long j;
        BingRulesManager bingRulesManager;
        BingRule fulfilledBingRule;
        JsonObject contentAsJsonObject = event.getContentAsJsonObject();
        long j2 = 0;
        boolean z = false;
        if (contentAsJsonObject != null) {
            String str = "lifetime";
            if (contentAsJsonObject.has(str)) {
                long asLong = contentAsJsonObject.get(str).getAsLong();
                long currentTimeMillis = System.currentTimeMillis() - event.getOriginServerTs();
                if (currentTimeMillis > asLong) {
                    z = true;
                }
                long j3 = asLong;
                j2 = currentTimeMillis;
                j = j3;
                bingRulesManager = this.mDataHandler.getBingRulesManager();
                String str2 = " in ";
                if (!z && bingRulesManager != null) {
                    fulfilledBingRule = bingRulesManager.fulfilledBingRule(event);
                    if (fulfilledBingRule != null) {
                        String str3 = " event id ";
                        if (fulfilledBingRule.shouldNotify()) {
                            if (Event.EVENT_TYPE_CALL_INVITE.equals(event.getType())) {
                                long age = event.getAge();
                                if (LongCompanionObject.MAX_VALUE == age) {
                                    age = System.currentTimeMillis() - event.getOriginServerTs();
                                }
                                if (age > 120000) {
                                    String str4 = LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("IGNORED onBingEvent rule id ");
                                    sb.append(fulfilledBingRule.ruleId);
                                    sb.append(str3);
                                    sb.append(event.eventId);
                                    sb.append(str2);
                                    sb.append(event.roomId);
                                    Log.d(str4, sb.toString());
                                    return;
                                }
                            }
                            String str5 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onBingEvent rule id ");
                            sb2.append(fulfilledBingRule.ruleId);
                            sb2.append(str3);
                            sb2.append(event.eventId);
                            sb2.append(str2);
                            sb2.append(event.roomId);
                            Log.d(str5, sb2.toString());
                            this.mDataHandler.onBingEvent(event, roomState, fulfilledBingRule);
                        } else {
                            String str6 = LOG_TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("rule id ");
                            sb3.append(fulfilledBingRule.ruleId);
                            sb3.append(str3);
                            sb3.append(event.eventId);
                            sb3.append(str2);
                            sb3.append(event.roomId);
                            sb3.append(" has a mute notify rule");
                            Log.d(str6, sb3.toString());
                        }
                    }
                }
                if (z) {
                    String str7 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("outOfTimeEvent for ");
                    sb4.append(event.eventId);
                    sb4.append(str2);
                    sb4.append(event.roomId);
                    Log.e(str7, sb4.toString());
                    String str8 = LOG_TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("outOfTimeEvent maxlifetime ");
                    sb5.append(j);
                    sb5.append(" eventLifeTime ");
                    sb5.append(j2);
                    Log.e(str8, sb5.toString());
                }
            }
        }
        j = 0;
        bingRulesManager = this.mDataHandler.getBingRulesManager();
        String str22 = " in ";
        fulfilledBingRule = bingRulesManager.fulfilledBingRule(event);
        if (fulfilledBingRule != null) {
        }
        if (z) {
        }
    }
}
