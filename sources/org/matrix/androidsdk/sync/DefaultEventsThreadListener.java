package org.matrix.androidsdk.sync;

import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.sync.SyncResponse;

public class DefaultEventsThreadListener implements EventsThreadListener {
    private final MXDataHandler mDataHandler;

    public DefaultEventsThreadListener(MXDataHandler mXDataHandler) {
        this.mDataHandler = mXDataHandler;
    }

    public void onSyncResponse(SyncResponse syncResponse, String str, boolean z) {
        this.mDataHandler.onSyncResponse(syncResponse, str, z);
    }

    public void onSyncError(MatrixError matrixError) {
        this.mDataHandler.onSyncError(matrixError);
    }

    public void onConfigurationError(String str) {
        this.mDataHandler.onConfigurationError(str);
    }
}
