package org.matrix.androidsdk.crypto;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.algorithms.IMXDecrypting;
import org.matrix.androidsdk.crypto.algorithms.IMXEncrypting;

public class MXCryptoAlgorithms {
    private static final String LOG_TAG = MXCryptoAlgorithms.class.getSimpleName();
    private static MXCryptoAlgorithms mSharedInstance = null;
    private final Map<String, Class<IMXDecrypting>> mDecryptors;
    private final Map<String, Class<IMXEncrypting>> mEncryptors = new HashMap();

    public static MXCryptoAlgorithms sharedAlgorithms() {
        if (mSharedInstance == null) {
            mSharedInstance = new MXCryptoAlgorithms();
        }
        return mSharedInstance;
    }

    private MXCryptoAlgorithms() {
        String str = "## MXCryptoAlgorithms() : fails to add MXCRYPTO_ALGORITHM_OLM ";
        String str2 = CryptoConstantsKt.MXCRYPTO_ALGORITHM_OLM;
        String str3 = "## MXCryptoAlgorithms() : fails to add MXCRYPTO_ALGORITHM_MEGOLM ";
        String str4 = CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM;
        try {
            this.mEncryptors.put(str4, Class.forName("org.matrix.androidsdk.crypto.algorithms.megolm.MXMegolmEncryption"));
        } catch (Exception e) {
            String str5 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str3);
            sb.append(e.getMessage());
            Log.e(str5, sb.toString(), e);
        }
        try {
            this.mEncryptors.put(str2, Class.forName("org.matrix.androidsdk.crypto.algorithms.olm.MXOlmEncryption"));
        } catch (Exception e2) {
            String str6 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(e2.getMessage());
            Log.e(str6, sb2.toString(), e2);
        }
        this.mDecryptors = new HashMap();
        try {
            this.mDecryptors.put(str4, Class.forName("org.matrix.androidsdk.crypto.algorithms.megolm.MXMegolmDecryption"));
        } catch (Exception e3) {
            String str7 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(e3.getMessage());
            Log.e(str7, sb3.toString(), e3);
        }
        try {
            this.mDecryptors.put(str2, Class.forName("org.matrix.androidsdk.crypto.algorithms.olm.MXOlmDecryption"));
        } catch (Exception e4) {
            String str8 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(e4.getMessage());
            Log.e(str8, sb4.toString(), e4);
        }
    }

    public Class<IMXEncrypting> encryptorClassForAlgorithm(String str) {
        if (!TextUtils.isEmpty(str)) {
            return (Class) this.mEncryptors.get(str);
        }
        return null;
    }

    public Class<IMXDecrypting> decryptorClassForAlgorithm(String str) {
        if (!TextUtils.isEmpty(str)) {
            return (Class) this.mDecryptors.get(str);
        }
        return null;
    }

    public List<String> supportedAlgorithms() {
        return new ArrayList(this.mEncryptors.keySet());
    }
}
