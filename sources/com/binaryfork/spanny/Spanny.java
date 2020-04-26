package com.binaryfork.spanny;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class Spanny extends SpannableStringBuilder {
    private int flag = 33;

    public interface GetSpan {
        Object getSpan();
    }

    public Spanny() {
        super("");
    }

    public Spanny(CharSequence charSequence) {
        super(charSequence);
    }

    public Spanny(CharSequence charSequence, Object... objArr) {
        super(charSequence);
        for (Object span : objArr) {
            setSpan(span, 0, length());
        }
    }

    public Spanny(CharSequence charSequence, Object obj) {
        super(charSequence);
        setSpan(obj, 0, charSequence.length());
    }

    public Spanny append(CharSequence charSequence, Object... objArr) {
        append(charSequence);
        for (Object span : objArr) {
            setSpan(span, length() - charSequence.length(), length());
        }
        return this;
    }

    public Spanny append(CharSequence charSequence, Object obj) {
        append(charSequence);
        setSpan(obj, length() - charSequence.length(), length());
        return this;
    }

    public Spanny append(CharSequence charSequence, ImageSpan imageSpan) {
        StringBuilder sb = new StringBuilder();
        sb.append(".");
        sb.append(charSequence);
        String sb2 = sb.toString();
        append((CharSequence) sb2);
        setSpan(imageSpan, length() - sb2.length(), (length() - sb2.length()) + 1);
        return this;
    }

    public Spanny append(CharSequence charSequence) {
        super.append(charSequence);
        return this;
    }

    @Deprecated
    public Spanny appendText(CharSequence charSequence) {
        append(charSequence);
        return this;
    }

    public void setFlag(int i) {
        this.flag = i;
    }

    private void setSpan(Object obj, int i, int i2) {
        setSpan(obj, i, i2, this.flag);
    }

    public Spanny findAndSpan(CharSequence charSequence, GetSpan getSpan) {
        int i = 0;
        while (i != -1) {
            i = toString().indexOf(charSequence.toString(), i);
            if (i != -1) {
                setSpan(getSpan.getSpan(), i, charSequence.length() + i);
                i += charSequence.length();
            }
        }
        return this;
    }

    public static SpannableString spanText(CharSequence charSequence, Object... objArr) {
        SpannableString spannableString = new SpannableString(charSequence);
        for (Object span : objArr) {
            spannableString.setSpan(span, 0, charSequence.length(), 33);
        }
        return spannableString;
    }

    public static SpannableString spanText(CharSequence charSequence, Object obj) {
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(obj, 0, charSequence.length(), 33);
        return spannableString;
    }
}
