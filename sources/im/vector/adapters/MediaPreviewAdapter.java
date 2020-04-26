package im.vector.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import fr.gouv.tchap.a.R;
import java.util.List;
import org.matrix.androidsdk.data.RoomMediaMessage;

public class MediaPreviewAdapter extends Adapter<MediaItemViewHolder> {
    /* access modifiers changed from: private */
    public final EventListener mEventListener;
    private final List<RoomMediaMessage> mImagePreviewList;

    public interface EventListener {
        void onMediaMessagePreviewClicked(RoomMediaMessage roomMediaMessage);
    }

    public static class MediaItemViewHolder extends ViewHolder {
        @BindView(2131296646)
        ImageView mImagePreview;

        MediaItemViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }

    public class MediaItemViewHolder_ViewBinding implements Unbinder {
        private MediaItemViewHolder target;

        public MediaItemViewHolder_ViewBinding(MediaItemViewHolder mediaItemViewHolder, View view) {
            this.target = mediaItemViewHolder;
            mediaItemViewHolder.mImagePreview = (ImageView) Utils.findRequiredViewAsType(view, R.id.image_preview, "field 'mImagePreview'", ImageView.class);
        }

        public void unbind() {
            MediaItemViewHolder mediaItemViewHolder = this.target;
            if (mediaItemViewHolder != null) {
                this.target = null;
                mediaItemViewHolder.mImagePreview = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public MediaPreviewAdapter(List<RoomMediaMessage> list, EventListener eventListener) {
        this.mImagePreviewList = list;
        this.mEventListener = eventListener;
    }

    public int getItemCount() {
        return this.mImagePreviewList.size();
    }

    public MediaItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MediaItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_media_preview, viewGroup, false));
    }

    public void onBindViewHolder(MediaItemViewHolder mediaItemViewHolder, int i) {
        Context context = mediaItemViewHolder.mImagePreview.getContext();
        final RoomMediaMessage roomMediaMessage = (RoomMediaMessage) this.mImagePreviewList.get(i);
        String mimeType = roomMediaMessage.getMimeType(context);
        Uri uri = roomMediaMessage.getUri();
        if (mimeType != null) {
            if (mimeType.startsWith("image") || mimeType.startsWith("video")) {
                Glide.with(context).asBitmap().load(uri).apply(new RequestOptions().frame(0)).into(mediaItemViewHolder.mImagePreview);
            } else {
                mediaItemViewHolder.mImagePreview.setImageResource(R.drawable.filetype_attachment);
                mediaItemViewHolder.mImagePreview.setScaleType(ScaleType.FIT_CENTER);
            }
        }
        mediaItemViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MediaPreviewAdapter.this.mEventListener.onMediaMessagePreviewClicked(roomMediaMessage);
            }
        });
    }
}
