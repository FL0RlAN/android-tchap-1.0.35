package org.matrix.androidsdk.adapters;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import org.matrix.androidsdk.core.EventDisplay;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomCreateContent.Predecessor;
import org.matrix.androidsdk.rest.model.RoomMember;

public class MessageRow {
    private Event mEvent;
    private Predecessor mRoomCreateContentPredecessor;
    private final RoomState mRoomState;
    private final RoomMember mSender;
    private final String mSenderDisplayName;
    private SpannableString mText;

    public MessageRow(Event event, RoomState roomState) {
        this.mEvent = event;
        this.mRoomState = roomState;
        if (roomState == null) {
            this.mSenderDisplayName = event.getSender();
            this.mRoomCreateContentPredecessor = null;
            this.mSender = null;
            return;
        }
        this.mSenderDisplayName = roomState.getMemberName(event.getSender());
        if (roomState.getRoomCreateContent() != null) {
            this.mRoomCreateContentPredecessor = roomState.getRoomCreateContent().predecessor;
        }
        this.mSender = roomState.getMember(event.getSender());
    }

    public Event getEvent() {
        return this.mEvent;
    }

    public void updateEvent(Event event) {
        this.mEvent = event;
        this.mText = null;
    }

    public Spannable getText(ParagraphStyle paragraphStyle, EventDisplay eventDisplay) {
        if (this.mText == null) {
            CharSequence textualDisplay = eventDisplay.getTextualDisplay(this.mEvent, this.mRoomState);
            if (textualDisplay == null) {
                textualDisplay = "";
            }
            this.mText = new SpannableString(textualDisplay);
            replaceQuoteSpans(this.mText, paragraphStyle);
        }
        return this.mText;
    }

    public String getSenderDisplayName() {
        return this.mSenderDisplayName;
    }

    public Predecessor getRoomCreateContentPredecessor() {
        return this.mRoomCreateContentPredecessor;
    }

    public RoomMember getSender() {
        return this.mSender;
    }

    private void replaceQuoteSpans(Spannable spannable, ParagraphStyle paragraphStyle) {
        QuoteSpan[] quoteSpanArr;
        for (QuoteSpan quoteSpan : (QuoteSpan[]) spannable.getSpans(0, spannable.length(), QuoteSpan.class)) {
            int spanStart = spannable.getSpanStart(quoteSpan);
            int spanEnd = spannable.getSpanEnd(quoteSpan);
            int spanFlags = spannable.getSpanFlags(quoteSpan);
            spannable.removeSpan(quoteSpan);
            spannable.setSpan(paragraphStyle, spanStart, spanEnd, spanFlags);
        }
    }
}
