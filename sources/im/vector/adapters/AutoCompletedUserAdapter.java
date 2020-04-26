package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorRoomActivity;
import im.vector.settings.VectorLocale;
import im.vector.util.VectorUtils;
import im.vector.view.VectorCircularImageView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.rest.model.User;

public class AutoCompletedUserAdapter extends ArrayAdapter<User> {
    /* access modifiers changed from: private */
    public static final Comparator<User> mUserComparatorByDisplayname = new Comparator<User>() {
        public int compare(User user, User user2) {
            return (TextUtils.isEmpty(user.displayname) ? user.user_id : user.displayname).compareToIgnoreCase(TextUtils.isEmpty(user2.displayname) ? user2.user_id : user2.displayname);
        }
    };
    /* access modifiers changed from: private */
    public static final Comparator<User> mUserComparatorByUserId = new Comparator<User>() {
        public int compare(User user, User user2) {
            return user.user_id.compareToIgnoreCase(user2.user_id);
        }
    };
    private final Context mContext;
    private Filter mFilter;
    /* access modifiers changed from: private */
    public boolean mIsSearchingMatrixId = false;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResourceId;
    /* access modifiers changed from: private */
    public boolean mProvideMatrixIdOnly = false;
    private final MXSession mSession;
    /* access modifiers changed from: private */
    public List<User> mUsersList = new ArrayList();

    private class AutoCompletedUserFilter extends Filter {
        private AutoCompletedUserFilter() {
        }

        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            ArrayList arrayList = new ArrayList();
            AutoCompletedUserAdapter.this.mIsSearchingMatrixId = false;
            if (charSequence != null) {
                String lowerCase = charSequence.toString().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
                if (lowerCase.startsWith("@")) {
                    lowerCase = lowerCase.substring(1);
                }
                if (lowerCase.length() > 0) {
                    for (User user : AutoCompletedUserAdapter.this.mUsersList) {
                        if (user.displayname != null && user.displayname.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).startsWith(lowerCase)) {
                            arrayList.add(user);
                        }
                    }
                }
            }
            if (AutoCompletedUserAdapter.this.mIsSearchingMatrixId) {
                Collections.sort(arrayList, AutoCompletedUserAdapter.mUserComparatorByUserId);
            } else {
                Collections.sort(arrayList, AutoCompletedUserAdapter.mUserComparatorByDisplayname);
            }
            filterResults.values = arrayList;
            filterResults.count = arrayList.size();
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            AutoCompletedUserAdapter.this.clear();
            AutoCompletedUserAdapter.this.addAll((List) filterResults.values);
            if (filterResults.count > 0) {
                AutoCompletedUserAdapter.this.notifyDataSetChanged();
            } else {
                AutoCompletedUserAdapter.this.notifyDataSetInvalidated();
            }
        }

        public CharSequence convertResultToString(Object obj) {
            User user = (User) obj;
            if (AutoCompletedUserAdapter.this.mIsSearchingMatrixId || AutoCompletedUserAdapter.this.mProvideMatrixIdOnly) {
                return user.user_id;
            }
            return VectorRoomActivity.sanitizeDisplayname(user.displayname);
        }
    }

    public AutoCompletedUserAdapter(Context context, int i, MXSession mXSession, Collection<User> collection) {
        super(context, i);
        this.mContext = context;
        this.mLayoutResourceId = i;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        this.mSession = mXSession;
        addAll(collection);
        this.mUsersList = new ArrayList(collection);
    }

    public void setProvideMatrixIdOnly(boolean z) {
        this.mProvideMatrixIdOnly = z;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        return getView(i, view, viewGroup, true);
    }

    public View getView(int i, View view, ViewGroup viewGroup, boolean z) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mLayoutResourceId, viewGroup, false);
        }
        User user = (User) getItem(i);
        if (z) {
            VectorUtils.loadUserAvatar(this.mContext, this.mSession, (VectorCircularImageView) view.findViewById(R.id.item_user_auto_complete_avatar), user.getAvatarUrl(), user.user_id, user.displayname);
        }
        TextView textView = (TextView) view.findViewById(R.id.item_user_auto_complete_name);
        if (!this.mIsSearchingMatrixId) {
            String str = user.displayname;
            if (this.mProvideMatrixIdOnly) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" (");
                sb.append(user.user_id);
                sb.append(")");
                str = sb.toString();
            }
            textView.setText(str);
        } else {
            textView.setText(user.user_id);
        }
        return view;
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new AutoCompletedUserFilter();
        }
        return this.mFilter;
    }
}
