package instance.matching.version2.unit;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import java.util.*;

import static instance.matching.version2.utility.SparqlExecutor.execSelect;
import static instance.matching.version2.utility.VariableDef.*;

/**
 * Created by ciferlv on 17-3-30.
 */
public class Document {

    private Map<String, Triples> graph = Collections.synchronizedMap(new HashMap<String, Triples>());
    private Set<String> instances = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> classes = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> dataProperties = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> objectProperties = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> targetSubject = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> targetType = null;

    public Document(Set<String> targetType) {

        this.targetType = targetType;
    }

    public void processDataTriples(Model model) {

        filterTargetType(model);
        classifySubject();
        reinforceGraph();
    }

    public void addTriplesToGraph(String sub, String pre, String obj) {

        sub = sub.toLowerCase();
        pre = pre.toLowerCase();
        obj = obj.toLowerCase();

        if (graph.containsKey(sub)) {

            Triples myTriples = graph.get(sub);
            myTriples.addObjectToPredicate(obj, pre);
        } else {

            graph.put(sub, new Triples(sub, pre, obj));
        }
    }

    private void filterTargetType(Model model) {

        Queue<String> queue = new LinkedList<String>();

        for (String str : targetType) {

            queue.offer(str);
        }

        targetType.clear();
        while (!queue.isEmpty()) {

            String str = queue.peek();
            queue.poll();

            if (!targetType.contains(str.toLowerCase())) {
                targetType.add(str.toLowerCase());
            } else {
                continue;
            }

            String queryString =
                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
                            "PREFIX target:<" + str.split("#")[0] + "#>" +
                            "select ?subject where " +
                            "{ ?subject rdfs:subClassOf target:" + str.split("#")[1] + "}";

            ResultSet results = execSelect(queryString, model);

            while (results.hasNext()) {

                QuerySolution soln = results.nextSolution();
                String tempSubject = soln.get("subject").toString();

                if (!targetType.contains(tempSubject.toLowerCase())) {
                    queue.offer(tempSubject);
                }
            }
        }
    }

    private void classifySubject() {

        Iterator itera = graph.entrySet().iterator();

        while (itera.hasNext()) {

            Map.Entry entry = (Map.Entry) itera.next();

            String subject = (String) entry.getKey();
            Triples triples = (Triples) entry.getValue();

            Set<String> myType = triples.getType();

            if (myType.contains(CLASS_TYPE)) {
                classes.add(subject);
            } else if (myType.contains(DATA_TYPE_PROPERTY)) {
                dataProperties.add(subject);
            } else if (myType.contains(OBJECT_TYPE_PROPERTY)) {
                objectProperties.add(subject);
            } else {
                instances.add(subject);

                for (String tempType : myType) {
                    if (targetType.contains(tempType)) {
                        targetSubject.add(subject);
                        break;
                    }
                }
            }
        }
    }

    private void reinforceSub(Triples tri, String tarPred, String tarObj) {

        Triples tarTri = graph.get(tarObj);

        Map<String, Set<String>> objToRemoved = tarTri.getPredObjBeRemoved();

        Iterator iterRemove = objToRemoved.entrySet().iterator();

        while (iterRemove.hasNext()) {

            Map.Entry entry = (Map.Entry) iterRemove.next();
            String pred = (String) entry.getKey();
            Set<String> objSet = (Set<String>) entry.getValue();

            for (String obj : objSet) {

                reinforceSub(tarTri, pred, obj);
            }
        }

        objToRemoved.clear();

        if (tri == null) return;

        Map<String, Set<String>> predObj = tarTri.getPredicateObject();
        Iterator iter = predObj.entrySet().iterator();
        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry) iter.next();
            String pred = (String) entry.getKey();
            Set<String> objSet = (Set<String>) entry.getValue();

            for (String obj : objSet) {
                String tempPred = tarPred + '@' + pred;
                tri.addObjectToPredicate(obj, tempPred);
            }
        }
    }


    private void reinforceGraph() {

        for (String subject : instances) {

            reinforceSub(null, "", subject);
        }
    }

    public Set<String> getTargetType() {
        return targetType;
    }

    public Map<String, Triples> getGraph() {
        return graph;
    }

    public Set<String> getInstances() {
        return instances;
    }

    public Set<String> getClasses() {
        return classes;
    }

    public Set<String> getDataProperties() {
        return dataProperties;
    }

    public Set<String> getObjectProperties() {
        return objectProperties;
    }

    public Set<String> getTargetSubject() {
        return targetSubject;
    }
}
