package im.vector.util;

import java.io.Serializable;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

public class SlidableMediaInfo implements Serializable {
    public EncryptedFileInfo mEncryptedFileInfo;
    public EncryptedFileInfo mEncryptedThumbnailFileInfo;
    public String mFileName;
    public String mMediaUrl;
    public String mMessageType;
    public String mMimeType;
    public int mOrientation = 0;
    public int mRotationAngle = 0;
    public String mThumbnailUrl;
}
