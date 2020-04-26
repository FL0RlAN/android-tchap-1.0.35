package im.vector.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Filter.FilterListener;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import fr.gouv.tchap.a.R;
import im.vector.adapters.AutoCompletedCommandLineAdapter;
import im.vector.adapters.AutoCompletedUserAdapter;
import im.vector.util.AutoCompletionMode;
import im.vector.util.SlashCommandsParser.SlashCommand;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;

public class VectorAutoCompleteTextView extends AppCompatMultiAutoCompleteTextView {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorAutoCompleteTextView.class.getSimpleName();
    public AutoCompletedCommandLineAdapter mAdapterCommand;
    public AutoCompletedUserAdapter mAdapterUser;
    private boolean mAddColonOnFirstItem;
    private AutoCompletionMode mAutoCompletionMode;
    private ListPopupWindow mListPopupWindow;
    /* access modifiers changed from: private */
    public String mPendingFilter;
    private Field mPopupCanBeUpdatedField;

    /* renamed from: im.vector.view.VectorAutoCompleteTextView$5 reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$im$vector$util$AutoCompletionMode = new int[AutoCompletionMode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            $SwitchMap$im$vector$util$AutoCompletionMode[AutoCompletionMode.USER_MODE.ordinal()] = 1;
            $SwitchMap$im$vector$util$AutoCompletionMode[AutoCompletionMode.COMMAND_MODE.ordinal()] = 2;
        }
    }

    private static class VectorAutoCompleteTokenizer implements Tokenizer {
        static final List<Character> mAllowedTokens = Arrays.asList(new Character[]{Character.valueOf(','), Character.valueOf(';'), Character.valueOf('.'), Character.valueOf(' '), Character.valueOf(10), Character.valueOf(9)});

        private VectorAutoCompleteTokenizer() {
        }

        public int findTokenStart(CharSequence charSequence, int i) {
            int i2 = i;
            while (i2 > 0 && !mAllowedTokens.contains(Character.valueOf(charSequence.charAt(i2 - 1)))) {
                i2--;
            }
            while (i2 < i && charSequence.charAt(i2) == ' ') {
                i2++;
            }
            return i2;
        }

        public int findTokenEnd(CharSequence charSequence, int i) {
            int length = charSequence.length();
            while (i < length) {
                if (mAllowedTokens.contains(Character.valueOf(charSequence.charAt(i)))) {
                    return i;
                }
                i++;
            }
            return length;
        }

        public CharSequence terminateToken(CharSequence charSequence) {
            int length = charSequence.length();
            while (length > 0 && charSequence.charAt(length - 1) == ' ') {
                length--;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(charSequence);
            sb.append(" ");
            return sb.toString();
        }
    }

    public VectorAutoCompleteTextView(Context context) {
        super(context, null);
    }

    public VectorAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setInputType(getInputType() & (getInputType() ^ 65536));
    }

    public VectorAutoCompleteTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setInputType(getInputType() & (getInputType() ^ 65536));
    }

    public void updateAutoCompletionMode(boolean z) {
        AutoCompletionMode withText = AutoCompletionMode.Companion.getWithText(getText().toString());
        if (withText != this.mAutoCompletionMode || z) {
            this.mAutoCompletionMode = withText;
            int i = AnonymousClass5.$SwitchMap$im$vector$util$AutoCompletionMode[withText.ordinal()];
            if (i == 1) {
                setAdapter(this.mAdapterUser);
                setThreshold(3);
            } else if (i == 2) {
                setAdapter(this.mAdapterCommand);
                setThreshold(1);
            }
        }
    }

    public void initAutoCompletion(MXSession mXSession) {
        initAutoCompletion();
        buildAdapter(mXSession, mXSession.getDataHandler().getStore().getUsers(), null);
    }

    public void initAutoCompletions(final MXSession mXSession, Room room) {
        initAutoCompletion();
        final List slashCommandList = getSlashCommandList();
        buildAdapter(mXSession, new ArrayList(), slashCommandList);
        getUsersList(mXSession, room, new SimpleApiCallback<List<User>>() {
            public void onSuccess(List<User> list) {
                VectorAutoCompleteTextView.this.buildAdapter(mXSession, list, slashCommandList);
            }
        });
    }

    private void getUsersList(final MXSession mXSession, Room room, final ApiCallback<List<User>> apiCallback) {
        if (room != null) {
            room.getMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
                public void onSuccess(List<RoomMember> list) {
                    ArrayList arrayList = new ArrayList();
                    for (RoomMember userId : list) {
                        User user = mXSession.getDataHandler().getUser(userId.getUserId());
                        if (user != null) {
                            arrayList.add(user);
                        }
                    }
                    apiCallback.onSuccess(arrayList);
                }
            });
        } else {
            apiCallback.onSuccess(new ArrayList());
        }
    }

    private List<SlashCommand> getSlashCommandList() {
        return new ArrayList(Arrays.asList(SlashCommand.values()));
    }

    private void initAutoCompletion() {
        setTokenizer(new VectorAutoCompleteTokenizer());
        if (this.mPopupCanBeUpdatedField == null) {
            try {
                this.mPopupCanBeUpdatedField = AutoCompleteTextView.class.getDeclaredField("mPopupCanBeUpdated");
                this.mPopupCanBeUpdatedField.setAccessible(true);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## initAutoCompletion() : failed to retrieve mPopupCanBeUpdated ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        if (this.mListPopupWindow == null) {
            try {
                Field declaredField = AutoCompleteTextView.class.getDeclaredField("mPopup");
                declaredField.setAccessible(true);
                this.mListPopupWindow = (ListPopupWindow) declaredField.get(this);
            } catch (Exception e2) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## initAutoCompletion() : failed to retrieve mListPopupWindow ");
                sb2.append(e2.getMessage());
                Log.e(str2, sb2.toString(), e2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void buildAdapter(MXSession mXSession, Collection<User> collection, Collection<SlashCommand> collection2) {
        if (collection2 != null) {
            this.mAdapterCommand = new AutoCompletedCommandLineAdapter(getContext(), R.layout.item_command_auto_complete, mXSession, collection2);
        }
        if (collection != null) {
            this.mAdapterUser = new AutoCompletedUserAdapter(getContext(), R.layout.item_user_auto_complete, mXSession, collection);
        }
        updateAutoCompletionMode(true);
    }

    public void setProvideMatrixIdOnly(boolean z) {
        this.mAdapterUser.setProvideMatrixIdOnly(z);
    }

    /* access modifiers changed from: private */
    public void adjustPopupSize(AutoCompletedUserAdapter autoCompletedUserAdapter, AutoCompletedCommandLineAdapter autoCompletedCommandLineAdapter) {
        if (this.mListPopupWindow != null) {
            FrameLayout frameLayout = new FrameLayout(getContext());
            int i = (autoCompletedUserAdapter == null || autoCompletedCommandLineAdapter != null) ? (autoCompletedCommandLineAdapter == null || autoCompletedUserAdapter != null) ? 0 : autoCompletedCommandLineAdapter.getCount() : autoCompletedUserAdapter.getCount();
            View view = null;
            int i2 = 0;
            for (int i3 = 0; i3 < i; i3++) {
                if (autoCompletedUserAdapter != null) {
                    view = autoCompletedUserAdapter.getView(i3, view, frameLayout, false);
                }
                if (autoCompletedCommandLineAdapter != null) {
                    view = autoCompletedCommandLineAdapter.getView(i3, view, frameLayout);
                }
                view.measure(0, 0);
                i2 = Math.max(i2, view.getMeasuredWidth());
            }
            this.mListPopupWindow.setContentWidth(i2);
        }
    }

    public void setAddColonOnFirstItem(boolean z) {
        this.mAddColonOnFirstItem = z;
    }

    /* access modifiers changed from: protected */
    public void replaceText(CharSequence charSequence) {
        String obj = getText().toString();
        super.replaceText(charSequence);
        if (this.mAddColonOnFirstItem) {
            try {
                Editable text = getText();
                if (obj != null && !obj.startsWith(charSequence.toString()) && text.toString().startsWith(charSequence.toString()) && charSequence.toString().startsWith("@")) {
                    int length = charSequence.length();
                    StringBuilder sb = new StringBuilder();
                    sb.append(charSequence);
                    sb.append(":");
                    text.replace(0, length, sb.toString());
                }
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## replaceText() : failed ");
                sb2.append(e.getMessage());
                Log.e(str, sb2.toString(), e);
            }
        }
        setInputType(getInputType() & getInputType() & -65537);
        setInputType(getInputType() & (getInputType() ^ 65536));
    }

    /* access modifiers changed from: protected */
    public void performFiltering(final CharSequence charSequence, final int i, final int i2, int i3) {
        CharSequence charSequence2;
        if (this.mPopupCanBeUpdatedField == null) {
            super.performFiltering(charSequence, i, i2, i3);
            return;
        }
        StringBuilder sb = new StringBuilder();
        String str = "";
        sb.append(charSequence == null ? str : charSequence.toString());
        sb.append(i);
        sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
        sb.append(i2);
        String sb2 = sb.toString();
        if (!TextUtils.equals(sb2, this.mPendingFilter)) {
            dismissDropDown();
        }
        try {
            this.mPopupCanBeUpdatedField.setBoolean(this, true);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## performFiltering() : mPopupCanBeUpdatedField.setBoolean failed ");
            sb3.append(e.getMessage());
            Log.e(str2, sb3.toString(), e);
        }
        this.mPendingFilter = sb2;
        if (this.mAutoCompletionMode == AutoCompletionMode.USER_MODE) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    CharSequence charSequence;
                    StringBuilder sb = new StringBuilder();
                    String str = "";
                    sb.append(VectorAutoCompleteTextView.this.getText() == null ? str : VectorAutoCompleteTextView.this.getText().toString());
                    sb.append(i);
                    sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
                    sb.append(i2);
                    if (TextUtils.equals(sb.toString(), VectorAutoCompleteTextView.this.mPendingFilter)) {
                        try {
                            charSequence = charSequence.subSequence(i, i2);
                        } catch (Exception e) {
                            String access$300 = VectorAutoCompleteTextView.LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## performFiltering() failed ");
                            sb2.append(e.getMessage());
                            Log.e(access$300, sb2.toString(), e);
                            charSequence = str;
                        }
                        VectorAutoCompleteTextView.this.mAdapterUser.getFilter().filter(charSequence, new FilterListener() {
                            public void onFilterComplete(int i) {
                                VectorAutoCompleteTextView.this.adjustPopupSize(VectorAutoCompleteTextView.this.mAdapterUser, null);
                                VectorAutoCompleteTextView.this.onFilterComplete(i);
                            }
                        });
                    }
                }
            }, 700);
            return;
        }
        try {
            charSequence2 = charSequence.subSequence(i, i2);
        } catch (Exception e2) {
            String str3 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("## performFiltering() failed ");
            sb4.append(e2.getMessage());
            Log.e(str3, sb4.toString(), e2);
            charSequence2 = str;
        }
        this.mAdapterCommand.getFilter().filter(charSequence2, new FilterListener() {
            public void onFilterComplete(int i) {
                VectorAutoCompleteTextView vectorAutoCompleteTextView = VectorAutoCompleteTextView.this;
                vectorAutoCompleteTextView.adjustPopupSize(null, vectorAutoCompleteTextView.mAdapterCommand);
                VectorAutoCompleteTextView.this.onFilterComplete(i);
            }
        });
    }
}
