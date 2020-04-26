package im.vector.util;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;
import im.vector.listeners.IMessagesAdapterActionsListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;

public class MatrixURLSpan extends ClickableSpan implements ParcelableSpan {
    public static final Creator<MatrixURLSpan> CREATOR = new Creator<MatrixURLSpan>() {
        public MatrixURLSpan createFromParcel(Parcel parcel) {
            return new MatrixURLSpan(parcel);
        }

        public MatrixURLSpan[] newArray(int i) {
            return new MatrixURLSpan[i];
        }
    };
    private static final String LOG_TAG = MatrixURLSpan.class.getSimpleName();
    private final IMessagesAdapterActionsListener mActionsListener;
    private final Pattern mPattern;
    private final String mURL;

    public int describeContents() {
        return 0;
    }

    public MatrixURLSpan(String str, Pattern pattern, IMessagesAdapterActionsListener iMessagesAdapterActionsListener) {
        this.mURL = str;
        this.mPattern = pattern;
        this.mActionsListener = iMessagesAdapterActionsListener;
    }

    private MatrixURLSpan(Parcel parcel) {
        this.mURL = parcel.readString();
        this.mPattern = null;
        this.mActionsListener = null;
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return getClass().hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        writeToParcelInternal(parcel, i);
    }

    public void writeToParcelInternal(Parcel parcel, int i) {
        parcel.writeString(this.mURL);
    }

    private String getURL() {
        return this.mURL;
    }

    public void onClick(View view) {
        try {
            if (this.mPattern == MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER) {
                if (this.mActionsListener != null) {
                    this.mActionsListener.onMatrixUserIdClick(this.mURL);
                }
            } else if (this.mPattern == MXPatterns.PATTERN_CONTAIN_MATRIX_ALIAS) {
                if (this.mActionsListener != null) {
                    this.mActionsListener.onRoomAliasClick(this.mURL);
                }
            } else if (this.mPattern == MXPatterns.PATTERN_CONTAIN_MATRIX_ROOM_IDENTIFIER) {
                if (this.mActionsListener != null) {
                    this.mActionsListener.onRoomIdClick(this.mURL);
                }
            } else if (this.mPattern == MXPatterns.PATTERN_CONTAIN_MATRIX_EVENT_IDENTIFIER) {
                if (this.mActionsListener != null) {
                    this.mActionsListener.onEventIdClick(this.mURL);
                }
            } else if (this.mPattern != MXPatterns.PATTERN_CONTAIN_MATRIX_GROUP_IDENTIFIER) {
                Uri parse = Uri.parse(getURL());
                if (this.mActionsListener != null) {
                    this.mActionsListener.onURLClick(parse);
                } else {
                    ExternalApplicationsUtilKt.openUrlInExternalBrowser(view.getContext(), parse);
                }
            } else if (this.mActionsListener != null) {
                this.mActionsListener.onGroupIdClick(this.mURL);
            }
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MatrixURLSpan : on click failed ");
            sb.append(e.getLocalizedMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public static void refreshMatrixSpans(SpannableStringBuilder spannableStringBuilder, IMessagesAdapterActionsListener iMessagesAdapterActionsListener) {
        if (spannableStringBuilder != null && spannableStringBuilder.length() != 0) {
            String spannableStringBuilder2 = spannableStringBuilder.toString();
            for (int i = 0; i < MXPatterns.MATRIX_PATTERNS.size(); i++) {
                Pattern pattern = (Pattern) MXPatterns.MATRIX_PATTERNS.get(i);
                Matcher matcher = pattern.matcher(spannableStringBuilder);
                while (matcher.find()) {
                    try {
                        int start = matcher.start(0);
                        if (start == 0 || spannableStringBuilder2.charAt(start - 1) != '/') {
                            spannableStringBuilder.setSpan(new MatrixURLSpan(spannableStringBuilder2.substring(matcher.start(0), matcher.end(0)), pattern, iMessagesAdapterActionsListener), start, matcher.end(0), 33);
                        }
                    } catch (Exception e) {
                        String str = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("refreshMatrixSpans ");
                        sb.append(e.getLocalizedMessage());
                        Log.e(str, sb.toString(), e);
                    }
                }
            }
        }
    }
}
