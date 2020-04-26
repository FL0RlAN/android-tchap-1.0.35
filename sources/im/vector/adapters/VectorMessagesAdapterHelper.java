package im.vector.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.listeners.IMessagesAdapterActionsListener;
import im.vector.settings.VectorLocale;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.MatrixLinkMovementMethod;
import im.vector.util.MatrixURLSpan;
import im.vector.util.RiotEventDisplay;
import im.vector.util.VectorImageGetter;
import im.vector.util.VectorUtils;
import im.vector.view.PillView;
import im.vector.view.PillView.OnUpdateListener;
import im.vector.view.UrlPreviewView;
import im.vector.widgets.WidgetsManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.groups.GroupsManager;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.URLPreview;
import org.matrix.androidsdk.rest.model.group.Group;
import org.matrix.androidsdk.rest.model.group.GroupProfile;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.StickerMessage;
import org.matrix.androidsdk.view.HtmlTagHandler;

class VectorMessagesAdapterHelper {
    private static final String AVATAR_URL_KEY = "avatar_url";
    private static final String DISPLAYNAME_KEY = "displayname";
    public static final String END_FENCED_BLOCK = "</code></pre>";
    private static final Pattern FENCED_CODE_BLOCK_PATTERN = Pattern.compile("(?m)(?=<pre><code>)|(?<=</code></pre>)");
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorMessagesAdapterHelper.class.getSimpleName();
    private static final String MEMBERSHIP_KEY = "membership";
    public static final String START_FENCED_BLOCK = "<pre><code>";
    private static final Set<String> mAllowedHTMLTags = new HashSet(Arrays.asList(new String[]{"font", "del", "h1", "h2", "h3", "h4", "h5", "h6", "blockquote", "p", "a", "ul", "ol", "sup", "sub", "nl", "li", "b", "i", "u", "strong", "em", "strike", "code", "hr", "br", "div", "table", "thead", "caption", "tbody", "tr", "th", "td", "pre", "span", "img"}));
    private static final Pattern mHtmlPatter = Pattern.compile("<(\\w+)[^>]*>", 2);
    /* access modifiers changed from: private */
    public final VectorMessagesAdapter mAdapter;
    private Map<String, String[]> mCodeBlocksMap = new HashMap();
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public IMessagesAdapterActionsListener mEventsListener;
    private final Map<String, List<String>> mExtractedUrls = new HashMap();
    private final Map<String, String> mHtmlMap = new HashMap();
    private VectorImageGetter mImageGetter;
    private MatrixLinkMovementMethod mLinkMovementMethod;
    /* access modifiers changed from: private */
    public final Set<String> mPendingUrls = new HashSet();
    /* access modifiers changed from: private */
    public Map<String, Drawable> mPillsDrawableCache = new HashMap();
    private Room mRoom = null;
    /* access modifiers changed from: private */
    public final MXSession mSession;
    /* access modifiers changed from: private */
    public final Map<String, URLPreview> mUrlsPreviews = new HashMap();

    VectorMessagesAdapterHelper(Context context, MXSession mXSession, VectorMessagesAdapter vectorMessagesAdapter) {
        this.mContext = context;
        this.mSession = mXSession;
        this.mAdapter = vectorMessagesAdapter;
    }

    /* access modifiers changed from: 0000 */
    public void setVectorMessagesAdapterActionsListener(IMessagesAdapterActionsListener iMessagesAdapterActionsListener) {
        this.mEventsListener = iMessagesAdapterActionsListener;
    }

    /* access modifiers changed from: 0000 */
    public void setLinkMovementMethod(MatrixLinkMovementMethod matrixLinkMovementMethod) {
        this.mLinkMovementMethod = matrixLinkMovementMethod;
    }

    /* access modifiers changed from: 0000 */
    public void setImageGetter(VectorImageGetter vectorImageGetter) {
        this.mImageGetter = vectorImageGetter;
    }

    public void setSenderValue(View view, MessageRow messageRow, boolean z) {
        TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_sender);
        View findViewById = view.findViewById(R.id.messagesAdapter_flair_groups_list);
        if (textView != null) {
            Event event = messageRow.getEvent();
            findViewById.setVisibility(8);
            findViewById.setTag(null);
            if (z) {
                textView.setVisibility(8);
                return;
            }
            String type = event.getType();
            if (event.isCallEvent() || Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type) || "m.room.member".equals(type) || Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type) || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type) || Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY.equals(type) || "m.room.encryption".equals(type)) {
                textView.setVisibility(8);
                return;
            }
            textView.setVisibility(0);
            textView.setText(DinsicUtils.getNameFromDisplayName(messageRow.getSenderDisplayName()));
            final String sender = event.getSender();
            final String charSequence = textView.getText() == null ? "" : textView.getText().toString();
            textView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (VectorMessagesAdapterHelper.this.mEventsListener != null) {
                        VectorMessagesAdapterHelper.this.mEventsListener.onSenderNameClick(sender, charSequence);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void refreshGroupFlairView(View view, Event event, Set<String> set, String str) {
        int i;
        View view2 = view;
        final Event event2 = event;
        Set<String> set2 = set;
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## refreshGroupFlairView () : ");
        sb.append(event2.sender);
        sb.append(" allows flair to ");
        sb.append(set2);
        Log.d(str2, sb.toString());
        String str3 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## refreshGroupFlairView () : room related groups ");
        sb2.append(this.mRoom.getState().getRelatedGroups());
        Log.d(str3, sb2.toString());
        if (!set.isEmpty()) {
            set2.retainAll(this.mRoom.getState().getRelatedGroups());
        }
        String str4 = LOG_TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("## refreshGroupFlairView () : group ids to display ");
        sb3.append(set2);
        Log.d(str4, sb3.toString());
        if (set.isEmpty()) {
            view2.setVisibility(8);
        } else if (this.mSession.isAlive()) {
            int i2 = 0;
            view2.setVisibility(0);
            ArrayList arrayList = new ArrayList();
            arrayList.add((ImageView) view2.findViewById(R.id.message_avatar_group_1).findViewById(R.id.avatar_img));
            arrayList.add((ImageView) view2.findViewById(R.id.message_avatar_group_2).findViewById(R.id.avatar_img));
            arrayList.add((ImageView) view2.findViewById(R.id.message_avatar_group_3).findViewById(R.id.avatar_img));
            TextView textView = (TextView) view2.findViewById(R.id.message_more_than_expected);
            final ArrayList arrayList2 = new ArrayList(set2);
            int min = Math.min(arrayList2.size(), arrayList.size());
            int i3 = 0;
            while (i3 < min) {
                final String str5 = (String) arrayList2.get(i3);
                ImageView imageView = (ImageView) arrayList.get(i3);
                imageView.setVisibility(i2);
                Group group = this.mSession.getGroupsManager().getGroup(str5);
                if (group == null) {
                    group = new Group(str5);
                }
                GroupProfile groupProfile = this.mSession.getGroupsManager().getGroupProfile(str5);
                if (groupProfile != null) {
                    String str6 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## refreshGroupFlairView () : profile of ");
                    sb4.append(str5);
                    sb4.append(" is cached");
                    Log.d(str6, sb4.toString());
                    group.setGroupProfile(groupProfile);
                    VectorUtils.loadGroupAvatar(this.mContext, this.mSession, imageView, group);
                    i = i3;
                } else {
                    VectorUtils.loadGroupAvatar(this.mContext, this.mSession, imageView, group);
                    String str7 = LOG_TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("## refreshGroupFlairView () : get profile of ");
                    sb5.append(str5);
                    Log.d(str7, sb5.toString());
                    GroupsManager groupsManager = this.mSession.getGroupsManager();
                    final View view3 = view;
                    ImageView imageView2 = imageView;
                    final String str8 = str;
                    String str9 = str5;
                    i = i3;
                    final ImageView imageView3 = imageView2;
                    AnonymousClass2 r0 = new ApiCallback<GroupProfile>() {
                        private void refresh(GroupProfile groupProfile) {
                            if (TextUtils.equals((String) view3.getTag(), str8)) {
                                Group group = new Group(str5);
                                group.setGroupProfile(groupProfile);
                                String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## refreshGroupFlairView () : refresh group avatar ");
                                sb.append(str5);
                                Log.d(access$100, sb.toString());
                                VectorUtils.loadGroupAvatar(VectorMessagesAdapterHelper.this.mContext, VectorMessagesAdapterHelper.this.mSession, imageView3, group);
                            }
                        }

                        public void onSuccess(GroupProfile groupProfile) {
                            String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshGroupFlairView () : get profile of ");
                            sb.append(str5);
                            sb.append(" succeeded");
                            Log.d(access$100, sb.toString());
                            refresh(groupProfile);
                        }

                        public void onNetworkError(Exception exc) {
                            String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshGroupFlairView () : get profile of ");
                            sb.append(str5);
                            sb.append(" failed ");
                            sb.append(exc.getMessage());
                            Log.e(access$100, sb.toString(), exc);
                            refresh(null);
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshGroupFlairView () : get profile of ");
                            sb.append(str5);
                            sb.append(" failed ");
                            sb.append(matrixError.getMessage());
                            Log.e(access$100, sb.toString());
                            refresh(null);
                        }

                        public void onUnexpectedError(Exception exc) {
                            String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshGroupFlairView () : get profile of ");
                            sb.append(str5);
                            sb.append(" failed ");
                            sb.append(exc.getMessage());
                            Log.e(access$100, sb.toString(), exc);
                            refresh(null);
                        }
                    };
                    groupsManager.getGroupProfile(str9, r0);
                }
                i3 = i + 1;
                i2 = 0;
            }
            for (int i4 = i3; i4 < arrayList.size(); i4++) {
                ((ImageView) arrayList.get(i4)).setVisibility(8);
            }
            int i5 = 8;
            if (set.size() > arrayList.size()) {
                i5 = 0;
            }
            textView.setVisibility(i5);
            textView.setText(this.mContext.getString(R.string.plus_x, new Object[]{Integer.valueOf(set.size() - arrayList.size())}));
            if (set.size() > 0) {
                view2.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (VectorMessagesAdapterHelper.this.mEventsListener != null) {
                            VectorMessagesAdapterHelper.this.mEventsListener.onGroupFlairClick(event2.getSender(), arrayList2);
                        }
                    }
                });
            } else {
                view2.setOnClickListener(null);
            }
        }
    }

    private void refreshGroupFlairView(final View view, final Event event) {
        StringBuilder sb = new StringBuilder();
        sb.append(event.getSender());
        sb.append("__");
        sb.append(event.eventId);
        final String sb2 = sb.toString();
        if (this.mRoom == null) {
            this.mRoom = this.mSession.getDataHandler().getRoom(event.roomId, false);
            if (this.mRoom == null) {
                Log.d(LOG_TAG, "## refreshGroupFlairView () : the room is not available");
                view.setVisibility(8);
                return;
            }
        }
        if (this.mRoom.getState().getRelatedGroups().isEmpty()) {
            Log.d(LOG_TAG, "## refreshGroupFlairView () : no related group");
            view.setVisibility(8);
            return;
        }
        view.setTag(sb2);
        String str = LOG_TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("## refreshGroupFlairView () : eventId ");
        sb3.append(event.eventId);
        sb3.append(" from ");
        sb3.append(event.sender);
        Log.d(str, sb3.toString());
        Set userPublicisedGroups = this.mSession.getGroupsManager().getUserPublicisedGroups(event.getSender());
        if (userPublicisedGroups != null) {
            refreshGroupFlairView(view, event, userPublicisedGroups, sb2);
        } else {
            view.setVisibility(8);
            this.mSession.getGroupsManager().getUserPublicisedGroups(event.getSender(), false, new ApiCallback<Set<String>>() {
                public void onSuccess(Set<String> set) {
                    VectorMessagesAdapterHelper.this.refreshGroupFlairView(view, event, set, sb2);
                }

                public void onNetworkError(Exception exc) {
                    String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## refreshGroupFlairView failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$100, sb.toString(), exc);
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## refreshGroupFlairView failed ");
                    sb.append(matrixError.getMessage());
                    Log.e(access$100, sb.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    String access$100 = VectorMessagesAdapterHelper.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## refreshGroupFlairView failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$100, sb.toString(), exc);
                }
            });
        }
    }

    static TextView setTimestampValue(View view, String str) {
        TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_timestamp);
        if (textView != null) {
            if (TextUtils.isEmpty(str)) {
                textView.setVisibility(8);
            } else {
                textView.setVisibility(0);
                textView.setText(str);
            }
        }
        return textView;
    }

    /* access modifiers changed from: 0000 */
    public void loadMemberAvatar(ImageView imageView, MessageRow messageRow) {
        Event event = messageRow.getEvent();
        RoomMember sender = messageRow.getSender();
        JsonObject contentAsJsonObject = event.getContentAsJsonObject();
        String str = AVATAR_URL_KEY;
        String str2 = null;
        String asString = (!contentAsJsonObject.has(str) || contentAsJsonObject.get(str) == JsonNull.INSTANCE) ? null : contentAsJsonObject.get(str).getAsString();
        String str3 = MEMBERSHIP_KEY;
        if (contentAsJsonObject.has(str3)) {
            CharSequence asString2 = contentAsJsonObject.get(str3) == JsonNull.INSTANCE ? null : contentAsJsonObject.get(str3).getAsString();
            if (TextUtils.equals(asString2, "invite")) {
                asString = sender != null ? sender.getAvatarUrl() : null;
            }
            if (TextUtils.equals(asString2, "join")) {
                String str4 = DISPLAYNAME_KEY;
                if (contentAsJsonObject.has(str4) && contentAsJsonObject.get(str4) != JsonNull.INSTANCE) {
                    str2 = contentAsJsonObject.get(str4).getAsString();
                }
            }
        }
        String sender2 = event.getSender();
        if (this.mSession.isAlive()) {
            if (TextUtils.isEmpty(str2) && sender != null) {
                str2 = sender.displayname;
            }
            String str5 = str2;
            if (sender != null && asString == null) {
                asString = sender.getAvatarUrl();
            }
            String str6 = asString;
            if (sender != null) {
                VectorUtils.loadUserAvatar(this.mContext, this.mSession, imageView, str6, sender.getUserId(), str5);
            } else {
                VectorUtils.loadUserAvatar(this.mContext, this.mSession, imageView, str6, sender2, str5);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public View setSenderAvatar(View view, MessageRow messageRow, boolean z) {
        Event event = messageRow.getEvent();
        View findViewById = view.findViewById(R.id.messagesAdapter_roundAvatar);
        if (findViewById != null) {
            final String sender = event.getSender();
            findViewById.setClickable(true);
            findViewById.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    return VectorMessagesAdapterHelper.this.mEventsListener != null && VectorMessagesAdapterHelper.this.mEventsListener.onAvatarLongClick(sender);
                }
            });
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (VectorMessagesAdapterHelper.this.mEventsListener != null) {
                        VectorMessagesAdapterHelper.this.mEventsListener.onAvatarClick(sender);
                    }
                }
            });
        }
        if (findViewById != null) {
            if (z) {
                findViewById.setVisibility(4);
            } else {
                findViewById.setVisibility(0);
                ImageView imageView = (ImageView) findViewById.findViewById(R.id.avatar_img);
                imageView.setTag(null);
                loadMemberAvatar(imageView, messageRow);
            }
        }
        return findViewById;
    }

    static void setHeader(View view, String str, int i) {
        View findViewById = view.findViewById(R.id.messagesAdapter_message_header);
        if (findViewById == null) {
            return;
        }
        if (str != null) {
            ((TextView) view.findViewById(R.id.messagesAdapter_message_header_text)).setText(str);
            int i2 = 0;
            findViewById.setVisibility(0);
            View findViewById2 = findViewById.findViewById(R.id.messagesAdapter_message_header_top_margin);
            if (i == 0) {
                i2 = 8;
            }
            findViewById2.setVisibility(i2);
            return;
        }
        findViewById.setVisibility(8);
    }

    public void initializeLayoutsDisplay(View view) {
        View findViewById = view.findViewById(R.id.content_download_progress_layout);
        View findViewById2 = view.findViewById(R.id.content_upload_progress_layout);
        View findViewById3 = view.findViewById(R.id.upload_event_spinner);
        View findViewById4 = view.findViewById(R.id.message_adapter_sticker_layout);
        if (findViewById2 != null) {
            findViewById2.setVisibility(8);
        }
        if (findViewById != null) {
            findViewById.setVisibility(8);
        }
        if (findViewById3 != null) {
            findViewById3.setVisibility(8);
        }
        if (findViewById4 != null) {
            findViewById4.setVisibility(8);
        }
    }

    public void hideStickerDescription(View view) {
        View findViewById = view.findViewById(R.id.message_adapter_sticker_layout);
        if (findViewById != null) {
            findViewById.setVisibility(8);
        }
    }

    public void showStickerDescription(View view, StickerMessage stickerMessage) {
        View findViewById = view.findViewById(R.id.message_adapter_sticker_layout);
        TextView textView = (TextView) view.findViewById(R.id.message_adapter_sticker_description);
        if (findViewById != null && textView != null) {
            findViewById.setVisibility(0);
            textView.setVisibility(0);
            textView.setText(stickerMessage.body);
        }
    }

    /* access modifiers changed from: 0000 */
    public void hideReadReceipts(View view) {
        View findViewById = view.findViewById(R.id.messagesAdapter_avatars_list);
        if (findViewById != null) {
            findViewById.setVisibility(8);
        }
    }

    /* access modifiers changed from: 0000 */
    public void displayReadReceipts(View view, MessageRow messageRow, boolean z, Map<String, RoomMember> map) {
        Map<String, RoomMember> map2 = map;
        View findViewById = view.findViewById(R.id.messagesAdapter_avatars_list);
        if (findViewById != null && this.mSession.isAlive()) {
            final String str = messageRow.getEvent().eventId;
            IMXStore store = this.mSession.getDataHandler().getStore();
            if (z) {
                findViewById.setVisibility(8);
                return;
            }
            List eventReceipts = store.getEventReceipts(messageRow.getEvent().roomId, str, true, true);
            if (eventReceipts == null || eventReceipts.size() == 0) {
                findViewById.setVisibility(8);
                return;
            }
            if (this.mRoom == null) {
                this.mRoom = this.mSession.getDataHandler().getRoom(messageRow.getEvent().roomId, false);
                if (this.mRoom == null) {
                    Log.d(LOG_TAG, "## displayReadReceipts () : the room is not available");
                    findViewById.setVisibility(8);
                    return;
                }
            }
            findViewById.setVisibility(0);
            ArrayList arrayList = new ArrayList();
            arrayList.add(findViewById.findViewById(R.id.message_avatar_receipt_1).findViewById(R.id.avatar_img));
            arrayList.add(findViewById.findViewById(R.id.message_avatar_receipt_2).findViewById(R.id.avatar_img));
            arrayList.add(findViewById.findViewById(R.id.message_avatar_receipt_3).findViewById(R.id.avatar_img));
            arrayList.add(findViewById.findViewById(R.id.message_avatar_receipt_4).findViewById(R.id.avatar_img));
            arrayList.add(findViewById.findViewById(R.id.message_avatar_receipt_5).findViewById(R.id.avatar_img));
            TextView textView = (TextView) findViewById.findViewById(R.id.message_more_than_expected);
            int min = Math.min(eventReceipts.size(), arrayList.size());
            int i = 0;
            while (i < min) {
                ReceiptData receiptData = (ReceiptData) eventReceipts.get(i);
                RoomMember member = this.mRoom.getState().getMember(receiptData.userId);
                if (member == null && map2 != null) {
                    member = (RoomMember) map2.get(receiptData.userId);
                }
                ImageView imageView = (ImageView) arrayList.get(i);
                imageView.setVisibility(0);
                imageView.setTag(null);
                if (member != null) {
                    VectorUtils.loadRoomMemberAvatar(this.mContext, this.mSession, imageView, member);
                } else {
                    VectorUtils.loadUserAvatar(this.mContext, this.mSession, imageView, null, receiptData.userId, receiptData.userId);
                }
                i++;
            }
            textView.setVisibility(eventReceipts.size() <= arrayList.size() ? 8 : 0);
            textView.setText(this.mContext.getString(R.string.x_plus, new Object[]{Integer.valueOf(eventReceipts.size() - arrayList.size())}));
            while (i < arrayList.size()) {
                ((View) arrayList.get(i)).setVisibility(4);
                i++;
            }
            View findViewById2 = findViewById.findViewById(R.id.read_receipt_avatars_list);
            if (findViewById2 == null) {
                findViewById2 = findViewById;
            }
            if (eventReceipts.size() > 0) {
                findViewById2.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (VectorMessagesAdapterHelper.this.mEventsListener != null) {
                            VectorMessagesAdapterHelper.this.mEventsListener.onMoreReadReceiptClick(str);
                        }
                    }
                });
            } else {
                findViewById2.setOnClickListener(null);
            }
        }
    }

    static void setMediaProgressLayout(View view, View view2) {
        int i = ((MarginLayoutParams) view2.getLayoutParams()).leftMargin;
        View findViewById = view.findViewById(R.id.content_download_progress_layout);
        if (findViewById != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) findViewById.getLayoutParams();
            marginLayoutParams.setMargins(i, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
            findViewById.setLayoutParams(marginLayoutParams);
        }
        View findViewById2 = view.findViewById(R.id.content_upload_progress_layout);
        if (findViewById2 != null) {
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) findViewById2.getLayoutParams();
            marginLayoutParams2.setMargins(i, marginLayoutParams2.topMargin, marginLayoutParams2.rightMargin, marginLayoutParams2.bottomMargin);
            findViewById2.setLayoutParams(marginLayoutParams2);
        }
    }

    private void makeLinkClickable(SpannableStringBuilder spannableStringBuilder, final URLSpan uRLSpan, boolean z) {
        int spanStart = spannableStringBuilder.getSpanStart(uRLSpan);
        int spanEnd = spannableStringBuilder.getSpanEnd(uRLSpan);
        if (spanStart >= 0 && spanEnd >= 0) {
            int spanFlags = spannableStringBuilder.getSpanFlags(uRLSpan);
            if (PillView.isPillable(uRLSpan.getURL())) {
                StringBuilder sb = new StringBuilder();
                sb.append(uRLSpan.getURL());
                String str = " ";
                sb.append(str);
                sb.append(z);
                sb.append(str);
                sb.append(spannableStringBuilder.subSequence(spanStart, spanEnd).toString());
                final String sb2 = sb.toString();
                Drawable drawable = (Drawable) this.mPillsDrawableCache.get(sb2);
                if (drawable == null) {
                    PillView pillView = new PillView(this.mContext);
                    pillView.setBackgroundResource(17170445);
                    final WeakReference weakReference = new WeakReference(pillView);
                    pillView.initData(spannableStringBuilder.subSequence(spanStart, spanEnd), uRLSpan.getURL(), this.mSession, new OnUpdateListener() {
                        public void onAvatarUpdate() {
                            WeakReference weakReference = weakReference;
                            if (weakReference != null && weakReference.get() != null) {
                                VectorMessagesAdapterHelper.this.mPillsDrawableCache.put(sb2, ((PillView) weakReference.get()).getDrawable(true));
                                VectorMessagesAdapterHelper.this.mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    pillView.setHighlighted(z);
                    drawable = pillView.getDrawable(false);
                }
                if (drawable != null) {
                    this.mPillsDrawableCache.put(sb2, drawable);
                    ImageSpan imageSpan = new ImageSpan(drawable);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    spannableStringBuilder.setSpan(imageSpan, spanStart, spanEnd, spanFlags);
                }
            } else {
                spannableStringBuilder.setSpan(new ClickableSpan() {
                    public void onClick(View view) {
                        if (VectorMessagesAdapterHelper.this.mEventsListener != null) {
                            VectorMessagesAdapterHelper.this.mEventsListener.onURLClick(Uri.parse(uRLSpan.getURL()));
                        }
                    }
                }, spanStart, spanEnd, spanFlags);
            }
            spannableStringBuilder.removeSpan(uRLSpan);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean containsFencedCodeBlocks(Message message) {
        return message.formatted_body != null && message.formatted_body.contains(START_FENCED_BLOCK) && message.formatted_body.contains(END_FENCED_BLOCK);
    }

    /* access modifiers changed from: 0000 */
    public String[] getFencedCodeBlocks(Message message) {
        if (TextUtils.isEmpty(message.formatted_body)) {
            return new String[0];
        }
        String[] strArr = (String[]) this.mCodeBlocksMap.get(message.formatted_body);
        if (strArr == null) {
            strArr = FENCED_CODE_BLOCK_PATTERN.split(message.formatted_body);
            this.mCodeBlocksMap.put(message.formatted_body, strArr);
        }
        return strArr;
    }

    /* access modifiers changed from: 0000 */
    public void highlightFencedCode(TextView textView) {
        if (textView != null) {
            textView.setBackgroundColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_markdown_block_background_color));
        }
    }

    /* access modifiers changed from: 0000 */
    public void applyLinkMovementMethod(TextView textView) {
        if (textView != null) {
            MatrixLinkMovementMethod matrixLinkMovementMethod = this.mLinkMovementMethod;
            if (matrixLinkMovementMethod != null) {
                textView.setMovementMethod(matrixLinkMovementMethod);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public CharSequence highlightPattern(Spannable spannable, String str, CharacterStyle characterStyle, boolean z) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(spannable) && spannable.length() >= str.length()) {
            String lowerCase = spannable.toString().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
            String lowerCase2 = str.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
            int indexOf = lowerCase.indexOf(lowerCase2, 0);
            while (indexOf >= 0) {
                int length = lowerCase2.length() + indexOf;
                spannable.setSpan(characterStyle, indexOf, length, 33);
                spannable.setSpan(new StyleSpan(1), indexOf, length, 33);
                indexOf = lowerCase.indexOf(lowerCase2, length);
            }
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannable);
        URLSpan[] uRLSpanArr = (URLSpan[]) spannableStringBuilder.getSpans(0, spannable.length(), URLSpan.class);
        if (uRLSpanArr != null && uRLSpanArr.length > 0) {
            for (URLSpan makeLinkClickable : uRLSpanArr) {
                makeLinkClickable(spannableStringBuilder, makeLinkClickable, z);
            }
        }
        MatrixURLSpan.refreshMatrixSpans(spannableStringBuilder, this.mEventsListener);
        return spannableStringBuilder;
    }

    /* access modifiers changed from: 0000 */
    public CharSequence convertToHtml(String str) {
        Spanned spanned;
        HtmlTagHandler htmlTagHandler = new HtmlTagHandler();
        htmlTagHandler.mContext = this.mContext;
        htmlTagHandler.setCodeBlockBackgroundColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_markdown_block_background_color));
        if (str == null) {
            return "";
        }
        boolean z = !str.contains("<table>");
        if (VERSION.SDK_INT >= 24) {
            VectorImageGetter vectorImageGetter = this.mImageGetter;
            if (!z) {
                htmlTagHandler = null;
            }
            spanned = Html.fromHtml(str, 4, vectorImageGetter, htmlTagHandler);
        } else {
            VectorImageGetter vectorImageGetter2 = this.mImageGetter;
            if (!z) {
                htmlTagHandler = null;
            }
            spanned = Html.fromHtml(str, vectorImageGetter2, htmlTagHandler);
        }
        if (TextUtils.isEmpty(spanned)) {
            return spanned;
        }
        int length = spanned.length() - 1;
        int i = 0;
        while (i < spanned.length() - 1 && 10 == spanned.charAt(i)) {
            i++;
        }
        while (length >= 0 && 10 == spanned.charAt(length)) {
            length--;
        }
        if (length < i) {
            return spanned.subSequence(0, 0);
        }
        return spanned.subSequence(i, length + 1);
    }

    static boolean isDisplayableEvent(Context context, MessageRow messageRow) {
        boolean z = false;
        if (messageRow == null) {
            return false;
        }
        Event event = messageRow.getEvent();
        if (event == null) {
            return false;
        }
        String type = event.getType();
        if (Event.EVENT_TYPE_MESSAGE.equals(type)) {
            Message message = JsonUtils.toMessage(event.getContent());
            if (!event.isRedacted() && (!TextUtils.isEmpty(message.body) || TextUtils.equals(message.msgtype, Message.MSGTYPE_EMOTE))) {
                z = true;
            }
            return z;
        } else if (Event.EVENT_TYPE_STICKER.equals(type)) {
            if (!TextUtils.isEmpty(JsonUtils.toStickerMessage(event.getContent()).body) && !event.isRedacted()) {
                z = true;
            }
            return z;
        } else if (Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type) || Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type)) {
            if (messageRow.getText(null, new RiotEventDisplay(context)) != null) {
                z = true;
            }
            return z;
        } else if (event.isCallEvent()) {
            if (Event.EVENT_TYPE_CALL_INVITE.equals(type) || Event.EVENT_TYPE_CALL_ANSWER.equals(type) || Event.EVENT_TYPE_CALL_HANGUP.equals(type)) {
                z = true;
            }
            return z;
        } else if ("m.room.member".equals(type) || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type)) {
            if (messageRow.getText(null, new RiotEventDisplay(context)) != null) {
                z = true;
            }
            return z;
        } else if (Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY.equals(type)) {
            return true;
        } else {
            if ("m.room.encrypted".equals(type) || "m.room.encryption".equals(type)) {
                RiotEventDisplay riotEventDisplay = new RiotEventDisplay(context);
                if (event.hasContentFields() && messageRow.getText(null, riotEventDisplay) != null) {
                    z = true;
                }
                return z;
            }
            if (TextUtils.equals(WidgetsManager.WIDGET_EVENT_TYPE, event.getType())) {
                return true;
            }
            if (Event.EVENT_TYPE_STATE_ROOM_CREATE.equals(type) && messageRow.getRoomCreateContentPredecessor() != null) {
                z = true;
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    public String getSanitisedHtml(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String str2 = (String) this.mHtmlMap.get(str);
        if (str2 == null) {
            str2 = sanitiseHTML(str);
            this.mHtmlMap.put(str, str2);
        }
        return str2;
    }

    private static String sanitiseHTML(String str) {
        Matcher matcher = mHtmlPatter.matcher(str);
        HashSet<String> hashSet = new HashSet<>();
        while (matcher.find()) {
            try {
                String substring = str.substring(matcher.start(1), matcher.end(1));
                if (!mAllowedHTMLTags.contains(substring)) {
                    hashSet.add(substring);
                }
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sanitiseHTML failed ");
                sb.append(e.getLocalizedMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        if (hashSet.isEmpty()) {
            return str;
        }
        String str3 = "";
        String str4 = str3;
        for (String str5 : hashSet) {
            if (!str4.isEmpty()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str4);
                sb2.append("|");
                str4 = sb2.toString();
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str4);
            sb3.append(str5);
            str4 = sb3.toString();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("<\\/?(");
        sb4.append(str4);
        sb4.append(")[^>]*>");
        return str.replaceAll(sb4.toString(), str3);
    }

    private List<String> extractWebUrl(String str) {
        List<String> list = (List) this.mExtractedUrls.get(str);
        if (list == null) {
            list = new ArrayList<>();
            Matcher matcher = Patterns.WEB_URL.matcher(str);
            while (matcher.find()) {
                try {
                    String substring = str.substring(matcher.start(0), matcher.end(0));
                    if (!list.contains(substring)) {
                        list.add(substring);
                    }
                } catch (Exception e) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## extractWebUrl() ");
                    sb.append(e.getMessage());
                    Log.e(str2, sb.toString(), e);
                }
            }
            this.mExtractedUrls.put(str, list);
        }
        return list;
    }

    /* access modifiers changed from: 0000 */
    public void manageURLPreviews(Message message, View view, String str) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.messagesAdapter_urls_preview_list);
        if (linearLayout != null) {
            if (TextUtils.isEmpty(message.body)) {
                linearLayout.setVisibility(8);
                return;
            }
            List<String> extractWebUrl = extractWebUrl(message.body);
            if (extractWebUrl.isEmpty()) {
                linearLayout.setVisibility(8);
            } else if (!TextUtils.equals((String) linearLayout.getTag(), str) || linearLayout.getChildCount() != extractWebUrl.size()) {
                linearLayout.setTag(str);
                while (linearLayout.getChildCount() > 0) {
                    linearLayout.removeViewAt(0);
                }
                linearLayout.setVisibility(0);
                for (final String str2 : extractWebUrl) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2.hashCode());
                    sb.append("---");
                    final String sb2 = sb.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str2);
                    sb3.append("<----->");
                    sb3.append(str);
                    String sb4 = sb3.toString();
                    if (!this.mSession.isURLPreviewEnabled()) {
                        if (!this.mUrlsPreviews.containsKey(sb2)) {
                            this.mUrlsPreviews.put(sb2, null);
                            this.mAdapter.notifyDataSetChanged();
                        }
                    } else if (UrlPreviewView.Companion.didUrlPreviewDismiss(sb4)) {
                        String str3 = LOG_TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("## manageURLPreviews() : ");
                        sb5.append(sb4);
                        sb5.append(" has been dismissed");
                        Log.d(str3, sb5.toString());
                    } else if (!this.mPendingUrls.contains(str2)) {
                        if (!this.mUrlsPreviews.containsKey(sb2)) {
                            this.mPendingUrls.add(str2);
                            this.mSession.getEventsApiClient().getURLPreview(str2, System.currentTimeMillis(), new ApiCallback<URLPreview>() {
                                public void onSuccess(URLPreview uRLPreview) {
                                    VectorMessagesAdapterHelper.this.mPendingUrls.remove(str2);
                                    if (!VectorMessagesAdapterHelper.this.mUrlsPreviews.containsKey(sb2)) {
                                        VectorMessagesAdapterHelper.this.mUrlsPreviews.put(sb2, uRLPreview);
                                        VectorMessagesAdapterHelper.this.mAdapter.notifyDataSetChanged();
                                    }
                                }

                                public void onNetworkError(Exception exc) {
                                    onSuccess((URLPreview) null);
                                }

                                public void onMatrixError(MatrixError matrixError) {
                                    onSuccess((URLPreview) null);
                                }

                                public void onUnexpectedError(Exception exc) {
                                    onSuccess((URLPreview) null);
                                }
                            });
                        } else {
                            UrlPreviewView urlPreviewView = new UrlPreviewView(this.mContext);
                            urlPreviewView.setUrlPreview(this.mContext, this.mSession, (URLPreview) this.mUrlsPreviews.get(sb2), sb4);
                            linearLayout.addView(urlPreviewView);
                        }
                    }
                }
            }
        }
    }
}
