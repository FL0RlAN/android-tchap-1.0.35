package im.vector.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorMemberDetailsActivity;
import im.vector.activity.VectorRoomActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.rest.model.group.Group;
import org.matrix.androidsdk.rest.model.group.GroupRoom;
import org.matrix.androidsdk.rest.model.group.GroupUser;

public class GroupUtils {
    private static final String LOG_TAG = GroupUtils.class.getSimpleName();

    public static List<Group> getFilteredGroups(List<Group> list, CharSequence charSequence) {
        String trim = charSequence != null ? charSequence.toString().trim() : null;
        if (TextUtils.isEmpty(trim)) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        Pattern compile = Pattern.compile(Pattern.quote(trim), 2);
        for (Group group : list) {
            if (compile.matcher(group.getDisplayName()).find()) {
                arrayList.add(group);
            }
        }
        return arrayList;
    }

    public static List<GroupUser> getFilteredGroupUsers(List<GroupUser> list, CharSequence charSequence) {
        String trim = charSequence != null ? charSequence.toString().trim() : null;
        if (TextUtils.isEmpty(trim)) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        Pattern compile = Pattern.compile(Pattern.quote(trim), 2);
        for (GroupUser groupUser : list) {
            if (compile.matcher(groupUser.getDisplayname()).find()) {
                arrayList.add(groupUser);
            }
        }
        return arrayList;
    }

    public static List<GroupRoom> getFilteredGroupRooms(List<GroupRoom> list, CharSequence charSequence) {
        String trim = charSequence != null ? charSequence.toString().trim() : null;
        if (TextUtils.isEmpty(trim)) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        Pattern compile = Pattern.compile(Pattern.quote(trim), 2);
        for (GroupRoom groupRoom : list) {
            if (compile.matcher(groupRoom.getDisplayName()).find()) {
                arrayList.add(groupRoom);
            }
        }
        return arrayList;
    }

    public static void openGroupUserPage(Activity activity, MXSession mXSession, GroupUser groupUser) {
        Intent intent = new Intent(activity, VectorMemberDetailsActivity.class);
        intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, groupUser.userId);
        if (!TextUtils.isEmpty(groupUser.avatarUrl)) {
            intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_AVATAR_URL, groupUser.avatarUrl);
        }
        if (!TextUtils.isEmpty(groupUser.displayname)) {
            intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_DISPLAY_NAME, groupUser.displayname);
        }
        intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", mXSession.getCredentials().userId);
        activity.startActivity(intent);
    }

    public static void openGroupRoom(final Activity activity, MXSession mXSession, final GroupRoom groupRoom, final SuccessCallback<Void> successCallback) {
        Room room = mXSession.getDataHandler().getStore().getRoom(groupRoom.roomId);
        if (room == null || !room.isJoined()) {
            final RoomPreviewData roomPreviewData = new RoomPreviewData(mXSession, groupRoom.roomId, null, groupRoom.canonicalAlias, null);
            roomPreviewData.fetchPreviewData(new ApiCallback<Void>() {
                private void onDone() {
                    successCallback.onSuccess(null);
                    CommonActivityUtils.previewRoom(activity, roomPreviewData);
                }

                public void onSuccess(Void voidR) {
                    onDone();
                }

                private void onError() {
                    roomPreviewData.setPublicRoom(groupRoom);
                    roomPreviewData.setRoomName(groupRoom.name);
                    onDone();
                }

                public void onNetworkError(Exception exc) {
                    onError();
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError();
                }

                public void onUnexpectedError(Exception exc) {
                    onError();
                }
            });
            return;
        }
        successCallback.onSuccess(null);
        Intent intent = new Intent(activity, VectorRoomActivity.class);
        intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", mXSession.getMyUserId());
        intent.putExtra("EXTRA_ROOM_ID", groupRoom.roomId);
        activity.startActivity(intent);
    }
}
