package im.vector.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationCompat.InboxStyle;
import androidx.core.app.NotificationCompat.WearableExtender;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.JoinRoomActivity;
import im.vector.activity.JoinRoomActivity.Companion;
import im.vector.activity.LockScreenActivity;
import im.vector.activity.VectorFakeRoomPreviewActivity;
import im.vector.activity.VectorHomeActivity;
import im.vector.activity.VectorRoomActivity;
import im.vector.push.PushManager;
import im.vector.receiver.DismissNotificationReceiver;
import im.vector.util.BitmapUtilKt;
import im.vector.util.PreferencesManager;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\u000e\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J \u0010\u001a\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u001a\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0001\u0010\u001d\u001a\u00020\u000bH\u0007J(\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u00042\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u0004H\u0007J*\u0010\"\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0002J\u0018\u0010\"\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010%\u001a\u00020&J:\u0010\"\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0018\u0010'\u001a\u0014\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020*0)0(2\u0006\u0010+\u001a\u00020*2\u0006\u0010%\u001a\u00020&J&\u0010,\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\f\u0010-\u001a\b\u0012\u0004\u0012\u00020.0)2\u0006\u0010#\u001a\u00020$J0\u0010/\u001a\u00020\u001c2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u00042\u0006\u00100\u001a\u00020\u00042\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\u0004H\u0007J\u000e\u00101\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u00102\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u00103\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u00104\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u001a\u00105\u001a\u00020.2\u0006\u0010\u0014\u001a\u00020\u00152\b\u00106\u001a\u0004\u0018\u00010\u0004H\u0002J\u000e\u00107\u001a\u00020&2\u0006\u0010\u0014\u001a\u00020\u0015J(\u00108\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010%\u001a\u00020&2\u0006\u00109\u001a\u00020&H\u0003J\u0016\u0010:\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010;\u001a\u00020\u001cR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bXT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bXT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006<"}, d2 = {"Lim/vector/notifications/NotificationUtils;", "", "()V", "CALL_NOTIFICATION_CHANNEL_ID", "", "JOIN_ACTION", "LISTENING_FOR_EVENTS_NOTIFICATION_CHANNEL_ID", "LOG_TAG", "kotlin.jvm.PlatformType", "NOISY_NOTIFICATION_CHANNEL_ID_BASE", "NOTIFICATION_ID_FOREGROUND_SERVICE", "", "NOTIFICATION_ID_MESSAGES", "QUICK_LAUNCH_ACTION", "REJECT_ACTION", "SILENT_NOTIFICATION_CHANNEL_ID", "TAP_TO_VIEW_ACTION", "noisyNotificationChannelId", "addTextStyle", "", "context", "Landroid/content/Context;", "builder", "Landroidx/core/app/NotificationCompat$Builder;", "roomsNotifications", "Lim/vector/notifications/RoomsNotifications;", "addTextStyleWithSeveralRooms", "buildForegroundServiceNotification", "Landroid/app/Notification;", "subTitleResId", "buildIncomingCallNotification", "roomName", "matrixId", "callId", "buildMessageNotification", "bingRule", "Lorg/matrix/androidsdk/rest/model/bingrules/BingRule;", "isBackground", "", "notifiedEventsByRoomId", "", "", "Lim/vector/notifications/NotifiedEvent;", "eventToNotify", "buildMessagesListNotification", "messagesStrings", "", "buildPendingCallNotification", "roomId", "cancelAllNotifications", "cancelNotificationForegroundService", "cancelNotificationMessage", "createNotificationChannels", "ensureTitleNotEmpty", "title", "isDoNotDisturbModeOn", "manageNotificationSound", "isBing", "showNotificationMessage", "notification", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: NotificationUtils.kt */
public final class NotificationUtils {
    private static final String CALL_NOTIFICATION_CHANNEL_ID = "CALL_NOTIFICATION_CHANNEL_ID";
    public static final NotificationUtils INSTANCE = new NotificationUtils();
    private static final String JOIN_ACTION = "NotificationUtils.JOIN_ACTION";
    private static final String LISTENING_FOR_EVENTS_NOTIFICATION_CHANNEL_ID = "LISTEN_FOR_EVENTS_NOTIFICATION_CHANNEL_ID";
    private static final String LOG_TAG = NotificationUtils.class.getSimpleName();
    private static final String NOISY_NOTIFICATION_CHANNEL_ID_BASE = "DEFAULT_NOISY_NOTIFICATION_CHANNEL_ID_BASE";
    public static final int NOTIFICATION_ID_FOREGROUND_SERVICE = 61;
    private static final int NOTIFICATION_ID_MESSAGES = 60;
    private static final String QUICK_LAUNCH_ACTION = "NotificationUtils.QUICK_LAUNCH_ACTION";
    private static final String REJECT_ACTION = "NotificationUtils.REJECT_ACTION";
    private static final String SILENT_NOTIFICATION_CHANNEL_ID = "DEFAULT_SILENT_NOTIFICATION_CHANNEL_ID";
    public static final String TAP_TO_VIEW_ACTION = "NotificationUtils.TAP_TO_VIEW_ACTION";
    private static String noisyNotificationChannelId;

    private NotificationUtils() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0080, code lost:
        if (android.text.TextUtils.equals(r1, r2.toString()) == false) goto L_0x0082;
     */
    private final void createNotificationChannels(Context context) {
        if (VERSION.SDK_INT >= 26) {
            Object systemService = context.getSystemService("notification");
            if (systemService != null) {
                NotificationManager notificationManager = (NotificationManager) systemService;
                String str = "channel";
                if (noisyNotificationChannelId == null) {
                    Iterator it = notificationManager.getNotificationChannels().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        NotificationChannel notificationChannel = (NotificationChannel) it.next();
                        Intrinsics.checkExpressionValueIsNotNull(notificationChannel, str);
                        String id = notificationChannel.getId();
                        Intrinsics.checkExpressionValueIsNotNull(id, "channel.id");
                        if (StringsKt.startsWith$default(id, NOISY_NOTIFICATION_CHANNEL_ID_BASE, false, 2, null)) {
                            noisyNotificationChannelId = notificationChannel.getId();
                            break;
                        }
                    }
                }
                String str2 = noisyNotificationChannelId;
                if (str2 != null) {
                    NotificationChannel notificationChannel2 = notificationManager.getNotificationChannel(str2);
                    Intrinsics.checkExpressionValueIsNotNull(notificationChannel2, str);
                    Uri sound = notificationChannel2.getSound();
                    Uri notificationRingTone = PreferencesManager.getNotificationRingTone(context);
                    if (!((sound == null) ^ (notificationRingTone == null))) {
                        if (sound != null) {
                            CharSequence uri = sound.toString();
                            if (notificationRingTone == null) {
                                Intrinsics.throwNpe();
                            }
                        }
                    }
                    notificationManager.deleteNotificationChannel(noisyNotificationChannelId);
                    noisyNotificationChannelId = null;
                }
                if (noisyNotificationChannelId == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("DEFAULT_NOISY_NOTIFICATION_CHANNEL_ID_BASE_");
                    sb.append(System.currentTimeMillis());
                    noisyNotificationChannelId = sb.toString();
                    NotificationChannel notificationChannel3 = new NotificationChannel(noisyNotificationChannelId, context.getString(R.string.notification_noisy_notifications), 3);
                    notificationChannel3.setDescription(context.getString(R.string.notification_noisy_notifications));
                    notificationChannel3.setSound(PreferencesManager.getNotificationRingTone(context), null);
                    notificationChannel3.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel3);
                }
                String str3 = SILENT_NOTIFICATION_CHANNEL_ID;
                if (notificationManager.getNotificationChannel(str3) == null) {
                    NotificationChannel notificationChannel4 = new NotificationChannel(str3, context.getString(R.string.notification_silent_notifications), 3);
                    notificationChannel4.setDescription(context.getString(R.string.notification_silent_notifications));
                    notificationChannel4.setSound(null, null);
                    notificationManager.createNotificationChannel(notificationChannel4);
                }
                String str4 = LISTENING_FOR_EVENTS_NOTIFICATION_CHANNEL_ID;
                if (notificationManager.getNotificationChannel(str4) == null) {
                    NotificationChannel notificationChannel5 = new NotificationChannel(str4, context.getString(R.string.notification_listening_for_events), 1);
                    notificationChannel5.setDescription(context.getString(R.string.notification_listening_for_events));
                    notificationChannel5.setSound(null, null);
                    notificationChannel5.setShowBadge(false);
                    notificationManager.createNotificationChannel(notificationChannel5);
                }
                String str5 = CALL_NOTIFICATION_CHANNEL_ID;
                if (notificationManager.getNotificationChannel(str5) == null) {
                    NotificationChannel notificationChannel6 = new NotificationChannel(str5, context.getString(R.string.call), 3);
                    notificationChannel6.setDescription(context.getString(R.string.call));
                    notificationChannel6.setSound(null, null);
                    notificationManager.createNotificationChannel(notificationChannel6);
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.app.NotificationManager");
        }
    }

    public final Notification buildForegroundServiceNotification(Context context, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intent intent = new Intent(context, VectorHomeActivity.class);
        intent.setFlags(603979776);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);
        createNotificationChannels(context);
        Builder contentIntent = new Builder(context, LISTENING_FOR_EVENTS_NOTIFICATION_CHANNEL_ID).setWhen(System.currentTimeMillis()).setContentTitle(context.getString(R.string.tchap_app_name)).setContentText(context.getString(i)).setSmallIcon(R.drawable.logo_transparent).setContentIntent(activity);
        if (VERSION.SDK_INT >= 16) {
            Intrinsics.checkExpressionValueIsNotNull(contentIntent, "builder");
            contentIntent.setPriority(-2);
        }
        Notification build = contentIntent.build();
        build.flags |= 32;
        if (VERSION.SDK_INT < 23) {
            try {
                build.getClass().getMethod("setLatestEventInfo", new Class[]{Context.class, CharSequence.class, CharSequence.class, PendingIntent.class}).invoke(build, new Object[]{context, context.getString(R.string.tchap_app_name), context.getString(i), activity});
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## buildNotification(): Exception - setLatestEventInfo() Msg=");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        Intrinsics.checkExpressionValueIsNotNull(build, "notification");
        return build;
    }

    public final Notification buildIncomingCallNotification(Context context, String str, String str2, String str3) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "roomName");
        Intrinsics.checkParameterIsNotNull(str2, "matrixId");
        Intrinsics.checkParameterIsNotNull(str3, "callId");
        createNotificationChannels(context);
        Builder lights = new Builder(context, CALL_NOTIFICATION_CHANNEL_ID).setWhen(System.currentTimeMillis()).setContentTitle(ensureTitleNotEmpty(context, str)).setContentText(context.getString(R.string.incoming_call)).setSmallIcon(R.drawable.incoming_call_notification_transparent).setLights(-16711936, 500, 500);
        if (VERSION.SDK_INT >= 16) {
            Intrinsics.checkExpressionValueIsNotNull(lights, "builder");
            lights.setPriority(2);
        }
        TaskStackBuilder addNextIntent = TaskStackBuilder.create(context).addParentStack(VectorHomeActivity.class).addNextIntent(new Intent(context, VectorHomeActivity.class).setFlags(872415232).putExtra(VectorHomeActivity.EXTRA_CALL_SESSION_ID, str2).putExtra(VectorHomeActivity.EXTRA_CALL_ID, str3));
        Intrinsics.checkExpressionValueIsNotNull(addNextIntent, "TaskStackBuilder.create(…   .addNextIntent(intent)");
        lights.setContentIntent(addNextIntent.getPendingIntent(new Random().nextInt(1000), 134217728));
        Notification build = lights.build();
        Intrinsics.checkExpressionValueIsNotNull(build, "builder.build()");
        return build;
    }

    public final Notification buildPendingCallNotification(Context context, String str, String str2, String str3, String str4) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "roomName");
        Intrinsics.checkParameterIsNotNull(str2, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str3, "matrixId");
        Intrinsics.checkParameterIsNotNull(str4, "callId");
        createNotificationChannels(context);
        Builder smallIcon = new Builder(context, CALL_NOTIFICATION_CHANNEL_ID).setWhen(System.currentTimeMillis()).setContentTitle(ensureTitleNotEmpty(context, str)).setContentText(context.getString(R.string.call_in_progress)).setSmallIcon(R.drawable.incoming_call_notification_transparent);
        if (VERSION.SDK_INT >= 16) {
            Intrinsics.checkExpressionValueIsNotNull(smallIcon, "builder");
            smallIcon.setPriority(2);
        }
        TaskStackBuilder addNextIntent = TaskStackBuilder.create(context).addParentStack(VectorRoomActivity.class).addNextIntent(new Intent(context, VectorRoomActivity.class).putExtra("EXTRA_ROOM_ID", str2).putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", str3).putExtra(VectorRoomActivity.EXTRA_START_CALL_ID, str4));
        Intrinsics.checkExpressionValueIsNotNull(addNextIntent, "TaskStackBuilder.create(…addNextIntent(roomIntent)");
        smallIcon.setContentIntent(addNextIntent.getPendingIntent(new Random().nextInt(1000), 134217728));
        Notification build = smallIcon.build();
        Intrinsics.checkExpressionValueIsNotNull(build, "builder.build()");
        return build;
    }

    private final void addTextStyleWithSeveralRooms(Context context, Builder builder, RoomsNotifications roomsNotifications) {
        Intent intent;
        InboxStyle inboxStyle = new InboxStyle();
        for (RoomNotifications roomNotifications : roomsNotifications.mRoomNotifications) {
            SpannableString spannableString = new SpannableString(roomNotifications.mMessagesSummary);
            spannableString.setSpan(new StyleSpan(1), 0, roomNotifications.mMessageHeader.length(), 33);
            inboxStyle.addLine(spannableString);
        }
        inboxStyle.setBigContentTitle(context.getString(R.string.tchap_app_name));
        inboxStyle.setSummaryText(roomsNotifications.mSummaryText);
        builder.setStyle(inboxStyle);
        TaskStackBuilder create = TaskStackBuilder.create(context);
        Intrinsics.checkExpressionValueIsNotNull(create, "TaskStackBuilder.create(context)");
        create.addNextIntentWithParentStack(new Intent(context, VectorHomeActivity.class));
        if (roomsNotifications.mIsInvitationEvent) {
            intent = CommonActivityUtils.buildIntentPreviewRoom(roomsNotifications.mSessionId, roomsNotifications.mRoomId, context, VectorFakeRoomPreviewActivity.class);
        } else {
            Intent intent2 = new Intent(context, VectorRoomActivity.class);
            intent2.putExtra("EXTRA_ROOM_ID", roomsNotifications.mRoomId);
            intent = intent2;
        }
        if (intent == null) {
            Intrinsics.throwNpe();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(TAP_TO_VIEW_ACTION);
        sb.append((int) System.currentTimeMillis());
        intent.setAction(sb.toString());
        create.addNextIntent(intent);
        builder.setContentIntent(create.getPendingIntent(0, 134217728));
        NotificationUtils notificationUtils = this;
        TaskStackBuilder addNextIntent = TaskStackBuilder.create(context).addNextIntent(new Intent(context, VectorHomeActivity.class));
        Intrinsics.checkExpressionValueIsNotNull(addNextIntent, "TaskStackBuilder.create(…NextIntent(openIntentTap)");
        builder.addAction(R.drawable.ic_home_black_24dp, context.getString(R.string.bottom_action_home), addNextIntent.getPendingIntent(0, 134217728));
    }

    private final void addTextStyle(Context context, Builder builder, RoomsNotifications roomsNotifications) {
        Intent intent;
        if (roomsNotifications.mRoomNotifications.size() != 0) {
            if (roomsNotifications.mRoomNotifications.size() > 1) {
                addTextStyleWithSeveralRooms(context, builder, roomsNotifications);
                return;
            }
            SpannableString spannableString = null;
            InboxStyle inboxStyle = new InboxStyle();
            for (CharSequence spannableString2 : roomsNotifications.mReversedMessagesList) {
                inboxStyle.addLine(new SpannableString(spannableString2));
            }
            inboxStyle.setBigContentTitle(roomsNotifications.mContentTitle);
            roomsNotifications.mReversedMessagesList.size();
            if (!TextUtils.isEmpty(roomsNotifications.mSummaryText)) {
                inboxStyle.setSummaryText(roomsNotifications.mSummaryText);
            }
            builder.setStyle(inboxStyle);
            if (roomsNotifications.mIsInvitationEvent) {
                NotificationUtils notificationUtils = this;
                Companion companion = JoinRoomActivity.Companion;
                String str = roomsNotifications.mRoomId;
                String str2 = "roomsNotifications.mRoomId";
                Intrinsics.checkExpressionValueIsNotNull(str, str2);
                String str3 = roomsNotifications.mSessionId;
                String str4 = "roomsNotifications.mSessionId";
                Intrinsics.checkExpressionValueIsNotNull(str3, str4);
                Intent rejectRoomIntent = companion.getRejectRoomIntent(context, str, str3);
                StringBuilder sb = new StringBuilder();
                sb.append(REJECT_ACTION);
                sb.append((int) System.currentTimeMillis());
                rejectRoomIntent.setAction(sb.toString());
                builder.addAction(R.drawable.vector_notification_reject_invitation, context.getString(R.string.reject), PendingIntent.getActivity(context, 0, rejectRoomIntent, 0));
                Companion companion2 = JoinRoomActivity.Companion;
                String str5 = roomsNotifications.mRoomId;
                Intrinsics.checkExpressionValueIsNotNull(str5, str2);
                String str6 = roomsNotifications.mSessionId;
                Intrinsics.checkExpressionValueIsNotNull(str6, str4);
                Intent joinRoomIntent = companion2.getJoinRoomIntent(context, str5, str6);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(JOIN_ACTION);
                sb2.append((int) System.currentTimeMillis());
                joinRoomIntent.setAction(sb2.toString());
                builder.addAction(R.drawable.vector_notification_accept_invitation, context.getString(R.string.join), PendingIntent.getActivity(context, 0, joinRoomIntent, 0));
            } else if (!LockScreenActivity.isDisplayingALockScreenActivity()) {
                Intent intent2 = new Intent(context, LockScreenActivity.class);
                intent2.putExtra(LockScreenActivity.EXTRA_ROOM_ID, roomsNotifications.mRoomId);
                intent2.putExtra(LockScreenActivity.EXTRA_SENDER_NAME, roomsNotifications.mSenderName);
                intent2.putExtra(LockScreenActivity.EXTRA_MESSAGE_BODY, roomsNotifications.mQuickReplyBody);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(QUICK_LAUNCH_ACTION);
                sb3.append((int) System.currentTimeMillis());
                intent2.setAction(sb3.toString());
                builder.addAction(R.drawable.vector_notification_quick_reply, context.getString(R.string.action_quick_reply), PendingIntent.getActivity(context, 0, intent2, 0));
            }
            if (roomsNotifications.mIsInvitationEvent) {
                intent = CommonActivityUtils.buildIntentPreviewRoom(roomsNotifications.mSessionId, roomsNotifications.mRoomId, context, VectorFakeRoomPreviewActivity.class);
                Intrinsics.checkExpressionValueIsNotNull(intent, "CommonActivityUtils.buil…viewActivity::class.java)");
            } else {
                intent = new Intent(context, VectorRoomActivity.class);
                intent.putExtra("EXTRA_ROOM_ID", roomsNotifications.mRoomId);
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(TAP_TO_VIEW_ACTION);
            sb4.append((int) System.currentTimeMillis());
            intent.setAction(sb4.toString());
            TaskStackBuilder addNextIntent = TaskStackBuilder.create(context).addNextIntentWithParentStack(new Intent(context, VectorHomeActivity.class)).addNextIntent(intent);
            Intrinsics.checkExpressionValueIsNotNull(addNextIntent, "TaskStackBuilder.create(…NextIntent(roomIntentTap)");
            builder.setContentIntent(addNextIntent.getPendingIntent(0, 134217728));
            builder.addAction(R.drawable.vector_notification_open, context.getString(R.string.action_open), addNextIntent.getPendingIntent(0, 134217728));
            if (!roomsNotifications.mIsInvitationEvent) {
                try {
                    WearableExtender wearableExtender = new WearableExtender();
                    wearableExtender.addAction(new Action.Builder(R.drawable.logo_transparent, roomsNotifications.mWearableMessage, addNextIntent.getPendingIntent(0, 134217728)).build());
                    builder.extend(wearableExtender);
                } catch (Exception e) {
                    String str7 = LOG_TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("## addTextStyleWithSeveralRooms() : WearableExtender failed ");
                    sb5.append(e.getMessage());
                    Log.e(str7, sb5.toString(), e);
                }
            }
        }
    }

    private final void manageNotificationSound(Context context, Builder builder, boolean z, boolean z2) {
        int color = ContextCompat.getColor(context, R.color.vector_fuchsia_color);
        if (z) {
            builder.setPriority(0);
            builder.setColor(0);
        } else if (z2) {
            builder.setPriority(1);
            builder.setColor(color);
        } else {
            builder.setPriority(0);
            builder.setColor(0);
        }
        String str = SILENT_NOTIFICATION_CHANNEL_ID;
        if (!z) {
            builder.setDefaults(4);
            if (z2 && PreferencesManager.getNotificationRingTone(context) != null) {
                builder.setSound(PreferencesManager.getNotificationRingTone(context));
                if (VERSION.SDK_INT >= 26) {
                    String str2 = noisyNotificationChannelId;
                    if (str2 == null) {
                        Intrinsics.throwNpe();
                    }
                    builder.setChannelId(str2);
                }
            } else if (VERSION.SDK_INT >= 26) {
                builder.setChannelId(str);
            }
            try {
                Matrix instance = Matrix.getInstance(VectorApp.getInstance());
                if (instance == null) {
                    Intrinsics.throwNpe();
                }
                PushManager pushManager = instance.getPushManager();
                Intrinsics.checkExpressionValueIsNotNull(pushManager, "Matrix.getInstance(Vecto…Instance())!!.pushManager");
                if (pushManager.isScreenTurnedOn()) {
                    Object systemService = VectorApp.getInstance().getSystemService("power");
                    if (systemService != null) {
                        WakeLock newWakeLock = ((PowerManager) systemService).newWakeLock(268435466, "Tchap:manageNotificationSound");
                        newWakeLock.acquire(3000);
                        newWakeLock.release();
                        return;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type android.os.PowerManager");
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "## turnScreenOn() failed", e);
            }
        } else if (VERSION.SDK_INT >= 26) {
            builder.setChannelId(str);
        }
    }

    public final Notification buildMessageNotification(Context context, boolean z) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Notification notification = null;
        try {
            RoomsNotifications loadRoomsNotifications = RoomsNotifications.loadRoomsNotifications(context);
            if (loadRoomsNotifications != null) {
                return buildMessageNotification(context, loadRoomsNotifications, new BingRule(), z);
            }
            return notification;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## buildMessageNotification() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return notification;
        }
    }

    public final Notification buildMessageNotification(Context context, Map<String, ? extends List<? extends NotifiedEvent>> map, NotifiedEvent notifiedEvent, boolean z) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(map, "notifiedEventsByRoomId");
        Intrinsics.checkParameterIsNotNull(notifiedEvent, "eventToNotify");
        Notification notification = null;
        try {
            RoomsNotifications roomsNotifications = new RoomsNotifications(notifiedEvent, map);
            BingRule bingRule = notifiedEvent.mBingRule;
            Intrinsics.checkExpressionValueIsNotNull(bingRule, "eventToNotify.mBingRule");
            notification = buildMessageNotification(context, roomsNotifications, bingRule, z);
            RoomsNotifications.saveRoomNotifications(context, roomsNotifications);
            return notification;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## buildMessageNotification() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return notification;
        }
    }

    private final Notification buildMessageNotification(Context context, RoomsNotifications roomsNotifications, BingRule bingRule, boolean z) {
        String str = "builder";
        try {
            Bitmap bitmap = null;
            if (!roomsNotifications.mIsInvitationEvent && !TextUtils.isEmpty(roomsNotifications.mRoomAvatarPath)) {
                Options options = new Options();
                options.inPreferredConfig = Config.ARGB_8888;
                try {
                    bitmap = BitmapFactory.decodeFile(roomsNotifications.mRoomAvatarPath, options);
                } catch (OutOfMemoryError e) {
                    Log.e(LOG_TAG, "decodeFile failed with an oom", e);
                }
            }
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("prepareNotification : with sound ");
            sb.append(BingRule.isDefaultNotificationSound(bingRule.getNotificationSound()));
            Log.d(str2, sb.toString());
            createNotificationChannels(context);
            Builder deleteIntent = new Builder(context, SILENT_NOTIFICATION_CHANNEL_ID).setWhen(roomsNotifications.mContentTs).setContentTitle(ensureTitleNotEmpty(context, roomsNotifications.mContentTitle)).setContentText(roomsNotifications.mContentText).setSmallIcon(R.drawable.logo_transparent).setNumber(roomsNotifications.getNumberOfRoomsWithMessages()).setDeleteIntent(PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(context.getApplicationContext(), DismissNotificationReceiver.class), 134217728));
            try {
                Intrinsics.checkExpressionValueIsNotNull(deleteIntent, str);
                addTextStyle(context, deleteIntent, roomsNotifications);
            } catch (Exception e2) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## buildMessageNotification() : addTextStyle failed ");
                sb2.append(e2.getMessage());
                Log.e(str3, sb2.toString(), e2);
            }
            if (roomsNotifications.mRoomNotifications.size() == 1 && bitmap != null) {
                deleteIntent.setLargeIcon(BitmapUtilKt.createSquareBitmap(bitmap));
            }
            Intrinsics.checkExpressionValueIsNotNull(deleteIntent, str);
            manageNotificationSound(context, deleteIntent, z, BingRule.isDefaultNotificationSound(bingRule.getNotificationSound()));
            return deleteIntent.build();
        } catch (Exception e3) {
            String str4 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## buildMessageNotification() : failed");
            sb3.append(e3.getMessage());
            Log.e(str4, sb3.toString(), e3);
            return null;
        }
    }

    public final Notification buildMessagesListNotification(Context context, List<? extends CharSequence> list, BingRule bingRule) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(list, "messagesStrings");
        Intrinsics.checkParameterIsNotNull(bingRule, "bingRule");
        try {
            createNotificationChannels(context);
            Builder number = new Builder(context, SILENT_NOTIFICATION_CHANNEL_ID).setWhen(System.currentTimeMillis()).setContentTitle(context.getString(R.string.tchap_app_name)).setContentText((CharSequence) list.get(0)).setSmallIcon(R.drawable.logo_transparent).setNumber(list.size());
            InboxStyle inboxStyle = new InboxStyle();
            int min = Math.min(10, list.size());
            for (int i = 0; i < min; i++) {
                inboxStyle.addLine((CharSequence) list.get(i));
            }
            inboxStyle.setBigContentTitle(context.getString(R.string.tchap_app_name)).setSummaryText(context.getResources().getQuantityString(R.plurals.notification_unread_notified_messages, list.size(), new Object[]{Integer.valueOf(list.size())}));
            number.setStyle(inboxStyle);
            TaskStackBuilder create = TaskStackBuilder.create(context);
            Intrinsics.checkExpressionValueIsNotNull(create, "TaskStackBuilder.create(context)");
            Intent intent = new Intent(context, VectorHomeActivity.class);
            StringBuilder sb = new StringBuilder();
            sb.append(TAP_TO_VIEW_ACTION);
            sb.append((int) System.currentTimeMillis());
            intent.setAction(sb.toString());
            create.addNextIntent(intent);
            number.setContentIntent(create.getPendingIntent(0, 134217728));
            Intrinsics.checkExpressionValueIsNotNull(number, "builder");
            manageNotificationSound(context, number, false, BingRule.isDefaultNotificationSound(bingRule.getNotificationSound()));
            return number.build();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## buildMessagesListNotification() : failed");
            sb2.append(e.getMessage());
            Log.e(str, sb2.toString(), e);
            return null;
        }
    }

    public final void showNotificationMessage(Context context, Notification notification) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(notification, "notification");
        NotificationManagerCompat.from(context).notify(60, notification);
    }

    public final void cancelNotificationMessage(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        NotificationManagerCompat.from(context).cancel(60);
    }

    public final void cancelNotificationForegroundService(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        NotificationManagerCompat.from(context).cancel(61);
    }

    public final void cancelAllNotifications(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        try {
            NotificationManagerCompat.from(context).cancelAll();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## cancelAllNotifications() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public final boolean isDoNotDisturbModeOn(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        boolean z = false;
        if (VERSION.SDK_INT < 23) {
            return false;
        }
        Object systemService = context.getSystemService("notification");
        if (systemService != null) {
            int currentInterruptionFilter = ((NotificationManager) systemService).getCurrentInterruptionFilter();
            if (currentInterruptionFilter == 3 || currentInterruptionFilter == 4) {
                z = true;
            }
            return z;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.app.NotificationManager");
    }

    private final CharSequence ensureTitleNotEmpty(Context context, String str) {
        CharSequence charSequence = str;
        if (TextUtils.isEmpty(charSequence)) {
            String string = context.getString(R.string.app_name);
            Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.app_name)");
            return string;
        }
        if (str == null) {
            Intrinsics.throwNpe();
        }
        return charSequence;
    }
}
