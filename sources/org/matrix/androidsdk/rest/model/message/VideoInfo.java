package org.matrix.androidsdk.rest.model.message;

import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

public class VideoInfo {
    public Long duration;
    public Integer h;
    public String mimetype;
    public Long size;
    public EncryptedFileInfo thumbnail_file;
    public ThumbnailInfo thumbnail_info;
    public String thumbnail_url;
    public Integer w;

    public VideoInfo deepCopy() {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.h = this.h;
        videoInfo.w = this.w;
        videoInfo.mimetype = this.mimetype;
        videoInfo.duration = this.duration;
        videoInfo.thumbnail_url = this.thumbnail_url;
        ThumbnailInfo thumbnailInfo = this.thumbnail_info;
        if (thumbnailInfo != null) {
            videoInfo.thumbnail_info = thumbnailInfo.deepCopy();
        }
        EncryptedFileInfo encryptedFileInfo = this.thumbnail_file;
        if (encryptedFileInfo != null) {
            videoInfo.thumbnail_file = encryptedFileInfo.deepCopy();
        }
        return videoInfo;
    }
}
