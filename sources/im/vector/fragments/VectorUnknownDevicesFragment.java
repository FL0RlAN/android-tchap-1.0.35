package im.vector.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;
import im.vector.adapters.VectorUnknownDevicesAdapter;
import im.vector.adapters.VectorUnknownDevicesAdapter.IVerificationAdapterListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;

public class VectorUnknownDevicesFragment extends DialogFragment {
    private static final String ARG_IS_FOR_CALLING = "VectorUnknownDevicesFragment.ARG_IS_FOR_CALLING";
    private static final String ARG_SESSION_ID = "VectorUnknownDevicesFragment.ARG_SESSION_ID";
    /* access modifiers changed from: private */
    public static IUnknownDevicesSendAnywayListener mListener;
    private static MXUsersDevicesMap<MXDeviceInfo> mUnknownDevicesMap;
    private List<Pair<String, List<MXDeviceInfo>>> mDevicesList;
    /* access modifiers changed from: private */
    public ExpandableListView mExpandableListView;
    private boolean mIsForCalling;
    /* access modifiers changed from: private */
    public boolean mIsSendAnywayTapped = false;
    /* access modifiers changed from: private */
    public MXSession mSession;

    public interface IUnknownDevicesSendAnywayListener {
        void onSendAnyway();
    }

    public static VectorUnknownDevicesFragment newInstance(String str, MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap, boolean z, IUnknownDevicesSendAnywayListener iUnknownDevicesSendAnywayListener) {
        VectorUnknownDevicesFragment vectorUnknownDevicesFragment = new VectorUnknownDevicesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SESSION_ID, str);
        bundle.putBoolean(ARG_IS_FOR_CALLING, z);
        mUnknownDevicesMap = mXUsersDevicesMap;
        mListener = iUnknownDevicesSendAnywayListener;
        vectorUnknownDevicesFragment.setArguments(bundle);
        return vectorUnknownDevicesFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSession = Matrix.getMXSession(getActivity(), getArguments().getString(ARG_SESSION_ID));
        this.mIsForCalling = getArguments().getBoolean(ARG_IS_FOR_CALLING);
    }

    private static List<Pair<String, List<MXDeviceInfo>>> getDevicesList() {
        ArrayList arrayList = new ArrayList();
        MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap = mUnknownDevicesMap;
        if (mXUsersDevicesMap != null) {
            for (String str : mXUsersDevicesMap.getUserIds()) {
                ArrayList arrayList2 = new ArrayList();
                for (String object : mUnknownDevicesMap.getUserDeviceIds(str)) {
                    arrayList2.add(mUnknownDevicesMap.getObject(object, str));
                }
                arrayList.add(new Pair(str, arrayList2));
            }
        }
        return arrayList;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View inflate = layoutInflater.inflate(R.layout.dialog_unknown_devices, null);
        this.mExpandableListView = (ExpandableListView) inflate.findViewById(R.id.unknown_devices_list_view);
        this.mDevicesList = getDevicesList();
        final VectorUnknownDevicesAdapter vectorUnknownDevicesAdapter = new VectorUnknownDevicesAdapter(getContext(), this.mDevicesList);
        vectorUnknownDevicesAdapter.setListener(new IVerificationAdapterListener() {
            final ApiCallback<Void> mCallback = new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    AnonymousClass1.this.refresh();
                }

                public void onNetworkError(Exception exc) {
                    AnonymousClass1.this.refresh();
                }

                public void onMatrixError(MatrixError matrixError) {
                    AnonymousClass1.this.refresh();
                }

                public void onUnexpectedError(Exception exc) {
                    AnonymousClass1.this.refresh();
                }
            };

            /* access modifiers changed from: private */
            public void refresh() {
                vectorUnknownDevicesAdapter.notifyDataSetChanged();
            }

            public void OnVerifyDeviceClick(MXDeviceInfo mXDeviceInfo) {
                if (mXDeviceInfo.mVerified != 1) {
                    CommonActivityUtils.displayDeviceVerificationDialog(mXDeviceInfo, mXDeviceInfo.userId, VectorUnknownDevicesFragment.this.mSession, VectorUnknownDevicesFragment.this.getActivity(), this.mCallback);
                } else {
                    VectorUnknownDevicesFragment.this.mSession.getCrypto().setDeviceVerification(0, mXDeviceInfo.deviceId, mXDeviceInfo.userId, this.mCallback);
                }
            }

            public void OnBlockDeviceClick(MXDeviceInfo mXDeviceInfo) {
                if (mXDeviceInfo.mVerified == 2) {
                    VectorUnknownDevicesFragment.this.mSession.getCrypto().setDeviceVerification(0, mXDeviceInfo.deviceId, mXDeviceInfo.userId, this.mCallback);
                } else {
                    VectorUnknownDevicesFragment.this.mSession.getCrypto().setDeviceVerification(2, mXDeviceInfo.deviceId, mXDeviceInfo.userId, this.mCallback);
                }
                refresh();
            }
        });
        this.mExpandableListView.addHeaderView(layoutInflater.inflate(R.layout.dialog_unknown_devices_header, null));
        this.mExpandableListView.setGroupIndicator(null);
        this.mExpandableListView.setAdapter(vectorUnknownDevicesAdapter);
        this.mExpandableListView.post(new Runnable() {
            public void run() {
                int groupCount = vectorUnknownDevicesAdapter.getGroupCount();
                for (int i = 0; i < groupCount; i++) {
                    VectorUnknownDevicesFragment.this.mExpandableListView.expandGroup(i);
                }
            }
        });
        Builder title = new Builder(getActivity()).setView(inflate).setTitle((int) R.string.unknown_devices_alert_title);
        if (mListener != null) {
            title.setPositiveButton(this.mIsForCalling ? R.string.call_anyway : R.string.send_anyway, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    VectorUnknownDevicesFragment.this.mIsSendAnywayTapped = true;
                }
            });
            title.setNeutralButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        } else {
            title.setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }
        return title.create();
    }

    public void dismissAllowingStateLoss() {
        if (getFragmentManager() != null) {
            super.dismissAllowingStateLoss();
        }
        mUnknownDevicesMap = null;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        setDevicesKnown(this.mDevicesList);
    }

    private void setDevicesKnown(List<Pair<String, List<MXDeviceInfo>>> list) {
        if (mUnknownDevicesMap != null) {
            mUnknownDevicesMap = null;
            ArrayList arrayList = new ArrayList();
            for (Pair pair : list) {
                arrayList.addAll((Collection) pair.second);
            }
            this.mSession.getCrypto().setDevicesKnown(arrayList, new ApiCallback<Void>() {
                private void onDone() {
                    if (VectorUnknownDevicesFragment.this.mIsSendAnywayTapped && VectorUnknownDevicesFragment.mListener != null) {
                        VectorUnknownDevicesFragment.mListener.onSendAnyway();
                    }
                    VectorUnknownDevicesFragment.mListener = null;
                    if (VectorUnknownDevicesFragment.this.isAdded() && VectorUnknownDevicesFragment.this.isResumed()) {
                        VectorUnknownDevicesFragment.this.dismissAllowingStateLoss();
                    }
                }

                public void onSuccess(Void voidR) {
                    onDone();
                }

                public void onNetworkError(Exception exc) {
                    onDone();
                }

                public void onMatrixError(MatrixError matrixError) {
                    onDone();
                }

                public void onUnexpectedError(Exception exc) {
                    onDone();
                }
            });
        }
    }
}
