package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.settings.VectorLocale;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.rest.model.User;

public class VectorRoomCreationAdapter extends ArrayAdapter<ParticipantAdapterItem> {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorRoomCreationAdapter.class.getSimpleName();
    private final int mAddMemberLayoutResourceId;
    private final Context mContext;
    private final List<String> mDisplayNamesList = new ArrayList();
    private final LayoutInflater mLayoutInflater;
    private final int mMemberLayoutResourceId;
    /* access modifiers changed from: private */
    public IRoomCreationAdapterListener mRoomCreationAdapterListener;
    private final MXSession mSession;

    public interface IRoomCreationAdapterListener {
        void OnRemoveParticipantClick(ParticipantAdapterItem participantAdapterItem);
    }

    public int getItemViewType(int i) {
        return i == 0 ? 0 : 1;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public VectorRoomCreationAdapter(Context context, int i, int i2, MXSession mXSession) {
        super(context, i2);
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mAddMemberLayoutResourceId = i;
        this.mMemberLayoutResourceId = i2;
        this.mSession = mXSession;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mDisplayNamesList.clear();
        for (int i = 0; i < getCount(); i++) {
            ParticipantAdapterItem participantAdapterItem = (ParticipantAdapterItem) getItem(i);
            if (!TextUtils.isEmpty(participantAdapterItem.mDisplayName)) {
                this.mDisplayNamesList.add(participantAdapterItem.mDisplayName.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()));
            }
        }
    }

    public void setRoomCreationAdapterListener(IRoomCreationAdapterListener iRoomCreationAdapterListener) {
        this.mRoomCreationAdapterListener = iRoomCreationAdapterListener;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i == 0) {
            if (view == null) {
                view = this.mLayoutInflater.inflate(this.mAddMemberLayoutResourceId, viewGroup, false);
            }
            return view;
        }
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mMemberLayoutResourceId, viewGroup, false);
        }
        final ParticipantAdapterItem participantAdapterItem = (ParticipantAdapterItem) getItem(i);
        TextView textView = (TextView) view.findViewById(R.id.filtered_list_name);
        TextView textView2 = (TextView) view.findViewById(R.id.filtered_list_email);
        participantAdapterItem.displayAvatar(this.mSession, (ImageView) view.findViewById(R.id.filtered_list_avatar));
        textView.setText(participantAdapterItem.getUniqueDisplayName(this.mDisplayNamesList));
        User user = null;
        MXSession mXSession = null;
        for (MXSession mXSession2 : Matrix.getMXSessions(this.mContext)) {
            if (user == null) {
                user = mXSession2.getDataHandler().getUser(participantAdapterItem.mUserId);
                mXSession = mXSession2;
            }
        }
        String userOnlineStatus = user != null ? VectorUtils.getUserOnlineStatus(this.mContext, mXSession, participantAdapterItem.mUserId, new SimpleApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                VectorRoomCreationAdapter.this.notifyDataSetChanged();
            }
        }) : "";
        if (participantAdapterItem.mContact != null) {
            textView2.setText(participantAdapterItem.mUserId);
        } else {
            textView2.setText(userOnlineStatus);
        }
        View findViewById = view.findViewById(R.id.filtered_list_remove_button);
        findViewById.setVisibility(0);
        findViewById.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorRoomCreationAdapter.this.mRoomCreationAdapterListener != null) {
                    try {
                        VectorRoomCreationAdapter.this.mRoomCreationAdapterListener.OnRemoveParticipantClick(participantAdapterItem);
                    } catch (Exception e) {
                        String access$100 = VectorRoomCreationAdapter.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## getView() : OnRemoveParticipantClick fails ");
                        sb.append(e.getMessage());
                        Log.e(access$100, sb.toString(), e);
                    }
                }
            }
        });
        return view;
    }
}
