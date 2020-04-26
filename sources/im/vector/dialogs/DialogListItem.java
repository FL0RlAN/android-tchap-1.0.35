package im.vector.dialogs;

import fr.gouv.tchap.a.R;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b0\u0018\u00002\u00020\u0001:\t\t\n\u000b\f\r\u000e\u000f\u0010\u0011B\u001b\b\u0002\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u0001\t\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a¨\u0006\u001b"}, d2 = {"Lim/vector/dialogs/DialogListItem;", "", "iconRes", "", "titleRes", "(II)V", "getIconRes", "()I", "getTitleRes", "SelectPicture", "SendFile", "SendSticker", "SendVoice", "StartVideoCall", "StartVoiceCall", "TakePhoto", "TakePhotoVideo", "TakeVideo", "Lim/vector/dialogs/DialogListItem$StartVoiceCall;", "Lim/vector/dialogs/DialogListItem$StartVideoCall;", "Lim/vector/dialogs/DialogListItem$SendFile;", "Lim/vector/dialogs/DialogListItem$SendVoice;", "Lim/vector/dialogs/DialogListItem$SendSticker;", "Lim/vector/dialogs/DialogListItem$TakePhoto;", "Lim/vector/dialogs/DialogListItem$TakeVideo;", "Lim/vector/dialogs/DialogListItem$TakePhotoVideo;", "Lim/vector/dialogs/DialogListItem$SelectPicture;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DialogListItem.kt */
public abstract class DialogListItem {
    private final int iconRes;
    private final int titleRes;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$SelectPicture;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class SelectPicture extends DialogListItem {
        public static final SelectPicture INSTANCE = new SelectPicture();

        private SelectPicture() {
            super(R.drawable.tchap_ic_attached_files, R.string.option_select_image, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$SendFile;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class SendFile extends DialogListItem {
        public static final SendFile INSTANCE = new SendFile();

        private SendFile() {
            super(R.drawable.tchap_ic_attached_files, R.string.option_send_files, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$SendSticker;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class SendSticker extends DialogListItem {
        public static final SendSticker INSTANCE = new SendSticker();

        private SendSticker() {
            super(R.drawable.ic_send_sticker, R.string.option_send_sticker, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$SendVoice;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class SendVoice extends DialogListItem {
        public static final SendVoice INSTANCE = new SendVoice();

        private SendVoice() {
            super(R.drawable.vector_micro_green, R.string.option_send_voice, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$StartVideoCall;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class StartVideoCall extends DialogListItem {
        public static final StartVideoCall INSTANCE = new StartVideoCall();

        private StartVideoCall() {
            super(R.drawable.tchap_ic_video, R.string.action_video_call, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$StartVoiceCall;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class StartVoiceCall extends DialogListItem {
        public static final StartVoiceCall INSTANCE = new StartVoiceCall();

        private StartVoiceCall() {
            super(R.drawable.tchap_ic_start_call, R.string.action_voice_call, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$TakePhoto;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class TakePhoto extends DialogListItem {
        public static final TakePhoto INSTANCE = new TakePhoto();

        private TakePhoto() {
            super(R.drawable.tchap_ic_camera, R.string.option_take_photo, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$TakePhotoVideo;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class TakePhotoVideo extends DialogListItem {
        public static final TakePhotoVideo INSTANCE = new TakePhotoVideo();

        private TakePhotoVideo() {
            super(R.drawable.tchap_ic_camera, R.string.option_take_photo_video, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/dialogs/DialogListItem$TakeVideo;", "Lim/vector/dialogs/DialogListItem;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DialogListItem.kt */
    public static final class TakeVideo extends DialogListItem {
        public static final TakeVideo INSTANCE = new TakeVideo();

        private TakeVideo() {
            super(R.drawable.tchap_ic_video, R.string.option_take_video, null);
        }
    }

    private DialogListItem(int i, int i2) {
        this.iconRes = i;
        this.titleRes = i2;
    }

    public /* synthetic */ DialogListItem(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2);
    }

    public final int getIconRes() {
        return this.iconRes;
    }

    public final int getTitleRes() {
        return this.titleRes;
    }
}
