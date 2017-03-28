package instance.matching.helper;

/**
 * Created by ciferlv on 17-3-5.
 */
public class TrimStringHelper {

    public static String trimString(String str) {

        String result = str.replaceAll("\\s*", "")
                .replace("-", "")
                .replace("/", "")
                .replace("(","")
                .replace(")","")
                .toLowerCase();
        return result;
    }

}
