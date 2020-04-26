package org.matrix.androidsdk.rest.model.message;

import org.matrix.androidsdk.crypto.MXEncryptedAttachments.EncryptionResult;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

public class ImageMessage extends MediaMessage {
    public EncryptedFileInfo file;
    public ImageInfo info;
    public String url;

    public ImageMessage() {
        this.msgtype = Message.MSGTYPE_IMAGE;
    }

    public ImageMessage deepCopy() {
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.msgtype = this.msgtype;
        imageMessage.body = this.body;
        imageMessage.url = this.url;
        EncryptedFileInfo encryptedFileInfo = this.file;
        if (encryptedFileInfo != null) {
            imageMessage.file = encryptedFileInfo.deepCopy();
        }
        return imageMessage;
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
        ImageInfo imageInfo = this.info;
        if (imageInfo == null) {
            return null;
        }
        if (imageInfo.thumbnail_file != null) {
            return this.info.thumbnail_file.url;
        }
        return this.info.thumbnailUrl;
    }

    public void setThumbnailUrl(EncryptionResult encryptionResult, String str) {
        if (encryptionResult != null) {
            this.info.thumbnail_file = encryptionResult.mEncryptedFileInfo;
            this.info.thumbnail_file.url = str;
            this.info.thumbnailUrl = null;
            return;
        }
        this.info.thumbnailUrl = str;
    }

    public String getMimeType() {
        EncryptedFileInfo encryptedFileInfo = this.file;
        if (encryptedFileInfo != null) {
            return encryptedFileInfo.mimetype;
        }
        ImageInfo imageInfo = this.info;
        if (imageInfo != null) {
            return imageInfo.mimetype;
        }
        return null;
    }

    public int getRotation() {
        ImageInfo imageInfo = this.info;
        if (imageInfo == null || imageInfo.rotation == null) {
            return Integer.MAX_VALUE;
        }
        return this.info.rotation.intValue();
    }

    public int getOrientation() {
        ImageInfo imageInfo = this.info;
        if (imageInfo == null || imageInfo.orientation == null) {
            return 0;
        }
        return this.info.orientation.intValue();
    }
}
