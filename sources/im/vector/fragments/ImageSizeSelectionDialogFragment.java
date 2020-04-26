package im.vector.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.fragment.app.DialogFragment;
import fr.gouv.tchap.a.R;
import im.vector.adapters.ImageCompressionDescription;
import im.vector.adapters.ImageSizesAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageSizeSelectionDialogFragment extends DialogFragment {
    private static final String SELECTIONS_LIST = "SELECTIONS_LIST";
    private List<ImageCompressionDescription> mEntries = null;
    /* access modifiers changed from: private */
    public ImageSizeListener mListener = null;

    public interface ImageSizeListener {
        void onSelected(int i);
    }

    public static ImageSizeSelectionDialogFragment newInstance(Collection<ImageCompressionDescription> collection) {
        ImageSizeSelectionDialogFragment imageSizeSelectionDialogFragment = new ImageSizeSelectionDialogFragment();
        imageSizeSelectionDialogFragment.setArguments(new Bundle());
        imageSizeSelectionDialogFragment.setEntries(collection);
        return imageSizeSelectionDialogFragment;
    }

    private void setEntries(Collection<ImageCompressionDescription> collection) {
        this.mEntries = new ArrayList(collection);
    }

    public void setListener(ImageSizeListener imageSizeListener) {
        this.mListener = imageSizeListener;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        List<ImageCompressionDescription> list = this.mEntries;
        if (list != null) {
            bundle.putSerializable(SELECTIONS_LIST, (ArrayList) list);
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        if (bundle != null) {
            String str = SELECTIONS_LIST;
            if (bundle.containsKey(str)) {
                this.mEntries = (ArrayList) bundle.getSerializable(str);
            }
        }
        onCreateDialog.setTitle(getString(R.string.compression_options));
        return onCreateDialog;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View inflate = layoutInflater.inflate(R.layout.dialog_base_list_view, viewGroup, false);
        ListView listView = (ListView) inflate.findViewById(R.id.list_view);
        ImageSizesAdapter imageSizesAdapter = new ImageSizesAdapter(getActivity(), R.layout.adapter_item_image_size);
        List<ImageCompressionDescription> list = this.mEntries;
        if (list != null) {
            imageSizesAdapter.addAll(list);
        }
        listView.setAdapter(imageSizesAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (ImageSizeSelectionDialogFragment.this.mListener != null) {
                    ImageSizeSelectionDialogFragment.this.mListener.onSelected(i);
                }
                ImageSizeSelectionDialogFragment.this.dismiss();
            }
        });
        return inflate;
    }
}
