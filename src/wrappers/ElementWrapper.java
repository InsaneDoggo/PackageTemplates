package wrappers;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import custom.dialogs.SelectFileTemplateDialog;
import custom.impl.ClickListener;
import models.BaseElement;
import utils.WrappersFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CeH9 on 06.07.2016.
 */
public abstract class ElementWrapper extends BaseWrapper {

    public JLabel jlName;
    public EditorTextField etfName;
    private DirectoryWrapper parent;

    private PackageTemplateWrapper packageTemplateWrapper;
    // check box
    //btn for groovy

    public abstract void buildView(Project project, JPanel container, GridBag bag);
    public abstract void removeMyself();
    public abstract void addElement(ElementWrapper element);
    public abstract BaseElement getElement();
    public abstract boolean isDirectory();
    public abstract void replaceNameVariable(HashMap<String, String> mapVariables);
    public abstract ValidationInfo isNameValid(List<String> listAllTemplates);
    public abstract ValidationInfo validateFields();
    public abstract void writeFile(PsiDirectory currentDir, Project project);
    public abstract void updateParents(DirectoryWrapper dwParent);
    public abstract void initNonSerializableFields();
    public abstract void setEnabled(boolean isEnabled);

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

    public void reBuild() {
        packageTemplateWrapper.reBuildView();
    }

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

    private void createPopupForEditMode(MouseEvent mouseEvent) {
        JPopupMenu popupMenu = new JBPopupMenu();

        JMenuItem itemAddFile = new JBMenuItem("Add File", AllIcons.FileTypes.Text);
        JMenuItem itemAddDirectory = new JBMenuItem("Add Directory", AllIcons.Nodes.Package);
        JMenuItem itemDelete = new JBMenuItem("Delete", AllIcons.Actions.Delete);

        itemAddFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddFile();
            }
        });
        itemAddDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDirectory();
            }
        });
        itemDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteElement();
            }
        });

        popupMenu.add(itemAddFile);
        popupMenu.add(itemAddDirectory);

        // if not root element
        if (getParent() != null) {
            popupMenu.add(itemDelete);
        }
        popupMenu.show(jlName, mouseEvent.getX(), mouseEvent.getY());
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
                System.out.println("onCancel");
            }
        };
        dialog.show();
    }

}
