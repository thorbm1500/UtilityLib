package me.ixwavey.utilities.io;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URI;
import java.nio.file.InvalidPathException;
import java.util.*;

import static org.bukkit.configuration.file.YamlConfiguration.*;

@SuppressWarnings("unused")
public abstract class YamlConfiguration {

    private final Plugin plugin;

    private static File file;
    private final String fileName;
    private FileConfiguration fileConfiguration;

    protected YamlConfiguration(@NotNull final Plugin plugin, @NotNull String fileName) {
        this.plugin = plugin;
        if (!fileName.endsWith(".yml")) fileName += ".yml";
        this.fileName = fileName;

        save();
        reload();
    }

    /**
     * Saves the file with the current {@link FileConfiguration}.
     * If a file does not already exist, it will be created.
     */
    @SneakyThrows
    protected void save() {
        getFile();
        plugin.saveResource(fileName, true);
        if (fileConfiguration != null) fileConfiguration.save(getFile());
    }

    /**
     * Reloads the {@link FileConfiguration} from the disk. All unsaved changes will be lost during this operation.
     */
    protected void reload() {
        fileConfiguration = loadConfiguration(getFile());
    }

    private @NotNull File file() {
        return new File(plugin.getDataFolder(), fileName);
    }

    private @NotNull File getFile() {
        if (file == null) file = file();
        return file;
    }

    /**
     * Checks whether the given path exists in the file.
     * @param path Path in file.
     * @return True | False
     */
    protected boolean contains(@NotNull final String path) {
        return options().contains(path);
    }

    /**
     * Checks whether the given children exist in the file.
     * @param parent   Parent path to check against.
     * @param children All children to check for.
     * @return True | False
     */
    protected boolean contains(@NotNull String parent, @NotNull final String... children) {
        if (!parent.endsWith(".")) parent += ".";
        for (final String child : children) {
            if (!options().contains(parent + child)) return false;
        }
        return true;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link String}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link String}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    protected boolean isString(@NotNull final String path) {
        if (!contains(path)) return false;
        return options().get(path) instanceof String;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Integer}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Integer}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    protected boolean isInt(@NotNull final String path) {
        if (!contains(path)) return false;
        return options().get(path) instanceof Integer;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Long}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Long}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    protected boolean isLong(@NotNull final String path) {
        if (!contains(path)) return false;
        return options().get(path) instanceof Long;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Float}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Float}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    protected boolean isFloat(@NotNull final String path) {
        if (!contains(path)) return false;
        return options().get(path) instanceof Float;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Double}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Double}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    protected boolean isDouble(@NotNull final String path) {
        if (!contains(path)) return false;
        return options().get(path) instanceof Double;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Boolean}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Boolean}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    protected boolean isBoolean(@NotNull final String path) {
        if (!contains(path)) return false;
        return options().get(path) instanceof Boolean;
    }

        /**
     * Overwrite or add data to the file.<br>
     * To remove an entry, see {@link YamlConfiguration#remove}
     * @param path  Path in the file.
     * @param value Value to write
     */
    protected void set(@NotNull final String path, @NotNull final Object value) {
        fileConfiguration.set(path, value);
        save();
    }

    /**
     * Overwrite or add data to the file. If a given value is null, the entry in the file will be removed.
     *
     * @param path  Path in the file.
     * @param value Map containing children with a value to write attached to each.
     */
    protected void set(@NotNull final String path, @NotNull final Map<String,Object> value) {
        for (final var index : value.entrySet()) fileConfiguration.set(path + index.getKey(), index.getValue());
        save();
    }

    /**
     * Remove the entry at the specified path from the configuration file.
     * @param path Path to the entry.
     * @return True if the entry was removed, otherwise false if there is no entry at the specified path.
     */
    protected boolean remove(@NotNull final String path) {
        return remove(path, false);
    }

    /**
     * Remove the entry at the specified path from the configuration file.
     * @param path Path to the entry.
     * @param save True if the file should be saved after removal of entry.
     * @return True if the entry was removed, otherwise false if there is no entry at the specified path.
     */
    protected boolean remove(@NotNull final String path, final boolean save) {
        if (!contains(path)) return false;
        fileConfiguration.set(path, null);
        if (save) save();
        return true;
    }

    protected @NotNull Set<String> getKeys(@NotNull final String path) {
        return getKeys(path, false);
    }

    protected @Nullable Set<String> getKeys(@NotNull final String path, @Nullable final Set<String> def) {
        return getKeys(path, false, def);
    }

    protected @NotNull Set<String> getKeys(@NotNull final String path, final boolean deep) {
        return getKeys(path, deep, new HashSet<>());
    }

    protected Set<String> getKeys(@NotNull final String path, final boolean deep, final @Nullable Set<String> def) {
        if (!contains(path)) return def;
        return options().getConfigurationSection(path).getKeys(deep);
    }

    protected @NotNull Map<String, Object> getValues(@NotNull final String path) {
        return getValues(path, false, new HashMap<>());
    }

    protected Map<String, Object> getValues(@NotNull final String path, @Nullable final Map<String, Object> def) {
        return getValues(path, false, def);
    }

    protected @NotNull Map<String, Object> getValues(@NotNull final String path, final boolean deep) {
        return getValues(path, deep, new HashMap<>());
    }

    protected Map<String, Object> getValues(@NotNull final String path, final boolean deep, @Nullable final Map<String, Object> def) {
        if (!contains(path)) return def;
        return options().getConfigurationSection(path).getValues(deep);
    }


    /**
     * Get a {@link String} from the specified path in the file.
     *
     * @param path Path to get String from.
     * @return The String found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected String getString(@NotNull final String path) {
        if (!contains(path)) return null;
        return options().getString(path);
    }

    /**
     * Get a {@link String} from the specified path in the file.
     *
     * @param path Path to get String from.
     * @param def  Default value.
     * @return The String found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected String getString(@NotNull final String path, @Nullable final String def) {
        final String value = getString(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Component} from the specified path in the file.
     *
     * @param path Path to get Component from.
     * @return The String found as {@link Component}. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Component getComponent(@NotNull final String path) {
        final String value = getString(path);
        return value == null ? null : MiniMessage.miniMessage().deserialize(value);
    }

    /**
     * Get a {@link Component} from the specified path in the file.
     *
     * @param path Path to get Component from.
     * @param def  Default value.
     * @return The String found as {@link Component}. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Component getComponent(@NotNull final String path, @Nullable final String def) {
        return MiniMessage.miniMessage().deserialize(getString(path, def));
    }

    /**
     * Get a {@link String}{@link List} from the specified path in the file.
     *
     * @param path Path to get String List from.
     * @return The String List found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected List<String> getStringList(@NotNull final String path) {
        if (!contains(path)) return null;
        return options().getStringList(path);
    }

    /**
     * Get a {@link String}{@link List} from the specified path in the file.
     *
     * @param path Path to get String List from.
     * @param def  Default value.
     * @return The String List found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected List<String> getStringList(@NotNull final String path, @Nullable final List<String> def) {
        final List<String> value = getStringList(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Component}{@link List} from the specified path in the file.
     *
     * @param path Path to get Component List from.
     * @return The Component List found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected List<Component> getComponentList(@NotNull final String path) {
        final List<String> value = getStringList(path);
        if (value == null) return null;

        final List<Component> list = new ArrayList<>();
        for (String s : value) list.add(MiniMessage.miniMessage().deserialize(s));
        return list;
    }

    /**
     * Get a {@link Component}{@link List} from the specified path in the file.
     *
     * @param path Path to get Component List from.
     * @param def  Default value.
     * @return The Component List found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected List<Component> getComponentList(@NotNull final String path, @Nullable final List<Component> def) {
        final List<Component> value = getComponentList(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Byte} from the specified path in the file.
     *
     * @param path Path to get Byte from.
     * @return The Byte found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Byte getByte(@NotNull final String path) {
        if (!contains(path)) return null;
        return options().get(path) instanceof Byte b ? b : null;
    }

    /**
     * Get a {@link Byte} from the specified path in the file.
     *
     * @param path Path to get Byte from.
     * @param def  Default value.
     * @return The Byte found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Byte getByte(@NotNull final String path, final byte def) {
        final Byte value = getByte(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Integer} from the specified path in the file.
     *
     * @param path Path to get Integer from.
     * @return The Integer found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Integer getInt(@NotNull final String path) {
        if (!contains(path)) return null;
        return options().getInt(path);
    }

    /**
     * Get a {@link Integer} from the specified path in the file.
     *
     * @param path Path to get Integer from.
     * @param def  Default value.
     * @return The Integer found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Integer getInt(@NotNull final String path, final int def) {
        final Integer value = getInt(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Long} from the specified path in the file.
     *
     * @param path Path to get Long from.
     * @return The Long found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Long getLong(@NotNull final String path) {
        if (!contains(path)) return null;
        return options().getLong(path);
    }

    /**
     * Get a {@link Long} from the specified path in the file.
     *
     * @param path Path to get Long from.
     * @param def  Default value.
     * @return The Long found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Long getLong(@NotNull final String path, final long def) {
        final Long value = getLong(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Float} from the specified path in the file.
     *
     * @param path Path to get Float from.
     * @return The Float found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Float getFloat(@NotNull final String path) {
        if (!contains(path)) return null;
        return (float) options().getDouble(path);
    }

    /**
     * Get a {@link Float} from the specified path in the file.
     *
     * @param path Path to get Float from.
     * @param def  Default value.
     * @return The Float found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Float getFloat(@NotNull final String path, final float def) {
        final Float value = getFloat(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Double} from the specified path in the file.
     *
     * @param path Path to get Double from.
     * @return The Double found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Double getDouble(@NotNull final String path) {
        if (!contains(path)) return null;
        return options().getDouble(path);
    }

    /**
     * Get a {@link Double} from the specified path in the file.
     *
     * @param path Path to get Double from.
     * @param def  Default value.
     * @return The Double found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Double getDouble(@NotNull final String path, final double def) {
        final Double value = getDouble(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Boolean} from the specified path in the file.
     *
     * @param path Path to get Boolean from.
     * @return The Boolean found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Boolean getBoolean(@NotNull final String path) {
        if (!contains(path)) return null;
        return options().getBoolean(path);
    }

    /**
     * Get a {@link Boolean} from the specified path in the file.
     *
     * @param path Path to get Boolean from.
     * @param def  Default value.
     * @return The Boolean found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Boolean getBoolean(@NotNull final String path, final boolean def) {
        final Boolean value = getBoolean(path);
        return value == null ? def : value;
    }

    /**
     * Get a {@link Material} from the specified path in the file.
     *
     * @param path Path to get Material from.
     * @return The Material found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Material getMaterial(@NotNull final String path) {
        final String value = getString(path);
        if (value == null) return null;

        return Material.getMaterial(value.toUpperCase());
    }

    /**
     * Get a {@link Material} from the specified path in the file.
     *
     * @param path Path to get Material from.
     * @param def  Default value.
     * @return The Material found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    protected Material getMaterial(@NotNull final String path, @Nullable final Material def) {
        final Material value = getMaterial(path);
        return value == null ? def : value;
    }

    /**
     * Get an {@link ItemStack} from the specified path in the configuration file.
     * <br>
     * The expected format can be viewed below. Any deviation from this format might cause errors to be thrown
     * or might just be ignored all together.
     * <pre>
     * {@code
     * item_entry:
     *    item: DIAMOND
     *    name: 'Item Name'
     *    lore:
     *      - 'Text Here'
     *    amount: 64
     *    enchantments:
     *      UNBREAKING:3
     *    potion_effects:
     *      SWIFTNESS:
     *        duration: 60
     *        amplifier: 1
     *        ambient: true
     *        particles: true
     *     flags:
     *       - HIDE_ATTRIBUTES
     *     custom_data_tags:
     *       1:
     *         namespace: namespace
     *         key: key
     *         type: int
     *         value: 25
     *    }
     * </pre><br>
     * Available Parameters
     * <pre>
     * {@code
     * item
     * name
     * lore
     * amount
     * skull_texture
     * glow
     * enchantments
     * potion_effects
     * flags
     * custom_data_tags
     *    }
     * </pre>
     *
     * @param path Path to get ItemStack from.
     * @return The {@link ItemStack} found.
     * @throws InvalidPathException     If the configuration file does not contain the path specified.
     * @throws IllegalArgumentException If the {@link Material} defined does not exist.
     * @throws IllegalArgumentException If the {@link Material} defined is {@link Material#AIR}
     * @throws IllegalArgumentException If the {@link NamespacedKey} is defined incorrectly in Custom Data Tags.
     * @throws IllegalArgumentException If the {@link ItemFlag} defined is invalid.
     * @throws RuntimeException         If the value is defined incorrectly and is unable to be read, for the Custom Data Tags.
     */
    protected @NotNull ItemStack getItemStack(@NotNull final String path) throws InvalidPathException, IllegalArgumentException, RuntimeException {
        if (!contains(path)) throw new InvalidPathException(path, "Invalid path given. No item found!");

        final Material material = getMaterial(path + ".item");
        if (material == null) throw new IllegalArgumentException("Item type not found: " + getString(path + ".item"));
        if (material.isAir()) throw new IllegalArgumentException("Air is not a valid type!");

        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        if (contains(path + ".name")) meta.displayName(getComponent(path + ".name"));
        if (contains(path + ".lore")) meta.lore(getComponentList(path + ".lore"));
        if (contains(path + ".amount")) item.setAmount(getInt(path + ".amount", 1));
        if (material == Material.PLAYER_HEAD && contains(path + ".skull_texture")) {
            final PlayerProfile skullProfile = Bukkit.createProfile(UUID.randomUUID(), "");
            PlayerTextures skullTexture = skullProfile.getTextures();
            try {
                skullTexture.setSkin(URI.create(getString(path + ".skull_texture")).toURL());
                skullProfile.setTextures(skullTexture);
                final SkullMeta skullMeta = (SkullMeta) meta;
                skullMeta.setPlayerProfile(skullProfile);
            } catch (final Exception e) { throw new RuntimeException("Failed to create and assign skull texture!\n %s:%s".formatted(e.getCause(),e.getMessage())); }
        }
        if (contains(path + ".enchantments")) {
            try {
                final Registry<@NotNull Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
                for (final var index : getValues(path + ".enchantments").entrySet()) {
                    final Enchantment enchant = enchantmentRegistry.get(NamespacedKey.fromString(index.getKey().substring((path + ".enchantments.").length()).toUpperCase()));
                    if (enchant == null) continue;
                    final Integer level = (Integer) index.getValue();
                    meta.addEnchant(enchant, level == null ? 1 : level, true);
                }
            } catch (final Exception ignored) {}
        }
        if (material == Material.POTION && contains(path + ".potion_effects")) {
            final PotionMeta potionMeta = (PotionMeta) meta;
            try {
                final Registry<@NotNull PotionEffectType> potionEffectRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT);
                for (final String k : getKeys(path + ".potion_effects")) {
                    final NamespacedKey key = NamespacedKey.fromString(k);
                    if (key == null) continue;
                    final PotionEffectType effectType = potionEffectRegistry.get(key);
                    if (effectType == null) continue;
                    final PotionEffect effect = new PotionEffect(effectType, getInt(k + ".duration", 30), getInt(k + ".amplifier", 1), getBoolean(k + ".ambient", true), getBoolean(k + ".particles", true));
                    potionMeta.addCustomEffect(effect, true);
                }
            } catch (final Exception ignored) {}
        }
        if (contains(path + ".flags")) {
            for (final String s : getStringList(path + ".flags", new ArrayList<>())) {
                try {
                    meta.addItemFlags(ItemFlag.valueOf(s));
                } catch (final IllegalArgumentException e) { throw new IllegalArgumentException("Invalid item flag. No such item flag exists: " + s); }
            }
        }
        if (contains(path + ".glow")) {
            final Boolean glow = getBoolean(path + ".glow");
            if (glow != null) meta.setEnchantmentGlintOverride(glow);
        }
        if (contains(path + ".custom_data_tags")) {
            final PersistentDataContainer container = meta.getPersistentDataContainer();
            for (String k : getKeys(path + ".custom_data_tags")) {
                final String namespace = getString(k + ".namespace");
                if (namespace == null) throw new IllegalArgumentException("Error reading Namespace from %s. Namespace cannot be null!".formatted(k));
                final String key = getString(k + ".key");
                if (key == null) throw new IllegalArgumentException("Error reading Key from %s. Key cannot be null!".formatted(k));
                switch (getString(k + ".type", "None Found.").toLowerCase()) {
                    case "byte" -> {
                        final Byte v = getByte(k + ".value");
                        if (v == null) throw new RuntimeException("Error reading byte value for custom data key %s".formatted(k));
                        container.set(new NamespacedKey(namespace,key), PersistentDataType.BYTE, v);
                    }
                    case "int", "integer" -> {
                        final Integer v = getInt(k + ".value");
                        if (v == null) throw new RuntimeException("Error reading integer value for custom data key %s".formatted(k));
                        container.set(new NamespacedKey(namespace,key), PersistentDataType.INTEGER, v);
                    }
                    case "long" -> {
                        final Long v = getLong(k + ".value");
                        if (v == null) throw new RuntimeException("Error reading long value for custom data key %s".formatted(k));
                        container.set(new NamespacedKey(namespace,key), PersistentDataType.LONG, v);
                    }
                    case "float" -> {
                        final Float v = getFloat(k + ".value");
                        if (v == null) throw new RuntimeException("Error reading float value for custom data key %s".formatted(k));
                        container.set(new NamespacedKey(namespace,key), PersistentDataType.FLOAT, v);
                    }
                    case "double" -> {
                        final Double v = getDouble(k + ".value");
                        if (v == null) throw new RuntimeException("Error reading double value for custom data key %s".formatted(k));
                        container.set(new NamespacedKey(namespace,key), PersistentDataType.DOUBLE, v);
                    }
                    case "boolean" -> {
                        final Boolean v = getBoolean(k + ".value");
                        if (v == null) throw new RuntimeException("Error reading boolean value for custom data key %s".formatted(k));
                        container.set(new NamespacedKey(namespace,key), PersistentDataType.BOOLEAN, v);
                    }
                    case "string" -> {
                        final String v = getString(k + ".value");
                        if (v == null) throw new RuntimeException("Error reading string value for custom data key %s".formatted(k));
                        container.set(new NamespacedKey(namespace,key), PersistentDataType.STRING, v);
                    }
                    default -> throw new IllegalArgumentException("Invalid tag type: " + getString(k + ".type"));
                }
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Get a {@link List} of defined {@link ItemStack}s from the specified path in the file.
     *
     * @param path Path to get the list of ItemStacks from.
     * @return A list of all ItemStacks found at the path. If no ItemStacks are found, then an empty list.
     * @throws RuntimeException See {@link YamlConfiguration#getItemStack(String)}
     */
    protected List<ItemStack> getItemStackList(@NotNull final String path) throws RuntimeException {
        final List<ItemStack> list = new ArrayList<>();
        getKeys(path).forEach(key -> {
            try {
                list.add(getItemStack(key));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return list;
    }

    /**
     * @return The {@link FileConfiguration}
     */
    protected FileConfiguration options() {
        if (fileConfiguration == null) reload();
        return fileConfiguration;
    }
}
