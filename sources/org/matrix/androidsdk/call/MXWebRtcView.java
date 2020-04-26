package org.matrix.androidsdk.call;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import com.oney.WebRTCModule.EglUtils;
import java.lang.reflect.Method;
import java.util.List;
import org.webrtc.EglBase;
import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.RendererCommon.RendererEvents;
import org.webrtc.RendererCommon.ScalingType;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

public class MXWebRtcView extends ViewGroup {
    private static final ScalingType DEFAULT_SCALING_TYPE = ScalingType.SCALE_ASPECT_FIT;
    private static final Method IS_IN_LAYOUT;
    private static final String LOG_TAG;
    private int frameHeight;
    private int frameRotation;
    private int frameWidth;
    private final Object layoutSyncRoot = new Object();
    private boolean mirror;
    private final RendererEvents rendererEvents = new RendererEvents() {
        public void onFirstFrameRendered() {
        }

        public void onFrameResolutionChanged(int i, int i2, int i3) {
            MXWebRtcView.this.onFrameResolutionChanged(i, i2, i3);
        }
    };
    private final Runnable requestSurfaceViewRendererLayoutRunnable = new Runnable() {
        public void run() {
            MXWebRtcView.this.requestSurfaceViewRendererLayout();
        }
    };
    private ScalingType scalingType;
    private final SurfaceViewRenderer surfaceViewRenderer;
    private VideoTrack videoTrack;

    /* renamed from: org.matrix.androidsdk.call.MXWebRtcView$3 reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$org$webrtc$RendererCommon$ScalingType = new int[ScalingType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            $SwitchMap$org$webrtc$RendererCommon$ScalingType[ScalingType.SCALE_ASPECT_FILL.ordinal()] = 1;
            $SwitchMap$org$webrtc$RendererCommon$ScalingType[ScalingType.SCALE_ASPECT_FIT.ordinal()] = 2;
        }
    }

    static {
        Class<MXWebRtcView> cls = MXWebRtcView.class;
        LOG_TAG = cls.getSimpleName();
        Method method = null;
        try {
            Method method2 = cls.getMethod("isInLayout", new Class[0]);
            if (Boolean.TYPE.isAssignableFrom(method2.getReturnType())) {
                method = method2;
            }
        } catch (NoSuchMethodException unused) {
        }
        IS_IN_LAYOUT = method;
    }

    public MXWebRtcView(Context context) {
        super(context);
        this.surfaceViewRenderer = new SurfaceViewRenderer(context);
        addView(this.surfaceViewRenderer);
        setMirror(false);
        setScalingType(DEFAULT_SCALING_TYPE);
    }

    private final SurfaceViewRenderer getSurfaceViewRenderer() {
        return this.surfaceViewRenderer;
    }

    private boolean invokeIsInLayout() {
        Method method = IS_IN_LAYOUT;
        if (method == null) {
            return false;
        }
        try {
            return ((Boolean) method.invoke(this, new Object[0])).booleanValue();
        } catch (Throwable unused) {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        try {
            tryAddRendererToVideoTrack();
        } catch (Exception e) {
            Log.e(LOG_TAG, "onAttachedToWindow", e);
        } catch (Throwable th) {
            super.onAttachedToWindow();
            throw th;
        }
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        try {
            removeRendererFromVideoTrack();
        } catch (Exception e) {
            Log.e(LOG_TAG, "onAttachedToWindow", e);
        } catch (Throwable th) {
            super.onDetachedFromWindow();
            throw th;
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: private */
    public void onFrameResolutionChanged(int i, int i2, int i3) {
        boolean z;
        synchronized (this.layoutSyncRoot) {
            if (this.frameHeight != i2) {
                this.frameHeight = i2;
                z = true;
            } else {
                z = false;
            }
            if (this.frameRotation != i3) {
                this.frameRotation = i3;
                z = true;
            }
            if (this.frameWidth != i) {
                this.frameWidth = i;
                z = true;
            }
        }
        if (z) {
            post(this.requestSurfaceViewRendererLayoutRunnable);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        ScalingType scalingType2;
        float f;
        float f2;
        int i9 = i4 - i2;
        int i10 = i3 - i;
        int i11 = 0;
        if (!(i9 == 0 || i10 == 0)) {
            synchronized (this.layoutSyncRoot) {
                i6 = this.frameHeight;
                i7 = this.frameRotation;
                i8 = this.frameWidth;
                scalingType2 = this.scalingType;
            }
            getSurfaceViewRenderer();
            if (AnonymousClass3.$SwitchMap$org$webrtc$RendererCommon$ScalingType[scalingType2.ordinal()] == 1) {
                i5 = 0;
            } else if (!(i6 == 0 || i8 == 0)) {
                if (i7 % 180 == 0) {
                    f2 = (float) i8;
                    f = (float) i6;
                } else {
                    f2 = (float) i6;
                    f = (float) i8;
                }
                Point displaySize = RendererCommon.getDisplaySize(scalingType2, f2 / f, i10, i9);
                int i12 = (i10 - displaySize.x) / 2;
                i5 = (i9 - displaySize.y) / 2;
                i10 = displaySize.x + i12;
                i9 = i5 + displaySize.y;
                i11 = i12;
            }
            this.surfaceViewRenderer.layout(i11, i5, i10, i9);
        }
        i5 = 0;
        i10 = 0;
        i9 = 0;
        this.surfaceViewRenderer.layout(i11, i5, i10, i9);
    }

    private void removeRendererFromVideoTrack() {
        SurfaceViewRenderer surfaceViewRenderer2 = this.surfaceViewRenderer;
        if (surfaceViewRenderer2 != null) {
            VideoTrack videoTrack2 = this.videoTrack;
            if (videoTrack2 != null) {
                videoTrack2.removeSink(surfaceViewRenderer2);
            }
            getSurfaceViewRenderer().release();
            synchronized (this.layoutSyncRoot) {
                this.frameHeight = 0;
                this.frameRotation = 0;
                this.frameWidth = 0;
            }
            requestSurfaceViewRendererLayout();
        }
    }

    /* access modifiers changed from: private */
    public void requestSurfaceViewRendererLayout() {
        getSurfaceViewRenderer().requestLayout();
        if (!invokeIsInLayout()) {
            onLayout(false, getLeft(), getTop(), getRight(), getBottom());
        }
    }

    public void setMirror(boolean z) {
        if (this.mirror != z) {
            this.mirror = z;
            getSurfaceViewRenderer().setMirror(z);
            requestSurfaceViewRendererLayout();
        }
    }

    private void setScalingType(ScalingType scalingType2) {
        synchronized (this.layoutSyncRoot) {
            if (this.scalingType != scalingType2) {
                this.scalingType = scalingType2;
                getSurfaceViewRenderer().setScalingType(scalingType2);
                requestSurfaceViewRendererLayout();
            }
        }
    }

    public void setStream(MediaStream mediaStream) {
        VideoTrack videoTrack2 = null;
        if (mediaStream != null) {
            List list = mediaStream.videoTracks;
            if (!list.isEmpty()) {
                videoTrack2 = (VideoTrack) list.get(0);
            }
        }
        setVideoTrack(videoTrack2);
    }

    private void setVideoTrack(VideoTrack videoTrack2) {
        VideoTrack videoTrack3 = this.videoTrack;
        if (videoTrack3 != videoTrack2) {
            if (videoTrack3 != null) {
                removeRendererFromVideoTrack();
            }
            this.videoTrack = videoTrack2;
            if (videoTrack2 != null) {
                tryAddRendererToVideoTrack();
            }
        }
    }

    public void setZOrder(int i) {
        SurfaceViewRenderer surfaceViewRenderer2 = getSurfaceViewRenderer();
        if (i == 0) {
            surfaceViewRenderer2.setZOrderMediaOverlay(false);
        } else if (i == 1) {
            surfaceViewRenderer2.setZOrderMediaOverlay(true);
        } else if (i == 2) {
            surfaceViewRenderer2.setZOrderOnTop(true);
        }
    }

    private void tryAddRendererToVideoTrack() {
        if (this.videoTrack != null && ViewCompat.isAttachedToWindow(this)) {
            EglBase.Context rootEglBaseContext = EglUtils.getRootEglBaseContext();
            if (rootEglBaseContext == null) {
                Log.e(LOG_TAG, "Failed to render a VideoTrack!");
                return;
            }
            SurfaceViewRenderer surfaceViewRenderer2 = getSurfaceViewRenderer();
            surfaceViewRenderer2.init(rootEglBaseContext, this.rendererEvents);
            this.videoTrack.addSink(surfaceViewRenderer2);
        }
    }
}
