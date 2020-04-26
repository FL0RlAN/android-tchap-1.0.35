package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.ErrorRequestCoordinator;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.ApplicationVersionSignature;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;

public class RequestBuilder<TranscodeType> implements Cloneable, ModelTypes<RequestBuilder<TranscodeType>> {
    protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    private final Context context;
    private final RequestOptions defaultRequestOptions;
    private RequestBuilder<TranscodeType> errorBuilder;
    private final Glide glide;
    private final GlideContext glideContext;
    private boolean isDefaultTransitionOptionsSet;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;
    private Object model;
    private RequestListener<TranscodeType> requestListener;
    private final RequestManager requestManager;
    protected RequestOptions requestOptions;
    private Float thumbSizeMultiplier;
    private RequestBuilder<TranscodeType> thumbnailBuilder;
    private final Class<TranscodeType> transcodeClass;
    private TransitionOptions<?, ? super TranscodeType> transitionOptions;

    /* renamed from: com.bumptech.glide.RequestBuilder$2 reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$Priority = new int[Priority.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0066 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0071 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x007c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0087 */
        static {
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.LOW.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.HIGH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.IMMEDIATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 1;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 2;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 3;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 4;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 5;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 6;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 7;
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    protected RequestBuilder(Glide glide2, RequestManager requestManager2, Class<TranscodeType> cls, Context context2) {
        this.isDefaultTransitionOptionsSet = true;
        this.glide = glide2;
        this.requestManager = requestManager2;
        this.transcodeClass = cls;
        this.defaultRequestOptions = requestManager2.getDefaultRequestOptions();
        this.context = context2;
        this.transitionOptions = requestManager2.getDefaultTransitionOptions(cls);
        this.requestOptions = this.defaultRequestOptions;
        this.glideContext = glide2.getGlideContext();
    }

    protected RequestBuilder(Class<TranscodeType> cls, RequestBuilder<?> requestBuilder) {
        this(requestBuilder.glide, requestBuilder.requestManager, cls, requestBuilder.context);
        this.model = requestBuilder.model;
        this.isModelSet = requestBuilder.isModelSet;
        this.requestOptions = requestBuilder.requestOptions;
    }

    public RequestBuilder<TranscodeType> apply(RequestOptions requestOptions2) {
        Preconditions.checkNotNull(requestOptions2);
        this.requestOptions = getMutableOptions().apply(requestOptions2);
        return this;
    }

    /* access modifiers changed from: protected */
    public RequestOptions getMutableOptions() {
        RequestOptions requestOptions2 = this.defaultRequestOptions;
        RequestOptions requestOptions3 = this.requestOptions;
        return requestOptions2 == requestOptions3 ? requestOptions3.clone() : requestOptions3;
    }

    public RequestBuilder<TranscodeType> transition(TransitionOptions<?, ? super TranscodeType> transitionOptions2) {
        this.transitionOptions = (TransitionOptions) Preconditions.checkNotNull(transitionOptions2);
        this.isDefaultTransitionOptionsSet = false;
        return this;
    }

    public RequestBuilder<TranscodeType> listener(RequestListener<TranscodeType> requestListener2) {
        this.requestListener = requestListener2;
        return this;
    }

    public RequestBuilder<TranscodeType> error(RequestBuilder<TranscodeType> requestBuilder) {
        this.errorBuilder = requestBuilder;
        return this;
    }

    public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType> requestBuilder) {
        this.thumbnailBuilder = requestBuilder;
        return this;
    }

    public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType>... requestBuilderArr) {
        RequestBuilder<TranscodeType> requestBuilder = null;
        if (requestBuilderArr == null || requestBuilderArr.length == 0) {
            return thumbnail(null);
        }
        for (int length = requestBuilderArr.length - 1; length >= 0; length--) {
            RequestBuilder<TranscodeType> requestBuilder2 = requestBuilderArr[length];
            if (requestBuilder2 != null) {
                requestBuilder = requestBuilder == null ? requestBuilder2 : requestBuilder2.thumbnail(requestBuilder);
            }
        }
        return thumbnail(requestBuilder);
    }

    public RequestBuilder<TranscodeType> thumbnail(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.thumbSizeMultiplier = Float.valueOf(f);
        return this;
    }

    public RequestBuilder<TranscodeType> load(Object obj) {
        return loadGeneric(obj);
    }

    private RequestBuilder<TranscodeType> loadGeneric(Object obj) {
        this.model = obj;
        this.isModelSet = true;
        return this;
    }

    public RequestBuilder<TranscodeType> load(Bitmap bitmap) {
        return loadGeneric(bitmap).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    public RequestBuilder<TranscodeType> load(Drawable drawable) {
        return loadGeneric(drawable).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    public RequestBuilder<TranscodeType> load(String str) {
        return loadGeneric(str);
    }

    public RequestBuilder<TranscodeType> load(Uri uri) {
        return loadGeneric(uri);
    }

    public RequestBuilder<TranscodeType> load(File file) {
        return loadGeneric(file);
    }

    public RequestBuilder<TranscodeType> load(Integer num) {
        return loadGeneric(num).apply(RequestOptions.signatureOf(ApplicationVersionSignature.obtain(this.context)));
    }

    @Deprecated
    public RequestBuilder<TranscodeType> load(URL url) {
        return loadGeneric(url);
    }

    public RequestBuilder<TranscodeType> load(byte[] bArr) {
        RequestBuilder<TranscodeType> loadGeneric = loadGeneric(bArr);
        if (!loadGeneric.requestOptions.isDiskCacheStrategySet()) {
            loadGeneric = loadGeneric.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
        }
        return !loadGeneric.requestOptions.isSkipMemoryCacheSet() ? loadGeneric.apply(RequestOptions.skipMemoryCacheOf(true)) : loadGeneric;
    }

    public RequestBuilder<TranscodeType> clone() {
        try {
            RequestBuilder<TranscodeType> requestBuilder = (RequestBuilder) super.clone();
            requestBuilder.requestOptions = requestBuilder.requestOptions.clone();
            requestBuilder.transitionOptions = requestBuilder.transitionOptions.clone();
            return requestBuilder;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public <Y extends Target<TranscodeType>> Y into(Y y) {
        return into(y, null);
    }

    /* access modifiers changed from: 0000 */
    public <Y extends Target<TranscodeType>> Y into(Y y, RequestListener<TranscodeType> requestListener2) {
        return into(y, requestListener2, getMutableOptions());
    }

    private <Y extends Target<TranscodeType>> Y into(Y y, RequestListener<TranscodeType> requestListener2, RequestOptions requestOptions2) {
        Util.assertMainThread();
        Preconditions.checkNotNull(y);
        if (this.isModelSet) {
            RequestOptions autoClone = requestOptions2.autoClone();
            Request buildRequest = buildRequest(y, requestListener2, autoClone);
            Request request = y.getRequest();
            if (!buildRequest.isEquivalentTo(request) || isSkipMemoryCacheWithCompletePreviousRequest(autoClone, request)) {
                this.requestManager.clear((Target<?>) y);
                y.setRequest(buildRequest);
                this.requestManager.track(y, buildRequest);
                return y;
            }
            buildRequest.recycle();
            if (!((Request) Preconditions.checkNotNull(request)).isRunning()) {
                request.begin();
            }
            return y;
        }
        throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    private boolean isSkipMemoryCacheWithCompletePreviousRequest(RequestOptions requestOptions2, Request request) {
        return !requestOptions2.isMemoryCacheable() && request.isComplete();
    }

    public ViewTarget<ImageView, TranscodeType> into(ImageView imageView) {
        Util.assertMainThread();
        Preconditions.checkNotNull(imageView);
        RequestOptions requestOptions2 = this.requestOptions;
        if (!requestOptions2.isTransformationSet() && requestOptions2.isTransformationAllowed() && imageView.getScaleType() != null) {
            switch (AnonymousClass2.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
                case 1:
                    requestOptions2 = requestOptions2.clone().optionalCenterCrop();
                    break;
                case 2:
                    requestOptions2 = requestOptions2.clone().optionalCenterInside();
                    break;
                case 3:
                case 4:
                case 5:
                    requestOptions2 = requestOptions2.clone().optionalFitCenter();
                    break;
                case 6:
                    requestOptions2 = requestOptions2.clone().optionalCenterInside();
                    break;
            }
        }
        return (ViewTarget) into(this.glideContext.buildImageViewTarget(imageView, this.transcodeClass), null, requestOptions2);
    }

    @Deprecated
    public FutureTarget<TranscodeType> into(int i, int i2) {
        return submit(i, i2);
    }

    public FutureTarget<TranscodeType> submit() {
        return submit(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public FutureTarget<TranscodeType> submit(int i, int i2) {
        final RequestFutureTarget requestFutureTarget = new RequestFutureTarget(this.glideContext.getMainHandler(), i, i2);
        if (Util.isOnBackgroundThread()) {
            this.glideContext.getMainHandler().post(new Runnable() {
                public void run() {
                    if (!requestFutureTarget.isCancelled()) {
                        RequestBuilder requestBuilder = RequestBuilder.this;
                        RequestFutureTarget requestFutureTarget = requestFutureTarget;
                        requestBuilder.into(requestFutureTarget, (RequestListener<TranscodeType>) requestFutureTarget);
                    }
                }
            });
        } else {
            into((Y) requestFutureTarget, (RequestListener<TranscodeType>) requestFutureTarget);
        }
        return requestFutureTarget;
    }

    public Target<TranscodeType> preload(int i, int i2) {
        return into((Y) PreloadTarget.obtain(this.requestManager, i, i2));
    }

    public Target<TranscodeType> preload() {
        return preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Deprecated
    public <Y extends Target<File>> Y downloadOnly(Y y) {
        return getDownloadOnlyRequest().into(y);
    }

    @Deprecated
    public FutureTarget<File> downloadOnly(int i, int i2) {
        return getDownloadOnlyRequest().submit(i, i2);
    }

    /* access modifiers changed from: protected */
    public RequestBuilder<File> getDownloadOnlyRequest() {
        return new RequestBuilder(File.class, this).apply(DOWNLOAD_ONLY_OPTIONS);
    }

    private Priority getThumbnailPriority(Priority priority) {
        int i = AnonymousClass2.$SwitchMap$com$bumptech$glide$Priority[priority.ordinal()];
        if (i == 1) {
            return Priority.NORMAL;
        }
        if (i == 2) {
            return Priority.HIGH;
        }
        if (i == 3 || i == 4) {
            return Priority.IMMEDIATE;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unknown priority: ");
        sb.append(this.requestOptions.getPriority());
        throw new IllegalArgumentException(sb.toString());
    }

    private Request buildRequest(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener2, RequestOptions requestOptions2) {
        return buildRequestRecursive(target, requestListener2, null, this.transitionOptions, requestOptions2.getPriority(), requestOptions2.getOverrideWidth(), requestOptions2.getOverrideHeight(), requestOptions2);
    }

    /* JADX WARNING: type inference failed for: r15v0 */
    /* JADX WARNING: type inference failed for: r3v0, types: [com.bumptech.glide.request.RequestCoordinator] */
    /* JADX WARNING: type inference failed for: r13v0, types: [com.bumptech.glide.request.RequestCoordinator] */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r15v2 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r0v4, types: [com.bumptech.glide.request.ErrorRequestCoordinator] */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r15v3 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 6 */
    private Request buildRequestRecursive(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener2, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions2, Priority priority, int i, int i2, RequestOptions requestOptions2) {
        ? r15;
        ? r3;
        if (this.errorBuilder != null) {
            ? errorRequestCoordinator = new ErrorRequestCoordinator(requestCoordinator);
            r15 = errorRequestCoordinator;
            r3 = errorRequestCoordinator;
        } else {
            r15 = 0;
            r3 = requestCoordinator;
        }
        Request buildThumbnailRequestRecursive = buildThumbnailRequestRecursive(target, requestListener2, r3, transitionOptions2, priority, i, i2, requestOptions2);
        if (r15 == 0) {
            return buildThumbnailRequestRecursive;
        }
        int overrideWidth = this.errorBuilder.requestOptions.getOverrideWidth();
        int overrideHeight = this.errorBuilder.requestOptions.getOverrideHeight();
        if (Util.isValidDimensions(i, i2) && !this.errorBuilder.requestOptions.isValidOverride()) {
            overrideWidth = requestOptions2.getOverrideWidth();
            overrideHeight = requestOptions2.getOverrideHeight();
        }
        int i3 = overrideWidth;
        int i4 = overrideHeight;
        RequestBuilder<TranscodeType> requestBuilder = this.errorBuilder;
        ErrorRequestCoordinator errorRequestCoordinator2 = r15;
        errorRequestCoordinator2.setRequests(buildThumbnailRequestRecursive, requestBuilder.buildRequestRecursive(target, requestListener2, r15, requestBuilder.transitionOptions, requestBuilder.requestOptions.getPriority(), i3, i4, this.errorBuilder.requestOptions));
        return errorRequestCoordinator2;
    }

    private Request buildThumbnailRequestRecursive(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener2, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions2, Priority priority, int i, int i2, RequestOptions requestOptions2) {
        RequestCoordinator requestCoordinator2 = requestCoordinator;
        Priority priority2 = priority;
        RequestBuilder<TranscodeType> requestBuilder = this.thumbnailBuilder;
        if (requestBuilder != null) {
            if (!this.isThumbnailBuilt) {
                TransitionOptions<?, ? super TranscodeType> transitionOptions3 = requestBuilder.isDefaultTransitionOptionsSet ? transitionOptions2 : requestBuilder.transitionOptions;
                Priority priority3 = this.thumbnailBuilder.requestOptions.isPrioritySet() ? this.thumbnailBuilder.requestOptions.getPriority() : getThumbnailPriority(priority2);
                int overrideWidth = this.thumbnailBuilder.requestOptions.getOverrideWidth();
                int overrideHeight = this.thumbnailBuilder.requestOptions.getOverrideHeight();
                if (Util.isValidDimensions(i, i2) && !this.thumbnailBuilder.requestOptions.isValidOverride()) {
                    overrideWidth = requestOptions2.getOverrideWidth();
                    overrideHeight = requestOptions2.getOverrideHeight();
                }
                int i3 = overrideWidth;
                int i4 = overrideHeight;
                ThumbnailRequestCoordinator thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(requestCoordinator2);
                Request obtainRequest = obtainRequest(target, requestListener2, requestOptions2, thumbnailRequestCoordinator, transitionOptions2, priority, i, i2);
                this.isThumbnailBuilt = true;
                RequestBuilder<TranscodeType> requestBuilder2 = this.thumbnailBuilder;
                ThumbnailRequestCoordinator thumbnailRequestCoordinator2 = thumbnailRequestCoordinator;
                Request buildRequestRecursive = requestBuilder2.buildRequestRecursive(target, requestListener2, thumbnailRequestCoordinator, transitionOptions3, priority3, i3, i4, requestBuilder2.requestOptions);
                this.isThumbnailBuilt = false;
                thumbnailRequestCoordinator2.setRequests(obtainRequest, buildRequestRecursive);
                return thumbnailRequestCoordinator2;
            }
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
        } else if (this.thumbSizeMultiplier == null) {
            return obtainRequest(target, requestListener2, requestOptions2, requestCoordinator, transitionOptions2, priority, i, i2);
        } else {
            ThumbnailRequestCoordinator thumbnailRequestCoordinator3 = new ThumbnailRequestCoordinator(requestCoordinator2);
            RequestListener<TranscodeType> requestListener3 = requestListener2;
            ThumbnailRequestCoordinator thumbnailRequestCoordinator4 = thumbnailRequestCoordinator3;
            TransitionOptions<?, ? super TranscodeType> transitionOptions4 = transitionOptions2;
            int i5 = i;
            int i6 = i2;
            thumbnailRequestCoordinator3.setRequests(obtainRequest(target, requestListener3, requestOptions2, thumbnailRequestCoordinator4, transitionOptions4, priority, i5, i6), obtainRequest(target, requestListener3, requestOptions2.clone().sizeMultiplier(this.thumbSizeMultiplier.floatValue()), thumbnailRequestCoordinator4, transitionOptions4, getThumbnailPriority(priority2), i5, i6));
            return thumbnailRequestCoordinator3;
        }
    }

    private Request obtainRequest(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener2, RequestOptions requestOptions2, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions2, Priority priority, int i, int i2) {
        Context context2 = this.context;
        GlideContext glideContext2 = this.glideContext;
        return SingleRequest.obtain(context2, glideContext2, this.model, this.transcodeClass, requestOptions2, i, i2, priority, target, requestListener2, this.requestListener, requestCoordinator, glideContext2.getEngine(), transitionOptions2.getTransitionFactory());
    }
}
