package xyz.baqel.bcore.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

public class JavaUtil {

    private static CharMatcher CHAR_MATCHER_ASCII;
	    
    private static Pattern UUID_PATTERN;
    
    private static int DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES = 5;
    
    static {
        CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').or(CharMatcher.inRange('a', 'z')).or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.WHITESPACE).precomputed();
        UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
    }

    public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Float tryParseFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    public static boolean isUUID(String string) {
        return JavaUtil.UUID_PATTERN.matcher(string).find();
    }
    
    public static boolean containsIgnoreCase(Iterable<? extends String> elements, String string) {
        for (String element : elements) {
            if (StringUtils.containsIgnoreCase(element, string)) {
                return true;
            }
        }
        return false;
    }
    
    public static String format(Number number) {
        return format(number, DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES);
    }
    
    public static String format(Number number, int decimalPlaces) {
        return format(number, decimalPlaces, RoundingMode.HALF_DOWN);
    }
    
    public static String format(Number number, int decimalPlaces, RoundingMode roundingMode) {
        Preconditions.checkNotNull((Object)number, (Object)"The number cannot be null");
        return new BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros().toPlainString();
    }
    
    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd) {
        return andJoin(collection, delimiterBeforeAnd, ", ");
    }
    
    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        List<String> contents = new ArrayList<String>(collection);
        String last = contents.remove(contents.size() - 1);
        StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join(contents));
        if (delimiterBeforeAnd) {
            builder.append(delimiter);
        }
        return builder.append(" and ").append(last).toString();
    }
    
    public static String toRomanNumeral(int input) {
        if (input < 1 || input > 3999)
            return "Invalid Roman Number Value";
        String s = "";
        while (input >= 1000) {
            s += "M";
            input -= 1000;        }
        while (input >= 900) {
            s += "CM";
            input -= 900;
        }
        while (input >= 500) {
            s += "D";
            input -= 500;
        }
        while (input >= 400) {
            s += "CD";
            input -= 400;
        }
        while (input >= 100) {
            s += "C";
            input -= 100;
        }
        while (input >= 90) {
            s += "XC";
            input -= 90;
        }
        while (input >= 50) {
            s += "L";
            input -= 50;
        }
        while (input >= 40) {
            s += "XL";
            input -= 40;
        }
        while (input >= 10) {
            s += "X";
            input -= 10;
        }
        while (input >= 9) {
            s += "IX";
            input -= 9;
        }
        while (input >= 5) {
            s += "V";
            input -= 5;
        }
        while (input >= 4) {
            s += "IV";
            input -= 4;
        }
        while (input >= 1) {
            s += "I";
            input -= 1;
        }
        return s;
    }
    
    public static int getDEFAULT_NUMBER_FORMAT_DECIMAL_PLACES() {
		return DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES;
	}
    
    public static Pattern getUUID_PATTERN() {
		return UUID_PATTERN;
	}
}
