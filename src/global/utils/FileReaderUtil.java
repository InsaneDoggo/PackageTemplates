package global.utils;

import com.google.common.base.Charsets;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import global.Const;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Arsen on 04.09.2016.
 */
public class FileReaderUtil {

    public static String readFile(String path) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), Charsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
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

    public static FileChooserDescriptor getDirectoryDescriptor() {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        return descriptor;
    }

}
