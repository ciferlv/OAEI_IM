package instance.matching.version2.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static instance.matching.version2.utility.VariableDef.initialSamplePersent;

/**
 * Created by xinzelv on 17-3-27.
 */
public class Alignment {

    private Logger logger = LoggerFactory.getLogger(Alignment.class);

    private List<CounterPart> counterPartList = Collections.synchronizedList(new ArrayList<CounterPart>());


    public synchronized void addCounterPart(CounterPart counterPart) {

        counterPartList.add(counterPart);
    }

    public int size() {

        return counterPartList.size();
    }

    public boolean contains(CounterPart cp) {

        if(counterPartList.contains(cp)) return true;
        else return false;
    }

    public CounterPart findCounterPart(int index) {

        if (index < counterPartList.size() && index >= 0) {
            return counterPartList.get(index);
        } else {

            logger.info("[Alignment.counterPartList] index is not found!");
            return null;
        }

    }

    public Alignment generateSample() {

        Alignment sample = new Alignment();
        Random r = new Random();

        int sampleSize = (int) (size() * initialSamplePersent);

        while (sample.size() < sampleSize) {

            int index = r.nextInt(size());
            sample.addCounterPart(findCounterPart(index));
        }
        return sample;
    }

    public String toString() {

        StringBuffer bf = new StringBuffer();

        bf.append("size: " + counterPartList.size() + "\n");

        for (CounterPart cp : counterPartList) {
            bf.append(cp.toString());
        }
        return String.valueOf(bf);
    }

    public List<CounterPart> getCounterPartList() {
        return counterPartList;
    }
}
