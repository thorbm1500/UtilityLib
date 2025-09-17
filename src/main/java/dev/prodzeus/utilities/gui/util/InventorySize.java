package dev.prodzeus.utilities.gui.util;

@SuppressWarnings("unused")
public enum InventorySize {
    TINY(9),
    EXTRA_SMALL(18),
    SMALL(27),
    MEDIUM(36),
    LARGE(45),
    EXTRA_LARGE(54)
    ;

    private final int size;

    InventorySize(final int size) {
        this.size = size;
    }

    public int get() {
        return size;
    }
}
