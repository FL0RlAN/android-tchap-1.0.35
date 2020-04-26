package im.vector.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\"\u0016\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"emojisPattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "containsOnlyEmojis", "", "str", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: Emoji.kt */
public final class EmojiKt {
    private static final Pattern emojisPattern = Pattern.compile("((?:[🌀-🗿]|[🤀-🧿]|[😀-🙏]|[🚀-🛿]|[☀-⛿]️?|[✀-➿]️?|Ⓜ️?|[🇦-🇿]{1,2}|[🅰🅱🅾🅿🆎🆑-🆚]️?|[#*0-9]️?⃣|[↔-↙↩-↪]️?|[⬅-⬇⬛⬜⭐⭕]️?|[⤴⤵]️?|[〰〽]️?|[㊗㊙]️?|[🈁🈂🈚🈯🈲-🈺🉐🉑]️?|[‼⁉]️?|[▪▫▶◀◻-◾]️?|[©®]️?|[™ℹ]️?|🀄️?|🃏️?|[⌚⌛⌨⏏⏩-⏳⏸-⏺]️?))");

    public static final boolean containsOnlyEmojis(String str) {
        boolean z = false;
        if (str != null) {
            CharSequence charSequence = str;
            if (!(charSequence.length() == 0)) {
                Matcher matcher = emojisPattern.matcher(charSequence);
                int i = -1;
                int i2 = -1;
                while (matcher.find()) {
                    int start = matcher.start();
                    if (i < 0) {
                        if (start > 0) {
                            return false;
                        }
                    } else if (start != i2) {
                        return false;
                    }
                    i2 = matcher.end();
                    i = start;
                }
                if (-1 != i && i2 == str.length()) {
                    z = true;
                }
            }
        }
        return z;
    }
}
