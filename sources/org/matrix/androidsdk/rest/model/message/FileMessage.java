package org.matrix.androidsdk.rest.model.message;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.MXEncryptedAttachments.EncryptionResult;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

public class FileMessage extends MediaMessage {
    private static final String LOG_TAG = FileMessage.class.getSimpleName();
    public EncryptedFileInfo file;
    public FileInfo info;
    public String url;

    public FileMessage() {
        this.msgtype = Message.MSGTYPE_FILE;
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

    public FileMessage deepCopy() {
        FileMessage fileMessage = new FileMessage();
        fileMessage.msgtype = this.msgtype;
        fileMessage.body = this.body;
        fileMessage.url = this.url;
        FileInfo fileInfo = this.info;
        if (fileInfo != null) {
            fileMessage.info = fileInfo.deepCopy();
        }
        EncryptedFileInfo encryptedFileInfo = this.file;
        if (encryptedFileInfo != null) {
            fileMessage.file = encryptedFileInfo.deepCopy();
        }
        return fileMessage;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0016, code lost:
        if ("text/uri-list".equals(r4.info.mimetype) != false) goto L_0x0018;
     */
    public String getMimeType() {
        FileInfo fileInfo = this.info;
        if (fileInfo == null) {
            return null;
        }
        if (!TextUtils.isEmpty(fileInfo.mimetype)) {
        }
        if (this.body.indexOf(46) > 0) {
            String substring = this.body.substring(this.body.lastIndexOf(46) + 1, this.body.length());
            try {
                this.info.mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(substring.toLowerCase());
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getMimeType() : getMimeTypeFromExtensionfailed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        if (TextUtils.isEmpty(this.info.mimetype)) {
            this.info.mimetype = "application/octet-stream";
        }
        return this.info.mimetype;
    }
}
