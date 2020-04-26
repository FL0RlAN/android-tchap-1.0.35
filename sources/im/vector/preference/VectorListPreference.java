package im.vector.preference;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import fr.gouv.tchap.a.R;

public class VectorListPreference extends ListPreference {
    private boolean mIsWarningIconVisible = false;
    /* access modifiers changed from: private */
    public OnPreferenceWarningIconClickListener mWarningIconClickListener;
    private View mWarningIconView;

    public interface OnPreferenceWarningIconClickListener {
        void onWarningIconClick(Preference preference);
    }

    public VectorListPreference(Context context) {
        super(context);
    }

    public VectorListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VectorListPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public View onCreateView(ViewGroup viewGroup) {
        setWidgetLayoutResource(R.layout.vector_settings_list_preference_with_warning);
        View onCreateView = super.onCreateView(viewGroup);
        this.mWarningIconView = onCreateView.findViewById(R.id.list_preference_warning_icon);
        this.mWarningIconView.setVisibility(this.mIsWarningIconVisible ? 0 : 8);
        this.mWarningIconView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorListPreference.this.mWarningIconClickListener != null) {
                    VectorListPreference.this.mWarningIconClickListener.onWarningIconClick(VectorListPreference.this);
                }
            }
        });
        return onCreateView;
    }

    public void setOnPreferenceWarningIconClickListener(OnPreferenceWarningIconClickListener onPreferenceWarningIconClickListener) {
        this.mWarningIconClickListener = onPreferenceWarningIconClickListener;
    }

    public void setWarningIconVisible(boolean z) {
        this.mIsWarningIconVisible = z;
        View view = this.mWarningIconView;
        if (view != null) {
            view.setVisibility(this.mIsWarningIconVisible ? 0 : 8);
        }
    }
}
