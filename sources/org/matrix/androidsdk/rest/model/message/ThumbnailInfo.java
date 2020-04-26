package org.matrix.androidsdk.rest.model.message;

public class ThumbnailInfo {
    public Integer h;
    public String mimetype;
    public Long size;
    public Integer w;

    public ThumbnailInfo deepCopy() {
        ThumbnailInfo thumbnailInfo = new ThumbnailInfo();
        thumbnailInfo.w = this.w;
        thumbnailInfo.h = this.h;
        thumbnailInfo.size = this.size;
        thumbnailInfo.mimetype = this.mimetype;
        return thumbnailInfo;
    }
}
