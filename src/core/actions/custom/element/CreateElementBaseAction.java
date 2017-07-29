package core.actions.custom.element;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import core.actions.custom.interfaces.IHasWriteRules;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import core.report.models.SuccessActionReport;
import core.search.SearchAction;
import core.search.SearchEngine;
import core.writeRules.WriteRules;
import global.dialogs.impl.NeverShowAskCheckBox;
import global.models.BaseElement;
import global.utils.Logger;
import global.utils.file.PsiHelper;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by Arsen on 29.07.2017.
 */
public abstract class CreateElementBaseAction extends SimpleAction implements IHasWriteRules {

    private Project project;
    private BaseElement element;
    private Object fileResult;

    public CreateElementBaseAction(Project project, BaseElement element) {
        this.project = project;
        this.element = element;
    }

    @Override
    public void doRun() {
        fileResult = null;

        if (parentAction instanceof IHasPsiDirectory) {
            PsiDirectory psiParent = ((IHasPsiDirectory) parentAction).getPsiDirectory();
            String parentPath = psiParent.getVirtualFile().getPath();

            // Custom Path
            if (element.getCustomPath() != null) {
                parentPath = getCustomPath(element, parentPath);

                if (parentPath == null) {
                    ReportHelper.setState(ExecutionState.FAILED);
                    ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.CustomPathNotFound"), "Custom Path result == null"));
                    return;
                }
            }

            //Write Rules
            java.io.File fileDuplicate = getDuplicateFile(parentPath);

            // Duplicate
            if (fileDuplicate.exists()) {
                WriteRules rules = element.getWriteRules();
                if (rules == WriteRules.FROM_PARENT) {
                    rules = geWriteRulesFromParent(parentAction);
                }

                switch (rules) {
                    default:
                    case ASK_ME:
                        if (!onAsk(fileDuplicate)) {
                            ReportHelper.setState(ExecutionState.FAILED);
                            return;
                        }
                        break;
                    case OVERWRITE:
                        if (!onOverwrite(fileDuplicate)) {
                            ReportHelper.setState(ExecutionState.FAILED);
                            return;
                        }
                        break;
                    case USE_EXISTING:
                        if (!onUseExisting(fileDuplicate)) {
                            ReportHelper.setState(ExecutionState.FAILED);
                            return;
                        }
                        break;
                }
            } else {
                // No duplicate, create
                createElement(parentPath);
            }
        }

        if (fileResult == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }

        ReportHelper.putReport(new SuccessActionReport(this, toString()));
    }

    private boolean onAsk(java.io.File fileDuplicate) {
        int dialogAnswerCode = Messages.showYesNoDialog(project,
                String.format(Localizer.get("warning.ArgAlreadyExists"), elementToString()),
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
                ReportHelper.putReport(new FailedActionReport(this, e.getMessage()));
                return false;
            }
        }
        // Create
        createElement(fileDuplicate.getParentFile().getPath());
        return true;
    }

    private boolean onUseExisting(java.io.File fileDuplicate) {
        fileResult = PsiHelper.findPsiFileByPath(project, fileDuplicate.getPath());
        return true;
    }

    //=================================================================
    //  Abstraction
    //=================================================================
    protected abstract void createElement(String path);
    protected abstract java.io.File getDuplicateFile(String path);
    protected abstract String elementToString();

    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public WriteRules getWriteRules() {
        return element.getWriteRules();
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
