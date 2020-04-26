package org.matrix.androidsdk.core;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.matrix.androidsdk.R;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.interfaces.HtmlToolbox;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContent;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

public class EventDisplay {
    private static final String LOG_TAG = EventDisplay.class.getSimpleName();
    private static final String MESSAGE_IN_REPLY_TO_FIRST_PART = "<blockquote>";
    private static final String MESSAGE_IN_REPLY_TO_HREF_TAG_END = "\">";
    private static final String MESSAGE_IN_REPLY_TO_LAST_PART = "</a>";
    public static final boolean mDisplayRedactedEvents = false;
    protected final Context mContext;
    protected final HtmlToolbox mHtmlToolbox;
    protected boolean mPrependAuthor;

    public static String getRedactionMessage(Context context, Event event, RoomState roomState) {
        return null;
    }

    public EventDisplay(Context context) {
        this(context, null);
    }

    public EventDisplay(Context context, HtmlToolbox htmlToolbox) {
        this.mContext = context.getApplicationContext();
        this.mHtmlToolbox = htmlToolbox;
    }

    public void setPrependMessagesWithAuthor(boolean z) {
        this.mPrependAuthor = z;
    }

    protected static String getUserDisplayName(String str, RoomState roomState) {
        return roomState != null ? roomState.getMemberName(str) : str;
    }

    public CharSequence getTextualDisplay(Event event, RoomState roomState) {
        return getTextualDisplay(null, event, roomState);
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r2v3, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v24, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v25, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v33, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v34, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v44, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v45, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r13v13, types: [android.text.SpannableString] */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v5, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r2v6, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v70, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r13v23 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: type inference failed for: r12v71 */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r1v6, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r13v25, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r2v11, types: [java.lang.CharSequence, java.lang.Object] */
    /* JADX WARNING: type inference failed for: r13v29, types: [android.text.SpannableStringBuilder] */
    /* JADX WARNING: type inference failed for: r1v7, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r14v20 */
    /* JADX WARNING: type inference failed for: r2v12 */
    /* JADX WARNING: type inference failed for: r12v76 */
    /* JADX WARNING: type inference failed for: r2v13 */
    /* JADX WARNING: type inference failed for: r12v79, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r12v81, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r14v25, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r2v14 */
    /* JADX WARNING: type inference failed for: r0v23, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v15 */
    /* JADX WARNING: type inference failed for: r13v33 */
    /* JADX WARNING: type inference failed for: r13v35, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v16, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v17, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v18 */
    /* JADX WARNING: type inference failed for: r2v19 */
    /* JADX WARNING: type inference failed for: r2v20 */
    /* JADX WARNING: type inference failed for: r12v120 */
    /* JADX WARNING: type inference failed for: r12v121 */
    /* JADX WARNING: type inference failed for: r12v122 */
    /* JADX WARNING: type inference failed for: r12v123 */
    /* JADX WARNING: type inference failed for: r12v124 */
    /* JADX WARNING: type inference failed for: r12v125 */
    /* JADX WARNING: type inference failed for: r13v57 */
    /* JADX WARNING: type inference failed for: r2v21 */
    /* JADX WARNING: type inference failed for: r2v22 */
    /* JADX WARNING: type inference failed for: r12v126 */
    /* JADX WARNING: type inference failed for: r12v127 */
    /* JADX WARNING: type inference failed for: r13v58 */
    /* JADX WARNING: type inference failed for: r2v23 */
    /* JADX WARNING: type inference failed for: r2v24 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v2
  assigns: []
  uses: []
  mth insns count: 394
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 27 */
    public CharSequence getTextualDisplay(Integer num, Event event, RoomState roomState) {
        ? r2;
        String str;
        String str2;
        String str3;
        ? r13;
        ? r12;
        String str4;
        boolean z;
        String str5 = "msgtype";
        String str6 = "history_visibility";
        ? r22 = 0;
        try {
            JsonObject contentAsJsonObject = event.getContentAsJsonObject();
            String userDisplayName = getUserDisplayName(event.getSender(), roomState);
            String type = event.getType();
            if (event.isCallEvent()) {
                if (Event.EVENT_TYPE_CALL_INVITE.equals(type)) {
                    try {
                        z = contentAsJsonObject.get("offer").getAsJsonObject().get("sdp").getAsString().contains("m=video");
                    } catch (Exception e) {
                        String str7 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("getTextualDisplay : ");
                        sb.append(e.getMessage());
                        Log.e(str7, sb.toString(), e);
                        z = false;
                    }
                    if (z) {
                        return this.mContext.getString(R.string.notice_placed_video_call, new Object[]{userDisplayName});
                    }
                    return this.mContext.getString(R.string.notice_placed_voice_call, new Object[]{userDisplayName});
                } else if (Event.EVENT_TYPE_CALL_ANSWER.equals(type)) {
                    return this.mContext.getString(R.string.notice_answered_call, new Object[]{userDisplayName});
                } else if (!Event.EVENT_TYPE_CALL_HANGUP.equals(type)) {
                    return type;
                } else {
                    return this.mContext.getString(R.string.notice_ended_call, new Object[]{userDisplayName});
                }
            } else if (Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY.equals(type)) {
                JsonElement jsonElement = contentAsJsonObject.get(str6);
                String str8 = RoomState.HISTORY_VISIBILITY_SHARED;
                String asString = jsonElement != null ? contentAsJsonObject.get(str6).getAsString() : str8;
                if (TextUtils.equals(asString, str8)) {
                    str4 = this.mContext.getString(R.string.notice_room_visibility_shared);
                } else if (TextUtils.equals(asString, RoomState.HISTORY_VISIBILITY_INVITED)) {
                    str4 = this.mContext.getString(R.string.notice_room_visibility_invited);
                } else if (TextUtils.equals(asString, RoomState.HISTORY_VISIBILITY_JOINED)) {
                    str4 = this.mContext.getString(R.string.notice_room_visibility_joined);
                } else if (TextUtils.equals(asString, RoomState.HISTORY_VISIBILITY_WORLD_READABLE)) {
                    str4 = this.mContext.getString(R.string.notice_room_visibility_world_readable);
                } else {
                    str4 = this.mContext.getString(R.string.notice_room_visibility_unknown, new Object[]{asString});
                }
                r22 = this.mContext.getString(R.string.notice_made_future_room_visibility, new Object[]{userDisplayName, str4});
                return r22;
            } else {
                if (Event.EVENT_TYPE_RECEIPT.equals(type)) {
                    r22 = "Read Receipt";
                } else {
                    String str9 = "body";
                    if (Event.EVENT_TYPE_MESSAGE.equals(type)) {
                        ? r1 = "";
                        ? asString2 = contentAsJsonObject.get(str5) != null ? contentAsJsonObject.get(str5).getAsString() : r1;
                        if (contentAsJsonObject.has(str9)) {
                            r22 = contentAsJsonObject.get(str9).getAsString();
                        }
                        if (contentAsJsonObject.has("formatted_body") && contentAsJsonObject.has("format")) {
                            r22 = getFormattedMessage(this.mContext, contentAsJsonObject, roomState, this.mHtmlToolbox);
                        }
                        if (TextUtils.equals(asString2, Message.MSGTYPE_IMAGE) && TextUtils.isEmpty(r22)) {
                            r12 = this.mContext.getString(R.string.summary_user_sent_image, new Object[]{userDisplayName});
                        } else if (TextUtils.equals(asString2, Message.MSGTYPE_EMOTE)) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("* ");
                            sb2.append(userDisplayName);
                            sb2.append(" ");
                            sb2.append(r22);
                            r12 = sb2.toString();
                        } else if (TextUtils.isEmpty(r22)) {
                            r22 = r1;
                        } else if (this.mPrependAuthor) {
                            r13 = new SpannableStringBuilder(this.mContext.getString(R.string.summary_message, new Object[]{userDisplayName, r22}));
                            if (num != null) {
                                try {
                                    ((SpannableStringBuilder) r13).setSpan(new ForegroundColorSpan(num.intValue()), 0, userDisplayName.length() + 1, 33);
                                    ((SpannableStringBuilder) r13).setSpan(new StyleSpan(1), 0, userDisplayName.length() + 1, 33);
                                } catch (Exception e2) {
                                    e = e2;
                                    r2 = r13;
                                }
                            }
                            r22 = r13;
                        }
                    } else if (Event.EVENT_TYPE_STICKER.equals(type)) {
                        if (contentAsJsonObject.has(str9)) {
                            r22 = contentAsJsonObject.get(str9).getAsString();
                        }
                        if (TextUtils.isEmpty(r22)) {
                            r22 = this.mContext.getString(R.string.summary_user_sent_sticker, new Object[]{userDisplayName});
                        }
                    } else if ("m.room.encryption".equals(type)) {
                        r22 = this.mContext.getString(R.string.notice_end_to_end, new Object[]{userDisplayName, event.getWireEventContent().algorithm});
                    } else if ("m.room.encrypted".equals(type)) {
                        if (event.isRedacted()) {
                            String redactionMessage = getRedactionMessage(this.mContext, event, roomState);
                            if (TextUtils.isEmpty(redactionMessage)) {
                                return null;
                            }
                            return redactionMessage;
                        }
                        if (event.getCryptoError() != null) {
                            MXCryptoError cryptoError = event.getCryptoError();
                            if (TextUtils.equals(cryptoError.errcode, MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_ERROR_CODE)) {
                                str3 = this.mContext.getResources().getString(R.string.notice_crypto_error_unkwown_inbound_session_id);
                            } else {
                                str3 = cryptoError.getLocalizedMessage();
                            }
                            str2 = this.mContext.getString(R.string.notice_crypto_unable_to_decrypt, new Object[]{str3});
                        } else {
                            str2 = null;
                        }
                        if (TextUtils.isEmpty(str2)) {
                            str2 = this.mContext.getString(R.string.encrypted_message);
                        }
                        ? spannableString = new SpannableString(str2);
                        spannableString.setSpan(new StyleSpan(2), 0, str2.length(), 33);
                        r13 = spannableString;
                        r22 = r13;
                    } else if (Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type)) {
                        String asString3 = contentAsJsonObject.getAsJsonPrimitive("topic").getAsString();
                        if (event.isRedacted()) {
                            asString3 = getRedactionMessage(this.mContext, event, roomState);
                            if (TextUtils.isEmpty(asString3)) {
                                return null;
                            }
                        }
                        if (!TextUtils.isEmpty(asString3)) {
                            r12 = this.mContext.getString(R.string.notice_room_topic_changed, new Object[]{userDisplayName, asString3});
                        } else {
                            r12 = this.mContext.getString(R.string.notice_room_topic_removed, new Object[]{userDisplayName});
                        }
                    } else if (Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type)) {
                        JsonPrimitive asJsonPrimitive = contentAsJsonObject.getAsJsonPrimitive(TermsResponse.NAME);
                        if (asJsonPrimitive == null) {
                            str = null;
                        } else {
                            str = asJsonPrimitive.getAsString();
                        }
                        if (event.isRedacted()) {
                            str = getRedactionMessage(this.mContext, event, roomState);
                            if (TextUtils.isEmpty(str)) {
                                return null;
                            }
                        }
                        if (!TextUtils.isEmpty(str)) {
                            r12 = this.mContext.getString(R.string.notice_room_name_changed, new Object[]{userDisplayName, str});
                        } else {
                            r12 = this.mContext.getString(R.string.notice_room_name_removed, new Object[]{userDisplayName});
                        }
                    } else if (Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type)) {
                        String str10 = JsonUtils.toRoomThirdPartyInvite(event.getContent()).display_name;
                        if (event.isRedacted()) {
                            str10 = getRedactionMessage(this.mContext, event, roomState);
                            if (TextUtils.isEmpty(str10)) {
                                return null;
                            }
                        }
                        if (str10 != null) {
                            r12 = this.mContext.getString(R.string.notice_room_third_party_invite, new Object[]{userDisplayName, str10});
                        } else {
                            JsonObject prevContentAsJsonObject = event.getPrevContentAsJsonObject();
                            if (prevContentAsJsonObject != null) {
                                r12 = this.mContext.getString(R.string.notice_room_third_party_revoked_invite, new Object[]{userDisplayName, prevContentAsJsonObject.get("display_name")});
                            }
                        }
                    } else if ("m.room.member".equals(type)) {
                        r22 = getMembershipNotice(this.mContext, event, roomState);
                    }
                    r22 = r12;
                }
                return r22;
            }
        } catch (Exception e3) {
            e = e3;
            r2 = r22;
            String str11 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("getTextualDisplay() ");
            sb3.append(e.getMessage());
            Log.e(str11, sb3.toString(), e);
            r22 = r2;
            return r22;
        }
    }

    protected static String senderDisplayNameForEvent(Event event, EventContent eventContent, EventContent eventContent2, RoomState roomState) {
        String sender = event.getSender();
        if (event.isRedacted()) {
            return sender;
        }
        if (roomState != null) {
            sender = roomState.getMemberName(event.getSender());
        }
        if (eventContent == null) {
            return sender;
        }
        String str = "join";
        if (TextUtils.equals(str, eventContent.membership)) {
            return (!TextUtils.isEmpty(eventContent.displayname) || (eventContent2 != null && TextUtils.equals(str, eventContent2.membership) && !TextUtils.isEmpty(eventContent2.displayname))) ? eventContent.displayname : sender;
        }
        return sender;
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00ce  */
    public static String getMembershipNotice(Context context, Event event, RoomState roomState) {
        String str;
        String str2;
        Context context2 = context;
        Event event2 = event;
        RoomState roomState2 = roomState;
        JsonObject contentAsJsonObject = event.getContentAsJsonObject();
        String str3 = null;
        if (contentAsJsonObject == null || contentAsJsonObject.entrySet().size() == 0) {
            return null;
        }
        EventContent eventContent = JsonUtils.toEventContent(event.getContentAsJsonObject());
        EventContent prevContent = event.getPrevContent();
        String senderDisplayNameForEvent = senderDisplayNameForEvent(event2, eventContent, prevContent, roomState2);
        String str4 = prevContent != null ? prevContent.membership : null;
        String str5 = prevContent != null ? prevContent.displayname : null;
        String str6 = eventContent.displayname;
        if (str6 == null) {
            str6 = event2.stateKey;
            if (!(str6 == null || roomState2 == null || event.isRedacted())) {
                str6 = roomState2.getMemberName(str6);
            }
        }
        if (TextUtils.equals(str4, eventContent.membership)) {
            String redactionMessage = getRedactionMessage(context, event, roomState);
            if (!event.isRedacted()) {
                if (!TextUtils.equals(senderDisplayNameForEvent, str5)) {
                    if (!TextUtils.isEmpty(str5)) {
                        str = TextUtils.isEmpty(senderDisplayNameForEvent) ? context2.getString(R.string.notice_display_name_removed, new Object[]{event.getSender(), str5}) : context2.getString(R.string.notice_display_name_changed_from, new Object[]{event.getSender(), str5, senderDisplayNameForEvent});
                    } else if (!TextUtils.equals(event.getSender(), senderDisplayNameForEvent)) {
                        str = context2.getString(R.string.notice_display_name_set, new Object[]{event.getSender(), senderDisplayNameForEvent});
                    }
                    str2 = eventContent.avatar_url;
                    if (prevContent != null) {
                        str3 = prevContent.avatar_url;
                    }
                    if (!TextUtils.equals(str3, str2)) {
                        if (!TextUtils.isEmpty(str)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str);
                            sb.append(" ");
                            sb.append(context2.getString(R.string.notice_avatar_changed_too));
                            str = sb.toString();
                        } else {
                            str = context2.getString(R.string.notice_avatar_url_changed, new Object[]{senderDisplayNameForEvent});
                        }
                    }
                    return str;
                }
                str = "";
                str2 = eventContent.avatar_url;
                if (prevContent != null) {
                }
                if (!TextUtils.equals(str3, str2)) {
                }
                return str;
            } else if (redactionMessage == null) {
                return null;
            } else {
                return context2.getString(R.string.notice_profile_change_redacted, new Object[]{senderDisplayNameForEvent, redactionMessage});
            }
        } else {
            String str7 = "invite";
            if (!str7.equals(eventContent.membership)) {
                String str8 = "join";
                if (!str8.equals(eventContent.membership)) {
                    String str9 = "ban";
                    if ("leave".equals(eventContent.membership)) {
                        if (TextUtils.equals(event2.sender, MXCallsManager.getConferenceUserId(event2.roomId))) {
                            return context2.getString(R.string.notice_voip_finished);
                        }
                        if (TextUtils.equals(event.getSender(), event2.stateKey)) {
                            if (prevContent == null || !TextUtils.equals(prevContent.membership, str7)) {
                                if (eventContent.displayname == null && str5 != null) {
                                    senderDisplayNameForEvent = str5;
                                }
                                return context2.getString(R.string.notice_room_leave, new Object[]{senderDisplayNameForEvent});
                            }
                            return context2.getString(R.string.notice_room_reject, new Object[]{senderDisplayNameForEvent});
                        } else if (str4 != null) {
                            if (str4.equals(str7)) {
                                return context2.getString(R.string.notice_room_withdraw, new Object[]{senderDisplayNameForEvent, str6});
                            } else if (str4.equals(str8)) {
                                return context2.getString(R.string.notice_room_kick, new Object[]{senderDisplayNameForEvent, str6});
                            } else if (str4.equals(str9)) {
                                return context2.getString(R.string.notice_room_unban, new Object[]{senderDisplayNameForEvent, str6});
                            }
                        }
                    } else if (str9.equals(eventContent.membership)) {
                        return context2.getString(R.string.notice_room_ban, new Object[]{senderDisplayNameForEvent, str6});
                    } else {
                        if (RoomMember.MEMBERSHIP_KICK.equals(eventContent.membership)) {
                            return context2.getString(R.string.notice_room_kick, new Object[]{senderDisplayNameForEvent, str6});
                        }
                        String str10 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unknown membership: ");
                        sb2.append(eventContent.membership);
                        Log.e(str10, sb2.toString());
                    }
                    return null;
                } else if (TextUtils.equals(event2.sender, MXCallsManager.getConferenceUserId(event2.roomId))) {
                    return context2.getString(R.string.notice_voip_started);
                } else {
                    return context2.getString(R.string.notice_room_join, new Object[]{senderDisplayNameForEvent});
                }
            } else if (eventContent.third_party_invite != null) {
                return context2.getString(R.string.notice_room_third_party_registered_invite, new Object[]{str6, eventContent.third_party_invite.display_name});
            } else {
                if (!(roomState2 == null || roomState.getDataHandler() == null)) {
                    str3 = roomState.getDataHandler().getUserId();
                }
                if (TextUtils.equals(event2.stateKey, str3)) {
                    return context2.getString(R.string.notice_room_invite_you, new Object[]{senderDisplayNameForEvent});
                } else if (event2.stateKey == null) {
                    return context2.getString(R.string.notice_room_invite_no_invitee, new Object[]{senderDisplayNameForEvent});
                } else if (str6.equals(MXCallsManager.getConferenceUserId(event2.roomId))) {
                    return context2.getString(R.string.notice_requested_voip_conference, new Object[]{senderDisplayNameForEvent});
                } else {
                    return context2.getString(R.string.notice_room_invite, new Object[]{senderDisplayNameForEvent, str6});
                }
            }
        }
    }

    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r1v2, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r1v3, types: [android.text.Html$ImageGetter] */
    /* JADX WARNING: type inference failed for: r8v3 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r1v6, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r8v10, types: [android.text.Spanned] */
    /* JADX WARNING: type inference failed for: r8v11, types: [android.text.Spanned] */
    /* JADX WARNING: type inference failed for: r1v7, types: [android.text.Html$ImageGetter] */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r8v15 */
    /* JADX WARNING: type inference failed for: r8v16 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00eb, code lost:
        r1 = r1;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v1
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.text.Html$ImageGetter]
  uses: [java.lang.CharSequence, android.text.Html$ImageGetter]
  mth insns count: 81
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 7 */
    private CharSequence getFormattedMessage(Context context, JsonObject jsonObject, RoomState roomState, HtmlToolbox htmlToolbox) {
        TagHandler tagHandler;
        ? r8;
        ? r1 = 0;
        if (Message.FORMAT_MATRIX_HTML.equals(jsonObject.getAsJsonPrimitive("format").getAsString())) {
            String asString = jsonObject.getAsJsonPrimitive("formatted_body").getAsString();
            if (htmlToolbox != null) {
                asString = htmlToolbox.convert(asString);
            }
            String str = "m.relates_to";
            if (jsonObject.has(str)) {
                JsonElement jsonElement = jsonObject.get(str);
                if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("m.in_reply_to")) {
                    String str2 = MESSAGE_IN_REPLY_TO_FIRST_PART;
                    if (asString.startsWith(str2)) {
                        String str3 = MESSAGE_IN_REPLY_TO_LAST_PART;
                        int indexOf = asString.indexOf(str3);
                        if (indexOf != -1) {
                            String substring = asString.substring(indexOf + 4);
                            int indexOf2 = substring.indexOf(MESSAGE_IN_REPLY_TO_HREF_TAG_END);
                            int indexOf3 = substring.indexOf(str3);
                            if (!(indexOf2 == 1 || indexOf3 == -1)) {
                                int i = indexOf2 + 2;
                                String substring2 = substring.substring(i, indexOf3);
                                String memberName = roomState.getMemberName(substring2);
                                if (!memberName.equals(substring2)) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(substring.substring(0, i));
                                    sb.append(memberName);
                                    sb.append(substring.substring(indexOf3));
                                    substring = sb.toString();
                                }
                            }
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str2);
                            sb2.append(context.getString(R.string.message_reply_to_prefix));
                            sb2.append(substring);
                            asString = sb2.toString();
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(asString)) {
                if (htmlToolbox != null) {
                    ? imageGetter = htmlToolbox.getImageGetter();
                    tagHandler = htmlToolbox.getTagHandler(asString);
                    r1 = imageGetter;
                } else {
                    tagHandler = null;
                }
                if (VERSION.SDK_INT >= 24) {
                    r8 = Html.fromHtml(asString, 12, r1, tagHandler);
                } else {
                    r8 = Html.fromHtml(asString, r1, tagHandler);
                }
                ? r12 = r8;
                while (r12.length() > 0 && r12.charAt(r12.length() - 1) == 10) {
                    r12 = r12.subSequence(0, r12.length() - 1);
                }
            }
        }
        return r1;
    }
}
