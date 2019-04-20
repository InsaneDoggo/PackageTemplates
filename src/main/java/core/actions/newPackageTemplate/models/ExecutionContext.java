package core.actions.newPackageTemplate.models;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by Arsen on 16.03.2017.
 */
public class ExecutionContext {

    public VirtualFile virtualFile;
    public Project project;
    public String ctxDirPath;
    public String ctxFullPath;

}
