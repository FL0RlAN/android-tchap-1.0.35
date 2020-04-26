package im.vector.fragments;

import android.text.TextUtils;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.fragments.MatrixMessagesFragment;

public class VectorMessagesFragment extends MatrixMessagesFragment {
    private static final String LOG_TAG = VectorMessagesFragment.class.getSimpleName();

    public static VectorMessagesFragment newInstance(String str) {
        VectorMessagesFragment vectorMessagesFragment = new VectorMessagesFragment();
        vectorMessagesFragment.setArguments(getArgument(str));
        return vectorMessagesFragment;
    }

    /* access modifiers changed from: protected */
    public void displayInitializeTimelineError(Object obj) {
        String str;
        if (obj instanceof MatrixError) {
            MatrixError matrixError = (MatrixError) obj;
            str = TextUtils.equals(matrixError.errcode, MatrixError.NOT_FOUND) ? getContext().getString(R.string.failed_to_load_timeline_position, new Object[]{Matrix.getApplicationName()}) : matrixError.getLocalizedMessage();
        } else {
            str = obj instanceof Exception ? ((Exception) obj).getLocalizedMessage() : "";
        }
        if (!TextUtils.isEmpty(str)) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("displayInitializeTimelineError : ");
            sb.append(str);
            Log.d(str2, sb.toString());
            Toast.makeText(getContext(), str, 0).show();
        }
    }

    public void renewHistory() {
        requestInitialHistory();
    }
}
