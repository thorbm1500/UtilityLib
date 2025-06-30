package me.ixwavey.utilities.gui.util;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@SuppressWarnings("unused")
public abstract class Page {

    /**
     * This method should contain all creation of items and buttons and will be called on each update.
     */
    public abstract void generate();

    private final HashMap<Integer,Button> buttons = new HashMap<>();
    private final HashMap<Integer,ItemStack> items = new HashMap<>();

    public HashMap<Integer,Button> getButtons() {
        return buttons;
    }

    public HashMap<Integer,ItemStack> getItems() {
        return items;
    }

    protected Page addButton(final int slot, final Button button) {
        buttons.put(slot,button);
        return this;
    }

    protected Page addItem(final int slot, final ItemStack item) {
        items.put(slot,item);
        return this;
    }

    protected Page clearButtons() {
        buttons.clear();
        return this;
    }

    protected Page clearItems() {
        items.clear();
        return this;
    }
}
