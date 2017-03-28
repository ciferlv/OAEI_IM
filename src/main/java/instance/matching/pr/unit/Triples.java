package instance.matching.pr.unit;

import java.util.*;

/**
 * Created by xinzelv on 3/19/17.
 */
public class Triples {

    private String subject;

    private Map<String, Set<String>> predicateObject = new HashMap<String, Set<String>>();

    private Set<String> type = new HashSet<String>();

    public Triples() {
    }

    public Triples(String tempSubject, String tempPredicate, String tempObject) {

        this.subject = tempSubject;

        addObjectToPredicate(tempObject, tempPredicate);
    }

    public void addObjectToPredicate(String tempObject, String tempPredicate) {

//        System.out.println(tempPredicate);
        if (tempPredicate.equals("type")) {

            type.add(tempObject);

        } else {
            if (predicateObject.containsKey(tempPredicate)) {

                Set<String> tempObjectList = predicateObject.get(tempPredicate);

                if (!tempObjectList.contains(tempObject)) {

                    predicateObject.remove(tempPredicate);
                    tempObjectList.add(tempObject);
                    predicateObject.put(tempPredicate, tempObjectList);
                }
            } else {

                Set<String> tempList = new HashSet<String>();
                tempList.add(tempObject);
                predicateObject.put(tempPredicate, tempList);
            }
        }
    }

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

}
