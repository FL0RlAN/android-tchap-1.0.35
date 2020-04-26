package im.vector.widgets;

import android.text.TextUtils;
import java.io.Serializable;
import java.net.URLEncoder;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.rest.model.Event;

public class Widget implements Serializable {
    private String mSessionId;
    private String mUrl;
    private WidgetContent mWidgetContent;
    private Event mWidgetEvent;
    private String mWidgetId;

    public Widget(MXSession mXSession, Event event) throws Exception {
        if (TextUtils.equals(event.type, WidgetsManager.WIDGET_EVENT_TYPE)) {
            this.mWidgetId = event.stateKey;
            this.mWidgetEvent = event;
            this.mSessionId = mXSession.getMyUserId();
            this.mWidgetContent = WidgetContent.toWidgetContent(event.getContentAsJsonObject());
            this.mUrl = this.mWidgetContent.url;
            String str = this.mUrl;
            if (str != null) {
                this.mUrl = str.replace("$matrix_user_id", mXSession.getMyUserId());
                String str2 = mXSession.getMyUser().displayname;
                String str3 = this.mUrl;
                if (str2 == null) {
                    str2 = mXSession.getMyUserId();
                }
                this.mUrl = str3.replace("$matrix_display_name", str2);
                String avatarUrl = mXSession.getMyUser().getAvatarUrl();
                String str4 = this.mUrl;
                if (avatarUrl == null) {
                    avatarUrl = "";
                }
                this.mUrl = str4.replace("$matrix_avatar_url", avatarUrl);
            }
            if (this.mWidgetContent.data != null) {
                for (String str5 : this.mWidgetContent.data.keySet()) {
                    Object obj = this.mWidgetContent.data.get(str5);
                    if (obj instanceof String) {
                        String str6 = this.mUrl;
                        StringBuilder sb = new StringBuilder();
                        sb.append("$");
                        sb.append(str5);
                        this.mUrl = str6.replace(sb.toString(), URLEncoder.encode((String) obj, "utf-8"));
                    }
                }
                return;
            }
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("unsupported event type ");
        sb2.append(event.type);
        throw new Exception(sb2.toString());
    }

    public boolean isActive() {
        return (this.mWidgetContent.type == null || this.mUrl == null) ? false : true;
    }

    public String getWidgetId() {
        return this.mWidgetId;
    }

    public Event getWidgetEvent() {
        return this.mWidgetEvent;
    }

    public String getSessionId() {
        return this.mSessionId;
    }

    public String getRoomId() {
        return this.mWidgetEvent.roomId;
    }

    private String getType() {
        return this.mWidgetContent.type;
    }

    public String getUrl() {
        return this.mUrl;
    }

    private String getName() {
        return this.mWidgetContent.name;
    }

    public String getHumanName() {
        return this.mWidgetContent.getHumanName();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Widget: ");
        sb.append(this);
        sb.append("p> id: ");
        sb.append(getWidgetId());
        sb.append(" - type: ");
        sb.append(getType());
        sb.append(" - name: ");
        sb.append(getName());
        sb.append(" - url: ");
        sb.append(getUrl());
        return sb.toString();
    }
}
