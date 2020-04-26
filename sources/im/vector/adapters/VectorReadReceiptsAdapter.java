package im.vector.adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import fr.gouv.tchap.a.R;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomMember;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u001d\u001eB=\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0016\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\f\u0012\u0006\u0010\r\u001a\u00020\u000e¢\u0006\u0002\u0010\u000fJ\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0014H\u0016J\u0018\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0014H\u0016R\u001e\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n \u0012*\u0004\u0018\u00010\u00110\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lim/vector/adapters/VectorReadReceiptsAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lim/vector/adapters/VectorReadReceiptsAdapter$ReadReceiptViewHolder;", "mContext", "Landroid/content/Context;", "mSession", "Lorg/matrix/androidsdk/MXSession;", "mRoom", "Lorg/matrix/androidsdk/data/Room;", "list", "Ljava/util/ArrayList;", "Lorg/matrix/androidsdk/rest/model/ReceiptData;", "Lkotlin/collections/ArrayList;", "listener", "Lim/vector/adapters/VectorReadReceiptsAdapter$VectorReadReceiptsAdapterListener;", "(Landroid/content/Context;Lorg/matrix/androidsdk/MXSession;Lorg/matrix/androidsdk/data/Room;Ljava/util/ArrayList;Lim/vector/adapters/VectorReadReceiptsAdapter$VectorReadReceiptsAdapterListener;)V", "mLayoutInflater", "Landroid/view/LayoutInflater;", "kotlin.jvm.PlatformType", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ReadReceiptViewHolder", "VectorReadReceiptsAdapterListener", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorReadReceiptsAdapter.kt */
public final class VectorReadReceiptsAdapter extends Adapter<ReadReceiptViewHolder> {
    private final ArrayList<ReceiptData> list;
    /* access modifiers changed from: private */
    public final VectorReadReceiptsAdapterListener listener;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final LayoutInflater mLayoutInflater = LayoutInflater.from(this.mContext);
    private final Room mRoom;
    private final MXSession mSession;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u00020\f8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000e\"\u0004\b\u0013\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0016"}, d2 = {"Lim/vector/adapters/VectorReadReceiptsAdapter$ReadReceiptViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "imageView", "Landroid/widget/ImageView;", "getImageView", "()Landroid/widget/ImageView;", "setImageView", "(Landroid/widget/ImageView;)V", "tsTextView", "Landroid/widget/TextView;", "getTsTextView", "()Landroid/widget/TextView;", "setTsTextView", "(Landroid/widget/TextView;)V", "userNameTextView", "getUserNameTextView", "setUserNameTextView", "getView", "()Landroid/view/View;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorReadReceiptsAdapter.kt */
    public static final class ReadReceiptViewHolder extends ViewHolder {
        @BindView(2131296323)
        public ImageView imageView;
        @BindView(2131296881)
        public TextView tsTextView;
        @BindView(2131296883)
        public TextView userNameTextView;
        private final View view;

        public ReadReceiptViewHolder(View view2) {
            Intrinsics.checkParameterIsNotNull(view2, "view");
            super(view2);
            this.view = view2;
            ButterKnife.bind((Object) this, this.view);
        }

        public final View getView() {
            return this.view;
        }

        public final ImageView getImageView() {
            ImageView imageView2 = this.imageView;
            if (imageView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("imageView");
            }
            return imageView2;
        }

        public final void setImageView(ImageView imageView2) {
            Intrinsics.checkParameterIsNotNull(imageView2, "<set-?>");
            this.imageView = imageView2;
        }

        public final TextView getUserNameTextView() {
            TextView textView = this.userNameTextView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("userNameTextView");
            }
            return textView;
        }

        public final void setUserNameTextView(TextView textView) {
            Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
            this.userNameTextView = textView;
        }

        public final TextView getTsTextView() {
            TextView textView = this.tsTextView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tsTextView");
            }
            return textView;
        }

        public final void setTsTextView(TextView textView) {
            Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
            this.tsTextView = textView;
        }
    }

    public final class ReadReceiptViewHolder_ViewBinding implements Unbinder {
        private ReadReceiptViewHolder target;

        public ReadReceiptViewHolder_ViewBinding(ReadReceiptViewHolder readReceiptViewHolder, View view) {
            this.target = readReceiptViewHolder;
            readReceiptViewHolder.imageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.avatar_img_vector, "field 'imageView'", ImageView.class);
            readReceiptViewHolder.userNameTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.read_receipt_user_name, "field 'userNameTextView'", TextView.class);
            readReceiptViewHolder.tsTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.read_receipt_ts, "field 'tsTextView'", TextView.class);
        }

        public void unbind() {
            ReadReceiptViewHolder readReceiptViewHolder = this.target;
            if (readReceiptViewHolder != null) {
                this.target = null;
                readReceiptViewHolder.imageView = null;
                readReceiptViewHolder.userNameTextView = null;
                readReceiptViewHolder.tsTextView = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lim/vector/adapters/VectorReadReceiptsAdapter$VectorReadReceiptsAdapterListener;", "", "onMemberClicked", "", "userId", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorReadReceiptsAdapter.kt */
    public interface VectorReadReceiptsAdapterListener {
        void onMemberClicked(String str);
    }

    public VectorReadReceiptsAdapter(Context context, MXSession mXSession, Room room, ArrayList<ReceiptData> arrayList, VectorReadReceiptsAdapterListener vectorReadReceiptsAdapterListener) {
        Intrinsics.checkParameterIsNotNull(context, "mContext");
        Intrinsics.checkParameterIsNotNull(mXSession, "mSession");
        Intrinsics.checkParameterIsNotNull(room, "mRoom");
        Intrinsics.checkParameterIsNotNull(arrayList, "list");
        Intrinsics.checkParameterIsNotNull(vectorReadReceiptsAdapterListener, CastExtraArgs.LISTENER);
        this.mContext = context;
        this.mSession = mXSession;
        this.mRoom = room;
        this.list = arrayList;
        this.listener = vectorReadReceiptsAdapterListener;
    }

    public ReadReceiptViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        View inflate = this.mLayoutInflater.inflate(R.layout.adapter_item_read_receipt, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "view");
        return new ReadReceiptViewHolder(inflate);
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void onBindViewHolder(ReadReceiptViewHolder readReceiptViewHolder, int i) {
        Intrinsics.checkParameterIsNotNull(readReceiptViewHolder, "holder");
        Object obj = this.list.get(i);
        Intrinsics.checkExpressionValueIsNotNull(obj, "list[position]");
        ReceiptData receiptData = (ReceiptData) obj;
        RoomMember member = this.mRoom.getMember(receiptData.userId);
        if (member == null) {
            VectorUtils.loadUserAvatar(this.mContext, this.mSession, readReceiptViewHolder.getImageView(), null, receiptData.userId, receiptData.userId);
        } else {
            VectorUtils.loadRoomMemberAvatar(this.mContext, this.mSession, readReceiptViewHolder.getImageView(), member);
        }
        TextView userNameTextView = readReceiptViewHolder.getUserNameTextView();
        if (member == null) {
            userNameTextView.setText(receiptData.userId);
        } else {
            userNameTextView.setText(member.getName());
        }
        userNameTextView.setOnLongClickListener(new VectorReadReceiptsAdapter$onBindViewHolder$$inlined$let$lambda$1(userNameTextView, this, member, receiptData));
        userNameTextView.setOnClickListener(new VectorReadReceiptsAdapter$onBindViewHolder$$inlined$let$lambda$2(this, member, receiptData));
        TextView tsTextView = readReceiptViewHolder.getTsTextView();
        String tsToString = AdapterUtils.tsToString(this.mContext, receiptData.originServerTs, false);
        StringBuilder sb = new StringBuilder();
        sb.append(this.mContext.getString(R.string.read_receipt));
        sb.append(" : ");
        sb.append(tsToString);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(sb.toString());
        spannableStringBuilder.setSpan(new StyleSpan(1), 0, this.mContext.getString(R.string.read_receipt).length(), 33);
        tsTextView.setText(spannableStringBuilder);
        tsTextView.setOnLongClickListener(new VectorReadReceiptsAdapter$onBindViewHolder$$inlined$let$lambda$3(tsToString, this, receiptData, member));
        tsTextView.setOnClickListener(new VectorReadReceiptsAdapter$onBindViewHolder$$inlined$let$lambda$4(this, receiptData, member));
        readReceiptViewHolder.getView().setOnClickListener(new VectorReadReceiptsAdapter$onBindViewHolder$3(this, member));
    }
}
