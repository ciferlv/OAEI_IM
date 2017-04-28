import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ciferlv on 17-4-25.
 */
public class readPropertyFilesTest {

    private static Logger logger = LoggerFactory.getLogger(readPropertyFilesTest.class);

    private static void swap(String s1, String s2) {

        String tmp = s1;
        s1 = s2;
        s2 = tmp;
    }

    public static void main(String[] args) throws IOException {

//        Properties pro = new Properties();
//        FileInputStream in = new FileInputStream(CONF_FILE_PATH[0]);
//        pro.load(in);
//        in.close();
//
//        logger.info(String.valueOf(pro.get("inst1_path")));

        String a = "a";
        String b = "b";
        String tmp = a;
        a = b;
        b = tmp;
//        swap(a,b);
        System.out.println("a:" + a);
        System.out.println("b:" + b);
    }
}
