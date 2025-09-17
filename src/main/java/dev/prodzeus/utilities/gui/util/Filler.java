package dev.prodzeus.utilities.gui.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("unused")
public enum Filler {
    BLACK("black", Material.BLACK_STAINED_GLASS_PANE),
    GRAY("gray",Material.GRAY_STAINED_GLASS_PANE),
    LIGHT_GRAY("light_gray",Material.LIGHT_GRAY_STAINED_GLASS_PANE),
    WHITE("white",Material.WHITE_STAINED_GLASS_PANE),
    RED("red",Material.RED_STAINED_GLASS_PANE),
    BROWN("brown",Material.BROWN_STAINED_GLASS_PANE),
    GREEN("green",Material.GREEN_STAINED_GLASS_PANE),
    LIME("lime",Material.LIME_STAINED_GLASS_PANE),
    BLUE("blue",Material.BLUE_STAINED_GLASS_PANE),
    LIGHT_BLUE("light_blue",Material.LIGHT_BLUE_STAINED_GLASS_PANE),
    CYAN("cyan",Material.CYAN_STAINED_GLASS_PANE),
    YELLOW("yellow",Material.YELLOW_STAINED_GLASS_PANE),
    ORANGE("orange",Material.ORANGE_STAINED_GLASS_PANE),
    PURPLE("purple",Material.PURPLE_STAINED_GLASS_PANE),
    PINK("pink",Material.PINK_STAINED_GLASS_PANE),
    MAGENTA("magenta",Material.MAGENTA_STAINED_GLASS_PANE)
    ;

    private final Material material;
    private final String name;

    Filler(final String name, final Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack get() {
        final ItemStack filler = new ItemStack(this.material);
        ItemMeta meta = filler.getItemMeta();
        meta.setHideTooltip(true);
        filler.setItemMeta(meta);
        return filler;
    }

    public static Filler of(final String type) {
        return Filler.valueOf(type);
    }
}
