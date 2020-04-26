package org.matrix.androidsdk.data.store;

public interface IMXStoreListener {
    void onReadReceiptsLoaded(String str);

    void onStoreCorrupted(String str, String str2);

    void onStoreOOM(String str, String str2);

    void onStoreReady(String str);

    void postProcess(String str);
}
