package core.actions.custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import core.actions.custom.interfaces.IHasWriteRules;
import core.search.SearchAction;
import core.search.SearchEngine;
import core.writeRules.WriteRules;
import global.Const;
import global.dialogs.impl.NeverShowAskCheckBox;
import global.models.BaseElement;
import global.models.File;
import global.utils.Logger;
import global.utils.file.FileWriter;
import global.utils.file.PsiHelper;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileFromTemplateAction extends SimpleAction implements IHasWriteRules {

    private Properties properties;
    private FileTemplate template;
    private File file;
    private Project project;

    //result
    private PsiElement psiElementResult;

    public CreateFileFromTemplateAction(Properties properties, FileTemplate template, File file, Project project) {
        this.properties = properties;
        this.template = template;
        this.file = file;
        this.project = project;
    }

    @Override
    public boolean run() {
        psiElementResult = null;

        if (parentAction instanceof IHasPsiDirectory) {
            PsiDirectory psiParent = ((IHasPsiDirectory) parentAction).getPsiDirectory();
            String path = psiParent.getVirtualFile().getPath();

            // Custom Path
            if (file.getCustomPath() != null) {
                path = getCustomPath(file, path);

                if (path == null) {
                    isDone = false;
                    return false;
                }
            }

            //Write Rules
            java.io.File fileDuplicate = new java.io.File(path + java.io.File.separator
                    + file.getName() + Const.FILE_EXTENSION_SEPARATOR + file.getExtension());
            if (fileDuplicate.exists()) {
                WriteRules rules = file.getWriteRules();
                if (rules == WriteRules.FROM_PARENT) {
                    rules = geWriteRulesFromParent(parentAction);
                }

                switch (rules) {
                    default:
                    case ASK_ME:
                        if (!onAsk(fileDuplicate)) {
                            return false;
                        }
                        break;
                    case OVERWRITE:
                        if (!onOverwrite(fileDuplicate)) {
                            return false;
                        }
                        break;
                    case USE_EXISTING:
                        if (!onUseExisting(fileDuplicate)) {
                            return false;
                        }
                        break;
                }
            } else {
                // No duplicate, create
                psiElementResult = FileWriter.createFileFromTemplate(project, template, file.getName(), properties, path);
            }
        }

        if (psiElementResult == null) {
            isDone = false;
            return false;
        }

        return super.run();
    }

    private boolean onAsk(java.io.File fileDuplicate) {
        int dialogAnswerCode = Messages.showYesNoDialog(project,
                String.format(Localizer.get("warning.ArgAlreadyExists"), file.getName() + Const.FILE_EXTENSION_SEPARATOR + file.getExtension()),
                Localizer.get("title.WriteConflict"),
                Localizer.get("action.Overwrite"),
                Localizer.get("action.Skip"),
                Messages.getQuestionIcon(),
                new NeverShowAskCheckBox()
        );
        if (dialogAnswerCode == Messages.OK) {
            if (!onOverwrite(fileDuplicate)) {
                return false;
            }
        } else {
            if (!onUseExisting(fileDuplicate)) {
                return false;
            }
        }
        return true;
    }

    private boolean onOverwrite(java.io.File fileDuplicate) {
        //Remove
        PsiFile psiDuplicate = PsiHelper.findPsiFileByPath(project, fileDuplicate.getPath());
        if (psiDuplicate != null) {
            try {
                psiDuplicate.delete();
            } catch (IncorrectOperationException e) {
                Logger.log("CreateFileFromTemplateAction " + e.getMessage());
                Logger.printStack(e);
                isDone = false;
                return false;
            }
        }
        // Create
        psiElementResult = FileWriter.createFileFromTemplate(project, template, file.getName(), properties, fileDuplicate.getParentFile().getPath());
        return true;
    }

    private boolean onUseExisting(java.io.File fileDuplicate) {
        psiElementResult = PsiHelper.findPsiFileByPath(project, fileDuplicate.getPath());
        return true;
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public WriteRules getWriteRules() {
        return file.getWriteRules();
    }

    @Nullable
    private String getCustomPath(BaseElement element, String pathFrom) {
        ArrayList<SearchAction> actions = element.getCustomPath().getListSearchAction();

        java.io.File searchResultFile = SearchEngine.find(new java.io.File(pathFrom), actions);

        if (searchResultFile == null) {
            //print name of last action
            Logger.log("getCustomPath File Not Found: " + (actions.isEmpty() ? "" : actions.get(actions.size() - 1).getName()));
            return null;
        }

        return searchResultFile.getPath();
    }

}
