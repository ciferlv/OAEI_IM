package instance.matching.nlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xinzelv on 17-3-31.
 */
public class I_SUB {

    private static Logger logger = LoggerFactory.getLogger(I_SUB.class);


    public static double I_SUBScore(String st1, String st2) {

        if (st1 == null || st2 == null || st1.length() == 0 || st2.length() == 0)
            return -1;

        String s1 = st1;
        String s2 = st2;

        int l1 = s1.length(), l2 = s2.length();
        int L1 = l1, L2 = l2;

        double common = 0;
        int best = 2;

        while (s1.length() > 0 && s2.length() > 0 && best != 0) {

            best = 0;
            l1 = s1.length();
            l2 = s2.length();
            int i = 0, j = 0;
            int startS1 = 0, endS1 = 0;
            int startS2 = 0, endS2 = 0;
            int p = 0;
            for (i = 0; (i < l1) && (l1 - i > best); i++) {
                j = 0;
                while (l2 - j > best) {
                    int k = i;
                    for (; (j < l2) && (s1.charAt(k) != s2.charAt(j)); j++) {
                    }
                    if (j != l2) {
                        p = j;
                        for (j++, k++; (j < l2) && (k < l1) &&
                                (s1.charAt(k) == s2.charAt(j)); j++, k++) {
                        }
                        if (k - i > best) {
                            best = k - i;
                            startS1 = i;
                            endS1 = k;
                            startS2 = p;
                            endS2 = j;
                        }
                    }
                }
            }

            char[] newString = new char[s1.length() - (endS1 - startS1)];
            j = 0;
            for (i = 0; i < s1.length(); i++) {
                if (i >= startS1 && i < endS1)
                    continue;

                newString[j++] = s1.charAt(i);
            }
            s1 = new String(newString);
            newString = new char[s2.length() - (endS2 - startS2)];
            j = 0;
            for (i = 0; i < s2.length(); i++) {
                if (i >= startS2 && i < endS2)
                    continue;

                newString[j++] = s2.charAt(i);
            }
            s2 = new String(newString);
            if (best > 2) {
                common += best;
            } else best = 0;
        }

        double commonality = 0;
        double scaledCommon = (2.0 * common) / (L1 + L2);
        commonality = scaledCommon;
        double winklerImprovement = winklerImprovement(st1, st2, commonality);

        double dissimilarity = 0;

        double rest1 = L1 - common;
        double rest2 = L2 - common;
        double unmatchedS1 = Math.max(rest1, 0);
        double unmatchedS2 = Math.max(rest2, 0);

        unmatchedS1 = rest1 / L1;
        unmatchedS2 = rest2 / L2;

        double suma = unmatchedS1 + unmatchedS2;
        double product = unmatchedS1 * unmatchedS2;
        double p = 0.6;

        if ((suma - product) == 0) {
            dissimilarity = 0;
        } else dissimilarity = (product) / (p + (1 - p) * (suma - product));

        return commonality - dissimilarity + winklerImprovement;
    }

    private static double winklerImprovement(String s1, String s2, double commonality) {

        int i, n = Math.min(s1.length(), s2.length());
        for (i = 0; i < n; i++) {
            if (s1.charAt(i) != s2.charAt(i))
                break;
        }
        double commonPrefixLength = Math.min(4, i);
        double winkler = commonPrefixLength * 0.1 * (1 - commonality);
        return winkler;
    }
}
