package im.vector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.RiotEventDisplay;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.rest.model.Event;

public class VectorRoomsSelectionAdapter extends ArrayAdapter<RoomSummary> {
    private static final String LOG_TAG = VectorRoomsSelectionAdapter.class.getSimpleName();
    private final Context mContext;
    private final LayoutInflater mLayoutInflater = LayoutInflater.from(this.mContext);
    private final int mLayoutResourceId;
    private final MXSession mSession;

    public VectorRoomsSelectionAdapter(Context context, int i, MXSession mXSession) {
        super(context, i);
        this.mContext = context;
        this.mLayoutResourceId = i;
        this.mSession = mXSession;
    }

    private String getFormattedTimestamp(Event event) {
        String tsToString = AdapterUtils.tsToString(this.mContext, event.getOriginServerTs(), false);
        StringBuilder sb = new StringBuilder();
        sb.append(this.mContext.getString(R.string.today));
        sb.append(" ");
        String sb2 = sb.toString();
        return tsToString.startsWith(sb2) ? tsToString.substring(sb2.length()) : tsToString;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mLayoutResourceId, viewGroup, false);
        }
        if (!this.mSession.isAlive()) {
            Log.e(LOG_TAG, "getView : the session is not anymore valid");
            return view;
        }
        RoomSummary roomSummary = (RoomSummary) getItem(i);
        ImageView imageView = (ImageView) view.findViewById(R.id.room_avatar);
        TextView textView = (TextView) view.findViewById(R.id.roomSummaryAdapter_roomName);
        TextView textView2 = (TextView) view.findViewById(R.id.roomSummaryAdapter_roomMessage);
        TextView textView3 = (TextView) view.findViewById(R.id.roomSummaryAdapter_ts);
        View findViewById = view.findViewById(R.id.recents_separator);
        Room room = this.mSession.getDataHandler().getRoom(roomSummary.getRoomId());
        if (room != null) {
            VectorUtils.loadRoomAvatar(this.mContext, this.mSession, imageView, room);
        }
        if (roomSummary.getLatestReceivedEvent() != null) {
            RiotEventDisplay riotEventDisplay = new RiotEventDisplay(this.mContext);
            riotEventDisplay.setPrependMessagesWithAuthor(true);
            textView2.setText(riotEventDisplay.getTextualDisplay(Integer.valueOf(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_riot_primary_text_color)), roomSummary.getLatestReceivedEvent(), roomSummary.getLatestRoomState()));
            textView3.setText(getFormattedTimestamp(roomSummary.getLatestReceivedEvent()));
            textView3.setTextColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_default_text_light_color));
            textView3.setTypeface(null, 0);
            textView3.setVisibility(0);
        } else {
            textView2.setText("");
            textView3.setVisibility(8);
        }
        Room room2 = this.mSession.getDataHandler().getRoom(roomSummary.getRoomId());
        if (room2 != null) {
            textView.setText(DinsicUtils.getRoomDisplayName(this.mContext, room2));
        } else {
            textView.setText(null);
        }
        findViewById.setVisibility(0);
        view.findViewById(R.id.bing_indicator_unread_message).setVisibility(4);
        view.findViewById(R.id.recents_groups_separator_line).setVisibility(8);
        view.findViewById(R.id.roomSummaryAdapter_action).setVisibility(8);
        view.findViewById(R.id.roomSummaryAdapter_action_image).setVisibility(8);
        view.findViewById(R.id.recents_groups_invitation_group).setVisibility(8);
        return view;
    }
}
