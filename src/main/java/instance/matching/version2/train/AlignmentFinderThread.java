package instance.matching.version2.train;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.PredPairList;
import instance.matching.version2.unit.Triples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

import static instance.matching.version2.utility.VariableDef.alignThreshold;
import static instance.matching.version2.utility.VariableDef.predPairNumNeedThreshold;
import static instance.matching.version2.utility.VariableDef.predPairSize;

/**
 * Created by xinzelv on 17-3-29.
 */
public class AlignmentFinderThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(AlignmentFinderThread.class);

    private Triples tri1 = null;
    private Triples tri2 = null;
    private PredPairList predPairList = null;

    private Alignment alignment = null;

    public AlignmentFinderThread(Triples tri1,
                                 Triples tri2,
                                 PredPairList ppl,
                                 Alignment align) {

        this.tri1 = tri1;
        this.tri2 = tri2;
        this.predPairList = ppl;
        this.alignment = align;
    }

    public void run() {

        Map<Double, Integer> result = tri1.calSimToTri(tri2, predPairList);

        Iterator iter = result.entrySet().iterator();

        Map.Entry entry = (Map.Entry) iter.next();

        double simi = ((Double) entry.getKey()).doubleValue();
        int cntMatched = ((Integer) entry.getValue()).intValue();

        if ( cntMatched> /*alignThreshold*/predPairNumNeedThreshold) {

//            logger.info(String.valueOf(value));
            alignment.addCounterPart(new CounterPart(tri1.getSubject(), tri2.getSubject()));
        }
    }
}
