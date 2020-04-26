package im.vector.fragments;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import androidx.appcompat.app.AlertDialog;
import im.vector.settings.FontScale;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$displayTextSizeSelection$1 implements OnClickListener {
    final /* synthetic */ Activity $activity;
    final /* synthetic */ AlertDialog $dialog;
    final /* synthetic */ View $v;

    VectorSettingsPreferencesFragment$displayTextSizeSelection$1(AlertDialog alertDialog, View view, Activity activity) {
        this.$dialog = alertDialog;
        this.$v = view;
        this.$activity = activity;
    }

    public final void onClick(View view) {
        this.$dialog.dismiss();
        FontScale.INSTANCE.updateFontScale(((CheckedTextView) this.$v).getText().toString());
        Activity activity = this.$activity;
        activity.startActivity(activity.getIntent());
        this.$activity.finish();
    }
}
