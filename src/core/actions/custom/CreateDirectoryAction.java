package core.actions.custom;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiDirectory;
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
import global.models.Directory;
import global.utils.Logger;
import global.utils.file.PsiHelper;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateDirectoryAction extends SimpleAction implements IHasPsiDirectory, IHasWriteRules {

    private Directory directory;
    private Project project;

    //result
    private PsiDirectory psiDirectoryResult;

    public CreateDirectoryAction(Directory directory, Project project) {
        this.directory = directory;
        this.project = project;
    }

    @Override
    public void doRun() {
        psiDirectoryResult = null;

        if (parentAction instanceof IHasPsiDirectory) {
            PsiDirectory psiParent = ((IHasPsiDirectory) parentAction).getPsiDirectory();

            // Custom Path
            if (directory.getCustomPath() != null) {
                psiParent = getPsiDirectoryFromCustomPath(directory, psiParent.getVirtualFile().getPath());

                if (psiParent == null) {
                    ReportHelper.setState(ExecutionState.FAILED);
                    ReportHelper.addReport(new FailedActionReport(this, Localizer.get("error.CustomPathNotFound"), "Custom Path result == null"));
                    return;
                }
            }

            // check existence
            psiDirectoryResult = psiParent.findSubdirectory(directory.getName());
            if (psiDirectoryResult == null) {
                // create new one
                createSubDir(psiParent, directory.getName());
            } else {
                // WRITE CONFLICT
                WriteRules rules = directory.getWriteRules();
                if (rules == WriteRules.FROM_PARENT) {
                    rules = geWriteRulesFromParent(parentAction);
                }

                switch (rules) {
                    default:
                    case ASK_ME:
                        if (!onAsk(psiDirectoryResult)) {
                            ReportHelper.setState(ExecutionState.FAILED);
                            return;
                        }
                        break;
                    case OVERWRITE:
                        if (!onOverwrite(psiDirectoryResult)) {
                            ReportHelper.setState(ExecutionState.FAILED);
                            return;
                        }
                        break;
                    case USE_EXISTING:
                        if (!onUseExisting(psiDirectoryResult)) {
                            ReportHelper.setState(ExecutionState.FAILED);
                            return;
                        }
                        break;
                }
            }
        }

        if (psiDirectoryResult == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }

        ReportHelper.addReport(new SuccessActionReport(this, toString()));
    }

    private boolean onAsk(PsiDirectory psiDuplicate) {
        int dialogAnswerCode = Messages.showYesNoDialog(project,
                String.format(Localizer.get("warning.ArgDirectoryAlreadyExists"), psiDuplicate.getName()),
                Localizer.get("title.WriteConflict"),
                Localizer.get("action.Overwrite"),
                Localizer.get("action.UseExisting"),
                Messages.getQuestionIcon(),
                new NeverShowAskCheckBox()
        );
        if (dialogAnswerCode == Messages.OK) {
            if (!onOverwrite(psiDuplicate)) {
                return false;
            }
        } else {
            if (!onUseExisting(psiDuplicate)) {
                return false;
            }
        }
        return true;
    }

    private boolean onOverwrite(PsiDirectory psiDuplicate) {
        PsiDirectory psiParent = psiDuplicate.getParent();
        String name = psiDuplicate.getName();

        try {
            //Remove
            psiDuplicate.delete();
            // Create
            createSubDir(psiParent, name);
            return true;
        } catch (Exception e) {
            Logger.log("CreateDirectoryAction " + e.getMessage());
            Logger.printStack(e);
            ReportHelper.addReport(new FailedActionReport(this, e.getMessage()));
            return false;
        }
    }

    private boolean onUseExisting(PsiDirectory psiDuplicate) {
        psiDirectoryResult = psiDuplicate;
        return true;
    }

    private void createSubDir(PsiDirectory psiParent, String name) {
        ApplicationManager.getApplication().invokeAndWait(() -> {
                    Runnable runnable = () ->
                            psiDirectoryResult = ApplicationManager.getApplication().runWriteAction(
                                    (Computable<PsiDirectory>) () -> psiParent.createSubdirectory(name));

                    CommandProcessor.getInstance().executeCommand(project, runnable, "testId", "testId");
                }
        );
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public PsiDirectory getPsiDirectory() {
        return psiDirectoryResult;
    }

    @Override
    public WriteRules getWriteRules() {
        return directory.getWriteRules();
    }

    @Nullable
    private PsiDirectory getPsiDirectoryFromCustomPath(BaseElement element, String pathFrom) {
        ArrayList<SearchAction> actions = element.getCustomPath().getListSearchAction();

        java.io.File searchResultFile = SearchEngine.find(new java.io.File(pathFrom), actions);

        if (searchResultFile == null) {
            //print name of last action
            Logger.log("getCustomPath File Not Found: " + (actions.isEmpty() ? "" : actions.get(actions.size() - 1).getName()));
            return null;
        }

        return PsiHelper.findPsiDirByPath(project, searchResultFile.getPath());
    }

    @Override
    public String toString() {
        return String.format("Create Directory: %s",
                directory.getName()
        );
    }

}
