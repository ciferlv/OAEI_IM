package instance.matching.pr.unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPairList {

    private List<PredPair> predPairList = new ArrayList<PredPair>();


    public PredPair contains(PredPair pp) {

        Iterator<PredPair> iter = predPairList.iterator();

        while (iter.hasNext()) {

            PredPair tempPP = iter.next();
            if (tempPP.equals(pp)) {
                return tempPP;
            }
        }
        return null;
    }

    public void add(PredPair pp) {

        PredPair tempPP = contains(pp);
        if (tempPP == null) {
            predPairList.add(tempPP);
        } else tempPP.setTime(tempPP.getTime() + 1);
    }

    public String toString() {

        StringBuffer buffer = new StringBuffer();

        for (PredPair pp : predPairList) {

            buffer.append(pp.toString()+"\n");
        }
        return String.valueOf(buffer);
    }


}
