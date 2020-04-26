package im.vector.util;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReference;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u0015\u0010\u0002\u001a\u00110\u0003¢\u0006\f\b\u0004\u0012\b\b\u0005\u0012\u0004\b\b(\u0006¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "p1", "Ljava/io/File;", "Lkotlin/ParameterName;", "name", "file", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: FileUtils.kt */
final class FileUtilsKt$lsFiles$1 extends FunctionReference implements Function1<File, Boolean> {
    public static final FileUtilsKt$lsFiles$1 INSTANCE = new FileUtilsKt$lsFiles$1();

    FileUtilsKt$lsFiles$1() {
        super(1);
    }

    public final String getName() {
        return "logAction";
    }

    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinPackage(FileUtilsKt.class, "vector_appAgentWithoutvoipWithpinningMatrixorg");
    }

    public final String getSignature() {
        return "logAction(Ljava/io/File;)Z";
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return Boolean.valueOf(invoke((File) obj));
    }

    public final boolean invoke(File file) {
        Intrinsics.checkParameterIsNotNull(file, "p1");
        return FileUtilsKt.logAction(file);
    }
}
