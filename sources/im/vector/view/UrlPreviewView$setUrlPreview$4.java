package im.vector.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import im.vector.util.ExternalApplicationsUtilKt;
import kotlin.Metadata;
import org.matrix.androidsdk.rest.model.URLPreview;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: UrlPreviewView.kt */
final class UrlPreviewView$setUrlPreview$4 implements OnClickListener {
    final /* synthetic */ Context $context;
    final /* synthetic */ URLPreview $preview;

    UrlPreviewView$setUrlPreview$4(Context context, URLPreview uRLPreview) {
        this.$context = context;
        this.$preview = uRLPreview;
    }

    public final void onClick(View view) {
        ExternalApplicationsUtilKt.openUrlInExternalBrowser(this.$context, this.$preview.getRequestedURL());
    }
}
