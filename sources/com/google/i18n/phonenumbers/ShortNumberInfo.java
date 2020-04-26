package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneNumberDesc;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.internal.MatcherApi;
import com.google.i18n.phonenumbers.internal.RegexBasedMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShortNumberInfo {
    private static final ShortNumberInfo INSTANCE = new ShortNumberInfo(RegexBasedMatcher.create());
    private static final Set<String> REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT = new HashSet();
    private static final Logger logger = Logger.getLogger(ShortNumberInfo.class.getName());
    private final Map<Integer, List<String>> countryCallingCodeToRegionCodeMap = CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap();
    private final MatcherApi matcherApi;

    /* renamed from: com.google.i18n.phonenumbers.ShortNumberInfo$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost = new int[ShortNumberCost.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            $SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[ShortNumberCost.PREMIUM_RATE.ordinal()] = 1;
            $SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[ShortNumberCost.UNKNOWN_COST.ordinal()] = 2;
            $SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[ShortNumberCost.STANDARD_RATE.ordinal()] = 3;
            try {
                $SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[ShortNumberCost.TOLL_FREE.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public enum ShortNumberCost {
        TOLL_FREE,
        STANDARD_RATE,
        PREMIUM_RATE,
        UNKNOWN_COST
    }

    static {
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("BR");
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("CL");
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("NI");
    }

    public static ShortNumberInfo getInstance() {
        return INSTANCE;
    }

    ShortNumberInfo(MatcherApi matcherApi2) {
        this.matcherApi = matcherApi2;
    }

    private List<String> getRegionCodesForCountryCode(int i) {
        List list = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(i));
        if (list == null) {
            list = new ArrayList(0);
        }
        return Collections.unmodifiableList(list);
    }

    private boolean regionDialingFromMatchesNumber(PhoneNumber phoneNumber, String str) {
        return getRegionCodesForCountryCode(phoneNumber.getCountryCode()).contains(str);
    }

    public boolean isPossibleShortNumberForRegion(PhoneNumber phoneNumber, String str) {
        if (!regionDialingFromMatchesNumber(phoneNumber, str)) {
            return false;
        }
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        if (shortNumberMetadataForRegion == null) {
            return false;
        }
        return shortNumberMetadataForRegion.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(getNationalSignificantNumber(phoneNumber).length()));
    }

    public boolean isPossibleShortNumber(PhoneNumber phoneNumber) {
        List<String> regionCodesForCountryCode = getRegionCodesForCountryCode(phoneNumber.getCountryCode());
        int length = getNationalSignificantNumber(phoneNumber).length();
        for (String shortNumberMetadataForRegion : regionCodesForCountryCode) {
            PhoneMetadata shortNumberMetadataForRegion2 = MetadataManager.getShortNumberMetadataForRegion(shortNumberMetadataForRegion);
            if (shortNumberMetadataForRegion2 != null && shortNumberMetadataForRegion2.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(length))) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidShortNumberForRegion(PhoneNumber phoneNumber, String str) {
        if (!regionDialingFromMatchesNumber(phoneNumber, str)) {
            return false;
        }
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        if (shortNumberMetadataForRegion == null) {
            return false;
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phoneNumber);
        if (!matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getGeneralDesc())) {
            return false;
        }
        return matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getShortCode());
    }

    public boolean isValidShortNumber(PhoneNumber phoneNumber) {
        List regionCodesForCountryCode = getRegionCodesForCountryCode(phoneNumber.getCountryCode());
        String regionCodeForShortNumberFromRegionList = getRegionCodeForShortNumberFromRegionList(phoneNumber, regionCodesForCountryCode);
        if (regionCodesForCountryCode.size() <= 1 || regionCodeForShortNumberFromRegionList == null) {
            return isValidShortNumberForRegion(phoneNumber, regionCodeForShortNumberFromRegionList);
        }
        return true;
    }

    public ShortNumberCost getExpectedCostForRegion(PhoneNumber phoneNumber, String str) {
        if (!regionDialingFromMatchesNumber(phoneNumber, str)) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        if (shortNumberMetadataForRegion == null) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phoneNumber);
        if (!shortNumberMetadataForRegion.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(nationalSignificantNumber.length()))) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        if (matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getPremiumRate())) {
            return ShortNumberCost.PREMIUM_RATE;
        }
        if (matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getStandardRate())) {
            return ShortNumberCost.STANDARD_RATE;
        }
        if (matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getTollFree())) {
            return ShortNumberCost.TOLL_FREE;
        }
        if (isEmergencyNumber(nationalSignificantNumber, str)) {
            return ShortNumberCost.TOLL_FREE;
        }
        return ShortNumberCost.UNKNOWN_COST;
    }

    public ShortNumberCost getExpectedCost(PhoneNumber phoneNumber) {
        List<String> regionCodesForCountryCode = getRegionCodesForCountryCode(phoneNumber.getCountryCode());
        if (regionCodesForCountryCode.size() == 0) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        if (regionCodesForCountryCode.size() == 1) {
            return getExpectedCostForRegion(phoneNumber, (String) regionCodesForCountryCode.get(0));
        }
        ShortNumberCost shortNumberCost = ShortNumberCost.TOLL_FREE;
        for (String expectedCostForRegion : regionCodesForCountryCode) {
            ShortNumberCost expectedCostForRegion2 = getExpectedCostForRegion(phoneNumber, expectedCostForRegion);
            int i = AnonymousClass1.$SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[expectedCostForRegion2.ordinal()];
            if (i == 1) {
                return ShortNumberCost.PREMIUM_RATE;
            }
            if (i == 2) {
                shortNumberCost = ShortNumberCost.UNKNOWN_COST;
            } else if (i != 3) {
                if (i != 4) {
                    Logger logger2 = logger;
                    Level level = Level.SEVERE;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognised cost for region: ");
                    sb.append(expectedCostForRegion2);
                    logger2.log(level, sb.toString());
                }
            } else if (shortNumberCost != ShortNumberCost.UNKNOWN_COST) {
                shortNumberCost = ShortNumberCost.STANDARD_RATE;
            }
        }
        return shortNumberCost;
    }

    private String getRegionCodeForShortNumberFromRegionList(PhoneNumber phoneNumber, List<String> list) {
        if (list.size() == 0) {
            return null;
        }
        if (list.size() == 1) {
            return (String) list.get(0);
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phoneNumber);
        for (String str : list) {
            PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
            if (shortNumberMetadataForRegion != null && matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getShortCode())) {
                return str;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public Set<String> getSupportedRegions() {
        return MetadataManager.getSupportedShortNumberRegions();
    }

    /* access modifiers changed from: 0000 */
    public String getExampleShortNumber(String str) {
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        String str2 = "";
        if (shortNumberMetadataForRegion == null) {
            return str2;
        }
        PhoneNumberDesc shortCode = shortNumberMetadataForRegion.getShortCode();
        return shortCode.hasExampleNumber() ? shortCode.getExampleNumber() : str2;
    }

    /* access modifiers changed from: 0000 */
    public String getExampleShortNumberForCost(String str, ShortNumberCost shortNumberCost) {
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        String str2 = "";
        if (shortNumberMetadataForRegion == null) {
            return str2;
        }
        PhoneNumberDesc phoneNumberDesc = null;
        int i = AnonymousClass1.$SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[shortNumberCost.ordinal()];
        if (i == 1) {
            phoneNumberDesc = shortNumberMetadataForRegion.getPremiumRate();
        } else if (i == 3) {
            phoneNumberDesc = shortNumberMetadataForRegion.getStandardRate();
        } else if (i == 4) {
            phoneNumberDesc = shortNumberMetadataForRegion.getTollFree();
        }
        return (phoneNumberDesc == null || !phoneNumberDesc.hasExampleNumber()) ? str2 : phoneNumberDesc.getExampleNumber();
    }

    public boolean connectsToEmergencyNumber(String str, String str2) {
        return matchesEmergencyNumberHelper(str, str2, true);
    }

    public boolean isEmergencyNumber(CharSequence charSequence, String str) {
        return matchesEmergencyNumberHelper(charSequence, str, false);
    }

    private boolean matchesEmergencyNumberHelper(CharSequence charSequence, String str, boolean z) {
        CharSequence extractPossibleNumber = PhoneNumberUtil.extractPossibleNumber(charSequence);
        boolean z2 = false;
        if (PhoneNumberUtil.PLUS_CHARS_PATTERN.matcher(extractPossibleNumber).lookingAt()) {
            return false;
        }
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        if (shortNumberMetadataForRegion == null || !shortNumberMetadataForRegion.hasEmergency()) {
            return false;
        }
        String normalizeDigitsOnly = PhoneNumberUtil.normalizeDigitsOnly(extractPossibleNumber);
        if (z && !REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.contains(str)) {
            z2 = true;
        }
        return this.matcherApi.matchNationalNumber(normalizeDigitsOnly, shortNumberMetadataForRegion.getEmergency(), z2);
    }

    public boolean isCarrierSpecific(PhoneNumber phoneNumber) {
        String regionCodeForShortNumberFromRegionList = getRegionCodeForShortNumberFromRegionList(phoneNumber, getRegionCodesForCountryCode(phoneNumber.getCountryCode()));
        String nationalSignificantNumber = getNationalSignificantNumber(phoneNumber);
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(regionCodeForShortNumberFromRegionList);
        return shortNumberMetadataForRegion != null && matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getCarrierSpecific());
    }

    public boolean isCarrierSpecificForRegion(PhoneNumber phoneNumber, String str) {
        boolean z = false;
        if (!regionDialingFromMatchesNumber(phoneNumber, str)) {
            return false;
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phoneNumber);
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        if (shortNumberMetadataForRegion != null && matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getCarrierSpecific())) {
            z = true;
        }
        return z;
    }

    public boolean isSmsServiceForRegion(PhoneNumber phoneNumber, String str) {
        boolean z = false;
        if (!regionDialingFromMatchesNumber(phoneNumber, str)) {
            return false;
        }
        PhoneMetadata shortNumberMetadataForRegion = MetadataManager.getShortNumberMetadataForRegion(str);
        if (shortNumberMetadataForRegion != null && matchesPossibleNumberAndNationalNumber(getNationalSignificantNumber(phoneNumber), shortNumberMetadataForRegion.getSmsServices())) {
            z = true;
        }
        return z;
    }

    private static String getNationalSignificantNumber(PhoneNumber phoneNumber) {
        StringBuilder sb = new StringBuilder();
        if (phoneNumber.isItalianLeadingZero()) {
            char[] cArr = new char[phoneNumber.getNumberOfLeadingZeros()];
            Arrays.fill(cArr, '0');
            sb.append(new String(cArr));
        }
        sb.append(phoneNumber.getNationalNumber());
        return sb.toString();
    }

    private boolean matchesPossibleNumberAndNationalNumber(String str, PhoneNumberDesc phoneNumberDesc) {
        if (phoneNumberDesc.getPossibleLengthCount() <= 0 || phoneNumberDesc.getPossibleLengthList().contains(Integer.valueOf(str.length()))) {
            return this.matcherApi.matchNationalNumber(str, phoneNumberDesc, false);
        }
        return false;
    }
}
