package models;

import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import custom.components.TemplateView;
import custom.components.VariableView;
import utils.GridBagFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CeH9 on 25.06.2016.
 */
public class TemplateContainer {
    private String name;
    private String description;
    private TemplateView templateView;
    private ArrayList<VariableView> listVariableView;

    private GridBag bag;
    private JPanel container;
    private EditorTextField etfTemplateName;

    public TemplateContainer(String name, String description, TemplateView templateView) {
        this.name = name;
        this.description = description;
        this.templateView = templateView;
        this.listVariableView = new ArrayList<>();
    }

    public EditorTextField getEtfTemplateName() {
        return etfTemplateName;
    }

    public void setEtfTemplateName(EditorTextField etfTemplateName) {
        this.etfTemplateName = etfTemplateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TemplateView getTemplateView() {
        return templateView;
    }

    public void setTemplateView(TemplateView templateView) {
        this.templateView = templateView;
    }

    public ArrayList<VariableView> getListVariableView() {
        return listVariableView;
    }

    public void setListVariableView(ArrayList<VariableView> listVariableView) {
        this.listVariableView = listVariableView;
    }

    public void addVariable(VariableView variable) {
        listVariableView.add(variable);
    }

    public void addTemplateView(TemplateView templateView) {
        if (templateView.getListTemplateView() != null) {
            templateView.getListTemplateView().add(templateView);
        }
    }

    public JPanel buildView() {
        if (container == null) {
            container = new JPanel(new GridBagLayout());
            bag = GridBagFactory.getGridBagForGlobals();
        }

        JLabel label = new JLabel("Global Variables", JLabel.CENTER);
        container.add(label, bag.nextLine().next().fillCellHorizontally().coverLine(3));

        for (VariableView variableView : listVariableView) {
            variableView.buildView(this, container, bag);
        }

        return container;
    }

    public void rebuildView() {
        if (container != null) {
            container.removeAll();
            bag = GridBagFactory.getGridBagForGlobals();
        }

        buildView();
    }

    public void collectDataFromFields() {
        setName(getEtfTemplateName().getText());

        getTemplateView().collectDataFromFields();

        for (VariableView variableView : getListVariableView()) {
            variableView.collectDataFromFields();
        }
    }

    public HashMap<String, String> collectGlobalVarsAsMap() {
        HashMap<String, String> map = new HashMap<>();
        for( VariableView variableView : getListVariableView() ){
            map.put(variableView.getKey(), variableView.getValue());
        }
        return map;
    }
}
