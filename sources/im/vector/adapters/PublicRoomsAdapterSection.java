package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;
import java.util.Comparator;
import java.util.List;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

public class PublicRoomsAdapterSection extends AdapterSection<PublicRoom> {
    private int mEstimatedPublicRoomsCount = -1;
    private boolean mHasMoreResults;

    public PublicRoomsAdapterSection(Context context, String str, int i, int i2, int i3, int i4, List<PublicRoom> list, Comparator<PublicRoom> comparator) {
        super(context, str, i, i2, i3, i4, list, comparator);
    }

    /* access modifiers changed from: protected */
    public void updateTitle() {
        String str;
        String str2 = "   ";
        if (TextUtils.isEmpty(this.mCurrentFilterPattern)) {
            if (this.mEstimatedPublicRoomsCount > 0) {
                String str3 = this.mTitle;
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(this.mEstimatedPublicRoomsCount);
                str = str3.concat(sb.toString());
            } else {
                str = this.mTitle;
            }
        } else if (getNbItems() <= 0) {
            str = this.mTitle;
        } else if (this.mHasMoreResults) {
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
        formatTitle(str);
    }

    public void setEstimatedPublicRoomsCount(int i) {
        this.mEstimatedPublicRoomsCount = i;
        this.mHasMoreResults = false;
    }

    public void setHasMoreResults(boolean z) {
        this.mHasMoreResults = z;
    }
}
