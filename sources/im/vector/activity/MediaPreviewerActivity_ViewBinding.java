package im.vector.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class MediaPreviewerActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private MediaPreviewerActivity target;
    private View view7f0901d5;

    public MediaPreviewerActivity_ViewBinding(MediaPreviewerActivity mediaPreviewerActivity) {
        this(mediaPreviewerActivity, mediaPreviewerActivity.getWindow().getDecorView());
    }

    public MediaPreviewerActivity_ViewBinding(final MediaPreviewerActivity mediaPreviewerActivity, View view) {
        super(mediaPreviewerActivity, view);
        this.target = mediaPreviewerActivity;
        mediaPreviewerActivity.mPreviewerImageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.media_previewer_image_view, "field 'mPreviewerImageView'", ImageView.class);
        mediaPreviewerActivity.mPreviewerVideoView = (VideoView) Utils.findRequiredViewAsType(view, R.id.media_previewer_video_view, "field 'mPreviewerVideoView'", VideoView.class);
        mediaPreviewerActivity.mPreviewerVideoThumbnail = (ImageView) Utils.findRequiredViewAsType(view, R.id.media_previewer_video_thumbnail, "field 'mPreviewerVideoThumbnail'", ImageView.class);
        mediaPreviewerActivity.mPreviewerRecyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.media_previewer_list, "field 'mPreviewerRecyclerView'", RecyclerView.class);
        mediaPreviewerActivity.mFileNameView = (TextView) Utils.findRequiredViewAsType(view, R.id.media_previewer_file_name, "field 'mFileNameView'", TextView.class);
        mediaPreviewerActivity.mPlayCircleView = (ImageView) Utils.findRequiredViewAsType(view, R.id.media_previewer_video_play, "field 'mPlayCircleView'", ImageView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.media_previewer_send_button, "method 'onClick'");
        this.view7f0901d5 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                mediaPreviewerActivity.onClick();
            }
        });
    }

    public void unbind() {
        MediaPreviewerActivity mediaPreviewerActivity = this.target;
        if (mediaPreviewerActivity != null) {
            this.target = null;
            mediaPreviewerActivity.mPreviewerImageView = null;
            mediaPreviewerActivity.mPreviewerVideoView = null;
            mediaPreviewerActivity.mPreviewerVideoThumbnail = null;
            mediaPreviewerActivity.mPreviewerRecyclerView = null;
            mediaPreviewerActivity.mFileNameView = null;
            mediaPreviewerActivity.mPlayCircleView = null;
            this.view7f0901d5.setOnClickListener(null);
            this.view7f0901d5 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
