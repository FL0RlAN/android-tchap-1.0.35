package org.matrix.androidsdk.core.listeners;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0006J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/core/listeners/StepProgressListener;", "", "onStepProgress", "", "step", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step;", "Step", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: StepProgressListener.kt */
public interface StepProgressListener {

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0002¢\u0006\u0002\u0010\u0002\u0001\u0003\u0006\u0007\b¨\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step;", "", "()V", "ComputingKey", "DownloadingKey", "ImportingKey", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step$ComputingKey;", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step$DownloadingKey;", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step$ImportingKey;", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: StepProgressListener.kt */
    public static abstract class Step {

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÖ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step$ComputingKey;", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step;", "progress", "", "total", "(II)V", "getProgress", "()I", "getTotal", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
        /* compiled from: StepProgressListener.kt */
        public static final class ComputingKey extends Step {
            private final int progress;
            private final int total;

            public static /* synthetic */ ComputingKey copy$default(ComputingKey computingKey, int i, int i2, int i3, Object obj) {
                if ((i3 & 1) != 0) {
                    i = computingKey.progress;
                }
                if ((i3 & 2) != 0) {
                    i2 = computingKey.total;
                }
                return computingKey.copy(i, i2);
            }

            public final int component1() {
                return this.progress;
            }

            public final int component2() {
                return this.total;
            }

            public final ComputingKey copy(int i, int i2) {
                return new ComputingKey(i, i2);
            }

            public boolean equals(Object obj) {
                if (this != obj) {
                    if (obj instanceof ComputingKey) {
                        ComputingKey computingKey = (ComputingKey) obj;
                        if (this.progress == computingKey.progress) {
                            if (this.total == computingKey.total) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
                return true;
            }

            public int hashCode() {
                return (this.progress * 31) + this.total;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("ComputingKey(progress=");
                sb.append(this.progress);
                sb.append(", total=");
                sb.append(this.total);
                sb.append(")");
                return sb.toString();
            }

            public ComputingKey(int i, int i2) {
                super(null);
                this.progress = i;
                this.total = i2;
            }

            public final int getProgress() {
                return this.progress;
            }

            public final int getTotal() {
                return this.total;
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step$DownloadingKey;", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step;", "()V", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
        /* compiled from: StepProgressListener.kt */
        public static final class DownloadingKey extends Step {
            public static final DownloadingKey INSTANCE = new DownloadingKey();

            private DownloadingKey() {
                super(null);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÖ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step$ImportingKey;", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener$Step;", "progress", "", "total", "(II)V", "getProgress", "()I", "getTotal", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
        /* compiled from: StepProgressListener.kt */
        public static final class ImportingKey extends Step {
            private final int progress;
            private final int total;

            public static /* synthetic */ ImportingKey copy$default(ImportingKey importingKey, int i, int i2, int i3, Object obj) {
                if ((i3 & 1) != 0) {
                    i = importingKey.progress;
                }
                if ((i3 & 2) != 0) {
                    i2 = importingKey.total;
                }
                return importingKey.copy(i, i2);
            }

            public final int component1() {
                return this.progress;
            }

            public final int component2() {
                return this.total;
            }

            public final ImportingKey copy(int i, int i2) {
                return new ImportingKey(i, i2);
            }

            public boolean equals(Object obj) {
                if (this != obj) {
                    if (obj instanceof ImportingKey) {
                        ImportingKey importingKey = (ImportingKey) obj;
                        if (this.progress == importingKey.progress) {
                            if (this.total == importingKey.total) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
                return true;
            }

            public int hashCode() {
                return (this.progress * 31) + this.total;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("ImportingKey(progress=");
                sb.append(this.progress);
                sb.append(", total=");
                sb.append(this.total);
                sb.append(")");
                return sb.toString();
            }

            public ImportingKey(int i, int i2) {
                super(null);
                this.progress = i;
                this.total = i2;
            }

            public final int getProgress() {
                return this.progress;
            }

            public final int getTotal() {
                return this.total;
            }
        }

        private Step() {
        }

        public /* synthetic */ Step(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    void onStepProgress(Step step);
}
