package dev.prodzeus.utilities.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.List.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.*;

/**
 * All utilities needed for making, changing and customizing ItemStacks.
 * <br>
 * Methods will return null in case of being wrongly used.
 * The most likely reason for having null returned is either a null item or item without any itemMeta.
 * <p>
 * This class makes use of {@link MiniMessage} format.
 * </p>
 */
@SuppressWarnings("unused")
public class ItemUtil {

    /**
     * Checks if the item or meta is null.
     */
    private static boolean isValid(final ItemStack item) {
        return item != null && item.getItemMeta() != null;
    }

    private static String removeDefaultFormat(final String text) {
        return "<!italic><white>%s".formatted(text.replaceAll("<reset>","<reset><!italic><white>"));
    }

    /**
     * Set an item's displayname.
     *
     * @param item Item to update.
     * @param name New displayname.
     */
    public static void setName(final ItemStack item, final String name) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.displayName(miniMessage().deserialize(removeDefaultFormat(name)));
        item.setItemMeta(meta);
    }

    /**
     * Set the item's lore.
     *
     * @param item Item to update.
     * @param line New lore.
     */
    public static void setLore(final ItemStack item, final String line) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.lore(of(miniMessage().deserialize(removeDefaultFormat(line))));
        item.setItemMeta(meta);
    }

    /**
     * Set the item's lore with multiple lines.
     *
     * @param item  Item to update.
     * @param lines New lore.
     */
    public static void setLore(final ItemStack item, final String... lines) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        for (String line : lines) {
            lore.add(miniMessage().deserialize(removeDefaultFormat(line)));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Set the item's lore with multiple lines.
     *
     * @param item  Item to update.
     * @param lines New lore.
     */
    public static void setLore(final ItemStack item, final List<String> lines) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        for (String line : lines) {
            lore.add(miniMessage().deserialize(removeDefaultFormat(line)));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Add a new line of lore to the item, without losing or changing the current lore.
     *
     * @param item Item to update.
     * @param line New lore to add.
     */
    public static void addLore(final ItemStack item, final String line) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        List<Component> currentLore = meta.lore();

        if (currentLore == null) currentLore = new ArrayList<>();

        currentLore.add(miniMessage().deserialize(removeDefaultFormat(line)));
        meta.lore(currentLore);
        item.setItemMeta(meta);
    }

    /**
     * Add new lines of lore to the item, without losing or changing the current lore.
     *
     * @param item  Item to update.
     * @param lines New lore to add.
     */
    public static void addLore(final ItemStack item, final String... lines) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        List<Component> currentLore = meta.lore();

        if (currentLore == null) currentLore = new ArrayList<>();

        for (var line : lines) {
            currentLore.add(miniMessage().deserialize(removeDefaultFormat(line)));
        }

        meta.lore(currentLore);
        item.setItemMeta(meta);
    }

    /**
     * Enable or disable enchantment glint on the item.
     *
     * @param item  Item to update.
     * @param state true | false
     */
    public static void setGlint(final ItemStack item, final boolean state) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.setEnchantmentGlintOverride(state);
        item.setItemMeta(meta);
    }

    /**
     * Enable or disable tooltip on the item.
     *
     * @param item  Item to update.
     * @param state true | false
     */
    public static void setHideTooltip(final ItemStack item, final boolean state) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.setHideTooltip(state);
        item.setItemMeta(meta);
    }

    /**
     * Add an {@link Enchantment} to the item.
     *
     * @param item               Item to update.
     * @param enchantment        Enchantment to add.
     * @param level              The enchantment's level.
     * @param overrideLevelLimit Whether to ignore vanilla level limit.
     */
    public static void addEnchant(final ItemStack item, final Enchantment enchantment, final int level, final boolean overrideLevelLimit) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchantment, level, overrideLevelLimit);
        item.setItemMeta(meta);
    }

    /**
     * Add an {@link Enchantment} to the item.
     *
     * @param item        Item to update.
     * @param enchantment Enchantment to add.
     * @param level       The enchantment's level.
     */
    public static void addEnchant(final ItemStack item, final Enchantment enchantment, final int level) {
        addEnchant(item, enchantment, level, true);
    }

    /**
     * Add an {@link Enchantment} to the item, at it's base level.
     *
     * @param item        Item to update.
     * @param enchantment Enchantment to add.
     */
    public static void addEnchant(final ItemStack item, final Enchantment enchantment) {
        addEnchant(item, enchantment, 1, false);
    }

    /**
     * Add new ItemFlags to the item.
     *
     * @param item  Item to update.
     * @param flags Flags to add.
     */
    public static void addItemFlags(final ItemStack item, final ItemFlag... flags) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flags);
        item.setItemMeta(meta);
    }

    /**
     * Add new AttributeModifiers to the item.
     *
     * @param item      Item to update.
     * @param attribute Attribute to add.
     * @param modifier  Modifier to set.
     */
    public static void addAttributeModifier(final ItemStack item, final Attribute attribute, final AttributeModifier modifier) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(meta);
    }

    /**
     * Set whether the item should be fire-resistant or not.
     *
     * @param item  Item to update.
     * @param state true | false
     */
    public static void setFireResistance(final ItemStack item, final boolean state) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.setFireResistant(state);
        item.setItemMeta(meta);
    }

    /**
     * Update the item's max stack size.
     *
     * @param item Item to update.
     * @param size New max stack size.
     */
    public static void setMaxStackSize(final ItemStack item, final int size) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(size);
        item.setItemMeta(meta);
    }

    /**
     * Set whether the item should be unbreakable or not.
     *
     * @param item  Item to update.
     * @param state true | false
     */
    public static void setUnbreakable(final ItemStack item, final boolean state) {
        if (!isValid(item)) return;

        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(state);
        item.setItemMeta(meta);
    }

    /**
     * Get all enchantments and their respective levels from the item.
     *
     * @param item Item to get enchantments from.
     * @return All the enchantments of the item or an empty map, should the item not have any enchantments.
     */
    @Contract(pure = true, value = "null -> null")
    public static @Nullable Map<Enchantment, Integer> getEnchantments(final ItemStack item) {
        if (!isValid(item)) return null;

        return item.getItemMeta().getEnchants();
    }

    /**
     * Get a specific enchantments level from the item.
     *
     * @param item        Item to get the enchantment level from.
     * @param enchantment The enchantment to get the level from.
     * @return The enchantments level or 0 should the item not contain the specified enchantment.
     */
    @Contract(pure = true)
    public static int getEnchantmentLevel(final ItemStack item, final Enchantment enchantment) {
        if (!isValid(item)) return 0;

        return item.getItemMeta().getEnchantLevel(enchantment);
    }

    /**
     * Get all ItemFlags from the item.
     *
     * @param item Item to get flags from.
     * @return All the ItemFlags from the item, or an empty Set, should the item not have any ItemFlags.
     */
    @Contract(pure = true, value = "null -> null")
    public static @Nullable Set<ItemFlag> getItemFlags(final ItemStack item) {
        if (!isValid(item)) return null;

        return item.getItemMeta().getItemFlags();
    }

    /**
     * Check whether the item has a displayname set or not.
     *
     * @param item Item to check.
     * @return true | false
     */
    @Contract(pure = true, value = "null -> null")
    public static @Nullable Boolean hasDisplayName(final ItemStack item) {
        if (!isValid(item)) return null;

        return item.getItemMeta().hasDisplayName();
    }

    /**
     * Check whether the item has lore or not.
     *
     * @param item Item to check.
     * @return true | false
     */
    @Contract(pure = true, value = "null -> null")
    public static @Nullable Boolean hasLore(final ItemStack item) {
        if (!isValid(item)) return null;

        return item.getItemMeta().hasLore();
    }

    /**
     * Check whether the item is enchanted or not.
     *
     * @param item Item to check.
     * @return true | false
     */
    @Contract(pure = true, value = "null -> null")
    public static @Nullable Boolean isEnchanted(final ItemStack item) {
        if (!isValid(item)) return null;

        return item.getItemMeta().hasEnchants();
    }

    /**
     * Check whether the item is enchanted with a specific enchantment or not.
     *
     * @param item Item to check.
     * @return true | false
     */
    @Contract(pure = true)
    public static @Nullable Boolean hasEnchantment(final ItemStack item, final Enchantment enchantment) {
        if (!isValid(item)) return null;

        return item.getItemMeta().hasEnchant(enchantment);
    }

    /**
     * Check whether the item has glint override set.
     *
     * @param item Item to check.
     * @return true | false
     */
    @Contract(pure = true)
    public static @Nullable Boolean hasGlintOverride(final ItemStack item) {
        if (!isValid(item)) return null;

        return item.getItemMeta().hasEnchantmentGlintOverride();
    }

    /**
     * Get the material's name in a formatted and readable format.
     *
     * @param material Material from which name will be taken.
     * @return Formatted text.
     */
    @Contract(pure = true, value = "null -> null")
    public static @Nullable String getItemName(Material material) {
        if (material == null) return null;
        String rawItemName = material.toString().toLowerCase().replace("_", " ");

        int itemLength = rawItemName.split("\\s").length;
        String[] itemNameArray = rawItemName.split(" ");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < itemLength; i++) {
            String word = itemNameArray[i].substring(0, 1).toUpperCase() + itemNameArray[i].substring(1);
            builder.append(word);
            builder.append(" ");
        }

        String builtMessage = builder.toString();

        return builtMessage.stripTrailing();
    }

    /**
     * Get the item's name in a formatted and readable format.
     *
     * @param item Item to get name from
     * @return Formatted text
     */
    @Contract(pure = true, value = "null -> null")
    public static @Nullable String getItemName(ItemStack item) {
        if (item == null) return null;
        return getItemName(item.getType());
    }
}
