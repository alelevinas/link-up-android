package com.fiuba.tdp.linkup.components.dummy;

import com.fiuba.tdp.linkup.domain.LinkUpMatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<LinkUpMatch> ITEMS = new ArrayList<LinkUpMatch>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createLinkUpMatch(i));
        }
    }

    private static void addItem(LinkUpMatch item) {
        ITEMS.add(item);
    }

    private static LinkUpMatch createLinkUpMatch(int position) {
        return new LinkUpMatch(String.valueOf(position), "Item " + position, "https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/13912571_10154556791580967_9146574132461188875_n.jpg?oh=480f549e46d5aff420ffa44a616a0167&oe=5A5CF8A2", "1506300199145");
    }
}
