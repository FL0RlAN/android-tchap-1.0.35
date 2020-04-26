package im.vector.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import fr.gouv.tchap.a.R;
import im.vector.dialogs.DialogListItem;
import im.vector.dialogs.DialogListItem.SelectPicture;
import im.vector.dialogs.DialogListItem.TakePhoto;
import im.vector.dialogs.DialogSendItemAdapter;
import im.vector.util.PermissionsToolsKt;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;

public class SelectPictureActivity extends AppCompatActivity {
    private static final String CAMERA_VALUE_TITLE = "attachment";
    private static final String LOG_TAG = SelectPictureActivity.class.getSimpleName();
    private static final int REQUEST_FILES_REQUEST_CODE = 2;
    private static final int TAKE_IMAGE_REQUEST_CODE = 1;
    private String mLatestTakePictureCameraUri = "";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        final ArrayList arrayList = new ArrayList();
        arrayList.add(SelectPicture.INSTANCE);
        arrayList.add(TakePhoto.INSTANCE);
        new Builder(this).setAdapter(new DialogSendItemAdapter(this, arrayList), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SelectPictureActivity.this.onSendChoiceClicked((DialogListItem) arrayList.get(i));
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                SelectPictureActivity.this.finish();
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void onSendChoiceClicked(DialogListItem dialogListItem) {
        if (dialogListItem instanceof SelectPicture) {
            launchImageSelectionIntent();
        } else if ((dialogListItem instanceof TakePhoto) && PermissionsToolsKt.checkPermissions(3, (Activity) this, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA)) {
            launchNativeCamera();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 569) {
            boolean z = false;
            for (int i2 = 0; i2 < strArr.length; i2++) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onRequestPermissionsResult(): ");
                sb.append(strArr[i2]);
                sb.append("=");
                sb.append(iArr[i2]);
                Log.d(str, sb.toString());
                if ("android.permission.CAMERA".equals(strArr[i2])) {
                    if (iArr[i2] == 0) {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission granted");
                        z = true;
                    } else {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission not granted");
                    }
                }
                if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(strArr[i2])) {
                    if (iArr[i2] == 0) {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission granted");
                    } else {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission not granted");
                    }
                }
            }
            if (z) {
                launchNativeCamera();
            } else {
                Toast.makeText(this, getString(R.string.missing_permissions_warning), 0).show();
            }
        }
    }

    private void launchImageSelectionIntent() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType(ResourceUtils.MIME_TYPE_IMAGE_ALL);
        startActivityForResult(intent, 2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x007d A[SYNTHETIC, Splitter:B:15:0x007d] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00d4  */
    private void launchNativeCamera() {
        Uri uri;
        Log.d(LOG_TAG, "***** launch native camera *********");
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        ContentValues contentValues = new ContentValues();
        StringBuilder sb = new StringBuilder();
        sb.append(CAMERA_VALUE_TITLE);
        sb.append(simpleDateFormat.format(date));
        contentValues.put("title", sb.toString());
        contentValues.put("mime_type", ResourceUtils.MIME_TYPE_JPEG);
        String str = null;
        try {
            uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri == null) {
                try {
                    Log.e(LOG_TAG, "Cannot use the external storage media to save image");
                } catch (UnsupportedOperationException unused) {
                } catch (Exception e) {
                    e = e;
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unable to insert camera URI into MediaStore.Images.Media.EXTERNAL_CONTENT_URI. ");
                    sb2.append(e);
                    Log.e(str2, sb2.toString());
                    if (uri == null) {
                    }
                    if (uri == null) {
                    }
                    if (uri != null) {
                    }
                    this.mLatestTakePictureCameraUri = str;
                    startActivityForResult(intent, 1);
                }
            }
        } catch (UnsupportedOperationException unused2) {
            uri = null;
            Log.e(LOG_TAG, "Unable to insert camera URI into MediaStore.Images.Media.EXTERNAL_CONTENT_URI - no SD card? Attempting to insert into device storage.");
            if (uri == null) {
            }
            if (uri == null) {
            }
            if (uri != null) {
            }
            this.mLatestTakePictureCameraUri = str;
            startActivityForResult(intent, 1);
        } catch (Exception e2) {
            e = e2;
            uri = null;
            String str22 = LOG_TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("Unable to insert camera URI into MediaStore.Images.Media.EXTERNAL_CONTENT_URI. ");
            sb22.append(e);
            Log.e(str22, sb22.toString());
            if (uri == null) {
            }
            if (uri == null) {
            }
            if (uri != null) {
            }
            this.mLatestTakePictureCameraUri = str;
            startActivityForResult(intent, 1);
        }
        if (uri == null) {
            try {
                uri = getContentResolver().insert(Media.INTERNAL_CONTENT_URI, contentValues);
                if (uri == null) {
                    Log.e(LOG_TAG, "Cannot use the internal storage to save media to save image");
                }
            } catch (Exception e3) {
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Unable to insert camera URI into internal storage. Giving up. ");
                sb3.append(e3);
                Log.e(str3, sb3.toString());
            }
        }
        if (uri == null) {
            intent.putExtra("output", uri);
            String str4 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("trying to take a photo on ");
            sb4.append(uri.toString());
            Log.d(str4, sb4.toString());
        } else {
            Log.d(LOG_TAG, "trying to take a photo with no predefined uri");
        }
        if (uri != null) {
            str = uri.toString();
        }
        this.mLatestTakePictureCameraUri = str;
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        Log.d(LOG_TAG, "****** on activity result ");
        if (i2 != -1) {
            finish();
        } else if (i == 1 || i == 2) {
            Intent intent2 = new Intent();
            if (intent == null || intent.getData() == null) {
                String str = this.mLatestTakePictureCameraUri;
                if (str != null) {
                    intent2.setData(Uri.parse(str));
                }
            } else {
                intent2.setData(intent.getData());
            }
            if (VERSION.SDK_INT >= 18 && intent != null) {
                intent2.setClipData(intent.getClipData());
            }
            setResult(-1, intent2);
            finish();
        }
    }
}
