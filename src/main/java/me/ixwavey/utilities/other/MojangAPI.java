package me.ixwavey.utilities.other;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for making use of Mojang's API.
 * <br>
 * Methods will return null in case of being wrongly used.
 * The most likely reason for having null returned is a failed request.
 */
@SuppressWarnings("unused")
public class MojangAPI {
    /**
     * Get a player's {@link UUID} from their username.
     * @param username The player's username.
     * @return The player's UUID.
     */
    @SneakyThrows
    @Contract(pure = true, value = "null -> null")
    public static UUID getUUID(final String username) {
        final Pattern regex = Pattern.compile("(?i)id\"\\s*:\\s*\"(?<uuid>.*?)\"");
        final URL url = new URI("https://api.mojang.com/users/profiles/minecraft/%s".formatted(username)).toURL();

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection(); con.setRequestMethod("GET");
        final InputStream inputStream = con.getInputStream();
        con.disconnect();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder data = new StringBuilder();
        String readLine;

        while ((readLine = reader.readLine()) != null) data.append(readLine);

        Matcher matchData = regex.matcher(data.toString());

        if (matchData.find()) {
            String rawUUID = matchData.group("uuid");
            for (int i = 0; i < 33; i++) {
                if (i == 8 || i == 13 || i == 18 || i == 23) {
                    final String targetUUIDString = new StringBuilder(rawUUID).insert(i, '-').toString();
                    rawUUID = targetUUIDString;
                    if (i == 23) {
                        return UUID.fromString(targetUUIDString);
                    }
                }
            }
        }
        return null;
    }
}
