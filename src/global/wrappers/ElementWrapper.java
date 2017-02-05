package global.wrappers;

import base.ElementVisitor;
import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import core.actions.newPackageTemplate.dialogs.select.fileTemplate.SelectFileTemplateDialog;
import core.script.ScriptDialog;
import core.search.customPath.CustomPath;
import core.search.customPath.dialog.CustomPathDialog;
import global.listeners.ClickListener;
import global.models.BaseElement;
import global.utils.factories.WrappersFactory;
import global.utils.i18n.Localizer;
import global.views.IconLabel;
import icons.PluginIcons;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by CeH9 on 06.07.2016.
 */
public abstract class ElementWrapper extends BaseWrapper {

    private DirectoryWrapper parent;
    private Exception writeException;
    private PackageTemplateWrapper packageTemplateWrapper;


    //=================================================================
    //  Abstraction
    //=================================================================
    public abstract void accept(ElementVisitor visitor);

    public abstract void buildView(Project project, JPanel container);

    public abstract void removeMyself();

    public abstract void addElement(ElementWrapper element);

    public abstract BaseElement getElement();

    public abstract boolean isDirectory();

    public abstract ValidationInfo isNameValid(List<String> listAllTemplates);

    public abstract ValidationInfo validateFields();

    public abstract void setEnabled(boolean isEnabled);


    //=================================================================
    //  UI
    //=================================================================
    public JLabel jlName;
    public EditorTextField etfName;
    public IconLabel jlCustomPath;

    private void createPopupForEditMode(MouseEvent mouseEvent) {
        JPopupMenu popupMenu = new JBPopupMenu();

        JMenuItem itemAddFile = new JBMenuItem(Localizer.get("AddFile"), AllIcons.FileTypes.Text);
        JMenuItem itemAddDirectory = new JBMenuItem(Localizer.get("AddDirectory"), AllIcons.Nodes.Package);
        JMenuItem itemDelete = new JBMenuItem(Localizer.get("Delete"), AllIcons.Actions.Delete);

        itemAddFile.addActionListener(e -> AddFile());
        itemAddDirectory.addActionListener(e -> addDirectory());
        itemDelete.addActionListener(e -> deleteElement());
        // if NOT root element
        if (getParent() != null) {
            popupMenu.add(itemDelete);
        }

        popupMenu.add(itemAddFile);
        popupMenu.add(itemAddDirectory);

        addGroovyMenuItems(popupMenu);
        addCustomPathMenuItems(popupMenu);


        popupMenu.show(jlName, mouseEvent.getX(), mouseEvent.getY());
    }

    public void reBuild() {
        packageTemplateWrapper.reBuildView();
    }

    private void addGroovyMenuItems(JPopupMenu popupMenu) {
        // With Groovy script
        if (getElement().getScript() != null && !getElement().getScript().isEmpty()) {
            JMenuItem itemEdit = new JBMenuItem(Localizer.get("EditScript"), PluginIcons.SCRIPT);
            JMenuItem itemDelete = new JBMenuItem(Localizer.get("DeleteScript"), AllIcons.Actions.Delete);

            itemEdit.addActionListener(e -> new ScriptDialog(
                    getPackageTemplateWrapper().getProject(), getElement().getScript()) {
                @Override
                public void onSuccess(String code) {
                    getElement().setScript(code);
                    updateComponentsState();
                }
            }.show());

            itemDelete.addActionListener(e -> {
                getElement().setScript("");
                updateComponentsState();
            });

            popupMenu.add(itemEdit);
            popupMenu.add(itemDelete);
        } else {
            // Without Groovy script
            JMenuItem itemAddGroovy = new JBMenuItem(Localizer.get("AddScript"), PluginIcons.SCRIPT);

            itemAddGroovy.addActionListener(e -> new ScriptDialog(getPackageTemplateWrapper().getProject()) {
                @Override
                public void onSuccess(String code) {
                    getElement().setScript(code);
                    updateComponentsState();
                }
            }.show());

            popupMenu.add(itemAddGroovy);
        }
    }

    private void addCustomPathMenuItems(JPopupMenu popupMenu) {
        if (getElement().getCustomPath() != null) {
            JMenuItem itemEdit = new JBMenuItem(Localizer.get("EditCustomPath"), PluginIcons.CUSTOM_PATH);
            JMenuItem itemDelete = new JBMenuItem(Localizer.get("DeleteCustomPath"), AllIcons.Actions.Delete);

            itemEdit.addActionListener(e -> new CustomPathDialog(getPackageTemplateWrapper().getProject(), getElement().getCustomPath()) {
                @Override
                public void onSuccess(CustomPath customPath) {
                    getElement().setCustomPath(customPath);
                    updateComponentsState();
                }
            }.show());

            itemDelete.addActionListener(e -> {
                getElement().setCustomPath(null);
                updateComponentsState();
            });

            popupMenu.add(itemEdit);
            popupMenu.add(itemDelete);
        } else {
            JMenuItem itemAdd = new JBMenuItem(Localizer.get("AddCustomPath"), PluginIcons.CUSTOM_PATH);

            itemAdd.addActionListener(e -> new CustomPathDialog(getPackageTemplateWrapper().getProject(),null) {
                @Override
                public void onSuccess(CustomPath customPath) {
                    getElement().setCustomPath(customPath);
                    updateComponentsState();
                }
            }.show());

            popupMenu.add(itemAdd);
        }
    }

    protected void updateOptionIcons() {
        if (getElement().getScript() != null && !getElement().getScript().isEmpty()) {
            jlGroovy.enableIcon();
        } else {
            jlGroovy.disableIcon();
        }

        if (getElement().getCustomPath() != null) {
            jlCustomPath.enableIcon();
        } else {
            jlCustomPath.disableIcon();
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    public void addMouseListener() {
        jlName.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    switch (getPackageTemplateWrapper().getMode()) {
                        case EDIT:
                        case CREATE:
                            createPopupForEditMode(mouseEvent);
                            break;
                        case USAGE:
                            break;
                    }
                }
            }
        });
    }

    public void addDirectory() {
        getPackageTemplateWrapper().collectDataFromFields();

        DirectoryWrapper dirParent;
        if (isDirectory()) {
            dirParent = ((DirectoryWrapper) this);
        } else {
            dirParent = getParent();
        }

        dirParent.addElement(WrappersFactory.createNewWrappedDirectory(dirParent));
        dirParent.reBuild();
    }

    public void deleteElement() {
        removeMyself();

        getParent().getPackageTemplateWrapper().collectDataFromFields();
        getParent().reBuild();
    }

    public void AddFile() {
        SelectFileTemplateDialog dialog = new SelectFileTemplateDialog(getPackageTemplateWrapper().getProject()) {
            @Override
            public void onSuccess(FileTemplate fileTemplate) {
                getPackageTemplateWrapper().collectDataFromFields();

                DirectoryWrapper parent;
                if (isDirectory()) {
                    parent = ((DirectoryWrapper) ElementWrapper.this);
                } else {
                    parent = getParent();
                }
                parent.addElement(WrappersFactory.createNewWrappedFile(parent, fileTemplate.getName(), fileTemplate.getExtension()));
                parent.reBuild();
            }

            @Override
            public void onCancel() {
            }
        };
        dialog.show();
    }


    //=================================================================
    //  Getters | Setters
    //=================================================================
    public DirectoryWrapper getParent() {
        return parent;
    }

    public void setParent(DirectoryWrapper parent) {
        this.parent = parent;
    }

    public PackageTemplateWrapper getPackageTemplateWrapper() {
        return packageTemplateWrapper;
    }

    public void setPackageTemplateWrapper(PackageTemplateWrapper packageTemplateWrapper) {
        this.packageTemplateWrapper = packageTemplateWrapper;
    }

    public Exception getWriteException() {
        return writeException;
    }

    public void setWriteException(Exception writeException) {
        this.writeException = writeException;
    }

}
