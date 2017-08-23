package core.actions.custom;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import core.report.models.SuccessActionReport;
import core.search.SearchAction;
import core.search.SearchEngine;
import core.search.customPath.CustomPath;
import core.textInjection.InjectDirection;
import core.textInjection.TextInjection;
import core.textInjection.VelocityHelper;
import global.Const;
import global.utils.Logger;
import core.regexp.RegexHelper;
import global.utils.text.StringTools;
import global.utils.file.VFSHelper;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.MatchResult;

/**
 * Created by Arsen on 09.01.2017.
 */
public class InjectTextAction extends SimpleAction {

    private Project project;
    private TextInjection textInjection;
    private HashMap<String, String> mapVariables;

    public InjectTextAction(Project project, TextInjection textInjection, HashMap<String, String> mapVariables) {
        this.project = project;
        this.textInjection = textInjection;
        this.mapVariables = mapVariables;
    }

    @Override
    public void doRun() {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            CommandProcessor.getInstance().executeCommand(project, () -> {
                ApplicationManager.getApplication().runWriteAction(() -> {
                    doRunInWriteThread();
                });
            }, getClass().getSimpleName(), getClass().getSimpleName());
        }, ModalityState.defaultModalityState());
    }

    private void doRunInWriteThread() {
        // Custom Path
        String path = getCustomPath(mapVariables.get(Const.Key.CTX_DIR_PATH), textInjection.getCustomPath());

        if (path == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.CustomPathNotFound"), "InjectTextAction Custom Path result == null"));
            return;
        }

        try {
            PsiFile psiFile = VFSHelper.findPsiFileByPath(project, path);
            if (psiFile == null) {
                ReportHelper.setState(ExecutionState.FAILED);
                ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.CustomPathNotFound"), "InjectTextAction findPsiFileByPath result == null"));
                return;
            }

            injectText(psiFile);
            ReportHelper.putReport(new SuccessActionReport(this, toString()));
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
        if (document == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.internalError"), "InjectTextAction document==nul"));
            Logger.log("InjectTextAction document==nul");
            return;
        }

        int offset = getOffset(document, textInjection.getInjectDirection());
        if (offset == -1) {
            return;
        }

        String textToInsert = fromVelocity(textInjection.getTextToInject());
        if (textToInsert == null) {
            return;
        }

        // Replace
        if (textInjection.getInjectDirection() == InjectDirection.REPLACE) {
            doReplace(document, textToInsert);
        }
        // Insert
        else {
            document.insertString(offset, textToInsert);
        }
    }

    private String fromVelocity(String velocityTemplate) {
        String fromTemplate = VelocityHelper.fromTemplate(velocityTemplate, mapVariables);
        if (fromTemplate == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.VelocityTemplateError"), "InjectTextAction VelocityTemplateError"));
            Logger.log("InjectTextAction VelocityTemplateError");
            return null;
        }
        return fromTemplate;
    }

    private int getOffset(Document document, InjectDirection injectDirection) {
        switch (injectDirection) {
            case BEFORE:
            case AFTER:
            case PREV_LINE:
            case NEXT_LINE:
            case REPLACE:
                return getDirectedOffset(document, injectDirection);
            case START_OF_FILE:
                return 0;
            case SOF_END_OF_LINE:
                return document.getLineEndOffset(0);
            case END_OF_FILE:
                return document.getText().length() - 1;
            case EOF_START_OF_LINE:
                return document.getLineStartOffset(document.getLineCount() - 1);
        }

        throw new RuntimeException("Unknown InjectDirection");
    }

    private int getDirectedOffset(Document document, InjectDirection injectDirection) {
        String text = document.getText();

        int matchCount;
        String logId;

        if (textInjection.isRegexp()) {
            matchCount = RegexHelper.matchCount(text, textInjection.getTextToSearch());
            logId = "Regex";
        } else {
            matchCount = StringTools.matchCount(text, textInjection.getTextToSearch());
            logId = "NonRegex";
        }

        // Too much Matches
        if (matchCount > 1) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.MoreThenOneMatch"), "InjectTextAction " + logId + " MoreThenOneMatch"));
            Logger.log("InjectTextAction " + logId + " MoreThenOneMatch");
            return -1;
        }
        // No Matches
        if (matchCount <= 0) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.NoMatches"), "InjectTextAction " + logId + " NoMatches"));
            Logger.log("InjectTextAction " + logId + " NoMatches");
            return -1;
        }

        MatchResult matchResult;
        if (textInjection.isRegexp()) {
            matchResult = RegexHelper.match(text, textInjection.getTextToSearch());
        } else {
            matchResult = StringTools.match(text, textInjection.getTextToSearch());
        }

        // No matchResult
        if (matchResult == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.NoMatches"), "InjectTextAction " + logId + " matchResult NoMatches"));
            Logger.log("InjectTextAction " + logId + " matchResult NoMatches");
        }

        if (injectDirection == InjectDirection.BEFORE) {
            return matchResult.start();
        }
        if (injectDirection == InjectDirection.AFTER) {
            return matchResult.end();
        }
        if (injectDirection == InjectDirection.PREV_LINE) {
            int lineNumber = document.getLineNumber(matchResult.end());
            return document.getLineEndOffset(getPrevLine(lineNumber));
        }
        if (injectDirection == InjectDirection.NEXT_LINE) {
            int lineNumber = document.getLineNumber(matchResult.end());
            return document.getLineStartOffset(getNextLine(lineNumber, document.getLineCount()));
        }
        if (injectDirection == InjectDirection.REPLACE) {
            setReplaceArgs(matchResult);
            return matchResult.start();
        }

        throw new RuntimeException("getDirectedOffset Unknown InjectDirection");
    }

    private int getNextLine(int lineNumber, int totalCount) {
        if (lineNumber >= totalCount - 1) {
            lineNumber = totalCount - 1;
        }

        return lineNumber + 1;
    }

    private int getPrevLine(int lineNumber) {
        if (lineNumber <= 0) {
            lineNumber = 0;
        }

        return lineNumber - 1;
    }


    //=================================================================
    //  Replace
    //=================================================================
    private MatchResult matchResultReplace;

    private void setReplaceArgs(MatchResult matchResult) {
        matchResultReplace = matchResult;
    }

    private void doReplace(Document document, String textToInsert) {
        document.replaceString(matchResultReplace.start(), matchResultReplace.end(), textToInsert);
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + textInjection.getDescription();
    }

}
