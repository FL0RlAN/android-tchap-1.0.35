package im.vector.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;

public class VectorUnknownDevicesAdapter extends BaseExpandableListAdapter {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorUnknownDevicesAdapter.class.getSimpleName();
    private final LayoutInflater mLayoutInflater;
    /* access modifiers changed from: private */
    public IVerificationAdapterListener mListener;
    private final List<Pair<String, List<MXDeviceInfo>>> mUnknownDevicesList;

    public interface IVerificationAdapterListener {
        void OnBlockDeviceClick(MXDeviceInfo mXDeviceInfo);

        void OnVerifyDeviceClick(MXDeviceInfo mXDeviceInfo);
    }

    public Object getChild(int i, int i2) {
        return null;
    }

    public long getChildId(int i, int i2) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    public VectorUnknownDevicesAdapter(Context context, List<Pair<String, List<MXDeviceInfo>>> list) {
        this.mLayoutInflater = LayoutInflater.from(context);
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mUnknownDevicesList = list;
    }

    public void setListener(IVerificationAdapterListener iVerificationAdapterListener) {
        this.mListener = iVerificationAdapterListener;
    }

    private String getGroupTitle(int i) {
        if (i >= this.mUnknownDevicesList.size()) {
            return "???";
        }
        return (String) ((Pair) this.mUnknownDevicesList.get(i)).first;
    }

    public int getGroupCount() {
        return this.mUnknownDevicesList.size();
    }

    public Object getGroup(int i) {
        return getGroupTitle(i);
    }

    public long getGroupId(int i) {
        return (long) getGroupTitle(i).hashCode();
    }

    public int getChildrenCount(int i) {
        return ((List) ((Pair) this.mUnknownDevicesList.get(i)).second).size();
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(R.layout.adapter_item_vector_unknown_devices_header, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.heading);
        if (textView != null) {
            textView.setText(getGroupTitle(i));
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.heading_image);
        if (z) {
            imageView.setImageResource(R.drawable.ic_material_expand_less_black);
        } else {
            imageView.setImageResource(R.drawable.ic_material_expand_more_black);
        }
        return view;
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(R.layout.adapter_item_member_details_devices, viewGroup, false);
        }
        final MXDeviceInfo mXDeviceInfo = (MXDeviceInfo) ((List) ((Pair) this.mUnknownDevicesList.get(i)).second).get(i2);
        Button button = (Button) view.findViewById(R.id.button_verify);
        Button button2 = (Button) view.findViewById(R.id.button_block);
        TextView textView = (TextView) view.findViewById(R.id.device_name);
        TextView textView2 = (TextView) view.findViewById(R.id.device_id);
        ImageView imageView = (ImageView) view.findViewById(R.id.device_e2e_icon);
        button.setTransformationMethod(null);
        button2.setTransformationMethod(null);
        textView.setText(mXDeviceInfo.displayName());
        textView2.setText(mXDeviceInfo.deviceId);
        int i3 = mXDeviceInfo.mVerified;
        if (i3 == 1) {
            imageView.setImageResource(R.drawable.e2e_verified);
        } else if (i3 != 2) {
            imageView.setImageResource(R.drawable.e2e_warning);
        } else {
            imageView.setImageResource(R.drawable.e2e_blocked);
        }
        int i4 = mXDeviceInfo.mVerified;
        if (i4 == -1) {
            button.setText(R.string.encryption_information_verify);
            button2.setText(R.string.encryption_information_block);
        } else if (i4 == 0) {
            button.setText(R.string.encryption_information_verify);
            button2.setText(R.string.encryption_information_block);
        } else if (i4 != 1) {
            button.setText(R.string.encryption_information_verify);
            button2.setText(R.string.encryption_information_unblock);
        } else {
            button.setText(R.string.encryption_information_unverify);
            button2.setText(R.string.encryption_information_block);
        }
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorUnknownDevicesAdapter.this.mListener != null) {
                    try {
                        VectorUnknownDevicesAdapter.this.mListener.OnVerifyDeviceClick(mXDeviceInfo);
                    } catch (Exception e) {
                        String access$100 = VectorUnknownDevicesAdapter.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## getChildView() : OnVerifyDeviceClick fails ");
                        sb.append(e.getMessage());
                        Log.e(access$100, sb.toString(), e);
                    }
                }
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorUnknownDevicesAdapter.this.mListener != null) {
                    try {
                        VectorUnknownDevicesAdapter.this.mListener.OnBlockDeviceClick(mXDeviceInfo);
                    } catch (Exception e) {
                        String access$100 = VectorUnknownDevicesAdapter.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## getChildView() : OnBlockDeviceClick fails ");
                        sb.append(e.getMessage());
                        Log.e(access$100, sb.toString(), e);
                    }
                }
            }
        });
        return view;
    }
}
