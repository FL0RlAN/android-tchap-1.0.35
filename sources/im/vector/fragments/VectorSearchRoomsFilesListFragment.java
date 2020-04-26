package im.vector.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.fragment.app.FragmentActivity;
import im.vector.activity.VectorMediasViewerActivity;
import im.vector.adapters.VectorMessagesAdapter;
import im.vector.adapters.VectorSearchFilesListAdapter;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.Message;

public class VectorSearchRoomsFilesListFragment extends VectorSearchMessagesListFragment {
    public static VectorSearchRoomsFilesListFragment newInstance(String str, String str2, int i) {
        VectorSearchRoomsFilesListFragment vectorSearchRoomsFilesListFragment = new VectorSearchRoomsFilesListFragment();
        vectorSearchRoomsFilesListFragment.setArguments(getArguments(str, str2, i));
        return vectorSearchRoomsFilesListFragment;
    }

    public VectorMessagesAdapter createMessagesAdapter() {
        boolean z = true;
        this.mIsMediaSearch = true;
        MXSession mXSession = this.mSession;
        FragmentActivity activity = getActivity();
        if (this.mRoomId != null) {
            z = false;
        }
        VectorSearchFilesListAdapter vectorSearchFilesListAdapter = new VectorSearchFilesListAdapter(mXSession, activity, z, getMXMediaCache());
        if (this.mMediaScanManager != null) {
            vectorSearchFilesListAdapter.setMediaScanManager(this.mMediaScanManager);
        }
        return vectorSearchFilesListAdapter;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.mMessageListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Event event = ((MessageRow) ((VectorMessagesAdapter) VectorSearchRoomsFilesListFragment.this.mAdapter).getItem(i)).getEvent();
                if (((VectorMessagesAdapter) VectorSearchRoomsFilesListFragment.this.mAdapter).isInSelectionMode()) {
                    ((VectorMessagesAdapter) VectorSearchRoomsFilesListFragment.this.mAdapter).onEventTap(null);
                    return;
                }
                if (VectorSearchRoomsFilesListFragment.this.mMediaScanManager != null && !VectorSearchRoomsFilesListFragment.this.mMediaScanManager.isUncheckedOrUntrustedMediaEvent(event)) {
                    Message message = JsonUtils.toMessage(event.getContent());
                    if (!Message.MSGTYPE_IMAGE.equals(message.msgtype)) {
                        if (!Message.MSGTYPE_VIDEO.equals(message.msgtype)) {
                            if (Message.MSGTYPE_FILE.equals(message.msgtype)) {
                                FileMessage fileMessage = JsonUtils.toFileMessage(event.getContent());
                                String url = fileMessage.getUrl();
                                if (url != null) {
                                    VectorSearchRoomsFilesListFragment.this.onMediaAction(123456, url, fileMessage.getMimeType(), fileMessage.body, fileMessage.file);
                                }
                            }
                        }
                    }
                    List listSlidableMessages = VectorSearchRoomsFilesListFragment.this.listSlidableMessages();
                    int mediaMessagePosition = VectorSearchRoomsFilesListFragment.this.getMediaMessagePosition(listSlidableMessages, message);
                    if (mediaMessagePosition >= 0) {
                        Intent intent = new Intent(VectorSearchRoomsFilesListFragment.this.getActivity(), VectorMediasViewerActivity.class);
                        intent.putExtra(VectorMediasViewerActivity.EXTRA_MATRIX_ID, VectorSearchRoomsFilesListFragment.this.mSession.getCredentials().userId);
                        intent.putExtra(VectorMediasViewerActivity.KEY_THUMBNAIL_WIDTH, ((VectorMessagesAdapter) VectorSearchRoomsFilesListFragment.this.mAdapter).getMaxThumbnailWidth());
                        intent.putExtra(VectorMediasViewerActivity.KEY_THUMBNAIL_HEIGHT, ((VectorMessagesAdapter) VectorSearchRoomsFilesListFragment.this.mAdapter).getMaxThumbnailHeight());
                        intent.putExtra(VectorMediasViewerActivity.KEY_INFO_LIST, (ArrayList) listSlidableMessages);
                        intent.putExtra(VectorMediasViewerActivity.KEY_INFO_LIST_INDEX, mediaMessagePosition);
                        VectorSearchRoomsFilesListFragment.this.getActivity().startActivity(intent);
                    }
                }
            }
        });
        return onCreateView;
    }
}
