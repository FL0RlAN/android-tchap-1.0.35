package timber.log;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Timber {
    private static final List<Tree> FOREST = new ArrayList();
    private static final Tree[] TREE_ARRAY_EMPTY = new Tree[0];
    private static final Tree TREE_OF_SOULS = new Tree() {
        public void v(String str, Object... objArr) {
            for (Tree v : Timber.forestAsArray) {
                v.v(str, objArr);
            }
        }

        public void v(Throwable th, String str, Object... objArr) {
            for (Tree v : Timber.forestAsArray) {
                v.v(th, str, objArr);
            }
        }

        public void v(Throwable th) {
            for (Tree v : Timber.forestAsArray) {
                v.v(th);
            }
        }

        public void d(String str, Object... objArr) {
            for (Tree d : Timber.forestAsArray) {
                d.d(str, objArr);
            }
        }

        public void d(Throwable th, String str, Object... objArr) {
            for (Tree d : Timber.forestAsArray) {
                d.d(th, str, objArr);
            }
        }

        public void d(Throwable th) {
            for (Tree d : Timber.forestAsArray) {
                d.d(th);
            }
        }

        public void i(String str, Object... objArr) {
            for (Tree i : Timber.forestAsArray) {
                i.i(str, objArr);
            }
        }

        public void i(Throwable th, String str, Object... objArr) {
            for (Tree i : Timber.forestAsArray) {
                i.i(th, str, objArr);
            }
        }

        public void i(Throwable th) {
            for (Tree i : Timber.forestAsArray) {
                i.i(th);
            }
        }

        public void w(String str, Object... objArr) {
            for (Tree w : Timber.forestAsArray) {
                w.w(str, objArr);
            }
        }

        public void w(Throwable th, String str, Object... objArr) {
            for (Tree w : Timber.forestAsArray) {
                w.w(th, str, objArr);
            }
        }

        public void w(Throwable th) {
            for (Tree w : Timber.forestAsArray) {
                w.w(th);
            }
        }

        public void e(String str, Object... objArr) {
            for (Tree e : Timber.forestAsArray) {
                e.e(str, objArr);
            }
        }

        public void e(Throwable th, String str, Object... objArr) {
            for (Tree e : Timber.forestAsArray) {
                e.e(th, str, objArr);
            }
        }

        public void e(Throwable th) {
            for (Tree e : Timber.forestAsArray) {
                e.e(th);
            }
        }

        public void wtf(String str, Object... objArr) {
            for (Tree wtf : Timber.forestAsArray) {
                wtf.wtf(str, objArr);
            }
        }

        public void wtf(Throwable th, String str, Object... objArr) {
            for (Tree wtf : Timber.forestAsArray) {
                wtf.wtf(th, str, objArr);
            }
        }

        public void wtf(Throwable th) {
            for (Tree wtf : Timber.forestAsArray) {
                wtf.wtf(th);
            }
        }

        public void log(int i, String str, Object... objArr) {
            for (Tree log : Timber.forestAsArray) {
                log.log(i, str, objArr);
            }
        }

        public void log(int i, Throwable th, String str, Object... objArr) {
            for (Tree log : Timber.forestAsArray) {
                log.log(i, th, str, objArr);
            }
        }

        public void log(int i, Throwable th) {
            for (Tree log : Timber.forestAsArray) {
                log.log(i, th);
            }
        }

        /* access modifiers changed from: protected */
        public void log(int i, String str, String str2, Throwable th) {
            throw new AssertionError("Missing override for log method.");
        }
    };
    static volatile Tree[] forestAsArray = TREE_ARRAY_EMPTY;

    public static class DebugTree extends Tree {
        private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
        private static final int CALL_STACK_INDEX = 5;
        private static final int MAX_LOG_LENGTH = 4000;
        private static final int MAX_TAG_LENGTH = 23;

        /* access modifiers changed from: protected */
        public String createStackElementTag(StackTraceElement stackTraceElement) {
            String className = stackTraceElement.getClassName();
            Matcher matcher = ANONYMOUS_CLASS.matcher(className);
            if (matcher.find()) {
                className = matcher.replaceAll("");
            }
            String substring = className.substring(className.lastIndexOf(46) + 1);
            return substring.length() > 23 ? substring.substring(0, 23) : substring;
        }

        /* access modifiers changed from: 0000 */
        public final String getTag() {
            String tag = super.getTag();
            if (tag != null) {
                return tag;
            }
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length > 5) {
                return createStackElementTag(stackTrace[5]);
            }
            throw new IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }

        /* access modifiers changed from: protected */
        public void log(int i, String str, String str2, Throwable th) {
            int min;
            if (str2.length() < MAX_LOG_LENGTH) {
                if (i == 7) {
                    Log.wtf(str, str2);
                } else {
                    Log.println(i, str, str2);
                }
                return;
            }
            int i2 = 0;
            int length = str2.length();
            while (i2 < length) {
                int indexOf = str2.indexOf(10, i2);
                if (indexOf == -1) {
                    indexOf = length;
                }
                while (true) {
                    min = Math.min(indexOf, i2 + MAX_LOG_LENGTH);
                    String substring = str2.substring(i2, min);
                    if (i == 7) {
                        Log.wtf(str, substring);
                    } else {
                        Log.println(i, str, substring);
                    }
                    if (min >= indexOf) {
                        break;
                    }
                    i2 = min;
                }
                i2 = min + 1;
            }
        }
    }

    public static abstract class Tree {
        final ThreadLocal<String> explicitTag = new ThreadLocal<>();

        /* access modifiers changed from: protected */
        @Deprecated
        public boolean isLoggable(int i) {
            return true;
        }

        /* access modifiers changed from: protected */
        public abstract void log(int i, String str, String str2, Throwable th);

        /* access modifiers changed from: 0000 */
        public String getTag() {
            String str = (String) this.explicitTag.get();
            if (str != null) {
                this.explicitTag.remove();
            }
            return str;
        }

        public void v(String str, Object... objArr) {
            prepareLog(2, null, str, objArr);
        }

        public void v(Throwable th, String str, Object... objArr) {
            prepareLog(2, th, str, objArr);
        }

        public void v(Throwable th) {
            prepareLog(2, th, null, new Object[0]);
        }

        public void d(String str, Object... objArr) {
            prepareLog(3, null, str, objArr);
        }

        public void d(Throwable th, String str, Object... objArr) {
            prepareLog(3, th, str, objArr);
        }

        public void d(Throwable th) {
            prepareLog(3, th, null, new Object[0]);
        }

        public void i(String str, Object... objArr) {
            prepareLog(4, null, str, objArr);
        }

        public void i(Throwable th, String str, Object... objArr) {
            prepareLog(4, th, str, objArr);
        }

        public void i(Throwable th) {
            prepareLog(4, th, null, new Object[0]);
        }

        public void w(String str, Object... objArr) {
            prepareLog(5, null, str, objArr);
        }

        public void w(Throwable th, String str, Object... objArr) {
            prepareLog(5, th, str, objArr);
        }

        public void w(Throwable th) {
            prepareLog(5, th, null, new Object[0]);
        }

        public void e(String str, Object... objArr) {
            prepareLog(6, null, str, objArr);
        }

        public void e(Throwable th, String str, Object... objArr) {
            prepareLog(6, th, str, objArr);
        }

        public void e(Throwable th) {
            prepareLog(6, th, null, new Object[0]);
        }

        public void wtf(String str, Object... objArr) {
            prepareLog(7, null, str, objArr);
        }

        public void wtf(Throwable th, String str, Object... objArr) {
            prepareLog(7, th, str, objArr);
        }

        public void wtf(Throwable th) {
            prepareLog(7, th, null, new Object[0]);
        }

        public void log(int i, String str, Object... objArr) {
            prepareLog(i, null, str, objArr);
        }

        public void log(int i, Throwable th, String str, Object... objArr) {
            prepareLog(i, th, str, objArr);
        }

        public void log(int i, Throwable th) {
            prepareLog(i, th, null, new Object[0]);
        }

        /* access modifiers changed from: protected */
        public boolean isLoggable(String str, int i) {
            return isLoggable(i);
        }

        private void prepareLog(int i, Throwable th, String str, Object... objArr) {
            String tag = getTag();
            if (isLoggable(tag, i)) {
                if (str != null && str.length() == 0) {
                    str = null;
                }
                if (str != null) {
                    if (objArr.length > 0) {
                        str = formatMessage(str, objArr);
                    }
                    if (th != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append("\n");
                        sb.append(getStackTraceString(th));
                        str = sb.toString();
                    }
                } else if (th != null) {
                    str = getStackTraceString(th);
                } else {
                    return;
                }
                log(i, tag, str, th);
            }
        }

        /* access modifiers changed from: protected */
        public String formatMessage(String str, Object[] objArr) {
            return String.format(str, objArr);
        }

        private String getStackTraceString(Throwable th) {
            StringWriter stringWriter = new StringWriter(256);
            PrintWriter printWriter = new PrintWriter(stringWriter, false);
            th.printStackTrace(printWriter);
            printWriter.flush();
            return stringWriter.toString();
        }
    }

    public static void v(String str, Object... objArr) {
        TREE_OF_SOULS.v(str, objArr);
    }

    public static void v(Throwable th, String str, Object... objArr) {
        TREE_OF_SOULS.v(th, str, objArr);
    }

    public static void v(Throwable th) {
        TREE_OF_SOULS.v(th);
    }

    public static void d(String str, Object... objArr) {
        TREE_OF_SOULS.d(str, objArr);
    }

    public static void d(Throwable th, String str, Object... objArr) {
        TREE_OF_SOULS.d(th, str, objArr);
    }

    public static void d(Throwable th) {
        TREE_OF_SOULS.d(th);
    }

    public static void i(String str, Object... objArr) {
        TREE_OF_SOULS.i(str, objArr);
    }

    public static void i(Throwable th, String str, Object... objArr) {
        TREE_OF_SOULS.i(th, str, objArr);
    }

    public static void i(Throwable th) {
        TREE_OF_SOULS.i(th);
    }

    public static void w(String str, Object... objArr) {
        TREE_OF_SOULS.w(str, objArr);
    }

    public static void w(Throwable th, String str, Object... objArr) {
        TREE_OF_SOULS.w(th, str, objArr);
    }

    public static void w(Throwable th) {
        TREE_OF_SOULS.w(th);
    }

    public static void e(String str, Object... objArr) {
        TREE_OF_SOULS.e(str, objArr);
    }

    public static void e(Throwable th, String str, Object... objArr) {
        TREE_OF_SOULS.e(th, str, objArr);
    }

    public static void e(Throwable th) {
        TREE_OF_SOULS.e(th);
    }

    public static void wtf(String str, Object... objArr) {
        TREE_OF_SOULS.wtf(str, objArr);
    }

    public static void wtf(Throwable th, String str, Object... objArr) {
        TREE_OF_SOULS.wtf(th, str, objArr);
    }

    public static void wtf(Throwable th) {
        TREE_OF_SOULS.wtf(th);
    }

    public static void log(int i, String str, Object... objArr) {
        TREE_OF_SOULS.log(i, str, objArr);
    }

    public static void log(int i, Throwable th, String str, Object... objArr) {
        TREE_OF_SOULS.log(i, th, str, objArr);
    }

    public static void log(int i, Throwable th) {
        TREE_OF_SOULS.log(i, th);
    }

    public static Tree asTree() {
        return TREE_OF_SOULS;
    }

    public static Tree tag(String str) {
        for (Tree tree : forestAsArray) {
            tree.explicitTag.set(str);
        }
        return TREE_OF_SOULS;
    }

    public static void plant(Tree tree) {
        if (tree == null) {
            throw new NullPointerException("tree == null");
        } else if (tree != TREE_OF_SOULS) {
            synchronized (FOREST) {
                FOREST.add(tree);
                forestAsArray = (Tree[]) FOREST.toArray(new Tree[FOREST.size()]);
            }
        } else {
            throw new IllegalArgumentException("Cannot plant Timber into itself.");
        }
    }

    public static void plant(Tree... treeArr) {
        if (treeArr != null) {
            int length = treeArr.length;
            int i = 0;
            while (i < length) {
                Tree tree = treeArr[i];
                if (tree == null) {
                    throw new NullPointerException("trees contains null");
                } else if (tree != TREE_OF_SOULS) {
                    i++;
                } else {
                    throw new IllegalArgumentException("Cannot plant Timber into itself.");
                }
            }
            synchronized (FOREST) {
                Collections.addAll(FOREST, treeArr);
                forestAsArray = (Tree[]) FOREST.toArray(new Tree[FOREST.size()]);
            }
            return;
        }
        throw new NullPointerException("trees == null");
    }

    public static void uproot(Tree tree) {
        synchronized (FOREST) {
            if (FOREST.remove(tree)) {
                forestAsArray = (Tree[]) FOREST.toArray(new Tree[FOREST.size()]);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot uproot tree which is not planted: ");
                sb.append(tree);
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    public static void uprootAll() {
        synchronized (FOREST) {
            FOREST.clear();
            forestAsArray = TREE_ARRAY_EMPTY;
        }
    }

    public static List<Tree> forest() {
        List<Tree> unmodifiableList;
        synchronized (FOREST) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(FOREST));
        }
        return unmodifiableList;
    }

    public static int treeCount() {
        int size;
        synchronized (FOREST) {
            size = FOREST.size();
        }
        return size;
    }

    private Timber() {
        throw new AssertionError("No instances.");
    }
}
