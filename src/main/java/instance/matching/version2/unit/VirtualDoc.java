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
public class VirtualDoc {

    private Map<String, Instance> graph = Collections.synchronizedMap(new HashMap<String, Instance>());
    private Set<String> instSet = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> classSet = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> dataPropSet = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> objPropSet = Collections.synchronizedSet(new HashSet<String>());
    private List<String> tarSubList = Collections.synchronizedList(new ArrayList<String>());
    private Set<String> tarTypeSet = null;

    public VirtualDoc(Set<String> tarTypeSet) {

        this.tarTypeSet = tarTypeSet;
    }

    public void processGraph(Model model) {

        filterTarType(model);
        classifySub();
        reinforceGraph();
    }

    public void addInstToGraph(String sub, String pre, String obj, int type) {

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

    private void filterTarType(Model model) {

        Queue<String> queue = new LinkedList<String>();

        for (String str : tarTypeSet) {

            queue.offer(str);
        }

        tarTypeSet.clear();
        while (!queue.isEmpty()) {

            String str = queue.peek();
            queue.poll();

            if (!tarTypeSet.contains(str.toLowerCase())) {
                tarTypeSet.add(str.toLowerCase());
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

                if (!tarTypeSet.contains(tempSubject.toLowerCase())) {
                    queue.offer(tempSubject);
                }
            }
        }
    }

    private void classifySub() {

        Iterator graphIter = graph.entrySet().iterator();

        while (graphIter.hasNext()) {

            Map.Entry entry = (Map.Entry) graphIter.next();

            String sub = (String) entry.getKey();
            Instance inst = (Instance) entry.getValue();

            Set<String> myTypeSet = inst.getTypeSet();

            if (myTypeSet.contains(CLASS_TYPE)) {

                classSet.add(sub);
            } else if (myTypeSet.contains(DATA_TYPE_PROPERTY)) {

                dataPropSet.add(sub);
            } else if (myTypeSet.contains(OBJECT_TYPE_PROPERTY)) {

                objPropSet.add(sub);
            } else {

                instSet.add(sub);
                for (String myType : myTypeSet) {
                    if (tarTypeSet.contains(myType)) {
                        tarSubList.add(sub);
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
            String prop = (String) entry.getKey();
            Set<Value> valSet = (Set<Value>) entry.getValue();

            for (Value val : valSet) {

                String myProp = tarProp + '@' + prop;
                inst.addValueToProp(val.getValue(), myProp, val.getType());
            }
        }
    }


    private void reinforceGraph() {

        for (String sub : instSet) {

            reinforceSub(null, "", sub);
        }
    }

    public Map<String, Instance> getGraph() {
        return graph;
    }

    public List<String> getTarSubList() {
        return tarSubList;
    }

    public String graphToString() {

        StringBuffer buffer = new StringBuffer();

        Iterator graphIter = graph.entrySet().iterator();

        while (graphIter.hasNext()) {

            Map.Entry entry = (Map.Entry) graphIter.next();

            Instance inst = (Instance) entry.getValue();

            buffer.append(inst.toString() + "\n");
        }

        return String.valueOf(buffer);
    }

}
