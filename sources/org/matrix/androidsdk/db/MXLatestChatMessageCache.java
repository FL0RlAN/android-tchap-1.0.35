package org.matrix.androidsdk.db;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.core.FileContentUtils;
import org.matrix.androidsdk.core.Log;

public class MXLatestChatMessageCache {
    private static final String FILENAME = "ConsoleLatestChatMessageCache";
    private static final String LOG_TAG = MXLatestChatMessageCache.class.getSimpleName();
    final String MXLATESTMESSAGES_STORE_FOLDER = "MXLatestMessagesStore";
    private File mLatestMessagesDirectory = null;
    private File mLatestMessagesFile = null;
    private Map<String, String> mLatestMesssageByRoomId = null;
    private String mUserId = null;

    public MXLatestChatMessageCache(String str) {
        this.mUserId = str;
    }

    public void clearCache(Context context) {
        FileContentUtils.deleteDirectory(this.mLatestMessagesDirectory);
        this.mLatestMesssageByRoomId = null;
    }

    private void openLatestMessagesDict(Context context) {
        String str = "";
        String str2 = FILENAME;
        if (this.mLatestMesssageByRoomId == null) {
            this.mLatestMesssageByRoomId = new HashMap();
            try {
                this.mLatestMessagesDirectory = new File(context.getApplicationContext().getFilesDir(), "MXLatestMessagesStore");
                this.mLatestMessagesDirectory = new File(this.mLatestMessagesDirectory, this.mUserId);
                File file = this.mLatestMessagesDirectory;
                StringBuilder sb = new StringBuilder();
                sb.append(str2.hashCode());
                sb.append(str);
                this.mLatestMessagesFile = new File(file, sb.toString());
                if (!this.mLatestMessagesDirectory.exists()) {
                    this.mLatestMessagesDirectory.mkdirs();
                    File filesDir = context.getApplicationContext().getFilesDir();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2.hashCode());
                    sb2.append(str);
                    File file2 = new File(filesDir, sb2.toString());
                    if (file2.exists()) {
                        file2.renameTo(this.mLatestMessagesFile);
                    }
                }
                if (this.mLatestMessagesFile.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(this.mLatestMessagesFile);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    this.mLatestMesssageByRoomId = (Map) objectInputStream.readObject();
                    objectInputStream.close();
                    fileInputStream.close();
                }
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## openLatestMessagesDict failed ");
                sb3.append(e.getMessage());
                Log.e(str3, sb3.toString(), e);
            }
        }
    }

    public String getLatestText(Context context, String str) {
        if (this.mLatestMesssageByRoomId == null) {
            openLatestMessagesDict(context);
        }
        String str2 = "";
        return (!TextUtils.isEmpty(str) && this.mLatestMesssageByRoomId.containsKey(str)) ? (String) this.mLatestMesssageByRoomId.get(str) : str2;
    }

    private void saveLatestMessagesDict(Context context) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.mLatestMessagesFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.mLatestMesssageByRoomId);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## saveLatestMessagesDict() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void updateLatestMessage(Context context, String str, String str2) {
        if (this.mLatestMesssageByRoomId == null) {
            openLatestMessagesDict(context);
        }
        if (TextUtils.isEmpty(str2)) {
            this.mLatestMesssageByRoomId.remove(str);
        }
        this.mLatestMesssageByRoomId.put(str, str2);
        saveLatestMessagesDict(context);
    }
}
