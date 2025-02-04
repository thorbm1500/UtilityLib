package me.ixwavey.utilities.Formatting;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {

    /**
     * Format number for better readability.
     * <p></p>
     * @param locale                Locale format to be followed.
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return                      Formatted string.
     */
    public static String formatToString(double number, Locale locale, int minimumFractionDigits, int maximumFractionDigits){
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
        numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return numberFormatter.format(number);
    }

    /**
     * Format number with zero decimals, for better readability.
     * <p></p>
     * @param locale                Locale format to be followed.
     * @param number                Number to be formatted.
     * @return                      Formatted string.
     */
    public static String formatToString(double number, Locale locale){
        return formatToString(number,locale,0,0);
    }

    /**
     * Format number for better readability.
     * <p></p>
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return                      Formatted string.
     */
    public static String formatToString(double number, int minimumFractionDigits, int maximumFractionDigits){
        NumberFormat numberFormatter = NumberFormat.getInstance();
        numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return numberFormatter.format(number);
    }

    /**
     * Format number with zero decimals, for better readability.
     * <p></p>
     * @param number                Number to be formatted.
     * @return                      Formatted string.
     */
    public static String formatToString(double number){
        return formatToString(number,0,0);
    }

    /**
     * Format number for better readability.
     * <p></p>
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return                      Formatted string.
     */
    public static double formatToDouble(double number, int minimumFractionDigits, int maximumFractionDigits){
        NumberFormat numberFormatter = NumberFormat.getIntegerInstance();
        //numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        //numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return Double.parseDouble(numberFormatter.format(number));
    }

    /**
     * Round up or down to the nearest whole number.
     * <p></p>
     * @param number                Number to be formatted.
     * @return                      Formatted number.
     */
    public static double formatRoundDouble(double number){
        return Double.parseDouble(NumberFormat.getIntegerInstance().format(number));
    }

}
