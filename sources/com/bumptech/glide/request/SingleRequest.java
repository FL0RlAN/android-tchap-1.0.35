package com.bumptech.glide.request;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.core.util.Pools.Pool;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Engine.LoadStatus;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.FactoryPools.Factory;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;

public final class SingleRequest<R> implements Request, SizeReadyCallback, ResourceCallback, Poolable {
    private static final String GLIDE_TAG = "Glide";
    private static final boolean IS_VERBOSE_LOGGABLE = Log.isLoggable(TAG, 2);
    private static final Pool<SingleRequest<?>> POOL = FactoryPools.simple(150, new Factory<SingleRequest<?>>() {
        public SingleRequest<?> create() {
            return new SingleRequest<>();
        }
    });
    private static final String TAG = "Request";
    private TransitionFactory<? super R> animationFactory;
    private Context context;
    private Engine engine;
    private Drawable errorDrawable;
    private Drawable fallbackDrawable;
    private GlideContext glideContext;
    private int height;
    private boolean isCallingCallbacks;
    private LoadStatus loadStatus;
    private Object model;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private Priority priority;
    private RequestCoordinator requestCoordinator;
    private RequestListener<R> requestListener;
    private RequestOptions requestOptions;
    private Resource<R> resource;
    private long startTime;
    private final StateVerifier stateVerifier;
    private Status status;
    private final String tag;
    private Target<R> target;
    private RequestListener<R> targetListener;
    private Class<R> transcodeClass;
    private int width;

    private enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CANCELLED,
        CLEARED,
        PAUSED
    }

    public static <R> SingleRequest<R> obtain(Context context2, GlideContext glideContext2, Object obj, Class<R> cls, RequestOptions requestOptions2, int i, int i2, Priority priority2, Target<R> target2, RequestListener<R> requestListener2, RequestListener<R> requestListener3, RequestCoordinator requestCoordinator2, Engine engine2, TransitionFactory<? super R> transitionFactory) {
        SingleRequest<R> singleRequest = (SingleRequest) POOL.acquire();
        if (singleRequest == null) {
            singleRequest = new SingleRequest<>();
        }
        singleRequest.init(context2, glideContext2, obj, cls, requestOptions2, i, i2, priority2, target2, requestListener2, requestListener3, requestCoordinator2, engine2, transitionFactory);
        return singleRequest;
    }

    SingleRequest() {
        this.tag = IS_VERBOSE_LOGGABLE ? String.valueOf(super.hashCode()) : null;
        this.stateVerifier = StateVerifier.newInstance();
    }

    private void init(Context context2, GlideContext glideContext2, Object obj, Class<R> cls, RequestOptions requestOptions2, int i, int i2, Priority priority2, Target<R> target2, RequestListener<R> requestListener2, RequestListener<R> requestListener3, RequestCoordinator requestCoordinator2, Engine engine2, TransitionFactory<? super R> transitionFactory) {
        this.context = context2;
        this.glideContext = glideContext2;
        this.model = obj;
        this.transcodeClass = cls;
        this.requestOptions = requestOptions2;
        this.overrideWidth = i;
        this.overrideHeight = i2;
        this.priority = priority2;
        this.target = target2;
        this.targetListener = requestListener2;
        this.requestListener = requestListener3;
        this.requestCoordinator = requestCoordinator2;
        this.engine = engine2;
        this.animationFactory = transitionFactory;
        this.status = Status.PENDING;
    }

    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    public void recycle() {
        assertNotCallingCallbacks();
        this.context = null;
        this.glideContext = null;
        this.model = null;
        this.transcodeClass = null;
        this.requestOptions = null;
        this.overrideWidth = -1;
        this.overrideHeight = -1;
        this.target = null;
        this.requestListener = null;
        this.targetListener = null;
        this.requestCoordinator = null;
        this.animationFactory = null;
        this.loadStatus = null;
        this.errorDrawable = null;
        this.placeholderDrawable = null;
        this.fallbackDrawable = null;
        this.width = -1;
        this.height = -1;
        POOL.release(this);
    }

    public void begin() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                this.width = this.overrideWidth;
                this.height = this.overrideHeight;
            }
            onLoadFailed(new GlideException("Received null model"), getFallbackDrawable() == null ? 5 : 3);
        } else if (this.status == Status.RUNNING) {
            throw new IllegalArgumentException("Cannot restart a running request");
        } else if (this.status == Status.COMPLETE) {
            onResourceReady(this.resource, DataSource.MEMORY_CACHE);
        } else {
            this.status = Status.WAITING_FOR_SIZE;
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                onSizeReady(this.overrideWidth, this.overrideHeight);
            } else {
                this.target.getSize(this);
            }
            if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && canNotifyStatusChanged()) {
                this.target.onLoadStarted(getPlaceholderDrawable());
            }
            if (IS_VERBOSE_LOGGABLE) {
                StringBuilder sb = new StringBuilder();
                sb.append("finished run method in ");
                sb.append(LogTime.getElapsedMillis(this.startTime));
                logV(sb.toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void cancel() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.target.removeCallback(this);
        this.status = Status.CANCELLED;
        LoadStatus loadStatus2 = this.loadStatus;
        if (loadStatus2 != null) {
            loadStatus2.cancel();
            this.loadStatus = null;
        }
    }

    private void assertNotCallingCallbacks() {
        if (this.isCallingCallbacks) {
            throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you're trying to start a fallback request when a load fails, use RequestBuilder#error(RequestBuilder). Otherwise consider posting your into() or clear() calls to the main thread using a Handler instead.");
        }
    }

    public void clear() {
        Util.assertMainThread();
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        if (this.status != Status.CLEARED) {
            cancel();
            Resource<R> resource2 = this.resource;
            if (resource2 != null) {
                releaseResource(resource2);
            }
            if (canNotifyCleared()) {
                this.target.onLoadCleared(getPlaceholderDrawable());
            }
            this.status = Status.CLEARED;
        }
    }

    public boolean isPaused() {
        return this.status == Status.PAUSED;
    }

    public void pause() {
        clear();
        this.status = Status.PAUSED;
    }

    private void releaseResource(Resource<?> resource2) {
        this.engine.release(resource2);
        this.resource = null;
    }

    public boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }

    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

    public boolean isResourceSet() {
        return isComplete();
    }

    public boolean isCancelled() {
        return this.status == Status.CANCELLED || this.status == Status.CLEARED;
    }

    public boolean isFailed() {
        return this.status == Status.FAILED;
    }

    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null) {
            this.errorDrawable = this.requestOptions.getErrorPlaceholder();
            if (this.errorDrawable == null && this.requestOptions.getErrorId() > 0) {
                this.errorDrawable = loadDrawable(this.requestOptions.getErrorId());
            }
        }
        return this.errorDrawable;
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null) {
            this.placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
            if (this.placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0) {
                this.placeholderDrawable = loadDrawable(this.requestOptions.getPlaceholderId());
            }
        }
        return this.placeholderDrawable;
    }

    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null) {
            this.fallbackDrawable = this.requestOptions.getFallbackDrawable();
            if (this.fallbackDrawable == null && this.requestOptions.getFallbackId() > 0) {
                this.fallbackDrawable = loadDrawable(this.requestOptions.getFallbackId());
            }
        }
        return this.fallbackDrawable;
    }

    private Drawable loadDrawable(int i) {
        return DrawableDecoderCompat.getDrawable((Context) this.glideContext, i, this.requestOptions.getTheme() != null ? this.requestOptions.getTheme() : this.context.getTheme());
    }

    private void setErrorPlaceholder() {
        if (canNotifyStatusChanged()) {
            Drawable drawable = null;
            if (this.model == null) {
                drawable = getFallbackDrawable();
            }
            if (drawable == null) {
                drawable = getErrorDrawable();
            }
            if (drawable == null) {
                drawable = getPlaceholderDrawable();
            }
            this.target.onLoadFailed(drawable);
        }
    }

    public void onSizeReady(int i, int i2) {
        this.stateVerifier.throwIfRecycled();
        if (IS_VERBOSE_LOGGABLE) {
            StringBuilder sb = new StringBuilder();
            sb.append("Got onSizeReady in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            logV(sb.toString());
        }
        if (this.status == Status.WAITING_FOR_SIZE) {
            this.status = Status.RUNNING;
            float sizeMultiplier = this.requestOptions.getSizeMultiplier();
            this.width = maybeApplySizeMultiplier(i, sizeMultiplier);
            this.height = maybeApplySizeMultiplier(i2, sizeMultiplier);
            if (IS_VERBOSE_LOGGABLE) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("finished setup for calling load in ");
                sb2.append(LogTime.getElapsedMillis(this.startTime));
                logV(sb2.toString());
            }
            Engine engine2 = this.engine;
            GlideContext glideContext2 = this.glideContext;
            LoadStatus load = engine2.load(glideContext2, this.model, this.requestOptions.getSignature(), this.width, this.height, this.requestOptions.getResourceClass(), this.transcodeClass, this.priority, this.requestOptions.getDiskCacheStrategy(), this.requestOptions.getTransformations(), this.requestOptions.isTransformationRequired(), this.requestOptions.isScaleOnlyOrNoTransform(), this.requestOptions.getOptions(), this.requestOptions.isMemoryCacheable(), this.requestOptions.getUseUnlimitedSourceGeneratorsPool(), this.requestOptions.getUseAnimationPool(), this.requestOptions.getOnlyRetrieveFromCache(), this);
            this.loadStatus = load;
            if (this.status != Status.RUNNING) {
                this.loadStatus = null;
            }
            if (IS_VERBOSE_LOGGABLE) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("finished onSizeReady in ");
                sb3.append(LogTime.getElapsedMillis(this.startTime));
                logV(sb3.toString());
            }
        }
    }

    private static int maybeApplySizeMultiplier(int i, float f) {
        return i == Integer.MIN_VALUE ? i : Math.round(f * ((float) i));
    }

    private boolean canSetResource() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || requestCoordinator2.canSetImage(this);
    }

    private boolean canNotifyCleared() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || requestCoordinator2.canNotifyCleared(this);
    }

    private boolean canNotifyStatusChanged() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || requestCoordinator2.canNotifyStatusChanged(this);
    }

    private boolean isFirstReadyResource() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || !requestCoordinator2.isAnyResourceSet();
    }

    private void notifyLoadSuccess() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        if (requestCoordinator2 != null) {
            requestCoordinator2.onRequestSuccess(this);
        }
    }

    private void notifyLoadFailed() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        if (requestCoordinator2 != null) {
            requestCoordinator2.onRequestFailed(this);
        }
    }

    public void onResourceReady(Resource<?> resource2, DataSource dataSource) {
        this.stateVerifier.throwIfRecycled();
        this.loadStatus = null;
        if (resource2 == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected to receive a Resource<R> with an object of ");
            sb.append(this.transcodeClass);
            sb.append(" inside, but instead got null.");
            onLoadFailed(new GlideException(sb.toString()));
            return;
        }
        Object obj = resource2.get();
        if (obj == null || !this.transcodeClass.isAssignableFrom(obj.getClass())) {
            releaseResource(resource2);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected to receive an object of ");
            sb2.append(this.transcodeClass);
            sb2.append(" but instead got ");
            String str = "";
            sb2.append(obj != null ? obj.getClass() : str);
            sb2.append("{");
            sb2.append(obj);
            sb2.append("} inside Resource{");
            sb2.append(resource2);
            sb2.append("}.");
            if (obj == null) {
                str = " To indicate failure return a null Resource object, rather than a Resource object containing null data.";
            }
            sb2.append(str);
            onLoadFailed(new GlideException(sb2.toString()));
        } else if (!canSetResource()) {
            releaseResource(resource2);
            this.status = Status.COMPLETE;
        } else {
            onResourceReady(resource2, obj, dataSource);
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0093, code lost:
        if (r7.targetListener.onResourceReady(r9, r7.model, r7.target, r10, r6) == false) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0080, code lost:
        if (r7.requestListener.onResourceReady(r9, r7.model, r7.target, r10, r6) == false) goto L_0x0082;
     */
    private void onResourceReady(Resource<R> resource2, R r, DataSource dataSource) {
        boolean isFirstReadyResource = isFirstReadyResource();
        this.status = Status.COMPLETE;
        this.resource = resource2;
        if (this.glideContext.getLogLevel() <= 3) {
            StringBuilder sb = new StringBuilder();
            sb.append("Finished loading ");
            sb.append(r.getClass().getSimpleName());
            sb.append(" from ");
            sb.append(dataSource);
            sb.append(" for ");
            sb.append(this.model);
            sb.append(" with size [");
            sb.append(this.width);
            sb.append("x");
            sb.append(this.height);
            sb.append("] in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            sb.append(" ms");
            Log.d(GLIDE_TAG, sb.toString());
        }
        this.isCallingCallbacks = true;
        try {
            if (this.requestListener != null) {
            }
            if (this.targetListener != null) {
            }
            this.target.onResourceReady(r, this.animationFactory.build(dataSource, isFirstReadyResource));
            this.isCallingCallbacks = false;
            notifyLoadSuccess();
        } catch (Throwable th) {
            this.isCallingCallbacks = false;
            throw th;
        }
    }

    public void onLoadFailed(GlideException glideException) {
        onLoadFailed(glideException, 5);
    }

    /* JADX INFO: finally extract failed */
    private void onLoadFailed(GlideException glideException, int i) {
        this.stateVerifier.throwIfRecycled();
        int logLevel = this.glideContext.getLogLevel();
        if (logLevel <= i) {
            StringBuilder sb = new StringBuilder();
            sb.append("Load failed for ");
            sb.append(this.model);
            sb.append(" with size [");
            sb.append(this.width);
            sb.append("x");
            sb.append(this.height);
            sb.append("]");
            String sb2 = sb.toString();
            String str = GLIDE_TAG;
            Log.w(str, sb2, glideException);
            if (logLevel <= 4) {
                glideException.logRootCauses(str);
            }
        }
        this.loadStatus = null;
        this.status = Status.FAILED;
        this.isCallingCallbacks = true;
        try {
            if ((this.requestListener == null || !this.requestListener.onLoadFailed(glideException, this.model, this.target, isFirstReadyResource())) && (this.targetListener == null || !this.targetListener.onLoadFailed(glideException, this.model, this.target, isFirstReadyResource()))) {
                setErrorPlaceholder();
            }
            this.isCallingCallbacks = false;
            notifyLoadFailed();
        } catch (Throwable th) {
            this.isCallingCallbacks = false;
            throw th;
        }
    }

    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof SingleRequest)) {
            return false;
        }
        SingleRequest singleRequest = (SingleRequest) request;
        if (this.overrideWidth != singleRequest.overrideWidth || this.overrideHeight != singleRequest.overrideHeight || !Util.bothModelsNullEquivalentOrEquals(this.model, singleRequest.model) || !this.transcodeClass.equals(singleRequest.transcodeClass) || !this.requestOptions.equals(singleRequest.requestOptions) || this.priority != singleRequest.priority) {
            return false;
        }
        RequestListener<R> requestListener2 = this.requestListener;
        RequestListener<R> requestListener3 = singleRequest.requestListener;
        if (requestListener2 != null) {
            if (requestListener3 == null) {
                return false;
            }
        } else if (requestListener3 != null) {
            return false;
        }
        return true;
    }

    private void logV(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" this: ");
        sb.append(this.tag);
        Log.v(TAG, sb.toString());
    }
}
