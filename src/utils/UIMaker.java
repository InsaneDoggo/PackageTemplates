package utils;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.template.impl.TemplateColors;
import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import custom.components.TemplateView;
import custom.dialogs.SelectFileTemplateDialog;
import custom.impl.ClickListener;
import models.InputBlock;
import models.TextRange;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by CeH9 on 16.06.2016.
 */

public class UIMaker {

    public static final int DEFAULT_PADDING = 0;
    public static final int PADDING = 20;
    public static final int PADDING_LABEL = 8;
    public static final int DIALOG_MIN_WIDTH = 400;

    public static EditorTextField getEditorTextField(String defValue, Project project) {
        EditorTextField etfName = new EditorTextField();
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);

        etfName.addSettingsProvider(new EditorSettingsProvider() {
            @Override
            public void customizeSettings(EditorEx editor) {
                addHighlightListener(project, etfName, editor);
                etfName.setText(etfName.getText());
            }
        });
        etfName.setText(defValue);
        return etfName;
    }

    private static void addHighlightListener(final Project project, final EditorTextField etfName, EditorEx editor) {
        etfName.addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(DocumentEvent event) {
            }

            @Override
            public void documentChanged(DocumentEvent event) {
                if (editor == null || !StringTools.containsVariable(event.getDocument().getText())) {
                    return;
                }
                //highlight text
                applyHighlightRange(event.getDocument().getText(), project, editor);
            }
        });
    }

    public static void applyHighlightRange(String text, Project project, Editor editor) {
        if (project == null) {
            Logger.log("project = null");
            return;
        }

        EditorColorsScheme scheme = editor.getColorsScheme(); // or EditorColorsManager.getInstance().getGlobalScheme()

        TextAttributes attributes = scheme.getAttributes(TemplateColors.TEMPLATE_VARIABLE_ATTRIBUTES);

        ArrayList<TextRange> list = StringTools.findVariable(text);
        for (TextRange textRange : list) {
            HighlightManager.getInstance(project).addRangeHighlight(
                    editor,
                    textRange.getBegin(),
                    textRange.getEnd(),
                    attributes,
                    true,
                    null
            );
        }
    }

    public static void setLeftPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                padding,
                DEFAULT_PADDING,
                DEFAULT_PADDING
        ));
    }

    public static void setRightPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                DEFAULT_PADDING,
                DEFAULT_PADDING,
                padding
        ));
    }

    public static void setHorizontalPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                padding,
                DEFAULT_PADDING,
                padding
        ));
    }

    public static JPanel getClassPanel(InputBlock inputBlock, int paddingScale, Icon icon) {
        return getDefaultPanel(inputBlock, paddingScale, icon);
    }

    public static JPanel getDirectoryPanel(InputBlock inputBlock, int paddingScale) {
        return getDefaultPanel(inputBlock, paddingScale, AllIcons.Nodes.Package);
    }

    public static JPanel getDefaultPanel(InputBlock inputBlock, int paddingScale, Icon icon) {
        JPanel container = new JPanel(new GridBagLayout());
        setLeftPadding(container, DEFAULT_PADDING + PADDING * paddingScale);

        JLabel jLabel = new JLabel(icon, SwingConstants.LEFT);
        if (!inputBlock.getElement().isDirectory()) {
            jLabel.setText(inputBlock.getElement().getTemplateName());
        }
        inputBlock.getTfName().setText(inputBlock.getElement().getName());

        GridBag bag = getDefaultGridBag();
        container.add(jLabel, bag.nextLine().next().insets(0, 0, 0, 8));
        container.add(inputBlock.getTfName(), bag.next());

        return container;
    }

    @NotNull
    public static GridBag getDefaultGridBag() {
        return new GridBag()
                .setDefaultWeightX(1, 1)
                .setDefaultInsets(new Insets(4, 0, 4, 0))
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    @NotNull
    public static GridBag getDefaultGridBagForGlobals() {
        return new GridBag()
//                .setDefaultWeightX(0, 0.02)
                .setDefaultWeightX(1, 0.5)
                .setDefaultWeightX(2, 0.5)
                .setDefaultInsets(new Insets(4, 0, 4, 0))
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    public static JPanel getClassView(TemplateView templateView, Project project) {
        JPanel container = new JPanel(new GridBagLayout());
        setLeftPadding(container, DEFAULT_PADDING);

        JLabel jLabel = new JLabel(getIconByFileExtension(templateView.getExtension()), SwingConstants.LEFT);
        templateView.setJlName(jLabel);
        jLabel.setText(templateView.getTemplateName());
        setRightPadding(jLabel, PADDING_LABEL);

        EditorTextField etfName = getEditorTextField(templateView.getPredefinedName(), project);
        templateView.setEtfName(etfName);


        GridBag bag = getDefaultGridBag();
        container.add(jLabel, bag.nextLine().next());
        container.add(etfName, bag.next());

        addMouseListener(templateView, container, project);

        return container;
    }

    public static JPanel getPackageView(TemplateView templateView, Project project) {
        JPanel container = new JPanel(new GridBagLayout());
        setLeftPadding(container, DEFAULT_PADDING);

        JLabel jLabel = new JLabel(AllIcons.Nodes.Package, SwingConstants.LEFT);
        templateView.setJlName(jLabel);
        jLabel.setText("Directory");
        setRightPadding(jLabel, PADDING_LABEL);

        EditorTextField etfName = getEditorTextField(templateView.getPredefinedName(), project);
        templateView.setEtfName(etfName);

        GridBag bag = getDefaultGridBag();
        container.add(jLabel, bag.nextLine().next());
        container.add(etfName, bag.next());

        addMouseListener(templateView, container, project);


        return container;
    }

    private static void addMouseListener(final TemplateView templateView, final JPanel container, Project project) {
        container.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    JPopupMenu popupMenu = new JBPopupMenu();

                    JMenuItem itemAddFile = new JBMenuItem("Add File", AllIcons.FileTypes.Text);
                    JMenuItem itemAddDirectory = new JBMenuItem("Add Directory", AllIcons.Nodes.Package);
                    JMenuItem itemDelete = new JBMenuItem("Delete", AllIcons.Actions.Delete);

                    itemAddFile.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            AddFile(templateView, project);
                        }
                    });
                    itemAddDirectory.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addDirectory(templateView, project);
                        }
                    });
                    itemDelete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            deleteFile(templateView, project);
                        }
                    });

                    popupMenu.add(itemAddFile);
                    popupMenu.add(itemAddDirectory);
                    popupMenu.add(itemDelete);
                    popupMenu.show(container, mouseEvent.getX(), mouseEvent.getY());
                }
            }
        });
    }

    public static boolean isLeftClick(MouseEvent event) {
        return SwingUtilities.isLeftMouseButton(event);
    }

    private static void addDirectory(TemplateView templateView, Project project) {
        templateView.collectDataFromFields();

        TemplateView parent;
        if (templateView.isDirectory()) {
            parent = templateView;
        } else {
            parent = templateView.getTemplateParent();
        }
        templateView.addTemplate(new TemplateView("Unnamed", parent));
        templateView.reBuild(project);
    }

    public static void deleteFile(TemplateView templateView, Project project) {
        templateView.removeMyself();

        templateView.collectDataFromFields();
        templateView.reBuild(project);
    }

    public static void AddFile(TemplateView templateView, Project project) {
        SelectFileTemplateDialog dialog = new SelectFileTemplateDialog(project) {
            @Override
            public void onSuccess(FileTemplate fileTemplate) {
                templateView.collectDataFromFields();

                TemplateView parent;
                if (templateView.isDirectory()) {
                    parent = templateView;
                } else {
                    parent = templateView.getTemplateParent();
                }
                templateView.addTemplate(new TemplateView("Unnamed", fileTemplate.getName(), fileTemplate.getExtension(), parent));
                templateView.reBuild(project);
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }
        };
        dialog.show();
    }

    public static Icon getIconByFileExtension(String extension) {
        return FileTypeManager.getInstance().getFileTypeByExtension(extension).getIcon();
    }

}
