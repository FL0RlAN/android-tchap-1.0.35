package org.matrix.androidsdk.rest.json;

import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.util.Locale;

public class MatrixFieldNamingStrategy implements FieldNamingStrategy {
    private static String separateCamelCase(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt) && sb.length() != 0) {
                sb.append(str2);
            }
            sb.append(charAt);
        }
        return sb.toString();
    }

    public String translateName(Field field) {
        return separateCamelCase(field.getName(), "_").toLowerCase(Locale.ENGLISH);
    }
}
