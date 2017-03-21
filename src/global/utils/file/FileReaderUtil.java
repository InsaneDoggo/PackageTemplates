package global.utils.file;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiDirectory;
import global.Const;
import global.utils.Logger;

import java.io.*;

/**
 * Created by Arsen on 04.09.2016.
 */
public class FileReaderUtil {

    public static String readFile(String path) {
        return readFile(new File(path));
    }

    public static String readFile(File file) {
        final String[] result = new String[1];
        ApplicationManager.getApplication().invokeAndWait(() ->
                        result[0] = ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
                            try {
                                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Const.charsets.UTF_8));
                                StringBuilder sb = new StringBuilder();
                                String line = br.readLine();

                                while (line != null) {
                                    sb.append(line);
                                    sb.append("\n");
                                    line = br.readLine();
                                }

                                br.close();
                                return sb.toString();
                            } catch (Exception e) {
                                Logger.log("Error read file " + e.getMessage());
                                Logger.printStack(e);
                                return null;
                            }
                        })
                , ModalityState.defaultModalityState());
        return result[0];
    }

    public static FileChooserDescriptor getPackageTemplatesDescriptor() {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        descriptor.withFileFilter(file -> {
            if (file.getExtension() == null) {
                return false;
            }

            return file.getExtension().toLowerCase().equals(Const.PACKAGE_TEMPLATES_EXTENSION);
        });
        return descriptor;
    }

    public static FileChooserDescriptor getPackageTemplatesDescriptorForImport() {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, true);
        descriptor.withFileFilter(file -> {
            if (file.getExtension() == null) {
                return false;
            }

            return file.getExtension().toLowerCase().equals(Const.PACKAGE_TEMPLATES_EXTENSION);
        });
        return descriptor;
    }

    public static FileChooserDescriptor getDirectoryDescriptor() {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        return descriptor;
    }

}
