package im.vector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.gouv.tchap.a.R;

public class ImageSizesAdapter extends ArrayAdapter<ImageCompressionDescription> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater = LayoutInflater.from(this.mContext);
    private final int mLayoutResourceId;

    public ImageSizesAdapter(Context context, int i) {
        super(context, i);
        this.mContext = context;
        this.mLayoutResourceId = i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mLayoutResourceId, viewGroup, false);
        }
        ImageCompressionDescription imageCompressionDescription = (ImageCompressionDescription) getItem(i);
        ((TextView) view.findViewById(R.id.ImageSizesAdapter_format)).setText(imageCompressionDescription.mCompressionText);
        ((TextView) view.findViewById(R.id.ImageSizesAdapter_info)).setText(imageCompressionDescription.mCompressionInfoText);
        return view;
    }
}
