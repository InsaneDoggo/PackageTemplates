package core.actions.custom.appCode;

import com.intellij.psi.PsiDirectory;
import com.jetbrains.cidr.actions.newFile.XcodeCreateFileDialog;
import com.jetbrains.cidr.lang.actions.newFile.OCNewFileActionBase;
import com.jetbrains.cidr.xcode.model.PBXGroup;
import com.jetbrains.cidr.xcode.model.PBXProjectFile;

/**
 * Created by Arsen on 18.02.2017.
 */
public class CreateFilesDialog extends XcodeCreateFileDialog {

    public CreateFilesDialog(PBXProjectFile pbxProjectFile, PsiDirectory psiDirectory, PBXGroup pbxGroup, OCNewFileActionBase.CreateFileDialogBase createFileDialogBase) {
        super(pbxProjectFile, psiDirectory, pbxGroup, createFileDialogBase);
    }

}
