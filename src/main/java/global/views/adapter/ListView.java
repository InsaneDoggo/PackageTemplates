package global.views.adapter;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen on 17.04.2017.
 */
public abstract class ListView<Item> extends JPanel {

    public List<Item> list;

    public ListView(List<Item> list) {
        this(new MigLayout(new LC().insets("0").gridGap("0", "2pt")), list);
    }

    public ListView(MigLayout layout, List<Item> list) {
        super(layout);
        this.list = list;

        init();
        buildUI();
    }

    private void init() {
        listCollectors = new ArrayList<>();
    }


    //=================================================================
    //  UI
    //=================================================================
    @Nullable
    public abstract CollectDataFromUI onBuildView(Item item, int pos);

    private void buildUI() {
        for (int pos = 0; pos < list.size(); pos++) {
            Item item = list.get(pos);

            CollectDataFromUI collectDataFromUI = onBuildView(item, pos);
            if (collectDataFromUI != null) {
                listCollectors.add(collectDataFromUI);
            }
        }
    }

    private void clearUI() {
        removeAll();
    }

    //=================================================================
    //  Collect
    //=================================================================
    ArrayList<CollectDataFromUI> listCollectors;

    public interface CollectDataFromUI {
        void collect();
    }

    /**
     * Записывает инфу из вьюх в коллекцию
     */
    public void collectDataFromUI() {
        for (CollectDataFromUI item : listCollectors) {
            item.collect();
        }
    }


    //=================================================================
    //  API
    //=================================================================
    public void reBuildUI() {
        listCollectors.clear();
        clearUI();
        buildUI();
    }

}
