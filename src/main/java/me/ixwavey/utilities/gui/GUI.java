package me.ixwavey.utilities.gui;

import me.ixwavey.utilities.gui.util.Button;
import me.ixwavey.utilities.gui.util.Filler;
import me.ixwavey.utilities.gui.util.InventorySize;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

@SuppressWarnings("unused")
public abstract class GUI<E extends Enum<E>> implements Listener {

    //Management
    private static final HashMap<UUID, GUI<?>> instances = new HashMap<>();

    //Instance
    protected final Player player;
    protected final UUID uuid;
    private final Plugin plugin;
    private final Inventory inventory;
    private E page;
    private final EnumMap<E, Runnable> pages;
    private final Stack<E> history = new Stack<>();
    private final HashMap<Integer, Button> buttons = new HashMap<>();
    private final HashMap<Integer, ItemStack> items = new HashMap<>();

    //GUI Settings
    private Filler filler = Filler.BLACK;
    private boolean allowItemPickup = true;
    private boolean allowQuickClose = true;
    private boolean destroyOnClose = true;

    /**
     * Creates a new GUI instance with a medium size.
     *
     * @param plugin Plugin calling the GUI.
     * @param player The player linked to the GUI.
     */
    public GUI(final Plugin plugin, final Player player, final Class<E> enumClass) {
        this(plugin, player, InventorySize.SMALL, null, null, enumClass);
    }

    /**
     * Creates a new GUI instance with a medium size.
     *
     * @param plugin Plugin calling the GUI.
     * @param player The player linked to the GUI.
     * @param title  The title of the inventory.
     */
    public GUI(final Plugin plugin, final Player player, final String title, final Class<E> enumClass) {
        this(plugin, player, InventorySize.SMALL, title, null, enumClass);
    }

    /**
     * Creates a new GUI instance.
     *
     * @param plugin Plugin calling the GUI.
     * @param player The player linked to the GUI.
     * @param size   The size of the inventory.
     */
    public GUI(final Plugin plugin, final Player player, final InventorySize size, final Class<E> enumClass) {
        this(plugin, player, size, null, null, enumClass);
    }

    /**
     * Creates a new GUI instance.
     *
     * @param plugin Plugin calling the GUI.
     * @param player The player linked to the GUI.
     * @param size   The size of the inventory.
     * @param title  The title of the inventory.
     */
    public GUI(final Plugin plugin, final Player player, final InventorySize size, final String title, final Class<E> enumClass) {
        this(plugin, player, size, title, null, enumClass);
    }

    /**
     * Creates a new GUI instance.
     *
     * @param plugin Plugin calling the GUI.
     * @param player The player linked to the GUI.
     * @param size   The size of the inventory.
     * @param filler The item the inventory is filled with as background.
     */
    public GUI(final Plugin plugin, final Player player, final InventorySize size, final Filler filler, final Class<E> enumClass) {
        this(plugin, player, size, null, filler, enumClass);
    }

    /**
     * Creates a new GUI instance.
     *
     * @param plugin Plugin calling the GUI.
     * @param player The player linked to the GUI.
     * @param size   The size of the inventory.
     * @param title  The title of the inventory.
     * @param filler The item the inventory is filled with as background.
     */
    public GUI(final Plugin plugin, final Player player, final InventorySize size, @Nullable final String title, @Nullable final Filler filler, final Class<E> enumClass) {
        this.plugin = plugin;
        this.player = player;
        this.uuid = player.getUniqueId();
        this.pages = new EnumMap<>(enumClass);
        newInstance();
        this.inventory = title == null ? Bukkit.createInventory(player, size.get()) : Bukkit.createInventory(player, size.get(), MiniMessage.miniMessage().deserialize(title));
        this.filler = filler == null ? this.filler : filler;
    }

    /**
     * Register pages to the menu.
     * @param page The Enum of the Page to register.
     * @param runnable The method attached to the page, for rendering its components.
     * @return The GUI instance.
     */
    protected GUI<?> registerPage(E page, @NotNull Runnable runnable) {
        this.pages.put(page,runnable);
        return this;
    }

    /**
     * Renders the current page to the screen. This is used automatically and should not be needed to be called manually.
     * @return The GUI instance.
     * @throws IllegalStateException In case no pages has been registered.
     */
    protected GUI<?> renderPage()throws  IllegalStateException {
        if (page == null) throw new IllegalStateException("No pages has been registered. The menu relies on pages, and will not function without defining the pages to render.");
        fill();
        this.pages.get(this.page).run();
        for (var index : buttons.entrySet()) {
            inventory.setItem(index.getKey(),index.getValue().getItem());
            items.remove(index.getKey());
        }
        for (var index : items.entrySet()) {
            inventory.setItem(index.getKey(),index.getValue());
        }
        return this;
    }

    /**
     * Changes the current page and updates the GUI to show the new page.
     * @param page Page to update to.
     * @return The GUI instance.
     */
    protected GUI<?> changePage(final E page) {
        history.add(this.page);
        this.page = page;
        clearComponents();
        renderPage();
        return this;
    }

    private void clearComponents() {
        buttons.clear();
        items.clear();
    }

    protected GUI<?> addButton(final int slot, final Button button) {
        buttons.put(slot,button);
        return this;
    }

    protected GUI<?> addItem(final int slot, final ItemStack item) {
        items.put(slot,item);
        return this;
    }

    /**
     * Fills the inventory with the filler item.
     * @return The GUI instance.
     */
    protected GUI<?> fill() {
        return fill(0, inventory.getSize() - 1);
    }

    /**
     * Fills a specific part of the GUI with the filler item.
     * @param start Start slot of the fill area.
     * @param end End slot of the fill area.
     * @return The GUI instance.
     */
    protected GUI<?> fill(final int start, final int end) {
        for (int i = start; i <= end; i++) {
            inventory.setItem(i, filler.get());
        }
        return this;
    }

    /**
     * Sets whether the Player is allowed to pick up items while the GUI is active and opened.
     * @param allow True | False
     * @return The GUI instance.
     */
    protected GUI<?> allowItemPickup(final boolean allow) {
        this.allowItemPickup = allow;
        return this;
    }

    /**
     * Sets whether the Player is allowed to quick-close the GUI by e.g. pressing the escape button.
     * @param allow True | False
     * @return The GUI instance.
     */
    protected GUI<?> allowQuickClose(final boolean allow) {
        this.allowQuickClose = allow;
        return this;
    }

    /**
     * Sets whether the GUI instance should be destroyed when closed by the Player.
     * @param destroy True | False
     * @return The GUI instance.
     */
    protected GUI<?> destroyOnClose(final boolean destroy) {
        this.destroyOnClose = destroy;
        return this;
    }

    /**
     * Updates the filler item of the GUI to the new given Filler.
     * @param filler Filler to update to.
     * @return The GUI instance.
     */
    protected GUI<?> setFiller(final Filler filler) {
        this.filler = filler;
        return this;
    }

    /**
     * Used to cache the new instance of the GUI.
     * @return The GUI instance.
     */
    private GUI<?> newInstance() {
        if (instances.containsKey(this.uuid)) instances.get(this.uuid).destroy();
        instances.put(uuid, this);
        return this;
    }

    /**
     * Opens the GUI for the Player.
     * @return The GUI instance.
     */
    protected GUI<?> open() {
        registerListeners();
        player.openInventory(inventory);
        return this;
    }

    /**
     * Closes the GUI for the Player.
     * @param destroy Whether the GUI should destroy itself when closed.
     * @return The GUI instance.
     */
    protected GUI<?> close(final boolean destroy) {
        if (destroy) destroy();
        else {
            unregisterListeners();
            inventory.close();
        }
        return this;
    }

    /**
     * Destroys the GUI instance and removes the instance and UUID from cache.
     * @return Whether the listeners were successfully unregistered.
     */
    protected boolean destroy() {
        instances.remove(uuid);
        return unregisterListeners();
    }

    /**
     * Registers the listeners for the GUI instance.
     */
    private void registerListeners() {
        if (HandlerList.getRegisteredListeners(plugin).stream().noneMatch(l -> l.getListener() == this)) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else if (unregisterListeners()) registerListeners();
    }

    /**
     * Unregisters the listeners for the GUI instance.
     * @return Whether the unregistration was successful.
     */
    private boolean unregisterListeners() {
        if (HandlerList.getRegisteredListeners(plugin).stream().anyMatch(l -> l.getListener() == this)) {
            HandlerList.unregisterAll(this);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final Player player = event.getWhoClicked() instanceof Player p ? p : null;
        if (player == null || !this.uuid.equals(player.getUniqueId()) || !event.getInventory().equals(this.inventory))
            return;
        event.setCancelled(true);
        if (event.getClick() == ClickType.DOUBLE_CLICK) return;

        final int slot = event.getRawSlot();
        final Button button = buttons.get(slot);
        if (button != null) button.onClick(event);
    }

    @EventHandler
    public void onItemPickup(final EntityPickupItemEvent event) {
        if (allowItemPickup) return;

        final Player player = event.getEntity() instanceof Player p ? p : null;
        if (player == null || !this.uuid.equals(player.getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (allowQuickClose) return;

        final Player player = event.getPlayer() instanceof Player p ? p : null;
        if (player == null || !this.uuid.equals(player.getUniqueId())) return;

        if (destroyOnClose) destroy();
        else new BukkitRunnable() {
            @Override
            public void run() {
                open();
            }
        }.runTask(plugin);
    }

    /**
     * Get an active GUI instance for a given Player.
     * @param player The Player.
     * @return The GUI instance if one was found, otherwise null.
     */
    public static GUI<?> hasGUIInstance(final Player player) {
        return instances.getOrDefault(player.getUniqueId(), null);
    }
}