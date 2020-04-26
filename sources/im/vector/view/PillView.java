package im.vector.view;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.VectorApp;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.rest.model.User;

public class PillView extends LinearLayout {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = PillView.class.getSimpleName();
    /* access modifiers changed from: private */
    public PillImageView mAvatarView;
    /* access modifiers changed from: private */
    public OnUpdateListener mOnUpdateListener = null;
    private View mPillLayout;
    private TextView mTextView;

    public interface OnUpdateListener {
        void onAvatarUpdate();
    }

    public PillView(Context context) {
        super(context);
        initView();
    }

    public PillView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public PillView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.pill_view, this);
        this.mTextView = (TextView) findViewById(R.id.pill_text_view);
        this.mAvatarView = (PillImageView) findViewById(R.id.pill_avatar_view);
        this.mPillLayout = findViewById(R.id.pill_layout);
    }

    public static boolean isPillable(String str) {
        String linkedId = PermalinkUtils.getLinkedId(str);
        return linkedId != null && (MXPatterns.isRoomAlias(linkedId) || MXPatterns.isUserId(linkedId));
    }

    public void initData(CharSequence charSequence, String str, final MXSession mXSession, OnUpdateListener onUpdateListener) {
        String charSequence2 = charSequence.toString();
        this.mOnUpdateListener = onUpdateListener;
        this.mAvatarView.setOnUpdateListener(onUpdateListener);
        if (MXPatterns.isUserId(charSequence2)) {
            this.mTextView.setText(DinsicUtils.computeDisplayNameFromUserId(charSequence2));
        } else {
            this.mTextView.setText(charSequence2);
        }
        Theme theme = getContext().getTheme();
        int[] iArr = new int[1];
        iArr[0] = MXPatterns.isRoomAlias(charSequence2) ? R.attr.vctr_pill_background_room_alias : R.attr.vctr_pill_background_user_id;
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(iArr);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        this.mPillLayout.setBackground(ContextCompat.getDrawable(getContext(), resourceId));
        Theme theme2 = getContext().getTheme();
        int[] iArr2 = new int[1];
        iArr2[0] = MXPatterns.isRoomAlias(charSequence2) ? R.attr.vctr_pill_text_color_room_alias : R.attr.vctr_pill_text_color_user_id;
        TypedArray obtainStyledAttributes2 = theme2.obtainStyledAttributes(iArr2);
        int resourceId2 = obtainStyledAttributes2.getResourceId(0, 0);
        obtainStyledAttributes2.recycle();
        this.mTextView.setTextColor(ContextCompat.getColor(getContext(), resourceId2));
        final String linkedId = PermalinkUtils.getLinkedId(str);
        if (MXPatterns.isUserId(linkedId)) {
            User user = mXSession.getDataHandler().getUser(linkedId);
            if (user == null) {
                user = new User();
                user.user_id = linkedId;
                user.displayname = DinsicUtils.computeDisplayNameFromUserId(linkedId);
            }
            VectorUtils.loadUserAvatar(VectorApp.getInstance(), mXSession, this.mAvatarView, user);
            return;
        }
        mXSession.getDataHandler().roomIdByAlias(linkedId, new ApiCallback<String>() {
            public void onSuccess(String str) {
                if (PillView.this.mOnUpdateListener != null) {
                    Room room = mXSession.getDataHandler().getRoom(str, false);
                    if (room != null) {
                        VectorUtils.loadRoomAvatar((Context) VectorApp.getInstance(), mXSession, (ImageView) PillView.this.mAvatarView, room);
                        return;
                    }
                    PillView.this.mAvatarView.setImageBitmap(VectorUtils.getAvatar(VectorApp.getInstance(), VectorUtils.getAvatarColor(str), linkedId, true));
                    final RoomPreviewData roomPreviewData = new RoomPreviewData(mXSession, str, null, linkedId, null);
                    roomPreviewData.fetchPreviewData(new ApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            if (PillView.this.mOnUpdateListener != null) {
                                VectorUtils.loadRoomAvatar((Context) VectorApp.getInstance(), mXSession, (ImageView) PillView.this.mAvatarView, roomPreviewData);
                            }
                        }

                        public void onNetworkError(Exception exc) {
                            String access$200 = PillView.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## initData() : fetchPreviewData failed ");
                            sb.append(exc.getMessage());
                            Log.e(access$200, sb.toString(), exc);
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            String access$200 = PillView.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## initData() : fetchPreviewData failed ");
                            sb.append(matrixError.getMessage());
                            Log.e(access$200, sb.toString());
                        }

                        public void onUnexpectedError(Exception exc) {
                            String access$200 = PillView.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## initData() : fetchPreviewData failed ");
                            sb.append(exc.getMessage());
                            Log.e(access$200, sb.toString(), exc);
                        }
                    });
                }
            }

            public void onNetworkError(Exception exc) {
                String access$200 = PillView.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## initData() : roomIdByAlias failed ");
                sb.append(exc.getMessage());
                Log.e(access$200, sb.toString(), exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$200 = PillView.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## initData() : roomIdByAlias failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$200, sb.toString());
            }

            public void onUnexpectedError(Exception exc) {
                String access$200 = PillView.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## initData() : roomIdByAlias failed ");
                sb.append(exc.getMessage());
                Log.e(access$200, sb.toString(), exc);
            }
        });
    }

    public void setHighlighted(boolean z) {
        if (z) {
            this.mPillLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pill_background_bing));
            this.mTextView.setTextColor(ContextCompat.getColor(getContext(), 17170443));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0006, code lost:
        if (getDrawingCache() == null) goto L_0x0008;
     */
    public Drawable getDrawable(boolean z) {
        if (!z) {
            try {
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getDrawable() : failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        destroyDrawingCache();
        setDrawingCacheEnabled(true);
        measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        buildDrawingCache(true);
        if (getDrawingCache() != null) {
            return new BitmapDrawable(getContext().getResources(), Bitmap.createBitmap(getDrawingCache()));
        }
        return null;
    }
}
