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

    private static Logger logger = LoggerFactory.getLogger(TaskFileParser.class);
    private static InputStream in = null;
    private static Model model = null;

    public static void parseTaskFile(String filePath, VirtualDoc virtualDoc, Model mod) {

        model = mod;
        getStopWords();
        accessFile(filePath);
        generateVirtualDoc(virtualDoc);
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

    public static void generateVirtualDoc(VirtualDoc virtualDoc) {

        StmtIterator iter = model.listStatements();

        while (iter.hasNext()) {

            Statement stmt = iter.nextStatement();
            Resource sub = stmt.getSubject();
            Property prop = stmt.getPredicate();
            RDFNode val = stmt.getObject();


            String subString = sub.toString();
            String propString = prop.getLocalName();

            String valString;

            if (val == null) continue;

            if (val.isResource()) {

//                if (propString.equals("type")) {
//                    valString = val.asResource().getURI();
//                } else {
//                    valString = val.asResource().getLocalName();
//                }

                valString = val.asResource().getURI();
                virtualDoc.addInstToGraph(subString, propString, valString, URI_TYPE);

            } else if (val.isLiteral()) {

                valString = formatWords(val.asLiteral().getLexicalForm());
                virtualDoc.addInstToGraph(subString, propString, valString, THING_TYPE);

                if (valString.equals("")) continue;

            } else {
                continue;
            }


        }
    }
}
