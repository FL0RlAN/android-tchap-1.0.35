package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.io.IOException;

final class zzu implements Continuation<Bundle, String> {
    private final /* synthetic */ zzr zzbo;

    zzu(zzr zzr) {
        this.zzbo = zzr;
    }

    public final /* synthetic */ Object then(Task task) throws Exception {
        return zzr.zza((Bundle) task.getResult(IOException.class));
    }
}
