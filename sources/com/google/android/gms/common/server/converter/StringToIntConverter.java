package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.server.response.FastJsonResponse.FieldConverter;
import java.util.ArrayList;
import java.util.HashMap;

public final class StringToIntConverter extends AbstractSafeParcelable implements FieldConverter<String, Integer> {
    public static final Creator<StringToIntConverter> CREATOR = new zac();
    private final int zale;
    private final HashMap<String, Integer> zapl;
    private final SparseArray<String> zapm;
    private final ArrayList<zaa> zapn;

    public static final class zaa extends AbstractSafeParcelable {
        public static final Creator<zaa> CREATOR = new zad();
        private final int versionCode;
        final String zapo;
        final int zapp;

        zaa(int i, String str, int i2) {
            this.versionCode = i;
            this.zapo = str;
            this.zapp = i2;
        }

        zaa(String str, int i) {
            this.versionCode = 1;
            this.zapo = str;
            this.zapp = i;
        }

        public final void writeToParcel(Parcel parcel, int i) {
            int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
            SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
            SafeParcelWriter.writeString(parcel, 2, this.zapo, false);
            SafeParcelWriter.writeInt(parcel, 3, this.zapp);
            SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
        }
    }

    StringToIntConverter(int i, ArrayList<zaa> arrayList) {
        this.zale = i;
        this.zapl = new HashMap<>();
        this.zapm = new SparseArray<>();
        this.zapn = null;
        ArrayList arrayList2 = arrayList;
        int size = arrayList2.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList2.get(i2);
            i2++;
            zaa zaa2 = (zaa) obj;
            add(zaa2.zapo, zaa2.zapp);
        }
    }

    public final int zacj() {
        return 7;
    }

    public final int zack() {
        return 0;
    }

    public StringToIntConverter() {
        this.zale = 1;
        this.zapl = new HashMap<>();
        this.zapm = new SparseArray<>();
        this.zapn = null;
    }

    public final StringToIntConverter add(String str, int i) {
        this.zapl.put(str, Integer.valueOf(i));
        this.zapm.put(i, str);
        return this;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zale);
        ArrayList arrayList = new ArrayList();
        for (String str : this.zapl.keySet()) {
            arrayList.add(new zaa(str, ((Integer) this.zapl.get(str)).intValue()));
        }
        SafeParcelWriter.writeTypedList(parcel, 2, arrayList, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final /* synthetic */ Object convertBack(Object obj) {
        String str = (String) this.zapm.get(((Integer) obj).intValue());
        if (str == null) {
            String str2 = "gms_unknown";
            if (this.zapl.containsKey(str2)) {
                return str2;
            }
        }
        return str;
    }

    public final /* synthetic */ Object convert(Object obj) {
        Integer num = (Integer) this.zapl.get((String) obj);
        return num == null ? (Integer) this.zapl.get("gms_unknown") : num;
    }
}
