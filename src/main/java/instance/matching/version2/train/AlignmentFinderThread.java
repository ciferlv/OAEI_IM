package instance.matching.version2.train;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.Instance;
import instance.matching.version2.unit.PropPairList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

import static instance.matching.version2.utility.VariableDef.ALIGN_THRESHOLD;

/**
 * Created by xinzelv on 17-3-29.
 */
public class AlignmentFinderThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(AlignmentFinderThread.class);

    private Instance tri1 = null;
    private Instance tri2 = null;
    private PropPairList propPairList = null;

    private Alignment alignment = null;

    public AlignmentFinderThread(Instance tri1,
                                 Instance tri2,
                                 PropPairList ppl,
                                 Alignment align) {

        this.tri1 = tri1;
        this.tri2 = tri2;
        this.propPairList = ppl;
        this.alignment = align;
    }

    public void run() {

        Map<Double, Integer> result = tri1.calSimToInst(tri2, propPairList);

        Iterator iter = result.entrySet().iterator();

        Map.Entry entry = (Map.Entry) iter.next();

        double simi = ((Double) entry.getKey()).doubleValue();
        int cntMatched = ((Integer) entry.getValue()).intValue();

//        if ( cntMatched> /*ALIGN_THRESHOLD*/PROP_PAIR_NUM_NEED_THRESHOLD) {

//            logger.info(String.valueOf(value));
//            alignment.addCounterPart(new CounterPart(tri1.getSub(), tri2.getSub()));
//        }
        if (simi > ALIGN_THRESHOLD) {

//            logger.info(String.valueOf(value));
            alignment.addCounterPart(new CounterPart(tri1.getSub(), tri2.getSub()));
        }
    }
}
