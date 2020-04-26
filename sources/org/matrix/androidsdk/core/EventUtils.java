package org.matrix.androidsdk.core;

import android.text.TextUtils;
import java.util.regex.Pattern;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomDirectoryVisibility;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public class EventUtils {
    private static final String LOG_TAG = EventUtils.class.getSimpleName();

    public static boolean shouldHighlight(MXSession mXSession, Event event) {
        boolean z = false;
        if (!(mXSession == null || event == null)) {
            BingRule fulfillRule = mXSession.fulfillRule(event);
            if (fulfillRule != null) {
                z = fulfillRule.shouldHighlight();
                if (z) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## shouldHighlight() : the event ");
                    sb.append(event.roomId);
                    sb.append("/");
                    sb.append(event.eventId);
                    sb.append(" is higlighted by ");
                    sb.append(fulfillRule);
                    Log.d(str, sb.toString());
                }
            }
        }
        return z;
    }

    public static boolean shouldNotify(MXSession mXSession, Event event, String str) {
        boolean z = false;
        if (event == null || mXSession == null) {
            Log.e(LOG_TAG, "shouldNotify invalid params");
            return false;
        }
        String str2 = "shouldNotify null room ID";
        if (event.roomId == null) {
            Log.e(LOG_TAG, str2);
            return false;
        } else if (event.getSender() == null) {
            Log.e(LOG_TAG, str2);
            return false;
        } else if (TextUtils.equals(event.roomId, str)) {
            return false;
        } else {
            if (shouldHighlight(mXSession, event)) {
                return true;
            }
            if (RoomDirectoryVisibility.DIRECTORY_VISIBILITY_PRIVATE.equals(mXSession.getDataHandler().getRoom(event.roomId).getVisibility()) && !TextUtils.equals(event.getSender(), mXSession.getCredentials().userId)) {
                z = true;
            }
            return z;
        }
    }

    public static boolean caseInsensitiveFind(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("(\\W|^)");
            sb.append(Pattern.quote(str));
            sb.append("(\\W|$)");
            return Pattern.compile(sb.toString(), 2).matcher(str2).find();
        } catch (Exception e) {
            Log.e(LOG_TAG, "## caseInsensitiveFind() : failed", e);
            return false;
        }
    }
}
