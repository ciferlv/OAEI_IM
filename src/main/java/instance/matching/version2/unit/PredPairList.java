package instance.matching.version2.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPairList {

    private Logger logger = LoggerFactory.getLogger(PredPairList.class);
    private List<PredPair> predPairList = Collections.synchronizedList(new ArrayList<PredPair>());


    public synchronized PredPair contains(PredPair pp) {

        Iterator<PredPair> iter = predPairList.iterator();

        while (iter.hasNext()) {

            PredPair tempPP = iter.next();
            if (tempPP.equals(pp)) {
                return tempPP;
            }
        }
        return null;
    }

    public synchronized void add(PredPair pp) {

        if (pp == null) return;
        PredPair tempPP = contains(pp);
        if (tempPP == null) {
            predPairList.add(pp);
        } else tempPP.setTime(tempPP.getTime() + 1);
    }

    public void resize(int size) {

        for (int i = predPairList.size() - 1; i >= size; i--) {

            predPairList.remove(i);
        }

    }

    public int size() {

        return predPairList.size();
    }

    @Override
    public synchronized String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("size: " + size() + "\n");

        for (PredPair pp : predPairList) {

            buffer.append(pp.toString() + "\n");
        }
        return String.valueOf(buffer);
    }

    public void sort() {
        Collections.sort(predPairList);
    }

    public List<PredPair> getPredPairList() {
        return predPairList;
    }
}
