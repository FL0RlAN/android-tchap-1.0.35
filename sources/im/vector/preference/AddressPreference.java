package im.vector.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import fr.gouv.tchap.a.R;

public class AddressPreference extends VectorCustomActionEditTextPreference {
    private boolean mIsMainIconVisible = false;
    private ImageView mMainAddressIconView;

    public AddressPreference(Context context) {
        super(context);
    }

    public AddressPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AddressPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public View onCreateView(ViewGroup viewGroup) {
        setWidgetLayoutResource(R.layout.vector_settings_address_preference);
        View onCreateView = super.onCreateView(viewGroup);
        this.mMainAddressIconView = (ImageView) onCreateView.findViewById(R.id.main_address_icon_view);
        this.mMainAddressIconView.setVisibility(this.mIsMainIconVisible ? 0 : 8);
        return onCreateView;
    }

    public void setMainIconVisible(boolean z) {
        this.mIsMainIconVisible = z;
        ImageView imageView = this.mMainAddressIconView;
        if (imageView != null) {
            imageView.setVisibility(this.mIsMainIconVisible ? 0 : 8);
        }
    }

    public View getMainIconView() {
        return this.mMainAddressIconView;
    }
}
