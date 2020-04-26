package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.verification.VerificationEmoji.EmojiRepresentation;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lorg/matrix/androidsdk/crypto/verification/VerificationEmoji$EmojiRepresentation;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: SASVerificationTransaction.kt */
final class SASVerificationTransaction$getShortCodeRepresentation$1 extends Lambda implements Function1<EmojiRepresentation, String> {
    public static final SASVerificationTransaction$getShortCodeRepresentation$1 INSTANCE = new SASVerificationTransaction$getShortCodeRepresentation$1();

    SASVerificationTransaction$getShortCodeRepresentation$1() {
        super(1);
    }

    public final String invoke(EmojiRepresentation emojiRepresentation) {
        Intrinsics.checkParameterIsNotNull(emojiRepresentation, "it");
        return emojiRepresentation.getEmoji();
    }
}
