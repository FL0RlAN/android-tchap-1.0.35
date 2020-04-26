package im.vector.listeners;

import android.net.Uri;
import java.util.List;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.rest.model.Event;

public interface IMessagesAdapterActionsListener {
    void onAvatarClick(String str);

    boolean onAvatarLongClick(String str);

    void onContentClick(int i);

    boolean onContentLongClick(int i);

    void onE2eIconClick(Event event, MXDeviceInfo mXDeviceInfo);

    void onEventAction(Event event, String str, int i);

    void onEventDecrypted();

    void onEventIdClick(String str);

    void onGroupFlairClick(String str, List<String> list);

    void onGroupIdClick(String str);

    void onInvalidIndexes();

    void onMatrixUserIdClick(String str);

    void onMediaDownloaded(int i);

    void onMoreReadReceiptClick(String str);

    void onRoomAliasClick(String str);

    void onRoomIdClick(String str);

    void onRowClick(int i);

    boolean onRowLongClick(int i);

    void onSelectedEventChange(Event event);

    void onSenderNameClick(String str, String str2);

    void onURLClick(Uri uri);

    boolean shouldHighlightEvent(Event event);
}
