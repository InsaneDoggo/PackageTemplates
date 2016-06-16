package models;

import com.intellij.ui.EditorTextField;

import javax.swing.*;

/**
 * Created by Arsen on 15.06.2016.
 */

public class InputBlock {

    private TemplateElement element;
    private JLabel label;
    private EditorTextField textField;

    public InputBlock(TemplateElement element, JLabel label, EditorTextField textField) {
        this.element = element;
        this.label = label;
        this.textField = textField;
    }

    public TemplateElement getElement() {
        return element;
    }

    public void setElement(TemplateElement element) {
        this.element = element;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public EditorTextField getTextField() {
        return textField;
    }

    public void setTextField(EditorTextField textField) {
        this.textField = textField;
    }
}
