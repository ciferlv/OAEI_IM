package instance.matching.version2.train;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.PredPairList;
import instance.matching.version2.unit.Triples;

/**
 * Created by xinzelv on 17-3-29.
 */
public class AlignmentFinderThread implements Runnable{

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

        if( tri1.calSimToTri(tri2,predPairList) > 0.65)
        {
            alignment.addCounterPart(new CounterPart(tri1.getSubject(),tri2.getSubject()));
        }
    }
}
