package instance.matching.version2.nlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xinzelv on 17-4-2.
 */
public class Jaccard {

    static Logger logger = LoggerFactory.getLogger(Jaccard.class);

    public static double jaccardSimi(String s1, String s2)
    {
        String[] ss1 = s1.split(" "), ss2 = s2.split(" ");

        Set<String> union = new HashSet<String>();
        Set<String> set1 = new HashSet<String>(), set2 = new HashSet<String>();
        for (int i = 0; i < ss1.length; ++i) {
            union.add(ss1[i]);
            set1.add(ss1[i]);
        }
        for (int i = 0; i < ss2.length; ++i) {
            union.add(ss2[i]);
            set2.add(ss2[i]);
        }

        return (set1.size() + set2.size() - union.size()) / (double) union.size();
    }
}
