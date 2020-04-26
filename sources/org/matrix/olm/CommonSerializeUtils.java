package org.matrix.olm;

import android.util.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

abstract class CommonSerializeUtils {
    private static final String LOG_TAG = "CommonSerializeUtils";

    /* access modifiers changed from: protected */
    public abstract void deserialize(byte[] bArr, byte[] bArr2) throws Exception;

    /* access modifiers changed from: protected */
    public abstract byte[] serialize(byte[] bArr, StringBuffer stringBuffer);

    CommonSerializeUtils() {
    }

    /* access modifiers changed from: protected */
    public void serialize(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        byte[] randomKey = OlmUtility.getRandomKey();
        StringBuffer stringBuffer = new StringBuffer();
        byte[] serialize = serialize(randomKey, stringBuffer);
        if (serialize != null) {
            String str = "UTF-8";
            objectOutputStream.writeObject(new String(randomKey, str));
            objectOutputStream.writeObject(new String(serialize, str));
            return;
        }
        throw new OlmException(100, String.valueOf(stringBuffer));
    }

    /* access modifiers changed from: protected */
    public void deserialize(ObjectInputStream objectInputStream) throws Exception {
        String str = "UTF-8";
        objectInputStream.defaultReadObject();
        String str2 = (String) objectInputStream.readObject();
        String str3 = (String) objectInputStream.readObject();
        try {
            deserialize(str3.getBytes(str), str2.getBytes(str));
            Log.d(LOG_TAG, "## deserializeObject(): success");
        } catch (Exception e) {
            throw new OlmException(101, e.getMessage());
        }
    }
}
