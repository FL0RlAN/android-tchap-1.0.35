package org.piwik.sdk.tools;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String priceString(Integer num) {
        if (num == null) {
            return null;
        }
        NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.US);
        numberInstance.setMinimumFractionDigits(2);
        double intValue = (double) num.intValue();
        Double.isNaN(intValue);
        return numberInstance.format(intValue / 100.0d);
    }
}
