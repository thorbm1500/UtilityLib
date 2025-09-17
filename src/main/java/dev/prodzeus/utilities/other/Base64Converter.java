package dev.prodzeus.utilities.other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Contract;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All utilities needed for converting from and to Base64 in various different cases.
 * @author Wonkglorg (Original Author), ixWavey
 */
@SuppressWarnings("unused")
public class Base64Converter {
    private static final Logger log = Logger.getLogger(Base64Converter.class.getName());

    private Base64Converter() {
        throw new IllegalStateException("Base64Converter class.");
    }

    //-----------------------------------FROM BASE64-----------------------------------

    /**
     * Converts a {@link Base64} encoded string back to an {@link Object}.
     *
     * @param str Base64 encoded string.
     * @return Decoded object or null if decoding fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static Object fromBase64(final String str) {
        byte[] data = Base64.getDecoder().decode(str);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {

            return ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to decode Base64 to object: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Converts a {@link Base64} encoded string back to a {@link List}.
     *
     * @param str Base64 encoded string.
     * @return Decoded object or null if decoding fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static List<Object> listFromBase64(final String str) {
        byte[] data = Base64.getDecoder().decode(str);
        try (ObjectInputStream dataInput = new ObjectInputStream(new ByteArrayInputStream(data))) {

            int length = dataInput.readInt();
            List<Object> items = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                items.add(dataInput.readObject());
            }
            return items;

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to decode Base64 to ArrayList: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Deserializes a {@link Base64} encoded string into a single {@link ItemStack}.
     *
     * @param data Base64 encoded string.
     * @return {@link ItemStack} or null if deserialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static ItemStack itemFromBase64(final String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            return (ItemStack) dataInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to deserialize Base64 to ItemStack: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Deserializes a {@link Base64} encoded string into a single {@link Material}.
     *
     * @param data Base64 encoded string.
     * @return {@link ItemStack} or null if deserialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static Material materialFromBase64(final String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            return (Material) dataInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to deserialize Base64 to ItemStack: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Deserializes a {@link Base64} encoded string into an array of {@link ItemStack}.
     *
     * @param data Base64 encoded string.
     * @return Array of {@link ItemStack}, or null if deserialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static ItemStack[] itemStackArrayFromBase64(final String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            int length = dataInput.readInt();
            ItemStack[] items = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            return items;

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to deserialize Base64 to ItemStack array: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Deserializes a {@link Base64} encoded string into an array of {@link Material}.
     *
     * @param data Base64 encoded string.
     * @return Array of {@link Material}, or null if deserialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static Material[] materialStackArrayFromBase64(final String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            int length = dataInput.readInt();
            Material[] items = new Material[length];
            for (int i = 0; i < length; i++) {
                items[i] = (Material) dataInput.readObject();
            }
            return items;

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to deserialize Base64 to ItemStack array: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Deserializes a {@link Base64} encoded string into an ArrayList of {@link ItemStack}.
     *
     * @param data Base64 encoded string.
     * @return ArrayList of {@link ItemStack}, or null if deserialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static List<ItemStack> itemStackArrayListFromBase64(final String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            int length = dataInput.readInt();
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                items.add((ItemStack) dataInput.readObject());
            }
            return items;

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to deserialize Base64 to ItemStack array: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Deserializes a {@link Base64} encoded string into an ArrayList of {@link Material}.
     *
     * @param data Base64 encoded string.
     * @return ArrayList of {@link Material}, or null if deserialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static List<Material> materialArrayListFromBase64(final String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            int length = dataInput.readInt();
            List<Material> items = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                items.add((Material) dataInput.readObject());
            }
            return items;

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to deserialize Base64 to ItemStack array: " + e.getMessage(), e);
            return null;
        }
    }

    //-----------------------------------TO BASE64-----------------------------------

    /**
     * Converts an object to a {@link Base64} encoded string.
     *
     * @param obj The object to encode.
     * @return Base64 encoded string or null if encoding fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static String toBase64(final Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(obj);
            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to convert object to Base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Serializes an {@link ArrayList} to a {@link Base64} encoded string.
     *
     * @param list ArrayList.
     * @return Base64 encoded string or null if serialization fails.
     */
    @Contract(pure = true)
    public static String listToBase64(final List<Object> list) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(list.size());
            for (var obj : list) {
                dataOutput.writeObject(obj);
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to serialize ArrayList to Base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Serializes a single {@link ItemStack} to a {@link Base64} encoded string.
     *
     * @param item {@link ItemStack}.
     * @return Base64 encoded string or null if serialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static String itemToBase64(final ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to serialize ItemStack to Base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Serializes a single {@link Material} to a {@link Base64} encoded string.
     *
     * @param item {@link Material}.
     * @return Base64 encoded string or null if serialization fails.
     */
    @Contract(pure = true, value = "null -> null")
    public static String materialToBase64(final Material item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to serialize ItemStack to Base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Serializes an array of {@link ItemStack} to a {@link Base64} encoded string.
     *
     * @param items Array of {@link ItemStack}.
     * @return Base64 encoded string or null if serialization fails.
     */
    @Contract(pure = true)
    public static String itemStackArrayToBase64(final ItemStack[] items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to serialize ItemStack array to Base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Serializes an array of {@link Material} to a {@link Base64} encoded string.
     *
     * @param items Array of {@link Material}.
     * @return Base64 encoded string or null if serialization fails.
     */
    @Contract(pure = true)
    public static String materialArrayToBase64(final Material[] items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.length);
            for (Material item : items) {
                dataOutput.writeObject(item);
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to serialize ItemStack array to Base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Serializes an ArrayList of {@link ItemStack} to a {@link Base64} encoded string.
     *
     * @param items ArrayList of {@link ItemStack}.
     * @return Base64 encoded string or null if serialization fails.
     */
    @Contract(pure = true)
    public static String itemStackArrayListToBase64(final List<ItemStack> items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.size());
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to serialize ItemStack array to Base64: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Serializes an ArrayList of {@link Material} to a {@link Base64} encoded string.
     *
     * @param items ArrayList of {@link Material}.
     * @return Base64 encoded string or null if serialization fails.
     */
    @Contract(pure = true)
    public static String materialArrayListToBase64(final List<Material> items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.size());
            for (Material item : items) {
                dataOutput.writeObject(item);
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to serialize Material array to Base64: " + e.getMessage(), e);
            return null;
        }
    }
}
