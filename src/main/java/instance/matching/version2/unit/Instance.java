package instance.matching.version2.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static instance.matching.version2.nlp.CalSimilarity.calValSetSim;
import static instance.matching.version2.utility.VariableDef.PROP_PAIR_THRESHOLD;
import static instance.matching.version2.utility.VariableDef.URI_TYPE;

/**
 * Created by xinzelv on 3/19/17.
 */
public class Instance {

    private Logger logger = LoggerFactory.getLogger(Instance.class);

    private String sub;

    private Map<String, Set<Value>> propValue = Collections.synchronizedMap(new HashMap<String, Set<Value>>());

    private Set<String> typeSet = new HashSet<String>();

    //store the objects whose type are URI type
    private Map<String, Set<Value>> propUri = Collections.synchronizedMap(new HashMap<String, Set<Value>>());


    public Instance(String sub, String pred, String obj, int objType) {

        this.sub = sub;

        addValueToProp(obj, pred, objType);
    }


    public synchronized void addValueToProp(String myVal, String myProp, int valType) {

        if (myVal.equals("") || myProp.equals("")) return;

        if (myProp.equals("type")) {

            typeSet.add(myVal);
        } else {

            Map<String, Set<Value>> ptr;

            if (valType == URI_TYPE) {
                ptr = propUri;
            } else {
                ptr = propValue;
            }

//            ptr = propValue;

            if (ptr.containsKey(myProp)) {

                Set<Value> myValueSet = ptr.get(myProp);
                myValueSet.add(new Value(myVal, valType));
            } else {

                Set<Value> mySet = new HashSet<Value>();
                mySet.add(new Value(myVal, valType));
                ptr.put(myProp, mySet);
            }
        }
    }

    public Map<Double, Integer> calSimToInst(Instance inst, PropPairList ppl) {

        double totalSimi = 0;
        int matchedCnt = 0, simiCnt = 0;

        Map<Double, Integer> res = new HashMap<Double, Integer>();

        Map<String, Set<Value>> myPropValue = inst.getPropValue();

        for (PropPair pp : ppl.getPropPairList()) {

            Set<Value> valSet1 = propValue.get(pp.getProp1());
            Set<Value> valSet2 = myPropValue.get(pp.getProp2());

            if (valSet1 != null && valSet2 != null) {

                double eachSimi = calValSetSim(valSet1, valSet2);
                totalSimi += eachSimi;
                simiCnt++;
                if (eachSimi > PROP_PAIR_THRESHOLD) matchedCnt++;
            }

        }

        res.put(1.0 * totalSimi / simiCnt, matchedCnt);
        return res;
    }

    @Override
    public String toString() {

        StringBuffer out = new StringBuffer("sub: ");

        out.append(sub);
        out.append("\n\n");

        for (String key : propValue.keySet()) {

            out.append("property: " + key + "\n");

            Set<Value> myValueSet = propValue.get(key);

            for (Value myValue : myValueSet) {

                out.append("value: " + myValue.getValue() + "\n");
            }
        }

        for (String key : propUri.keySet()) {

            out.append("property: " + key + "\n");

            Set<Value> myValueSet = propUri.get(key);

            for (Value myValue : myValueSet) {

                out.append("value: " + myValue.getValue() + "\n");
            }
        }

        out.append("predicate: type\n");
        for (String myType : typeSet) {

            out.append("value: " + myType + "\n");
        }

        return String.valueOf(out);
    }

    public String getSub() {
        return sub;
    }

    public Map<String, Set<Value>> getPropValue() {
        return propValue;
    }

    public Set<String> getTypeSet() {
        return typeSet;
    }

    public Map<String, Set<Value>> getPropUri() {
        return propUri;
    }
}

