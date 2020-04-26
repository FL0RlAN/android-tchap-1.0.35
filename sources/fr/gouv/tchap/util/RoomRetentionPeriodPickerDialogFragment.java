package fr.gouv.tchap.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.NumberPicker;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.session.room.model.RoomRetentionKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u000b0\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lfr/gouv/tchap/util/RoomRetentionPeriodPickerDialogFragment;", "", "activity", "Landroid/app/Activity;", "(Landroid/app/Activity;)V", "create", "Landroid/app/Dialog;", "value", "", "valueChangeListener", "Lkotlin/Function1;", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: RoomRetentionPeriodPickerDialogFragment.kt */
public final class RoomRetentionPeriodPickerDialogFragment {
    private final Activity activity;

    public RoomRetentionPeriodPickerDialogFragment(Activity activity2) {
        Intrinsics.checkParameterIsNotNull(activity2, "activity");
        this.activity = activity2;
    }

    public final Dialog create(int i, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "valueChangeListener");
        NumberPicker numberPicker = new NumberPicker(this.activity);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(RoomRetentionKt.DEFAULT_RETENTION_VALUE_IN_DAYS);
        numberPicker.setValue(i);
        AlertDialog create = new Builder(this.activity).setTitle((int) R.string.tchap_room_creation_retention).setPositiveButton((int) R.string.ok, (OnClickListener) new RoomRetentionPeriodPickerDialogFragment$create$1(function1, numberPicker)).setNegativeButton((int) R.string.cancel, (OnClickListener) null).setView((View) numberPicker).create();
        Intrinsics.checkExpressionValueIsNotNull(create, "AlertDialog.Builder(acti…                .create()");
        return create;
    }
}
