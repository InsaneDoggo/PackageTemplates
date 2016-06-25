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
import com.intellij.util.ui.MouseEventHandler;
import custom.components.TemplateView;
import custom.dialogs.SelectTemplateDialog;
import models.InputBlock;
import models.TextRange;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
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
        EditorTextField etfName = new EditorTextField("Test");
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

    public static JPanel getClassPanel(InputBlock inputBlock, int paddingScale) {
        return getDefaultPanel(inputBlock, paddingScale, AllIcons.Nodes.Class);
    }

    public static JPanel getDirectoryPanel(InputBlock inputBlock, int paddingScale) {
        return getDefaultPanel(inputBlock, paddingScale, AllIcons.Nodes.Package);
    }

    public static JPanel getDefaultPanel(InputBlock inputBlock, int paddingScale, Icon icon) {
        JPanel container = new JPanel(new GridBagLayout());

        GridBag bag = getDefaultGridBag();

        JLabel jLabel = new JLabel(icon, SwingConstants.LEFT);
        setLeftPadding(container, DEFAULT_PADDING + PADDING * paddingScale);

        container.add(jLabel, bag.nextLine().next());
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

    public static JPanel getClassView(TemplateView templateView, Project project) {
        JPanel container = new JPanel(new GridBagLayout());
        setLeftPadding(container, DEFAULT_PADDING);

        JLabel jLabel = new JLabel(getIconByFileExtension(templateView.getExtension()), SwingConstants.LEFT);
        jLabel.setText(templateView.getTemplateName());
        setRightPadding(jLabel, PADDING_LABEL);

        EditorTextField etfName = new EditorTextField("");
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        jLabel.setText("Directory");
        setRightPadding(jLabel, PADDING_LABEL);

        EditorTextField etfName = new EditorTextField("");
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBag bag = getDefaultGridBag();
        container.add(jLabel, bag.nextLine().next());
        container.add(etfName, bag.next());

        addMouseListener(templateView, container, project);

        return container;
    }

    private static void addMouseListener(final TemplateView templateView, final JPanel container, Project project) {
        container.addMouseListener(new MouseEventHandler() {
            @Override
            protected void handle(MouseEvent event) {
                if (event.getID() == MouseEvent.MOUSE_RELEASED && SwingUtilities.isRightMouseButton(event)) {
                    JPopupMenu popupMenu = new JBPopupMenu();

                    JMenuItem itemAddFile = new JBMenuItem("Add file", AllIcons.FileTypes.Text);
                    JMenuItem itemAddDirectory = new JBMenuItem("Add directory", AllIcons.Nodes.Package);
                    JMenuItem itemDelete = new JBMenuItem("Delete", AllIcons.Actions.Delete);

                    itemAddFile.addMouseListener(new MouseEventHandler() {
                        @Override
                        protected void handle(MouseEvent event) {
                            if (isLeftClick(event)) {
                                AddFile(templateView, project);
                                System.out.println("AddFile");
                            }
                        }
                    });
                    itemAddDirectory.addMouseListener(new MouseEventHandler() {
                        @Override
                        protected void handle(MouseEvent event) {
                            if (isLeftClick(event)) {
                                addDirectory(templateView, project);
                                System.out.println("AddFolder");
                            }
                        }
                    });
                    itemDelete.addMouseListener(new MouseEventHandler() {
                        @Override
                        protected void handle(MouseEvent event) {
                            if (isLeftClick(event)) {
                                deleteFile(templateView);
                                System.out.println("Delete");
                            }
                        }
                    });

                    popupMenu.add(itemAddFile);
                    popupMenu.add(itemAddDirectory);
                    popupMenu.add(itemDelete);
                    popupMenu.show(container, event.getX(), event.getY());
                }
            }
        });
    }

    private static boolean isLeftClick(MouseEvent event) {
        return event.getID() == MouseEvent.MOUSE_RELEASED && SwingUtilities.isLeftMouseButton(event);
    }

    private static void addDirectory(TemplateView templateView, Project project) {
        TemplateView parent;
        if (templateView.isDirectory()) {
            parent = templateView;
        } else {
            parent = templateView.getTemplateParent();
        }
        templateView.addTemplate(new TemplateView("Unnamed", parent));
        templateView.reBuild();
    }

    private static void deleteFile(TemplateView templateView) {
        templateView.removeMyself();
        templateView.reBuild();
    }

    private static void AddFile(TemplateView templateView, Project project) {
        SelectTemplateDialog dialog = new SelectTemplateDialog(project) {
            @Override
            public void onSuccess(FileTemplate fileTemplate) {
                TemplateView parent;
                if (templateView.isDirectory()) {
                    parent = templateView;
                } else {
                    parent = templateView.getTemplateParent();
                }
                templateView.addTemplate(new TemplateView("Unnamed", fileTemplate.getName(), fileTemplate.getExtension(), parent));
                templateView.reBuild();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }
        };
        dialog.show();
    }

    private static Icon getIconByFileExtension(String extension) {
        return FileTypeManager.getInstance().getFileTypeByExtension(extension).getIcon();
    }

}
