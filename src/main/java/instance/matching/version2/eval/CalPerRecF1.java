package instance.matching.version2.eval;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ciferlv on 17-3-30.
 */
public class CalPerRecF1 {

    private Logger logger = LoggerFactory.getLogger(CalPerRecF1.class);
    private Alignment refAlign, resultAlign;
    private double precision, recall, f1_score;

    private Alignment correctAlign = new Alignment();
    private Alignment wrongAlign = new Alignment();
    private Alignment unfoundAlign = new Alignment();

    public CalPerRecF1(Alignment refAlign, Alignment resultAlign) {

        this.refAlign = refAlign;
        this.resultAlign = resultAlign;
        proceed();
    }

    public void proceed() {

        int correctNum = 0, wrongNum = 0;
        int refSize, resultSize;
        List<CounterPart> refCPL = refAlign.getCounterPartList();
        List<CounterPart> resultCPL = resultAlign.getCounterPartList();

        refSize = refAlign.size();
        resultSize = resultAlign.size();

        for (CounterPart cp : resultCPL) {
            if (refCPL.contains(cp)) {

                correctNum++;
                correctAlign.addCounterPart(cp);
            } else {
                wrongNum++;
                wrongAlign.addCounterPart(cp);
            }
        }

        precision = 1.0 * correctNum / resultSize;
        recall = 1.0 * correctNum / refSize;
        f1_score = (precision + recall) * 0.5;

        logger.info("precision: " + precision);
        logger.info("recall: " + recall);
        logger.info("f1_score: " + f1_score);
    }


}
