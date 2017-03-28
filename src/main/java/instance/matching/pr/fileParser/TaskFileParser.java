package instance.matching.pr.fileParser;

import instance.matching.pr.unit.StopWords;
import instance.matching.pr.unit.Triples;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import static instance.matching.helper.VariableDef.CLASS_TYPE;
import static instance.matching.helper.VariableDef.DATA_TYPE_PROPERTY;
import static instance.matching.helper.VariableDef.OBJECT_TYPE_PROPERTY;

/**
 * Created by xinzelv on 3/19/17.
 */
public class TaskFileParser {

    private Logger logger = LoggerFactory.getLogger(TaskFileParser.class);

    private String filePath;
    private InputStream in = null;

    private Set<String> targetType;
    private Model model = ModelFactory.createDefaultModel();

    private Map<String, Triples> graph = Collections.synchronizedMap(new HashMap<String, Triples>());
    private Set<String> instances = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> classes = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> dataProperties = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> objectProperties = Collections.synchronizedSet(new HashSet<String>());
    private Set<String> targetSubject = Collections.synchronizedSet(new HashSet<String>());

    private StopWords sw = new StopWords();

    public TaskFileParser(String filePath, Set<String> targetType) throws FileNotFoundException {

        this.filePath = filePath;
        this.targetType = targetType;

        accessFile();
        extractEntity();
        filterTargetType();
        classifySubject();
    }

    private void accessFile() {

        in = FileManager.get().open(filePath);
        if (in == null) {
            throw new IllegalArgumentException("File: " + filePath + " not found");
        }

        if (filePath.endsWith(".nt")) {
            model.read(in, "", "N3");
        } else {
            model.read(in, "");
        }
    }

    private void extractEntity() throws FileNotFoundException {

        StmtIterator iter = model.listStatements();

        while (iter.hasNext()) {

            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            String subjectString = subject.toString();
            String predicateString = predicate.getLocalName();

            String objectString = null;
            if (object.isResource()) {

                objectString = object.asResource().getURI();
            } else if (object.isLiteral()) {

                objectString = sw.formatWords(object.asLiteral().getLexicalForm());
                if (objectString.equals("")) continue;
            } else {
                continue;
            }

            if (graph.containsKey(subjectString)) {

                Triples myTriples = graph.get(subjectString);
                myTriples.addObjectToPredicate(objectString, predicateString);
            } else {
                graph.put(subjectString, new Triples(subjectString, predicateString, objectString));
            }
        }
    }

    private void filterTargetType() {

        Queue<String> queue = new LinkedList<String>();

        for (String str : targetType) {

            queue.offer(str);
        }

        while (!queue.isEmpty()) {

            String str = queue.peek();
            queue.poll();

            String queryString =
                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
                            "PREFIX target:<" + str.split("#")[0] + "#>" +
                            "select ?subject where " +
                            "{ ?subject rdfs:subClassOf target:" + str.split("#")[1] + "}";

            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();

            while (results.hasNext()) {

                QuerySolution soln = results.nextSolution();
                String tempSubject = soln.get("subject").toString();
                if (!targetType.contains(tempSubject)) {
                    targetType.add(tempSubject);
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, Triples> getGraph() {
        return graph;
    }

    public Set<String> getTargetSubject() {
        return targetSubject;
    }

    public Set<String> getTargetType() {
        return targetType;
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
}
