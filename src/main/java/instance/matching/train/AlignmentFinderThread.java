package instance.matching.train;

import instance.matching.unit.Alignment;
import instance.matching.unit.CounterPart;
import instance.matching.unit.Instance;
import instance.matching.unit.PropPairList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

import static instance.matching.unit.Disjoint.isDisjoint;
import static instance.matching.utility.ParamDef.ALIGN_THRESHOLD;
import static instance.matching.utility.ParamDef.prop_pair_num_need_threshold;
import static instance.matching.utility.ParamDef.*;

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

        if (isDisjoint(inst1, inst2)) {
            return;
        }

        Map<Double, Integer> result = inst1.calSimToInst(inst2, propPairList);

        Iterator iter = result.entrySet().iterator();

        Map.Entry entry = (Map.Entry) iter.next();

        double simi = ((Double) entry.getKey()).doubleValue();
        int cntMatched = ((Integer) entry.getValue()).intValue();

        if (!use_average_simi) {

            if (cntMatched > prop_pair_num_need_threshold) {

                alignment.addCounterPart(new CounterPart(inst1.getSub(), inst2.getSub()));
            }
        } else {

            if (simi > ALIGN_THRESHOLD) {

                alignment.addCounterPart(new CounterPart(inst1.getSub(), inst2.getSub()));
            }
        }
    }
}
