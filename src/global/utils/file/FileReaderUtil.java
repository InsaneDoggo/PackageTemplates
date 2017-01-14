package global.utils.file;

import com.google.common.base.Charsets;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import global.Const;
import global.utils.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Arsen on 04.09.2016.
 */
public class FileReaderUtil {

    public static String readFile(String path) {
        return readFile(new File(path));
    }

    public static String readFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
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
            return null;
        }
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
