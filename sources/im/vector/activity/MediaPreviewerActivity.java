package im.vector.activity;

import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import fr.gouv.tchap.a.R;
import im.vector.adapters.MediaPreviewAdapter;
import im.vector.adapters.MediaPreviewAdapter.EventListener;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.RoomMediaMessage;

public class MediaPreviewerActivity extends MXCActionBarActivity implements EventListener {
    public static final String EXTRA_CAMERA_PICTURE_URI = "EXTRA_CAMERA_PICTURE_URI";
    public static final String EXTRA_ROOM_TITLE = "EXTRA_ROOM_TITLE";
    private static final String LOG_TAG = MediaPreviewerActivity.class.getSimpleName();
    private RoomMediaMessage mCurrentRoomMediaMessage;
    @BindView(2131296722)
    TextView mFileNameView;
    @BindView(2131296726)
    ImageView mPlayCircleView;
    @BindView(2131296723)
    ImageView mPreviewerImageView;
    @BindView(2131296724)
    RecyclerView mPreviewerRecyclerView;
    @BindView(2131296727)
    ImageView mPreviewerVideoThumbnail;
    @BindView(2131296728)
    VideoView mPreviewerVideoView;

    public int getLayoutRes() {
        return R.layout.activity_media_previewer;
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.d(LOG_TAG, "onCreate : restart the application");
            CommonActivityUtils.restartApp(this);
        } else if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
        } else {
            this.mPreviewerVideoView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    MediaPreviewerActivity.this.onVideoPreviewClicked();
                    return false;
                }
            });
            this.mPlayCircleView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    MediaPreviewerActivity.this.onVideoPreviewClicked();
                    return false;
                }
            });
            configureToolbar();
            getSupportActionBar().setTitle((CharSequence) getIntent().getExtras().getString(EXTRA_ROOM_TITLE));
            setupRecyclerView(getSharedItems());
        }
    }

    @OnClick({2131296725})
    public void onClick() {
        setResult(-1, getIntent());
        finish();
    }

    public void onMediaMessagePreviewClicked(RoomMediaMessage roomMediaMessage) {
        if (roomMediaMessage != this.mCurrentRoomMediaMessage) {
            this.mCurrentRoomMediaMessage = roomMediaMessage;
            updatePreview(roomMediaMessage);
        }
    }

    private void updatePreview(RoomMediaMessage roomMediaMessage) {
        this.mPreviewerVideoView.pause();
        this.mFileNameView.setText(roomMediaMessage.getFileName(this));
        String mimeType = roomMediaMessage.getMimeType(this);
        Uri uri = roomMediaMessage.getUri();
        if (mimeType == null) {
            return;
        }
        if (mimeType.startsWith("image")) {
            this.mPreviewerImageView.setVisibility(0);
            this.mPreviewerVideoView.setVisibility(8);
            this.mPreviewerVideoThumbnail.setVisibility(8);
            this.mPlayCircleView.setVisibility(8);
            Glide.with((FragmentActivity) this).load(uri).apply(new RequestOptions().fitCenter()).into(this.mPreviewerImageView);
        } else if (mimeType.startsWith("video")) {
            this.mPreviewerImageView.setVisibility(8);
            this.mPreviewerVideoView.setVisibility(8);
            this.mPreviewerVideoThumbnail.setVisibility(0);
            this.mPlayCircleView.setVisibility(0);
            Glide.with((FragmentActivity) this).load(uri).apply(new RequestOptions().fitCenter().frame(0)).into(this.mPreviewerVideoThumbnail);
            this.mPreviewerVideoView.setVideoURI(uri);
            this.mPreviewerVideoView.seekTo(0);
        } else {
            this.mPreviewerImageView.setVisibility(0);
            this.mPreviewerVideoView.setVisibility(8);
            this.mPreviewerVideoThumbnail.setVisibility(8);
            this.mPreviewerImageView.setImageResource(R.drawable.filetype_attachment);
        }
    }

    private void setupRecyclerView(List<RoomMediaMessage> list) {
        this.mPreviewerRecyclerView.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.mPreviewerRecyclerView.setAdapter(new MediaPreviewAdapter(list, this));
        updatePreview((RoomMediaMessage) list.get(0));
    }

    private List<RoomMediaMessage> getSharedItems() {
        List<RoomMediaMessage> listRoomMediaMessages = RoomMediaMessage.listRoomMediaMessages(getIntent());
        if (listRoomMediaMessages.isEmpty()) {
            listRoomMediaMessages.add(new RoomMediaMessage(Uri.parse(getIntent().getStringExtra(EXTRA_CAMERA_PICTURE_URI))));
        }
        return listRoomMediaMessages;
    }

    /* access modifiers changed from: private */
    public void onVideoPreviewClicked() {
        if (!this.mPreviewerVideoView.isPlaying()) {
            this.mPreviewerVideoView.setVisibility(0);
            this.mPreviewerVideoThumbnail.setVisibility(8);
            this.mPlayCircleView.setVisibility(8);
            this.mPreviewerVideoView.start();
            return;
        }
        this.mPreviewerVideoThumbnail.setVisibility(0);
        this.mPlayCircleView.setVisibility(0);
        this.mPreviewerVideoView.setVisibility(8);
        this.mPreviewerVideoView.pause();
    }
}
