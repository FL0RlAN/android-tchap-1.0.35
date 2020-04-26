package org.matrix.androidsdk.rest.model.message;

import org.matrix.androidsdk.crypto.MXEncryptedAttachments.EncryptionResult;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

public class VideoMessage extends MediaMessage {
    public EncryptedFileInfo file;
    public VideoInfo info;
    public String url;

    public VideoMessage() {
        this.msgtype = Message.MSGTYPE_VIDEO;
    }

    public String getUrl() {
        String str = this.url;
        if (str != null) {
            return str;
        }
        EncryptedFileInfo encryptedFileInfo = this.file;
        if (encryptedFileInfo != null) {
            return encryptedFileInfo.url;
        }
        return null;
    }

    public void setUrl(EncryptionResult encryptionResult, String str) {
        if (encryptionResult != null) {
            this.file = encryptionResult.mEncryptedFileInfo;
            this.file.url = str;
            this.url = null;
            return;
        }
        this.url = str;
    }

    public String getThumbnailUrl() {
        VideoInfo videoInfo = this.info;
        if (videoInfo != null && videoInfo.thumbnail_url != null) {
            return this.info.thumbnail_url;
        }
        VideoInfo videoInfo2 = this.info;
        if (videoInfo2 == null || videoInfo2.thumbnail_file == null) {
            return null;
        }
        return this.info.thumbnail_file.url;
    }

    public void setThumbnailUrl(EncryptionResult encryptionResult, String str) {
        if (encryptionResult != null) {
            this.info.thumbnail_file = encryptionResult.mEncryptedFileInfo;
            this.info.thumbnail_file.url = str;
            this.info.thumbnail_url = null;
            return;
        }
        this.info.thumbnail_url = str;
    }

    public VideoMessage deepCopy() {
        VideoMessage videoMessage = new VideoMessage();
        videoMessage.url = this.url;
        videoMessage.msgtype = this.msgtype;
        videoMessage.body = this.body;
        VideoInfo videoInfo = this.info;
        if (videoInfo != null) {
            videoMessage.info = videoInfo.deepCopy();
        }
        EncryptedFileInfo encryptedFileInfo = this.file;
        if (encryptedFileInfo != null) {
            videoMessage.file = encryptedFileInfo.deepCopy();
        }
        return videoMessage;
    }

    public String getMimeType() {
        VideoInfo videoInfo = this.info;
        if (videoInfo != null) {
            return videoInfo.mimetype;
        }
        return null;
    }
}
