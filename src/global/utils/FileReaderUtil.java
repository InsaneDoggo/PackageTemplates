package global.utils;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by Arsen on 04.09.2016.
 */
public class FileReaderUtil {

    public static String readFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } catch (Exception e){
            Logger.log("Error read file " + e.getMessage());
            return null;
        }
    }

}
