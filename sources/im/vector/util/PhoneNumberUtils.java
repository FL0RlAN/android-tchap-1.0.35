package im.vector.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.core.util.Pair;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import im.vector.settings.VectorLocale;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.matrix.androidsdk.core.Log;

public class PhoneNumberUtils {
    public static final String COUNTRY_CODE_PREF_KEY = "COUNTRY_CODE_PREF_KEY";
    private static final String LOG_TAG = PhoneNumberUtils.class.getSimpleName();
    private static String[] mCountryCodes;
    private static List<CountryPhoneData> mCountryIndicatorList;
    private static Map<String, String> mCountryNameByCC;
    private static String[] mCountryNames;
    private static final Map<String, String> mE164PhoneNumberByText = new HashMap();
    private static final Map<String, Object> mPhoneNumberByText = new HashMap();

    public static void onLocaleUpdate() {
        mCountryCodes = null;
        mCountryIndicatorList = null;
    }

    private static void buildCountryCodesList() {
        if (mCountryCodes == null) {
            Locale applicationLocale = VectorLocale.INSTANCE.getApplicationLocale();
            String[] iSOCountries = Locale.getISOCountries();
            ArrayList arrayList = new ArrayList();
            for (String str : iSOCountries) {
                arrayList.add(new Pair(str, new Locale("", str).getDisplayCountry(applicationLocale)));
            }
            Collections.sort(arrayList, new Comparator<Pair<String, String>>() {
                public int compare(Pair<String, String> pair, Pair<String, String> pair2) {
                    return ((String) pair.second).compareTo((String) pair2.second);
                }
            });
            mCountryNameByCC = new HashMap(iSOCountries.length);
            mCountryCodes = new String[iSOCountries.length];
            mCountryNames = new String[iSOCountries.length];
            for (int i = 0; i < iSOCountries.length; i++) {
                Pair pair = (Pair) arrayList.get(i);
                mCountryCodes[i] = (String) pair.first;
                mCountryNames[i] = (String) pair.second;
                mCountryNameByCC.put(pair.first, pair.second);
            }
        }
    }

    public static List<CountryPhoneData> getCountriesWithIndicator() {
        if (mCountryIndicatorList == null) {
            ArrayList arrayList = new ArrayList();
            buildCountryCodesList();
            for (Entry entry : mCountryNameByCC.entrySet()) {
                int countryCodeForRegion = PhoneNumberUtil.getInstance().getCountryCodeForRegion((String) entry.getKey());
                if (countryCodeForRegion > 0) {
                    arrayList.add(new CountryPhoneData((String) entry.getKey(), (String) entry.getValue(), countryCodeForRegion));
                }
            }
            Collections.sort(arrayList, new Comparator<CountryPhoneData>() {
                public int compare(CountryPhoneData countryPhoneData, CountryPhoneData countryPhoneData2) {
                    return countryPhoneData.getCountryName().compareTo(countryPhoneData2.getCountryName());
                }
            });
            mCountryIndicatorList = arrayList;
        }
        return mCountryIndicatorList;
    }

    public static String getHumanCountryCode(String str) {
        buildCountryCodesList();
        if (!TextUtils.isEmpty(str)) {
            return (String) mCountryNameByCC.get(str);
        }
        return null;
    }

    public static String getCountryCode(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String str = COUNTRY_CODE_PREF_KEY;
        String str2 = "";
        if (!defaultSharedPreferences.contains(str) || TextUtils.isEmpty(defaultSharedPreferences.getString(str, str2))) {
            try {
                String upperCase = ((TelephonyManager) context.getSystemService("phone")).getNetworkCountryIso().toUpperCase(VectorLocale.INSTANCE.getApplicationLocale());
                if (!TextUtils.isEmpty(upperCase) || TextUtils.isEmpty(Locale.getDefault().getCountry()) || PhoneNumberUtil.getInstance().getCountryCodeForRegion(Locale.getDefault().getCountry()) == 0) {
                    setCountryCode(context, upperCase);
                } else {
                    setCountryCode(context, Locale.getDefault().getCountry());
                }
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getCountryCode failed ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
        }
        return defaultSharedPreferences.getString(str, str2);
    }

    public static void setCountryCode(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(COUNTRY_CODE_PREF_KEY, str).apply();
    }

    private static String getMapKey(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        String str3 = "μ";
        sb.append(str3);
        sb.append(str2);
        sb.append(str3);
        sb.append(str);
        return sb.toString();
    }

    private static PhoneNumber getPhoneNumber(String str, String str2) {
        String mapKey = getMapKey(str, str2);
        PhoneNumber phoneNumber = null;
        if (mPhoneNumberByText.containsKey(mapKey)) {
            Object obj = mPhoneNumberByText.get(mapKey);
            if (obj instanceof PhoneNumber) {
                return (PhoneNumber) obj;
            }
            return null;
        }
        try {
            phoneNumber = PhoneNumberUtil.getInstance().parse(str, str2);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getPhoneNumber() : failed ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
        }
        if (phoneNumber != null) {
            mPhoneNumberByText.put(mapKey, phoneNumber);
            return phoneNumber;
        }
        mPhoneNumberByText.put(mapKey, "");
        return phoneNumber;
    }

    public static String getE164format(Context context, String str) {
        return getE164format(str, getCountryCode(context));
    }

    private static String getE164format(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        String mapKey = getMapKey(str, str2);
        String str3 = (String) mE164PhoneNumberByText.get(mapKey);
        if (str3 == null) {
            str3 = "";
            try {
                PhoneNumber phoneNumber = getPhoneNumber(str, str2);
                if (phoneNumber != null) {
                    str3 = PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberFormat.E164);
                }
            } catch (Exception e) {
                String str4 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getE164format() failed ");
                sb.append(e.getMessage());
                Log.e(str4, sb.toString(), e);
            }
            if (str3.startsWith("+")) {
                str3 = str3.substring(1);
            }
            mE164PhoneNumberByText.put(mapKey, str3);
        }
        if (!TextUtils.isEmpty(str3)) {
            return str3;
        }
        return null;
    }

    public static String getE164format(PhoneNumber phoneNumber) {
        if (phoneNumber != null) {
            return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberFormat.E164);
        }
        return null;
    }
}
