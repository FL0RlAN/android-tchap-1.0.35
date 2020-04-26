package org.matrix.androidsdk.view;

import android.content.Context;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Layout.Alignment;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan.Standard;
import android.text.style.StrikethroughSpan;
import android.text.style.TypefaceSpan;
import androidx.core.content.ContextCompat;
import java.util.Stack;
import org.xml.sax.XMLReader;

public class HtmlTagHandler implements TagHandler {
    private static final BulletSpan bullet = new BulletSpan(10);
    private static final int indent = 10;
    private static final int listItemIndent = 20;
    private final Stack<String> lists = new Stack<>();
    public int mCodeBlockBackgroundColor = -1;
    public Context mContext;
    private final Stack<Integer> olNextIndex = new Stack<>();
    StringBuilder tableHtmlBuilder = new StringBuilder();
    int tableTagLevel = 0;

    private static class Center {
        private Center() {
        }
    }

    private static class Code {
        private Code() {
        }
    }

    private static class Ol {
        private Ol() {
        }
    }

    private static class Strike {
        private Strike() {
        }
    }

    private static class Table {
        private Table() {
        }
    }

    private static class Td {
        private Td() {
        }
    }

    private static class Th {
        private Th() {
        }
    }

    private static class Tr {
        private Tr() {
        }
    }

    private static class Ul {
        private Ul() {
        }
    }

    public void setCodeBlockBackgroundColor(int i) {
        this.mCodeBlockBackgroundColor = i;
    }

    public void handleTag(boolean z, String str, Editable editable, XMLReader xMLReader) {
        String str2 = str;
        Editable editable2 = editable;
        String str3 = "table";
        String str4 = "strike";
        String str5 = "s";
        String str6 = "center";
        String str7 = "code";
        String str8 = "li";
        String str9 = "\n";
        String str10 = "ol";
        String str11 = "ul";
        int i = 10;
        if (z) {
            if (str2.equalsIgnoreCase(str11)) {
                this.lists.push(str2);
            } else if (str2.equalsIgnoreCase(str10)) {
                this.lists.push(str2);
                this.olNextIndex.push(Integer.valueOf(1));
            } else if (str2.equalsIgnoreCase(str8)) {
                if (editable.length() > 0 && editable2.charAt(editable.length() - 1) != 10) {
                    editable2.append(str9);
                }
                String str12 = (String) this.lists.peek();
                if (str12.equalsIgnoreCase(str10)) {
                    start(editable2, new Ol());
                    editable2.append(((Integer) this.olNextIndex.peek()).toString()).append(". ");
                    Stack<Integer> stack = this.olNextIndex;
                    stack.push(Integer.valueOf(((Integer) stack.pop()).intValue() + 1));
                } else if (str12.equalsIgnoreCase(str11)) {
                    start(editable2, new Ul());
                }
            } else if (str2.equalsIgnoreCase(str7)) {
                start(editable2, new Code());
            } else if (str2.equalsIgnoreCase(str6)) {
                start(editable2, new Center());
            } else if (str2.equalsIgnoreCase(str5) || str2.equalsIgnoreCase(str4)) {
                start(editable2, new Strike());
            } else if (str2.equalsIgnoreCase(str3)) {
                start(editable2, new Table());
                if (this.tableTagLevel == 0) {
                    this.tableHtmlBuilder = new StringBuilder();
                    editable2.append("table placeholder");
                }
                this.tableTagLevel++;
            } else if (str2.equalsIgnoreCase("tr")) {
                start(editable2, new Tr());
            } else if (str2.equalsIgnoreCase("th")) {
                start(editable2, new Th());
            } else if (str2.equalsIgnoreCase("td")) {
                start(editable2, new Td());
            }
        } else if (str2.equalsIgnoreCase(str11)) {
            this.lists.pop();
        } else if (str2.equalsIgnoreCase(str10)) {
            this.lists.pop();
            this.olNextIndex.pop();
        } else if (str2.equalsIgnoreCase(str8)) {
            if (((String) this.lists.peek()).equalsIgnoreCase(str11)) {
                if (editable.length() > 0 && editable2.charAt(editable.length() - 1) != 10) {
                    editable2.append(str9);
                }
                if (this.lists.size() > 1) {
                    i = 10 - bullet.getLeadingMargin(true);
                    if (this.lists.size() > 2) {
                        i -= (this.lists.size() - 2) * 20;
                    }
                }
                end(editable2, Ul.class, false, new Standard((this.lists.size() - 1) * 20), new BulletSpan(i));
            } else if (((String) this.lists.peek()).equalsIgnoreCase(str10)) {
                if (editable.length() > 0 && editable2.charAt(editable.length() - 1) != 10) {
                    editable2.append(str9);
                }
                int size = (this.lists.size() - 1) * 20;
                if (this.lists.size() > 2) {
                    size -= (this.lists.size() - 2) * 20;
                }
                end(editable2, Ol.class, false, new Standard(size));
            }
        } else if (str2.equalsIgnoreCase(str7)) {
            if (-1 == this.mCodeBlockBackgroundColor) {
                this.mCodeBlockBackgroundColor = ContextCompat.getColor(this.mContext, 17170432);
            }
            end(editable2, Code.class, false, new BackgroundColorSpan(this.mCodeBlockBackgroundColor), new TypefaceSpan("monospace"));
        } else if (str2.equalsIgnoreCase(str6)) {
            end(editable2, Center.class, true, new AlignmentSpan.Standard(Alignment.ALIGN_CENTER));
        } else if (str2.equalsIgnoreCase(str5) || str2.equalsIgnoreCase(str4)) {
            end(editable2, Strike.class, false, new StrikethroughSpan());
        } else if (str2.equalsIgnoreCase(str3)) {
            this.tableTagLevel--;
            end(editable2, Table.class, false, new Object[0]);
        } else if (str2.equalsIgnoreCase("tr")) {
            end(editable2, Tr.class, false, new Object[0]);
        } else if (str2.equalsIgnoreCase("th")) {
            end(editable2, Th.class, false, new Object[0]);
        } else if (str2.equalsIgnoreCase("td")) {
            end(editable2, Td.class, false, new Object[0]);
        }
        storeTableTags(z, str);
    }

    private void storeTableTags(boolean z, String str) {
        if (this.tableTagLevel > 0 || str.equalsIgnoreCase("table")) {
            this.tableHtmlBuilder.append("<");
            if (!z) {
                this.tableHtmlBuilder.append("/");
            }
            StringBuilder sb = this.tableHtmlBuilder;
            sb.append(str.toLowerCase());
            sb.append(">");
        }
    }

    private void start(Editable editable, Object obj) {
        int length = editable.length();
        editable.setSpan(obj, length, length, 17);
    }

    private void end(Editable editable, Class cls, boolean z, Object... objArr) {
        Object last = getLast(editable, cls);
        int spanStart = editable.getSpanStart(last);
        int length = editable.length();
        if (this.tableTagLevel > 0) {
            this.tableHtmlBuilder.append(extractSpanText(editable, cls));
        }
        editable.removeSpan(last);
        if (spanStart != length) {
            if (z) {
                editable.append("\n");
                length++;
            }
            for (Object span : objArr) {
                editable.setSpan(span, spanStart, length, 33);
            }
        }
    }

    private CharSequence extractSpanText(Editable editable, Class cls) {
        int spanStart = editable.getSpanStart(getLast(editable, cls));
        int length = editable.length();
        CharSequence subSequence = editable.subSequence(spanStart, length);
        editable.delete(spanStart, length);
        return subSequence;
    }

    private static Object getLast(Editable editable, Class cls) {
        Object[] spans = editable.getSpans(0, editable.length(), cls);
        if (spans.length == 0) {
            return null;
        }
        for (int length = spans.length; length > 0; length--) {
            int i = length - 1;
            if (editable.getSpanFlags(spans[i]) == 17) {
                return spans[i];
            }
        }
        return null;
    }
}
