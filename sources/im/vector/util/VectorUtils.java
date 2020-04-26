package im.vector.util;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.collection.LruCache;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.adapters.ParticipantAdapterItem;
import im.vector.settings.VectorLocale;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.core.ImageUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.group.Group;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

public class VectorUtils {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorUtils.class.getSimpleName();
    public static final int TAKE_IMAGE = 1;
    private static final LruCache<String, Bitmap> mAvatarImageByKeyDict = new LruCache<>(20971520);
    private static final List<Integer> mColorList;
    private static HandlerThread mImagesThread = null;
    /* access modifiers changed from: private */
    public static Handler mImagesThreadHandler = null;
    /* access modifiers changed from: private */
    public static Handler mUIHandler = null;
    private static final Pattern mUrlPattern = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)", 42);

    static {
        Integer valueOf = Integer.valueOf(-7632487);
        mColorList = new ArrayList(Arrays.asList(new Integer[]{valueOf, valueOf, valueOf}));
    }

    public static String getPublicRoomDisplayName(PublicRoom publicRoom) {
        String str = publicRoom.name;
        if (TextUtils.isEmpty(str)) {
            if (publicRoom.aliases == null || publicRoom.aliases.isEmpty()) {
                return publicRoom.roomId;
            }
            return (String) publicRoom.aliases.get(0);
        } else if (str.startsWith("#") || publicRoom.aliases == null || publicRoom.aliases.isEmpty()) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" (");
            sb.append((String) publicRoom.aliases.get(0));
            sb.append(")");
            return sb.toString();
        }
    }

    public static void getCallingRoomDisplayName(Context context, final MXSession mXSession, final Room room, final ApiCallback<String> apiCallback) {
        if (context == null || mXSession == null || room == null) {
            apiCallback.onSuccess(null);
        } else if (room.getNumberOfJoinedMembers() == 2) {
            room.getJoinedMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
                public void onSuccess(List<RoomMember> list) {
                    if (TextUtils.equals(((RoomMember) list.get(0)).getUserId(), mXSession.getMyUserId())) {
                        apiCallback.onSuccess(room.getState().getMemberName(((RoomMember) list.get(1)).getUserId()));
                    } else {
                        apiCallback.onSuccess(room.getState().getMemberName(((RoomMember) list.get(0)).getUserId()));
                    }
                }
            });
        } else {
            apiCallback.onSuccess(DinsicUtils.getRoomDisplayName(context, room));
        }
    }

    public static int getAvatarColor(String str) {
        long j = 0;
        if (!TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                j += (long) str.charAt(i);
            }
            j %= (long) mColorList.size();
        }
        return ((Integer) mColorList.get((int) j)).intValue();
    }

    private static Bitmap createAvatarThumbnail(Context context, int i, String str) {
        return createAvatar(i, str, (int) (context.getResources().getDisplayMetrics().density * 42.0f));
    }

    private static Bitmap createAvatar(int i, String str, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(i);
        Paint paint = new Paint();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        paint.setColor(-1);
        paint.setTextSize((float) ((i2 * 2) / 3));
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        canvas.drawText(str, (float) (((canvas.getWidth() - rect.width()) - rect.left) / 2), (float) (((canvas.getHeight() + rect.height()) - rect.bottom) / 2), paint);
        return createBitmap;
    }

    private static String getInitialLetter(String str) {
        String str2;
        if (!TextUtils.isEmpty(str)) {
            int i = 0;
            char charAt = str.charAt(0);
            int i2 = 1;
            if ((charAt == '@' || charAt == '#' || charAt == '+') && str.length() > 1) {
                i = 1;
            }
            char charAt2 = str.charAt(i);
            if (str.length() >= 2 && 8206 == charAt2) {
                i++;
                charAt2 = str.charAt(i);
            }
            if (55296 <= charAt2 && charAt2 <= 56319) {
                int i3 = i + 1;
                if (str.length() > i3) {
                    char charAt3 = str.charAt(i3);
                    if (56320 <= charAt3 && charAt3 <= 57343) {
                        i2 = 2;
                    }
                }
            }
            str2 = str.substring(i, i2 + i);
        } else {
            str2 = " ";
        }
        return str2.toUpperCase(VectorLocale.INSTANCE.getApplicationLocale());
    }

    public static Bitmap getAvatar(Context context, int i, String str, boolean z) {
        String initialLetter = getInitialLetter(str);
        StringBuilder sb = new StringBuilder();
        sb.append(initialLetter);
        sb.append("_");
        sb.append(i);
        String sb2 = sb.toString();
        Bitmap bitmap = (Bitmap) mAvatarImageByKeyDict.get(sb2);
        if (bitmap != null || !z) {
            return bitmap;
        }
        Bitmap createAvatarThumbnail = createAvatarThumbnail(context, i, initialLetter);
        mAvatarImageByKeyDict.put(sb2, createAvatarThumbnail);
        return createAvatarThumbnail;
    }

    /* access modifiers changed from: private */
    public static void setDefaultMemberAvatar(final ImageView imageView, String str, String str2) {
        if (imageView != null && !TextUtils.isEmpty(str)) {
            final Bitmap avatar = getAvatar(imageView.getContext(), getAvatarColor(str), TextUtils.isEmpty(str2) ? str : str2, true);
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                imageView.setImageBitmap(avatar);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" - ");
            sb.append(str2);
            final String sb2 = sb.toString();
            imageView.setTag(sb2);
            mUIHandler.post(new Runnable() {
                public void run() {
                    if (TextUtils.equals(sb2, (String) imageView.getTag())) {
                        imageView.setImageBitmap(avatar);
                    }
                }
            });
        }
    }

    public static void loadRoomAvatar(Context context, MXSession mXSession, ImageView imageView, Room room) {
        if (room != null) {
            loadUserAvatar(context, mXSession, imageView, DinsicUtils.getRoomAvatarUrl(room), room.getRoomId(), DinsicUtils.getRoomDisplayName(context, room));
        }
    }

    public static void loadRoomAvatar(Context context, MXSession mXSession, ImageView imageView, RoomPreviewData roomPreviewData) {
        if (roomPreviewData != null) {
            loadUserAvatar(context, mXSession, imageView, roomPreviewData.getRoomAvatarUrl(), roomPreviewData.getRoomId(), roomPreviewData.getRoomName());
        }
    }

    public static void loadGroupAvatar(Context context, MXSession mXSession, ImageView imageView, Group group) {
        if (group != null) {
            loadUserAvatar(context, mXSession, imageView, group.getAvatarUrl(), group.getGroupId(), group.getDisplayName());
        }
    }

    public static void loadCallAvatar(Context context, MXSession mXSession, ImageView imageView, Room room) {
        if (room != null && mXSession != null && imageView != null && mXSession.isAlive()) {
            Bitmap bitmap = null;
            imageView.setTag(null);
            String callAvatarUrl = room.getCallAvatarUrl();
            String roomId = room.getRoomId();
            String roomDisplayName = DinsicUtils.getRoomDisplayName(context, room);
            int i = imageView.getLayoutParams().width;
            if (i < 0) {
                ViewParent parent = imageView.getParent();
                while (i < 0 && parent != null) {
                    if (parent instanceof View) {
                        i = ((View) parent).getLayoutParams().width;
                    }
                    parent = parent.getParent();
                }
            }
            if (mXSession.getMediaCache().isAvatarThumbnailCached(callAvatarUrl, context.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size))) {
                mXSession.getMediaCache().loadAvatarThumbnail(mXSession.getHomeServerConfig(), imageView, callAvatarUrl, context.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size));
                return;
            }
            if (i > 0) {
                bitmap = createAvatar(getAvatarColor(roomId), getInitialLetter(roomDisplayName), i);
            }
            mXSession.getMediaCache().loadAvatarThumbnail(mXSession.getHomeServerConfig(), imageView, callAvatarUrl, context.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size), bitmap);
        }
    }

    public static void loadRoomMemberAvatar(Context context, MXSession mXSession, ImageView imageView, RoomMember roomMember) {
        if (roomMember != null) {
            loadUserAvatar(context, mXSession, imageView, roomMember.getAvatarUrl(), roomMember.getUserId(), roomMember.displayname);
        }
    }

    public static void loadUserAvatar(Context context, MXSession mXSession, ImageView imageView, User user) {
        if (user != null) {
            loadUserAvatar(context, mXSession, imageView, user.getAvatarUrl(), user.user_id, user.displayname);
        }
    }

    public static void loadUserAvatar(Context context, MXSession mXSession, ImageView imageView, String str, String str2, String str3) {
        if (mXSession != null && imageView != null && mXSession.isAlive()) {
            imageView.setTag(null);
            if (mXSession.getMediaCache().isAvatarThumbnailCached(str, context.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size))) {
                mXSession.getMediaCache().loadAvatarThumbnail(mXSession.getHomeServerConfig(), imageView, str, context.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size));
                return;
            }
            if (mImagesThread == null) {
                mImagesThread = new HandlerThread("ImagesThread", 1);
                mImagesThread.start();
                mImagesThreadHandler = new Handler(mImagesThread.getLooper());
                mUIHandler = new Handler(Looper.getMainLooper());
            }
            final Bitmap avatar = getAvatar(imageView.getContext(), getAvatarColor(str2), TextUtils.isEmpty(str3) ? str2 : str3, false);
            if (avatar != null) {
                imageView.setImageBitmap(avatar);
                if (!TextUtils.isEmpty(str)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(str2);
                    sb.append(str3);
                    final String sb2 = sb.toString();
                    imageView.setTag(sb2);
                    if (!MXMediaCache.isMediaUrlUnreachable(str)) {
                        Handler handler = mImagesThreadHandler;
                        final ImageView imageView2 = imageView;
                        final MXSession mXSession2 = mXSession;
                        final String str4 = str;
                        final Context context2 = context;
                        AnonymousClass3 r0 = new Runnable() {
                            public void run() {
                                if (TextUtils.equals(sb2, (String) imageView2.getTag())) {
                                    mXSession2.getMediaCache().loadAvatarThumbnail(mXSession2.getHomeServerConfig(), imageView2, str4, context2.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size), avatar);
                                }
                            }
                        };
                        handler.post(r0);
                        return;
                    }
                    return;
                }
                return;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("00");
            sb3.append(str);
            sb3.append(HelpFormatter.DEFAULT_OPT_PREFIX);
            sb3.append(str2);
            sb3.append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
            sb3.append(str3);
            final String sb4 = sb3.toString();
            imageView.setTag(sb4);
            Handler handler2 = mImagesThreadHandler;
            final ImageView imageView3 = imageView;
            final String str5 = str2;
            final String str6 = str3;
            final String str7 = str;
            final MXSession mXSession3 = mXSession;
            final Context context3 = context;
            AnonymousClass4 r02 = new Runnable() {
                public void run() {
                    if (TextUtils.equals(sb4, (String) imageView3.getTag())) {
                        imageView3.setTag(null);
                        VectorUtils.setDefaultMemberAvatar(imageView3, str5, str6);
                        if (!TextUtils.isEmpty(str7) && !MXMediaCache.isMediaUrlUnreachable(str7)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("11");
                            sb.append(str7);
                            sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
                            sb.append(str5);
                            sb.append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
                            sb.append(str6);
                            final String sb2 = sb.toString();
                            imageView3.setTag(sb2);
                            VectorUtils.mUIHandler.post(new Runnable() {
                                public void run() {
                                    if (TextUtils.equals(sb2, (String) imageView3.getTag())) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("22");
                                        sb.append(str7);
                                        sb.append(str5);
                                        sb.append(str6);
                                        final String sb2 = sb.toString();
                                        imageView3.setTag(sb2);
                                        VectorUtils.mImagesThreadHandler.post(new Runnable() {
                                            public void run() {
                                                if (TextUtils.equals(sb2, (String) imageView3.getTag())) {
                                                    mXSession3.getMediaCache().loadAvatarThumbnail(mXSession3.getHomeServerConfig(), imageView3, str7, context3.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size), VectorUtils.getAvatar(imageView3.getContext(), VectorUtils.getAvatarColor(str5), TextUtils.isEmpty(str6) ? str5 : str6, false));
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            };
            handler2.post(r02);
        }
    }

    public static String getApplicationVersion(Context context) {
        return Matrix.getInstance(context).getVersion(false, true);
    }

    private static void displayInWebView(final Context context, String str) {
        WebView webView = new WebView(context);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                new Builder(context).setMessage((int) R.string.ssl_could_not_verify).setPositiveButton((int) R.string.ssl_trust, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                }).setNegativeButton((int) R.string.ssl_do_not_trust, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                }).show();
            }
        });
        webView.loadUrl(str);
        new Builder(context).setView((View) webView).setPositiveButton(17039370, (OnClickListener) null).show();
    }

    public static void displayAppCopyright() {
        if (VectorApp.getCurrentActivity() != null) {
            displayInWebView(VectorApp.getCurrentActivity(), "https://www.tchap.gouv.fr/copyright");
        }
    }

    public static void displayAppTac() {
        if (VectorApp.getCurrentActivity() != null) {
            displayInWebView(VectorApp.getCurrentActivity(), "https://www.tchap.gouv.fr/tac.html");
        }
    }

    public static void displayAppPrivacyPolicy() {
        if (VectorApp.getCurrentActivity() != null) {
            displayInWebView(VectorApp.getCurrentActivity(), "https://www.tchap.gouv.fr/privacy");
        }
    }

    public static void displayThirdPartyLicenses() {
        if (VectorApp.getCurrentActivity() != null) {
            displayInWebView(VectorApp.getCurrentActivity(), "file:///android_asset/open_source_licenses.html");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0033 A[SYNTHETIC, Splitter:B:17:0x0033] */
    public static Uri getThumbnailUriFromIntent(Context context, Intent intent, MXMediaCache mXMediaCache) {
        Uri uri;
        if (!(intent == null || context == null || mXMediaCache == null)) {
            ClipData clipData = VERSION.SDK_INT >= 18 ? intent.getClipData() : null;
            if (clipData != null) {
                if (clipData.getItemCount() > 0) {
                    uri = clipData.getItemAt(0).getUri();
                    if (uri != null) {
                        try {
                            Resource openResource = ResourceUtils.openResource(context, uri, null);
                            if (openResource == null || !openResource.isJpegResource()) {
                                String str = "## getThumbnailUriFromIntent() : cannot manage ";
                                if (openResource != null) {
                                    String str2 = LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str);
                                    sb.append(uri);
                                    sb.append(" mMimeType ");
                                    sb.append(openResource.mMimeType);
                                    Log.d(str2, sb.toString());
                                } else {
                                    String str3 = LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(str);
                                    sb2.append(uri);
                                    sb2.append(" --> cannot open the dedicated file");
                                    Log.d(str3, sb2.toString());
                                }
                            } else {
                                InputStream inputStream = openResource.mContentStream;
                                int rotationAngleForBitmap = ImageUtils.getRotationAngleForBitmap(context, uri);
                                String str4 = LOG_TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("## getThumbnailUriFromIntent() :  ");
                                sb3.append(uri);
                                sb3.append(" rotationAngle ");
                                sb3.append(rotationAngleForBitmap);
                                Log.d(str4, sb3.toString());
                                uri = Uri.parse(ImageUtils.scaleAndRotateImage(context, inputStream, openResource.mMimeType, 1024, rotationAngleForBitmap, mXMediaCache));
                            }
                            return uri;
                        } catch (Exception e) {
                            String str5 = LOG_TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("## getThumbnailUriFromIntent failed ");
                            sb4.append(e.getMessage());
                            Log.e(str5, sb4.toString(), e);
                        }
                    }
                }
            } else if (intent.getData() != null) {
                uri = intent.getData();
                if (uri != null) {
                }
            }
            uri = null;
            if (uri != null) {
            }
        }
        return null;
    }

    private static String formatSecondsIntervalFloored(Context context, long j) {
        if (j < 0) {
            return context.getResources().getQuantityString(R.plurals.format_time_s, 0, new Object[]{Integer.valueOf(0)});
        } else if (j < 60) {
            int i = (int) j;
            return context.getResources().getQuantityString(R.plurals.format_time_s, i, new Object[]{Integer.valueOf(i)});
        } else if (j < 3600) {
            int i2 = (int) (j / 60);
            return context.getResources().getQuantityString(R.plurals.format_time_m, i2, new Object[]{Integer.valueOf(i2)});
        } else if (j < 86400) {
            int i3 = (int) (j / 3600);
            return context.getResources().getQuantityString(R.plurals.format_time_h, i3, new Object[]{Integer.valueOf(i3)});
        } else {
            int i4 = (int) (j / 86400);
            return context.getResources().getQuantityString(R.plurals.format_time_d, i4, new Object[]{Integer.valueOf(i4)});
        }
    }

    public static String getUserOnlineStatus(Context context, MXSession mXSession, String str, ApiCallback<Void> apiCallback) {
        String str2 = null;
        if (!(mXSession == null || str == null)) {
            User user = mXSession.getDataHandler().getStore().getUser(str);
            boolean z = user == null || user.isPresenceObsolete();
            if (apiCallback != null && z) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Get the user presence : ");
                sb.append(str);
                Log.d(str3, sb.toString());
                final String str4 = user != null ? user.presence : null;
                final MXSession mXSession2 = mXSession;
                final String str5 = str;
                final User user2 = user;
                final ApiCallback<Void> apiCallback2 = apiCallback;
                AnonymousClass6 r2 = new ApiCallback<Void>() {
                    /* JADX WARNING: Removed duplicated region for block: B:14:0x00b2  */
                    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
                    public void onSuccess(Void voidR) {
                        User user = mXSession2.getDataHandler().getStore().getUser(str5);
                        boolean z = true;
                        if (user2 == null && user == null) {
                            String access$300 = VectorUtils.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Don't find any presence info of ");
                            sb.append(str5);
                            Log.d(access$300, sb.toString());
                        } else if (user2 == null && user != null) {
                            String access$3002 = VectorUtils.LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Got the user presence : ");
                            sb2.append(str5);
                            Log.d(access$3002, sb2.toString());
                            if (z) {
                            }
                        } else if (user != null && !TextUtils.equals(str4, user.presence)) {
                            String access$3003 = VectorUtils.LOG_TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Got some new user presence info : ");
                            sb3.append(str5);
                            Log.d(access$3003, sb3.toString());
                            String access$3004 = VectorUtils.LOG_TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("currently_active : ");
                            sb4.append(user.currently_active);
                            Log.d(access$3004, sb4.toString());
                            String access$3005 = VectorUtils.LOG_TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("lastActiveAgo : ");
                            sb5.append(user.lastActiveAgo);
                            Log.d(access$3005, sb5.toString());
                            if (z) {
                                ApiCallback apiCallback = apiCallback2;
                                if (apiCallback != null) {
                                    try {
                                        apiCallback.onSuccess(null);
                                        return;
                                    } catch (Exception e) {
                                        Log.e(VectorUtils.LOG_TAG, "getUserOnlineStatus refreshCallback failed", e);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        z = false;
                        if (z) {
                        }
                    }

                    public void onNetworkError(Exception exc) {
                        String access$300 = VectorUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("getUserOnlineStatus onNetworkError ");
                        sb.append(exc.getLocalizedMessage());
                        Log.e(access$300, sb.toString(), exc);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        String access$300 = VectorUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("getUserOnlineStatus onMatrixError ");
                        sb.append(matrixError.getLocalizedMessage());
                        Log.e(access$300, sb.toString());
                    }

                    public void onUnexpectedError(Exception exc) {
                        String access$300 = VectorUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("getUserOnlineStatus onUnexpectedError ");
                        sb.append(exc.getLocalizedMessage());
                        Log.e(access$300, sb.toString(), exc);
                    }
                };
                mXSession.refreshUserPresence(str, r2);
            }
            if (user == null) {
                return null;
            }
            if (TextUtils.equals(user.presence, User.PRESENCE_ONLINE)) {
                str2 = context.getString(R.string.room_participants_online);
            } else if (TextUtils.equals(user.presence, User.PRESENCE_UNAVAILABLE)) {
                str2 = context.getString(R.string.room_participants_idle);
            } else if (TextUtils.equals(user.presence, User.PRESENCE_OFFLINE) || user.presence == null) {
                str2 = context.getString(R.string.room_participants_offline);
            }
            if (str2 != null) {
                if (user.currently_active != null && user.currently_active.booleanValue()) {
                    str2 = context.getString(R.string.room_participants_now, new Object[]{str2});
                } else if (user.lastActiveAgo != null && user.lastActiveAgo.longValue() > 0) {
                    str2 = context.getString(R.string.room_participants_ago, new Object[]{str2, formatSecondsIntervalFloored(context, user.getAbsoluteLastActiveAgo() / 1000)});
                }
            }
        }
        return str2;
    }

    public static boolean isUserOnline(Context context, MXSession mXSession, String str, SimpleApiCallback<Void> simpleApiCallback) {
        if (mXSession == null || str == null) {
            return false;
        }
        User user = mXSession.getDataHandler().getStore().getUser(str);
        boolean z = user == null || user.isPresenceObsolete();
        if (simpleApiCallback != null && z) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Get the user presence : ");
            sb.append(str);
            Log.d(str2, sb.toString());
            final String str3 = user != null ? user.presence : null;
            final MXSession mXSession2 = mXSession;
            final String str4 = str;
            final User user2 = user;
            final SimpleApiCallback<Void> simpleApiCallback2 = simpleApiCallback;
            AnonymousClass7 r1 = new ApiCallback<Void>() {
                /* JADX WARNING: Removed duplicated region for block: B:14:0x00b2  */
                /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
                public void onSuccess(Void voidR) {
                    User user = mXSession2.getDataHandler().getStore().getUser(str4);
                    boolean z = true;
                    if (user2 == null && user == null) {
                        String access$300 = VectorUtils.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Don't find any presence info of ");
                        sb.append(str4);
                        Log.d(access$300, sb.toString());
                    } else if (user2 == null && user != null) {
                        String access$3002 = VectorUtils.LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Got the user presence : ");
                        sb2.append(str4);
                        Log.d(access$3002, sb2.toString());
                        if (z) {
                        }
                    } else if (user != null && !TextUtils.equals(str3, user.presence)) {
                        String access$3003 = VectorUtils.LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Got some new user presence info : ");
                        sb3.append(str4);
                        Log.d(access$3003, sb3.toString());
                        String access$3004 = VectorUtils.LOG_TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("currently_active : ");
                        sb4.append(user.currently_active);
                        Log.d(access$3004, sb4.toString());
                        String access$3005 = VectorUtils.LOG_TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("lastActiveAgo : ");
                        sb5.append(user.lastActiveAgo);
                        Log.d(access$3005, sb5.toString());
                        if (z) {
                            SimpleApiCallback simpleApiCallback = simpleApiCallback2;
                            if (simpleApiCallback != null) {
                                try {
                                    simpleApiCallback.onSuccess(null);
                                    return;
                                } catch (Exception unused) {
                                    Log.e(VectorUtils.LOG_TAG, "getUserOnlineStatus refreshCallback failed");
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    z = false;
                    if (z) {
                    }
                }

                public void onNetworkError(Exception exc) {
                    String access$300 = VectorUtils.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getUserOnlineStatus onNetworkError ");
                    sb.append(exc.getLocalizedMessage());
                    Log.e(access$300, sb.toString());
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$300 = VectorUtils.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getUserOnlineStatus onMatrixError ");
                    sb.append(matrixError.getLocalizedMessage());
                    Log.e(access$300, sb.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    String access$300 = VectorUtils.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getUserOnlineStatus onUnexpectedError ");
                    sb.append(exc.getLocalizedMessage());
                    Log.e(access$300, sb.toString());
                }
            };
            mXSession.refreshUserPresence(str, r1);
        }
        if (user == null || !TextUtils.equals(user.presence, User.PRESENCE_ONLINE)) {
            return false;
        }
        return true;
    }

    public static Map<String, ParticipantAdapterItem> listKnownParticipants(MXSession mXSession) {
        Collection<User> users = mXSession.getDataHandler().getStore().getUsers();
        HashMap hashMap = new HashMap(users.size());
        for (User user : users) {
            if (!MXCallsManager.isConferenceUserId(user.user_id)) {
                if (user.displayname == null) {
                    user.displayname = DinsicUtils.computeDisplayNameFromUserId(user.user_id);
                }
                hashMap.put(user.user_id, new ParticipantAdapterItem(user));
            }
        }
        return hashMap;
    }

    public static Map<Integer, List<Integer>> getVisibleChildViews(ExpandableListView expandableListView, BaseExpandableListAdapter baseExpandableListAdapter) {
        int i;
        HashMap hashMap = new HashMap();
        long expandableListPosition = expandableListView.getExpandableListPosition(expandableListView.getFirstVisiblePosition());
        int packedPositionGroup = ExpandableListView.getPackedPositionGroup(expandableListPosition);
        int packedPositionChild = ExpandableListView.getPackedPositionChild(expandableListPosition);
        long expandableListPosition2 = expandableListView.getExpandableListPosition(expandableListView.getLastVisiblePosition());
        int packedPositionGroup2 = ExpandableListView.getPackedPositionGroup(expandableListPosition2);
        int packedPositionChild2 = ExpandableListView.getPackedPositionChild(expandableListPosition2);
        int i2 = packedPositionGroup;
        while (i2 <= packedPositionGroup2) {
            ArrayList arrayList = new ArrayList();
            if (i2 == packedPositionGroup2) {
                i = packedPositionChild2;
            } else {
                i = baseExpandableListAdapter.getChildrenCount(i2) - 1;
            }
            for (int i3 = i2 == packedPositionGroup ? packedPositionChild : 0; i3 <= i; i3++) {
                arrayList.add(Integer.valueOf(i3));
            }
            hashMap.put(Integer.valueOf(i2), arrayList);
            i2++;
        }
        return hashMap;
    }
}
