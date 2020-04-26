package org.matrix.androidsdk.rest.model.message;

import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

public class ImageInfo {
    public Integer h;
    public String mimetype;
    public Integer orientation;
    public Integer rotation;
    public Long size;
    public ThumbnailInfo thumbnailInfo;
    public String thumbnailUrl;
    public EncryptedFileInfo thumbnail_file;
    public Integer w;

    public ImageInfo deepCopy() {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.mimetype = this.mimetype;
        imageInfo.w = this.w;
        imageInfo.h = this.h;
        imageInfo.size = this.size;
        imageInfo.rotation = this.rotation;
        imageInfo.orientation = this.orientation;
        EncryptedFileInfo encryptedFileInfo = this.thumbnail_file;
        if (encryptedFileInfo != null) {
            imageInfo.thumbnail_file = encryptedFileInfo.deepCopy();
        }
        imageInfo.thumbnailUrl = this.thumbnailUrl;
        ThumbnailInfo thumbnailInfo2 = this.thumbnailInfo;
        if (thumbnailInfo2 != null) {
            imageInfo.thumbnailInfo = thumbnailInfo2.deepCopy();
        }
        return imageInfo;
    }
}
