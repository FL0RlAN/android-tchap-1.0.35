package im.vector.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorGroupDetailsActivity;
import im.vector.util.SystemUtilsKt;
import im.vector.util.VectorUtils;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.groups.GroupsManager;
import org.matrix.androidsdk.rest.model.group.Group;
import org.matrix.androidsdk.rest.model.group.GroupProfile;

public class VectorGroupsListAdapter extends ArrayAdapter<String> {
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public Map<String, Group> mGroupByGroupId = new HashMap();
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResourceId;
    /* access modifiers changed from: private */
    public final MXSession mSession;

    public VectorGroupsListAdapter(Context context, int i, MXSession mXSession) {
        super(context, i);
        this.mContext = context;
        this.mLayoutResourceId = i;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        this.mSession = mXSession;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mLayoutResourceId, viewGroup, false);
        }
        final String str = (String) getItem(i);
        Group group = (Group) this.mGroupByGroupId.get(str);
        if (group == null) {
            group = this.mSession.getGroupsManager().getGroup(str);
            if (group != null) {
                this.mGroupByGroupId.put(str, group);
            }
        }
        boolean z = group == null;
        if (group == null) {
            group = new Group(str);
        }
        view.findViewById(R.id.group_members_count).setVisibility(8);
        final TextView textView = (TextView) view.findViewById(R.id.group_name);
        textView.setTag(str);
        textView.setText(group.getDisplayName());
        textView.setTypeface(null, 0);
        final ImageView imageView = (ImageView) view.findViewById(R.id.room_avatar);
        VectorUtils.loadGroupAvatar(this.mContext, this.mSession, imageView, group);
        final TextView textView2 = (TextView) view.findViewById(R.id.group_topic);
        textView2.setText(group.getShortDescription());
        view.findViewById(R.id.group_more_action_click_area).setVisibility(4);
        view.findViewById(R.id.group_more_action_anchor).setVisibility(4);
        view.findViewById(R.id.group_more_action_ic).setVisibility(4);
        if (z) {
            GroupsManager groupsManager = this.mSession.getGroupsManager();
            final String str2 = str;
            AnonymousClass1 r1 = new ApiCallback<GroupProfile>() {
                public void onMatrixError(MatrixError matrixError) {
                }

                public void onNetworkError(Exception exc) {
                }

                public void onUnexpectedError(Exception exc) {
                }

                public void onSuccess(GroupProfile groupProfile) {
                    if (TextUtils.equals((String) textView.getTag(), str2)) {
                        Group group = (Group) VectorGroupsListAdapter.this.mGroupByGroupId.get(str2);
                        if (group == null) {
                            group = new Group(str2);
                            group.setGroupProfile(groupProfile);
                            VectorGroupsListAdapter.this.mGroupByGroupId.put(str2, group);
                        }
                        textView.setText(group.getDisplayName());
                        VectorUtils.loadGroupAvatar(VectorGroupsListAdapter.this.mContext, VectorGroupsListAdapter.this.mSession, imageView, group);
                        textView2.setText(group.getShortDescription());
                    }
                }
            };
            groupsManager.getGroupProfile(str, r1);
        }
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(VectorGroupsListAdapter.this.mContext, VectorGroupDetailsActivity.class);
                intent.putExtra(VectorGroupDetailsActivity.EXTRA_GROUP_ID, str);
                intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorGroupsListAdapter.this.mSession.getCredentials().userId);
                VectorGroupsListAdapter.this.mContext.startActivity(intent);
            }
        });
        view.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                SystemUtilsKt.copyToClipboard(VectorGroupsListAdapter.this.mContext, str);
                return true;
            }
        });
        return view;
    }
}
