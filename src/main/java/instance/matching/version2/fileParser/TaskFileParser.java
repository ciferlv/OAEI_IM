package instance.matching.version2.fileParser;

import instance.matching.version2.unit.Document;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static instance.matching.version2.unit.StopWords.formatWords;
import static instance.matching.version2.unit.StopWords.getStopWords;

/**
 * Created by xinzelv on 3/19/17.
 */
public class TaskFileParser {

    private Logger logger = LoggerFactory.getLogger(TaskFileParser.class);
    private static InputStream in = null;
    private static Model model = null;

    public static void parseTaskFile(String filePath, Document document) {

        model = ModelFactory.createDefaultModel();
        getStopWords();
        accessFile(filePath);
        generateDocument(document);
        document.processDataInstance(model);
    }

    public static void accessFile(String filePath) {

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

    public static void generateDocument(Document document) {

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

                objectString = formatWords(object.asLiteral().getLexicalForm());
                if (objectString.equals("")) continue;
            } else {
                continue;
            }

            document.addInstanceToGraph(subjectString, predicateString, objectString);
        }
    }
}
