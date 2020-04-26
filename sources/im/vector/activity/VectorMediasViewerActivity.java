package im.vector.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.viewpager.widget.ViewPager.PageTransformer;
import com.google.gson.JsonElement;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.media.MediaScanManager;
import fr.gouv.tchap.media.MediaScanManager.MediaScanManagerListener;
import fr.gouv.tchap.model.MediaScan;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.adapters.VectorMediasViewerAdapter;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.SlidableMediaInfo;
import java.io.File;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.MXMediaDownloadListener;

public class VectorMediasViewerActivity extends MXCActionBarActivity {
    public static final String EXTRA_MATRIX_ID = "ImageSliderActivity.EXTRA_MATRIX_ID";
    public static final String KEY_INFO_LIST = "ImageSliderActivity.KEY_INFO_LIST";
    public static final String KEY_INFO_LIST_INDEX = "ImageSliderActivity.KEY_INFO_LIST_INDEX";
    public static final String KEY_THUMBNAIL_HEIGHT = "ImageSliderActivity.KEY_THUMBNAIL_HEIGHT";
    public static final String KEY_THUMBNAIL_WIDTH = "ImageSliderActivity.KEY_THUMBNAIL_WIDTH";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorMediasViewerActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public VectorMediasViewerAdapter mAdapter;
    protected MediaScanManager mMediaScanManager;
    /* access modifiers changed from: private */
    public List<SlidableMediaInfo> mMediasList;
    /* access modifiers changed from: private */
    public int mPendingAction;
    /* access modifiers changed from: private */
    public int mPendingPosition;
    private MXSession mSession;
    private ViewPager mViewPager;

    public class DepthPageTransformer implements PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public DepthPageTransformer() {
        }

        public void transformPage(View view, float f) {
            int width = view.getWidth();
            if (f < -1.0f) {
                view.setAlpha(0.0f);
            } else if (f <= 0.0f) {
                view.setAlpha(1.0f);
                view.setTranslationX(0.0f);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
            } else if (f <= 1.0f) {
                view.setAlpha(1.0f - f);
                view.setTranslationX(((float) width) * (-f));
                float abs = ((1.0f - Math.abs(f)) * 0.25f) + MIN_SCALE;
                view.setScaleX(abs);
                view.setScaleY(abs);
            } else {
                view.setAlpha(0.0f);
            }
        }
    }

    public int getLayoutRes() {
        return R.layout.activity_vector_media_viewer;
    }

    public int getMenuRes() {
        return R.menu.vector_medias_viewer;
    }

    public void initUiAndData() {
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.d(LOG_TAG, "onCreate : restart the application");
            CommonActivityUtils.restartApp(this);
        } else if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
        } else {
            String str = null;
            Intent intent = getIntent();
            String str2 = EXTRA_MATRIX_ID;
            if (intent.hasExtra(str2)) {
                str = intent.getStringExtra(str2);
            }
            this.mSession = Matrix.getInstance(getApplicationContext()).getSession(str);
            MXSession mXSession = this.mSession;
            if (mXSession == null || !mXSession.isAlive()) {
                finish();
                Log.d(LOG_TAG, "onCreate : invalid session");
                return;
            }
            this.mMediasList = (List) intent.getSerializableExtra(KEY_INFO_LIST);
            List<SlidableMediaInfo> list = this.mMediasList;
            if (list == null || list.size() == 0) {
                finish();
                return;
            }
            this.mMediaScanManager = new MediaScanManager(this.mSession.getMediaScanRestClient(), this.realm);
            this.mMediaScanManager.setListener(new MediaScanManagerListener() {
                public void onMediaScanChange(MediaScan mediaScan) {
                    if (VectorMediasViewerActivity.this.mAdapter != null) {
                        VectorMediasViewerActivity.this.mAdapter.notifyDataSetChanged();
                    }
                }
            });
            this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
            int min = Math.min(intent.getIntExtra(KEY_INFO_LIST_INDEX, 0), this.mMediasList.size() - 1);
            int intExtra = intent.getIntExtra(KEY_THUMBNAIL_WIDTH, 0);
            int intExtra2 = intent.getIntExtra(KEY_THUMBNAIL_HEIGHT, 0);
            MXSession mXSession2 = this.mSession;
            VectorMediasViewerAdapter vectorMediasViewerAdapter = new VectorMediasViewerAdapter(this, mXSession2, mXSession2.getMediaCache(), this.mMediasList, intExtra, intExtra2);
            this.mAdapter = vectorMediasViewerAdapter;
            this.mAdapter.setMediaScanManager(this.mMediaScanManager);
            this.mViewPager.setAdapter(this.mAdapter);
            this.mViewPager.setPageTransformer(true, new DepthPageTransformer());
            this.mAdapter.autoPlayItemAt(min);
            this.mViewPager.setCurrentItem(min);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle((CharSequence) ((SlidableMediaInfo) this.mMediasList.get(min)).mFileName);
            }
            this.mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
                public void onPageScrollStateChanged(int i) {
                }

                public void onPageScrolled(int i, float f, int i2) {
                }

                public void onPageSelected(int i) {
                    if (VectorMediasViewerActivity.this.getSupportActionBar() != null) {
                        VectorMediasViewerActivity.this.getSupportActionBar().setTitle((CharSequence) ((SlidableMediaInfo) VectorMediasViewerActivity.this.mMediasList.get(i)).mFileName);
                    }
                    VectorMediasViewerActivity.this.supportInvalidateOptionsMenu();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mAdapter.stopPlayingVideo();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean z = false;
        if (CommonActivityUtils.shouldRestartApp(this)) {
            return false;
        }
        MenuItem findItem = menu.findItem(R.id.ic_action_share);
        if (findItem != null) {
            if (((SlidableMediaInfo) this.mMediasList.get(this.mViewPager.getCurrentItem())).mEncryptedFileInfo == null) {
                z = true;
            }
            findItem.setVisible(z);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void onAction(final int i, final int i2) {
        MXMediaCache mediaCache = Matrix.getInstance(this).getMediaCache();
        final SlidableMediaInfo slidableMediaInfo = (SlidableMediaInfo) this.mMediasList.get(i);
        if (slidableMediaInfo != null) {
            MediaScanManager mediaScanManager = this.mMediaScanManager;
            if (mediaScanManager != null && mediaScanManager.isTrustedSlidableMediaInfo(slidableMediaInfo)) {
                if (mediaCache.isMediaCached(slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType)) {
                    String str = slidableMediaInfo.mMediaUrl;
                    String str2 = slidableMediaInfo.mMimeType;
                    EncryptedFileInfo encryptedFileInfo = slidableMediaInfo.mEncryptedFileInfo;
                    final int i3 = i2;
                    final int i4 = i;
                    final MXMediaCache mXMediaCache = mediaCache;
                    AnonymousClass3 r1 = new SimpleApiCallback<File>() {
                        public void onSuccess(File file) {
                            File file2;
                            if (file != null) {
                                if (i3 != R.id.ic_action_download) {
                                    if (slidableMediaInfo.mFileName != null) {
                                        file2 = mXMediaCache.moveToShareFolder(file, slidableMediaInfo.mFileName);
                                    } else {
                                        file2 = mXMediaCache.moveToShareFolder(file, file.getName());
                                    }
                                    Uri uri = null;
                                    try {
                                        uri = FileProvider.getUriForFile(VectorMediasViewerActivity.this, "fr.gouv.tchap.a.fileProvider", file2);
                                    } catch (Exception e) {
                                        String access$400 = VectorMediasViewerActivity.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("onMediaAction Selected file cannot be shared ");
                                        sb.append(e.getMessage());
                                        Log.e(access$400, sb.toString(), e);
                                    }
                                    if (uri != null) {
                                        try {
                                            Intent intent = new Intent();
                                            intent.setFlags(1);
                                            intent.setAction("android.intent.action.SEND");
                                            intent.setType(slidableMediaInfo.mMimeType);
                                            intent.putExtra("android.intent.extra.STREAM", uri);
                                            VectorMediasViewerActivity.this.startActivity(intent);
                                        } catch (Exception e2) {
                                            String access$4002 = VectorMediasViewerActivity.LOG_TAG;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("## onAction : cannot display the media ");
                                            sb2.append(uri);
                                            sb2.append(" mimeType ");
                                            sb2.append(slidableMediaInfo.mMimeType);
                                            Log.e(access$4002, sb2.toString(), e2);
                                            Toast.makeText(VectorMediasViewerActivity.this, e2.getLocalizedMessage(), 0).show();
                                        }
                                    }
                                } else if (VectorMediasViewerActivity.this.checkWritePermission(PermissionsToolsKt.PERMISSION_REQUEST_CODE)) {
                                    CommonActivityUtils.saveMediaIntoDownloads(VectorMediasViewerActivity.this, file, slidableMediaInfo.mFileName, slidableMediaInfo.mMimeType, new SimpleApiCallback<String>() {
                                        public void onSuccess(String str) {
                                            Toast.makeText(VectorApp.getInstance(), VectorMediasViewerActivity.this.getText(R.string.media_slider_saved), 1).show();
                                        }
                                    });
                                } else {
                                    VectorMediasViewerActivity.this.mPendingPosition = i4;
                                    VectorMediasViewerActivity.this.mPendingAction = i3;
                                }
                            }
                        }
                    };
                    mediaCache.createTmpDecryptedMediaFile(str, str2, encryptedFileInfo, r1);
                } else {
                    final String downloadMedia = mediaCache.downloadMedia(this, this.mSession.getHomeServerConfig(), slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo);
                    if (downloadMedia != null) {
                        mediaCache.addDownloadListener(downloadMedia, new MXMediaDownloadListener() {
                            public void onDownloadError(String str, JsonElement jsonElement) {
                                MatrixError matrixError = JsonUtils.toMatrixError(jsonElement);
                                if (matrixError != null && matrixError.isSupportedErrorCode()) {
                                    Toast.makeText(VectorMediasViewerActivity.this, matrixError.getLocalizedMessage(), 1).show();
                                }
                            }

                            public void onDownloadComplete(String str) {
                                if (str.equals(downloadMedia)) {
                                    VectorMediasViewerActivity.this.onAction(i, i2);
                                }
                            }
                        });
                    }
                }
                return;
            }
        }
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onAction : the media is unchecked or untrusted ");
        sb.append(slidableMediaInfo.mMediaUrl);
        Log.e(str3, sb.toString());
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.ic_action_download && itemId != R.id.ic_action_share) {
            return super.onOptionsItemSelected(menuItem);
        }
        onAction(this.mViewPager.getCurrentItem(), menuItem.getItemId());
        return true;
    }

    public boolean checkWritePermission(int i) {
        return PermissionsToolsKt.checkPermissions(2, (Activity) this, i);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (!PermissionsToolsKt.allGranted(iArr)) {
            return;
        }
        if (i == 567) {
            onAction(this.mPendingPosition, this.mPendingAction);
        } else if (i == 600) {
            this.mAdapter.downloadMediaAndExportToDownloads();
        }
    }
}
