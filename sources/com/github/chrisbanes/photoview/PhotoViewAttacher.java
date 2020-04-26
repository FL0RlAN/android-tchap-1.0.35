package com.github.chrisbanes.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.OverScroller;

public class PhotoViewAttacher implements OnTouchListener, OnLayoutChangeListener {
    private static float DEFAULT_MAX_SCALE = 3.0f;
    private static float DEFAULT_MID_SCALE = 1.75f;
    /* access modifiers changed from: private */
    public static float DEFAULT_MIN_SCALE = 1.0f;
    private static int DEFAULT_ZOOM_DURATION = 200;
    private static final int EDGE_BOTH = 2;
    private static final int EDGE_LEFT = 0;
    private static final int EDGE_NONE = -1;
    private static final int EDGE_RIGHT = 1;
    /* access modifiers changed from: private */
    public static int SINGLE_TOUCH = 1;
    /* access modifiers changed from: private */
    public boolean mAllowParentInterceptOnEdge = true;
    private final Matrix mBaseMatrix = new Matrix();
    private float mBaseRotation;
    /* access modifiers changed from: private */
    public boolean mBlockParentIntercept = false;
    /* access modifiers changed from: private */
    public FlingRunnable mCurrentFlingRunnable;
    private final RectF mDisplayRect = new RectF();
    private final Matrix mDrawMatrix = new Matrix();
    private GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public ImageView mImageView;
    /* access modifiers changed from: private */
    public Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    /* access modifiers changed from: private */
    public OnLongClickListener mLongClickListener;
    private OnMatrixChangedListener mMatrixChangeListener;
    private final float[] mMatrixValues = new float[9];
    /* access modifiers changed from: private */
    public float mMaxScale = DEFAULT_MAX_SCALE;
    private float mMidScale = DEFAULT_MID_SCALE;
    /* access modifiers changed from: private */
    public float mMinScale = DEFAULT_MIN_SCALE;
    /* access modifiers changed from: private */
    public OnClickListener mOnClickListener;
    /* access modifiers changed from: private */
    public OnViewDragListener mOnViewDragListener;
    /* access modifiers changed from: private */
    public OnOutsidePhotoTapListener mOutsidePhotoTapListener;
    /* access modifiers changed from: private */
    public OnPhotoTapListener mPhotoTapListener;
    /* access modifiers changed from: private */
    public OnScaleChangedListener mScaleChangeListener;
    /* access modifiers changed from: private */
    public CustomGestureDetector mScaleDragDetector;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    /* access modifiers changed from: private */
    public int mScrollEdge = 2;
    /* access modifiers changed from: private */
    public OnSingleFlingListener mSingleFlingListener;
    /* access modifiers changed from: private */
    public final Matrix mSuppMatrix = new Matrix();
    /* access modifiers changed from: private */
    public OnViewTapListener mViewTapListener;
    /* access modifiers changed from: private */
    public int mZoomDuration = DEFAULT_ZOOM_DURATION;
    private boolean mZoomEnabled = true;
    /* access modifiers changed from: private */
    public OnGestureListener onGestureListener = new OnGestureListener() {
        public void onDrag(float f, float f2) {
            if (!PhotoViewAttacher.this.mScaleDragDetector.isScaling()) {
                if (PhotoViewAttacher.this.mOnViewDragListener != null) {
                    PhotoViewAttacher.this.mOnViewDragListener.onDrag(f, f2);
                }
                PhotoViewAttacher.this.mSuppMatrix.postTranslate(f, f2);
                PhotoViewAttacher.this.checkAndDisplayMatrix();
                ViewParent parent = PhotoViewAttacher.this.mImageView.getParent();
                if (!PhotoViewAttacher.this.mAllowParentInterceptOnEdge || PhotoViewAttacher.this.mScaleDragDetector.isScaling() || PhotoViewAttacher.this.mBlockParentIntercept) {
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                } else if ((PhotoViewAttacher.this.mScrollEdge == 2 || ((PhotoViewAttacher.this.mScrollEdge == 0 && f >= 1.0f) || (PhotoViewAttacher.this.mScrollEdge == 1 && f <= -1.0f))) && parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            }
        }

        public void onFling(float f, float f2, float f3, float f4) {
            PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
            photoViewAttacher.mCurrentFlingRunnable = new FlingRunnable(photoViewAttacher.mImageView.getContext());
            FlingRunnable access$800 = PhotoViewAttacher.this.mCurrentFlingRunnable;
            PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
            int access$900 = photoViewAttacher2.getImageViewWidth(photoViewAttacher2.mImageView);
            PhotoViewAttacher photoViewAttacher3 = PhotoViewAttacher.this;
            access$800.fling(access$900, photoViewAttacher3.getImageViewHeight(photoViewAttacher3.mImageView), (int) f3, (int) f4);
            PhotoViewAttacher.this.mImageView.post(PhotoViewAttacher.this.mCurrentFlingRunnable);
        }

        public void onScale(float f, float f2, float f3) {
            if (PhotoViewAttacher.this.getScale() >= PhotoViewAttacher.this.mMaxScale && f >= 1.0f) {
                return;
            }
            if (PhotoViewAttacher.this.getScale() > PhotoViewAttacher.this.mMinScale || f > 1.0f) {
                if (PhotoViewAttacher.this.mScaleChangeListener != null) {
                    PhotoViewAttacher.this.mScaleChangeListener.onScaleChange(f, f2, f3);
                }
                PhotoViewAttacher.this.mSuppMatrix.postScale(f, f, f2, f3);
                PhotoViewAttacher.this.checkAndDisplayMatrix();
            }
        }
    };

    /* renamed from: com.github.chrisbanes.photoview.PhotoViewAttacher$4 reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 1;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 2;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 3;
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private class AnimatedZoomRunnable implements Runnable {
        private final float mFocalX;
        private final float mFocalY;
        private final long mStartTime = System.currentTimeMillis();
        private final float mZoomEnd;
        private final float mZoomStart;

        public AnimatedZoomRunnable(float f, float f2, float f3, float f4) {
            this.mFocalX = f3;
            this.mFocalY = f4;
            this.mZoomStart = f;
            this.mZoomEnd = f2;
        }

        public void run() {
            float interpolate = interpolate();
            float f = this.mZoomStart;
            PhotoViewAttacher.this.onGestureListener.onScale((f + ((this.mZoomEnd - f) * interpolate)) / PhotoViewAttacher.this.getScale(), this.mFocalX, this.mFocalY);
            if (interpolate < 1.0f) {
                Compat.postOnAnimation(PhotoViewAttacher.this.mImageView, this);
            }
        }

        private float interpolate() {
            return PhotoViewAttacher.this.mInterpolator.getInterpolation(Math.min(1.0f, (((float) (System.currentTimeMillis() - this.mStartTime)) * 1.0f) / ((float) PhotoViewAttacher.this.mZoomDuration)));
        }
    }

    private class FlingRunnable implements Runnable {
        private int mCurrentX;
        private int mCurrentY;
        private final OverScroller mScroller;

        public FlingRunnable(Context context) {
            this.mScroller = new OverScroller(context);
        }

        public void cancelFling() {
            this.mScroller.forceFinished(true);
        }

        public void fling(int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
            if (displayRect != null) {
                int round = Math.round(-displayRect.left);
                float f = (float) i;
                if (f < displayRect.width()) {
                    i5 = Math.round(displayRect.width() - f);
                    i6 = 0;
                } else {
                    i6 = round;
                    i5 = i6;
                }
                int round2 = Math.round(-displayRect.top);
                float f2 = (float) i2;
                if (f2 < displayRect.height()) {
                    i7 = Math.round(displayRect.height() - f2);
                    i8 = 0;
                } else {
                    i8 = round2;
                    i7 = i8;
                }
                this.mCurrentX = round;
                this.mCurrentY = round2;
                if (!(round == i5 && round2 == i7)) {
                    this.mScroller.fling(round, round2, i3, i4, i6, i5, i8, i7, 0, 0);
                }
            }
        }

        public void run() {
            if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
                int currX = this.mScroller.getCurrX();
                int currY = this.mScroller.getCurrY();
                PhotoViewAttacher.this.mSuppMatrix.postTranslate((float) (this.mCurrentX - currX), (float) (this.mCurrentY - currY));
                PhotoViewAttacher.this.checkAndDisplayMatrix();
                this.mCurrentX = currX;
                this.mCurrentY = currY;
                Compat.postOnAnimation(PhotoViewAttacher.this.mImageView, this);
            }
        }
    }

    public PhotoViewAttacher(ImageView imageView) {
        this.mImageView = imageView;
        imageView.setOnTouchListener(this);
        imageView.addOnLayoutChangeListener(this);
        if (!imageView.isInEditMode()) {
            this.mBaseRotation = 0.0f;
            this.mScaleDragDetector = new CustomGestureDetector(imageView.getContext(), this.onGestureListener);
            this.mGestureDetector = new GestureDetector(imageView.getContext(), new SimpleOnGestureListener() {
                public void onLongPress(MotionEvent motionEvent) {
                    if (PhotoViewAttacher.this.mLongClickListener != null) {
                        PhotoViewAttacher.this.mLongClickListener.onLongClick(PhotoViewAttacher.this.mImageView);
                    }
                }

                public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                    if (PhotoViewAttacher.this.mSingleFlingListener == null || PhotoViewAttacher.this.getScale() > PhotoViewAttacher.DEFAULT_MIN_SCALE || motionEvent.getPointerCount() > PhotoViewAttacher.SINGLE_TOUCH || motionEvent.getPointerCount() > PhotoViewAttacher.SINGLE_TOUCH) {
                        return false;
                    }
                    return PhotoViewAttacher.this.mSingleFlingListener.onFling(motionEvent, motionEvent2, f, f2);
                }
            });
            this.mGestureDetector.setOnDoubleTapListener(new OnDoubleTapListener() {
                public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                    return false;
                }

                public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                    if (PhotoViewAttacher.this.mOnClickListener != null) {
                        PhotoViewAttacher.this.mOnClickListener.onClick(PhotoViewAttacher.this.mImageView);
                    }
                    RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    if (PhotoViewAttacher.this.mViewTapListener != null) {
                        PhotoViewAttacher.this.mViewTapListener.onViewTap(PhotoViewAttacher.this.mImageView, x, y);
                    }
                    if (displayRect != null) {
                        if (displayRect.contains(x, y)) {
                            float width = (x - displayRect.left) / displayRect.width();
                            float height = (y - displayRect.top) / displayRect.height();
                            if (PhotoViewAttacher.this.mPhotoTapListener != null) {
                                PhotoViewAttacher.this.mPhotoTapListener.onPhotoTap(PhotoViewAttacher.this.mImageView, width, height);
                            }
                            return true;
                        } else if (PhotoViewAttacher.this.mOutsidePhotoTapListener != null) {
                            PhotoViewAttacher.this.mOutsidePhotoTapListener.onOutsidePhotoTap(PhotoViewAttacher.this.mImageView);
                        }
                    }
                    return false;
                }

                public boolean onDoubleTap(MotionEvent motionEvent) {
                    try {
                        float scale = PhotoViewAttacher.this.getScale();
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        if (scale < PhotoViewAttacher.this.getMediumScale()) {
                            PhotoViewAttacher.this.setScale(PhotoViewAttacher.this.getMediumScale(), x, y, true);
                        } else if (scale < PhotoViewAttacher.this.getMediumScale() || scale >= PhotoViewAttacher.this.getMaximumScale()) {
                            PhotoViewAttacher.this.setScale(PhotoViewAttacher.this.getMinimumScale(), x, y, true);
                        } else {
                            PhotoViewAttacher.this.setScale(PhotoViewAttacher.this.getMaximumScale(), x, y, true);
                        }
                    } catch (ArrayIndexOutOfBoundsException unused) {
                    }
                    return true;
                }
            });
        }
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mGestureDetector.setOnDoubleTapListener(onDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
        this.mScaleChangeListener = onScaleChangedListener;
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        this.mSingleFlingListener = onSingleFlingListener;
    }

    @Deprecated
    public boolean isZoomEnabled() {
        return this.mZoomEnabled;
    }

    public RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    public boolean setDisplayMatrix(Matrix matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        } else if (this.mImageView.getDrawable() == null) {
            return false;
        } else {
            this.mSuppMatrix.set(matrix);
            checkAndDisplayMatrix();
            return true;
        }
    }

    public void setBaseRotation(float f) {
        this.mBaseRotation = f % 360.0f;
        update();
        setRotationBy(this.mBaseRotation);
        checkAndDisplayMatrix();
    }

    public void setRotationTo(float f) {
        this.mSuppMatrix.setRotate(f % 360.0f);
        checkAndDisplayMatrix();
    }

    public void setRotationBy(float f) {
        this.mSuppMatrix.postRotate(f % 360.0f);
        checkAndDisplayMatrix();
    }

    public float getMinimumScale() {
        return this.mMinScale;
    }

    public float getMediumScale() {
        return this.mMidScale;
    }

    public float getMaximumScale() {
        return this.mMaxScale;
    }

    public float getScale() {
        return (float) Math.sqrt((double) (((float) Math.pow((double) getValue(this.mSuppMatrix, 0), 2.0d)) + ((float) Math.pow((double) getValue(this.mSuppMatrix, 3), 2.0d))));
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i != i5 || i2 != i6 || i3 != i7 || i4 != i8) {
            updateBaseMatrix(this.mImageView.getDrawable());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b2  */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z;
        CustomGestureDetector customGestureDetector;
        boolean z2;
        GestureDetector gestureDetector;
        boolean z3 = false;
        if (!this.mZoomEnabled || !Util.hasDrawable((ImageView) view)) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            cancelFling();
        } else if (action == 1 || action == 3) {
            if (getScale() < this.mMinScale) {
                RectF displayRect = getDisplayRect();
                if (displayRect != null) {
                    AnimatedZoomRunnable animatedZoomRunnable = new AnimatedZoomRunnable(getScale(), this.mMinScale, displayRect.centerX(), displayRect.centerY());
                    view.post(animatedZoomRunnable);
                }
            } else if (getScale() > this.mMaxScale) {
                RectF displayRect2 = getDisplayRect();
                if (displayRect2 != null) {
                    AnimatedZoomRunnable animatedZoomRunnable2 = new AnimatedZoomRunnable(getScale(), this.mMaxScale, displayRect2.centerX(), displayRect2.centerY());
                    view.post(animatedZoomRunnable2);
                }
            }
            z = true;
            customGestureDetector = this.mScaleDragDetector;
            if (customGestureDetector == null) {
                boolean isScaling = customGestureDetector.isScaling();
                boolean isDragging = this.mScaleDragDetector.isDragging();
                boolean onTouchEvent = this.mScaleDragDetector.onTouchEvent(motionEvent);
                boolean z4 = !isScaling && !this.mScaleDragDetector.isScaling();
                boolean z5 = !isDragging && !this.mScaleDragDetector.isDragging();
                if (z4 && z5) {
                    z3 = true;
                }
                this.mBlockParentIntercept = z3;
                z2 = onTouchEvent;
            } else {
                z2 = z;
            }
            gestureDetector = this.mGestureDetector;
            if (gestureDetector == null && gestureDetector.onTouchEvent(motionEvent)) {
                return true;
            }
        }
        z = false;
        customGestureDetector = this.mScaleDragDetector;
        if (customGestureDetector == null) {
        }
        gestureDetector = this.mGestureDetector;
        return gestureDetector == null ? z2 : z2;
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.mAllowParentInterceptOnEdge = z;
    }

    public void setMinimumScale(float f) {
        Util.checkZoomLevels(f, this.mMidScale, this.mMaxScale);
        this.mMinScale = f;
    }

    public void setMediumScale(float f) {
        Util.checkZoomLevels(this.mMinScale, f, this.mMaxScale);
        this.mMidScale = f;
    }

    public void setMaximumScale(float f) {
        Util.checkZoomLevels(this.mMinScale, this.mMidScale, f);
        this.mMaxScale = f;
    }

    public void setScaleLevels(float f, float f2, float f3) {
        Util.checkZoomLevels(f, f2, f3);
        this.mMinScale = f;
        this.mMidScale = f2;
        this.mMaxScale = f3;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.mLongClickListener = onLongClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        this.mMatrixChangeListener = onMatrixChangedListener;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.mPhotoTapListener = onPhotoTapListener;
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener onOutsidePhotoTapListener) {
        this.mOutsidePhotoTapListener = onOutsidePhotoTapListener;
    }

    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.mViewTapListener = onViewTapListener;
    }

    public void setOnViewDragListener(OnViewDragListener onViewDragListener) {
        this.mOnViewDragListener = onViewDragListener;
    }

    public void setScale(float f) {
        setScale(f, false);
    }

    public void setScale(float f, boolean z) {
        setScale(f, (float) (this.mImageView.getRight() / 2), (float) (this.mImageView.getBottom() / 2), z);
    }

    public void setScale(float f, float f2, float f3, boolean z) {
        if (f < this.mMinScale || f > this.mMaxScale) {
            throw new IllegalArgumentException("Scale must be within the range of minScale and maxScale");
        } else if (z) {
            ImageView imageView = this.mImageView;
            AnimatedZoomRunnable animatedZoomRunnable = new AnimatedZoomRunnable(getScale(), f, f2, f3);
            imageView.post(animatedZoomRunnable);
        } else {
            this.mSuppMatrix.setScale(f, f, f2, f3);
            checkAndDisplayMatrix();
        }
    }

    public void setZoomInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public void setScaleType(ScaleType scaleType) {
        if (Util.isSupportedScaleType(scaleType) && scaleType != this.mScaleType) {
            this.mScaleType = scaleType;
            update();
        }
    }

    public boolean isZoomable() {
        return this.mZoomEnabled;
    }

    public void setZoomable(boolean z) {
        this.mZoomEnabled = z;
        update();
    }

    public void update() {
        if (this.mZoomEnabled) {
            updateBaseMatrix(this.mImageView.getDrawable());
        } else {
            resetMatrix();
        }
    }

    public void getDisplayMatrix(Matrix matrix) {
        matrix.set(getDrawMatrix());
    }

    public void getSuppMatrix(Matrix matrix) {
        matrix.set(this.mSuppMatrix);
    }

    private Matrix getDrawMatrix() {
        this.mDrawMatrix.set(this.mBaseMatrix);
        this.mDrawMatrix.postConcat(this.mSuppMatrix);
        return this.mDrawMatrix;
    }

    public Matrix getImageMatrix() {
        return this.mDrawMatrix;
    }

    public void setZoomTransitionDuration(int i) {
        this.mZoomDuration = i;
    }

    private float getValue(Matrix matrix, int i) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[i];
    }

    private void resetMatrix() {
        this.mSuppMatrix.reset();
        setRotationBy(this.mBaseRotation);
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
    }

    private void setImageViewMatrix(Matrix matrix) {
        this.mImageView.setImageMatrix(matrix);
        if (this.mMatrixChangeListener != null) {
            RectF displayRect = getDisplayRect(matrix);
            if (displayRect != null) {
                this.mMatrixChangeListener.onMatrixChanged(displayRect);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(getDrawMatrix());
        }
    }

    private RectF getDisplayRect(Matrix matrix) {
        Drawable drawable = this.mImageView.getDrawable();
        if (drawable == null) {
            return null;
        }
        this.mDisplayRect.set(0.0f, 0.0f, (float) drawable.getIntrinsicWidth(), (float) drawable.getIntrinsicHeight());
        matrix.mapRect(this.mDisplayRect);
        return this.mDisplayRect;
    }

    private void updateBaseMatrix(Drawable drawable) {
        if (drawable != null) {
            float imageViewWidth = (float) getImageViewWidth(this.mImageView);
            float imageViewHeight = (float) getImageViewHeight(this.mImageView);
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            this.mBaseMatrix.reset();
            float f = (float) intrinsicWidth;
            float f2 = imageViewWidth / f;
            float f3 = (float) intrinsicHeight;
            float f4 = imageViewHeight / f3;
            if (this.mScaleType == ScaleType.CENTER) {
                this.mBaseMatrix.postTranslate((imageViewWidth - f) / 2.0f, (imageViewHeight - f3) / 2.0f);
            } else if (this.mScaleType == ScaleType.CENTER_CROP) {
                float max = Math.max(f2, f4);
                this.mBaseMatrix.postScale(max, max);
                this.mBaseMatrix.postTranslate((imageViewWidth - (f * max)) / 2.0f, (imageViewHeight - (f3 * max)) / 2.0f);
            } else if (this.mScaleType == ScaleType.CENTER_INSIDE) {
                float min = Math.min(1.0f, Math.min(f2, f4));
                this.mBaseMatrix.postScale(min, min);
                this.mBaseMatrix.postTranslate((imageViewWidth - (f * min)) / 2.0f, (imageViewHeight - (f3 * min)) / 2.0f);
            } else {
                RectF rectF = new RectF(0.0f, 0.0f, f, f3);
                RectF rectF2 = new RectF(0.0f, 0.0f, imageViewWidth, imageViewHeight);
                if (((int) this.mBaseRotation) % 180 != 0) {
                    rectF = new RectF(0.0f, 0.0f, f3, f);
                }
                int i = AnonymousClass4.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()];
                if (i == 1) {
                    this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.CENTER);
                } else if (i == 2) {
                    this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.START);
                } else if (i == 3) {
                    this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.END);
                } else if (i == 4) {
                    this.mBaseMatrix.setRectToRect(rectF, rectF2, ScaleToFit.FILL);
                }
            }
            resetMatrix();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0080  */
    private boolean checkMatrixBounds() {
        float f;
        float imageViewWidth;
        float f2;
        float f3;
        float f4;
        float f5;
        RectF displayRect = getDisplayRect(getDrawMatrix());
        if (displayRect == null) {
            return false;
        }
        float height = displayRect.height();
        float width = displayRect.width();
        float imageViewHeight = (float) getImageViewHeight(this.mImageView);
        float f6 = 0.0f;
        if (height <= imageViewHeight) {
            int i = AnonymousClass4.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()];
            if (i == 2) {
                f4 = displayRect.top;
                f = -f4;
                imageViewWidth = (float) getImageViewWidth(this.mImageView);
                if (width > imageViewWidth) {
                    int i2 = AnonymousClass4.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()];
                    if (i2 != 2) {
                        if (i2 != 3) {
                            f2 = (imageViewWidth - width) / 2.0f;
                            f3 = displayRect.left;
                        } else {
                            f2 = imageViewWidth - width;
                            f3 = displayRect.left;
                        }
                        f6 = f2 - f3;
                    } else {
                        f6 = -displayRect.left;
                    }
                    this.mScrollEdge = 2;
                } else if (displayRect.left > 0.0f) {
                    this.mScrollEdge = 0;
                    f6 = -displayRect.left;
                } else if (displayRect.right < imageViewWidth) {
                    f6 = imageViewWidth - displayRect.right;
                    this.mScrollEdge = 1;
                } else {
                    this.mScrollEdge = -1;
                }
                this.mSuppMatrix.postTranslate(f6, f);
                return true;
            } else if (i != 3) {
                imageViewHeight = (imageViewHeight - height) / 2.0f;
                f5 = displayRect.top;
            } else {
                imageViewHeight -= height;
                f5 = displayRect.top;
            }
        } else if (displayRect.top > 0.0f) {
            f4 = displayRect.top;
            f = -f4;
            imageViewWidth = (float) getImageViewWidth(this.mImageView);
            if (width > imageViewWidth) {
            }
            this.mSuppMatrix.postTranslate(f6, f);
            return true;
        } else if (displayRect.bottom < imageViewHeight) {
            f5 = displayRect.bottom;
        } else {
            f = 0.0f;
            imageViewWidth = (float) getImageViewWidth(this.mImageView);
            if (width > imageViewWidth) {
            }
            this.mSuppMatrix.postTranslate(f6, f);
            return true;
        }
        f = imageViewHeight - f5;
        imageViewWidth = (float) getImageViewWidth(this.mImageView);
        if (width > imageViewWidth) {
        }
        this.mSuppMatrix.postTranslate(f6, f);
        return true;
    }

    /* access modifiers changed from: private */
    public int getImageViewWidth(ImageView imageView) {
        return (imageView.getWidth() - imageView.getPaddingLeft()) - imageView.getPaddingRight();
    }

    /* access modifiers changed from: private */
    public int getImageViewHeight(ImageView imageView) {
        return (imageView.getHeight() - imageView.getPaddingTop()) - imageView.getPaddingBottom();
    }

    private void cancelFling() {
        FlingRunnable flingRunnable = this.mCurrentFlingRunnable;
        if (flingRunnable != null) {
            flingRunnable.cancelFling();
            this.mCurrentFlingRunnable = null;
        }
    }
}
