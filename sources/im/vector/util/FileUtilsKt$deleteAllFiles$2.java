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
final class FileUtilsKt$deleteAllFiles$2 extends FunctionReference implements Function1<File, Boolean> {
    public static final FileUtilsKt$deleteAllFiles$2 INSTANCE = new FileUtilsKt$deleteAllFiles$2();

    FileUtilsKt$deleteAllFiles$2() {
        super(1);
    }

    public final String getName() {
        return "deleteAction";
    }

    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinPackage(FileUtilsKt.class, "vector_appAgentWithoutvoipWithpinningMatrixorg");
    }

    public final String getSignature() {
        return "deleteAction(Ljava/io/File;)Z";
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return Boolean.valueOf(invoke((File) obj));
    }

    public final boolean invoke(File file) {
        Intrinsics.checkParameterIsNotNull(file, "p1");
        return 
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0009: RETURN  (wrap: boolean
              0x0005: INVOKE  (r2v1 boolean) = (r2v0 'file' java.io.File) im.vector.util.FileUtilsKt.access$deleteAction(java.io.File):boolean type: STATIC) in method: im.vector.util.FileUtilsKt$deleteAllFiles$2.invoke(java.io.File):boolean, dex: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:213)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:210)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:203)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:316)
            	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:262)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:225)
            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
            	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:76)
            	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
            	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:32)
            	at jadx.core.codegen.CodeGen.generate(CodeGen.java:20)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0005: INVOKE  (r2v1 boolean) = (r2v0 'file' java.io.File) im.vector.util.FileUtilsKt.access$deleteAction(java.io.File):boolean type: STATIC in method: im.vector.util.FileUtilsKt$deleteAllFiles$2.invoke(java.io.File):boolean, dex: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:105)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:303)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:239)
            	... 19 more
            Caused by: java.util.ConcurrentModificationException
            	at java.util.ArrayList.removeIf(Unknown Source)
            	at jadx.core.dex.instructions.args.SSAVar.removeUse(SSAVar.java:86)
            	at jadx.core.utils.InsnRemover.unbindArgUsage(InsnRemover.java:90)
            	at jadx.core.dex.nodes.InsnNode.replaceArg(InsnNode.java:130)
            	at jadx.core.dex.nodes.InsnNode.replaceArg(InsnNode.java:134)
            	at jadx.core.codegen.InsnGen.inlineMethod(InsnGen.java:892)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:669)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:357)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:223)
            	... 22 more
            */
        /*
            this = this;
            java.lang.String r0 = "p1"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r0)
            boolean r2 = im.vector.util.FileUtilsKt.deleteAction(r2)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: im.vector.util.FileUtilsKt$deleteAllFiles$2.invoke(java.io.File):boolean");
    }
}
