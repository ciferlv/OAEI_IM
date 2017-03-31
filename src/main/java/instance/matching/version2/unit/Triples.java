package instance.matching.version2.unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static instance.matching.version2.nlp.CalSimilarity.calObjSetSim;

/**
 * Created by xinzelv on 3/19/17.
 */
public class Triples {

    private Logger logger = LoggerFactory.getLogger(Triples.class);

    private String subject;

    private Map<String, Set<String>> predicateObject = Collections.synchronizedMap(new HashMap<String, Set<String>>());

    private Set<String> type = new HashSet<String>();

    private Map<String, Set<String>> predObjBeRemoved = Collections.synchronizedMap(new HashMap<String, Set<String>>());


    public Triples(String tempSubject, String tempPredicate, String tempObject) {

        this.subject = tempSubject;

        addObjectToPredicate(tempObject, tempPredicate);
    }


    public void addObjectToPredicate(String tempObject, String tempPredicate) {

        if (tempPredicate.equals("type")) {

            type.add(tempObject);

        } else {

            Map<String, Set<String>> ptr = null;
            if (isURI(tempObject)) {
                ptr = predObjBeRemoved;
            } else {
                ptr = predicateObject;
            }

            if (ptr.containsKey(tempPredicate)) {

                Set<String> tempObjectSet = ptr.get(tempPredicate);
                tempObjectSet.add(tempObject);
            } else {

                Set<String> tempSet = new HashSet<String>();
                tempSet.add(tempObject);
                ptr.put(tempPredicate, tempSet);
            }
        }
    }

    public double calSimToTri(Triples tri, PredPairList ppl) {

        double sim = 0;
        int cnt = 0;

        Map<String, Set<String>> preObj = tri.getPredicateObject();

        for (PredPair pp : ppl.getPredPairList()) {

            Set<String> objSet1 = predicateObject.get(pp.getPred1());
            Set<String> objSet2 = preObj.get(pp.getPred2());

            if (objSet1 != null && objSet2 != null) {
                sim += calObjSetSim(objSet1, objSet2);
                cnt++;
            }

        }
        return sim / cnt;
    }

    private boolean isURI(String str) {

        if (str.startsWith("http")) return true;
        return false;
    }

    @Override
    public String toString() {

        StringBuffer out = new StringBuffer("subject: ");

        out.append(subject);
        out.append("\n\n");

        for (String key : predicateObject.keySet()) {

            out.append("predicate: " + key + "\n");

            Set<String> tempObjectList = predicateObject.get(key);

            for (String tempObject : tempObjectList) {

                out.append("object: " + tempObject + "\n");
            }
        }

        out.append("predicate: type\n");
        for (String str : type) {

            out.append("object: " + str + "\n");
        }

        return String.valueOf(out);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Set<String>> getPredicateObject() {
        return predicateObject;
    }

    public Set<String> getType() {
        return type;
    }

    public Map<String, Set<String>> getPredObjBeRemoved() {
        return predObjBeRemoved;
    }
}
