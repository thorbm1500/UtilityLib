package dev.prodzeus.utilities.gui.util;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class Button {

    private final ItemStack item;
    private int slot;

    private Button(final ItemStack item) {
        this.item = item;
    }

    private Button(final int slot, final ItemStack item) {
        this.item = item;
        this.slot = slot;
    }

    public static Button create(final int slot, final Material material, final Consumer<InventoryClickEvent> listener) {
        final ItemStack item = new ItemStack(material);
        return new Button(slot, item) {
            @Override
            public void onClick(final InventoryClickEvent event) {
                listener.accept(event);
            }
        };
    }

    public static Button create(final int slot, final ItemStack item, final Consumer<InventoryClickEvent> listener) {
        return new Button(slot, item) {
            @Override
            public void onClick(final InventoryClickEvent event) {
                listener.accept(event);
            }
        };
    }

    public static Button create(final ItemStack item, final Consumer<InventoryClickEvent> listener) {
        return new Button(item) {
            @Override
            public void onClick(final InventoryClickEvent event) {
                listener.accept(event);
            }
        };
    }

    public static Button create(final Material material, final Consumer<InventoryClickEvent> listener) {
        final ItemStack item = new ItemStack(material);
        return new Button(item) {
            @Override
            public void onClick(final InventoryClickEvent event) {
                listener.accept(event);
            }
        };
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(final int slot) {
        this.slot = slot;
    }

    public abstract void onClick(final InventoryClickEvent event);
}