package im.vector.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.media.MediaScanManager;
import fr.gouv.tchap.media.MediaScanManager.MediaScanManagerListener;
import fr.gouv.tchap.model.MediaScan;
import fr.gouv.tchap.sdk.session.room.model.RoomRetentionKt;
import fr.gouv.tchap.util.DinumUtilsKt;
import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.MXCActionBarActivity;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.activity.VectorHomeActivity;
import im.vector.activity.VectorMediasViewerActivity;
import im.vector.activity.VectorMemberDetailsActivity;
import im.vector.activity.VectorRoomActivity;
import im.vector.adapters.VectorMessagesAdapter;
import im.vector.extensions.MatrixSdkExtensionsKt;
import im.vector.listeners.IMessagesAdapterActionsListener;
import im.vector.receiver.VectorUniversalLinkReceiver;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.ExternalApplicationsUtilKt;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.PreferencesManager;
import im.vector.util.SlidableMediaInfo;
import im.vector.util.SystemUtilsKt;
import im.vector.util.VectorImageGetter;
import im.vector.util.VectorImageGetter.OnImageDownloadListener;
import im.vector.widgets.WidgetsManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.adapters.AbstractMessagesAdapter;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedEventContent;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment;
import org.matrix.androidsdk.fragments.MatrixMessagesFragment;
import org.matrix.androidsdk.listeners.MXMediaDownloadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.ImageMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.VideoMessage;

public class VectorMessageListFragment extends MatrixMessageListFragment<VectorMessagesAdapter> implements IMessagesAdapterActionsListener {
    private static final int ACTION_VECTOR_FORWARD = 2131296623;
    static final int ACTION_VECTOR_OPEN = 123456;
    private static final int ACTION_VECTOR_SAVE = 2131296630;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorMessageListFragment.class.getSimpleName();
    private static final String TAG_FRAGMENT_RECEIPTS_DIALOG = "TAG_FRAGMENT_RECEIPTS_DIALOG";
    private static final String TAG_FRAGMENT_USER_GROUPS_DIALOG = "TAG_FRAGMENT_USER_GROUPS_DIALOG";
    /* access modifiers changed from: private */
    public final ApiCallback<Void> mDeviceVerificationCallback = new ApiCallback<Void>() {
        public void onSuccess(Void voidR) {
            ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
        }

        public void onNetworkError(Exception exc) {
            ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
        }

        public void onMatrixError(MatrixError matrixError) {
            ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
        }

        public void onUnexpectedError(Exception exc) {
            ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
        }
    };
    private final Map<String, Boolean> mHighlightStatusByEventId = new HashMap();
    private int mInvalidIndexesCount = 0;
    private VectorMessageListFragmentListener mListener;
    protected MediaScanManager mMediaScanManager;
    /* access modifiers changed from: private */
    public EncryptedFileInfo mPendingEncryptedFileInfo;
    /* access modifiers changed from: private */
    public String mPendingFilename;
    /* access modifiers changed from: private */
    public String mPendingMediaMimeType;
    /* access modifiers changed from: private */
    public String mPendingMediaUrl;
    /* access modifiers changed from: private */
    public int mPendingMenuAction;
    /* access modifiers changed from: private */
    public AlertDialog mReRequestKeyDialog;
    private VectorImageGetter mVectorImageGetter;
    private VectorMessagesFragment messagesFragment;

    public interface VectorMessageListFragmentListener {
        void hideMainLoadingWheel();

        void hideNextEventsLoadingWheel();

        void hidePreviousEventsLoadingWheel();

        void onSelectedEventChange(Event event);

        void showMainLoadingWheel();

        void showNextEventsLoadingWheel();

        void showPreviousEventsLoadingWheel();
    }

    public void onEventIdClick(String str) {
    }

    public void onGroupIdClick(String str) {
    }

    public void onMediaDownloaded(int i) {
    }

    public void onRoomAliasClick(String str) {
    }

    public void onRoomIdClick(String str) {
    }

    public boolean onRowLongClick(int i) {
        return false;
    }

    public static VectorMessageListFragment newInstance(String str, String str2, String str3, String str4, int i) {
        VectorMessageListFragment vectorMessageListFragment = new VectorMessageListFragment();
        Bundle arguments = getArguments(str, str2, i);
        arguments.putString("MatrixMessageListFragment.ARG_EVENT_ID", str3);
        arguments.putString("MatrixMessageListFragment.ARG_PREVIEW_MODE_ID", str4);
        vectorMessageListFragment.setArguments(arguments);
        return vectorMessageListFragment;
    }

    public void setListener(VectorMessageListFragmentListener vectorMessageListFragmentListener) {
        this.mListener = vectorMessageListFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateView");
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        Bundle arguments = getArguments();
        String str = "MatrixMessageListFragment.ARG_EVENT_ID";
        if (arguments.containsKey(str)) {
            ((VectorMessagesAdapter) this.mAdapter).setSearchedEventId(arguments.getString(str, ""));
        }
        if (this.mRoom != null) {
            ((VectorMessagesAdapter) this.mAdapter).mIsRoomEncrypted = this.mRoom.isEncrypted();
            if (this.mSession != null) {
                DinumUtilsKt.clearExpiredRoomContents(this.mSession, this.mRoom);
            }
        }
        if (this.mSession != null) {
            this.mVectorImageGetter = new VectorImageGetter(this.mSession);
            ((VectorMessagesAdapter) this.mAdapter).setImageGetter(this.mVectorImageGetter);
        }
        this.mMessageListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                VectorMessageListFragment.this.onRowClick(i);
            }
        });
        onCreateView.setBackgroundColor(ThemeUtils.INSTANCE.getColor(getActivity(), R.attr.vctr_riot_primary_background_color));
        return onCreateView;
    }

    public MatrixMessagesFragment createMessagesFragmentInstance(String str) {
        this.messagesFragment = VectorMessagesFragment.newInstance(str);
        return this.messagesFragment;
    }

    /* access modifiers changed from: protected */
    public String getMatrixMessagesFragmentTag() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(".MATRIX_MESSAGE_FRAGMENT_TAG");
        return sb.toString();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        FragmentActivity activity = getActivity();
        if (activity instanceof VectorAppCompatActivity) {
            this.mMediaScanManager = new MediaScanManager(this.mSession.getMediaScanRestClient(), ((VectorAppCompatActivity) activity).realm);
            this.mMediaScanManager.setListener(new MediaScanManagerListener() {
                public void onMediaScanChange(MediaScan mediaScan) {
                    if (VectorMessageListFragment.this.isAdded() && VectorMessageListFragment.this.mAdapter != null) {
                        ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
                    }
                }
            });
            if (this.mAdapter != null && (this.mAdapter instanceof VectorMessagesAdapter)) {
                ((VectorMessagesAdapter) this.mAdapter).setMediaScanManager(this.mMediaScanManager);
            }
        }
    }

    public void onPause() {
        super.onPause();
        ((VectorMessagesAdapter) this.mAdapter).setVectorMessagesAdapterActionsListener(null);
        ((VectorMessagesAdapter) this.mAdapter).onPause();
        this.mVectorImageGetter.setListener(null);
    }

    public void onResume() {
        super.onResume();
        ((VectorMessagesAdapter) this.mAdapter).setVectorMessagesAdapterActionsListener(this);
        this.mVectorImageGetter.setListener(new OnImageDownloadListener() {
            public void onImageDownloaded(String str) {
                ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
            }
        });
    }

    public void onDetach() {
        super.onDetach();
        this.mMediaScanManager = null;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 567 && PermissionsToolsKt.allGranted(iArr)) {
            onMediaAction(this.mPendingMenuAction, this.mPendingMediaUrl, this.mPendingMediaMimeType, this.mPendingFilename, this.mPendingEncryptedFileInfo);
            this.mPendingMediaUrl = null;
            this.mPendingMediaMimeType = null;
            this.mPendingFilename = null;
            this.mPendingEncryptedFileInfo = null;
        }
    }

    public MXSession getSession(String str) {
        return Matrix.getMXSession(getActivity(), str);
    }

    public MXMediaCache getMXMediaCache() {
        return Matrix.getInstance(getActivity()).getMediaCache();
    }

    public VectorMessagesAdapter createMessagesAdapter() {
        VectorMessagesAdapter vectorMessagesAdapter = new VectorMessagesAdapter(this.mSession, getActivity(), getMXMediaCache());
        MediaScanManager mediaScanManager = this.mMediaScanManager;
        if (mediaScanManager != null) {
            vectorMessagesAdapter.setMediaScanManager(mediaScanManager);
        }
        return vectorMessagesAdapter;
    }

    public void onListTouch(MotionEvent motionEvent) {
        if (this.mCheckSlideToHide && motionEvent.getY() > ((float) this.mMessageListView.getHeight())) {
            this.mCheckSlideToHide = false;
            MXCActionBarActivity.dismissKeyboard(getActivity());
        }
    }

    /* access modifiers changed from: protected */
    public boolean canAddEvent(Event event) {
        String type = event.getType();
        return Event.EVENT_TYPE_MESSAGE.equals(type) || "m.room.encrypted".equals(type) || Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type) || Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type) || "m.room.member".equals(type) || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type) || Event.EVENT_TYPE_STICKER.equals(type) || (event.isCallEvent() && !Event.EVENT_TYPE_CALL_CANDIDATES.equals(type)) || WidgetsManager.WIDGET_EVENT_TYPE.equals(type);
    }

    public void onEvent(Event event, Direction direction, RoomState roomState) {
        super.onEvent(event, direction, roomState);
        if (direction == Direction.FORWARDS && this.mEventTimeLine != null && this.mEventTimeLine.isLiveTimeline()) {
            if (RoomRetentionKt.EVENT_TYPE_STATE_ROOM_RETENTION.equals(event.getType()) && DinumUtilsKt.clearExpiredRoomContents(this.mSession, this.mRoom)) {
                ((VectorMessagesAdapter) this.mAdapter).clear();
                this.mEventTimeLine.initHistory();
                this.messagesFragment.renewHistory();
            }
        }
    }

    public void setIsRoomEncrypted(boolean z) {
        if (((VectorMessagesAdapter) this.mAdapter).mIsRoomEncrypted != z) {
            ((VectorMessagesAdapter) this.mAdapter).mIsRoomEncrypted = z;
            ((VectorMessagesAdapter) this.mAdapter).notifyDataSetChanged();
        }
    }

    public ListView getMessageListView() {
        return this.mMessageListView;
    }

    public AbstractMessagesAdapter getMessageAdapter() {
        return this.mAdapter;
    }

    public void cancelSelectionMode() {
        if (this.mAdapter != null) {
            ((VectorMessagesAdapter) this.mAdapter).cancelSelectionMode();
        }
    }

    public Event getCurrentSelectedEvent() {
        if (this.mAdapter != null) {
            return ((VectorMessagesAdapter) this.mAdapter).getCurrentSelectedEvent();
        }
        return null;
    }

    public void onE2eIconClick(final Event event, final MXDeviceInfo mXDeviceInfo) {
        Builder builder = new Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        EncryptedEventContent encryptedEventContent = JsonUtils.toEncryptedEventContent(event.getWireContent().getAsJsonObject());
        View inflate = layoutInflater.inflate(R.layout.dialog_encryption_info, null);
        ((TextView) inflate.findViewById(R.id.encrypted_info_user_id)).setText(event.getSender());
        TextView textView = (TextView) inflate.findViewById(R.id.encrypted_info_curve25519_identity_key);
        if (mXDeviceInfo != null) {
            textView.setText(encryptedEventContent.sender_key);
        } else {
            textView.setText(R.string.encryption_information_none);
        }
        TextView textView2 = (TextView) inflate.findViewById(R.id.encrypted_info_claimed_ed25519_fingerprint_key);
        if (mXDeviceInfo != null) {
            textView2.setText(MatrixSdkExtensionsKt.getFingerprintHumanReadable(mXDeviceInfo));
        } else {
            textView2.setText(R.string.encryption_information_none);
        }
        ((TextView) inflate.findViewById(R.id.encrypted_info_algorithm)).setText(encryptedEventContent.algorithm);
        ((TextView) inflate.findViewById(R.id.encrypted_info_session_id)).setText(encryptedEventContent.session_id);
        View findViewById = inflate.findViewById(R.id.encrypted_info_decryption_error_label);
        TextView textView3 = (TextView) inflate.findViewById(R.id.encrypted_info_decryption_error);
        if (event.getCryptoError() != null) {
            findViewById.setVisibility(0);
            textView3.setVisibility(0);
            StringBuilder sb = new StringBuilder();
            String str = "**";
            sb.append(str);
            sb.append(event.getCryptoError().getLocalizedMessage());
            sb.append(str);
            textView3.setText(sb.toString());
        } else {
            findViewById.setVisibility(8);
            textView3.setVisibility(8);
        }
        View findViewById2 = inflate.findViewById(R.id.encrypted_info_no_device_information_layout);
        View findViewById3 = inflate.findViewById(R.id.encrypted_info_sender_device_information_layout);
        if (mXDeviceInfo != null) {
            findViewById2.setVisibility(8);
            findViewById3.setVisibility(0);
            ((TextView) inflate.findViewById(R.id.encrypted_info_name)).setText(mXDeviceInfo.displayName());
            ((TextView) inflate.findViewById(R.id.encrypted_info_device_id)).setText(mXDeviceInfo.deviceId);
            TextView textView4 = (TextView) inflate.findViewById(R.id.encrypted_info_verification);
            if (mXDeviceInfo.isUnknown() || mXDeviceInfo.isUnverified()) {
                textView4.setText(R.string.encryption_information_not_verified);
            } else if (mXDeviceInfo.isVerified()) {
                textView4.setText(R.string.encryption_information_verified);
            } else {
                textView4.setText(R.string.encryption_information_blocked);
            }
            ((TextView) inflate.findViewById(R.id.encrypted_ed25519_fingerprint)).setText(MatrixSdkExtensionsKt.getFingerprintHumanReadable(mXDeviceInfo));
        } else {
            findViewById2.setVisibility(0);
            findViewById3.setVisibility(8);
        }
        builder.setView(inflate);
        builder.setTitle((int) R.string.encryption_information_title);
        builder.setNeutralButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        if (!TextUtils.equals(encryptedEventContent.device_id, this.mSession.getCredentials().deviceId) && event.getCryptoError() == null && mXDeviceInfo != null) {
            if (mXDeviceInfo.isUnverified() || mXDeviceInfo.isUnknown()) {
                builder.setNegativeButton((int) R.string.encryption_information_verify, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonActivityUtils.displayDeviceVerificationDialog(mXDeviceInfo, event.getSender(), VectorMessageListFragment.this.mSession, VectorMessageListFragment.this.getActivity(), VectorMessageListFragment.this.mDeviceVerificationCallback);
                    }
                });
                builder.setPositiveButton((int) R.string.encryption_information_block, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorMessageListFragment.this.mSession.getCrypto().setDeviceVerification(2, mXDeviceInfo.deviceId, event.getSender(), VectorMessageListFragment.this.mDeviceVerificationCallback);
                    }
                });
            } else if (mXDeviceInfo.isVerified()) {
                builder.setNegativeButton((int) R.string.encryption_information_unverify, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorMessageListFragment.this.mSession.getCrypto().setDeviceVerification(0, mXDeviceInfo.deviceId, event.getSender(), VectorMessageListFragment.this.mDeviceVerificationCallback);
                    }
                });
                builder.setPositiveButton((int) R.string.encryption_information_block, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorMessageListFragment.this.mSession.getCrypto().setDeviceVerification(2, mXDeviceInfo.deviceId, event.getSender(), VectorMessageListFragment.this.mDeviceVerificationCallback);
                    }
                });
            } else {
                builder.setNegativeButton((int) R.string.encryption_information_verify, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonActivityUtils.displayDeviceVerificationDialog(mXDeviceInfo, event.getSender(), VectorMessageListFragment.this.mSession, VectorMessageListFragment.this.getActivity(), VectorMessageListFragment.this.mDeviceVerificationCallback);
                    }
                });
                builder.setPositiveButton((int) R.string.encryption_information_unblock, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorMessageListFragment.this.mSession.getCrypto().setDeviceVerification(0, mXDeviceInfo.deviceId, event.getSender(), VectorMessageListFragment.this.mDeviceVerificationCallback);
                    }
                });
            }
        }
        final AlertDialog show = builder.show();
        if (mXDeviceInfo == null) {
            this.mSession.getCrypto().getDeviceList().downloadKeys(Collections.singletonList(event.getSender()), true, new ApiCallback<MXUsersDevicesMap<MXDeviceInfo>>() {
                public void onMatrixError(MatrixError matrixError) {
                }

                public void onNetworkError(Exception exc) {
                }

                public void onUnexpectedError(Exception exc) {
                }

                public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                    FragmentActivity activity = VectorMessageListFragment.this.getActivity();
                    if (activity != null && !activity.isFinishing() && show.isShowing()) {
                        EncryptedEventContent encryptedEventContent = JsonUtils.toEncryptedEventContent(event.getWireContent().getAsJsonObject());
                        MXDeviceInfo deviceWithIdentityKey = VectorMessageListFragment.this.mSession.getCrypto().deviceWithIdentityKey(encryptedEventContent.sender_key, encryptedEventContent.algorithm);
                        if (deviceWithIdentityKey != null) {
                            show.cancel();
                            VectorMessageListFragment.this.onE2eIconClick(event, deviceWithIdentityKey);
                        }
                    }
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:0x0181  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0189  */
    public void onEventAction(final Event event, final String str, final int i) {
        EncryptedFileInfo encryptedFileInfo;
        String str2;
        String str3;
        String url;
        String mimeType;
        EncryptedFileInfo encryptedFileInfo2;
        if (i == R.id.ic_action_vector_resend_message) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VectorMessageListFragment.this.resend(event);
                }
            });
        } else if (i == R.id.ic_action_vector_redact_message) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Builder builder = new Builder(VectorMessageListFragment.this.getActivity());
                    StringBuilder sb = new StringBuilder();
                    sb.append(VectorMessageListFragment.this.getString(R.string.redact));
                    sb.append(" ?");
                    builder.setMessage((CharSequence) sb.toString()).setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (event.isUndelivered() || event.isUnknownDevice()) {
                                VectorMessageListFragment.this.mSession.getDataHandler().deleteRoomEvent(event);
                                ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).removeEventById(event.eventId);
                                ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
                                VectorMessageListFragment.this.mEventSendingListener.onMessageRedacted(event);
                                return;
                            }
                            VectorMessageListFragment.this.redactEvent(event.eventId);
                        }
                    }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                }
            });
        } else if (i == R.id.ic_action_vector_copy) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    SystemUtilsKt.copyToClipboard(VectorMessageListFragment.this.getActivity(), str);
                }
            });
        } else if (i == R.id.ic_action_vector_cancel_upload || i == R.id.ic_action_vector_cancel_download) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    new Builder(VectorMessageListFragment.this.getActivity()).setMessage(i == R.id.ic_action_vector_cancel_upload ? R.string.attachment_cancel_upload : R.string.attachment_cancel_download).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorMessageListFragment.this.mRoom.cancelEventSending(event);
                            VectorMessageListFragment.this.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
                                }
                            });
                        }
                    }).setNegativeButton((int) R.string.no, (OnClickListener) null).show();
                }
            });
        } else if (i == R.id.ic_action_vector_quote) {
            FragmentActivity activity = getActivity();
            if (activity != null && (activity instanceof VectorRoomActivity)) {
                String str4 = "\n\n";
                String[] split = str.split(str4);
                int i2 = 0;
                String str5 = "";
                String str6 = str5;
                while (i2 < split.length) {
                    if (!split[i2].trim().equals(str5)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str6);
                        sb.append("> ");
                        sb.append(split[i2]);
                        str6 = sb.toString();
                    }
                    i2++;
                    if (i2 != split.length) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str6);
                        sb2.append(str4);
                        str6 = sb2.toString();
                    }
                }
                VectorRoomActivity vectorRoomActivity = (VectorRoomActivity) activity;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str6);
                sb3.append(str4);
                vectorRoomActivity.insertQuoteInTextEditor(sb3.toString());
            }
        } else {
            String str7 = null;
            if (i == R.id.ic_action_vector_share || i == R.id.ic_action_vector_forward || i == R.id.ic_action_vector_save) {
                Message message = JsonUtils.toMessage(event.getContent());
                if (message instanceof ImageMessage) {
                    ImageMessage imageMessage = (ImageMessage) message;
                    url = imageMessage.getUrl();
                    mimeType = imageMessage.getMimeType();
                    encryptedFileInfo2 = imageMessage.file;
                } else {
                    if (message instanceof VideoMessage) {
                        VideoMessage videoMessage = (VideoMessage) message;
                        String url2 = videoMessage.getUrl();
                        EncryptedFileInfo encryptedFileInfo3 = videoMessage.file;
                        if (videoMessage.info != null) {
                            str7 = videoMessage.info.mimetype;
                        }
                        str2 = str7;
                        str3 = url2;
                        encryptedFileInfo = encryptedFileInfo3;
                    } else if (message instanceof FileMessage) {
                        FileMessage fileMessage = (FileMessage) message;
                        url = fileMessage.getUrl();
                        mimeType = fileMessage.getMimeType();
                        encryptedFileInfo2 = fileMessage.file;
                    } else {
                        str3 = null;
                        str2 = null;
                        encryptedFileInfo = null;
                    }
                    if (str3 == null) {
                        onMediaAction(i, str3, str2, message.body, encryptedFileInfo);
                        return;
                    } else if (i == R.id.ic_action_vector_share || i == R.id.ic_action_vector_forward || i == R.id.ic_action_vector_quote) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.SEND");
                        intent.putExtra("android.intent.extra.TEXT", str);
                        intent.setType("text/plain");
                        if (i == R.id.ic_action_vector_forward) {
                            CommonActivityUtils.sendFilesTo(getActivity(), intent);
                            return;
                        } else {
                            startActivity(intent);
                            return;
                        }
                    } else {
                        return;
                    }
                }
                encryptedFileInfo = encryptedFileInfo2;
                str3 = url;
                str2 = mimeType;
                if (str3 == null) {
                }
            } else if (i == R.id.ic_action_vector_report) {
                onMessageReport(event);
            } else if (i == R.id.ic_action_view_source || i == R.id.ic_action_view_decrypted_source) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        View inflate = VectorMessageListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.dialog_event_content, null);
                        ((TextView) inflate.findViewById(R.id.event_content_text_view)).setText(new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson((JsonElement) JsonUtils.toJson(i == R.id.ic_action_view_source ? event : event.getClearEvent())));
                        new Builder(VectorMessageListFragment.this.getActivity()).setView(inflate).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
                    }
                });
            } else if (i == R.id.ic_action_device_verification) {
                onE2eIconClick(event, ((VectorMessagesAdapter) this.mAdapter).getDeviceInfo(event.eventId));
            } else if (i == R.id.ic_action_re_request_e2e_key) {
                this.mSession.getCrypto().reRequestRoomKeyForEvent(event);
                this.mReRequestKeyDialog = new Builder(getActivity()).setTitle((int) R.string.e2e_re_request_encryption_key_dialog_title).setMessage((int) R.string.e2e_re_request_encryption_key_dialog_content).setPositiveButton((int) R.string.ok, (OnClickListener) null).setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        VectorMessageListFragment.this.mReRequestKeyDialog = null;
                    }
                }).show();
            }
        }
    }

    public void onEventDecrypted() {
        AlertDialog alertDialog = this.mReRequestKeyDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void onSelectedEventChange(Event event) {
        if (this.mListener != null && isAdded()) {
            this.mListener.onSelectedEventChange(event);
        }
    }

    private void onMessageReport(final Event event) {
        final EditText editText = new EditText(getActivity());
        new Builder(getActivity()).setTitle((int) R.string.room_event_action_report_prompt_reason).setView((View) editText).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VectorMessageListFragment.this.mRoom.report(event.eventId, -100, editText.getText().toString(), new SimpleApiCallback<Void>(VectorMessageListFragment.this.getActivity()) {
                    public void onSuccess(Void voidR) {
                        new Builder(VectorMessageListFragment.this.getActivity()).setMessage((int) R.string.room_event_action_report_prompt_ignore_user).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(event.sender);
                                VectorMessageListFragment.this.mSession.ignoreUsers(arrayList, new SimpleApiCallback<Void>() {
                                    public void onSuccess(Void voidR) {
                                    }
                                });
                            }
                        }).setNegativeButton((int) R.string.no, (OnClickListener) null).show();
                    }
                });
            }
        }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
    }

    /* access modifiers changed from: 0000 */
    public void onMediaAction(int i, String str, String str2, String str3, EncryptedFileInfo encryptedFileInfo) {
        String str4 = str;
        String str5 = str2;
        String name = new File(str3).getName();
        MXMediaCache mediaCache = Matrix.getInstance(getActivity()).getMediaCache();
        if (mediaCache.isMediaCached(str4, str5)) {
            final int i2 = i;
            final String str6 = name;
            final String str7 = str2;
            final String str8 = str;
            final String str9 = str3;
            final EncryptedFileInfo encryptedFileInfo2 = encryptedFileInfo;
            final MXMediaCache mXMediaCache = mediaCache;
            AnonymousClass20 r0 = new SimpleApiCallback<File>() {
                public void onSuccess(File file) {
                    if (file != null) {
                        int i = i2;
                        if (i != R.id.ic_action_vector_save && i != VectorMessageListFragment.ACTION_VECTOR_OPEN) {
                            Uri uri = null;
                            try {
                                uri = FileProvider.getUriForFile(VectorMessageListFragment.this.getActivity(), "fr.gouv.tchap.a.fileProvider", mXMediaCache.moveToShareFolder(file, str6));
                            } catch (Exception e) {
                                String access$3100 = VectorMessageListFragment.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("onMediaAction Selected file cannot be shared: ");
                                sb.append(e.getMessage());
                                Log.e(access$3100, sb.toString(), e);
                            }
                            if (uri != null) {
                                Intent intent = new Intent();
                                intent.setFlags(1);
                                intent.setAction("android.intent.action.SEND");
                                intent.setType(str7);
                                intent.putExtra("android.intent.extra.STREAM", uri);
                                if (i2 == R.id.ic_action_vector_forward) {
                                    CommonActivityUtils.sendFilesTo(VectorMessageListFragment.this.getActivity(), intent);
                                } else {
                                    VectorMessageListFragment.this.startActivity(intent);
                                }
                            }
                        } else if (PermissionsToolsKt.checkPermissions(2, (Fragment) VectorMessageListFragment.this, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE)) {
                            CommonActivityUtils.saveMediaIntoDownloads(VectorMessageListFragment.this.getActivity(), file, str6, str7, new SimpleApiCallback<String>() {
                                public void onSuccess(String str) {
                                    if (str == null) {
                                        return;
                                    }
                                    if (i2 == R.id.ic_action_vector_save) {
                                        Toast.makeText(VectorMessageListFragment.this.getActivity(), VectorMessageListFragment.this.getText(R.string.media_slider_saved), 1).show();
                                    } else {
                                        ExternalApplicationsUtilKt.openMedia(VectorMessageListFragment.this.getActivity(), str, str7);
                                    }
                                }
                            });
                        } else {
                            VectorMessageListFragment.this.mPendingMenuAction = i2;
                            VectorMessageListFragment.this.mPendingMediaUrl = str8;
                            VectorMessageListFragment.this.mPendingMediaMimeType = str7;
                            VectorMessageListFragment.this.mPendingFilename = str9;
                            VectorMessageListFragment.this.mPendingEncryptedFileInfo = encryptedFileInfo2;
                        }
                    }
                }
            };
            mediaCache.createTmpDecryptedMediaFile(str4, str5, encryptedFileInfo, r0);
            return;
        }
        EncryptedFileInfo encryptedFileInfo3 = encryptedFileInfo;
        String downloadMedia = mediaCache.downloadMedia(getActivity().getApplicationContext(), this.mSession.getHomeServerConfig(), str, str2, encryptedFileInfo);
        ((VectorMessagesAdapter) this.mAdapter).notifyDataSetChanged();
        if (downloadMedia != null) {
            final String str10 = downloadMedia;
            final int i3 = i;
            final String str11 = str;
            final String str12 = str2;
            final String str13 = name;
            final EncryptedFileInfo encryptedFileInfo4 = encryptedFileInfo;
            AnonymousClass21 r02 = new MXMediaDownloadListener() {
                public void onDownloadError(String str, JsonElement jsonElement) {
                    MatrixError matrixError = JsonUtils.toMatrixError(jsonElement);
                    if (matrixError != null && matrixError.isSupportedErrorCode() && VectorMessageListFragment.this.getActivity() != null) {
                        Toast.makeText(VectorMessageListFragment.this.getActivity(), matrixError.getLocalizedMessage(), 1).show();
                    }
                }

                public void onDownloadComplete(String str) {
                    if (str.equals(str10)) {
                        VectorMessageListFragment.this.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                VectorMessageListFragment.this.onMediaAction(i3, str11, str12, str13, encryptedFileInfo4);
                            }
                        });
                    }
                }
            };
            mediaCache.addDownloadListener(downloadMedia, r02);
        }
    }

    public boolean isDisplayAllEvents() {
        return PreferencesManager.displayAllEvents(getActivity());
    }

    public void showLoadingBackProgress() {
        if (this.mListener != null && isAdded()) {
            this.mListener.showPreviousEventsLoadingWheel();
        }
    }

    public void hideLoadingBackProgress() {
        if (this.mListener != null && isAdded()) {
            this.mListener.hidePreviousEventsLoadingWheel();
        }
    }

    public void showLoadingForwardProgress() {
        if (this.mListener != null && isAdded()) {
            this.mListener.showNextEventsLoadingWheel();
        }
    }

    public void hideLoadingForwardProgress() {
        if (this.mListener != null && isAdded()) {
            this.mListener.hideNextEventsLoadingWheel();
        }
    }

    public void showInitLoading() {
        if (this.mListener != null && isAdded()) {
            this.mListener.showMainLoadingWheel();
        }
    }

    public void hideInitLoading() {
        if (this.mListener != null && isAdded()) {
            this.mListener.hideMainLoadingWheel();
        }
    }

    /* access modifiers changed from: 0000 */
    public List<SlidableMediaInfo> listSlidableMessages() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < ((VectorMessagesAdapter) this.mAdapter).getCount(); i++) {
            Message message = JsonUtils.toMessage(((MessageRow) ((VectorMessagesAdapter) this.mAdapter).getItem(i)).getEvent().getContent());
            SlidableMediaInfo slidableMediaInfo = null;
            String str = message.msgtype;
            String str2 = Message.MSGTYPE_IMAGE;
            if (str2.equals(str)) {
                ImageMessage imageMessage = (ImageMessage) message;
                slidableMediaInfo = new SlidableMediaInfo();
                slidableMediaInfo.mMessageType = str2;
                slidableMediaInfo.mFileName = imageMessage.body;
                slidableMediaInfo.mMediaUrl = imageMessage.getUrl();
                slidableMediaInfo.mThumbnailUrl = imageMessage.getThumbnailUrl();
                slidableMediaInfo.mRotationAngle = imageMessage.getRotation();
                slidableMediaInfo.mOrientation = imageMessage.getOrientation();
                slidableMediaInfo.mMimeType = imageMessage.getMimeType();
                slidableMediaInfo.mEncryptedFileInfo = imageMessage.file;
                if (imageMessage.info != null) {
                    slidableMediaInfo.mEncryptedThumbnailFileInfo = imageMessage.info.thumbnail_file;
                }
            } else {
                String str3 = message.msgtype;
                String str4 = Message.MSGTYPE_VIDEO;
                if (str4.equals(str3)) {
                    VideoMessage videoMessage = (VideoMessage) message;
                    slidableMediaInfo = new SlidableMediaInfo();
                    slidableMediaInfo.mMessageType = str4;
                    slidableMediaInfo.mFileName = videoMessage.body;
                    slidableMediaInfo.mMediaUrl = videoMessage.getUrl();
                    slidableMediaInfo.mThumbnailUrl = videoMessage.getThumbnailUrl();
                    slidableMediaInfo.mMimeType = videoMessage.getMimeType();
                    slidableMediaInfo.mEncryptedFileInfo = videoMessage.file;
                    if (videoMessage.info != null) {
                        slidableMediaInfo.mEncryptedThumbnailFileInfo = videoMessage.info.thumbnail_file;
                    }
                }
            }
            if (slidableMediaInfo != null) {
                MediaScanManager mediaScanManager = this.mMediaScanManager;
                if (mediaScanManager != null && mediaScanManager.isTrustedSlidableMediaInfo(slidableMediaInfo)) {
                    arrayList.add(slidableMediaInfo);
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public int getMediaMessagePosition(List<SlidableMediaInfo> list, Message message) {
        String str = message instanceof ImageMessage ? ((ImageMessage) message).getUrl() : message instanceof VideoMessage ? ((VideoMessage) message).getUrl() : null;
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (((SlidableMediaInfo) list.get(i)).mMediaUrl.equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public void onRowClick(int i) {
        try {
            ((VectorMessagesAdapter) this.mAdapter).onEventTap(((MessageRow) ((VectorMessagesAdapter) this.mAdapter).getItem(i)).getEvent());
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onRowClick() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void onContentClick(int i) {
        try {
            Event event = ((MessageRow) ((VectorMessagesAdapter) this.mAdapter).getItem(i)).getEvent();
            if (((VectorMessagesAdapter) this.mAdapter).isInSelectionMode()) {
                ((VectorMessagesAdapter) this.mAdapter).onEventTap(null);
                return;
            }
            Message message = JsonUtils.toMessage(event.getContent());
            if (!Message.MSGTYPE_IMAGE.equals(message.msgtype)) {
                if (!Message.MSGTYPE_VIDEO.equals(message.msgtype)) {
                    if (!Message.MSGTYPE_FILE.equals(message.msgtype)) {
                        if (!Message.MSGTYPE_AUDIO.equals(message.msgtype)) {
                            ((VectorMessagesAdapter) this.mAdapter).onEventTap(event);
                        }
                    }
                    FileMessage fileMessage = JsonUtils.toFileMessage(event.getContent());
                    if (fileMessage.getUrl() != null) {
                        onMediaAction(ACTION_VECTOR_OPEN, fileMessage.getUrl(), fileMessage.getMimeType(), fileMessage.body, fileMessage.file);
                    }
                }
            }
            List listSlidableMessages = listSlidableMessages();
            int mediaMessagePosition = getMediaMessagePosition(listSlidableMessages, message);
            if (mediaMessagePosition >= 0) {
                Intent intent = new Intent(getActivity(), VectorMediasViewerActivity.class);
                intent.putExtra(VectorMediasViewerActivity.EXTRA_MATRIX_ID, this.mSession.getCredentials().userId);
                intent.putExtra(VectorMediasViewerActivity.KEY_THUMBNAIL_WIDTH, ((VectorMessagesAdapter) this.mAdapter).getMaxThumbnailWidth());
                intent.putExtra(VectorMediasViewerActivity.KEY_THUMBNAIL_HEIGHT, ((VectorMessagesAdapter) this.mAdapter).getMaxThumbnailHeight());
                intent.putExtra(VectorMediasViewerActivity.KEY_INFO_LIST, (ArrayList) listSlidableMessages);
                intent.putExtra(VectorMediasViewerActivity.KEY_INFO_LIST_INDEX, mediaMessagePosition);
                getActivity().startActivity(intent);
            }
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onContentClick() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public boolean onContentLongClick(int i) {
        return onRowLongClick(i);
    }

    public void onAvatarClick(String str) {
        try {
            Intent intent = new Intent(getActivity(), VectorMemberDetailsActivity.class);
            if (getRoomPreviewData() != null) {
                intent.putExtra(VectorMemberDetailsActivity.EXTRA_STORE_ID, Matrix.getInstance(getActivity()).addTmpStore(this.mEventTimeLine.getStore()));
            }
            intent.putExtra("EXTRA_ROOM_ID", this.mRoom.getRoomId());
            intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, str);
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
            getActivity().startActivityForResult(intent, 2);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onAvatarClick() failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
        }
    }

    public boolean onAvatarLongClick(String str) {
        if (getActivity() instanceof VectorRoomActivity) {
            try {
                RoomState state = this.mRoom.getState();
                if (state != null) {
                    String memberName = state.getMemberName(str);
                    if (!TextUtils.isEmpty(memberName)) {
                        ((VectorRoomActivity) getActivity()).insertUserDisplayNameInTextEditor(memberName);
                    }
                }
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onAvatarLongClick() failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        return true;
    }

    public void onSenderNameClick(String str, String str2) {
        if (getActivity() instanceof VectorRoomActivity) {
            try {
                ((VectorRoomActivity) getActivity()).insertUserDisplayNameInTextEditor(str2);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onSenderNameClick() failed ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
        }
    }

    public void onMoreReadReceiptClick(String str) {
        String str2 = TAG_FRAGMENT_RECEIPTS_DIALOG;
        try {
            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            VectorReadReceiptsDialogFragment vectorReadReceiptsDialogFragment = (VectorReadReceiptsDialogFragment) supportFragmentManager.findFragmentByTag(str2);
            if (vectorReadReceiptsDialogFragment != null) {
                vectorReadReceiptsDialogFragment.dismissAllowingStateLoss();
            }
            VectorReadReceiptsDialogFragment.Companion.newInstance(this.mSession.getMyUserId(), this.mRoom.getRoomId(), str).show(supportFragmentManager, str2);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onMoreReadReceiptClick() failed ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
        }
    }

    public void onGroupFlairClick(String str, List<String> list) {
        String str2 = TAG_FRAGMENT_USER_GROUPS_DIALOG;
        try {
            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            VectorUserGroupsDialogFragment vectorUserGroupsDialogFragment = (VectorUserGroupsDialogFragment) supportFragmentManager.findFragmentByTag(str2);
            if (vectorUserGroupsDialogFragment != null) {
                vectorUserGroupsDialogFragment.dismissAllowingStateLoss();
            }
            VectorUserGroupsDialogFragment.newInstance(this.mSession.getMyUserId(), str, list).show(supportFragmentManager, str2);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onGroupFlairClick() failed ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
        }
    }

    public void onURLClick(Uri uri) {
        String str = PermalinkUtils.ULINK_MATRIX_USER_ID_KEY;
        if (uri != null) {
            try {
                Map parseUniversalLink = VectorUniversalLinkReceiver.parseUniversalLink(uri);
                if (parseUniversalLink == null) {
                    ExternalApplicationsUtilKt.openUrlInExternalBrowser((Context) getActivity(), uri);
                } else if (parseUniversalLink.containsKey(str)) {
                    Intent intent = new Intent(getActivity(), VectorMemberDetailsActivity.class);
                    intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, (String) parseUniversalLink.get(str));
                    intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
                    getActivity().startActivityForResult(intent, 2);
                } else {
                    Intent intent2 = new Intent(getActivity(), VectorHomeActivity.class);
                    intent2.setFlags(603979776);
                    intent2.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_UNIVERSAL_LINK, uri);
                    getActivity().startActivity(intent2);
                }
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onURLClick() failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onMatrixUserIdClick(String str) {
        try {
            Intent intent = new Intent(getActivity(), VectorMemberDetailsActivity.class);
            intent.putExtra("EXTRA_ROOM_ID", this.mRoom.getRoomId());
            intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, str);
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
            startActivity(intent);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onMatrixUserIdClick() failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
        }
    }

    public void onInvalidIndexes() {
        this.mInvalidIndexesCount++;
        if (1 == this.mInvalidIndexesCount) {
            this.mMessageListView.post(new Runnable() {
                public void run() {
                    ((VectorMessagesAdapter) VectorMessageListFragment.this.mAdapter).notifyDataSetChanged();
                }
            });
        } else {
            this.mMessageListView.post(new Runnable() {
                public void run() {
                    if (VectorMessageListFragment.this.getActivity() != null) {
                        VectorMessageListFragment.this.getActivity().finish();
                    }
                }
            });
        }
    }

    public boolean shouldHighlightEvent(Event event) {
        boolean z = false;
        if (!(event == null || event.eventId == null)) {
            String str = event.eventId;
            Boolean bool = (Boolean) this.mHighlightStatusByEventId.get(str);
            if (bool != null) {
                return bool.booleanValue();
            }
            if (this.mSession.getDataHandler().getBingRulesManager().fulfilledHighlightBingRule(event) != null) {
                z = true;
            }
            this.mHighlightStatusByEventId.put(str, Boolean.valueOf(z));
        }
        return z;
    }
}
