package global.models;

import javax.swing.*;
import java.util.List;

/**
 * Created by CeH9 on 27.06.2016.
 */
public class TemplateListModel<T> extends AbstractListModel<T> {

    private List<T> list;

    public TemplateListModel(List<T> list) {
        this.list = list;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public T getElementAt(int index) {
        return list.get(index);
    }
}