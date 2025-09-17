package dev.prodzeus.utilities.lists;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class Sorting {

    /**
     * Sort a List from high to low.<br>
     * Supported datatypes include; Byte, Integer, Long, Float, Double
     * @param list List to sort
     */
    public static void highToLow(final List<Number> list) {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = list.size() - 1; i >= 0; i--) {
                if (i - 1 < 0) continue;
                if ((Double) list.get(i) == Math.max((Double) list.get(i),(Double) list.get(i - 1))) {
                    Collections.swap(list, i, i - 1);
                    sorted = false;
                }
            }
        }
    }
}
