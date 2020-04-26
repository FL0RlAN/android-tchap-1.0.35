package io.realm;

import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

final class io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo extends ColumnInfo {
    long maxColumnIndexValue;
    long nameIndex;
    long permissionsIndex;

    io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo(OsSchemaInfo osSchemaInfo) {
        super(2);
        OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassNameHelper.INTERNAL_CLASS_NAME);
        String str = TermsResponse.NAME;
        this.nameIndex = addColumnDetails(str, str, objectSchemaInfo);
        String str2 = "permissions";
        this.permissionsIndex = addColumnDetails(str2, str2, objectSchemaInfo);
        this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
    }

    io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo(ColumnInfo columnInfo, boolean z) {
        super(columnInfo, z);
        copy(columnInfo, this);
    }

    /* access modifiers changed from: protected */
    public final ColumnInfo copy(boolean z) {
        return new io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo(this, z);
    }

    /* access modifiers changed from: protected */
    public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
        io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo = (io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo) columnInfo;
        io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo2 = (io_realm_sync_permissions_ClassPermissionsRealmProxy$ClassPermissionsColumnInfo) columnInfo2;
        io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo2.nameIndex = io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo.nameIndex;
        io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo2.permissionsIndex = io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo.permissionsIndex;
        io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo2.maxColumnIndexValue = io_realm_sync_permissions_classpermissionsrealmproxy_classpermissionscolumninfo.maxColumnIndexValue;
    }
}
