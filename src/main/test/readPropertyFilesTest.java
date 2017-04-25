import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static instance.matching.version2.utility.ParamDef.CONF_FILE_PATH;

/**
 * Created by ciferlv on 17-4-25.
 */
public class readPropertyFilesTest {

    private static Logger logger = LoggerFactory.getLogger(readPropertyFilesTest.class);

    public static void main(String[] args) throws IOException {

        Properties pro = new Properties();
        FileInputStream in = new FileInputStream(CONF_FILE_PATH[0]);
        pro.load(in);
        in.close();

        logger.info(String.valueOf(pro.get("inst1_path")));

    }
}
