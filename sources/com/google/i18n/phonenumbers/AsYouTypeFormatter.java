package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonemetadata.NumberFormat;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.internal.RegexCache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsYouTypeFormatter {
    private static final Pattern CHARACTER_CLASS_PATTERN = Pattern.compile("\\[([^\\[\\]])*\\]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(DIGIT_PLACEHOLDER);
    private static final String DIGIT_PLACEHOLDER = " ";
    private static final Pattern ELIGIBLE_FORMAT_PATTERN = Pattern.compile("[-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～]*(\\$\\d[-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～]*)+");
    private static final PhoneMetadata EMPTY_METADATA = new PhoneMetadata().setInternationalPrefix("NA");
    private static final int MIN_LEADING_DIGITS_LENGTH = 3;
    private static final Pattern NATIONAL_PREFIX_SEPARATORS_PATTERN = Pattern.compile("[- ]");
    private static final char SEPARATOR_BEFORE_NATIONAL_NUMBER = ' ';
    private static final Pattern STANDALONE_DIGIT_PATTERN = Pattern.compile("\\d(?=[^,}][^,}])");
    private boolean ableToFormat;
    private StringBuilder accruedInput;
    private StringBuilder accruedInputWithoutFormatting;
    private String currentFormattingPattern;
    private PhoneMetadata currentMetadata;
    private String currentOutput;
    private String defaultCountry;
    private PhoneMetadata defaultMetadata;
    private String extractedNationalPrefix;
    private StringBuilder formattingTemplate = new StringBuilder();
    private boolean inputHasFormatting;
    private boolean isCompleteNumber;
    private boolean isExpectingCountryCallingCode;
    private int lastMatchPosition;
    private StringBuilder nationalNumber;
    private int originalPosition;
    private final PhoneNumberUtil phoneUtil;
    private int positionToRemember;
    private List<NumberFormat> possibleFormats;
    private StringBuilder prefixBeforeNationalNumber;
    private RegexCache regexCache;
    private boolean shouldAddSpaceAfterNationalPrefix;

    AsYouTypeFormatter(String str) {
        String str2 = "";
        this.currentOutput = str2;
        this.currentFormattingPattern = str2;
        this.accruedInput = new StringBuilder();
        this.accruedInputWithoutFormatting = new StringBuilder();
        this.ableToFormat = true;
        this.inputHasFormatting = false;
        this.isCompleteNumber = false;
        this.isExpectingCountryCallingCode = false;
        this.phoneUtil = PhoneNumberUtil.getInstance();
        this.lastMatchPosition = 0;
        this.originalPosition = 0;
        this.positionToRemember = 0;
        this.prefixBeforeNationalNumber = new StringBuilder();
        this.shouldAddSpaceAfterNationalPrefix = false;
        this.extractedNationalPrefix = str2;
        this.nationalNumber = new StringBuilder();
        this.possibleFormats = new ArrayList();
        this.regexCache = new RegexCache(64);
        this.defaultCountry = str;
        this.currentMetadata = getMetadataForRegion(this.defaultCountry);
        this.defaultMetadata = this.currentMetadata;
    }

    private PhoneMetadata getMetadataForRegion(String str) {
        PhoneMetadata metadataForRegion = this.phoneUtil.getMetadataForRegion(this.phoneUtil.getRegionCodeForCountryCode(this.phoneUtil.getCountryCodeForRegion(str)));
        if (metadataForRegion != null) {
            return metadataForRegion;
        }
        return EMPTY_METADATA;
    }

    private boolean maybeCreateNewTemplate() {
        Iterator it = this.possibleFormats.iterator();
        while (it.hasNext()) {
            NumberFormat numberFormat = (NumberFormat) it.next();
            String pattern = numberFormat.getPattern();
            if (this.currentFormattingPattern.equals(pattern)) {
                return false;
            }
            if (createFormattingTemplate(numberFormat)) {
                this.currentFormattingPattern = pattern;
                this.shouldAddSpaceAfterNationalPrefix = NATIONAL_PREFIX_SEPARATORS_PATTERN.matcher(numberFormat.getNationalPrefixFormattingRule()).find();
                this.lastMatchPosition = 0;
                return true;
            }
            it.remove();
        }
        this.ableToFormat = false;
        return false;
    }

    private void getAvailableFormats(String str) {
        List<NumberFormat> list;
        if (!this.isCompleteNumber || this.currentMetadata.intlNumberFormatSize() <= 0) {
            list = this.currentMetadata.numberFormats();
        } else {
            list = this.currentMetadata.intlNumberFormats();
        }
        boolean hasNationalPrefix = this.currentMetadata.hasNationalPrefix();
        for (NumberFormat numberFormat : list) {
            if ((!hasNationalPrefix || this.isCompleteNumber || numberFormat.getNationalPrefixOptionalWhenFormatting() || PhoneNumberUtil.formattingRuleHasFirstGroupOnly(numberFormat.getNationalPrefixFormattingRule())) && isFormatEligible(numberFormat.getFormat())) {
                this.possibleFormats.add(numberFormat);
            }
        }
        narrowDownPossibleFormats(str);
    }

    private boolean isFormatEligible(String str) {
        return ELIGIBLE_FORMAT_PATTERN.matcher(str).matches();
    }

    private void narrowDownPossibleFormats(String str) {
        int length = str.length() - 3;
        Iterator it = this.possibleFormats.iterator();
        while (it.hasNext()) {
            NumberFormat numberFormat = (NumberFormat) it.next();
            if (numberFormat.leadingDigitsPatternSize() != 0) {
                if (!this.regexCache.getPatternForRegex(numberFormat.getLeadingDigitsPattern(Math.min(length, numberFormat.leadingDigitsPatternSize() - 1))).matcher(str).lookingAt()) {
                    it.remove();
                }
            }
        }
    }

    private boolean createFormattingTemplate(NumberFormat numberFormat) {
        String pattern = numberFormat.getPattern();
        if (pattern.indexOf(124) != -1) {
            return false;
        }
        String str = "\\\\d";
        String replaceAll = STANDALONE_DIGIT_PATTERN.matcher(CHARACTER_CLASS_PATTERN.matcher(pattern).replaceAll(str)).replaceAll(str);
        this.formattingTemplate.setLength(0);
        String formattingTemplate2 = getFormattingTemplate(replaceAll, numberFormat.getFormat());
        if (formattingTemplate2.length() <= 0) {
            return false;
        }
        this.formattingTemplate.append(formattingTemplate2);
        return true;
    }

    private String getFormattingTemplate(String str, String str2) {
        Matcher matcher = this.regexCache.getPatternForRegex(str).matcher("999999999999999");
        matcher.find();
        String group = matcher.group();
        if (group.length() < this.nationalNumber.length()) {
            return "";
        }
        return group.replaceAll(str, str2).replaceAll("9", DIGIT_PLACEHOLDER);
    }

    public void clear() {
        String str = "";
        this.currentOutput = str;
        this.accruedInput.setLength(0);
        this.accruedInputWithoutFormatting.setLength(0);
        this.formattingTemplate.setLength(0);
        this.lastMatchPosition = 0;
        this.currentFormattingPattern = str;
        this.prefixBeforeNationalNumber.setLength(0);
        this.extractedNationalPrefix = str;
        this.nationalNumber.setLength(0);
        this.ableToFormat = true;
        this.inputHasFormatting = false;
        this.positionToRemember = 0;
        this.originalPosition = 0;
        this.isCompleteNumber = false;
        this.isExpectingCountryCallingCode = false;
        this.possibleFormats.clear();
        this.shouldAddSpaceAfterNationalPrefix = false;
        if (!this.currentMetadata.equals(this.defaultMetadata)) {
            this.currentMetadata = getMetadataForRegion(this.defaultCountry);
        }
    }

    public String inputDigit(char c) {
        this.currentOutput = inputDigitWithOptionToRememberPosition(c, false);
        return this.currentOutput;
    }

    public String inputDigitAndRememberPosition(char c) {
        this.currentOutput = inputDigitWithOptionToRememberPosition(c, true);
        return this.currentOutput;
    }

    private String inputDigitWithOptionToRememberPosition(char c, boolean z) {
        String str;
        this.accruedInput.append(c);
        if (z) {
            this.originalPosition = this.accruedInput.length();
        }
        if (!isDigitOrLeadingPlusSign(c)) {
            this.ableToFormat = false;
            this.inputHasFormatting = true;
        } else {
            c = normalizeAndAccrueDigitsAndPlusSign(c, z);
        }
        if (this.ableToFormat) {
            int length = this.accruedInputWithoutFormatting.length();
            if (length == 0 || length == 1 || length == 2) {
                return this.accruedInput.toString();
            }
            if (length == 3) {
                if (attemptToExtractIdd()) {
                    this.isExpectingCountryCallingCode = true;
                } else {
                    this.extractedNationalPrefix = removeNationalPrefixFromNationalNumber();
                    return attemptToChooseFormattingPattern();
                }
            }
            if (this.isExpectingCountryCallingCode) {
                if (attemptToExtractCountryCallingCode()) {
                    this.isExpectingCountryCallingCode = false;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(this.prefixBeforeNationalNumber);
                sb.append(this.nationalNumber.toString());
                return sb.toString();
            } else if (this.possibleFormats.size() <= 0) {
                return attemptToChooseFormattingPattern();
            } else {
                String inputDigitHelper = inputDigitHelper(c);
                String attemptToFormatAccruedDigits = attemptToFormatAccruedDigits();
                if (attemptToFormatAccruedDigits.length() > 0) {
                    return attemptToFormatAccruedDigits;
                }
                narrowDownPossibleFormats(this.nationalNumber.toString());
                if (maybeCreateNewTemplate()) {
                    return inputAccruedNationalNumber();
                }
                if (this.ableToFormat) {
                    str = appendNationalNumber(inputDigitHelper);
                } else {
                    str = this.accruedInput.toString();
                }
                return str;
            }
        } else if (this.inputHasFormatting) {
            return this.accruedInput.toString();
        } else {
            if (attemptToExtractIdd()) {
                if (attemptToExtractCountryCallingCode()) {
                    return attemptToChoosePatternWithPrefixExtracted();
                }
            } else if (ableToExtractLongerNdd()) {
                this.prefixBeforeNationalNumber.append(SEPARATOR_BEFORE_NATIONAL_NUMBER);
                return attemptToChoosePatternWithPrefixExtracted();
            }
            return this.accruedInput.toString();
        }
    }

    private String attemptToChoosePatternWithPrefixExtracted() {
        this.ableToFormat = true;
        this.isExpectingCountryCallingCode = false;
        this.possibleFormats.clear();
        this.lastMatchPosition = 0;
        this.formattingTemplate.setLength(0);
        this.currentFormattingPattern = "";
        return attemptToChooseFormattingPattern();
    }

    /* access modifiers changed from: 0000 */
    public String getExtractedNationalPrefix() {
        return this.extractedNationalPrefix;
    }

    private boolean ableToExtractLongerNdd() {
        if (this.extractedNationalPrefix.length() > 0) {
            this.nationalNumber.insert(0, this.extractedNationalPrefix);
            this.prefixBeforeNationalNumber.setLength(this.prefixBeforeNationalNumber.lastIndexOf(this.extractedNationalPrefix));
        }
        return !this.extractedNationalPrefix.equals(removeNationalPrefixFromNationalNumber());
    }

    private boolean isDigitOrLeadingPlusSign(char c) {
        if (Character.isDigit(c)) {
            return true;
        }
        if (this.accruedInput.length() != 1 || !PhoneNumberUtil.PLUS_CHARS_PATTERN.matcher(Character.toString(c)).matches()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public String attemptToFormatAccruedDigits() {
        for (NumberFormat numberFormat : this.possibleFormats) {
            Matcher matcher = this.regexCache.getPatternForRegex(numberFormat.getPattern()).matcher(this.nationalNumber);
            if (matcher.matches()) {
                this.shouldAddSpaceAfterNationalPrefix = NATIONAL_PREFIX_SEPARATORS_PATTERN.matcher(numberFormat.getNationalPrefixFormattingRule()).find();
                return appendNationalNumber(matcher.replaceAll(numberFormat.getFormat()));
            }
        }
        return "";
    }

    public int getRememberedPosition() {
        if (!this.ableToFormat) {
            return this.originalPosition;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.positionToRemember && i2 < this.currentOutput.length()) {
            if (this.accruedInputWithoutFormatting.charAt(i) == this.currentOutput.charAt(i2)) {
                i++;
            }
            i2++;
        }
        return i2;
    }

    private String appendNationalNumber(String str) {
        int length = this.prefixBeforeNationalNumber.length();
        if (!this.shouldAddSpaceAfterNationalPrefix || length <= 0 || this.prefixBeforeNationalNumber.charAt(length - 1) == ' ') {
            StringBuilder sb = new StringBuilder();
            sb.append(this.prefixBeforeNationalNumber);
            sb.append(str);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(new String(this.prefixBeforeNationalNumber));
        sb2.append(SEPARATOR_BEFORE_NATIONAL_NUMBER);
        sb2.append(str);
        return sb2.toString();
    }

    private String attemptToChooseFormattingPattern() {
        if (this.nationalNumber.length() < 3) {
            return appendNationalNumber(this.nationalNumber.toString());
        }
        getAvailableFormats(this.nationalNumber.toString());
        String attemptToFormatAccruedDigits = attemptToFormatAccruedDigits();
        if (attemptToFormatAccruedDigits.length() > 0) {
            return attemptToFormatAccruedDigits;
        }
        return maybeCreateNewTemplate() ? inputAccruedNationalNumber() : this.accruedInput.toString();
    }

    private String inputAccruedNationalNumber() {
        int length = this.nationalNumber.length();
        if (length <= 0) {
            return this.prefixBeforeNationalNumber.toString();
        }
        String str = "";
        for (int i = 0; i < length; i++) {
            str = inputDigitHelper(this.nationalNumber.charAt(i));
        }
        return this.ableToFormat ? appendNationalNumber(str) : this.accruedInput.toString();
    }

    private boolean isNanpaNumberWithNationalPrefix() {
        if (this.currentMetadata.getCountryCode() != 1 || this.nationalNumber.charAt(0) != '1' || this.nationalNumber.charAt(1) == '0' || this.nationalNumber.charAt(1) == '1') {
            return false;
        }
        return true;
    }

    private String removeNationalPrefixFromNationalNumber() {
        int i = 1;
        if (isNanpaNumberWithNationalPrefix()) {
            StringBuilder sb = this.prefixBeforeNationalNumber;
            sb.append('1');
            sb.append(SEPARATOR_BEFORE_NATIONAL_NUMBER);
            this.isCompleteNumber = true;
        } else {
            if (this.currentMetadata.hasNationalPrefixForParsing()) {
                Matcher matcher = this.regexCache.getPatternForRegex(this.currentMetadata.getNationalPrefixForParsing()).matcher(this.nationalNumber);
                if (matcher.lookingAt() && matcher.end() > 0) {
                    this.isCompleteNumber = true;
                    i = matcher.end();
                    this.prefixBeforeNationalNumber.append(this.nationalNumber.substring(0, i));
                }
            }
            i = 0;
        }
        String substring = this.nationalNumber.substring(0, i);
        this.nationalNumber.delete(0, i);
        return substring;
    }

    private boolean attemptToExtractIdd() {
        RegexCache regexCache2 = this.regexCache;
        StringBuilder sb = new StringBuilder();
        sb.append("\\+|");
        sb.append(this.currentMetadata.getInternationalPrefix());
        Matcher matcher = regexCache2.getPatternForRegex(sb.toString()).matcher(this.accruedInputWithoutFormatting);
        if (!matcher.lookingAt()) {
            return false;
        }
        this.isCompleteNumber = true;
        int end = matcher.end();
        this.nationalNumber.setLength(0);
        this.nationalNumber.append(this.accruedInputWithoutFormatting.substring(end));
        this.prefixBeforeNationalNumber.setLength(0);
        this.prefixBeforeNationalNumber.append(this.accruedInputWithoutFormatting.substring(0, end));
        if (this.accruedInputWithoutFormatting.charAt(0) != '+') {
            this.prefixBeforeNationalNumber.append(SEPARATOR_BEFORE_NATIONAL_NUMBER);
        }
        return true;
    }

    private boolean attemptToExtractCountryCallingCode() {
        if (this.nationalNumber.length() == 0) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        int extractCountryCode = this.phoneUtil.extractCountryCode(this.nationalNumber, sb);
        if (extractCountryCode == 0) {
            return false;
        }
        this.nationalNumber.setLength(0);
        this.nationalNumber.append(sb);
        String regionCodeForCountryCode = this.phoneUtil.getRegionCodeForCountryCode(extractCountryCode);
        if (PhoneNumberUtil.REGION_CODE_FOR_NON_GEO_ENTITY.equals(regionCodeForCountryCode)) {
            this.currentMetadata = this.phoneUtil.getMetadataForNonGeographicalRegion(extractCountryCode);
        } else if (!regionCodeForCountryCode.equals(this.defaultCountry)) {
            this.currentMetadata = getMetadataForRegion(regionCodeForCountryCode);
        }
        String num = Integer.toString(extractCountryCode);
        StringBuilder sb2 = this.prefixBeforeNationalNumber;
        sb2.append(num);
        sb2.append(SEPARATOR_BEFORE_NATIONAL_NUMBER);
        this.extractedNationalPrefix = "";
        return true;
    }

    private char normalizeAndAccrueDigitsAndPlusSign(char c, boolean z) {
        if (c == '+') {
            this.accruedInputWithoutFormatting.append(c);
        } else {
            c = Character.forDigit(Character.digit(c, 10), 10);
            this.accruedInputWithoutFormatting.append(c);
            this.nationalNumber.append(c);
        }
        if (z) {
            this.positionToRemember = this.accruedInputWithoutFormatting.length();
        }
        return c;
    }

    private String inputDigitHelper(char c) {
        Matcher matcher = DIGIT_PATTERN.matcher(this.formattingTemplate);
        if (matcher.find(this.lastMatchPosition)) {
            String replaceFirst = matcher.replaceFirst(Character.toString(c));
            this.formattingTemplate.replace(0, replaceFirst.length(), replaceFirst);
            this.lastMatchPosition = matcher.start();
            return this.formattingTemplate.substring(0, this.lastMatchPosition + 1);
        }
        if (this.possibleFormats.size() == 1) {
            this.ableToFormat = false;
        }
        this.currentFormattingPattern = "";
        return this.accruedInput.toString();
    }
}
