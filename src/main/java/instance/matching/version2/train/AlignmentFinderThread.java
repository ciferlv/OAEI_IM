package instance.matching.version2.train;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.Instance;
import instance.matching.version2.unit.PropPairList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

import static instance.matching.version2.utility.ParamDef.ALIGN_THRESHOLD;
import static instance.matching.version2.utility.ParamDef.PROP_PAIR_NUM_NEED_THRESHOLD;
import static instance.matching.version2.utility.ParamDef.*;

/**
 * Created by xinzelv on 17-3-29.
 */
public class AlignmentFinderThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(AlignmentFinderThread.class);

    private Instance inst1 = null;
    private Instance inst2 = null;
    private PropPairList propPairList = null;

    private Alignment alignment = null;

    public AlignmentFinderThread(Instance inst1,
                                 Instance inst2,
                                 PropPairList ppl,
                                 Alignment align) {

        this.inst1 = inst1;
        this.inst2 = inst2;
        this.propPairList = ppl;
        this.alignment = align;
    }

    public void run() {

        Map<Double, Integer> result = inst1.calSimToInst(inst2, propPairList);

        Iterator iter = result.entrySet().iterator();

        Map.Entry entry = (Map.Entry) iter.next();

        double simi = ((Double) entry.getKey()).doubleValue();
        int cntMatched = ((Integer) entry.getValue()).intValue();

        if (!use_average_simi) {

            if (cntMatched > PROP_PAIR_NUM_NEED_THRESHOLD) {

                alignment.addCounterPart(new CounterPart(inst1.getSub(), inst2.getSub()));
            }
        } else {

            if (simi > ALIGN_THRESHOLD) {

                alignment.addCounterPart(new CounterPart(inst1.getSub(), inst2.getSub()));
            }
        }
    }
}
