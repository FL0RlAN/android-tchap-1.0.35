package im.vector.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import fr.gouv.tchap.a.R;
import im.vector.contacts.ContactsManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0003\u001a\u000e\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u001f\u001a \u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020#2\b\b\u0002\u0010$\u001a\u00020\u0005\u001a,\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00052\b\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010$\u001a\u00020\u0005H\u0002\u001a \u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00052\u0006\u0010%\u001a\u00020&2\b\b\u0002\u0010$\u001a\u00020\u0005\u001a\u0016\u0010'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u0001\u001a\u000e\u0010+\u001a\u00020,2\u0006\u0010(\u001a\u00020)\u001a\u0016\u0010-\u001a\u00020\u00032\u0006\u0010(\u001a\u00020)2\u0006\u0010\u001e\u001a\u00020\u001f\u001a\u0016\u0010.\u001a\u00020\u00032\u0006\u0010(\u001a\u00020)2\u0006\u0010\u001e\u001a\u00020\u001f\u001a4\u0010/\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020#2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u0001012\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u0001012\u0006\u00103\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000e\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000f\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0010\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0011\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0012\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0013\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0014\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0015\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0016\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0017\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0018\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0019\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u001a\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u001b\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u001c\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000¨\u00064"}, d2 = {"LOG_TAG", "", "PERMISSIONS_DENIED", "", "PERMISSIONS_EMPTY", "", "PERMISSIONS_FOR_AUDIO_IP_CALL", "PERMISSIONS_FOR_MEMBERS_SEARCH", "PERMISSIONS_FOR_MEMBER_DETAILS", "PERMISSIONS_FOR_ROOM_AVATAR", "PERMISSIONS_FOR_TAKING_PHOTO", "PERMISSIONS_FOR_VIDEO_IP_CALL", "PERMISSIONS_FOR_VIDEO_RECORDING", "PERMISSIONS_FOR_WRITING_FILES", "PERMISSIONS_GRANTED", "PERMISSION_BYPASSED", "PERMISSION_CAMERA", "PERMISSION_READ_CONTACTS", "PERMISSION_RECORD_AUDIO", "PERMISSION_REQUEST_CODE", "PERMISSION_REQUEST_CODE_AUDIO_CALL", "PERMISSION_REQUEST_CODE_CHANGE_AVATAR", "PERMISSION_REQUEST_CODE_EXPORT_KEYS", "PERMISSION_REQUEST_CODE_LAUNCH_CAMERA", "PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA", "PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA", "PERMISSION_REQUEST_CODE_VIDEO_CALL", "PERMISSION_REQUEST_OTHER", "PERMISSION_WRITE_EXTERNAL_STORAGE", "allGranted", "grantResults", "", "checkPermissions", "permissionsToBeGrantedBitMap", "activity", "Landroid/app/Activity;", "requestCode", "fragment", "Landroidx/fragment/app/Fragment;", "hasToAskForPermission", "context", "Landroid/content/Context;", "searchedPermission", "logPermissionStatuses", "", "onPermissionResultAudioIpCall", "onPermissionResultVideoIpCall", "updatePermissionsToBeGranted", "permissionAlreadyDeniedList_out", "", "permissionsListToBeGranted_out", "permissionType", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: PermissionsTools.kt */
public final class PermissionsToolsKt {
    private static final String LOG_TAG = "PermissionUtils";
    private static final boolean PERMISSIONS_DENIED = false;
    private static final int PERMISSIONS_EMPTY = 0;
    public static final int PERMISSIONS_FOR_AUDIO_IP_CALL = 4;
    public static final int PERMISSIONS_FOR_MEMBERS_SEARCH = 8;
    public static final int PERMISSIONS_FOR_MEMBER_DETAILS = 8;
    public static final int PERMISSIONS_FOR_ROOM_AVATAR = 1;
    public static final int PERMISSIONS_FOR_TAKING_PHOTO = 3;
    public static final int PERMISSIONS_FOR_VIDEO_IP_CALL = 5;
    public static final int PERMISSIONS_FOR_VIDEO_RECORDING = 5;
    public static final int PERMISSIONS_FOR_WRITING_FILES = 2;
    private static final boolean PERMISSIONS_GRANTED = true;
    private static final int PERMISSION_BYPASSED = 0;
    public static final int PERMISSION_CAMERA = 1;
    private static final int PERMISSION_READ_CONTACTS = 8;
    private static final int PERMISSION_RECORD_AUDIO = 4;
    public static final int PERMISSION_REQUEST_CODE = 567;
    public static final int PERMISSION_REQUEST_CODE_AUDIO_CALL = 571;
    public static final int PERMISSION_REQUEST_CODE_CHANGE_AVATAR = 574;
    public static final int PERMISSION_REQUEST_CODE_EXPORT_KEYS = 573;
    public static final int PERMISSION_REQUEST_CODE_LAUNCH_CAMERA = 568;
    public static final int PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA = 569;
    public static final int PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA = 570;
    public static final int PERMISSION_REQUEST_CODE_VIDEO_CALL = 572;
    public static final int PERMISSION_REQUEST_OTHER = 600;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;

    public static final void logPermissionStatuses(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (VERSION.SDK_INT >= 23) {
            List<String> asList = Arrays.asList(new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_CONTACTS"});
            String str = LOG_TAG;
            Log.d(str, "## logPermissionStatuses() : log the permissions status used by the app");
            for (String str2 : asList) {
                StringBuilder sb = new StringBuilder();
                sb.append("Status of [");
                sb.append(str2);
                sb.append("] : ");
                sb.append(ContextCompat.checkSelfPermission(context, str2) == 0 ? "PERMISSION_GRANTED" : "PERMISSION_DENIED");
                Log.d(str, sb.toString());
            }
        }
    }

    public static /* synthetic */ boolean checkPermissions$default(int i, Activity activity, int i2, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            i2 = PERMISSION_REQUEST_CODE;
        }
        return checkPermissions(i, activity, i2);
    }

    public static final boolean checkPermissions(int i, Activity activity, int i2) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        return checkPermissions(i, activity, null, i2);
    }

    public static /* synthetic */ boolean checkPermissions$default(int i, Fragment fragment, int i2, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            i2 = PERMISSION_REQUEST_CODE;
        }
        return checkPermissions(i, fragment, i2);
    }

    public static final boolean checkPermissions(int i, Fragment fragment, int i2) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        return checkPermissions(i, fragment.getActivity(), fragment, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01ae, code lost:
        r13 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01b1, code lost:
        org.matrix.androidsdk.core.Log.d(r4, "## checkPermissions(): already denied permission not supported");
     */
    private static final boolean checkPermissions(int i, Activity activity, Fragment fragment, int i2) {
        String str;
        int i3 = i;
        Activity activity2 = activity;
        Fragment fragment2 = fragment;
        int i4 = i2;
        String str2 = LOG_TAG;
        if (activity2 == null) {
            Log.w(str2, "## checkPermissions(): invalid input data");
            return false;
        }
        if (i3 != 0) {
            if (4 == i3 || 5 == i3 || 3 == i3 || 8 == i3 || 8 == i3 || 1 == i3 || 5 == i3 || 2 == i3) {
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList arrayList2 = new ArrayList();
                String str3 = "";
                String str4 = "android.permission.CAMERA";
                boolean updatePermissionsToBeGranted = (1 != (i3 & 1) || !hasToAskForPermission(activity2, str4)) ? false : updatePermissionsToBeGranted(activity2, arrayList, arrayList2, str4) | false;
                String str5 = "android.permission.RECORD_AUDIO";
                if (4 == (i3 & 4)) {
                    updatePermissionsToBeGranted |= updatePermissionsToBeGranted(activity2, arrayList, arrayList2, str5);
                }
                String str6 = "android.permission.WRITE_EXTERNAL_STORAGE";
                if (2 == (i3 & 2)) {
                    updatePermissionsToBeGranted |= updatePermissionsToBeGranted(activity2, arrayList, arrayList2, str6);
                }
                String str7 = "android.permission.READ_CONTACTS";
                if (8 == (i3 & 8)) {
                    if (VERSION.SDK_INT >= 23) {
                        updatePermissionsToBeGranted |= updatePermissionsToBeGranted(activity2, arrayList, arrayList2, str7);
                    } else {
                        ContactsManager instance = ContactsManager.getInstance();
                        Intrinsics.checkExpressionValueIsNotNull(instance, "ContactsManager.getInstance()");
                        if (!instance.isContactBookAccessRequested()) {
                            arrayList2.add(str7);
                            updatePermissionsToBeGranted = true;
                        }
                    }
                }
                if (!arrayList.isEmpty()) {
                    if (i3 != 5 && i3 != 4) {
                        for (String str8 : arrayList) {
                            String str9 = "\n\n";
                            switch (str8.hashCode()) {
                                case 463403621:
                                    if (str8.equals(str4)) {
                                        if (!TextUtils.isEmpty(str3)) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(str3);
                                            sb.append(str9);
                                            str3 = sb.toString();
                                        }
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append(str3);
                                        sb2.append(activity2.getString(R.string.permissions_rationale_msg_camera));
                                        str = sb2.toString();
                                        break;
                                    }
                                    break;
                                case 1365911975:
                                    if (str8.equals(str6)) {
                                        if (!TextUtils.isEmpty(str3)) {
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append(str3);
                                            sb3.append(str9);
                                            str3 = sb3.toString();
                                        }
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str3);
                                        sb4.append(activity2.getString(R.string.permissions_rationale_msg_storage));
                                        str = sb4.toString();
                                        break;
                                    }
                                    break;
                                case 1831139720:
                                    if (str8.equals(str5)) {
                                        if (!TextUtils.isEmpty(str3)) {
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append(str3);
                                            sb5.append(str9);
                                            str3 = sb5.toString();
                                        }
                                        StringBuilder sb6 = new StringBuilder();
                                        sb6.append(str3);
                                        sb6.append(activity2.getString(R.string.permissions_rationale_msg_record_audio));
                                        str = sb6.toString();
                                        break;
                                    }
                                    break;
                                case 1977429404:
                                    if (str8.equals(str7)) {
                                        if (!TextUtils.isEmpty(str3)) {
                                            StringBuilder sb7 = new StringBuilder();
                                            sb7.append(str3);
                                            sb7.append(str9);
                                            str3 = sb7.toString();
                                        }
                                        StringBuilder sb8 = new StringBuilder();
                                        sb8.append(str3);
                                        sb8.append(activity2.getString(R.string.permissions_rationale_msg_contacts));
                                        str = sb8.toString();
                                        break;
                                    }
                                    break;
                            }
                        }
                    } else if (arrayList.contains(str4) && arrayList.contains(str5)) {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append(str3);
                        sb9.append(activity2.getString(R.string.permissions_rationale_msg_camera_and_audio));
                        str3 = sb9.toString();
                    } else if (arrayList.contains(str5)) {
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(str3);
                        sb10.append(activity2.getString(R.string.permissions_rationale_msg_record_audio));
                        String sb11 = sb10.toString();
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append(sb11);
                        sb12.append(activity2.getString(R.string.permissions_rationale_msg_record_audio_explanation));
                        str3 = sb12.toString();
                    } else if (arrayList.contains(str4)) {
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append(str3);
                        sb13.append(activity2.getString(R.string.permissions_rationale_msg_camera));
                        String sb14 = sb13.toString();
                        StringBuilder sb15 = new StringBuilder();
                        sb15.append(sb14);
                        sb15.append(activity2.getString(R.string.permissions_rationale_msg_camera_explanation));
                        str3 = sb15.toString();
                    }
                    new Builder(activity2).setTitle((int) R.string.permissions_rationale_popup_title).setMessage((CharSequence) str3).setOnCancelListener(new PermissionsToolsKt$checkPermissions$2(activity2)).setPositiveButton((int) R.string.ok, (OnClickListener) new PermissionsToolsKt$checkPermissions$3(arrayList2, fragment2, i4, activity2)).show();
                } else if (updatePermissionsToBeGranted) {
                    Object[] array = arrayList2.toArray(new String[0]);
                    if (array != null) {
                        String[] strArr = (String[]) array;
                        if (arrayList2.contains(str7) && VERSION.SDK_INT < 23) {
                            new Builder(activity2).setIcon(17301659).setTitle((int) R.string.permissions_rationale_popup_title).setMessage((int) R.string.permissions_msg_contacts_warning_other_androids).setPositiveButton((int) R.string.yes, (OnClickListener) new PermissionsToolsKt$checkPermissions$4(fragment2, strArr, i4, activity2)).setNegativeButton((int) R.string.no, (OnClickListener) new PermissionsToolsKt$checkPermissions$5(fragment2, strArr, i4, activity2)).show();
                        } else if (fragment2 != null) {
                            fragment2.requestPermissions(strArr, i4);
                        } else {
                            ActivityCompat.requestPermissions(activity2, strArr, i4);
                        }
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                    }
                }
                return false;
            }
            Log.w(str2, "## checkPermissions(): permissions to be granted are not supported");
            return false;
        }
        return true;
    }

    public static final boolean hasToAskForPermission(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "searchedPermission");
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null && strArr.length > 0) {
                for (String areEqual : strArr) {
                    if (Intrinsics.areEqual((Object) str, (Object) areEqual)) {
                        return true;
                    }
                }
            }
        } catch (NameNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString(), e);
        }
        return false;
    }

    private static final boolean updatePermissionsToBeGranted(Activity activity, List<String> list, List<String> list2, String str) {
        list2.add(str);
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), str) == 0) {
            return false;
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, str)) {
            return true;
        }
        list.add(str);
        return true;
    }

    public static final boolean onPermissionResultAudioIpCall(Context context, int[] iArr) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        boolean allGranted = allGranted(iArr);
        if (!allGranted) {
            Toast.makeText(context, R.string.permissions_action_not_performed_missing_permissions, 0).show();
        }
        return allGranted;
    }

    public static final boolean onPermissionResultVideoIpCall(Context context, int[] iArr) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        boolean allGranted = allGranted(iArr);
        if (!allGranted) {
            Toast.makeText(context, R.string.permissions_action_not_performed_missing_permissions, 0).show();
        }
        return allGranted;
    }

    public static final boolean allGranted(int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        if (iArr.length == 0) {
            return false;
        }
        int length = iArr.length;
        boolean z = true;
        for (int i = 0; i < length; i++) {
            z = z && iArr[i] == 0;
        }
        return z;
    }
}
