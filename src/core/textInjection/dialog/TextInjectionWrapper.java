package core.textInjection.dialog;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.IconUtil;
import core.textInjection.TextInjection;
import global.listeners.ClickListener;
import global.utils.Logger;
import global.wrappers.PackageTemplateWrapper;
import net.miginfocom.layout.CC;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Created by Arsen on 19.03.2017.
 */
public class TextInjectionWrapper {

    private TextInjection textInjection;

    public TextInjectionWrapper(TextInjection textInjection) {
        this.textInjection = textInjection;
    }


    //=================================================================
    //  UI
    //=================================================================
    private JBCheckBox cbEnabled;
    private JLabel jlDescription;

    public void buildView(PackageTemplateWrapper ptWrapper, JPanel container) {
        cbEnabled = new JBCheckBox("", textInjection.isEnabled());
        jlDescription = new JLabel(textInjection.getDescription());
        JButton btnDelete = new JButton(IconUtil.getRemoveIcon());
        JButton btnEdit = new JButton(IconUtil.getEditIcon());

        btnDelete.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //todo delete
                Logger.log("delete");
            }
        });

        container.add(cbEnabled, new CC().spanX().split(3).pushX().growX());
        container.add(btnDelete, new CC());

        if (ptWrapper.getMode() != PackageTemplateWrapper.ViewMode.USAGE) {
            btnEdit.addMouseListener(new ClickListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //todo edit
                    Logger.log("edit");
                }
            });
            container.add(btnEdit, new CC());
        }

        container.add(jlDescription, new CC().wrap());
    }


    //=================================================================
    //  Utils
    //=================================================================
    public void collectDataFromFields() {
        //nothing
    }


    //=================================================================
    //  G|S
    //=================================================================
    public TextInjection getTextInjection() {
        return textInjection;
    }

    public void setTextInjection(TextInjection textInjection) {
        this.textInjection = textInjection;
    }

}
