package im.vector.activity;

import java.util.Map;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: AbstractWidgetActivity.kt */
final class AbstractWidgetActivity$WidgetWebAppInterface$onWidgetEvent$1 implements Runnable {
    final /* synthetic */ Map $objectAsMap;
    final /* synthetic */ WidgetWebAppInterface this$0;

    AbstractWidgetActivity$WidgetWebAppInterface$onWidgetEvent$1(WidgetWebAppInterface widgetWebAppInterface, Map map) {
        this.this$0 = widgetWebAppInterface;
        this.$objectAsMap = map;
    }

    public final void run() {
        AbstractWidgetActivity.this.onWidgetMessage(this.$objectAsMap);
    }
}
