package instance.matching.version2.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static instance.matching.version2.nlp.CalSimilarity.calObjSetSim;
import static instance.matching.version2.utility.VariableDef.URI_TYPE;
import static instance.matching.version2.utility.VariableDef.PROP_PAIR_THRESHOLD;

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


    public synchronized void addValueToProp(String myObj, String myPred, int objType) {

        if (myObj.equals("") || myPred.equals("")) return;

        if (myPred.equals("type")) {

            typeSet.add(myObj);
        } else {

            Map<String, Set<Value>> ptr;

            if (objType == URI_TYPE) {
                ptr = propUri;
            } else {
                ptr = propValue;
            }

            if (ptr.containsKey(myPred)) {

                Set<Value> myValueSet = ptr.get(myPred);
                myValueSet.add(new Value(myObj, objType));
            } else {

                Set<Value> mySet = new HashSet<Value>();
                mySet.add(new Value(myObj, objType));
                ptr.put(myPred, mySet);
            }
        }
    }

    public Map<Double, Integer> calSimToIns(Instance ins, PropPairList ppl) {

        double simi = 0;
        int cntMatched = 0, simiCnt = 0;

        Map<Double, Integer> res = new HashMap<Double, Integer>();

        Map<String, Set<Value>> myPredToObj = ins.getPropValue();

        for (PropPair pp : ppl.getPropPairList()) {

            Set<Value> objSet1 = propValue.get(pp.getPred1());
            Set<Value> objSet2 = myPredToObj.get(pp.getPred2());

            if (objSet1 != null && objSet2 != null) {

                double value = calObjSetSim(objSet1, objSet2);
                simi += value;
                simiCnt++;
                if (value > PROP_PAIR_THRESHOLD) cntMatched++;
            }

        }

        res.put(simi / simiCnt, cntMatched);
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

        out.append("predicate: type\n");
        for (String myType : typeSet) {

            out.append("value: " + myType + "\n");
        }

        return String.valueOf(out);
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
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

