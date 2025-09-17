package dev.prodzeus.utilities.formatting;

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
     */
    @Contract(pure = true)
    public static void toMinecraftSmallFont(@NotNull final String text)throws IllegalArgumentException {
        text.chars().forEach(c -> c = getMinecraftFontChar((char) c));
    }

    /**
     * Get the corresponding Minecraft Small Font character. Accepts a-z & A-Z
     * @param c Char to compare with.
     * @return Character from the Minecraft Small Font.
     * @throws IllegalArgumentException Should the provided char not have a corresponding character.
     */
    private static char getMinecraftFontChar(final char c)throws IllegalArgumentException {
        return switch(c) {
            case 'a','A' -> 'ᴀ';
            case 'b','B' -> 'ʙ';
            case 'c','C' -> 'ᴄ';
            case 'd','D' -> 'ᴅ';
            case 'e','E' -> 'ᴇ';
            case 'f','F' -> 'ғ';
            case 'g','G' -> 'ɢ';
            case 'h','H' -> 'ʜ';
            case 'i','I' -> 'ɪ';
            case 'j','J' -> 'ᴊ';
            case 'k','K' -> 'ᴋ';
            case 'l','L' -> 'ʟ';
            case 'm','M' -> 'ᴍ';
            case 'n','N' -> 'ɴ';
            case 'o','O' -> 'ᴏ';
            case 'p','P' -> 'ᴘ';
            case 'q','Q' -> 'ǫ';
            case 'r','R' -> 'ʀ';
            case 's','S' -> 's';
            case 't','T' -> 'ᴛ';
            case 'u','U' -> 'ᴜ';
            case 'v','V' -> 'ᴠ';
            case 'w','W' -> 'ᴡ';
            case 'x','X' -> 'x';
            case 'y','Y' -> 'ʏ';
            case 'z','Z' -> 'ᴢ';
            case ' ' -> ' ';
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    private static char getNormalCharFromMinecraftChar(final char c) {
        return switch(c) {
            case 'ᴀ' -> 'a';
            case 'ʙ' -> 'b';
            case 'ᴄ' -> 'c';
            case 'ᴅ' -> 'd';
            case 'ᴇ' -> 'e';
            case 'ғ' -> 'f';
            case 'ɢ' -> 'g';
            case 'ʜ' -> 'h';
            case 'ɪ' -> 'i';
            case 'ᴊ' -> 'j';
            case 'ᴋ' -> 'k';
            case 'ʟ' -> 'l';
            case 'ᴍ' -> 'm';
            case 'ɴ' -> 'n';
            case 'ᴏ' -> 'o';
            case 'ᴘ' -> 'p';
            case 'ǫ' -> 'q';
            case 'ʀ' -> 'r';
            case 's' -> 's';
            case 'ᴛ' -> 't';
            case 'ᴜ' -> 'u';
            case 'ᴠ' -> 'v';
            case 'ᴡ' -> 'w';
            case 'x' -> 'x';
            case 'ʏ' -> 'y';
            case 'ᴢ' -> 'z';
            case ' ' -> ' ';
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    /**
     * Converts "Minecraft Small Font" letters from A-Z to normal letters.
     *
     * @param locale Locale to format text to. (Letter capitalization)
     * @param text Text to convert.
     */
    @Contract(pure = true)
    public static void fromMinecraftSmallFont(@NotNull final String text, final Locale locale) {
        text.chars().forEach(c -> c = getMinecraftFontChar((char) c));
        if (locale != null) {
            String copy = text.toUpperCase(locale);
            for(int i = 0 ; i < copy.length() ; i++) {
                char checked = copy.charAt(i);
                if (Character.isUpperCase(checked)) {
                    text.chars().forEach(c -> {
                        if (c==Character.toLowerCase(checked)) {
                            c = checked;
                        }
                    });
                    String c = String.valueOf(copy.charAt(i)).toLowerCase();
                }
            }
        }
    }

    /**
     * Converts "Minecraft Small Font" letters from A-Z to normal letters.
     *
     * @param text Text to convert.
     */
    @Contract(pure = true)
    public static void fromMinecraftSmallFont(@NotNull final String text) {
        fromMinecraftSmallFont(text, null);
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

    /**
     * Format seconds to time.
     * @param time Time in seconds
     * @param withDescriptions Whether to add "Seconds", "Minutes", and "Hours" to the final string.
     * @return Formatted string of time.
     */
    @Contract(pure = true)
    public static @NotNull String formatTime(final double time, final boolean withDescriptions) {
        final int convertedTime = (int) time;
        final int hours = (convertedTime / 60) / 60;
        final int minutes = (convertedTime / 60) % 60;
        final int seconds = convertedTime % 60;
        if (hours != 0) {
            if (withDescriptions) {
                return "%s %s, %s %s, and %s %s".formatted(String.valueOf(hours),hours > 1 ? "hours" : "hour",
                        String.valueOf(minutes), minutes == 1 ? "minute" : "minutes", String.valueOf(seconds), seconds == 1 ? "second" : "seconds");
            }
            return hours + ":" + minutes + ":" + seconds;
        } else if (minutes != 0) {
            if (withDescriptions) {
                return "%s %s and %s %s".formatted(String.valueOf(minutes), minutes == 1 ? "minute" : "minutes", String.valueOf(seconds), seconds == 1 ? "second" : "seconds");
            } else {
                return minutes + ":" + seconds;
            }
        } else if (withDescriptions) {
            return "%s %s".formatted(String.valueOf(seconds), seconds == 1 ? "second" : "seconds");
        } else return "00:" + seconds;
    }
}
