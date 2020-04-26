package im.vector.util;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.JsonObject;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.widgets.WidgetContent;
import im.vector.widgets.WidgetsManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.matrix.androidsdk.core.EventDisplay;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.interfaces.HtmlToolbox;
import org.matrix.androidsdk.rest.model.Event;

public class RiotEventDisplay extends EventDisplay {
    private static final String LOG_TAG = RiotEventDisplay.class.getSimpleName();
    private static final Map<String, Event> mClosingWidgetEventByStateKey = new HashMap();

    public RiotEventDisplay(Context context, HtmlToolbox htmlToolbox) {
        super(context, htmlToolbox);
    }

    public RiotEventDisplay(Context context) {
        super(context);
    }

    public CharSequence getTextualDisplay(Integer num, Event event, RoomState roomState) {
        CharSequence charSequence;
        String str = WidgetsManager.WIDGET_EVENT_TYPE;
        CharSequence charSequence2 = null;
        try {
            if (TextUtils.equals(event.getType(), str)) {
                JsonObject contentAsJsonObject = event.getContentAsJsonObject();
                String senderDisplayNameForEvent = senderDisplayNameForEvent(event, JsonUtils.toEventContent(event.getContentAsJsonObject()), event.getPrevContent(), roomState);
                if (contentAsJsonObject.entrySet().size() == 0) {
                    Event event2 = (Event) mClosingWidgetEventByStateKey.get(event.stateKey);
                    if (event2 == null) {
                        Iterator it = roomState.getStateEvents(new HashSet(Arrays.asList(new String[]{str}))).iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            Event event3 = (Event) it.next();
                            if (TextUtils.equals(event3.stateKey, event.stateKey) && !event3.getContentAsJsonObject().entrySet().isEmpty()) {
                                event2 = event3;
                                break;
                            }
                        }
                        if (event2 != null) {
                            mClosingWidgetEventByStateKey.put(event.stateKey, event2);
                        }
                    }
                    charSequence = this.mContext.getString(R.string.event_formatter_widget_removed, new Object[]{event2 != null ? WidgetContent.toWidgetContent(event2.getContentAsJsonObject()).getHumanName() : "undefined", senderDisplayNameForEvent});
                } else {
                    charSequence = this.mContext.getString(R.string.event_formatter_widget_added, new Object[]{WidgetContent.toWidgetContent(event.getContentAsJsonObject()).getHumanName(), senderDisplayNameForEvent});
                }
            } else {
                charSequence = super.getTextualDisplay(num, event, roomState);
            }
            charSequence2 = charSequence;
            if (event.getCryptoError() != null) {
                VectorApp.getInstance().getDecryptionFailureTracker().reportUnableToDecryptError(event, roomState, Matrix.getInstance(this.mContext).getDefaultSession().getMyUserId());
            }
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getTextualDisplay() ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
        }
        return charSequence2;
    }
}
