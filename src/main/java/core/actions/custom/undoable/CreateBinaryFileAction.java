package core.actions.custom.undoable;

import com.intellij.openapi.project.Project;
import core.actions.custom.element.CreateElementBaseAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import global.models.BinaryFile;
import global.utils.BinaryFileHelper;
import global.utils.i18n.Localizer;

import java.io.File;

public class CreateBinaryFileAction extends CreateElementBaseAction<BinaryFile, java.io.File> {

    private File binaryFile;

    public CreateBinaryFileAction(BinaryFile file, Project project) {
        super(project, file);
    }

    @Override
    public void doRun() {
        //preInit
        binaryFile = BinaryFileHelper.getBinaryFile(element.getAlias());
        if (binaryFile == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, String.format(Localizer.get("error.BinaryFileNotFoundWithArg"),
                    element.getAlias().getKey()), "Custom Path result == null"));
            return;
        }

        super.doRun();
    }

    @Override
    protected void createElement(String path) {
        //todo Custom name for binary!
        File fileTo = new File(path + File.separator + binaryFile.getName());
        new CopyFileAction(project, binaryFile, fileTo).run();
        fileResult = fileTo;
    }

    @Override
    protected java.io.File getDuplicateFile(String path) {
        return new java.io.File(path + java.io.File.separator + binaryFile.getName());
    }

    @Override
    protected String elementToString() {
        return String.format("%s",
                binaryFile.getName()
        );
    }

    @Override
    protected java.io.File findExistingResultFile(java.io.File fileDuplicate) {
        return fileDuplicate;
    }

    @Override
    protected boolean removeExistingElement(java.io.File fileDuplicate) {
        new TestDeleteFileAction(project, fileDuplicate).run();
        return true;
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public String toString() {
        return String.format("Create BinaryFile:    %s",
                element.getName()
        );
    }

}
