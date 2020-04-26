package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.adapters.ParticipantAdapterItem;
import im.vector.adapters.VectorRoomCreationAdapter;
import im.vector.adapters.VectorRoomCreationAdapter.IRoomCreationAdapterListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.anko.ToastsKt;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.CreateRoomParams;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;

public class VectorRoomCreationActivity extends MXCActionBarActivity {
    private static final int INVITE_USER_REQUEST_CODE = 456;
    private static final String PARTICIPANTS_LIST = "PARTICIPANTS_LIST";
    /* access modifiers changed from: private */
    public final String LOG_TAG = VectorRoomCreationActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public VectorRoomCreationAdapter mAdapter;
    private final Comparator<ParticipantAdapterItem> mAlphaComparator = new Comparator<ParticipantAdapterItem>() {
        public int compare(ParticipantAdapterItem participantAdapterItem, ParticipantAdapterItem participantAdapterItem2) {
            if (TextUtils.equals(participantAdapterItem.mUserId, VectorRoomCreationActivity.this.mSession.getMyUserId())) {
                return -1;
            }
            if (TextUtils.equals(participantAdapterItem2.mUserId, VectorRoomCreationActivity.this.mSession.getMyUserId())) {
                return 1;
            }
            String comparisonDisplayName = participantAdapterItem.getComparisonDisplayName();
            String comparisonDisplayName2 = participantAdapterItem2.getComparisonDisplayName();
            if (comparisonDisplayName == null) {
                return -1;
            }
            if (comparisonDisplayName2 == null) {
                return 1;
            }
            return String.CASE_INSENSITIVE_ORDER.compare(comparisonDisplayName, comparisonDisplayName2);
        }
    };
    /* access modifiers changed from: private */
    public final ApiCallback<String> mCreateDirectMessageCallBack = new ApiCallback<String>() {
        public void onSuccess(String str) {
            HashMap hashMap = new HashMap();
            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomCreationActivity.this.mSession.getMyUserId());
            hashMap.put("EXTRA_ROOM_ID", str);
            hashMap.put(VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER, Boolean.valueOf(true));
            Log.d(VectorRoomCreationActivity.this.LOG_TAG, "## mCreateDirectMessageCallBack: onSuccess - start goToRoomPage");
            VectorRoomCreationActivity vectorRoomCreationActivity = VectorRoomCreationActivity.this;
            CommonActivityUtils.goToRoomPage(vectorRoomCreationActivity, vectorRoomCreationActivity.mSession, hashMap);
        }

        private void onError(final String str) {
            VectorRoomCreationActivity.this.membersListView.post(new Runnable() {
                public void run() {
                    if (str != null) {
                        Toast.makeText(VectorRoomCreationActivity.this, str, 1).show();
                    }
                    VectorRoomCreationActivity.this.hideWaitingView();
                }
            });
        }

        public void onNetworkError(Exception exc) {
            onError(exc.getLocalizedMessage());
        }

        public void onMatrixError(MatrixError matrixError) {
            if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                VectorRoomCreationActivity.this.hideWaitingView();
                VectorRoomCreationActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                return;
            }
            onError(matrixError.getLocalizedMessage());
        }

        public void onUnexpectedError(Exception exc) {
            onError(exc.getLocalizedMessage());
        }
    };
    private boolean mIsFirstResume = true;
    /* access modifiers changed from: private */
    public List<ParticipantAdapterItem> mParticipants = new ArrayList();
    /* access modifiers changed from: private */
    public ListView membersListView;

    public int getLayoutRes() {
        return R.layout.activity_vector_room_creation;
    }

    public int getMenuRes() {
        return R.menu.vector_room_creation;
    }

    public void initUiAndData() {
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(this.LOG_TAG, "onCreate : Restart the application.");
            CommonActivityUtils.restartApp(this);
            return;
        }
        this.mSession = getSession(getIntent());
        if (this.mSession == null) {
            Log.e(this.LOG_TAG, "No MXSession.");
            finish();
            return;
        }
        setWaitingView(findViewById(R.id.room_creation_spinner_views));
        this.membersListView = (ListView) findViewById(R.id.room_creation_members_list_view);
        this.mAdapter = new VectorRoomCreationAdapter(this, R.layout.adapter_item_vector_creation_add_member, R.layout.adapter_item_vector_add_participants, this.mSession);
        if (!isFirstCreation()) {
            Bundle savedInstanceState = getSavedInstanceState();
            String str = PARTICIPANTS_LIST;
            if (savedInstanceState.containsKey(str)) {
                this.mParticipants.clear();
                this.mParticipants = new ArrayList((List) getSavedInstanceState().getSerializable(str));
                this.mAdapter.addAll(this.mParticipants);
                this.membersListView.setAdapter(this.mAdapter);
                this.mAdapter.setRoomCreationAdapterListener(new IRoomCreationAdapterListener() {
                    public void OnRemoveParticipantClick(ParticipantAdapterItem participantAdapterItem) {
                        VectorRoomCreationActivity.this.mParticipants.remove(participantAdapterItem);
                        VectorRoomCreationActivity.this.mAdapter.remove(participantAdapterItem);
                    }
                });
                this.membersListView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        if (i == 0) {
                            VectorRoomCreationActivity.this.launchSearchActivity();
                        }
                    }
                });
            }
        }
        this.mParticipants.add(new ParticipantAdapterItem((User) this.mSession.getMyUser()));
        this.mAdapter.addAll(this.mParticipants);
        this.membersListView.setAdapter(this.mAdapter);
        this.mAdapter.setRoomCreationAdapterListener(new IRoomCreationAdapterListener() {
            public void OnRemoveParticipantClick(ParticipantAdapterItem participantAdapterItem) {
                VectorRoomCreationActivity.this.mParticipants.remove(participantAdapterItem);
                VectorRoomCreationActivity.this.mAdapter.remove(participantAdapterItem);
            }
        });
        this.membersListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    VectorRoomCreationActivity.this.launchSearchActivity();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void launchSearchActivity() {
        Intent intent = new Intent(this, VectorRoomInviteMembersActivity.class);
        intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
        intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_HIDDEN_PARTICIPANT_ITEMS, (ArrayList) this.mParticipants);
        startActivityForResult(intent, INVITE_USER_REQUEST_CODE);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mIsFirstResume) {
            this.mIsFirstResume = false;
            launchSearchActivity();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable(PARTICIPANTS_LIST, (ArrayList) this.mParticipants);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle != null) {
            String str = PARTICIPANTS_LIST;
            if (bundle.containsKey(str)) {
                this.mParticipants = new ArrayList((List) bundle.getSerializable(str));
            } else {
                this.mParticipants.clear();
                this.mParticipants.add(new ParticipantAdapterItem((User) this.mSession.getMyUser()));
            }
            this.mAdapter.clear();
            this.mAdapter.addAll(this.mParticipants);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != INVITE_USER_REQUEST_CODE) {
            return;
        }
        if (i2 == -1) {
            List list = (List) intent.getSerializableExtra(VectorRoomInviteMembersActivity.EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS);
            this.mParticipants.addAll(list);
            this.mAdapter.addAll(list);
            this.mAdapter.sort(this.mAlphaComparator);
        } else if (1 == this.mParticipants.size()) {
            finish();
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return !CommonActivityUtils.shouldRestartApp(this) && this.mSession != null;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_create_room) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (this.mParticipants.isEmpty()) {
            createRoom(this.mParticipants);
        } else {
            this.mParticipants.remove(0);
            if (this.mParticipants.isEmpty()) {
                createRoom(this.mParticipants);
            } else if (this.mParticipants.size() > 1) {
                createRoom(this.mParticipants);
            } else {
                openOrCreateDirectChatRoom(((ParticipantAdapterItem) this.mParticipants.get(0)).mUserId);
            }
        }
        return true;
    }

    private void openOrCreateDirectChatRoom(final String str) {
        doesDirectChatRoomAlreadyExist(str, new ApiCallback<String>() {
            public void onSuccess(String str) {
                if (str != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", str);
                    hashMap.put("EXTRA_ROOM_ID", str);
                    VectorRoomCreationActivity vectorRoomCreationActivity = VectorRoomCreationActivity.this;
                    CommonActivityUtils.goToRoomPage(vectorRoomCreationActivity, vectorRoomCreationActivity.mSession, hashMap);
                    return;
                }
                VectorRoomCreationActivity.this.showWaitingView();
                DinsicUtils.createDirectChat(VectorRoomCreationActivity.this.mSession, str, VectorRoomCreationActivity.this.mCreateDirectMessageCallBack);
            }

            public void onNetworkError(Exception exc) {
                ToastsKt.toast((Context) VectorRoomCreationActivity.this, (CharSequence) exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                ToastsKt.toast((Context) VectorRoomCreationActivity.this, (CharSequence) matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                ToastsKt.toast((Context) VectorRoomCreationActivity.this, (CharSequence) exc.getLocalizedMessage());
            }
        });
    }

    private void doesDirectChatRoomAlreadyExist(String str, ApiCallback<String> apiCallback) {
        if (this.mSession != null) {
            IMXStore store = this.mSession.getDataHandler().getStore();
            if (store.getDirectChatRoomsDict() != null) {
                HashMap hashMap = new HashMap(store.getDirectChatRoomsDict());
                if (hashMap.containsKey(str)) {
                    doesDirectChatRoomAlreadyExistRecursive(new ArrayList((Collection) hashMap.get(str)), 0, str, apiCallback);
                } else {
                    apiCallback.onSuccess(null);
                }
            } else {
                apiCallback.onSuccess(null);
            }
        } else {
            apiCallback.onSuccess(null);
        }
    }

    /* access modifiers changed from: private */
    public void doesDirectChatRoomAlreadyExistRecursive(List<String> list, int i, String str, ApiCallback<String> apiCallback) {
        if (i >= list.size()) {
            String str2 = this.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## doesDirectChatRoomAlreadyExist(): for user=");
            sb.append(str);
            sb.append(" no found room");
            Log.d(str2, sb.toString());
            apiCallback.onSuccess(null);
            return;
        }
        Room room = this.mSession.getDataHandler().getRoom((String) list.get(i), false);
        if (room == null || !room.isReady() || room.isInvited() || room.isLeaving()) {
            doesDirectChatRoomAlreadyExistRecursive(list, i + 1, str, apiCallback);
            return;
        }
        final String str3 = str;
        final List<String> list2 = list;
        final int i2 = i;
        final ApiCallback<String> apiCallback2 = apiCallback;
        AnonymousClass6 r2 = new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                for (RoomMember userId : list) {
                    if (TextUtils.equals(userId.getUserId(), str3)) {
                        String access$000 = VectorRoomCreationActivity.this.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## doesDirectChatRoomAlreadyExist(): for user=");
                        sb.append(str3);
                        sb.append(" roomFound=");
                        sb.append((String) list2.get(i2));
                        Log.d(access$000, sb.toString());
                        apiCallback2.onSuccess(list2.get(i2));
                        return;
                    }
                }
                VectorRoomCreationActivity.this.doesDirectChatRoomAlreadyExistRecursive(list2, i2 + 1, str3, apiCallback2);
            }
        };
        room.getActiveMembersAsync(r2);
    }

    private void createRoom(List<ParticipantAdapterItem> list) {
        showWaitingView();
        CreateRoomParams createRoomParams = new CreateRoomParams();
        ArrayList arrayList = new ArrayList();
        for (ParticipantAdapterItem participantAdapterItem : list) {
            if (participantAdapterItem.mUserId != null) {
                arrayList.add(participantAdapterItem.mUserId);
            }
        }
        createRoomParams.addParticipantIds(this.mSession.getHomeServerConfig(), arrayList);
        this.mSession.createRoom(createRoomParams, new ApiCallback<String>() {
            public void onSuccess(final String str) {
                VectorRoomCreationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        HashMap hashMap = new HashMap();
                        hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomCreationActivity.this.mSession.getMyUserId());
                        hashMap.put("EXTRA_ROOM_ID", str);
                        CommonActivityUtils.goToRoomPage(VectorRoomCreationActivity.this, VectorRoomCreationActivity.this.mSession, hashMap);
                    }
                });
            }

            private void onError(final String str) {
                VectorRoomCreationActivity.this.membersListView.post(new Runnable() {
                    public void run() {
                        if (str != null) {
                            Toast.makeText(VectorRoomCreationActivity.this, str, 1).show();
                        }
                        VectorRoomCreationActivity.this.hideWaitingView();
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                    VectorRoomCreationActivity.this.hideWaitingView();
                    VectorRoomCreationActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                    return;
                }
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        });
    }
}
