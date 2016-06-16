package utils;

import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 16.06.2016.
 */

public class UIMaker {

    public static final int DEFAULT_PADDING = 0;
    public static final int PADDING = 10;

    public static EditorTextField getEditorTextField(String defValue, int paddingScale) {
        EditorTextField etfName = new EditorTextField();
        setLeftPadding(etfName, PADDING * paddingScale);
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);
        etfName.setMaximumSize(new Dimension(Integer.MAX_VALUE, etfName.getPreferredSize().height));
        etfName.setText(defValue);
        return etfName;
    }

    public static JLabel getLabel(String text, int paddingScale) {
        JLabel label = new JLabel(text);
        setLeftPadding(label, PADDING * paddingScale);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static void setLeftPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                padding,
                DEFAULT_PADDING,
                DEFAULT_PADDING
        ));
    }

}
