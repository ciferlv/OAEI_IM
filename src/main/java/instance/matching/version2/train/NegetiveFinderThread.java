package instance.matching.version2.train;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.Document;
import org.apache.jena.rdfxml.xmlinput.ALiteral;

import java.util.List;
import java.util.Random;

/**
 * Created by xinzelv on 17-4-5.
 */
public class NegetiveFinderThread implements Runnable {

    private CounterPart cp = null;

    private Document doc = null;

    private Alignment positives = null;

    private Alignment negetives = null;

    public NegetiveFinderThread(CounterPart cp,
                                Document doc,
                                Alignment positives,
                                Alignment negetives) {
        this.cp = cp;
        this.doc = doc;
        this.positives = positives;
        this.negetives = negetives;
    }

    public void run() {

        List<String> subList = doc.getTargetSubject();

        Random r = new Random();

        String sub1 = cp.getSubject1();

        boolean flag = false;
        do {
            int index = r.nextInt(subList.size());
            String tarSub = subList.get(index);
            if (!positives.contains(new CounterPart(sub1, tarSub))) {
                flag = true;
                negetives.addCounterPart(new CounterPart(sub1, tarSub));
            }
        } while (!flag);
    }
}
