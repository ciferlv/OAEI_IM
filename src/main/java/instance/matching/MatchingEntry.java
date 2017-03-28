package instance.matching;

import instance.matching.unit.SubjectSimilarity;
import instance.matching.helper.ComputePrecisionAndRecallHelper;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static instance.matching.helper.PrintMethodHelper.*;
import static instance.matching.helper.VariableDef.*;

/**
 * Created by ciferlv on 17-3-4.
 */
public class MatchingEntry {

    static Logger logger = LoggerFactory.getLogger(MatchingEntry.class);

    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        for (int i = 0; i < 3; i++) {

            PrintWriter outStream = new PrintWriter(new FileOutputStream(RESULT_FILE_PATH[i]));

            InsMatching im = new InsMatching(
                    RDF1_PATH[i],
                    RDF2_PATH[i],
                    OWL1_PATH[i],
                    OWL2_PATH[i],
                    STANDARD_PATH[i],
                    AIM_LOCAL_NAME[i]);

            im.readAndGetEntity();
            im.compare();

            String head = "<?xml version='1.0' encoding='utf-8' standalone='no'?>\n"
                    + "<rdf:RDF xmlns='http://knowledgeweb.semanticweb.org/heterogeneity/alignment#'\n"
                    + "\t\t xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' \n"
                    + "\t\t xmlns:xsd='http://www.w3.org/2001/XMLSchema#'\n"
                    + "\t\t xmlns:align='http://knowledgeweb.semanticweb.org/heterogeneity/alignment#'>\n"
                    + "<CounterPart>\n"
                    + "\t<xml>yes</xml>\n"
                    + "\t<level>0</level>\n"
                    + "\t<type>**</type>\n"
                    + "\t<onto1>\n"
                    + "\t\t<Ontology>\n"
                    + "\t\t\t<location>null</location>\n"
                    + "\t\t</Ontology>\n"
                    + "\t</onto1>\n"
                    + "\t<onto1>\n"
                    + "\t\t<Ontology>\n"
                    + "\t\t\t<location>null</location>\n"
                    + "\t\t</Ontology>\n"
                    + "\t</onto1>\n";

            printHead(outStream,head);

            List<SubjectSimilarity> results = im.getResultList();

            for (SubjectSimilarity subSim : results) {

                outStream.print(toRDF(subSim.getSubject1(), subSim.getSubject2(), "=", "1.0"));
            }

            printTail(outStream);

            outStream.close();

            Map<String, SubjectSimilarity> myLink = im.getMapCoreferentSubject();

            ComputePrecisionAndRecallHelper comPreAndRec =
                    new ComputePrecisionAndRecallHelper(STANDARD_PATH[i], myLink);

            logger.info("precision: " + comPreAndRec.getPrecision());
            logger.info("recall: " + comPreAndRec.getRecall());

            comPreAndRec.printUnFoundResult(UNFOUND_RESULT_FILE_PATH[i]);
            comPreAndRec.printCorrectResult(CORRECT_RESULT_FILE_PATH[i]);
            comPreAndRec.printWrongResult(WRONG_RESULT_FILE_PATH[i]);
        }
    }
}