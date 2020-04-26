package im.vector.ui.themes;

import im.vector.R;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u000b\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015B%\b\u0002\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b\u0001\u000b\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f ¨\u0006!"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes;", "", "dark", "", "black", "status", "(III)V", "getBlack", "()I", "getDark", "getStatus", "Call", "Default", "Directory", "Group", "Home", "Lock", "Login", "NoActionBar", "NoActionBarFullscreen", "Picker", "Search", "Lim/vector/ui/themes/ActivityOtherThemes$Default;", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBarFullscreen;", "Lim/vector/ui/themes/ActivityOtherThemes$Home;", "Lim/vector/ui/themes/ActivityOtherThemes$Group;", "Lim/vector/ui/themes/ActivityOtherThemes$Picker;", "Lim/vector/ui/themes/ActivityOtherThemes$Lock;", "Lim/vector/ui/themes/ActivityOtherThemes$Search;", "Lim/vector/ui/themes/ActivityOtherThemes$Call;", "Lim/vector/ui/themes/ActivityOtherThemes$Login;", "Lim/vector/ui/themes/ActivityOtherThemes$Directory;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ActivityOtherThemes.kt */
public abstract class ActivityOtherThemes {
    private final int black;
    private final int dark;
    private final int status;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Call;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Call extends ActivityOtherThemes {
        public static final Call INSTANCE = new Call();

        private Call() {
            super(R.style.CallActivityTheme_Dark, R.style.CallActivityTheme_Black, R.style.CallActivityTheme_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Default;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Default extends ActivityOtherThemes {
        public static final Default INSTANCE = new Default();

        private Default() {
            super(R.style.AppTheme_Dark, R.style.AppTheme_Black, R.style.AppTheme_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Directory;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Directory extends ActivityOtherThemes {
        public static final Directory INSTANCE = new Directory();

        private Directory() {
            super(R.style.DirectoryPickerTheme_Dark, R.style.DirectoryPickerTheme_Black, R.style.DirectoryPickerTheme_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Group;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Group extends ActivityOtherThemes {
        public static final Group INSTANCE = new Group();

        private Group() {
            super(R.style.GroupAppTheme_Dark, R.style.GroupAppTheme_Black, R.style.GroupAppTheme_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Home;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Home extends ActivityOtherThemes {
        public static final Home INSTANCE = new Home();

        private Home() {
            super(R.style.HomeActivityTheme_Dark, R.style.HomeActivityTheme_Black, R.style.HomeActivityTheme_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Lock;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Lock extends ActivityOtherThemes {
        public static final Lock INSTANCE = new Lock();

        private Lock() {
            super(R.style.Theme_Vector_Lock_Dark, R.style.Theme_Vector_Lock_Light, R.style.Theme_Vector_Lock_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Login;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Login extends ActivityOtherThemes {
        public static final Login INSTANCE = new Login();

        private Login() {
            super(R.style.LoginAppTheme_Dark, R.style.LoginAppTheme_Black, R.style.LoginAppTheme_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class NoActionBar extends ActivityOtherThemes {
        public static final NoActionBar INSTANCE = new NoActionBar();

        private NoActionBar() {
            super(R.style.AppTheme_NoActionBar_Dark, R.style.AppTheme_NoActionBar_Black, R.style.AppTheme_NoActionBar_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$NoActionBarFullscreen;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class NoActionBarFullscreen extends ActivityOtherThemes {
        public static final NoActionBarFullscreen INSTANCE = new NoActionBarFullscreen();

        private NoActionBarFullscreen() {
            super(R.style.AppTheme_NoActionBar_FullScreen_Dark, R.style.AppTheme_NoActionBar_FullScreen_Black, R.style.AppTheme_NoActionBar_FullScreen_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Picker;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Picker extends ActivityOtherThemes {
        public static final Picker INSTANCE = new Picker();

        private Picker() {
            super(R.style.CountryPickerTheme_Dark, R.style.CountryPickerTheme_Black, R.style.CountryPickerTheme_Status, null);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/ui/themes/ActivityOtherThemes$Search;", "Lim/vector/ui/themes/ActivityOtherThemes;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ActivityOtherThemes.kt */
    public static final class Search extends ActivityOtherThemes {
        public static final Search INSTANCE = new Search();

        private Search() {
            super(R.style.SearchesAppTheme_Dark, R.style.SearchesAppTheme_Black, R.style.SearchesAppTheme_Status, null);
        }
    }

    private ActivityOtherThemes(int i, int i2, int i3) {
        this.dark = i;
        this.black = i2;
        this.status = i3;
    }

    public /* synthetic */ ActivityOtherThemes(int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, i3);
    }

    public final int getDark() {
        return this.dark;
    }

    public final int getBlack() {
        return this.black;
    }

    public final int getStatus() {
        return this.status;
    }
}
