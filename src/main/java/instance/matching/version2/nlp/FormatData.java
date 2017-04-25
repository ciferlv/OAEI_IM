package instance.matching.version2.nlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import static instance.matching.version2.utility.ParamDef.stopwords_file_path;
import static instance.matching.version2.utility.ParamDef.stopWordSet;

/**
 * Created by xinzelv on 17-3-28.
 */
public class FormatData {

    private static Logger logger = LoggerFactory.getLogger(FormatData.class);


    public static void getStopWords() {

        try {
            FileReader fr = new FileReader(stopwords_file_path);
            BufferedReader br = new BufferedReader(fr);
            String s = br.readLine();
            while (s != null) {
                stopWordSet.add(s.trim());
                s = br.readLine();
            }
            fr.close();
            br.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    public static String formatWords(String str) {

        str = str.toLowerCase();

        String delimit = "~!@#$%^&*()_+|`-=\\{}[]:\";'<>?,./'";

        StringBuffer buffer = new StringBuffer();

        for (char ch : delimit.toCharArray()) {

            str = str.replace(ch, ' ');
        }

        String[] strArray = str.split("\\s+");

        for (String tempStr : strArray) {

            if (!stopWordSet.contains(tempStr)) {

                buffer.append(tempStr);
            }
        }

        return String.valueOf(buffer);
    }

    public static String formatDateTime(String date) {


        return "";
    }

}
