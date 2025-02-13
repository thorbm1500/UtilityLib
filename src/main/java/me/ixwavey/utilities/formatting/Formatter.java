package me.ixwavey.utilities.formatting;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("unused")
public class Formatter {

    // ---------- Strings ----------

    /**
     * Converts normal letters from A-Z to the popular "Minecraft Small Font".
     *
     * @param text Text to convert.
     * @return Converted text.
     */
    @Contract(pure = true)
    public static @NotNull String toMinecraftSmallFont(String text) {
        text = text.toLowerCase();
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case 'a' -> formattedText.append("ᴀ");
                case 'b' -> formattedText.append("ʙ");
                case 'c' -> formattedText.append("ᴄ");
                case 'd' -> formattedText.append("ᴅ");
                case 'e' -> formattedText.append("ᴇ");
                case 'f' -> formattedText.append("ғ");
                case 'g' -> formattedText.append("ɢ");
                case 'h' -> formattedText.append("ʜ");
                case 'i' -> formattedText.append("ɪ");
                case 'j' -> formattedText.append("ᴊ");
                case 'k' -> formattedText.append("ᴋ");
                case 'l' -> formattedText.append("ʟ");
                case 'm' -> formattedText.append("ᴍ");
                case 'n' -> formattedText.append("ɴ");
                case 'o' -> formattedText.append("ᴏ");
                case 'p' -> formattedText.append("ᴘ");
                case 'q' -> formattedText.append("ǫ");
                case 'r' -> formattedText.append("ʀ");
                case 's' -> formattedText.append("s");
                case 't' -> formattedText.append("ᴛ");
                case 'u' -> formattedText.append("ᴜ");
                case 'v' -> formattedText.append("ᴠ");
                case 'w' -> formattedText.append("ᴡ");
                case 'x' -> formattedText.append("x");
                case 'y' -> formattedText.append("ʏ");
                case 'z' -> formattedText.append("ᴢ");
                case ' ' -> formattedText.append(" ");
            }
        }
        return formattedText.toString();
    }

    /**
     * Converts "Minecraft Small Font" letters from A-Z to normal letters.
     *
     * @param text Text to convert.
     * @return Converted text in lowercase.
     */
    @Contract(pure = true)
    public static @NotNull String fromMinecraftSmallFont(final @NotNull String text) {
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case 'ᴀ' -> formattedText.append("a");
                case 'ʙ' -> formattedText.append("b");
                case 'ᴄ' -> formattedText.append("c");
                case 'ᴅ' -> formattedText.append("d");
                case 'ᴇ' -> formattedText.append("e");
                case 'ғ' -> formattedText.append("f");
                case 'ɢ' -> formattedText.append("g");
                case 'ʜ' -> formattedText.append("h");
                case 'ɪ' -> formattedText.append("i");
                case 'ᴊ' -> formattedText.append("j");
                case 'ᴋ' -> formattedText.append("k");
                case 'ʟ' -> formattedText.append("l");
                case 'ᴍ' -> formattedText.append("m");
                case 'ɴ' -> formattedText.append("n");
                case 'ᴏ' -> formattedText.append("o");
                case 'ᴘ' -> formattedText.append("p");
                case 'ǫ' -> formattedText.append("q");
                case 'ʀ' -> formattedText.append("r");
                case 's' -> formattedText.append("s");
                case 'ᴛ' -> formattedText.append("t");
                case 'ᴜ' -> formattedText.append("u");
                case 'ᴠ' -> formattedText.append("v");
                case 'ᴡ' -> formattedText.append("w");
                case 'x' -> formattedText.append("x");
                case 'ʏ' -> formattedText.append("y");
                case 'ᴢ' -> formattedText.append("z");
                case ' ' -> formattedText.append(" ");
            }
        }
        return formattedText.toString();
    }

    /**
     * Check if the first letter of the {@link String} is a vowel.
     * <p>
     * Supports all nordic languages except Faroese.
     * </p>
     *
     * @param text {@link String} to check.
     * @return true | false
     */
    @Contract(pure = true)
    public static boolean isFirstLetterVowel(@NotNull String text) {
        switch (text.toLowerCase().charAt(0)) {
            case 'a', 'e', 'é', 'i', 'í', 'o', 'ó', 'u', 'ú', 'y', 'ý', 'æ', 'ä', 'á', 'ø', 'ö', 'å' -> {
                return true;
            }
        }
        return false;
    }

    // ---------- Numbers ----------

    /**
     * Format number for better readability.
     *
     * @param locale Locale format to be followed.
     * @param number Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    static @NotNull String toString(final int number, final Locale locale) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
        return numberFormatter.format(number);
    }

    /**
     * Format number for better readability.
     *
     * @param locale Locale format to be followed.
     * @param number Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static @NotNull String toString(final long number, final Locale locale) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
        return numberFormatter.format(number);
    }

    /**
     * Format number for better readability.
     *
     * @param locale                Locale format to be followed.
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final float number, final Locale locale, final int minimumFractionDigits, final int maximumFractionDigits) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
        numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return numberFormatter.format(number);
    }

    /**
     * Format number with zero decimals, for better readability.
     *
     * @param locale Locale format to be followed.
     * @param number Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final float number, final Locale locale) {
        return toString(number, locale, 0, 0);
    }

    /**
     * Format number for better readability.
     *
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final float number, final int minimumFractionDigits, final int maximumFractionDigits) {
        NumberFormat numberFormatter = NumberFormat.getInstance();
        numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return numberFormatter.format(number);
    }

    /**
     * Format number with zero decimals, for better readability.
     *
     * @param number Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final float number) {
        return toString(number, 0, 0);
    }

    /**
     * Format number for better readability.
     *
     * @param locale                Locale format to be followed.
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final double number, final Locale locale, final int minimumFractionDigits, final int maximumFractionDigits) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
        numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return numberFormatter.format(number);
    }

    /**
     * Format number with zero decimals, for better readability.
     *
     * @param locale Locale format to be followed.
     * @param number Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final double number, final Locale locale) {
        return toString(number, locale, 0, 0);
    }

    /**
     * Format number for better readability.
     *
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final double number, final int minimumFractionDigits, final int maximumFractionDigits) {
        NumberFormat numberFormatter = NumberFormat.getInstance();
        numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return numberFormatter.format(number);
    }

    /**
     * Format number with zero decimals, for better readability.
     *
     * @param number Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static String toString(final double number) {
        return toString(number, 0, 0);
    }

    /**
     * Format number for better readability.
     *
     * @param minimumFractionDigits The minimum mount of decimals shown.
     * @param maximumFractionDigits The maximum mount of decimals shown.
     * @param number                Number to be formatted.
     * @return Formatted string.
     */
    @Contract(pure = true)
    public static double toDouble(final double number, final int minimumFractionDigits, final int maximumFractionDigits) {
        NumberFormat numberFormatter = NumberFormat.getIntegerInstance();
        numberFormatter.setMinimumFractionDigits(minimumFractionDigits);
        numberFormatter.setMaximumFractionDigits(maximumFractionDigits);
        return Double.parseDouble(numberFormatter.format(number));
    }

    /**
     * Round up or down to the nearest whole number.
     *
     * @param number Number to be formatted.
     * @return Formatted number.
     */
    @Contract(pure = true)
    public static double round(final double number) {
        return Double.parseDouble(NumberFormat.getIntegerInstance().format(number));
    }

    /**
     * Round up or down to the nearest whole number.
     *
     * @param number Number to be formatted.
     * @return Formatted number.
     */
    @Contract(pure = true)
    public static float round(final float number) {
        return Float.parseFloat(NumberFormat.getIntegerInstance().format(number));
    }

    /**
     * Format number to percentage with two decimals.
     *
     * @param number Number to format.
     * @return Formatted percentage.
     */
    @Contract(pure = true)
    public static @NotNull String formatPercentage(final double number) {
        DecimalFormat numberFormat = new DecimalFormat("#.##");
        return numberFormat.format(number) + "%";
    }
}
