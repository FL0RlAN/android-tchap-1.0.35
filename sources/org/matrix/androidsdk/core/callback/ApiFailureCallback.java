package org.matrix.androidsdk.core.callback;

import org.matrix.androidsdk.core.model.MatrixError;

public interface ApiFailureCallback extends ErrorCallback {
    void onMatrixError(MatrixError matrixError);

    void onNetworkError(Exception exc);
}
