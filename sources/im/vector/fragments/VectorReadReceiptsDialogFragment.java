package im.vector.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.adapters.VectorReadReceiptsAdapter;
import im.vector.adapters.VectorReadReceiptsAdapter.VectorReadReceiptsAdapterListener;
import im.vector.extensions.BasicExtensionsKt;
import im.vector.fragments.base.VectorBaseDialogFragment;
import java.util.ArrayList;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \u00162\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0002\u0016\u0017B\u0005¢\u0006\u0002\u0010\u0004J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J&\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u0010\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lim/vector/fragments/VectorReadReceiptsDialogFragment;", "Lim/vector/fragments/base/VectorBaseDialogFragment;", "Lim/vector/fragments/VectorReadReceiptsDialogFragment$VectorReadReceiptsDialogFragmentListener;", "Lim/vector/adapters/VectorReadReceiptsAdapter$VectorReadReceiptsAdapterListener;", "()V", "mAdapter", "Lim/vector/adapters/VectorReadReceiptsAdapter;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onCreateDialog", "Landroid/app/Dialog;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onMemberClicked", "userId", "", "Companion", "VectorReadReceiptsDialogFragmentListener", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorReadReceiptsDialogFragment.kt */
public final class VectorReadReceiptsDialogFragment extends VectorBaseDialogFragment<VectorReadReceiptsDialogFragmentListener> implements VectorReadReceiptsAdapterListener {
    private static final String ARG_EVENT_ID = "VectorReadReceiptsDialogFragment.ARG_EVENT_ID";
    private static final String ARG_ROOM_ID = "VectorReadReceiptsDialogFragment.ARG_ROOM_ID";
    private static final String ARG_SESSION_ID = "VectorReadReceiptsDialogFragment.ARG_SESSION_ID";
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = VectorReadReceiptsDialogFragment.class.getSimpleName();
    private HashMap _$_findViewCache;
    /* access modifiers changed from: private */
    public VectorReadReceiptsAdapter mAdapter;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lim/vector/fragments/VectorReadReceiptsDialogFragment$Companion;", "", "()V", "ARG_EVENT_ID", "", "ARG_ROOM_ID", "ARG_SESSION_ID", "LOG_TAG", "kotlin.jvm.PlatformType", "newInstance", "Lim/vector/fragments/VectorReadReceiptsDialogFragment;", "userId", "roomId", "eventId", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorReadReceiptsDialogFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final VectorReadReceiptsDialogFragment newInstance(String str, String str2, String str3) {
            Intrinsics.checkParameterIsNotNull(str, "userId");
            Intrinsics.checkParameterIsNotNull(str2, CryptoRoomEntityFields.ROOM_ID);
            Intrinsics.checkParameterIsNotNull(str3, "eventId");
            return (VectorReadReceiptsDialogFragment) BasicExtensionsKt.withArgs(new VectorReadReceiptsDialogFragment(), new VectorReadReceiptsDialogFragment$Companion$newInstance$1(str, str2, str3));
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001¨\u0006\u0002"}, d2 = {"Lim/vector/fragments/VectorReadReceiptsDialogFragment$VectorReadReceiptsDialogFragmentListener;", "Lim/vector/adapters/VectorReadReceiptsAdapter$VectorReadReceiptsAdapterListener;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorReadReceiptsDialogFragment.kt */
    public interface VectorReadReceiptsDialogFragmentListener extends VectorReadReceiptsAdapterListener {
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view == null) {
            View view2 = getView();
            if (view2 == null) {
                return null;
            }
            view = view2.findViewById(i);
            this._$_findViewCache.put(Integer.valueOf(i), view);
        }
        return view;
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public static final /* synthetic */ VectorReadReceiptsAdapter access$getMAdapter$p(VectorReadReceiptsDialogFragment vectorReadReceiptsDialogFragment) {
        VectorReadReceiptsAdapter vectorReadReceiptsAdapter = vectorReadReceiptsDialogFragment.mAdapter;
        if (vectorReadReceiptsAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mAdapter");
        }
        return vectorReadReceiptsAdapter;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Matrix instance = Matrix.getInstance(getContext());
        Bundle arguments = getArguments();
        if (arguments == null) {
            Intrinsics.throwNpe();
        }
        MXSession session = instance.getSession(arguments.getString(ARG_SESSION_ID));
        Bundle arguments2 = getArguments();
        if (arguments2 == null) {
            Intrinsics.throwNpe();
        }
        String string = arguments2.getString(ARG_ROOM_ID);
        Bundle arguments3 = getArguments();
        if (arguments3 == null) {
            Intrinsics.throwNpe();
        }
        String string2 = arguments3.getString(ARG_EVENT_ID);
        if (session == null || TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
            Log.e(LOG_TAG, "## onCreate() : invalid parameters");
            dismiss();
            return;
        }
        Room room = session.getDataHandler().getRoom(string);
        Intrinsics.checkExpressionValueIsNotNull(room, "mxSession.dataHandler.getRoom(roomId)");
        Context context = getContext();
        if (context == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(context, "context!!");
        IMXStore store = session.getDataHandler().getStore();
        if (store == null) {
            Intrinsics.throwNpe();
        }
        VectorReadReceiptsAdapter vectorReadReceiptsAdapter = new VectorReadReceiptsAdapter(context, session, room, new ArrayList(store.getEventReceipts(string, string2, true, true)), this);
        this.mAdapter = vectorReadReceiptsAdapter;
        room.getMembersAsync(new VectorReadReceiptsDialogFragment$onCreate$1(this));
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        Intrinsics.checkExpressionValueIsNotNull(onCreateDialog, "super.onCreateDialog(savedInstanceState)");
        onCreateDialog.setTitle(R.string.read_receipts_list);
        return onCreateDialog;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_member_list, viewGroup, false);
        if (inflate != null) {
            RecyclerView recyclerView = (RecyclerView) inflate;
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            VectorReadReceiptsAdapter vectorReadReceiptsAdapter = this.mAdapter;
            if (vectorReadReceiptsAdapter == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mAdapter");
            }
            recyclerView.setAdapter(vectorReadReceiptsAdapter);
            return recyclerView;
        }
        throw new TypeCastException("null cannot be cast to non-null type androidx.recyclerview.widget.RecyclerView");
    }

    public void onMemberClicked(String str) {
        Intrinsics.checkParameterIsNotNull(str, "userId");
        VectorReadReceiptsDialogFragmentListener vectorReadReceiptsDialogFragmentListener = (VectorReadReceiptsDialogFragmentListener) getListener();
        if (vectorReadReceiptsDialogFragmentListener != null) {
            vectorReadReceiptsDialogFragmentListener.onMemberClicked(str);
        }
    }
}
