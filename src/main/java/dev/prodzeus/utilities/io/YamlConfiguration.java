package dev.prodzeus.utilities.io;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
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
import org.jetbrains.annotations.Contract;
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
        plugin.saveResource(fileName, true);
        if (fileConfiguration != null) fileConfiguration.save(getFile());
    }

    /**
     * Reloads the {@link FileConfiguration} from the disk. All unsaved changes will be lost during this operation.
     */
    protected void reload() {
        fileConfiguration = loadConfiguration(getFile());
    }

    /**
     * Saves the current configuration and reloads from disk.
     */
    protected void saveAndReload() {
        save();
        reload();
    }

    /**
     * Get a new instance of the File.
     * @return The new file instance.
     */
    private @NotNull File file() {
        return new File(plugin.getDataFolder(), fileName);
    }

    /**
     * Get an instance of the File. If no instance is currently loaded, a new instance will be created.
     * @return The file instance.
     */
    private @NotNull File getFile() {
        if (file == null) file = file();
        return file;
    }

    /**
     * Get the current {@link FileConfiguration}.
     * If no configuration is currently loaded, a new configuration will be loaded and returned.
     * @return The {@link FileConfiguration}.
     */
    protected @NotNull FileConfiguration configuration() {
        if (fileConfiguration == null) reload();
        return fileConfiguration;
    }

    /**
     * Checks whether the given path exists in the file.
     * @param path Path in file.
     * @return True | False
     */
    @Contract(pure = true)
    public boolean contains(@NotNull final String path) {
        return configuration().contains(path);
    }

    /**
     * Checks whether the given children exist in the file.
     * @param parent   Parent path to check against.
     * @param children All children to check for.
     * @return True | False
     */
    @Contract(pure = true)
    public boolean contains(@NotNull String parent, @NotNull final String... children) {
        if (!parent.endsWith(".")) parent += ".";
        for (final String child : children) {
            if (!configuration().contains(parent + child)) return false;
        }
        return true;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link String}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link String}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    @Contract(pure = true)
    public boolean isString(@NotNull final String path) {
        if (!contains(path)) return false;
        return configuration().get(path) instanceof String;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Integer}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Integer}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    @Contract(pure = true)
    public boolean isInt(@NotNull final String path) {
        if (!contains(path)) return false;
        return configuration().get(path) instanceof Integer;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Long}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Long}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    @Contract(pure = true)
    public boolean isLong(@NotNull final String path) {
        if (!contains(path)) return false;
        return configuration().get(path) instanceof Long;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Float}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Float}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    @Contract(pure = true)
    public boolean isFloat(@NotNull final String path) {
        if (!contains(path)) return false;
        return configuration().get(path) instanceof Float;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Double}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Double}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    @Contract(pure = true)
    public boolean isDouble(@NotNull final String path) {
        if (!contains(path)) return false;
        return configuration().get(path) instanceof Double;
    }

    /**
     * Checks if the {@link Object} at the given path is an instance of {@link Boolean}
     * @param path Path in the configuration file to check.
     * @return True if the {@link Object} is an instance of {@link Boolean}, otherwise false.
     * If the path does not exist, false will also be returned.
     */
    @Contract(pure = true)
    public boolean isBoolean(@NotNull final String path) {
        if (!contains(path)) return false;
        return configuration().get(path) instanceof Boolean;
    }

        /**
     * Overwrite or add data to the file.<br>
     * To remove an entry, see {@link YamlConfiguration#remove}
     * @param path  Path in the file.
     * @param value Value to write
     */
    public void set(@NotNull final String path, @NotNull final Object value) {
        fileConfiguration.set(path, value);
        save();
    }

    /**
     * Overwrite or add data to the file. If a given value is null, the entry in the file will be removed.
     *
     * @param path  Path in the file.
     * @param value Map containing children with a value to write attached to each.
     */
    public void set(@NotNull final String path, @NotNull final Map<String,Object> value) {
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

    @Contract(pure = true)
    public @NotNull Set<String> getKeys(@NotNull final String path) {
        return getKeys(path, false);
    }

    @Contract(pure = true)
    public @Nullable Set<String> getKeys(@NotNull final String path, @Nullable final Set<String> def) {
        return getKeys(path, false, def);
    }

    @Contract(pure = true)
    public @NotNull Set<String> getKeys(@NotNull final String path, final boolean deep) {
        return getKeys(path, deep, new HashSet<>());
    }

    @Contract(pure = true)
    public Set<String> getKeys(@NotNull final String path, final boolean deep, final @Nullable Set<String> def) {
        if (!contains(path)) return def;
        return Objects.requireNonNull(configuration().getConfigurationSection(path)).getKeys(deep);
    }

    @Contract(pure = true)
    public @NotNull Map<String, Object> getValues(@NotNull final String path) {
        return getValues(path, false, new HashMap<>());
    }

    @Contract(pure = true)
    public Map<String, Object> getValues(@NotNull final String path, @Nullable final Map<String, Object> def) {
        return getValues(path, false, def);
    }

    @Contract(pure = true)
    public @NotNull Map<String, Object> getValues(@NotNull final String path, final boolean deep) {
        return getValues(path, deep, new HashMap<>());
    }

    @Contract(pure = true)
    public Map<String, Object> getValues(@NotNull final String path, final boolean deep, @Nullable final Map<String, Object> def) {
        if (!contains(path)) return def;
        return Objects.requireNonNull(configuration().getConfigurationSection(path)).getValues(deep);
    }


    /**
     * Get a {@link String} from the specified path in the file.
     *
     * @param path Path to get String from.
     * @return The String found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public String getString(@NotNull final String path) {
        if (!contains(path)) return null;
        return configuration().getString(path);
    }

    /**
     * Get a {@link String} from the specified path in the file.
     *
     * @param path Path to get String from.
     * @param def  Default value.
     * @return The String found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public String getString(@NotNull final String path, @Nullable final String def) {
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
    @Contract(pure = true)
    public Component getComponent(@NotNull final String path) {
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
    @Contract(pure = true)
    public Component getComponent(@NotNull final String path, @Nullable final String def) {
        return MiniMessage.miniMessage().deserialize(getString(path, def));
    }

    /**
     * Get a {@link String}{@link List} from the specified path in the file.
     *
     * @param path Path to get String List from.
     * @return The String List found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public List<String> getStringList(@NotNull final String path) {
        if (!contains(path)) return null;
        return configuration().getStringList(path);
    }

    /**
     * Get a {@link String}{@link List} from the specified path in the file.
     *
     * @param path Path to get String List from.
     * @param def  Default value.
     * @return The String List found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public List<String> getStringList(@NotNull final String path, @Nullable final List<String> def) {
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
    @Contract(pure = true)
    public List<Component> getComponentList(@NotNull final String path) {
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
    @Contract(pure = true)
    public List<Component> getComponentList(@NotNull final String path, @Nullable final List<Component> def) {
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
    @Contract(pure = true)
    public Byte getByte(@NotNull final String path) {
        if (!contains(path)) return null;
        return configuration().get(path) instanceof Byte b ? b : null;
    }

    /**
     * Get a {@link Byte} from the specified path in the file.
     *
     * @param path Path to get Byte from.
     * @param def  Default value.
     * @return The Byte found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Byte getByte(@NotNull final String path, final byte def) {
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
    @Contract(pure = true)
    public Integer getInt(@NotNull final String path) {
        if (!contains(path)) return null;
        return configuration().getInt(path);
    }

    /**
     * Get a {@link Integer} from the specified path in the file.
     *
     * @param path Path to get Integer from.
     * @param def  Default value.
     * @return The Integer found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Integer getInt(@NotNull final String path, final int def) {
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
    @Contract(pure = true)
    public Long getLong(@NotNull final String path) {
        if (!contains(path)) return null;
        return configuration().getLong(path);
    }

    /**
     * Get a {@link Long} from the specified path in the file.
     *
     * @param path Path to get Long from.
     * @param def  Default value.
     * @return The Long found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Long getLong(@NotNull final String path, final long def) {
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
    @Contract(pure = true)
    public Float getFloat(@NotNull final String path) {
        if (!contains(path)) return null;
        return (float) configuration().getDouble(path);
    }

    /**
     * Get a {@link Float} from the specified path in the file.
     *
     * @param path Path to get Float from.
     * @param def  Default value.
     * @return The Float found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Float getFloat(@NotNull final String path, final float def) {
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
    @Contract(pure = true)
    public Double getDouble(@NotNull final String path) {
        if (!contains(path)) return null;
        return configuration().getDouble(path);
    }

    /**
     * Get a {@link Double} from the specified path in the file.
     *
     * @param path Path to get Double from.
     * @param def  Default value.
     * @return The Double found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Double getDouble(@NotNull final String path, final double def) {
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
    @Contract(pure = true)
    public Boolean getBoolean(@NotNull final String path) {
        if (!contains(path)) return null;
        return configuration().getBoolean(path);
    }

    /**
     * Get a {@link Boolean} from the specified path in the file.
     *
     * @param path Path to get Boolean from.
     * @param def  Default value.
     * @return The Boolean found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Boolean getBoolean(@NotNull final String path, final boolean def) {
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
    @Contract(pure = true)
    public Material getMaterial(@NotNull final String path) {
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
    @Contract(pure = true)
    public Material getMaterial(@NotNull final String path, @Nullable final Material def) {
        final Material value = getMaterial(path);
        return value == null ? def : value;
    }

    /**
     * Get an {@link ItemStack} from the specified path in the configuration file.
     * <br>
     * The expected format can be viewed below.
     * Any deviation from this format <i>might</i> cause errors to be thrown
     * or might just be <i>ignored all together</i>.
     * <br>
     * <h3>Available Parameters</h3>
     * <li>{@code item} <i>required</i></li>
     * <li>{@code name}</li>
     * <li>{@code lore}</li>
     * <li>{@code amount}</li>
     * <li>{@code skull_texture}</li>
     * <li>{@code glow}</li>
     * <li>{@code enchantments}</li>
     * <li>{@code potion_effects}</li>
     * <li>{@code flags}</li>
     * <li>{@code custom_data_tags}</li>
     * <h3>Expected Format</h3>
     * <pre><code>
     *   entry_name:
     *     item: Material
     *     skull_texture: URL
     *     name: String
     *     lore:
     *       - String
     *     amount: Integer
     *     glow: Boolean
     *     enchantments:
     *       enchantment: Integer
     *     potion_effects:
     *       effect:
     *         duration: Integer
     *         amplifier: Integer
     *         ambient: Boolean
     *         particles: Boolean
     *     flags:
     *       - ItemFlag
     *     custom_data_tags:
     *       1:
     *         namespace: String
     *         key: String
     *         type: Object
     *         value: Object
     *     custom_model_data: Integer
     * </code></pre>
     * @param path                      Path to get ItemStack from.
     * @return                          The {@link ItemStack} found.
     * @throws InvalidPathException     If the configuration file does not contain the path specified.
     * @throws IllegalArgumentException If the {@link Material} defined does not exist.
     * @throws IllegalArgumentException If the {@link Material} defined is {@link Material#AIR}.
     * @throws IllegalArgumentException If the {@link NamespacedKey} is defined incorrectly in Custom Data Tags.
     * @throws IllegalArgumentException If the {@link ItemFlag} defined is invalid.
     * @throws RuntimeException         If the value is defined incorrectly and is unable to be read, for the Custom Data Tags.
     */
    public @NotNull ItemStack getItemStack(@NotNull final String path) throws InvalidPathException, IllegalArgumentException, RuntimeException {
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
                    final Enchantment enchant = enchantmentRegistry.get(Objects.requireNonNull(NamespacedKey.fromString(index.getKey().substring((path + ".enchantments.").length()).toUpperCase())));
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
        if (contains(path + ".custom_model_data")) {
            final Integer value = getInt(path + ".custom_model_data");
            if (value == null) throw new IllegalArgumentException("Error reading custom model data key %s. Value is either missing or configured incorrectly. The value must be a valid Integer!".formatted(path));
            meta.setCustomModelData(value);
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
    @Contract(pure = true)
    public List<ItemStack> getItemStackList(@NotNull final String path) throws RuntimeException {
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
     * Get a {@link Sound} from the specified path in the file.
     *
     * @param path Path to get Sound from.
     * @return The Sound found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Sound getSound(@NotNull final String path) {
        final String value = getString(path);
        if (value == null) return null;
        final NamespacedKey key = NamespacedKey.fromString(value);
        if (key == null) return null;
        return Registry.SOUNDS.get(key);
    }

    /**
     * Get a {@link Sound} from the specified path in the file.
     *
     * @param path Path to get Sound from.
     * @param def Default value.
     * @return The Sound found. Returns null if the path does not exist in the File, and no default value was defined,
     * otherwise returns the default value.
     */
    @Contract(pure = true)
    public Sound getSound(@NotNull final String path, @Nullable final Sound def) {
        final Sound sound = getSound(path);
        return sound == null ? def : sound;
    }
}