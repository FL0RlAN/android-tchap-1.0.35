package com.google.android.gms.dynamite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.CrashUtils;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class DynamiteModule {
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzd();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zze();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzf();
    public static final VersionPolicy PREFER_REMOTE = new zzb();
    private static Boolean zzid = null;
    private static zzi zzie = null;
    private static zzk zzif = null;
    private static String zzig = null;
    private static int zzih = -1;
    private static final ThreadLocal<zza> zzii = new ThreadLocal<>();
    private static final zza zzij = new zza();
    private static final VersionPolicy zzik = new zzc();
    private static final VersionPolicy zzil = new zzg();
    private final Context zzim;

    public static class DynamiteLoaderClassLoader {
        public static ClassLoader sClassLoader;
    }

    public static class LoadingException extends Exception {
        private LoadingException(String str) {
            super(str);
        }

        private LoadingException(String str, Throwable th) {
            super(str, th);
        }

        /* synthetic */ LoadingException(String str, zza zza) {
            this(str);
        }

        /* synthetic */ LoadingException(String str, Throwable th, zza zza) {
            this(str, th);
        }
    }

    public interface VersionPolicy {

        public interface zza {
            int getLocalVersion(Context context, String str);

            int zza(Context context, String str, boolean z) throws LoadingException;
        }

        public static class zzb {
            public int zziq = 0;
            public int zzir = 0;
            public int zzis = 0;
        }

        zzb zza(Context context, String str, zza zza2) throws LoadingException;
    }

    private static class zza {
        public Cursor zzin;

        private zza() {
        }

        /* synthetic */ zza(zza zza) {
            this();
        }
    }

    private static class zzb implements zza {
        private final int zzio;
        private final int zzip = 0;

        public zzb(int i, int i2) {
            this.zzio = i;
        }

        public final int zza(Context context, String str, boolean z) {
            return 0;
        }

        public final int getLocalVersion(Context context, String str) {
            return this.zzio;
        }
    }

    public static DynamiteModule load(Context context, VersionPolicy versionPolicy, String str) throws LoadingException {
        zzb zza2;
        String str2 = ":";
        String str3 = "DynamiteModule";
        zza zza3 = (zza) zzii.get();
        zza zza4 = new zza(null);
        zzii.set(zza4);
        try {
            zza2 = versionPolicy.zza(context, str, zzij);
            int i = zza2.zziq;
            int i2 = zza2.zzir;
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 68 + String.valueOf(str).length());
            sb.append("Considering local module ");
            sb.append(str);
            sb.append(str2);
            sb.append(i);
            sb.append(" and remote module ");
            sb.append(str);
            sb.append(str2);
            sb.append(i2);
            Log.i(str3, sb.toString());
            if (zza2.zzis == 0 || ((zza2.zzis == -1 && zza2.zziq == 0) || (zza2.zzis == 1 && zza2.zzir == 0))) {
                int i3 = zza2.zziq;
                int i4 = zza2.zzir;
                StringBuilder sb2 = new StringBuilder(91);
                sb2.append("No acceptable module found. Local version is ");
                sb2.append(i3);
                sb2.append(" and remote version is ");
                sb2.append(i4);
                sb2.append(".");
                throw new LoadingException(sb2.toString(), (zza) null);
            } else if (zza2.zzis == -1) {
                DynamiteModule zze = zze(context, str);
                if (zza4.zzin != null) {
                    zza4.zzin.close();
                }
                zzii.set(zza3);
                return zze;
            } else if (zza2.zzis == 1) {
                DynamiteModule zza5 = zza(context, str, zza2.zzir);
                if (zza4.zzin != null) {
                    zza4.zzin.close();
                }
                zzii.set(zza3);
                return zza5;
            } else {
                int i5 = zza2.zzis;
                StringBuilder sb3 = new StringBuilder(47);
                sb3.append("VersionPolicy returned invalid code:");
                sb3.append(i5);
                throw new LoadingException(sb3.toString(), (zza) null);
            }
        } catch (LoadingException e) {
            String str4 = "Failed to load remote module: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.w(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
            if (zza2.zziq == 0 || versionPolicy.zza(context, str, new zzb(zza2.zziq, 0)).zzis != -1) {
                throw new LoadingException("Remote load failed. No local fallback found.", e, null);
            }
            DynamiteModule zze2 = zze(context, str);
            if (zza4.zzin != null) {
                zza4.zzin.close();
            }
            zzii.set(zza3);
            return zze2;
        } catch (Throwable th) {
            if (zza4.zzin != null) {
                zza4.zzin.close();
            }
            zzii.set(zza3);
            throw th;
        }
    }

    public static int getLocalVersion(Context context, String str) {
        String str2 = "DynamiteModule";
        try {
            ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 61);
            sb.append("com.google.android.gms.dynamite.descriptors.");
            sb.append(str);
            sb.append(".ModuleDescriptor");
            Class loadClass = classLoader.loadClass(sb.toString());
            Field declaredField = loadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (declaredField.get(null).equals(str)) {
                return declaredField2.getInt(null);
            }
            String valueOf = String.valueOf(declaredField.get(null));
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 51 + String.valueOf(str).length());
            sb2.append("Module descriptor id '");
            sb2.append(valueOf);
            sb2.append("' didn't match expected id '");
            sb2.append(str);
            sb2.append("'");
            Log.e(str2, sb2.toString());
            return 0;
        } catch (ClassNotFoundException unused) {
            StringBuilder sb3 = new StringBuilder(String.valueOf(str).length() + 45);
            sb3.append("Local module descriptor class for ");
            sb3.append(str);
            sb3.append(" not found.");
            Log.w(str2, sb3.toString());
            return 0;
        } catch (Exception e) {
            String str3 = "Failed to load module descriptor class: ";
            String valueOf2 = String.valueOf(e.getMessage());
            Log.e(str2, valueOf2.length() != 0 ? str3.concat(valueOf2) : new String(str3));
            return 0;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:16|17|18|19) */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:40|41|42|43|52|53|54|55|(0)(0)) */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r2.set(null, java.lang.ClassLoader.getSystemClassLoader());
        r2 = java.lang.Boolean.FALSE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0088, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0035 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:40:0x007e */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00c1 A[SYNTHETIC, Splitter:B:57:0x00c1] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00e8 A[Catch:{ LoadingException -> 0x00c6, all -> 0x00f0 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:23:0x0052=Splitter:B:23:0x0052, B:18:0x0035=Splitter:B:18:0x0035, B:35:0x007b=Splitter:B:35:0x007b} */
    public static int zza(Context context, String str, boolean z) {
        Boolean bool;
        try {
            synchronized (DynamiteModule.class) {
                Boolean bool2 = zzid;
                if (bool2 == null) {
                    try {
                        Class loadClass = context.getApplicationContext().getClassLoader().loadClass(DynamiteLoaderClassLoader.class.getName());
                        Field declaredField = loadClass.getDeclaredField("sClassLoader");
                        synchronized (loadClass) {
                            ClassLoader classLoader = (ClassLoader) declaredField.get(null);
                            if (classLoader != null) {
                                if (classLoader == ClassLoader.getSystemClassLoader()) {
                                    bool = Boolean.FALSE;
                                } else {
                                    zza(classLoader);
                                    bool = Boolean.TRUE;
                                }
                            } else if ("com.google.android.gms".equals(context.getApplicationContext().getPackageName())) {
                                declaredField.set(null, ClassLoader.getSystemClassLoader());
                                bool = Boolean.FALSE;
                            } else {
                                int zzc = zzc(context, str, z);
                                if (zzig != null) {
                                    if (!zzig.isEmpty()) {
                                        zzh zzh = new zzh(zzig, ClassLoader.getSystemClassLoader());
                                        zza(zzh);
                                        declaredField.set(null, zzh);
                                        zzid = Boolean.TRUE;
                                        return zzc;
                                    }
                                }
                                return zzc;
                            }
                        }
                    } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
                        String str2 = "DynamiteModule";
                        String valueOf = String.valueOf(e);
                        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 30);
                        sb.append("Failed to load module via V2: ");
                        sb.append(valueOf);
                        Log.w(str2, sb.toString());
                        bool2 = Boolean.FALSE;
                        zzid = bool2;
                        if (bool2.booleanValue()) {
                            return zzc(context, str, z);
                        }
                        return zzb(context, str, z);
                    }
                }
            }
        } catch (LoadingException e2) {
            String str3 = "DynamiteModule";
            String str4 = "Failed to retrieve remote module version: ";
            String valueOf2 = String.valueOf(e2.getMessage());
            Log.w(str3, valueOf2.length() != 0 ? str4.concat(valueOf2) : new String(str4));
            return 0;
        } catch (Throwable th) {
            CrashUtils.addDynamiteErrorToDropBox(context, th);
            throw th;
        }
    }

    private static int zzb(Context context, String str, boolean z) {
        String str2 = "DynamiteModule";
        zzi zzj = zzj(context);
        if (zzj == null) {
            return 0;
        }
        try {
            if (zzj.zzaj() >= 2) {
                return zzj.zzb(ObjectWrapper.wrap(context), str, z);
            }
            Log.w(str2, "IDynamite loader version < 2, falling back to getModuleVersion2");
            return zzj.zza(ObjectWrapper.wrap(context), str, z);
        } catch (RemoteException e) {
            String str3 = "Failed to retrieve remote module version: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x00b0  */
    private static int zzc(Context context, String str, boolean z) throws LoadingException {
        Cursor cursor;
        Cursor cursor2 = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            String str2 = z ? "api_force_staging" : "api";
            StringBuilder sb = new StringBuilder(str2.length() + 42 + String.valueOf(str).length());
            sb.append("content://com.google.android.gms.chimera/");
            sb.append(str2);
            sb.append("/");
            sb.append(str);
            Cursor query = contentResolver.query(Uri.parse(sb.toString()), null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        int i = query.getInt(0);
                        if (i > 0) {
                            synchronized (DynamiteModule.class) {
                                zzig = query.getString(2);
                                int columnIndex = query.getColumnIndex("loaderVersion");
                                if (columnIndex >= 0) {
                                    zzih = query.getInt(columnIndex);
                                }
                            }
                            zza zza2 = (zza) zzii.get();
                            if (zza2 != null && zza2.zzin == null) {
                                zza2.zzin = query;
                                query = null;
                            }
                        }
                        if (query != null) {
                            query.close();
                        }
                        return i;
                    }
                } catch (Exception e) {
                    Throwable th = e;
                    cursor = query;
                    e = th;
                } catch (Throwable th2) {
                    cursor2 = query;
                    th = th2;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            Log.w("DynamiteModule", "Failed to retrieve remote module version.");
            throw new LoadingException("Failed to connect to dynamite module ContentResolver.", (zza) null);
        } catch (Exception e2) {
            e = e2;
            cursor = null;
            try {
                if (e instanceof LoadingException) {
                    throw e;
                }
                throw new LoadingException("V2 version check failed", e, null);
            } catch (Throwable th3) {
                th = th3;
                cursor2 = cursor;
                if (cursor2 != null) {
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            if (cursor2 != null) {
            }
            throw th;
        }
    }

    public static int getRemoteVersion(Context context, String str) {
        return zza(context, str, false);
    }

    private static DynamiteModule zze(Context context, String str) {
        String valueOf = String.valueOf(str);
        String str2 = "Selected local version of ";
        Log.i("DynamiteModule", valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static DynamiteModule zza(Context context, String str, int i) throws LoadingException {
        Boolean bool;
        try {
            synchronized (DynamiteModule.class) {
                bool = zzid;
            }
            if (bool == null) {
                throw new LoadingException("Failed to determine which loading route to use.", (zza) null);
            } else if (bool.booleanValue()) {
                return zzc(context, str, i);
            } else {
                return zzb(context, str, i);
            }
        } catch (Throwable th) {
            CrashUtils.addDynamiteErrorToDropBox(context, th);
            throw th;
        }
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws LoadingException {
        IObjectWrapper iObjectWrapper;
        String str2 = "Failed to load remote module.";
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(str);
        sb.append(", version >= ");
        sb.append(i);
        String sb2 = sb.toString();
        String str3 = "DynamiteModule";
        Log.i(str3, sb2);
        zzi zzj = zzj(context);
        if (zzj != null) {
            try {
                if (zzj.zzaj() >= 2) {
                    iObjectWrapper = zzj.zzb(ObjectWrapper.wrap(context), str, i);
                } else {
                    Log.w(str3, "Dynamite loader version < 2, falling back to createModuleContext");
                    iObjectWrapper = zzj.zza(ObjectWrapper.wrap(context), str, i);
                }
                if (ObjectWrapper.unwrap(iObjectWrapper) != null) {
                    return new DynamiteModule((Context) ObjectWrapper.unwrap(iObjectWrapper));
                }
                throw new LoadingException(str2, (zza) null);
            } catch (RemoteException e) {
                throw new LoadingException(str2, e, null);
            }
        } else {
            throw new LoadingException("Failed to create IDynamiteLoader.", (zza) null);
        }
    }

    private static zzi zzj(Context context) {
        zzi zzi;
        synchronized (DynamiteModule.class) {
            if (zzie != null) {
                zzi zzi2 = zzie;
                return zzi2;
            } else if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            } else {
                try {
                    IBinder iBinder = (IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                    if (iBinder == null) {
                        zzi = null;
                    } else {
                        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                        if (queryLocalInterface instanceof zzi) {
                            zzi = (zzi) queryLocalInterface;
                        } else {
                            zzi = new zzj(iBinder);
                        }
                    }
                    if (zzi != null) {
                        zzie = zzi;
                        return zzi;
                    }
                } catch (Exception e) {
                    String str = "DynamiteModule";
                    String str2 = "Failed to load IDynamiteLoader from GmsCore: ";
                    String valueOf = String.valueOf(e.getMessage());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                }
            }
        }
        return null;
    }

    public final Context getModuleContext() {
        return this.zzim;
    }

    private static DynamiteModule zzc(Context context, String str, int i) throws LoadingException {
        zzk zzk;
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(str);
        sb.append(", version >= ");
        sb.append(i);
        Log.i("DynamiteModule", sb.toString());
        synchronized (DynamiteModule.class) {
            zzk = zzif;
        }
        if (zzk != null) {
            zza zza2 = (zza) zzii.get();
            if (zza2 == null || zza2.zzin == null) {
                throw new LoadingException("No result cursor", (zza) null);
            }
            Context zza3 = zza(context.getApplicationContext(), str, i, zza2.zzin, zzk);
            if (zza3 != null) {
                return new DynamiteModule(zza3);
            }
            throw new LoadingException("Failed to get module context", (zza) null);
        }
        throw new LoadingException("DynamiteLoaderV2 was not cached.", (zza) null);
    }

    private static Boolean zzai() {
        Boolean valueOf;
        synchronized (DynamiteModule.class) {
            valueOf = Boolean.valueOf(zzih >= 2);
        }
        return valueOf;
    }

    private static Context zza(Context context, String str, int i, Cursor cursor, zzk zzk) {
        IObjectWrapper iObjectWrapper;
        String str2 = "DynamiteModule";
        try {
            ObjectWrapper.wrap(null);
            if (zzai().booleanValue()) {
                Log.v(str2, "Dynamite loader version >= 2, using loadModule2NoCrashUtils");
                iObjectWrapper = zzk.zzb(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(cursor));
            } else {
                Log.w(str2, "Dynamite loader version < 2, falling back to loadModule2");
                iObjectWrapper = zzk.zza(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(cursor));
            }
            return (Context) ObjectWrapper.unwrap(iObjectWrapper);
        } catch (Exception e) {
            String str3 = "Failed to load DynamiteLoader: ";
            String valueOf = String.valueOf(e.toString());
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return null;
        }
    }

    private static void zza(ClassLoader classLoader) throws LoadingException {
        zzk zzk;
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                zzk = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                if (queryLocalInterface instanceof zzk) {
                    zzk = (zzk) queryLocalInterface;
                } else {
                    zzk = new zzl(iBinder);
                }
            }
            zzif = zzk;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new LoadingException("Failed to instantiate dynamite loader", e, null);
        }
    }

    public final IBinder instantiate(String str) throws LoadingException {
        try {
            return (IBinder) this.zzim.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            String str2 = "Failed to instantiate module class: ";
            String valueOf = String.valueOf(str);
            throw new LoadingException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), e, null);
        }
    }

    private DynamiteModule(Context context) {
        this.zzim = (Context) Preconditions.checkNotNull(context);
    }
}
