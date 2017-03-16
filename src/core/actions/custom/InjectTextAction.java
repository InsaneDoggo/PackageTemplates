package core.actions.custom;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import core.search.SearchAction;
import core.search.SearchEngine;
import core.search.customPath.CustomPath;
import core.textInjection.InjectDirection;
import core.textInjection.TextInjection;
import global.Const;
import global.models.BaseElement;
import global.utils.Logger;
import global.utils.file.PsiHelper;
import global.utils.i18n.Localizer;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 09.01.2017.
 */
public class InjectTextAction extends SimpleAction {

    private Project project;
    private TextInjection textInjection;
    private HashMap<String, String> mapVariables;


    @Override
    public void doRun() {
        // Custom Path
        String path = getCustomPath(mapVariables.get(Const.Key.CTX_DIR_PATH), textInjection.getCustomPath());

        if (path == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.CustomPathNotFound"), "InjectTextAction Custom Path result == null"));
            return;
        }

        try {
            PsiFile psiFile = PsiHelper.findPsiFileByPath(project, path);
            if(psiFile == null){
                ReportHelper.setState(ExecutionState.FAILED);
                ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.CustomPathNotFound"), "InjectTextAction findPsiFileByPath result == null"));
                return;
            }

            injectText(psiFile);
        } catch (IncorrectOperationException ex) {
            Logger.logAndPrintStack("InjectTextAction " + ex.getMessage(), ex);
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

    private void injectText(PsiFile psiFile) {
        ReadonlyStatusHandler.OperationStatus status = ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(psiFile.getVirtualFile());
        if (status.hasReadonlyFiles()) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.ReadOnlyFile"), "InjectTextAction ReadOnlyFile"));
            Logger.log("InjectTextAction ReadOnlyFile");
            return;
        }

        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);

        int offset = getOffset(document, textInjection.getInjectDirection());
        int lineNumber = document.getLineNumber(offset);
        int lineEndOffset = document.getLineEndOffset(lineNumber);

//        String textToInsert = fromVelocity("String ${DDD};");
//        document.insertString(lineEndOffset, textToInsert);
    }

    private int getOffset(Document document, InjectDirection injectDirection) {
        switch (injectDirection){
            case BEFORE:
                //todo BEFORE
                break;
            case AFTER:
                //todo AFTER
                break;
            case REPLACE:
                //todo REPLACE
                break;
            case START_OF_FILE:
                return 0;
            case END_OF_FILE:
                return document.getText().length()-1;
        }

        throw new RuntimeException("Unknown InjectDirection");
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Nullable
    private String getCustomPath(String pathFrom, CustomPath customPath) {
        ArrayList<SearchAction> actions = customPath.getListSearchAction();

        java.io.File searchResultFile = SearchEngine.find(new java.io.File(pathFrom), actions);

        if (searchResultFile == null) {
            //print name of last action
            Logger.log("getCustomPath File Not Found: " + (actions.isEmpty() ? "" : actions.get(actions.size() - 1).getName()));
            return null;
        }

        return searchResultFile.getPath();
    }

}
