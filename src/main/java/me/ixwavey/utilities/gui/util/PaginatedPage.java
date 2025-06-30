package me.ixwavey.utilities.gui.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class PaginatedPage extends Page {

    /**
     * This method should store all creation and addition of new paginated items, and will be called on creation of a new Pagination Page instance.
     */
    protected abstract void initialize();

    private final List<Object> paginationList = new ArrayList<>();

    public final int paginationStartSlot;
    public final int paginationEndSlot;
    private final int maxPaginationItemsPerPage;

    protected int currentPage = 1;
    protected final int maxPages;

    private final Filler filler;

    public PaginatedPage(final int paginationStartSlot, final int paginationEndSlot) {
        this(paginationStartSlot,paginationEndSlot,null);
    }

    public PaginatedPage(final int paginationStartSlot, final int paginationEndSlot, @Nullable final Filler filler) {
        this.paginationStartSlot = paginationStartSlot;
        this.paginationEndSlot = paginationEndSlot;
        this.maxPaginationItemsPerPage = (paginationEndSlot + 1) - (paginationStartSlot + 1);
        this.filler = filler;

        initialize();
        this.maxPages = (paginationList.size() / this.maxPaginationItemsPerPage) + 1;
    }

    protected PaginatedPage addPaginatedItem(final Button button) {
        paginationList.add(button);
        return this;
    }

    /**
     * Generates the current page.
     */
    public void generate() {
        int item = (currentPage - 1) * maxPaginationItemsPerPage;
        final int listSize = paginationList.size();
        if (filler != null) {
            for (int i = paginationStartSlot; i <= paginationEndSlot; i++) {
                if (item > listSize) break;
                addItem(i,filler.get());
                item++;
            }
        }
        item = (currentPage - 1) * maxPaginationItemsPerPage;
        for (int i = paginationStartSlot; i <= paginationEndSlot; i++) {
            if (item > listSize) break;
            if (paginationList.get(item) instanceof Button button) addButton(i, button);
            else if (paginationList.get(item) instanceof ItemStack itemStack) addItem(i, itemStack);
            item++;
        }
    }

    /**
     * This only safely decreases the current page and will not update the menu.
     * @return The PaginatedPage instance.
     */
    protected PaginatedPage previousPage() {
        currentPage--;
        if (currentPage < 1) currentPage = 1;
        return this;
    }

    /**
     * This only safely increments the current page and will not update the menu.
     * @return The PaginatedPage instance.
     */
    protected PaginatedPage nextPage() {
        currentPage++;
        if (currentPage > maxPages) currentPage = maxPages;
        return this;
    }


}
