package custom.components;

import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.util.ui.GridBag;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.sun.tools.internal.xjc.reader.Ring.add;
import static utils.UIMaker.getDefaultGridBag;

/**
 * Created by CeH9 on 23.06.2016.
 */
public class TemplateView extends JPanel {

    private String defaultName;
    private String extension;
    private String templateName;
    private boolean isDirectory;
    private ArrayList<TemplateView> listTemplateView;

    public TemplateView(String defaultName, String templateName, String extension, boolean isDirectory, ArrayList<TemplateView> listTemplateView) {
        this.defaultName = defaultName;
        this.templateName = templateName;
        this.extension = extension;
        this.isDirectory = isDirectory;
        this.listTemplateView = listTemplateView;
    }

    public TemplateView buildView() {
        setLayout(new GridBagLayout());
        GridBag bag = getDefaultGridBag();

        if( isDirectory() ){
            add(UIMaker.getPackageView(this), bag.nextLine().next());

            for(TemplateView templateView : getListTemplateView()){
                TemplateView view = templateView.buildView();
                UIMaker.setLeftPadding(view, UIMaker.PADDING + UIMaker.DEFAULT_PADDING);
                add(view, bag.nextLine().next());
            }
        } else {
            add(UIMaker.getClassView(this), bag.nextLine().next());
        }
        return this;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public ArrayList<TemplateView> getListTemplateView() {
        return listTemplateView;
    }

    public void setListTemplateView(ArrayList<TemplateView> listTemplateView) {
        this.listTemplateView = listTemplateView;
    }
}
