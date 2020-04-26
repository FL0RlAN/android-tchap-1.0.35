package io.realm;

import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;

final class io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo extends ColumnInfo {
    long idIndex;
    long maxColumnIndexValue;
    long permissionsIndex;

    io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo(OsSchemaInfo osSchemaInfo) {
        super(2);
        OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(io_realm_sync_permissions_RealmPermissionsRealmProxy$ClassNameHelper.INTERNAL_CLASS_NAME);
        String str = "id";
        this.idIndex = addColumnDetails(str, str, objectSchemaInfo);
        String str2 = "permissions";
        this.permissionsIndex = addColumnDetails(str2, str2, objectSchemaInfo);
        this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
    }

    io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo(ColumnInfo columnInfo, boolean z) {
        super(columnInfo, z);
        copy(columnInfo, this);
    }

    /* access modifiers changed from: protected */
    public final ColumnInfo copy(boolean z) {
        return new io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo(this, z);
    }

    /* access modifiers changed from: protected */
    public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
        io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo = (io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo) columnInfo;
        io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo2 = (io_realm_sync_permissions_RealmPermissionsRealmProxy$RealmPermissionsColumnInfo) columnInfo2;
        io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo2.idIndex = io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo.idIndex;
        io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo2.permissionsIndex = io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo.permissionsIndex;
        io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo2.maxColumnIndexValue = io_realm_sync_permissions_realmpermissionsrealmproxy_realmpermissionscolumninfo.maxColumnIndexValue;
    }
}
