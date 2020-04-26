package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import im.vector.settings.VectorLocale;
import im.vector.util.SlashCommandsParser.SlashCommand;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.matrix.androidsdk.MXSession;

public class AutoCompletedCommandLineAdapter extends ArrayAdapter<String> {
    /* access modifiers changed from: private */
    public List<SlashCommand> mCommandLines = new ArrayList();
    private final Context mContext;
    private Filter mFilter;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResourceId;
    private final MXSession mSession;

    private class AutoCompletedCommandFilter extends Filter {
        private AutoCompletedCommandFilter() {
        }

        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            ArrayList arrayList = new ArrayList();
            if (!TextUtils.isEmpty(charSequence)) {
                String lowerCase = charSequence.toString().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
                if (lowerCase.startsWith("/")) {
                    for (SlashCommand slashCommand : AutoCompletedCommandLineAdapter.this.mCommandLines) {
                        if (slashCommand.getCommand() != null && slashCommand.getCommand().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).startsWith(lowerCase)) {
                            arrayList.add(slashCommand.getCommand());
                        }
                    }
                }
            }
            filterResults.values = arrayList;
            filterResults.count = arrayList.size();
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            AutoCompletedCommandLineAdapter.this.clear();
            AutoCompletedCommandLineAdapter.this.addAll((List) filterResults.values);
            if (filterResults.count > 0) {
                AutoCompletedCommandLineAdapter.this.notifyDataSetChanged();
            } else {
                AutoCompletedCommandLineAdapter.this.notifyDataSetInvalidated();
            }
        }

        public CharSequence convertResultToString(Object obj) {
            return (String) obj;
        }
    }

    static class CommandViewHolder {
        @BindView(2131296657)
        TextView tvCommandDescription;
        @BindView(2131296658)
        TextView tvCommandName;
        @BindView(2131296659)
        TextView tvCommandParameter;

        public CommandViewHolder(View view) {
            ButterKnife.bind((Object) this, view);
        }
    }

    public class CommandViewHolder_ViewBinding implements Unbinder {
        private CommandViewHolder target;

        public CommandViewHolder_ViewBinding(CommandViewHolder commandViewHolder, View view) {
            this.target = commandViewHolder;
            commandViewHolder.tvCommandName = (TextView) Utils.findRequiredViewAsType(view, R.id.item_command_auto_complete_name, "field 'tvCommandName'", TextView.class);
            commandViewHolder.tvCommandParameter = (TextView) Utils.findRequiredViewAsType(view, R.id.item_command_auto_complete_parameter, "field 'tvCommandParameter'", TextView.class);
            commandViewHolder.tvCommandDescription = (TextView) Utils.findRequiredViewAsType(view, R.id.item_command_auto_complete_description, "field 'tvCommandDescription'", TextView.class);
        }

        public void unbind() {
            CommandViewHolder commandViewHolder = this.target;
            if (commandViewHolder != null) {
                this.target = null;
                commandViewHolder.tvCommandName = null;
                commandViewHolder.tvCommandParameter = null;
                commandViewHolder.tvCommandDescription = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    public AutoCompletedCommandLineAdapter(Context context, int i, MXSession mXSession, Collection<SlashCommand> collection) {
        super(context, i);
        this.mContext = context;
        this.mLayoutResourceId = i;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        this.mSession = mXSession;
        this.mCommandLines = new ArrayList(collection);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        CommandViewHolder commandViewHolder;
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mLayoutResourceId, viewGroup, false);
            commandViewHolder = new CommandViewHolder(view);
            view.setTag(commandViewHolder);
        } else {
            commandViewHolder = (CommandViewHolder) view.getTag();
        }
        SlashCommand slashCommand = SlashCommand.get((String) getItem(i));
        if (slashCommand != null) {
            commandViewHolder.tvCommandName.setText(slashCommand.getCommand());
            commandViewHolder.tvCommandParameter.setText(slashCommand.getParam());
            commandViewHolder.tvCommandDescription.setText(slashCommand.getDescription());
        }
        return view;
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new AutoCompletedCommandFilter();
        }
        return this.mFilter;
    }
}
