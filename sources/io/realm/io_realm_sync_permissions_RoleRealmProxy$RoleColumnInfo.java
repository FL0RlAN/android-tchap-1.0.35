package io.realm;

import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

final class io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo extends ColumnInfo {
    long maxColumnIndexValue;
    long membersIndex;
    long nameIndex;

    io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo(OsSchemaInfo osSchemaInfo) {
        super(2);
        OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(io_realm_sync_permissions_RoleRealmProxy$ClassNameHelper.INTERNAL_CLASS_NAME);
        String str = TermsResponse.NAME;
        this.nameIndex = addColumnDetails(str, str, objectSchemaInfo);
        String str2 = "members";
        this.membersIndex = addColumnDetails(str2, str2, objectSchemaInfo);
        this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
    }

    io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo(ColumnInfo columnInfo, boolean z) {
        super(columnInfo, z);
        copy(columnInfo, this);
    }

    /* access modifiers changed from: protected */
    public final ColumnInfo copy(boolean z) {
        return new io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo(this, z);
    }

    /* access modifiers changed from: protected */
    public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
        io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo io_realm_sync_permissions_rolerealmproxy_rolecolumninfo = (io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo) columnInfo;
        io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo io_realm_sync_permissions_rolerealmproxy_rolecolumninfo2 = (io_realm_sync_permissions_RoleRealmProxy$RoleColumnInfo) columnInfo2;
        io_realm_sync_permissions_rolerealmproxy_rolecolumninfo2.nameIndex = io_realm_sync_permissions_rolerealmproxy_rolecolumninfo.nameIndex;
        io_realm_sync_permissions_rolerealmproxy_rolecolumninfo2.membersIndex = io_realm_sync_permissions_rolerealmproxy_rolecolumninfo.membersIndex;
        io_realm_sync_permissions_rolerealmproxy_rolecolumninfo2.maxColumnIndexValue = io_realm_sync_permissions_rolerealmproxy_rolecolumninfo.maxColumnIndexValue;
    }
}
