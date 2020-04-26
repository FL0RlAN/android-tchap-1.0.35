package io.realm;

import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;

final class io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo extends ColumnInfo {
    long idIndex;
    long maxColumnIndexValue;
    long roleIndex;

    io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo(OsSchemaInfo osSchemaInfo) {
        super(2);
        OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(io_realm_sync_permissions_PermissionUserRealmProxy$ClassNameHelper.INTERNAL_CLASS_NAME);
        String str = "id";
        this.idIndex = addColumnDetails(str, str, objectSchemaInfo);
        String str2 = "role";
        this.roleIndex = addColumnDetails(str2, str2, objectSchemaInfo);
        addBacklinkDetails(osSchemaInfo, "roles", io_realm_sync_permissions_RoleRealmProxy$ClassNameHelper.INTERNAL_CLASS_NAME, "members");
        this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
    }

    io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo(ColumnInfo columnInfo, boolean z) {
        super(columnInfo, z);
        copy(columnInfo, this);
    }

    /* access modifiers changed from: protected */
    public final ColumnInfo copy(boolean z) {
        return new io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo(this, z);
    }

    /* access modifiers changed from: protected */
    public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
        io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo = (io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo) columnInfo;
        io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo2 = (io_realm_sync_permissions_PermissionUserRealmProxy$PermissionUserColumnInfo) columnInfo2;
        io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo2.idIndex = io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo.idIndex;
        io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo2.roleIndex = io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo.roleIndex;
        io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo2.maxColumnIndexValue = io_realm_sync_permissions_permissionuserrealmproxy_permissionusercolumninfo.maxColumnIndexValue;
    }
}
