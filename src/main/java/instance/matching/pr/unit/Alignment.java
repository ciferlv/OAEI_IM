package instance.matching.pr.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xinzelv on 17-3-27.
 */
public class Alignment {

    private Logger logger = LoggerFactory.getLogger(Alignment.class);

    private List<CounterPart> counterPartList = Collections.synchronizedList(new ArrayList<CounterPart>());


    public void addCounterPart(CounterPart counterPart) {

        counterPartList.add(counterPart);
    }

    public int size() {

        return counterPartList.size();
    }

    public CounterPart findCounterPart(int index) {

        if (index < counterPartList.size() && index >= 0) {
            return counterPartList.get(index);
        } else {

            logger.info("[Alignment.counterPartList] index is not found!");
            return null;
        }

    }

    public String toString() {

        StringBuffer bf = new StringBuffer();

        bf.append("size: " + counterPartList.size() + "\n");

        for (CounterPart cp : counterPartList) {
            bf.append(cp.toString());
        }
        return String.valueOf(bf);
    }


}
