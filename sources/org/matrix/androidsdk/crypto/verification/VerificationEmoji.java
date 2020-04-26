package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.R;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u0007B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\b"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationEmoji;", "", "()V", "getEmojiForCode", "Lorg/matrix/androidsdk/crypto/verification/VerificationEmoji$EmojiRepresentation;", "code", "", "EmojiRepresentation", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: VerificationEmoji.kt */
public final class VerificationEmoji {
    public static final VerificationEmoji INSTANCE = new VerificationEmoji();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0003\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationEmoji$EmojiRepresentation;", "", "emoji", "", "nameResId", "", "(Ljava/lang/String;I)V", "getEmoji", "()Ljava/lang/String;", "getNameResId", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VerificationEmoji.kt */
    public static final class EmojiRepresentation {
        private final String emoji;
        private final int nameResId;

        public static /* synthetic */ EmojiRepresentation copy$default(EmojiRepresentation emojiRepresentation, String str, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = emojiRepresentation.emoji;
            }
            if ((i2 & 2) != 0) {
                i = emojiRepresentation.nameResId;
            }
            return emojiRepresentation.copy(str, i);
        }

        public final String component1() {
            return this.emoji;
        }

        public final int component2() {
            return this.nameResId;
        }

        public final EmojiRepresentation copy(String str, int i) {
            Intrinsics.checkParameterIsNotNull(str, KeyVerificationStart.SAS_MODE_EMOJI);
            return new EmojiRepresentation(str, i);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof EmojiRepresentation) {
                    EmojiRepresentation emojiRepresentation = (EmojiRepresentation) obj;
                    if (Intrinsics.areEqual((Object) this.emoji, (Object) emojiRepresentation.emoji)) {
                        if (this.nameResId == emojiRepresentation.nameResId) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            String str = this.emoji;
            return ((str != null ? str.hashCode() : 0) * 31) + this.nameResId;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("EmojiRepresentation(emoji=");
            sb.append(this.emoji);
            sb.append(", nameResId=");
            sb.append(this.nameResId);
            sb.append(")");
            return sb.toString();
        }

        public EmojiRepresentation(String str, int i) {
            Intrinsics.checkParameterIsNotNull(str, KeyVerificationStart.SAS_MODE_EMOJI);
            this.emoji = str;
            this.nameResId = i;
        }

        public final String getEmoji() {
            return this.emoji;
        }

        public final int getNameResId() {
            return this.nameResId;
        }
    }

    private VerificationEmoji() {
    }

    public final EmojiRepresentation getEmojiForCode(int i) {
        switch (i % 64) {
            case 0:
                return new EmojiRepresentation("🐶", R.string.verification_emoji_dog);
            case 1:
                return new EmojiRepresentation("🐱", R.string.verification_emoji_cat);
            case 2:
                return new EmojiRepresentation("🦁", R.string.verification_emoji_lion);
            case 3:
                return new EmojiRepresentation("🐎", R.string.verification_emoji_horse);
            case 4:
                return new EmojiRepresentation("🦄", R.string.verification_emoji_unicorn);
            case 5:
                return new EmojiRepresentation("🐷", R.string.verification_emoji_pig);
            case 6:
                return new EmojiRepresentation("🐘", R.string.verification_emoji_elephant);
            case 7:
                return new EmojiRepresentation("🐰", R.string.verification_emoji_rabbit);
            case 8:
                return new EmojiRepresentation("🐼", R.string.verification_emoji_panda);
            case 9:
                return new EmojiRepresentation("🐓", R.string.verification_emoji_rooster);
            case 10:
                return new EmojiRepresentation("🐧", R.string.verification_emoji_penguin);
            case 11:
                return new EmojiRepresentation("🐢", R.string.verification_emoji_turtle);
            case 12:
                return new EmojiRepresentation("🐟", R.string.verification_emoji_fish);
            case 13:
                return new EmojiRepresentation("🐙", R.string.verification_emoji_octopus);
            case 14:
                return new EmojiRepresentation("🦋", R.string.verification_emoji_butterfly);
            case 15:
                return new EmojiRepresentation("🌷", R.string.verification_emoji_flower);
            case 16:
                return new EmojiRepresentation("🌳", R.string.verification_emoji_tree);
            case 17:
                return new EmojiRepresentation("🌵", R.string.verification_emoji_cactus);
            case 18:
                return new EmojiRepresentation("🍄", R.string.verification_emoji_mushroom);
            case 19:
                return new EmojiRepresentation("🌏", R.string.verification_emoji_globe);
            case 20:
                return new EmojiRepresentation("🌙", R.string.verification_emoji_moon);
            case 21:
                return new EmojiRepresentation("☁️", R.string.verification_emoji_cloud);
            case 22:
                return new EmojiRepresentation("🔥", R.string.verification_emoji_fire);
            case 23:
                return new EmojiRepresentation("🍌", R.string.verification_emoji_banana);
            case 24:
                return new EmojiRepresentation("🍎", R.string.verification_emoji_apple);
            case 25:
                return new EmojiRepresentation("🍓", R.string.verification_emoji_strawberry);
            case 26:
                return new EmojiRepresentation("🌽", R.string.verification_emoji_corn);
            case 27:
                return new EmojiRepresentation("🍕", R.string.verification_emoji_pizza);
            case 28:
                return new EmojiRepresentation("🎂", R.string.verification_emoji_cake);
            case 29:
                return new EmojiRepresentation("❤️", R.string.verification_emoji_heart);
            case 30:
                return new EmojiRepresentation("😀", R.string.verification_emoji_smiley);
            case 31:
                return new EmojiRepresentation("🤖", R.string.verification_emoji_robot);
            case 32:
                return new EmojiRepresentation("🎩", R.string.verification_emoji_hat);
            case 33:
                return new EmojiRepresentation("👓", R.string.verification_emoji_glasses);
            case 34:
                return new EmojiRepresentation("🔧", R.string.verification_emoji_wrench);
            case 35:
                return new EmojiRepresentation("🎅", R.string.verification_emoji_santa);
            case 36:
                return new EmojiRepresentation("👍", R.string.verification_emoji_thumbsup);
            case 37:
                return new EmojiRepresentation("☂️", R.string.verification_emoji_umbrella);
            case 38:
                return new EmojiRepresentation("⌛", R.string.verification_emoji_hourglass);
            case 39:
                return new EmojiRepresentation("⏰", R.string.verification_emoji_clock);
            case 40:
                return new EmojiRepresentation("🎁", R.string.verification_emoji_gift);
            case 41:
                return new EmojiRepresentation("💡", R.string.verification_emoji_lightbulb);
            case 42:
                return new EmojiRepresentation("📕", R.string.verification_emoji_book);
            case 43:
                return new EmojiRepresentation("✏️", R.string.verification_emoji_pencil);
            case 44:
                return new EmojiRepresentation("📎", R.string.verification_emoji_paperclip);
            case 45:
                return new EmojiRepresentation("✂️", R.string.verification_emoji_scissors);
            case 46:
                return new EmojiRepresentation("🔒", R.string.verification_emoji_lock);
            case 47:
                return new EmojiRepresentation("🔑", R.string.verification_emoji_key);
            case 48:
                return new EmojiRepresentation("🔨", R.string.verification_emoji_hammer);
            case 49:
                return new EmojiRepresentation("☎️", R.string.verification_emoji_telephone);
            case 50:
                return new EmojiRepresentation("🏁", R.string.verification_emoji_flag);
            case 51:
                return new EmojiRepresentation("🚂", R.string.verification_emoji_train);
            case 52:
                return new EmojiRepresentation("🚲", R.string.verification_emoji_bicycle);
            case 53:
                return new EmojiRepresentation("✈️", R.string.verification_emoji_airplane);
            case 54:
                return new EmojiRepresentation("🚀", R.string.verification_emoji_rocket);
            case 55:
                return new EmojiRepresentation("🏆", R.string.verification_emoji_trophy);
            case 56:
                return new EmojiRepresentation("⚽", R.string.verification_emoji_ball);
            case 57:
                return new EmojiRepresentation("🎸", R.string.verification_emoji_guitar);
            case 58:
                return new EmojiRepresentation("🎺", R.string.verification_emoji_trumpet);
            case 59:
                return new EmojiRepresentation("🔔", R.string.verification_emoji_bell);
            case 60:
                return new EmojiRepresentation("⚓", R.string.verification_emoji_anchor);
            case 61:
                return new EmojiRepresentation("🎧", R.string.verification_emoji_headphone);
            case 62:
                return new EmojiRepresentation("📁", R.string.verification_emoji_folder);
            default:
                return new EmojiRepresentation("📌", R.string.verification_emoji_pin);
        }
    }
}
