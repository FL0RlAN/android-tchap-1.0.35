package im.vector.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public final class UrlPreviewView_ViewBinding implements Unbinder {
    private UrlPreviewView target;
    private View view7f090381;

    public UrlPreviewView_ViewBinding(UrlPreviewView urlPreviewView) {
        this(urlPreviewView, urlPreviewView);
    }

    public UrlPreviewView_ViewBinding(final UrlPreviewView urlPreviewView, View view) {
        this.target = urlPreviewView;
        urlPreviewView.mImageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.url_preview_image_view, "field 'mImageView'", ImageView.class);
        urlPreviewView.mTitleTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.url_preview_title_text_view, "field 'mTitleTextView'", TextView.class);
        urlPreviewView.mDescriptionTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.url_preview_description_text_view, "field 'mDescriptionTextView'", TextView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.url_preview_hide_image_view, "method 'closeUrlPreview$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        this.view7f090381 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                urlPreviewView.closeUrlPreview$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
    }

    public void unbind() {
        UrlPreviewView urlPreviewView = this.target;
        if (urlPreviewView != null) {
            this.target = null;
            urlPreviewView.mImageView = null;
            urlPreviewView.mTitleTextView = null;
            urlPreviewView.mDescriptionTextView = null;
            this.view7f090381.setOnClickListener(null);
            this.view7f090381 = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
