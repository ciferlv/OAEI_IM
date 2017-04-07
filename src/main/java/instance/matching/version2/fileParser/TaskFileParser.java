package instance.matching.version2.fileParser;

import instance.matching.version2.unit.VirtualDoc;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static instance.matching.version2.unit.StopWords.formatWords;
import static instance.matching.version2.unit.StopWords.getStopWords;
import static instance.matching.version2.utility.VariableDef.THING_TYPE;
import static instance.matching.version2.utility.VariableDef.URI_TYPE;

/**
 * Created by xinzelv on 3/19/17.
 */
public class TaskFileParser {

    private Logger logger = LoggerFactory.getLogger(TaskFileParser.class);
    private static InputStream in = null;
    private static Model model = null;

    public static void parseTaskFile(String filePath, VirtualDoc virtualDoc) {

        model = ModelFactory.createDefaultModel();
        getStopWords();
        accessFile(filePath);
        generateDocument(virtualDoc);
        virtualDoc.processGraph(model);
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

    public static void generateDocument(VirtualDoc virtualDoc) {

        StmtIterator iter = model.listStatements();

        while (iter.hasNext()) {

            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            String subjectString = subject.toString();
            String predicateString = predicate.getLocalName();

            String objectString;

            if (object.isResource()) {

                objectString = object.asResource().getURI();
                virtualDoc.addInstToGraph(subjectString, predicateString, objectString, URI_TYPE);

            } else if (object.isLiteral()) {

                objectString = formatWords(object.asLiteral().getLexicalForm());
                virtualDoc.addInstToGraph(subjectString, predicateString, objectString, THING_TYPE);

                if (objectString.equals("")) continue;
            } else {
                continue;
            }


        }
    }
}
