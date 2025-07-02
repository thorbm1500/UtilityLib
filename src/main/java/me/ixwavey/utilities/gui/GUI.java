package me.ixwavey.utilities.gui;

import me.ixwavey.utilities.gui.util.Button;
import me.ixwavey.utilities.gui.util.Filler;
import me.ixwavey.utilities.gui.util.InventorySize;
import me.ixwavey.utilities.item.ItemUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.*;

/**
 * Create inventory GUIs easy and fast.
 * <br><br>
 * Register your pages by using the {@link GUI#registerPage(Enum, Runnable)},
 * where the Runnable is a method where all items and buttons are defined and added using the {@link GUI#addItem(int, ItemStack)} and {@link GUI#addButton(int, Button)}.
 * <br><br>
 * The GUI also supports paginated pages, where each paginated item is added with {@link GUI#addPaginatedItem(ItemStack)} and {@link GUI#addPaginatedItem(Button)} respectively.
 * To set up a page for pagination, use the {@link GUI#enablePagination()}
 * @param <E> An Enum containing a title of each page used in your GUI.
 */
@SuppressWarnings("unused")
public abstract class GUI<E extends Enum<E>> implements Listener {

    //Management
    private static final HashMap<UUID, GUI<?>> instances = new HashMap<>();

    /**
     * Define settings and register pages.
     */
    protected abstract void initializeGUI();

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

    //Pagination
    private boolean isPaginated = false;
    private int paginationStartSlot;
    private int paginationEndSlot;
    private int currentPaginationPage = 1;
    private int maxPaginationPages = 1;
    private Material nextPageMaterial;
    private Material previousPageMaterial;
    private int previousPageButtonSlot;
    private int nextPageButtonSlot;
    private String previousPaginatedPageItemTitle = "<#DDDDDD><bold>Previous page";
    private List<String> previousPaginatedPageItemLore = List.of("<gray>Click to change page.");
    private String nextPaginatedPageItemTitle = "<#DDDDDD><bold>Next page";
    private List<String> nextPaginatedPageItemLore = List.of("<gray>Click to change page.");
    private boolean hideTooltipsOnPageButtons = false;
    private final List<Object> paginatedItems = new ArrayList<>();
    private final HashMap<Integer, Button> paginatedButtons = new HashMap<>();

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
     *
     * @param page     The Enum of the Page to register.
     * @param runnable The method attached to the page, for rendering its components.
     * @return The GUI instance.
     */
    protected GUI<?> registerPage(E page, @NotNull Runnable runnable) {
        this.pages.put(page, runnable);
        return this;
    }

    /**
     * Renders the current page to the screen. This is used automatically and should not be needed to be called manually.
     *
     * @return The GUI instance.
     * @throws IllegalStateException In case no pages have been registered.
     */
    protected GUI<?> renderPage() throws IllegalStateException {
        if (page == null)
            throw new IllegalStateException("No pages has been registered. The menu relies on pages, and will not function without defining the pages to render.");
        fill();
        if (isPaginated) {
            if (paginatedItems.isEmpty()) {
                this.pages.get(this.page).run();
                this.maxPaginationPages = paginatedItems.size() / ((paginationEndSlot + 1) - (paginationStartSlot + 1));
            }
            this.paginatedButtons.clear();
            int i = (currentPaginationPage - 1) * ((paginationEndSlot + 1) - (paginationStartSlot + 1));
            for (int slot = paginationStartSlot; slot < paginationEndSlot && paginatedItems.size() > i; slot++) {
                final Object index = paginatedItems.get(i);
                if (index instanceof ItemStack item) {
                    inventory.setItem(slot, item);
                } else if (index instanceof Button button) {
                    inventory.setItem(slot, button.getItem());
                    paginatedButtons.put(slot, button);
                }
                i++;
            }
            paginatedButtons.put(previousPageButtonSlot, createPreviousPageButton());
            paginatedButtons.put(nextPageButtonSlot, createNextPageButton());
            inventory.setItem(previousPageButtonSlot, paginatedButtons.get(previousPageButtonSlot).getItem());
            inventory.setItem(nextPageButtonSlot, paginatedButtons.get(nextPageButtonSlot).getItem());
        } else {
            this.pages.get(this.page).run();
            for (var index : buttons.entrySet()) {
                inventory.setItem(index.getKey(), index.getValue().getItem());
                items.remove(index.getKey());
            }
            for (var index : items.entrySet()) {
                inventory.setItem(index.getKey(), index.getValue());
            }
        }
        return this;
    }

    /**
     * Renders the previous page in the history.
     * Can be called at any time, and will only change and render a new page if one is found in history.
     *
     * @return The GUI instance.
     */
    protected GUI<?> changePageToPrevious() {
        if (!history.empty()) changePage(history.removeLast());
        return this;
    }

    /**
     * Changes the current page and updates the GUI to show the new page.
     *
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

    /**
     * Clears all components from cache.
     */
    private void clearComponents() {
        buttons.clear();
        items.clear();
        if (isPaginated) {
            this.isPaginated = false;
            currentPaginationPage = 1;
            maxPaginationPages = 1;
            paginatedButtons.clear();
            paginatedItems.clear();
        }
    }

    /**
     * Add a clickable button to the GUI.
     *
     * @param slot   The slot to place the button in.
     * @param button The button to add.
     * @return The GUI instance.
     */
    protected GUI<?> addButton(final int slot, final Button button) {
        buttons.put(slot, button);
        return this;
    }

    /**
     * Add a non-clickable item to the GUI.
     *
     * @param slot The slot to place the item in.
     * @param item The item to add.
     * @return The GUI instance.
     */
    protected GUI<?> addItem(final int slot, final ItemStack item) {
        items.put(slot, item);
        return this;
    }

    /**
     * Add a paginated non-clickable item to the GUI. Use this if you wish to use paginated items on a page.<br>
     * <br>
     * This can be combined and mixed with link@addPaginatedButton
     *
     * @param item Item to add.
     * @return The GUI instance.
     */
    protected GUI<?> addPaginatedItem(final ItemStack item) {
        this.paginatedItems.add(item);
        return this;
    }

    /**
     * Add a paginated clickable button to the GUI. Use this if you wish to use paginated buttons on a page.<br>
     * <br>
     * This can be combined and mixed with link@addPaginatedItem
     *
     * @param button Button to add.
     * @return The GUI instance.
     */
    protected GUI<?> addPaginatedItem(final Button button) {
        this.paginatedItems.add(button);
        return this;
    }

    /**
     * Fills the inventory with the filler item.
     *
     * @return The GUI instance.
     */
    protected GUI<?> fill() {
        return fill(0, inventory.getSize() - 1);
    }

    /**
     * Fills a specific part of the GUI with the filler item.
     *
     * @param start Start slot of the fill area.
     * @param end   End slot of the fill area.
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
     *
     * @param allow True | False
     * @return The GUI instance.
     */
    protected GUI<?> allowItemPickup(final boolean allow) {
        this.allowItemPickup = allow;
        return this;
    }

    /**
     * Sets whether the Player is allowed to quick-close the GUI by e.g., pressing the escape button.
     *
     * @param allow True | False
     * @return The GUI instance.
     */
    protected GUI<?> allowQuickClose(final boolean allow) {
        this.allowQuickClose = allow;
        return this;
    }

    /**
     * Sets whether the GUI instance should be destroyed when closed by the Player.
     *
     * @param destroy True | False
     * @return The GUI instance.
     */
    protected GUI<?> destroyOnClose(final boolean destroy) {
        this.destroyOnClose = destroy;
        return this;
    }

    /**
     * Updates the filler item of the GUI to the new given Filler.
     *
     * @param filler Filler to update to.
     * @return The GUI instance.
     */
    protected GUI<?> setFiller(final Filler filler) {
        this.filler = filler;
        return this;
    }

    protected GUI<?> enablePagination() {
        return enablePagination(0, inventory.getSize() > 9 ? inventory.getSize() - 9 : 9);
    }

    protected GUI<?> enablePagination(final int startSlot, final int endSlot) {
        return enablePagination(startSlot, endSlot, inventory.getSize() - 4, inventory.getSize() - 6);
    }

    protected GUI<?> enablePagination(final int startSlot, final int endSlot, final int previousPageButtonSlot, final int nextPageButtonSlot) {
        return enablePagination(startSlot, endSlot, previousPageButtonSlot, nextPageButtonSlot, Material.ARROW, Material.ARROW);
    }

    protected GUI<?> enablePagination(final int startSlot, final int endSlot, final int previousPageButtonSlot, final int nextPageButtonSlot, final Material previousPageButtonMaterial, final Material nextPageButtonMaterial) {
        this.isPaginated = true;
        this.paginationStartSlot = startSlot;
        this.paginationEndSlot = endSlot;
        this.previousPageButtonSlot = previousPageButtonSlot;
        this.nextPageButtonSlot = nextPageButtonSlot;
        this.previousPageMaterial = previousPageButtonMaterial;
        this.nextPageMaterial = nextPageButtonMaterial;
        return this;
    }

    protected GUI<?> setHideTooltipOnPageButtons(final boolean enable) {
        this.hideTooltipsOnPageButtons = enable;
        return this;
    }

    protected GUI<?> setPreviousPageButtonTitle(final String title) {
        this.previousPaginatedPageItemTitle = title;
        return this;
    }

    protected GUI<?> setPreviousPageButtonLore(final String... lore) {
        this.previousPaginatedPageItemLore = Arrays.asList(lore);
        return this;
    }

    protected GUI<?> setNextPageButtonTitle(final String title) {
        this.nextPaginatedPageItemTitle = title;
        return this;
    }

    protected GUI<?> setNextPageButtonLore(final String... lore) {
        this.nextPaginatedPageItemLore = Arrays.asList(lore);
        return this;
    }

    private Button createPreviousPageButton() {
        final ItemStack item = new ItemStack(this.previousPageMaterial);
        if (hideTooltipsOnPageButtons) {
            ItemUtil.setHideTooltip(item, true);
        } else {
            ItemUtil.setName(item, previousPaginatedPageItemTitle);
            ItemUtil.setLore(item, previousPaginatedPageItemLore);
        }
        return Button.create(item, e -> {
            if (--currentPaginationPage < 1) currentPaginationPage = 1;
            renderPage();
        });
    }

    private Button createNextPageButton() {
        final ItemStack item = new ItemStack(this.nextPageMaterial);
        if (hideTooltipsOnPageButtons) {
            ItemUtil.setHideTooltip(item, true);
        } else {
            ItemUtil.setName(item, this.nextPaginatedPageItemTitle);
            ItemUtil.setLore(item, this.nextPaginatedPageItemLore);
        }
        return Button.create(item, e -> {
            if (++currentPaginationPage > maxPaginationPages) currentPaginationPage = maxPaginationPages;
            renderPage();
        });
    }

    /**
     * Used to cache the new instance of the GUI.
     *
     * @return The GUI instance.
     */
    private GUI<?> newInstance() {
        if (instances.containsKey(this.uuid)) instances.get(this.uuid).destroy();
        instances.put(uuid, this);
        return this;
    }

    /**
     * Opens the GUI for the Player.
     *
     * @return The GUI instance.
     */
    protected GUI<?> open() {
        registerListeners();
        player.openInventory(inventory);
        return this;
    }

    /**
     * Closes the GUI for the Player.
     *
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
     * Destroys the GUI instance and removes the instance and UUID from the cache.
     *
     * @return Whether the listeners were successfully unregistered.
     */
    protected boolean destroy() {
        inventory.close();
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
     *
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
     *
     * @param player The Player.
     * @return The GUI instance if one was found, otherwise null.
     */
    public static GUI<?> hasActiveInstance(final Player player) {
        return instances.getOrDefault(player.getUniqueId(), null);
    }

    /**
     * Destroys every active instance in cache.
     */
    public static void cleanup() {
        instances.forEach((uuid1, gui) -> gui.destroy());
    }
}