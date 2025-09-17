package dev.prodzeus.utilities.player;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
public class PlayerUtil {
    /**
     * Check if the player's inventory contains the specified item.
     * @param player Player inspected.
     * @param item Item to check for.
     * @return true | false
     */
    @Contract(pure = true)
    public static boolean inventoryContains(final Player player, final ItemStack item){
        return player.getInventory().contains(item);
    }
}
