package im.vector.dialogs;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public final class DialogListItemHolder_ViewBinding implements Unbinder {
    private DialogListItemHolder target;

    public DialogListItemHolder_ViewBinding(DialogListItemHolder dialogListItemHolder, View view) {
        this.target = dialogListItemHolder;
        dialogListItemHolder.icon = (ImageView) Utils.findRequiredViewAsType(view, R.id.adapter_item_dialog_icon, "field 'icon'", ImageView.class);
        dialogListItemHolder.text = (TextView) Utils.findRequiredViewAsType(view, R.id.adapter_item_dialog_text, "field 'text'", TextView.class);
    }

    public void unbind() {
        DialogListItemHolder dialogListItemHolder = this.target;
        if (dialogListItemHolder != null) {
            this.target = null;
            dialogListItemHolder.icon = null;
            dialogListItemHolder.text = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
