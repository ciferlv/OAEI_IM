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
public class PropPairList {

    private Logger logger = LoggerFactory.getLogger(PropPairList.class);
    private List<PropPair> propPairList = Collections.synchronizedList(new ArrayList<PropPair>());


    public synchronized PropPair contains(PropPair pp) {

        Iterator<PropPair> iter = propPairList.iterator();

        while (iter.hasNext()) {

            PropPair tempPP = iter.next();
            if (tempPP.equals(pp)) {
                return tempPP;
            }
        }
        return null;
    }

    public synchronized void add(PropPair pp) {

        if (pp == null) return;
        PropPair tempPP = contains(pp);
        if (tempPP == null) {
            propPairList.add(pp);
        } else tempPP.setTime(tempPP.getTime() + 1);
    }

    public void resize(int size) {

        for (int i = propPairList.size() - 1; i >= size; i--) {

            propPairList.remove(i);
        }

    }

    public int size() {

        return propPairList.size();
    }

    @Override
    public synchronized String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("size: " + size() + "\n");

        for (PropPair pp : propPairList) {

            buffer.append(pp.toString() + "\n");
        }
        return String.valueOf(buffer);
    }

    public void sort() {
        Collections.sort(propPairList);
    }

    public List<PropPair> getPropPairList() {
        return propPairList;
    }
}
