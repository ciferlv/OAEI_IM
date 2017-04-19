package instance.matching.version2.unit;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static instance.matching.version2.nlp.FormatData.formatWords;
import static instance.matching.version2.utility.ParamDef.*;

/**
 * Created by ciferlv on 17-3-30.
 */
public class VirtualDoc {

    private Logger logger = LoggerFactory.getLogger(VirtualDoc.class);

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

    public void processGraph() {

        filterTarType();
        getTarSub();
        if (USE_REINFORCE) {
            reinforceGraph();
        }
    }

    public int findTypeIndex(String rtype) {

        int typeIndex = THING_TYPE_INDEX;
        if (rtype.equals(STRING_TYPE)) {
            typeIndex = STRING_TYPE_INDEX;
        } else if (rtype.equals(BOOLEAN_TYPE)) {
            typeIndex = BOOLEAN_TYPE_INDEX;
        } else if (rtype.equals(DATETIME_TYPE)) {
            typeIndex = DATETIME_TYPE_INDEX;
        } else if (rtype.equals(INTEGER_TYPE)) {
            typeIndex = INTEGER_TYPE_INDEX;
        } else if (rtype.equals(FLOAT_TYPE)) {
            typeIndex = FLOAT_TYPE_INDEX;
        } else if (rtype.equals(DATE_TYPE)) {
            typeIndex = DATE_TYPE_INDEX;
        }
        return typeIndex;
    }

    public void addStmtToGraph(Resource sub, Property prop, RDFNode val) {

        String subStr = sub.toString().toLowerCase();
//        String propStr = prop.getLocalName().toLowerCase();
        String propStr = prop.toString().toLowerCase();

        String valStr = "";
        String valLocalName = "";
        int typeIndex = THING_TYPE_INDEX;

        if (val.isResource()) {

            valStr = val.asResource().getURI().toLowerCase();
            valLocalName = val.asResource().getLocalName();
            if (valLocalName.equals("") || valLocalName == null) {

                String[] tempSplit = valStr.split("/");
                valLocalName = tempSplit[tempSplit.length - 1];
            }
            valLocalName = formatWords(valLocalName);
            typeIndex = URI_TYPE_INDEX;

            if (propStr.equals(TYPE_FULL_NAME)) {
                if (valStr.equals(CLASS_TYPE)) {
                    classSet.add(subStr);
                } else if (valStr.equals(OBJECT_TYPE_PROPERTY)) {
                    objPropSet.add(subStr);
                } else if (valStr.equals(DATA_TYPE_PROPERTY)) {
                    dataPropSet.add(subStr);
                } else {
                    instSet.add(subStr);
                }
            }

        } else if (val.isLiteral()) {

            valStr = formatWords(val.asLiteral().getLexicalForm());

            try {

                String rtype = val.asLiteral().getDatatypeURI();
                typeIndex = findTypeIndex(rtype);

            } catch (Exception e) {

            }
            if (valStr.equals("")) {

//                logger.info("The val is null after being formated.");
//                logger.info(val.asLiteral().getLexicalForm());
                return;
            }
        }


        if (graph.containsKey(subStr)) {

            Instance myInstance = graph.get(subStr);
            myInstance.addValueToProp(valStr, propStr, valLocalName, typeIndex);
        } else {

            graph.put(subStr, new Instance(subStr, propStr, valStr, valLocalName, typeIndex));
        }
    }

    private void filterTarType() {

        Map<String, Set<String>> classTree = new HashMap<String, Set<String>>();

        for (String strClass : classSet) {

            Instance myInst = graph.get(strClass);
            Set<Value> subClassOf;
            if (myInst.propUriIsEmpty()) {
                subClassOf = myInst.getPropValue().get(SUBCLASSOF_FULL_NAME);
            } else {
                subClassOf = myInst.getPropUri().get(SUBCLASSOF_FULL_NAME);
            }

            if (subClassOf == null) continue;

            for (Value strSubClass : subClassOf) {

                String myValue = strSubClass.getValue();
                if (classTree.containsKey(myValue)) {
                    Set<String> mySet = classTree.get(myValue);
                    mySet.add(strClass);
                } else {
                    Set<String> mySet = new HashSet<String>();
                    mySet.add(strClass);
                    classTree.put(myValue, mySet);
                }
            }
        }

        Queue<String> queue = new LinkedList<String>();

        for (String str : tarTypeSet) {

            queue.offer(str);
        }

        tarTypeSet.clear();
        while (!queue.isEmpty()) {

            String top = queue.peek();
            queue.poll();

            if (!tarTypeSet.contains(top)) {
                tarTypeSet.add(top);
            } else {
                continue;
            }

            Set<String> subClassSet = classTree.get(top);
            if (subClassSet == null) continue;
            for (String subClass : subClassSet) {

                queue.offer(subClass);
            }
        }
    }

    private void getTarSub() {

        for (String subStr : instSet) {

            Instance inst = graph.get(subStr);
            Set<String> myTypeSet = inst.getTypeSet();
            for (String myType : myTypeSet) {
                if (tarTypeSet.contains(myType)) {
                    tarSubList.add(subStr);
                    break;
                }
            }
        }
    }

    private void reinforceSub(Instance inst, String tarProp, String tarValue) {

        Instance tarInst = graph.get(tarValue);

        if (tarInst == null) return;

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
                inst.addValueToProp(val.getValue(), myProp, val.getLocalName(), val.getType());
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
