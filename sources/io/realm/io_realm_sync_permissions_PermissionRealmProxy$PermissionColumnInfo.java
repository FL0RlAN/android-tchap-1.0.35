package io.realm;

import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;

final class io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo extends ColumnInfo {
    long canCreateIndex;
    long canDeleteIndex;
    long canModifySchemaIndex;
    long canQueryIndex;
    long canReadIndex;
    long canSetPermissionsIndex;
    long canUpdateIndex;
    long maxColumnIndexValue;
    long roleIndex;

    io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo(OsSchemaInfo osSchemaInfo) {
        super(8);
        OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(io_realm_sync_permissions_PermissionRealmProxy$ClassNameHelper.INTERNAL_CLASS_NAME);
        String str = "role";
        this.roleIndex = addColumnDetails(str, str, objectSchemaInfo);
        String str2 = "canRead";
        this.canReadIndex = addColumnDetails(str2, str2, objectSchemaInfo);
        String str3 = "canUpdate";
        this.canUpdateIndex = addColumnDetails(str3, str3, objectSchemaInfo);
        String str4 = "canDelete";
        this.canDeleteIndex = addColumnDetails(str4, str4, objectSchemaInfo);
        String str5 = "canSetPermissions";
        this.canSetPermissionsIndex = addColumnDetails(str5, str5, objectSchemaInfo);
        String str6 = "canQuery";
        this.canQueryIndex = addColumnDetails(str6, str6, objectSchemaInfo);
        String str7 = "canCreate";
        this.canCreateIndex = addColumnDetails(str7, str7, objectSchemaInfo);
        String str8 = "canModifySchema";
        this.canModifySchemaIndex = addColumnDetails(str8, str8, objectSchemaInfo);
        this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
    }

    io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo(ColumnInfo columnInfo, boolean z) {
        super(columnInfo, z);
        copy(columnInfo, this);
    }

    /* access modifiers changed from: protected */
    public final ColumnInfo copy(boolean z) {
        return new io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo(this, z);
    }

    /* access modifiers changed from: protected */
    public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
        io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo = (io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo) columnInfo;
        io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2 = (io_realm_sync_permissions_PermissionRealmProxy$PermissionColumnInfo) columnInfo2;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.roleIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.roleIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.canReadIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.canReadIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.canUpdateIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.canUpdateIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.canDeleteIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.canDeleteIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.canSetPermissionsIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.canSetPermissionsIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.canQueryIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.canQueryIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.canCreateIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.canCreateIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.canModifySchemaIndex = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.canModifySchemaIndex;
        io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo2.maxColumnIndexValue = io_realm_sync_permissions_permissionrealmproxy_permissioncolumninfo.maxColumnIndexValue;
    }
}
