package im.vector.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import fr.gouv.tchap.a.R;
import im.vector.util.RoomUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class UnreadCounterBadgeView extends FrameLayout {
    public static final int DEFAULT = 2;
    public static final int HIGHLIGHTED = 0;
    public static final int NOTIFIED = 1;
    private TextView mCounterTextView;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

    public UnreadCounterBadgeView(Context context) {
        super(context);
        init();
    }

    public UnreadCounterBadgeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public UnreadCounterBadgeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.unread_counter_badge, this);
        this.mCounterTextView = (TextView) findViewById(R.id.unread_counter_badge_text_view);
    }

    public void updateCounter(int i, int i2) {
        updateText(RoomUtils.formatUnreadMessagesCounter(i), i2);
    }

    public void updateText(String str, int i) {
        if (!TextUtils.isEmpty(str)) {
            this.mCounterTextView.setText(str);
            setVisibility(0);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setCornerRadius(100.0f);
            if (i == 0) {
                gradientDrawable.setColor(ContextCompat.getColor(getContext(), R.color.vector_fuchsia_color));
            } else if (i == 1) {
                gradientDrawable.setColor(ContextCompat.getColor(getContext(), R.color.vector_green_color));
            } else {
                gradientDrawable.setColor(ContextCompat.getColor(getContext(), R.color.vector_silver_color));
            }
            this.mCounterTextView.setBackground(gradientDrawable);
            return;
        }
        setVisibility(8);
    }
}
