package instance.matching.version2;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.PropPairList;
import instance.matching.version2.unit.VirtualDoc;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static instance.matching.version2.eval.Metrics.calMetrics;
import static instance.matching.version2.fileParser.AlignFileParser.parseAlignFile;
import static instance.matching.version2.fileParser.TaskFileParser.parseTaskFile;
import static instance.matching.version2.train.AlignmentFinder.findResultAlign;
import static instance.matching.version2.train.InfoGainCalculator.calInfoGain;
import static instance.matching.version2.train.NegetiveFinder.findNegetives;
import static instance.matching.version2.train.PropPairFinder.findPropPair;
import static instance.matching.version2.utility.AlignWriter.printAlign;
import static instance.matching.version2.utility.FileWriter.printToFile;
import static instance.matching.version2.utility.VariableDef.*;


/**
 * Created by xinzelv on 17-3-27.
 */
public class prMatching {

    private static Logger logger = LoggerFactory.getLogger(prMatching.class);

    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        Set<String> targetType1 = new HashSet<String>();
        targetType1.add(TARGET_TYPE1[DATASET_INDEX].toLowerCase());

        Set<String> targetType2 = new HashSet<String>();
        targetType2.add(TARGET_TYPE2[DATASET_INDEX].toLowerCase());

        VirtualDoc doc1 = new VirtualDoc(targetType1);
        VirtualDoc doc2 = new VirtualDoc(targetType2);

        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();

        parseTaskFile(INST1_PATH[DATASET_INDEX], doc1, model1);
        parseTaskFile(INST2_PATH[DATASET_INDEX], doc2, model2);

        doc1.processGraph();
        doc2.processGraph();

        printToFile(INSTANCE_SET1_FILE_PATH[DATASET_INDEX], doc1.graphToString());
        printToFile(INSTANCE_SET2_FILE_PATH[DATASET_INDEX], doc2.graphToString());

        Alignment refAlign = new Alignment();
        parseAlignFile(STANDARD_PATH[DATASET_INDEX], refAlign);

        Alignment positives = refAlign.generatePositives();

        Alignment negetives = new Alignment();
        findNegetives(positives, doc2, negetives);
//        logger.info(negetives.toString());

        PropPairList ppl = new PropPairList();
        findPropPair(positives, doc1, doc2, ppl);
        calInfoGain(positives, negetives, doc1, doc2, ppl);
        printToFile(PROPPAIRLIST_FILE_PATH[DATASET_INDEX],ppl.toString());

        Alignment resultAlign = new Alignment();
        findResultAlign(doc1, doc2, ppl, resultAlign);
//        logger.info(resultAlign.toString());

        String head = "<?xml version='1.0' encoding='utf-8' standalone='no'?>\n"
                + "<rdf:RDF xmlns='http://knowledgeweb.semanticweb.org/heterogeneity/alignment#'\n"
                + "\t\t xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' \n"
                + "\t\t xmlns:xsd='http://www.w3.org/2001/XMLSchema#'\n"
                + "\t\t xmlns:align='http://knowledgeweb.semanticweb.org/heterogeneity/alignment#'>\n"
                + "<Alignment>\n"
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

        String tail = "</Alignment>\n</rdf:RDF>";

        printAlign(RESULT_FILE_PATH[DATASET_INDEX], head, tail, resultAlign);

        calMetrics(refAlign, resultAlign);
    }
}
