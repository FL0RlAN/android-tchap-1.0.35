package org.matrix.androidsdk.sync;

import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.sync.SyncResponse;

public interface EventsThreadListener {
    void onConfigurationError(String str);

    void onSyncError(MatrixError matrixError);

    void onSyncResponse(SyncResponse syncResponse, String str, boolean z);
}
