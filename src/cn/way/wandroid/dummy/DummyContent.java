package cn.way.wandroid.dummy;

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
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "AsynchronousHttpClient"));
        addItem(new DummyItem("2", "Gson"));
        addItem(new DummyItem("3", "Green Dao"));
        addItem(new DummyItem("4", "LifeManager"));
        addItem(new DummyItem("5", "Bulletin"));
        addItem(new DummyItem("6", "Graphics"));
        addItem(new DummyItem("7", "Dialog"));
        addItem(new DummyItem("8", "PullRefresh"));
        addItem(new DummyItem("9", "ImageLoader"));
        addItem(new DummyItem("10", "FragmentTabBar"));
        addItem(new DummyItem("11", "Views"));
        addItem(new DummyItem("12", "友盟社会化组件"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
