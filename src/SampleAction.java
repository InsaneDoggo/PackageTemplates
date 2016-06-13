import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;

import java.util.HashMap;

/**
 * Created by Arsen on 13.06.2016.
 */
public class SampleAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        FileTemplateManager fileTemplateManager = FileTemplateManager.getDefaultInstance();
        FileTemplate[] templates = fileTemplateManager.getAllTemplates();

        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file != null && file.isDirectory()) {
            PsiDirectory dir = PsiManager.getInstance(e.getProject()).findDirectory(e.getData(CommonDataKeys.VIRTUAL_FILE));

            HashMap<String, String> map = new HashMap<>();
            map.put("myVar", "\"HelloWorld\"");

            PsiClass psiClass = JavaDirectoryService.getInstance().createClass(dir, "MegaFile", "Prost", false, map);
            System.out.println("end");
        }

        System.out.println("end");
    }
}
