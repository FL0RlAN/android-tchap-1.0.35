package im.vector.features.hhs;

import android.content.Context;
import android.text.Html;
import com.binaryfork.spanny.Spanny;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0011B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J*\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\rJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lim/vector/features/hhs/ResourceLimitErrorFormatter;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "format", "", "matrixError", "Lorg/matrix/androidsdk/core/model/MatrixError;", "mode", "Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode;", "separator", "clickable", "", "uriAsLink", "", "uri", "Mode", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ResourceLimitErrorFormatter.kt */
public final class ResourceLimitErrorFormatter {
    private final Context context;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u000b\fB%\b\u0002\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b\u0001\u0002\r\u000e¨\u0006\u000f"}, d2 = {"Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode;", "", "mauErrorRes", "", "defaultErrorRes", "contactRes", "(III)V", "getContactRes", "()I", "getDefaultErrorRes", "getMauErrorRes", "Hard", "Soft", "Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode$Soft;", "Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode$Hard;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ResourceLimitErrorFormatter.kt */
    public static abstract class Mode {
        private final int contactRes;
        private final int defaultErrorRes;
        private final int mauErrorRes;

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode$Hard;", "Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: ResourceLimitErrorFormatter.kt */
        public static final class Hard extends Mode {
            public static final Hard INSTANCE = new Hard();

            private Hard() {
                super(R.string.resource_limit_hard_mau, R.string.resource_limit_hard_default, R.string.resource_limit_hard_contact, null);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode$Soft;", "Lim/vector/features/hhs/ResourceLimitErrorFormatter$Mode;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: ResourceLimitErrorFormatter.kt */
        public static final class Soft extends Mode {
            public static final Soft INSTANCE = new Soft();

            private Soft() {
                super(R.string.resource_limit_soft_mau, R.string.resource_limit_soft_default, R.string.resource_limit_soft_contact, null);
            }
        }

        private Mode(int i, int i2, int i3) {
            this.mauErrorRes = i;
            this.defaultErrorRes = i2;
            this.contactRes = i3;
        }

        public /* synthetic */ Mode(int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            this(i, i2, i3);
        }

        public final int getContactRes() {
            return this.contactRes;
        }

        public final int getDefaultErrorRes() {
            return this.defaultErrorRes;
        }

        public final int getMauErrorRes() {
            return this.mauErrorRes;
        }
    }

    public ResourceLimitErrorFormatter(Context context2) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        this.context = context2;
    }

    public static /* synthetic */ CharSequence format$default(ResourceLimitErrorFormatter resourceLimitErrorFormatter, MatrixError matrixError, Mode mode, CharSequence charSequence, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            charSequence = " ";
        }
        if ((i & 8) != 0) {
            z = false;
        }
        return resourceLimitErrorFormatter.format(matrixError, mode, charSequence, z);
    }

    public final CharSequence format(MatrixError matrixError, Mode mode, CharSequence charSequence, boolean z) {
        String str;
        CharSequence charSequence2;
        Intrinsics.checkParameterIsNotNull(matrixError, "matrixError");
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        Intrinsics.checkParameterIsNotNull(charSequence, "separator");
        if (Intrinsics.areEqual((Object) MatrixError.LIMIT_TYPE_MAU, (Object) matrixError.limitType)) {
            str = this.context.getString(mode.getMauErrorRes());
        } else {
            str = this.context.getString(mode.getDefaultErrorRes());
        }
        if (!z || matrixError.adminUri == null) {
            String string = this.context.getString(R.string.resource_limit_contact_admin);
            charSequence2 = this.context.getString(mode.getContactRes(), new Object[]{string});
        } else {
            String str2 = matrixError.adminUri;
            if (str2 == null) {
                Intrinsics.throwNpe();
            }
            Intrinsics.checkExpressionValueIsNotNull(str2, "matrixError.adminUri!!");
            String uriAsLink = uriAsLink(str2);
            charSequence2 = Html.fromHtml(this.context.getString(mode.getContactRes(), new Object[]{uriAsLink}));
        }
        Spanny append = new Spanny(str).append(charSequence).append(charSequence2);
        Intrinsics.checkExpressionValueIsNotNull(append, "Spanny(error)\n          …         .append(contact)");
        return append;
    }

    private final String uriAsLink(String str) {
        String string = this.context.getString(R.string.resource_limit_contact_admin);
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"");
        sb.append(str);
        sb.append("\">");
        sb.append(string);
        sb.append("</a>");
        return sb.toString();
    }
}
