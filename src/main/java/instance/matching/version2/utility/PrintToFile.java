package instance.matching.version2.utility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by xinzelv on 17-4-2.
 */
public class PrintToFile {

    public static void printToFile(String filePath,String str) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter( new FileOutputStream(filePath));

        pw.append(str);
        pw.flush();
        pw.close();

    }
}
