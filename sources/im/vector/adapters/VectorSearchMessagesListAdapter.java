package im.vector.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.util.RiotEventDisplay;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.Event;

public class VectorSearchMessagesListAdapter extends VectorMessagesAdapter {
    private static final String LOG_TAG = VectorSearchMessagesListAdapter.class.getSimpleName();
    private final boolean mDisplayRoomName;
    private String mPattern;

    /* access modifiers changed from: protected */
    public boolean mergeView(Event event, int i, boolean z) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean supportMessageRowMerge(MessageRow messageRow) {
        return false;
    }

    public VectorSearchMessagesListAdapter(MXSession mXSession, Context context, boolean z, MXMediaCache mXMediaCache) {
        super(mXSession, context, R.layout.adapter_item_vector_message_text_emote_notice, R.layout.adapter_item_vector_message_image_video, R.layout.adapter_item_vector_message_text_emote_notice, R.layout.adapter_item_vector_message_room_member, R.layout.adapter_item_vector_message_text_emote_notice, R.layout.adapter_item_vector_message_file, R.layout.adapter_item_vector_message_merge, R.layout.adapter_item_vector_message_image_video, R.layout.adapter_item_vector_message_emoji, R.layout.adapter_item_vector_message_code, R.layout.adapter_item_vector_message_image_video, R.layout.adapter_item_vector_message_redact, R.layout.adapter_item_tchap_media_scan, R.layout.adapter_item_vector_message_room_versioned, mXMediaCache);
        setNotifyOnChange(true);
        this.mDisplayRoomName = z;
        this.mBackgroundColorSpan = new BackgroundColorSpan(ContextCompat.getColor(this.mContext, R.color.vector_green_color));
    }

    public void setTextToHighlight(String str) {
        this.mPattern = str;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(22:1|2|(1:4)(1:5)|6|(1:8)|9|(1:11)(1:12)|13|(1:15)|16|17|18|19|20|(1:22)(1:23)|24|(2:26|(1:28)(1:29))|30|(1:32)(1:33)|34|(1:36)(1:37)|38) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x00bb */
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view2 = super.getView(i, view, viewGroup);
        try {
            MessageRow messageRow = (MessageRow) getItem(i);
            Event event = messageRow.getEvent();
            int i2 = 8;
            view2.findViewById(R.id.messagesAdapter_avatars_list).setVisibility(8);
            view2.findViewById(R.id.messagesAdapter_message_separator).setVisibility(8);
            view2.findViewById(R.id.messagesAdapter_action_image).setVisibility(8);
            view2.findViewById(R.id.messagesAdapter_top_margin_when_no_room_name).setVisibility(this.mDisplayRoomName ? 8 : 0);
            view2.findViewById(R.id.messagesAdapter_message_header).setVisibility(8);
            Room room = this.mSession.getDataHandler().getStore().getRoom(event.roomId);
            this.mHelper.loadMemberAvatar((ImageView) view2.findViewById(R.id.messagesAdapter_roundAvatar).findViewById(R.id.avatar_img), messageRow);
            TextView textView = (TextView) view2.findViewById(R.id.messagesAdapter_sender);
            if (textView != null) {
                textView.setText(messageRow.getSenderDisplayName());
            }
            TextView textView2 = (TextView) view2.findViewById(R.id.messagesAdapter_body);
            CharSequence textualDisplay = new RiotEventDisplay(this.mContext).getTextualDisplay(event, room != null ? room.getState() : null);
            if (textualDisplay == null) {
                textualDisplay = "";
            }
            textView2.setText(this.mHelper.highlightPattern(new SpannableString(textualDisplay), this.mPattern, this.mBackgroundColorSpan, false));
            this.mHelper.applyLinkMovementMethod(textView2);
            textView2.setText(textualDisplay.toString());
            ((TextView) view2.findViewById(R.id.messagesAdapter_timestamp)).setText(AdapterUtils.tsToString(this.mContext, event.getOriginServerTs(), true));
            view2.findViewById(R.id.messagesAdapter_message_room_name_layout).setVisibility(this.mDisplayRoomName ? 0 : 8);
            if (this.mDisplayRoomName) {
                TextView textView3 = (TextView) view2.findViewById(R.id.messagesAdapter_message_room_name_textview);
                if (room != null) {
                    textView3.setText(DinsicUtils.getRoomDisplayName(this.mContext, room));
                } else {
                    textView3.setText(null);
                }
            }
            View findViewById = view2.findViewById(R.id.messagesAdapter_search_message_day_separator);
            String headerMessage = headerMessage(i);
            if (!TextUtils.isEmpty(headerMessage)) {
                findViewById.setVisibility(0);
                ((TextView) view2.findViewById(R.id.messagesAdapter_message_header_text)).setText(headerMessage);
                findViewById.findViewById(R.id.messagesAdapter_message_header_top_margin).setVisibility(8);
                findViewById.findViewById(R.id.messagesAdapter_message_header_bottom_margin).setVisibility(8);
            } else {
                findViewById.setVisibility(8);
            }
            View findViewById2 = view2.findViewById(R.id.messagesAdapter_search_separator_line);
            if (TextUtils.isEmpty(headerMessage(i + 1))) {
                i2 = 0;
            }
            findViewById2.setVisibility(i2);
            view2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (VectorSearchMessagesListAdapter.this.mVectorMessagesAdapterEventsListener != null) {
                        VectorSearchMessagesListAdapter.this.mVectorMessagesAdapterEventsListener.onContentClick(i);
                    }
                }
            });
            view2.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (VectorSearchMessagesListAdapter.this.mVectorMessagesAdapterEventsListener != null) {
                        return VectorSearchMessagesListAdapter.this.mVectorMessagesAdapterEventsListener.onContentLongClick(i);
                    }
                    return false;
                }
            });
        } catch (Throwable th) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getView() failed ");
            sb.append(th.getMessage());
            Log.e(str, sb.toString(), th);
        }
        return view2;
    }
}
