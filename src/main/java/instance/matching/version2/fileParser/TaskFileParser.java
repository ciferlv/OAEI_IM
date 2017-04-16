package instance.matching.version2.fileParser;

import instance.matching.version2.unit.VirtualDoc;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static instance.matching.version2.unit.StopWords.getStopWords;

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

    public static void generateVirtualDoc(VirtualDoc vDoc) {

        StmtIterator iter = model.listStatements();

        while (iter.hasNext()) {

            Statement stmt = iter.nextStatement();
            Resource sub = stmt.getSubject();
            Property prop = stmt.getPredicate();
            RDFNode val = stmt.getObject();

            if (sub == null || val == null || prop == null) {
                logger.info("Sub or Prop or Val is null!");
                logger.info("sub: " + sub.toString());
                logger.info("prop: " + prop.toString());
                logger.info("val: " + val.toString());
                continue;
            }

            vDoc.addStmtToGraph(sub, prop, val);
        }
    }
}
