package com.fiuba.tdp.linkup.components.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class InterestsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<InterestItem> ITEMS = new ArrayList<InterestItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, InterestItem> ITEM_MAP = new HashMap<String, InterestItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(InterestItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static InterestItem createDummyItem(int position) {
        return new InterestItem("Interest " + position);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class InterestItem {
        public final String id;

        public InterestItem(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
