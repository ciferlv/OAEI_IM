package instance.matching.version2.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xinzelv on 17-3-28.
 */
public class StopWords {

    private static Logger logger = LoggerFactory.getLogger(StopWords.class);

    private static String filePath = "src/main/resources/config/stopwords.txt";

    private static Set<String> stopWordSet = new HashSet<String>();

    public static void getStopWords() {

        try {
            FileReader fr = new FileReader(filePath);
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

}
