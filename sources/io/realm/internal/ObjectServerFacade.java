package io.realm.internal;

import android.content.Context;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;
import io.realm.exceptions.RealmException;
import java.lang.reflect.InvocationTargetException;

public class ObjectServerFacade {
    private static final ObjectServerFacade nonSyncFacade = new ObjectServerFacade();
    private static ObjectServerFacade syncFacade;

    public void addSupportForObjectLevelPermissions(Builder builder) {
    }

    public void downloadInitialRemoteChanges(RealmConfiguration realmConfiguration) {
    }

    public void downloadInitialSubscriptions(Realm realm) {
    }

    public Object[] getSyncConfigurationOptions(RealmConfiguration realmConfiguration) {
        return new Object[11];
    }

    public String getSyncServerCertificateAssetName(RealmConfiguration realmConfiguration) {
        return null;
    }

    public String getSyncServerCertificateFilePath(RealmConfiguration realmConfiguration) {
        return null;
    }

    public void initialize(Context context, String str) {
    }

    public boolean isPartialRealm(RealmConfiguration realmConfiguration) {
        return false;
    }

    public void realmClosed(RealmConfiguration realmConfiguration) {
    }

    public boolean wasDownloadInterrupted(Throwable th) {
        return false;
    }

    public void wrapObjectStoreSessionIfRequired(OsRealmConfig osRealmConfig) {
    }

    static {
        String str = "Failed to init SyncObjectServerFacade";
        syncFacade = null;
        try {
            syncFacade = (ObjectServerFacade) Class.forName("io.realm.internal.SyncObjectServerFacade").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException unused) {
        } catch (InstantiationException e) {
            throw new RealmException(str, e);
        } catch (IllegalAccessException e2) {
            throw new RealmException(str, e2);
        } catch (NoSuchMethodException e3) {
            throw new RealmException(str, e3);
        } catch (InvocationTargetException e4) {
            throw new RealmException(str, e4.getTargetException());
        }
    }

    public static ObjectServerFacade getFacade(boolean z) {
        if (z) {
            return syncFacade;
        }
        return nonSyncFacade;
    }

    public static ObjectServerFacade getSyncFacadeIfPossible() {
        ObjectServerFacade objectServerFacade = syncFacade;
        if (objectServerFacade != null) {
            return objectServerFacade;
        }
        return nonSyncFacade;
    }
}
