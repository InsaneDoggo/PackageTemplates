package core.actions.custom;

import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import global.utils.Logger;
import global.utils.file.VFSHelper;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileAction extends SimpleAction {

    private File file;
    private String content;
    private Project project;

    public CreateFileAction(Project project, File file, String content) {
        this.file = file;
        this.content = content;
        this.project = project;
    }

    @Override
    public void doRun() {
        PsiDirectory psiParent = VFSHelper.findPsiDirByPath(project, file.getParentFile().getPath());
        if(psiParent==null){
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }

        try {
            PsiFile psiResultFile = PsiFileFactory.getInstance(project).createFileFromText(file.getName(), PlainTextFileType.INSTANCE, content);
//            PsiFile psiFile = psiDirectory.createFile(psiDirectory.createFile(file.getName()).getName());
//            psiFile.getVirtualFile().
            psiParent.add(psiResultFile);
        } catch (IncorrectOperationException ex){
            Logger.log("CreateFileAction " + ex.getMessage());
            Logger.printStack(ex);
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
