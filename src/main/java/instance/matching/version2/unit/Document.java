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

    private Map<String, Instance> graph = Collections.synchronizedMap(new HashMap<String, Instance>());
    private Set<String> instances = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> classes = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> dataProperties = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> objectProperties = Collections.synchronizedSet(new HashSet<String>());
    private List<String> targetSubject = Collections.synchronizedList(new ArrayList<String>());
    private Set<String> targetType = null;

    public Document(Set<String> targetType) {

        this.targetType = targetType;
    }

    public void processDataInstance(Model model) {

        filterTargetType(model);
        classifySubject();
        reinforceGraph();
    }

    public void addInstanceToGraph(String sub, String pre, String obj, int type) {

        sub = sub.toLowerCase();
        pre = pre.toLowerCase();
        obj = obj.toLowerCase();

        if (graph.containsKey(sub)) {

            Instance myInstance = graph.get(sub);
            myInstance.addValueToProp(obj, pre, type);
        } else {

            graph.put(sub, new Instance(sub, pre, obj, type));
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
            Instance instance = (Instance) entry.getValue();

            Set<String> myType = instance.getTypeSet();

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

    private void reinforceSub(Instance inst, String tarProp, String tarValue) {

        Instance tarInst = graph.get(tarValue);

        Map<String, Set<Value>> propUri = tarInst.getPropUri();

        Iterator iterPropUri = propUri.entrySet().iterator();

        while (iterPropUri.hasNext()) {

            Map.Entry entry = (Map.Entry) iterPropUri.next();

            String prop = (String) entry.getKey();
            Set<Value> valueSet = (Set<Value>) entry.getValue();

            for (Value value : valueSet) {

                reinforceSub(tarInst, prop, value.getValue());
            }
        }

        propUri.clear();

        if (inst == null) return;

        Map<String, Set<Value>> propValue = tarInst.getPropValue();
        Iterator iter = propValue.entrySet().iterator();
        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry) iter.next();
            String pred = (String) entry.getKey();
            Set<String> objSet = (Set<String>) entry.getValue();

            for (String obj : objSet) {
                String tempPred = tarProp + '@' + pred;
                inst.addValueToProp(obj, tempPred);
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

    public Map<String, Instance> getGraph() {
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

    public List<String> getTargetSubject() {
        return targetSubject;
    }

    public String graphToString() {

        StringBuffer buffer = new StringBuffer();

        Iterator iter = graph.entrySet().iterator();

        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry) iter.next();

            Instance tri = (Instance) entry.getValue();

            buffer.append(tri.toString() + "\n");
        }

        return String.valueOf(buffer);
    }

}
