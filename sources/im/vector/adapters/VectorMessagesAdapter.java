package im.vector.adapters;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.binaryfork.spanny.Spanny;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.media.AntiVirusScanStatus;
import fr.gouv.tchap.media.MediaScanManager;
import im.vector.VectorApp;
import im.vector.extensions.MatrixSdkExtensionsKt;
import im.vector.listeners.IMessagesAdapterActionsListener;
import im.vector.settings.VectorLocale;
import im.vector.ui.VectorQuoteSpan;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.EmojiKt;
import im.vector.util.EventGroup;
import im.vector.util.MatrixLinkMovementMethod;
import im.vector.util.MatrixURLSpan;
import im.vector.util.PreferencesManager;
import im.vector.util.RiotEventDisplay;
import im.vector.util.VectorImageGetter;
import im.vector.widgets.WidgetsManager;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.adapters.AbstractMessagesAdapter;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedEventContent;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.interfaces.HtmlToolbox;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.Event.SentState;
import org.matrix.androidsdk.rest.model.EventContent;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.StickerMessage;
import org.matrix.androidsdk.view.HtmlTagHandler;

public class VectorMessagesAdapter extends AbstractMessagesAdapter {
    private static final String LOG_TAG = VectorMessagesAdapter.class.getSimpleName();
    static final int NUM_ROW_TYPES = 14;
    static final int ROW_TYPE_CODE = 10;
    static final int ROW_TYPE_EMOJI = 9;
    static final int ROW_TYPE_EMOTE = 3;
    static final int ROW_TYPE_FILE = 4;
    static final int ROW_TYPE_HIDDEN = 7;
    static final int ROW_TYPE_IMAGE = 1;
    static final int ROW_TYPE_MEDIA_SCAN = 13;
    static final int ROW_TYPE_MERGE = 6;
    static final int ROW_TYPE_NOTICE = 2;
    static final int ROW_TYPE_ROOM_MEMBER = 8;
    static final int ROW_TYPE_STICKER = 11;
    static final int ROW_TYPE_TEXT = 0;
    static final int ROW_TYPE_VERSIONED_ROOM = 12;
    static final int ROW_TYPE_VIDEO = 5;
    private final boolean mAlwaysShowTimeStamps;
    protected BackgroundColorSpan mBackgroundColorSpan;
    private boolean mCanShowReadMarker;
    final Context mContext;
    private final int mDefaultMessageTextColor;
    /* access modifiers changed from: private */
    public Map<String, MXDeviceInfo> mE2eDeviceByEventId;
    private Map<String, Object> mE2eIconByEventId;
    private final int mEncryptingMessageTextColor;
    private final Map<String, String> mEventFormattedTsMap;
    private final List<EventGroup> mEventGroups;
    private final Map<String, MessageRow> mEventRowMap;
    private final Map<String, Integer> mEventType;
    protected final VectorMessagesAdapterHelper mHelper;
    private final Set<String> mHiddenEventIds;
    private final int mHighlightMessageTextColor;
    private String mHighlightedEventId;
    private HtmlToolbox mHtmlToolbox;
    /* access modifiers changed from: private */
    public VectorImageGetter mImageGetter;
    private boolean mIsPreviewMode;
    public boolean mIsRoomEncrypted;
    /* access modifiers changed from: private */
    public boolean mIsSearchMode;
    private boolean mIsUnreadViewMode;
    final LayoutInflater mLayoutInflater;
    private MatrixLinkMovementMethod mLinkMovementMethod;
    private List<MessageRow> mLiveMessagesRowList;
    private final Map<String, RoomMember> mLiveRoomMembers;
    private final Locale mLocale;
    private final int mMaxImageHeight;
    private final int mMaxImageWidth;
    private final MXMediaCache mMediaCache;
    protected MediaScanManager mMediaScanManager;
    private final VectorMessagesAdapterMediasHelper mMediasHelper;
    private List<Date> mMessagesDateList;
    private final int mNotSentMessageTextColor;
    private final Drawable mPadlockDrawable;
    private String mPattern;
    private String mReadMarkerEventId;
    /* access modifiers changed from: private */
    public ReadMarkerListener mReadMarkerListener;
    private String mReadReceiptEventId;
    private Date mReferenceDate;
    private final Map<Integer, Integer> mRowTypeToLayoutId;
    private String mSearchedEventId;
    /* access modifiers changed from: private */
    public Event mSelectedEvent;
    private final int mSendingMessageTextColor;
    final MXSession mSession;
    /* access modifiers changed from: private */
    public Set<String> mSessionIdsWaitingForE2eReRequest;
    private final boolean mShowReadReceipts;
    IMessagesAdapterActionsListener mVectorMessagesAdapterEventsListener;

    /* renamed from: im.vector.adapters.VectorMessagesAdapter$15 reason: invalid class name */
    static /* synthetic */ class AnonymousClass15 {
        static final /* synthetic */ int[] $SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus = new int[AntiVirusScanStatus.values().length];
        static final /* synthetic */ int[] $SwitchMap$org$matrix$androidsdk$rest$model$Event$SentState = new int[SentState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(11:0|1|2|3|(2:5|6)|7|9|10|11|12|14) */
        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0032 */
        static {
            try {
                $SwitchMap$org$matrix$androidsdk$rest$model$Event$SentState[SentState.SENDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$matrix$androidsdk$rest$model$Event$SentState[SentState.ENCRYPTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[AntiVirusScanStatus.IN_PROGRESS.ordinal()] = 1;
            $SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[AntiVirusScanStatus.INFECTED.ordinal()] = 2;
        }
    }

    public interface ReadMarkerListener {
        void onReadMarkerDisplayed(Event event, View view);
    }

    private static boolean isMergeableEvent(int i) {
        return (2 == i || 8 == i || 7 == i) ? false : true;
    }

    public int getViewTypeCount() {
        return 14;
    }

    public VectorMessagesAdapter(MXSession mXSession, Context context, MXMediaCache mXMediaCache) {
        this(mXSession, context, R.layout.adapter_item_vector_message_text_emote_notice, R.layout.adapter_item_vector_message_image_video, R.layout.adapter_item_vector_message_text_emote_notice, R.layout.adapter_item_vector_message_room_member, R.layout.adapter_item_vector_message_text_emote_notice, R.layout.adapter_item_vector_message_file, R.layout.adapter_item_vector_message_merge, R.layout.adapter_item_vector_message_image_video, R.layout.adapter_item_vector_message_emoji, R.layout.adapter_item_vector_message_code, R.layout.adapter_item_vector_message_image_video, R.layout.adapter_item_vector_message_redact, R.layout.adapter_item_vector_message_room_versioned, R.layout.adapter_item_tchap_media_scan, mXMediaCache);
    }

    VectorMessagesAdapter(MXSession mXSession, Context context, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, MXMediaCache mXMediaCache) {
        Context context2 = context;
        super(context, 0);
        this.mVectorMessagesAdapterEventsListener = null;
        this.mReferenceDate = new Date();
        this.mMessagesDateList = new ArrayList();
        this.mSearchedEventId = null;
        this.mHighlightedEventId = null;
        this.mEventFormattedTsMap = new HashMap();
        this.mE2eIconByEventId = new HashMap();
        this.mE2eDeviceByEventId = new HashMap();
        this.mSessionIdsWaitingForE2eReRequest = new HashSet();
        this.mRowTypeToLayoutId = new HashMap();
        this.mEventRowMap = new HashMap();
        this.mEventType = new HashMap();
        this.mIsSearchMode = false;
        this.mIsPreviewMode = false;
        this.mIsUnreadViewMode = false;
        this.mPattern = null;
        this.mLiveMessagesRowList = null;
        this.mHiddenEventIds = new HashSet();
        this.mLiveRoomMembers = new HashMap();
        this.mHtmlToolbox = new HtmlToolbox() {
            HtmlTagHandler mHtmlTagHandler;

            public String convert(String str) {
                String sanitisedHtml = VectorMessagesAdapter.this.mHelper.getSanitisedHtml(str);
                return sanitisedHtml != null ? sanitisedHtml : str;
            }

            public ImageGetter getImageGetter() {
                return VectorMessagesAdapter.this.mImageGetter;
            }

            public TagHandler getTagHandler(String str) {
                if (!(!str.contains("<table>"))) {
                    return null;
                }
                if (this.mHtmlTagHandler == null) {
                    this.mHtmlTagHandler = new HtmlTagHandler();
                    this.mHtmlTagHandler.mContext = VectorMessagesAdapter.this.mContext;
                    this.mHtmlTagHandler.setCodeBlockBackgroundColor(ThemeUtils.INSTANCE.getColor(VectorMessagesAdapter.this.mContext, R.attr.vctr_markdown_block_background_color));
                }
                return this.mHtmlTagHandler;
            }
        };
        this.mCanShowReadMarker = true;
        this.mEventGroups = new ArrayList();
        this.mContext = context2;
        this.mRowTypeToLayoutId.put(Integer.valueOf(0), Integer.valueOf(i));
        this.mRowTypeToLayoutId.put(Integer.valueOf(1), Integer.valueOf(i2));
        this.mRowTypeToLayoutId.put(Integer.valueOf(2), Integer.valueOf(i3));
        this.mRowTypeToLayoutId.put(Integer.valueOf(8), Integer.valueOf(i4));
        this.mRowTypeToLayoutId.put(Integer.valueOf(3), Integer.valueOf(i5));
        this.mRowTypeToLayoutId.put(Integer.valueOf(4), Integer.valueOf(i6));
        this.mRowTypeToLayoutId.put(Integer.valueOf(6), Integer.valueOf(i7));
        this.mRowTypeToLayoutId.put(Integer.valueOf(5), Integer.valueOf(i8));
        this.mRowTypeToLayoutId.put(Integer.valueOf(9), Integer.valueOf(i9));
        this.mRowTypeToLayoutId.put(Integer.valueOf(10), Integer.valueOf(i10));
        this.mRowTypeToLayoutId.put(Integer.valueOf(11), Integer.valueOf(i11));
        this.mRowTypeToLayoutId.put(Integer.valueOf(7), Integer.valueOf(i12));
        this.mRowTypeToLayoutId.put(Integer.valueOf(12), Integer.valueOf(i13));
        this.mRowTypeToLayoutId.put(Integer.valueOf(13), Integer.valueOf(i14));
        this.mMediaCache = mXMediaCache;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        setNotifyOnChange(false);
        this.mDefaultMessageTextColor = getDefaultMessageTextColor();
        this.mNotSentMessageTextColor = getNotSentMessageTextColor();
        this.mSendingMessageTextColor = getSendingMessageTextColor();
        this.mEncryptingMessageTextColor = getEncryptingMessageTextColor();
        this.mHighlightMessageTextColor = getHighlightMessageTextColor();
        this.mBackgroundColorSpan = new BackgroundColorSpan(getSearchHighlightMessageTextColor());
        Point point = new Point(0, 0);
        getScreenSize(point);
        int i15 = point.x;
        int i16 = point.y;
        if (i15 < i16) {
            this.mMaxImageWidth = Math.round(((float) i15) * 0.6f);
            this.mMaxImageHeight = Math.round(((float) i16) * 0.4f);
        } else {
            this.mMaxImageWidth = Math.round(((float) i15) * 0.4f);
            this.mMaxImageHeight = Math.round(((float) i16) * 0.6f);
        }
        this.mSession = mXSession;
        VectorMessagesAdapterMediasHelper vectorMessagesAdapterMediasHelper = new VectorMessagesAdapterMediasHelper(context, this.mSession, this.mMaxImageWidth, this.mMaxImageHeight, this.mNotSentMessageTextColor, this.mDefaultMessageTextColor);
        this.mMediasHelper = vectorMessagesAdapterMediasHelper;
        this.mHelper = new VectorMessagesAdapterHelper(context, this.mSession, this);
        this.mLocale = VectorLocale.INSTANCE.getApplicationLocale();
        this.mAlwaysShowTimeStamps = PreferencesManager.alwaysShowTimeStamps(VectorApp.getInstance());
        this.mShowReadReceipts = PreferencesManager.showReadReceipts(VectorApp.getInstance());
        ThemeUtils themeUtils = ThemeUtils.INSTANCE;
        Context context3 = this.mContext;
        this.mPadlockDrawable = themeUtils.tintDrawable(context3, ContextCompat.getDrawable(context3, R.drawable.e2e_unencrypted), R.attr.vctr_settings_icon_tint_color);
    }

    private void getScreenSize(Point point) {
        ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getSize(point);
    }

    public int getMaxThumbnailWidth() {
        return this.mMaxImageWidth;
    }

    public int getMaxThumbnailHeight() {
        return this.mMaxImageHeight;
    }

    private int getDefaultMessageTextColor() {
        return ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_message_text_color);
    }

    private int getNoticeTextColor() {
        return ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_notice_text_color);
    }

    private int getEncryptingMessageTextColor() {
        return ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_encrypting_message_text_color);
    }

    private int getSendingMessageTextColor() {
        return ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_sending_message_text_color);
    }

    private int getHighlightMessageTextColor() {
        return ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_highlighted_message_text_color);
    }

    private int getSearchHighlightMessageTextColor() {
        return ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_highlighted_searched_message_text_color);
    }

    private int getNotSentMessageTextColor() {
        return ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_unsent_message_text_color);
    }

    public void setMediaScanManager(MediaScanManager mediaScanManager) {
        this.mMediaScanManager = mediaScanManager;
    }

    /* access modifiers changed from: 0000 */
    public boolean supportMessageRowMerge(MessageRow messageRow) {
        return EventGroup.isSupported(messageRow);
    }

    public void addToFront(MessageRow messageRow) {
        if (isSupportedRow(messageRow)) {
            setNotifyOnChange(false);
            if (this.mIsSearchMode) {
                this.mLiveMessagesRowList.add(0, messageRow);
            } else {
                insert(messageRow, addToEventGroupToFront(messageRow) ? 1 : 0);
            }
            if (messageRow.getEvent().eventId != null) {
                this.mEventRowMap.put(messageRow.getEvent().eventId, messageRow);
            }
        }
    }

    public void remove(MessageRow messageRow) {
        if (messageRow == null) {
            return;
        }
        if (this.mIsSearchMode) {
            this.mLiveMessagesRowList.remove(messageRow);
            return;
        }
        removeFromEventGroup(messageRow);
        int position = getPosition(messageRow);
        super.remove(messageRow);
        checkEventGroupsMerge(messageRow, position);
    }

    public void add(MessageRow messageRow) {
        add(messageRow, true);
    }

    public void add(MessageRow messageRow, boolean z) {
        if (isSupportedRow(messageRow)) {
            setNotifyOnChange(false);
            if (this.mIsSearchMode) {
                this.mLiveMessagesRowList.add(messageRow);
            } else {
                addToEventGroup(messageRow);
                super.add(messageRow);
            }
            if (messageRow.getEvent().eventId != null) {
                this.mEventRowMap.put(messageRow.getEvent().eventId, messageRow);
            }
            if (this.mIsSearchMode || !z) {
                setNotifyOnChange(true);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public MessageRow getMessageRow(String str) {
        if (str != null) {
            return (MessageRow) this.mEventRowMap.get(str);
        }
        return null;
    }

    public MessageRow getClosestRow(Event event) {
        if (event == null) {
            return null;
        }
        return getClosestRowFromTs(event.eventId, event.getOriginServerTs());
    }

    public MessageRow getClosestRowFromTs(String str, long j) {
        MessageRow messageRow = getMessageRow(str);
        if (messageRow == null) {
            for (MessageRow messageRow2 : new ArrayList(this.mEventRowMap.values())) {
                if (!(messageRow2.getEvent() instanceof EventGroup)) {
                    long originServerTs = messageRow2.getEvent().getOriginServerTs();
                    if (originServerTs > j) {
                        if (messageRow != null) {
                            if (originServerTs < messageRow.getEvent().getOriginServerTs()) {
                                String str2 = LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## getClosestRowFromTs() ");
                                sb.append(messageRow2.getEvent().eventId);
                                Log.d(str2, sb.toString());
                            }
                        }
                        messageRow = messageRow2;
                    }
                }
            }
        }
        return messageRow;
    }

    public MessageRow getClosestRowBeforeTs(String str, long j) {
        MessageRow messageRow = getMessageRow(str);
        if (messageRow == null) {
            for (MessageRow messageRow2 : new ArrayList(this.mEventRowMap.values())) {
                if (!(messageRow2.getEvent() instanceof EventGroup)) {
                    long originServerTs = messageRow2.getEvent().getOriginServerTs();
                    if (originServerTs < j) {
                        if (messageRow != null) {
                            if (originServerTs > messageRow.getEvent().getOriginServerTs()) {
                                String str2 = LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## getClosestRowBeforeTs() ");
                                sb.append(messageRow2.getEvent().eventId);
                                Log.d(str2, sb.toString());
                            }
                        }
                        messageRow = messageRow2;
                    }
                }
            }
        }
        return messageRow;
    }

    public void updateEventById(Event event, String str) {
        if (((MessageRow) this.mEventRowMap.get(event.eventId)) == null) {
            MessageRow messageRow = (MessageRow) this.mEventRowMap.get(str);
            if (messageRow != null) {
                this.mEventRowMap.remove(str);
                this.mEventRowMap.put(event.eventId, messageRow);
            }
        } else {
            removeEventById(str);
        }
        notifyDataSetChanged();
    }

    public void removeEventById(String str) {
        setNotifyOnChange(false);
        MessageRow messageRow = (MessageRow) this.mEventRowMap.get(str);
        if (messageRow != null) {
            remove(messageRow);
            this.mEventRowMap.remove(str);
        }
    }

    public void setIsPreviewMode(boolean z) {
        this.mIsPreviewMode = z;
    }

    public void setIsUnreadViewMode(boolean z) {
        this.mIsUnreadViewMode = z;
    }

    public boolean isUnreadViewMode() {
        return this.mIsUnreadViewMode;
    }

    public void setSearchPattern(String str) {
        if (!TextUtils.equals(str, this.mPattern)) {
            this.mPattern = str;
            this.mIsSearchMode = !TextUtils.isEmpty(this.mPattern);
            if (this.mIsSearchMode) {
                if (this.mLiveMessagesRowList == null) {
                    this.mLiveMessagesRowList = new ArrayList();
                    for (int i = 0; i < getCount(); i++) {
                        this.mLiveMessagesRowList.add(getItem(i));
                    }
                }
            } else if (this.mLiveMessagesRowList != null) {
                clear();
                addAll(this.mLiveMessagesRowList);
                this.mLiveMessagesRowList = null;
            }
        }
    }

    public void clear() {
        super.clear();
        if (!this.mIsSearchMode) {
            this.mEventRowMap.clear();
        }
    }

    public int getItemViewType(int i) {
        if (i >= getCount()) {
            return 0;
        }
        return getItemViewType(((MessageRow) getItem(i)).getEvent());
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        if (i >= getCount()) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getView() : invalid index ");
            sb.append(i);
            sb.append(" >= ");
            sb.append(getCount());
            Log.e(str, sb.toString());
            if (view == null) {
                view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(0))).intValue(), viewGroup, false);
            }
            IMessagesAdapterActionsListener iMessagesAdapterActionsListener = this.mVectorMessagesAdapterEventsListener;
            if (iMessagesAdapterActionsListener != null) {
                iMessagesAdapterActionsListener.onInvalidIndexes();
            }
            return view;
        }
        int itemViewType = getItemViewType(i);
        if (!(view == null || itemViewType == ((Integer) view.getTag()).intValue())) {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## getView() : invalid view type : got ");
            sb2.append(view.getTag());
            sb2.append(" instead of ");
            sb2.append(itemViewType);
            Log.e(str2, sb2.toString());
            view = null;
        }
        switch (itemViewType) {
            case 0:
            case 9:
            case 10:
                view2 = getTextView(itemViewType, i, view, viewGroup);
                break;
            case 1:
            case 5:
            case 11:
                view2 = getImageVideoView(itemViewType, i, view, viewGroup);
                break;
            case 2:
            case 8:
                view2 = getNoticeRoomMemberView(itemViewType, i, view, viewGroup);
                break;
            case 3:
                view2 = getEmoteView(i, view, viewGroup);
                break;
            case 4:
                view2 = getFileView(i, view, viewGroup);
                break;
            case 6:
                view2 = getMergeView(i, view, viewGroup);
                break;
            case 7:
                view2 = getHiddenView(i, view, viewGroup);
                break;
            case 12:
                view2 = getVersionedRoomView(i, view, viewGroup);
                break;
            case 13:
                view2 = getMediaScanView(i, view, viewGroup);
                break;
            default:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown item view type for position ");
                sb3.append(i);
                throw new RuntimeException(sb3.toString());
        }
        if (this.mReadMarkerListener != null) {
            handleReadMarker(view2, i);
        }
        if (view2 != null) {
            view2.setBackgroundColor(0);
            view2.setTag(Integer.valueOf(itemViewType));
            displayE2eIcon(view2, i);
            displayE2eReRequest(view2, i);
        }
        return view2;
    }

    public void notifyDataSetChanged() {
        int i = 0;
        setNotifyOnChange(false);
        ArrayList arrayList = new ArrayList();
        while (i < getCount()) {
            MessageRow messageRow = (MessageRow) getItem(i);
            Event event = messageRow.getEvent();
            if (event != null && (event.isUndelivered() || event.isUnknownDevice())) {
                arrayList.add(messageRow);
                remove(messageRow);
                i--;
            }
            i++;
        }
        if (arrayList.size() > 0) {
            try {
                Collections.sort(arrayList, new Comparator<MessageRow>() {
                    public int compare(MessageRow messageRow, MessageRow messageRow2) {
                        long originServerTs = messageRow.getEvent().getOriginServerTs() - messageRow2.getEvent().getOriginServerTs();
                        if (originServerTs > 0) {
                            return 1;
                        }
                        return originServerTs < 0 ? -1 : 0;
                    }
                });
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## notifyDataSetChanged () : failed to sort undeliverableEvents ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
            addAll(arrayList);
        }
        setNotifyOnChange(true);
        refreshRefreshDateList();
        manageCryptoEvents();
        if (!VectorApp.isAppInBackground()) {
            super.notifyDataSetChanged();
        }
    }

    public void setLiveRoomMembers(List<RoomMember> list) {
        this.mLiveRoomMembers.clear();
        for (RoomMember roomMember : list) {
            this.mLiveRoomMembers.put(roomMember.getUserId(), roomMember);
        }
        notifyDataSetChanged();
    }

    public void onBingRulesUpdate() {
        notifyDataSetChanged();
    }

    public void onPause() {
        this.mEventFormattedTsMap.clear();
    }

    public void onEventTap(Event event) {
        if (!this.mIsSearchMode) {
            if (this.mSelectedEvent == null) {
                this.mSelectedEvent = event;
            } else {
                this.mSelectedEvent = null;
            }
            notifyDataSetChanged();
            IMessagesAdapterActionsListener iMessagesAdapterActionsListener = this.mVectorMessagesAdapterEventsListener;
            if (iMessagesAdapterActionsListener != null) {
                iMessagesAdapterActionsListener.onSelectedEventChange(this.mSelectedEvent);
            }
        }
    }

    public void setSearchedEventId(String str) {
        this.mSearchedEventId = str;
        updateHighlightedEventId();
    }

    public void cancelSelectionMode() {
        if (this.mSelectedEvent != null) {
            this.mSelectedEvent = null;
            notifyDataSetChanged();
            IMessagesAdapterActionsListener iMessagesAdapterActionsListener = this.mVectorMessagesAdapterEventsListener;
            if (iMessagesAdapterActionsListener != null) {
                iMessagesAdapterActionsListener.onSelectedEventChange(this.mSelectedEvent);
            }
        }
    }

    public boolean isInSelectionMode() {
        return this.mSelectedEvent != null;
    }

    public Event getCurrentSelectedEvent() {
        return this.mSelectedEvent;
    }

    public void setVectorMessagesAdapterActionsListener(IMessagesAdapterActionsListener iMessagesAdapterActionsListener) {
        this.mVectorMessagesAdapterEventsListener = iMessagesAdapterActionsListener;
        this.mMediasHelper.setVectorMessagesAdapterActionsListener(iMessagesAdapterActionsListener);
        this.mHelper.setVectorMessagesAdapterActionsListener(iMessagesAdapterActionsListener);
        MatrixLinkMovementMethod matrixLinkMovementMethod = this.mLinkMovementMethod;
        if (matrixLinkMovementMethod != null) {
            matrixLinkMovementMethod.updateListener(iMessagesAdapterActionsListener);
        } else if (iMessagesAdapterActionsListener != null) {
            this.mLinkMovementMethod = new MatrixLinkMovementMethod(iMessagesAdapterActionsListener);
        }
        this.mHelper.setLinkMovementMethod(this.mLinkMovementMethod);
    }

    public MXDeviceInfo getDeviceInfo(String str) {
        if (str != null) {
            return (MXDeviceInfo) this.mE2eDeviceByEventId.get(str);
        }
        return null;
    }

    private int getItemViewType(Event event) {
        String str = event.eventId;
        String type = event.getType();
        if (str != null && this.mHiddenEventIds.contains(str)) {
            return 7;
        }
        if ("m.room.encrypted".equals(type)) {
            return 0;
        }
        if (event instanceof EventGroup) {
            return 6;
        }
        if (this.mMediaScanManager.isUncheckedOrUntrustedMediaEvent(event)) {
            return 13;
        }
        if (str != null) {
            Integer num = (Integer) this.mEventType.get(str);
            if (num != null) {
                return num.intValue();
            }
        }
        int i = 8;
        if (Event.EVENT_TYPE_MESSAGE.equals(type)) {
            Message message = JsonUtils.toMessage(event.getContent());
            String str2 = message.msgtype;
            if (Message.MSGTYPE_TEXT.equals(str2)) {
                if (EmojiKt.containsOnlyEmojis(message.body)) {
                    i = 9;
                } else if (!TextUtils.isEmpty(message.formatted_body) && this.mHelper.containsFencedCodeBlocks(message)) {
                    i = 10;
                }
            } else if (Message.MSGTYPE_IMAGE.equals(str2)) {
                i = 1;
            } else if (Message.MSGTYPE_EMOTE.equals(str2)) {
                i = 3;
            } else if (Message.MSGTYPE_NOTICE.equals(str2)) {
                i = 2;
            } else if (Message.MSGTYPE_FILE.equals(str2) || Message.MSGTYPE_AUDIO.equals(str2)) {
                i = 4;
            } else if (Message.MSGTYPE_VIDEO.equals(str2)) {
                i = 5;
            }
            i = 0;
        } else if (Event.EVENT_TYPE_STICKER.equals(type)) {
            i = 11;
        } else if (!event.isCallEvent() && !Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY.equals(type) && !Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type) && !"m.room.member".equals(type) && !Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type) && !Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type) && !"m.room.encryption".equals(type)) {
            if (WidgetsManager.WIDGET_EVENT_TYPE.equals(type)) {
                return 8;
            }
            if (Event.EVENT_TYPE_STATE_ROOM_CREATE.equals(type)) {
                i = 12;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown event type: ");
                sb.append(type);
                throw new RuntimeException(sb.toString());
            }
        }
        if (str != null) {
            this.mEventType.put(str, Integer.valueOf(i));
        }
        return i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ed A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x010d  */
    private void manageSubView(final int i, View view, View view2, int i2) {
        boolean z;
        boolean z2;
        TextView timestampValue;
        View findViewById;
        int i3;
        MessageRow messageRow = (MessageRow) getItem(i);
        view.setClickable(true);
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener != null) {
                    VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener.onRowClick(i);
                }
            }
        });
        view.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                return VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener != null && VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener.onRowLongClick(i);
            }
        });
        Event event = messageRow.getEvent();
        int i4 = 0;
        if (this.mIsSearchMode || !isMergeableEvent(i2)) {
            z2 = false;
        } else {
            if (i > 0) {
                Event event2 = ((MessageRow) getItem(i - 1)).getEvent();
                if (isMergeableEvent(getItemViewType(event2)) && TextUtils.equals(event2.getSender(), event.getSender())) {
                    z2 = true;
                    i3 = i + 1;
                    if (i3 < getCount()) {
                        Event event3 = ((MessageRow) getItem(i3)).getEvent();
                        if (isMergeableEvent(getItemViewType(event3)) && TextUtils.equals(event3.getSender(), event.getSender())) {
                            z = true;
                            boolean mergeView = mergeView(event, i, z2);
                            this.mHelper.setSenderValue(view, messageRow, mergeView);
                            timestampValue = VectorMessagesAdapterHelper.setTimestampValue(view, getFormattedTimestamp(event));
                            if (timestampValue != null) {
                                if (messageRow.getEvent().isUndelivered() || messageRow.getEvent().isUnknownDevice()) {
                                    timestampValue.setTextColor(this.mNotSentMessageTextColor);
                                } else {
                                    timestampValue.setTextColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_default_text_light_color));
                                }
                                timestampValue.setVisibility((i + 1 == getCount() || this.mIsSearchMode || this.mAlwaysShowTimeStamps) ? 0 : 8);
                            }
                            View senderAvatar = this.mHelper.setSenderAvatar(view, messageRow, mergeView);
                            View findViewById2 = view.findViewById(R.id.messagesAdapter_body_layout);
                            findViewById = view.findViewById(R.id.messagesAdapter_message_separator);
                            if (findViewById != null) {
                                if (z || i + 1 == getCount()) {
                                    i4 = 8;
                                }
                                findViewById.setVisibility(i4);
                            }
                            VectorMessagesAdapterHelper.setHeader(view, headerMessage(i), i);
                            if (this.mShowReadReceipts) {
                                this.mHelper.hideReadReceipts(view);
                            } else {
                                this.mHelper.displayReadReceipts(view, messageRow, this.mIsPreviewMode, this.mLiveRoomMembers);
                            }
                            manageSelectionMode(view, event, i2);
                            setReadMarker(view, messageRow, mergeView, senderAvatar, findViewById2);
                            if (1 != i2 || 4 == i2 || 5 == i2 || 11 == i2) {
                                VectorMessagesAdapterHelper.setMediaProgressLayout(view, findViewById2);
                            }
                            return;
                        }
                    }
                }
            }
            z2 = false;
            i3 = i + 1;
            if (i3 < getCount()) {
            }
        }
        z = false;
        boolean mergeView2 = mergeView(event, i, z2);
        this.mHelper.setSenderValue(view, messageRow, mergeView2);
        timestampValue = VectorMessagesAdapterHelper.setTimestampValue(view, getFormattedTimestamp(event));
        if (timestampValue != null) {
        }
        View senderAvatar2 = this.mHelper.setSenderAvatar(view, messageRow, mergeView2);
        View findViewById22 = view.findViewById(R.id.messagesAdapter_body_layout);
        findViewById = view.findViewById(R.id.messagesAdapter_message_separator);
        if (findViewById != null) {
        }
        VectorMessagesAdapterHelper.setHeader(view, headerMessage(i), i);
        if (this.mShowReadReceipts) {
        }
        manageSelectionMode(view, event, i2);
        setReadMarker(view, messageRow, mergeView2, senderAvatar2, findViewById22);
        if (1 != i2) {
        }
        VectorMessagesAdapterHelper.setMediaProgressLayout(view, findViewById22);
    }

    private View getTextView(int i, int i2, View view, ViewGroup viewGroup) {
        List<TextView> list;
        int i3;
        boolean z = false;
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(i))).intValue(), viewGroup, false);
        }
        try {
            MessageRow messageRow = (MessageRow) getItem(i2);
            Event event = messageRow.getEvent();
            Message message = JsonUtils.toMessage(event.getContent());
            if (this.mVectorMessagesAdapterEventsListener != null && this.mVectorMessagesAdapterEventsListener.shouldHighlightEvent(event)) {
                z = true;
            }
            if (10 == i) {
                list = populateRowTypeCode(message, view, z);
            } else {
                TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_body);
                if (textView == null) {
                    Log.e(LOG_TAG, "getTextView : invalid layout");
                    return view;
                }
                textView.setText(this.mHelper.highlightPattern(messageRow.getText(new VectorQuoteSpan(this.mContext), new RiotEventDisplay(this.mContext, this.mHtmlToolbox)), this.mPattern, this.mBackgroundColorSpan, z));
                this.mHelper.applyLinkMovementMethod(textView);
                List arrayList = new ArrayList();
                arrayList.add(textView);
                list = arrayList;
            }
            if (messageRow.getEvent().isEncrypting()) {
                i3 = this.mEncryptingMessageTextColor;
            } else {
                if (!messageRow.getEvent().isSending()) {
                    if (!messageRow.getEvent().isUnsent()) {
                        if (!messageRow.getEvent().isUndelivered()) {
                            if (!messageRow.getEvent().isUnknownDevice()) {
                                i3 = z ? this.mHighlightMessageTextColor : this.mDefaultMessageTextColor;
                            }
                        }
                        i3 = this.mNotSentMessageTextColor;
                    }
                }
                i3 = this.mSendingMessageTextColor;
            }
            for (TextView textColor : list) {
                textColor.setTextColor(i3);
            }
            manageSubView(i2, view, view.findViewById(R.id.messagesAdapter_text_layout), i);
            for (TextView addContentViewListeners : list) {
                addContentViewListeners(view, addContentViewListeners, i2, i);
            }
            this.mHelper.manageURLPreviews(message, view, event.eventId);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getTextView() failed : ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
        return view;
    }

    private List<TextView> populateRowTypeCode(Message message, View view, boolean z) {
        String[] fencedCodeBlocks;
        ArrayList arrayList = new ArrayList();
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.messages_container);
        linearLayout.removeAllViews();
        for (String str : this.mHelper.getFencedCodeBlocks(message)) {
            if (!TextUtils.isEmpty(str)) {
                if (!str.startsWith(VectorMessagesAdapterHelper.START_FENCED_BLOCK) || !str.endsWith(VectorMessagesAdapterHelper.END_FENCED_BLOCK)) {
                    TextView textView = (TextView) this.mLayoutInflater.inflate(R.layout.adapter_item_vector_message_code_text, null);
                    String trim = str.trim();
                    if (TextUtils.equals(Message.FORMAT_MATRIX_HTML, message.format)) {
                        String sanitisedHtml = this.mHelper.getSanitisedHtml(trim);
                        if (sanitisedHtml != null) {
                            trim = sanitisedHtml;
                        }
                    }
                    textView.setText(this.mHelper.highlightPattern(new SpannableString(this.mHelper.convertToHtml(trim)), this.mPattern, this.mBackgroundColorSpan, z));
                    this.mHelper.applyLinkMovementMethod(textView);
                    linearLayout.addView(textView);
                    arrayList.add(textView);
                } else {
                    CharSequence convertToHtml = this.mHelper.convertToHtml(str.substring(11, str.length() - 13).replace("\n", "<br/>").replace(" ", "&nbsp;").trim());
                    View inflate = this.mLayoutInflater.inflate(R.layout.adapter_item_vector_message_code_block, null);
                    TextView textView2 = (TextView) inflate.findViewById(R.id.messagesAdapter_body);
                    textView2.setText(convertToHtml);
                    this.mHelper.highlightFencedCode(textView2);
                    this.mHelper.applyLinkMovementMethod(textView2);
                    linearLayout.addView(inflate);
                    arrayList.add(textView2);
                    ((View) textView2.getParent()).setBackgroundColor(ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_markdown_block_background_color));
                }
            }
        }
        return arrayList;
    }

    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [org.matrix.androidsdk.rest.model.message.Message] */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v4, types: [org.matrix.androidsdk.rest.model.message.StickerMessage] */
    /* JADX WARNING: type inference failed for: r1v6, types: [org.matrix.androidsdk.rest.model.message.VideoMessage] */
    /* JADX WARNING: type inference failed for: r1v8, types: [org.matrix.androidsdk.rest.model.message.ImageMessage] */
    /* JADX WARNING: type inference failed for: r1v13 */
    /* JADX WARNING: type inference failed for: r1v14 */
    /* JADX WARNING: type inference failed for: r1v15 */
    /* JADX WARNING: type inference failed for: r1v16 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v2
  assigns: []
  uses: []
  mth insns count: 70
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
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 5 */
    private View getImageVideoView(int i, int i2, View view, ViewGroup viewGroup) {
        ? r1;
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(i))).intValue(), viewGroup, false);
        }
        try {
            Event event = ((MessageRow) getItem(i2)).getEvent();
            ? r12 = 0;
            boolean z = true;
            if (i == 1) {
                ? imageMessage = JsonUtils.toImageMessage(event.getContent());
                z = imageMessage.getMimeType().equals("image/gif");
                r1 = imageMessage;
            } else if (i == 5) {
                r1 = JsonUtils.toVideoMessage(event.getContent());
            } else {
                if (i == 11) {
                    r12 = JsonUtils.toStickerMessage(event.getContent());
                }
                z = false;
                r1 = r12;
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.messagesAdapter_play_circle);
            if (imageView == null) {
                Log.e(LOG_TAG, "getImageVideoView : invalid layout");
                return view;
            }
            imageView.setVisibility(8);
            if (z) {
                imageView.setVisibility(0);
            }
            if (r1 != 0) {
                this.mHelper.hideStickerDescription(view);
                this.mMediasHelper.managePendingImageVideoDownload(view, event, r1, i2);
                this.mMediasHelper.managePendingImageVideoUpload(view, event, r1);
            }
            View findViewById = view.findViewById(R.id.messagesAdapter_image_layout);
            findViewById.setAlpha(event.isSent() ? 1.0f : 0.5f);
            manageSubView(i2, view, findViewById, i);
            addContentViewListeners(view, (ImageView) view.findViewById(R.id.messagesAdapter_image), i2, i);
            return view;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getImageVideoView() failed : ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    private View getNoticeRoomMemberView(int i, int i2, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(i))).intValue(), viewGroup, false);
        }
        try {
            MessageRow messageRow = (MessageRow) getItem(i2);
            Event event = messageRow.getEvent();
            Spannable text = messageRow.getText(null, new RiotEventDisplay(this.mContext));
            TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_body);
            if (textView == null) {
                Log.e(LOG_TAG, "getNoticeRoomMemberView : invalid layout");
                return view;
            }
            if (TextUtils.isEmpty(text)) {
                textView.setText("");
            } else {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                MatrixURLSpan.refreshMatrixSpans(spannableStringBuilder, this.mVectorMessagesAdapterEventsListener);
                textView.setText(spannableStringBuilder);
            }
            manageSubView(i2, view, view.findViewById(R.id.messagesAdapter_text_layout), i);
            addContentViewListeners(view, textView, i2, i);
            textView.setAlpha(1.0f);
            textView.setTextColor(getNoticeTextColor());
            this.mHelper.manageURLPreviews(JsonUtils.toMessage(event.getContent()), view, event.eventId);
            return view;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getNoticeRoomMemberView() failed : ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    private View getEmoteView(int i, View view, ViewGroup viewGroup) {
        int i2;
        String str = " ";
        String str2 = "* ";
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(3))).intValue(), viewGroup, false);
        }
        try {
            MessageRow messageRow = (MessageRow) getItem(i);
            Event event = messageRow.getEvent();
            TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_body);
            if (textView == null) {
                Log.e(LOG_TAG, "getEmoteView : invalid layout");
                return view;
            }
            Message message = JsonUtils.toMessage(event.getContent());
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(messageRow.getSenderDisplayName());
            sb.append(str);
            sb.append(message.body);
            CharSequence sb2 = sb.toString();
            if (TextUtils.equals(Message.FORMAT_MATRIX_HTML, message.format)) {
                String sanitisedHtml = this.mHelper.getSanitisedHtml(message.formatted_body);
                if (sanitisedHtml != null) {
                    sb2 = TextUtils.concat(new CharSequence[]{str2, messageRow.getSenderDisplayName(), str, this.mHelper.convertToHtml(sanitisedHtml)});
                }
            }
            textView.setText(this.mHelper.highlightPattern(new SpannableString(sb2), null, this.mBackgroundColorSpan, false));
            this.mHelper.applyLinkMovementMethod(textView);
            if (messageRow.getEvent().isEncrypting()) {
                i2 = this.mEncryptingMessageTextColor;
            } else {
                if (!messageRow.getEvent().isSending()) {
                    if (!messageRow.getEvent().isUnsent()) {
                        if (!messageRow.getEvent().isUndelivered()) {
                            if (!messageRow.getEvent().isUnknownDevice()) {
                                i2 = this.mDefaultMessageTextColor;
                            }
                        }
                        i2 = this.mNotSentMessageTextColor;
                    }
                }
                i2 = this.mSendingMessageTextColor;
            }
            textView.setTextColor(i2);
            manageSubView(i, view, view.findViewById(R.id.messagesAdapter_text_layout), 3);
            addContentViewListeners(view, textView, i, 3);
            this.mHelper.manageURLPreviews(message, view, event.eventId);
            return view;
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## getEmoteView() failed : ");
            sb3.append(e.getMessage());
            Log.e(str3, sb3.toString(), e);
        }
    }

    private View getFileView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(4))).intValue(), viewGroup, false);
        }
        try {
            Event event = ((MessageRow) getItem(i)).getEvent();
            FileMessage fileMessage = JsonUtils.toFileMessage(event.getContent());
            TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_filename);
            if (textView == null) {
                Log.e(LOG_TAG, "getFileView : invalid layout");
                return view;
            }
            textView.setPaintFlags(textView.getPaintFlags() | 8);
            textView.setText(fileMessage.body);
            ImageView imageView = (ImageView) view.findViewById(R.id.messagesAdapter_image_type);
            if (imageView != null) {
                imageView.setImageResource(Message.MSGTYPE_AUDIO.equals(fileMessage.msgtype) ? R.drawable.filetype_audio : R.drawable.filetype_attachment);
                imageView.setBackgroundColor(0);
            }
            this.mMediasHelper.managePendingFileDownload(view, event, fileMessage, i);
            this.mMediasHelper.managePendingUpload(view, event, 4, fileMessage.url);
            manageSubView(i, view, view.findViewById(R.id.messagesAdapter_file_layout), 4);
            addContentViewListeners(view, textView, i, 4);
            return view;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getFileView() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    private View getHiddenView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(7))).intValue(), viewGroup, false);
        }
        VectorMessagesAdapterHelper.setHeader(view, headerMessage(i), i);
        return view;
    }

    private View getMergeView(int i, View view, ViewGroup viewGroup) {
        float f;
        boolean z = false;
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(6))).intValue(), viewGroup, false);
        }
        try {
            final EventGroup eventGroup = (EventGroup) ((MessageRow) getItem(i)).getEvent();
            View findViewById = view.findViewById(R.id.messagesAdapter_merge_header_layout);
            TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_merge_header_text_view);
            TextView textView2 = (TextView) view.findViewById(R.id.messagesAdapter_merge_summary);
            View findViewById2 = view.findViewById(R.id.messagesAdapter_merge_separator);
            View findViewById3 = view.findViewById(R.id.messagesAdapter_merge_avatar_list);
            if (!(findViewById == null || textView == null || textView2 == null || findViewById2 == null)) {
                if (findViewById3 != null) {
                    findViewById2.setVisibility(eventGroup.isExpanded() ? 0 : 8);
                    textView2.setVisibility(eventGroup.isExpanded() ? 8 : 0);
                    findViewById3.setVisibility(eventGroup.isExpanded() ? 8 : 0);
                    textView.setText(eventGroup.isExpanded() ? R.string.merged_events_collapse : R.string.merged_events_expand);
                    if (!eventGroup.isExpanded()) {
                        findViewById3.setVisibility(0);
                        ArrayList arrayList = new ArrayList();
                        arrayList.add((ImageView) view.findViewById(R.id.mels_list_avatar_1));
                        arrayList.add((ImageView) view.findViewById(R.id.mels_list_avatar_2));
                        arrayList.add((ImageView) view.findViewById(R.id.mels_list_avatar_3));
                        arrayList.add((ImageView) view.findViewById(R.id.mels_list_avatar_4));
                        arrayList.add((ImageView) view.findViewById(R.id.mels_list_avatar_5));
                        List avatarRows = eventGroup.getAvatarRows(arrayList.size());
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            ImageView imageView = (ImageView) arrayList.get(i2);
                            if (i2 < avatarRows.size()) {
                                this.mHelper.loadMemberAvatar(imageView, (MessageRow) avatarRows.get(i2));
                                imageView.setVisibility(0);
                            } else {
                                imageView.setVisibility(8);
                            }
                        }
                        textView2.setText(eventGroup.toString(this.mContext));
                    }
                    findViewById.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            EventGroup eventGroup = eventGroup;
                            eventGroup.setIsExpanded(!eventGroup.isExpanded());
                            VectorMessagesAdapter.this.updateHighlightedEventId();
                            if (VectorMessagesAdapter.this.mSelectedEvent == null || !eventGroup.contains(VectorMessagesAdapter.this.mSelectedEvent.eventId)) {
                                VectorMessagesAdapter.this.notifyDataSetChanged();
                            } else {
                                VectorMessagesAdapter.this.cancelSelectionMode();
                            }
                        }
                    });
                    view.findViewById(R.id.messagesAdapter_highlight_message_marker).setBackgroundColor(ContextCompat.getColor(this.mContext, TextUtils.equals(this.mHighlightedEventId, eventGroup.eventId) ? R.color.vector_green_color : 17170445));
                    VectorMessagesAdapterHelper.setHeader(view, headerMessage(i), i);
                    boolean z2 = this.mSelectedEvent != null;
                    if (z2 && TextUtils.equals(eventGroup.eventId, this.mSelectedEvent.eventId)) {
                        z = true;
                    }
                    if (z2) {
                        if (!z) {
                            f = 0.2f;
                            view.findViewById(R.id.messagesAdapter_body_view).setAlpha(f);
                            return view;
                        }
                    }
                    f = 1.0f;
                    view.findViewById(R.id.messagesAdapter_body_view).setAlpha(f);
                    return view;
                }
            }
            Log.e(LOG_TAG, "getMergeView : invalid layout");
            return view;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getMergeView() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    private View getVersionedRoomView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(12))).intValue(), viewGroup, false);
        }
        MatrixURLSpan matrixURLSpan = new MatrixURLSpan(PermalinkUtils.createPermalink(((MessageRow) getItem(i)).getRoomCreateContentPredecessor().roomId), MXPatterns.PATTERN_CONTAIN_APP_LINK_PERMALINK_ROOM_ID, this.mVectorMessagesAdapterEventsListener);
        int color = ContextCompat.getColor(this.mContext, R.color.riot_primary_text_color_light);
        Spanny append = new Spanny((CharSequence) this.mContext.getString(R.string.room_tombstone_continuation_description), new StyleSpan(1), new ForegroundColorSpan(color)).append((CharSequence) "\n").append((CharSequence) this.mContext.getString(R.string.room_tombstone_predecessor_link), matrixURLSpan, new ForegroundColorSpan(color));
        TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_room_versioned_text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(append);
        return view;
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0125 A[Catch:{ Exception -> 0x018a }, LOOP:1: B:30:0x00df->B:41:0x0125, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0159 A[Catch:{ Exception -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0160 A[Catch:{ Exception -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0172 A[Catch:{ Exception -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0175 A[Catch:{ Exception -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00c3 A[EDGE_INSN: B:63:0x00c3->B:26:0x00c3 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0124 A[SYNTHETIC] */
    private View getMediaScanView(int i, View view, ViewGroup viewGroup) {
        int i2;
        int i3;
        View inflate = view == null ? this.mLayoutInflater.inflate(((Integer) this.mRowTypeToLayoutId.get(Integer.valueOf(13))).intValue(), viewGroup, false) : view;
        try {
            TextView textView = (TextView) inflate.findViewById(R.id.messagesAdapter_filename);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.messagesAdapter_media_scan_icon);
            TextView textView2 = (TextView) inflate.findViewById(R.id.messagesAdapter_media_scan_text);
            if (!(textView == null || imageView == null)) {
                if (textView2 != null) {
                    Event event = ((MessageRow) getItem(i)).getEvent();
                    String str = ((Message) JsonUtils.toClass(event.getContent(), Message.class)).body;
                    if (this.mMediaScanManager != null) {
                        if (event.isEncrypted()) {
                            int i4 = R.drawable.ic_notification_privacy_warning;
                            int i5 = R.string.tchap_scan_media_unavailable;
                            boolean z = false;
                            for (EncryptedFileInfo scanEncryptedMedia : event.getEncryptedFileInfos()) {
                                int i6 = AnonymousClass15.$SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[this.mMediaScanManager.scanEncryptedMedia(scanEncryptedMedia).getAntiVirusScanStatus().ordinal()];
                                if (i6 == 1) {
                                    i4 = R.drawable.tchap_scanning;
                                    i5 = R.string.tchap_scan_media_in_progress;
                                } else if (i6 != 2) {
                                    continue;
                                    if (z) {
                                        break;
                                    }
                                } else {
                                    str = this.mContext.getResources().getString(R.string.tchap_scan_media_untrusted_content_message, new Object[]{str});
                                    i4 = R.drawable.tchap_danger;
                                    i5 = R.string.tchap_scan_media_untrusted_content;
                                }
                                z = true;
                                continue;
                                if (z) {
                                }
                            }
                            i3 = i4;
                            i2 = i5;
                        } else if (event.mSentState == SentState.SENT) {
                            Iterator it = event.getMediaUrls().iterator();
                            int i7 = R.drawable.ic_notification_privacy_warning;
                            int i8 = R.string.tchap_scan_media_unavailable;
                            boolean z2 = false;
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                int i9 = AnonymousClass15.$SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[this.mMediaScanManager.scanUnencryptedMedia((String) it.next()).getAntiVirusScanStatus().ordinal()];
                                if (i9 == 1) {
                                    i7 = R.drawable.tchap_scanning;
                                    i8 = R.string.tchap_scan_media_in_progress;
                                } else if (i9 == 2) {
                                    str = this.mContext.getResources().getString(R.string.tchap_scan_media_untrusted_content_message, new Object[]{str});
                                    i7 = R.drawable.tchap_danger;
                                    i8 = R.string.tchap_scan_media_untrusted_content;
                                } else if (!z2) {
                                    break;
                                }
                                z2 = true;
                                if (!z2) {
                                }
                            }
                            i3 = i7;
                            i2 = i8;
                        } else {
                            int i10 = AnonymousClass15.$SwitchMap$org$matrix$androidsdk$rest$model$Event$SentState[event.mSentState.ordinal()];
                            if (i10 == 1 || i10 == 2) {
                                i3 = R.drawable.tchap_scanning;
                                i2 = R.string.tchap_scan_media_in_progress;
                            }
                        }
                        imageView.setImageResource(i3);
                        textView2.setText(this.mContext.getResources().getString(i2));
                        if (str == null) {
                            textView.setVisibility(0);
                            textView.setText(str);
                        } else {
                            textView.setVisibility(8);
                        }
                        View findViewById = inflate.findViewById(R.id.messagesAdapter_media_layout);
                        findViewById.setAlpha(!event.isSent() ? 1.0f : 0.5f);
                        manageSubView(i, inflate, findViewById, 13);
                        return inflate;
                    }
                    i3 = R.drawable.ic_notification_privacy_warning;
                    i2 = R.string.tchap_scan_media_unavailable;
                    imageView.setImageResource(i3);
                    textView2.setText(this.mContext.getResources().getString(i2));
                    if (str == null) {
                    }
                    View findViewById2 = inflate.findViewById(R.id.messagesAdapter_media_layout);
                    findViewById2.setAlpha(!event.isSent() ? 1.0f : 0.5f);
                    manageSubView(i, inflate, findViewById2, 13);
                    return inflate;
                }
            }
            Log.e(LOG_TAG, "getMediaScanView : invalid layout");
            return inflate;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getMediaScanView() failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString());
        }
    }

    private boolean isSupportedRow(MessageRow messageRow) {
        Event event = messageRow.getEvent();
        if (event == null || event.eventId == null) {
            Log.e(LOG_TAG, "## isSupportedRow() : invalid row");
            return false;
        }
        String str = event.eventId;
        MessageRow messageRow2 = (MessageRow) this.mEventRowMap.get(str);
        if (messageRow2 != null) {
            if (event.getAge() == Event.DUMMY_EVENT_AGE) {
                messageRow2.updateEvent(event);
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## isSupportedRow() : update the timestamp of ");
                sb.append(str);
                Log.d(str2, sb.toString());
            } else {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## isSupportedRow() : the event ");
                sb2.append(str);
                sb2.append(" has already been received");
                Log.e(str3, sb2.toString());
            }
            return false;
        }
        boolean isDisplayableEvent = VectorMessagesAdapterHelper.isDisplayableEvent(this.mContext, messageRow);
        if (!isDisplayableEvent) {
            String str4 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unsupported row. Event type: ");
            sb3.append(event.getType());
            Log.w(str4, sb3.toString());
        }
        if (isDisplayableEvent && TextUtils.equals(event.getType(), "m.room.member")) {
            String str5 = JsonUtils.toRoomMember(event.getContent()).membership;
            String str6 = "join";
            if (!PreferencesManager.showJoinLeaveMessages(this.mContext) && (TextUtils.equals(str5, "leave") || TextUtils.equals(str5, str6))) {
                EventContent prevContent = event.getPrevContent();
                isDisplayableEvent = prevContent != null ? TextUtils.equals(prevContent.membership, str5) : false;
            }
            if (isDisplayableEvent && !PreferencesManager.showAvatarDisplayNameChangeMessages(this.mContext) && TextUtils.equals(str5, str6)) {
                EventContent eventContent = JsonUtils.toEventContent(event.getContentAsJsonObject());
                EventContent prevContent2 = event.getPrevContent();
                String str7 = eventContent.displayname;
                String str8 = eventContent.avatar_url;
                if (prevContent2 != null) {
                    String str9 = prevContent2.displayname;
                    String str10 = prevContent2.avatar_url;
                    if (TextUtils.equals(prevContent2.membership, str6)) {
                        isDisplayableEvent = TextUtils.equals(str9, str7) && TextUtils.equals(str8, str10);
                    }
                }
            }
        }
        return isDisplayableEvent;
    }

    private String getFormattedTimestamp(Event event) {
        String str = (String) this.mEventFormattedTsMap.get(event.eventId);
        if (str != null) {
            return str;
        }
        String tsToString = event.isValidOriginServerTs() ? AdapterUtils.tsToString(this.mContext, event.getOriginServerTs(), true) : " ";
        this.mEventFormattedTsMap.put(event.eventId, tsToString);
        return tsToString;
    }

    private void refreshRefreshDateList() {
        ArrayList arrayList = new ArrayList();
        Date zeroTimeDate = AdapterUtils.zeroTimeDate(new Date());
        for (int i = 0; i < getCount(); i++) {
            Event event = ((MessageRow) getItem(i)).getEvent();
            if (event.isValidOriginServerTs()) {
                zeroTimeDate = AdapterUtils.zeroTimeDate(new Date(event.getOriginServerTs()));
            }
            arrayList.add(zeroTimeDate);
        }
        synchronized (this) {
            this.mMessagesDateList = arrayList;
            this.mReferenceDate = new Date();
        }
    }

    private String dateDiff(Date date, long j) {
        if (j == 0) {
            return this.mContext.getString(R.string.today);
        }
        if (j == 1) {
            return this.mContext.getString(R.string.yesterday);
        }
        if (j < 7) {
            return new SimpleDateFormat("EEEE", this.mLocale).format(date);
        }
        return DateUtils.formatDateRange(this.mContext, new Formatter(new StringBuilder(50), this.mLocale), date.getTime(), date.getTime(), 524310).toString();
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0020  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0029  */
    public String headerMessage(int i) {
        Date date;
        Date date2;
        synchronized (this) {
            if (i > 0) {
                if (i < this.mMessagesDateList.size()) {
                    date = (Date) this.mMessagesDateList.get(i - 1);
                    date2 = i >= this.mMessagesDateList.size() ? (Date) this.mMessagesDateList.get(i) : null;
                }
            }
            date = null;
            if (i >= this.mMessagesDateList.size()) {
            }
        }
        if (date2 == null) {
            return null;
        }
        if (date == null || 0 != date.getTime() - date2.getTime()) {
            return dateDiff(date2, (this.mReferenceDate.getTime() - date2.getTime()) / AdapterUtils.MS_IN_DAY);
        }
        return null;
    }

    private void manageSelectionMode(final View view, final Event event, final int i) {
        final String str = event.eventId;
        boolean z = true;
        boolean z2 = this.mSelectedEvent != null;
        if (!z2 || !TextUtils.equals(str, this.mSelectedEvent.eventId)) {
            z = false;
        }
        view.findViewById(R.id.messagesAdapter_action_image).setVisibility(z ? 0 : 8);
        float f = (!z2 || z) ? 1.0f : 0.2f;
        view.findViewById(R.id.messagesAdapter_body_view).setAlpha(f);
        view.findViewById(R.id.messagesAdapter_avatars_list).setAlpha(f);
        View findViewById = view.findViewById(R.id.messagesAdapter_urls_preview_list);
        if (findViewById != null) {
            findViewById.setAlpha(f);
        }
        TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_timestamp);
        if (z2 && z) {
            textView.setVisibility(0);
        }
        if (Event.EVENT_TYPE_STICKER.equals(event.getType())) {
            StickerMessage stickerMessage = JsonUtils.toStickerMessage(event.getContent());
            if (stickerMessage != null && z2 && z) {
                this.mHelper.showStickerDescription(view, stickerMessage);
            }
        }
        if (!(event instanceof EventGroup)) {
            View findViewById2 = view.findViewById(R.id.message_timestamp_layout);
            final Event event2 = event;
            final View view2 = view;
            final int i2 = i;
            AnonymousClass6 r0 = new OnClickListener() {
                public void onClick(View view) {
                    if (VectorMessagesAdapter.this.mSelectedEvent == null || !TextUtils.equals(str, VectorMessagesAdapter.this.mSelectedEvent.eventId)) {
                        VectorMessagesAdapter.this.onEventTap(event2);
                        return;
                    }
                    VectorMessagesAdapter vectorMessagesAdapter = VectorMessagesAdapter.this;
                    Event event = event2;
                    vectorMessagesAdapter.onMessageClick(event, vectorMessagesAdapter.getEventText(view2, event, i2), view2.findViewById(R.id.messagesAdapter_action_anchor));
                }
            };
            findViewById2.setOnClickListener(r0);
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (VectorMessagesAdapter.this.mIsSearchMode) {
                        return false;
                    }
                    VectorMessagesAdapter vectorMessagesAdapter = VectorMessagesAdapter.this;
                    Event event = event;
                    vectorMessagesAdapter.onMessageClick(event, vectorMessagesAdapter.getEventText(view, event, i), view.findViewById(R.id.messagesAdapter_action_anchor));
                    VectorMessagesAdapter.this.onEventTap(event);
                    return true;
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean mergeView(Event event, int i, boolean z) {
        if (z) {
            z = headerMessage(i) == null;
        }
        if (!z || event.isCallEvent()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public String getEventText(View view, Event event, int i) {
        if (view != null) {
            if (10 == i || i == 0) {
                return JsonUtils.toMessage(event.getContent()).body;
            }
            TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_body);
            if (textView != null) {
                return textView.getText().toString();
            }
        }
        return null;
    }

    private void addContentViewListeners(View view, View view2, final int i, int i2) {
        view2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener != null && i < VectorMessagesAdapter.this.getCount()) {
                    VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener.onContentClick(i);
                }
            }
        });
        final int i3 = i;
        final View view3 = view2;
        final int i4 = i2;
        final View view4 = view;
        AnonymousClass9 r1 = new OnLongClickListener() {
            public boolean onLongClick(View view) {
                if (i3 < VectorMessagesAdapter.this.getCount()) {
                    Event event = ((MessageRow) VectorMessagesAdapter.this.getItem(i3)).getEvent();
                    if (!VectorMessagesAdapter.this.mIsSearchMode) {
                        VectorMessagesAdapter vectorMessagesAdapter = VectorMessagesAdapter.this;
                        vectorMessagesAdapter.onMessageClick(event, vectorMessagesAdapter.getEventText(view3, event, i4), view4.findViewById(R.id.messagesAdapter_action_anchor));
                    }
                }
                return true;
            }
        };
        view2.setOnLongClickListener(r1);
    }

    private void displayE2eIcon(View view, int i) {
        ImageView imageView = (ImageView) view.findViewById(R.id.message_adapter_e2e_icon);
        if (imageView != null) {
            View findViewById = view.findViewById(R.id.e2e_sender_margin);
            View findViewById2 = view.findViewById(R.id.messagesAdapter_sender);
            final Event event = ((MessageRow) getItem(i)).getEvent();
            if (this.mE2eIconByEventId.containsKey(event.eventId)) {
                if (findViewById != null) {
                    findViewById.setVisibility(findViewById2.getVisibility());
                }
                imageView.setVisibility(0);
                Object obj = this.mE2eIconByEventId.get(event.eventId);
                if (obj instanceof Drawable) {
                    imageView.setImageDrawable((Drawable) obj);
                } else {
                    Integer num = (Integer) obj;
                    imageView.setImageResource(num.intValue());
                    if (num.intValue() == R.drawable.e2e_verified) {
                        imageView.setImageDrawable(ThemeUtils.INSTANCE.tintDrawable(getContext(), imageView.getDrawable(), R.attr.padlock_icon_tint_color));
                    }
                }
                int itemViewType = getItemViewType(i);
                if (itemViewType == 1 || itemViewType == 5 || itemViewType == 11) {
                    View findViewById3 = view.findViewById(R.id.messagesAdapter_body_layout);
                    MarginLayoutParams marginLayoutParams = (MarginLayoutParams) findViewById3.getLayoutParams();
                    MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) imageView.getLayoutParams();
                    marginLayoutParams2.setMargins(marginLayoutParams.leftMargin, marginLayoutParams2.topMargin, marginLayoutParams2.rightMargin, marginLayoutParams2.bottomMargin);
                    marginLayoutParams.setMargins(4, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
                    imageView.setLayoutParams(marginLayoutParams2);
                    findViewById3.setLayoutParams(marginLayoutParams);
                }
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener != null) {
                            VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener.onE2eIconClick(event, (MXDeviceInfo) VectorMessagesAdapter.this.mE2eDeviceByEventId.get(event.eventId));
                        }
                    }
                });
                return;
            }
            imageView.setVisibility(8);
            if (findViewById != null) {
                findViewById.setVisibility(8);
            }
        }
    }

    private void displayE2eReRequest(View view, int i) {
        TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_re_request_e2e_key);
        if (textView != null) {
            final Event event = ((MessageRow) getItem(i)).getEvent();
            final String sessionId = MatrixSdkExtensionsKt.getSessionId(event);
            if (!(sessionId == null || event.getCryptoError() == null)) {
                if (MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_ERROR_CODE.equals(event.getCryptoError().errcode)) {
                    textView.setVisibility(0);
                    if (this.mSessionIdsWaitingForE2eReRequest.contains(sessionId)) {
                        textView.setText(R.string.e2e_re_request_encryption_key_sent);
                        textView.setOnClickListener(null);
                        textView.setClickable(false);
                        return;
                    }
                    textView.setText(R.string.e2e_re_request_encryption_key);
                    textView.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            VectorMessagesAdapter.this.mSessionIdsWaitingForE2eReRequest.add(sessionId);
                            if (VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener != null) {
                                VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener.onEventAction(event, null, R.id.ic_action_re_request_e2e_key);
                            }
                            VectorMessagesAdapter.this.notifyDataSetChanged();
                        }
                    });
                    return;
                }
            }
            textView.setVisibility(8);
            textView.setOnClickListener(null);
            if (sessionId != null && this.mSessionIdsWaitingForE2eReRequest.contains(sessionId)) {
                IMessagesAdapterActionsListener iMessagesAdapterActionsListener = this.mVectorMessagesAdapterEventsListener;
                if (iMessagesAdapterActionsListener != null) {
                    iMessagesAdapterActionsListener.onEventDecrypted();
                }
                this.mSessionIdsWaitingForE2eReRequest.remove(sessionId);
            }
        }
    }

    private void manageCryptoEvents() {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        if (this.mIsRoomEncrypted && this.mSession.isCryptoEnabled()) {
            for (int i = 0; i < getCount(); i++) {
                Event event = ((MessageRow) getItem(i)).getEvent();
                if (event.mSentState != SentState.SENT) {
                    hashMap.put(event.eventId, Integer.valueOf(R.drawable.e2e_verified));
                } else if (!event.isEncrypted()) {
                    hashMap.put(event.eventId, this.mPadlockDrawable);
                } else if (event.getCryptoError() != null) {
                    hashMap.put(event.eventId, Integer.valueOf(R.drawable.e2e_blocked));
                } else {
                    EncryptedEventContent encryptedEventContent = JsonUtils.toEncryptedEventContent(event.getWireContent().getAsJsonObject());
                    if (!TextUtils.equals(this.mSession.getCredentials().deviceId, encryptedEventContent.device_id) || !TextUtils.equals(this.mSession.getMyUserId(), event.getSender())) {
                        MXDeviceInfo deviceWithIdentityKey = this.mSession.getCrypto().deviceWithIdentityKey(encryptedEventContent.sender_key, encryptedEventContent.algorithm);
                        if (deviceWithIdentityKey != null) {
                            hashMap2.put(event.eventId, deviceWithIdentityKey);
                            if (deviceWithIdentityKey.isVerified()) {
                                hashMap.put(event.eventId, Integer.valueOf(R.drawable.e2e_verified));
                            } else if (deviceWithIdentityKey.isBlocked()) {
                                hashMap.put(event.eventId, Integer.valueOf(R.drawable.e2e_blocked));
                            } else {
                                hashMap.put(event.eventId, Integer.valueOf(R.drawable.e2e_verified));
                            }
                        } else {
                            hashMap.put(event.eventId, Integer.valueOf(R.drawable.e2e_verified));
                        }
                    } else {
                        hashMap.put(event.eventId, Integer.valueOf(R.drawable.e2e_verified));
                        MXDeviceInfo deviceWithIdentityKey2 = this.mSession.getCrypto().deviceWithIdentityKey(encryptedEventContent.sender_key, encryptedEventContent.algorithm);
                        if (deviceWithIdentityKey2 != null) {
                            hashMap2.put(event.eventId, deviceWithIdentityKey2);
                        }
                    }
                }
            }
        }
        this.mE2eDeviceByEventId = hashMap2;
        this.mE2eIconByEventId = hashMap;
    }

    public void resetReadMarker() {
        Log.d(LOG_TAG, "resetReadMarker");
        this.mReadMarkerEventId = null;
    }

    public void updateReadMarker(String str, String str2) {
        this.mReadMarkerEventId = str;
        this.mReadReceiptEventId = str2;
        if (str != null && !str.equals(this.mReadMarkerEventId)) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("updateReadMarker read marker id has changed: ");
            sb.append(str);
            Log.d(str3, sb.toString());
            this.mCanShowReadMarker = true;
            notifyDataSetChanged();
        }
    }

    public void setReadMarkerListener(ReadMarkerListener readMarkerListener) {
        this.mReadMarkerListener = readMarkerListener;
    }

    public void setImageGetter(VectorImageGetter vectorImageGetter) {
        this.mImageGetter = vectorImageGetter;
        this.mHelper.setImageGetter(vectorImageGetter);
    }

    private void animateReadMarkerView(final Event event, final View view) {
        if (view != null && this.mCanShowReadMarker) {
            this.mCanShowReadMarker = false;
            if (view.getAnimation() == null) {
                Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.unread_marker_anim);
                loadAnimation.setStartOffset(500);
                loadAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(8);
                    }
                });
                view.setAnimation(loadAnimation);
            }
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    View view = view;
                    if (view != null && view.getAnimation() != null) {
                        view.setVisibility(0);
                        view.getAnimation().start();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (VectorMessagesAdapter.this.mReadMarkerListener != null) {
                                    VectorMessagesAdapter.this.mReadMarkerListener.onReadMarkerDisplayed(event, view);
                                }
                            }
                        }, view.getAnimation().getDuration() + view.getAnimation().getStartOffset());
                    } else if (VectorMessagesAdapter.this.mReadMarkerListener != null) {
                        VectorMessagesAdapter.this.mReadMarkerListener.onReadMarkerDisplayed(event, view);
                    }
                }
            });
        }
    }

    private boolean isReadMarkedEvent(Event event) {
        String str = this.mReadMarkerEventId;
        if (str == null || !this.mHiddenEventIds.contains(str) || !(event instanceof EventGroup)) {
            return event.eventId.equals(this.mReadMarkerEventId);
        }
        return ((EventGroup) event).contains(this.mReadMarkerEventId);
    }

    private void handleReadMarker(View view, int i) {
        MessageRow messageRow = (MessageRow) getItem(i);
        Event event = messageRow != null ? messageRow.getEvent() : null;
        View findViewById = view.findViewById(R.id.message_read_marker);
        if (findViewById == null) {
            return;
        }
        if (event != null && !event.isDummyEvent() && this.mReadMarkerEventId != null && this.mCanShowReadMarker && isReadMarkedEvent(event) && !this.mIsPreviewMode && !this.mIsSearchMode && (!this.mReadMarkerEventId.equals(this.mReadReceiptEventId) || i < getCount() - 1)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(" Display read marker ");
            sb.append(event.eventId);
            sb.append(" mReadMarkerEventId");
            sb.append(this.mReadMarkerEventId);
            Log.d(str, sb.toString());
            animateReadMarkerView(event, findViewById);
        } else if (8 != findViewById.getVisibility()) {
            Log.v(LOG_TAG, "hide read marker");
            findViewById.setVisibility(8);
        }
    }

    private void setReadMarker(View view, MessageRow messageRow, boolean z, View view2, View view3) {
        Event event = messageRow.getEvent();
        View findViewById = view.findViewById(R.id.messagesAdapter_highlight_message_marker);
        View findViewById2 = view.findViewById(R.id.message_read_marker);
        if (findViewById != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) findViewById.getLayoutParams();
            marginLayoutParams.setMargins(5, marginLayoutParams.topMargin, 5, marginLayoutParams.bottomMargin);
            if (!TextUtils.equals(this.mHighlightedEventId, event.eventId)) {
                findViewById.setBackgroundColor(ContextCompat.getColor(this.mContext, 17170445));
            } else if (this.mIsUnreadViewMode) {
                findViewById.setBackgroundColor(ContextCompat.getColor(this.mContext, 17170445));
                if (findViewById2 != null) {
                    animateReadMarkerView(event, findViewById2);
                }
            } else {
                LayoutParams layoutParams = view2.getLayoutParams();
                MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) view3.getLayoutParams();
                if (z) {
                    marginLayoutParams.setMargins(layoutParams.width + 5, marginLayoutParams.topMargin, 5, marginLayoutParams.bottomMargin);
                } else {
                    marginLayoutParams.setMargins(5, marginLayoutParams.topMargin, 5, marginLayoutParams.bottomMargin);
                }
                marginLayoutParams2.setMargins(4, marginLayoutParams2.topMargin, 4, marginLayoutParams2.bottomMargin);
                findViewById.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.vector_green_color));
            }
            findViewById.setLayoutParams(marginLayoutParams);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0202, code lost:
        if (org.matrix.androidsdk.rest.model.message.Message.MSGTYPE_FILE.equals(r3.msgtype) != false) goto L_0x0204;
     */
    public void onMessageClick(final Event event, final String str, View view) {
        PopupMenu popupMenu = VERSION.SDK_INT >= 19 ? new PopupMenu(this.mContext, view, GravityCompat.END) : new PopupMenu(this.mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.vector_room_message_settings, popupMenu.getMenu());
        boolean z = false;
        try {
            Field[] declaredFields = popupMenu.getClass().getDeclaredFields();
            int length = declaredFields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Field field = declaredFields[i];
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object obj = field.get(popupMenu);
                    Class.forName(obj.getClass().getName()).getMethod("setForceShowIcon", new Class[]{Boolean.TYPE}).invoke(obj, new Object[]{Boolean.valueOf(true)});
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onMessageClick : force to display the icons failed ");
            sb.append(e.getLocalizedMessage());
            Log.e(str2, sb.toString(), e);
        }
        Menu menu = popupMenu.getMenu();
        ThemeUtils.INSTANCE.tintMenuIcons(menu, ThemeUtils.INSTANCE.getColor(this.mContext, R.attr.vctr_settings_icon_tint_color));
        for (int i2 = 0; i2 < menu.size(); i2++) {
            menu.getItem(i2).setVisible(false);
        }
        MediaScanManager mediaScanManager = this.mMediaScanManager;
        boolean z2 = mediaScanManager == null || mediaScanManager.isUncheckedOrUntrustedMediaEvent(event);
        menu.findItem(R.id.ic_action_view_source).setVisible(true);
        menu.findItem(R.id.ic_action_view_decrypted_source).setVisible(event.isEncrypted() && event.getClearEvent() != null);
        menu.findItem(R.id.ic_action_vector_permalink).setVisible(false);
        if (!TextUtils.isEmpty(str) && !z2) {
            menu.findItem(R.id.ic_action_vector_copy).setVisible(true);
            menu.findItem(R.id.ic_action_vector_quote).setVisible(true);
        }
        if (event.isUploadingMedia(this.mMediaCache)) {
            menu.findItem(R.id.ic_action_vector_cancel_upload).setVisible(true);
        }
        if (event.isDownloadingMedia(this.mMediaCache)) {
            menu.findItem(R.id.ic_action_vector_cancel_download).setVisible(true);
        }
        if (event.canBeResent()) {
            menu.findItem(R.id.ic_action_vector_resend_message).setVisible(true);
            if (event.isUndelivered() || event.isUnknownDevice()) {
                menu.findItem(R.id.ic_action_vector_redact_message).setVisible(true);
            }
        } else if (event.mSentState == SentState.SENT) {
            boolean z3 = !this.mIsPreviewMode && event.stateKey == null && !TextUtils.equals(event.getType(), "m.room.encryption");
            if (z3) {
                if (!TextUtils.equals(event.sender, this.mSession.getMyUserId())) {
                    Room room = this.mSession.getDataHandler().getRoom(event.roomId);
                    if (!(room == null || room.getState().getPowerLevels() == null)) {
                        PowerLevels powerLevels = room.getState().getPowerLevels();
                        if (powerLevels.getUserPowerLevel(this.mSession.getMyUserId()) < powerLevels.redact) {
                            z3 = false;
                        }
                    }
                }
                z3 = true;
            }
            menu.findItem(R.id.ic_action_vector_redact_message).setVisible(z3);
            if (Event.EVENT_TYPE_MESSAGE.equals(event.getType()) && !z2) {
                Message message = JsonUtils.toMessage(event.getContentAsJsonObject());
                menu.findItem(R.id.ic_action_vector_share).setVisible(!this.mIsRoomEncrypted);
                menu.findItem(R.id.ic_action_vector_forward).setVisible(true);
                if (!Message.MSGTYPE_IMAGE.equals(message.msgtype)) {
                    if (!Message.MSGTYPE_VIDEO.equals(message.msgtype)) {
                    }
                }
                menu.findItem(R.id.ic_action_vector_save).setVisible(true);
                MenuItem findItem = menu.findItem(R.id.ic_action_vector_report);
                if (!this.mIsPreviewMode && !TextUtils.equals(event.sender, this.mSession.getMyUserId())) {
                    z = true;
                }
                findItem.setVisible(z);
            }
        }
        menu.findItem(R.id.ic_action_device_verification).setVisible(this.mE2eIconByEventId.containsKey(event.eventId));
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener != null) {
                    VectorMessagesAdapter.this.mVectorMessagesAdapterEventsListener.onEventAction(event, str, menuItem.getItemId());
                }
                VectorMessagesAdapter.this.cancelSelectionMode();
                return true;
            }
        });
        try {
            popupMenu.show();
        } catch (Exception e2) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" popup.show failed ");
            sb2.append(e2.getMessage());
            Log.e(str3, sb2.toString(), e2);
        }
    }

    private boolean addToEventGroupToFront(MessageRow messageRow) {
        MessageRow messageRow2 = null;
        if (supportMessageRowMerge(messageRow)) {
            MessageRow messageRow3 = (getCount() <= 0 || !(((MessageRow) getItem(0)).getEvent() instanceof EventGroup) || !((EventGroup) ((MessageRow) getItem(0)).getEvent()).canAddRow(messageRow)) ? null : (MessageRow) getItem(0);
            if (messageRow3 == null) {
                messageRow3 = new MessageRow(new EventGroup(this.mHiddenEventIds), null);
                this.mEventGroups.add((EventGroup) messageRow3.getEvent());
                super.insert(messageRow3, 0);
                this.mEventRowMap.put(messageRow3.getEvent().eventId, messageRow);
            }
            messageRow2 = messageRow3;
            ((EventGroup) messageRow2.getEvent()).addToFront(messageRow);
            updateHighlightedEventId();
        }
        if (messageRow2 != null) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0027, code lost:
        if (((im.vector.util.EventGroup) r2.getEvent()).canAddRow(r5) != false) goto L_0x003f;
     */
    private void addToEventGroup(MessageRow messageRow) {
        MessageRow messageRow2;
        if (supportMessageRowMerge(messageRow)) {
            int count = getCount();
            while (true) {
                count--;
                if (count < 0) {
                    break;
                }
                messageRow2 = (MessageRow) getItem(count);
                if (!(messageRow2.getEvent() instanceof EventGroup)) {
                    if (!TextUtils.equals(messageRow2.getEvent().getType(), "m.room.member")) {
                        break;
                    }
                }
            }
            messageRow2 = null;
            if (messageRow2 == null) {
                messageRow2 = new MessageRow(new EventGroup(this.mHiddenEventIds), null);
                super.add(messageRow2);
                this.mEventGroups.add((EventGroup) messageRow2.getEvent());
                this.mEventRowMap.put(messageRow2.getEvent().eventId, messageRow2);
            }
            ((EventGroup) messageRow2.getEvent()).add(messageRow);
            updateHighlightedEventId();
        }
    }

    private void removeFromEventGroup(MessageRow messageRow) {
        if (supportMessageRowMerge(messageRow)) {
            String str = messageRow.getEvent().eventId;
            for (EventGroup eventGroup : this.mEventGroups) {
                if (eventGroup.contains(str)) {
                    eventGroup.removeByEventId(str);
                    if (eventGroup.isEmpty()) {
                        this.mEventGroups.remove(eventGroup);
                        super.remove(messageRow);
                        updateHighlightedEventId();
                        return;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateHighlightedEventId() {
        if (this.mSearchedEventId != null && !this.mEventGroups.isEmpty() && this.mHiddenEventIds.contains(this.mSearchedEventId)) {
            for (EventGroup eventGroup : this.mEventGroups) {
                if (eventGroup.contains(this.mSearchedEventId)) {
                    this.mHighlightedEventId = eventGroup.eventId;
                    return;
                }
            }
        }
        this.mHighlightedEventId = this.mSearchedEventId;
    }

    private void checkEventGroupsMerge(MessageRow messageRow, int i) {
        if (i > 0 && i < getCount() - 1 && !EventGroup.isSupported(messageRow)) {
            int i2 = i - 1;
            Event event = ((MessageRow) getItem(i2)).getEvent();
            Event event2 = ((MessageRow) getItem(i)).getEvent();
            if (TextUtils.equals(event.getType(), "m.room.member") && (event2 instanceof EventGroup)) {
                EventGroup eventGroup = (EventGroup) event2;
                EventGroup eventGroup2 = null;
                while (true) {
                    if (i2 < 0) {
                        break;
                    } else if (((MessageRow) getItem(i2)).getEvent() instanceof EventGroup) {
                        eventGroup2 = (EventGroup) ((MessageRow) getItem(i2)).getEvent();
                        break;
                    } else {
                        i2--;
                    }
                }
                if (eventGroup2 != null) {
                    ArrayList<MessageRow> arrayList = new ArrayList<>(eventGroup.getRows());
                    if (eventGroup2.canAddRow((MessageRow) arrayList.get(0))) {
                        for (MessageRow add : arrayList) {
                            eventGroup2.add(add);
                        }
                    }
                    MessageRow messageRow2 = (MessageRow) this.mEventRowMap.get(eventGroup.eventId);
                    this.mEventGroups.remove(eventGroup);
                    super.remove(messageRow2);
                    updateHighlightedEventId();
                }
            }
        }
    }
}
