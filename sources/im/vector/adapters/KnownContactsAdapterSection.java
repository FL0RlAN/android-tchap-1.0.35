package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;
import java.util.Comparator;
import java.util.List;

public class KnownContactsAdapterSection extends AdapterSection<ParticipantAdapterItem> {
    private String mCustomHeaderExtra;
    private boolean mIsLimited;

    public KnownContactsAdapterSection(Context context, String str, int i, int i2, int i3, int i4, List<ParticipantAdapterItem> list, Comparator<ParticipantAdapterItem> comparator) {
        super(context, str, i, i2, i3, i4, list, comparator);
    }

    public void setIsLimited(boolean z) {
        this.mIsLimited = z;
    }

    public void setCustomHeaderExtra(String str) {
        this.mCustomHeaderExtra = str;
    }

    /* access modifiers changed from: protected */
    public void updateTitle() {
        String str;
        if (getNbItems() > 0) {
            String str2 = "   ";
            if (!TextUtils.isEmpty(this.mCustomHeaderExtra)) {
                String str3 = this.mTitle;
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(this.mCustomHeaderExtra);
                sb.append(", ");
                sb.append(getNbItems());
                str = str3.concat(sb.toString());
            } else if (!this.mIsLimited) {
                String str4 = this.mTitle;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(getNbItems());
                str = str4.concat(sb2.toString());
            } else {
                String str5 = this.mTitle;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("   >");
                sb3.append(getNbItems());
                str = str5.concat(sb3.toString());
            }
        } else {
            str = this.mTitle;
        }
        formatTitle(str);
    }
}
