package core.textInjection.dialog;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.IconUtil;
import core.textInjection.TextInjection;
import global.listeners.ClickListener;
import global.utils.factories.GsonFactory;
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
                ptWrapper.removeTextInjection(TextInjectionWrapper.this);
                ptWrapper.reBuildTextInjections();
            }
        });


        if (ptWrapper.getMode() != PackageTemplateWrapper.ViewMode.USAGE) {
            btnEdit.addMouseListener(new ClickListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showEditDialog(ptWrapper);
                }
            });
            container.add(cbEnabled, new CC());
            container.add(btnDelete, new CC());
            container.add(btnEdit, new CC());
        } else {
            container.add(cbEnabled, new CC());
        }

        container.add(jlDescription, new CC().wrap().pushX().growX().gapLeft("4pt"));
    }

    private void showEditDialog(PackageTemplateWrapper ptWrapper) {
        new TextInjectionDialog(ptWrapper.getProject(), GsonFactory.cloneObject(textInjection, TextInjection.class)) {
            @Override
            public void onSuccess(TextInjection result) {
                textInjection.copyPropertiesFrom(result);
                ptWrapper.reBuildTextInjections();
            }
        }.show();
    }


    //=================================================================
    //  Utils
    //=================================================================
    public void collectDataFromFields() {
        //nothing
        textInjection.setEnabled(cbEnabled.isSelected());
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
