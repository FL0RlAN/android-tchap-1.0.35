package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.Realm.Transaction;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "kotlin.jvm.PlatformType", "execute"}, k = 3, mv = {1, 1, 13})
/* compiled from: Helper.kt */
final class HelperKt$doRealmTransaction$1 implements Transaction {
    final /* synthetic */ Function1 $action;

    HelperKt$doRealmTransaction$1(Function1 function1) {
        this.$action = function1;
    }

    public final void execute(Realm realm) {
        Function1 function1 = this.$action;
        Intrinsics.checkExpressionValueIsNotNull(realm, "it");
        function1.invoke(realm);
    }
}
