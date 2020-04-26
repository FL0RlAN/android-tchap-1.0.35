package org.matrix.androidsdk.crypto.cryptostore.db;

import android.util.Base64;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Charsets;
import org.matrix.androidsdk.core.CompatUtil;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u001d\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004\u001a5\u0010\u0005\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u0001H\u00010\n¢\u0006\u0002\u0010\f\u001a8\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00010\u000e\"\b\b\u0000\u0010\u0001*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u000e0\n\u001a\"\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00100\n\u001a-\u0010\u0011\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u0002H\u00010\n¢\u0006\u0002\u0010\u0012\u001a\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00032\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015\u001a\n\u0010\u0016\u001a\u00020\u0003*\u00020\u0003¨\u0006\u0017"}, d2 = {"deserializeFromRealm", "T", "string", "", "(Ljava/lang/String;)Ljava/lang/Object;", "doRealmQueryAndCopy", "Lio/realm/RealmObject;", "realmConfiguration", "Lio/realm/RealmConfiguration;", "action", "Lkotlin/Function1;", "Lio/realm/Realm;", "(Lio/realm/RealmConfiguration;Lkotlin/jvm/functions/Function1;)Lio/realm/RealmObject;", "doRealmQueryAndCopyList", "", "doRealmTransaction", "", "doWithRealm", "(Lio/realm/RealmConfiguration;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "serializeForRealm", "o", "", "hash", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: Helper.kt */
public final class HelperKt {
    public static final String hash(String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        try {
            MessageDigest instance = MessageDigest.getInstance("md5");
            byte[] bytes = str.getBytes(Charsets.UTF_8);
            Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
            instance.update(bytes);
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            Intrinsics.checkExpressionValueIsNotNull(digest, "bytes");
            for (byte valueOf : digest) {
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                Object[] objArr = {Byte.valueOf(valueOf)};
                String format = String.format("%02X", Arrays.copyOf(objArr, objArr.length));
                Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
                sb.append(format);
            }
            String sb2 = sb.toString();
            Intrinsics.checkExpressionValueIsNotNull(sb2, "sb.toString()");
            if (sb2 != null) {
                String lowerCase = sb2.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
                return lowerCase;
            }
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        } catch (Exception unused) {
            return String.valueOf(str.hashCode());
        }
    }

    public static final <T> T doWithRealm(RealmConfiguration realmConfiguration, Function1<? super Realm, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull(realmConfiguration, "realmConfiguration");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Realm instance = Realm.getInstance(realmConfiguration);
        Intrinsics.checkExpressionValueIsNotNull(instance, "realm");
        T invoke = function1.invoke(instance);
        instance.close();
        return invoke;
    }

    public static final <T extends RealmObject> T doRealmQueryAndCopy(RealmConfiguration realmConfiguration, Function1<? super Realm, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull(realmConfiguration, "realmConfiguration");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Realm instance = Realm.getInstance(realmConfiguration);
        Intrinsics.checkExpressionValueIsNotNull(instance, "realm");
        RealmObject realmObject = (RealmObject) function1.invoke(instance);
        T t = realmObject != null ? (RealmObject) instance.copyFromRealm(realmObject) : null;
        instance.close();
        return t;
    }

    public static final <T extends RealmObject> Iterable<T> doRealmQueryAndCopyList(RealmConfiguration realmConfiguration, Function1<? super Realm, ? extends Iterable<? extends T>> function1) {
        Intrinsics.checkParameterIsNotNull(realmConfiguration, "realmConfiguration");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Realm instance = Realm.getInstance(realmConfiguration);
        Intrinsics.checkExpressionValueIsNotNull(instance, "realm");
        List copyFromRealm = instance.copyFromRealm((Iterable) function1.invoke(instance));
        instance.close();
        Intrinsics.checkExpressionValueIsNotNull(copyFromRealm, "copiedResult");
        return copyFromRealm;
    }

    public static final void doRealmTransaction(RealmConfiguration realmConfiguration, Function1<? super Realm, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(realmConfiguration, "realmConfiguration");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Realm instance = Realm.getInstance(realmConfiguration);
        instance.executeTransaction(new HelperKt$doRealmTransaction$1(function1));
        instance.close();
    }

    public static final String serializeForRealm(Object obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(CompatUtil.createGzipOutputStream(byteArrayOutputStream));
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public static final <T> T deserializeFromRealm(String str) {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(Base64.decode(bytes, 0))));
        T readObject = objectInputStream.readObject();
        objectInputStream.close();
        return readObject;
    }
}
