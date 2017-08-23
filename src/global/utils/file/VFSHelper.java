package global.utils.file;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import global.utils.Logger;

/**
 * Created by Arsen on 06.02.2017.
 */
public class VFSHelper {

    public static void refreshVirtualFile(String path) {
        VirtualFile virtualFile = findVirtualFileByPath(path);
        if (virtualFile == null) {
            Logger.log("refreshVirtualFile virtualFile is NULL");
            return;
        }

        virtualFile.refresh(true,true);
    }

    public static VirtualFile findVirtualFileByPath(String path) {
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    public static PsiDirectory findPsiDirByPath(Project project, String path) {
        VirtualFile virtualFile = findVirtualFileByPath(path);
        if (virtualFile == null) {
            Logger.log("findPsiDirByPath virtualFile is NULL");
            return null;
        }

        PsiDirectory psiParentDir = PsiManager.getInstance(project).findDirectory(virtualFile);
        if (psiParentDir == null) {
            Logger.log("findPsiDirByPath psiDirectory is NULL");
            return null;
        }

        return psiParentDir;
    }

    public static PsiFile findPsiFileByPath(Project project, String path) {
        VirtualFile virtualFile = findVirtualFileByPath(path);
        if (virtualFile == null) {
            Logger.log("findPsiFileByPath virtualFile is NULL");
            return null;
        }

        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile == null) {
            Logger.log("findPsiFileByPath psiFile is NULL");
            return null;
        }

        return psiFile;
    }

}
