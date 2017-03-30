package instance.matching.version2;

import instance.matching.version2.eval.CalPerRecF1;
import instance.matching.version2.fileParser.AlignFileParser;
import instance.matching.version2.fileParser.TaskFileParser;
import instance.matching.version2.train.AlignmentFinder;
import instance.matching.version2.train.PredPairFinder;
import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.PredPairList;
import instance.matching.version2.unit.Triples;
import instance.matching.version2.utility.PrintAlignment;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static instance.matching.version2.utility.VariableDef.initialSamplePersent;

/**
 * Created by xinzelv on 17-3-27.
 */
public class prMatching {

    private static Logger logger = LoggerFactory.getLogger(prMatching.class);

    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        String refAlignFilePath = "src/main/resources/dataSet/PR/person1/dataset11_dataset12_goldstandard_person.xml";
        String taskFilePath1 = "src/main/resources/dataSet/PR/person1/person11.rdf";
        String taskFilePath2 = "src/main/resources/dataSet/PR/person1/person12.rdf";

        Set<String> targetType1 = new HashSet<String>();
        targetType1.add("http://www.okkam.org/ontology_person1.owl#Person");

        Set<String> targetType2 = new HashSet<String>();
        targetType2.add("http://www.okkam.org/ontology_person2.owl#Person");

        TaskFileParser taskFileParser1 = new TaskFileParser(taskFilePath1, targetType1);
        TaskFileParser taskFileParser2 = new TaskFileParser(taskFilePath2, targetType2);

        AlignFileParser alignFileParser = new AlignFileParser(refAlignFilePath);


        Alignment refAlign = alignFileParser.getAligns();

        Map<String, Triples> graph1 = taskFileParser1.getGraph();
        Map<String, Triples> graph2 = taskFileParser2.getGraph();
        Set<String> targetSubject1 = taskFileParser1.getTargetSubject();
        Set<String> targetSubject2 = taskFileParser2.getTargetSubject();


        Alignment alignSample = new Alignment();
        Random r = new Random();

        int sampleSize = (int) (refAlign.size() * initialSamplePersent);

        while (alignSample.size() < sampleSize) {

            int index = r.nextInt(refAlign.size());
            alignSample.addCounterPart(refAlign.findCounterPart(index));
        }

        PredPairFinder ppf = new PredPairFinder(alignSample, graph1, graph2);
        ppf.findPredPair();
        PredPairList ppl = ppf.getPredPairList();
        logger.info(ppl.toString());

        AlignmentFinder af = new AlignmentFinder(graph1, graph2, targetSubject1, targetSubject2, ppl);
        af.findAlign();
        Alignment resultAlign = af.getResultAlign();
        logger.info(resultAlign.toString());

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
        String resultFilePath = "target/result.txt";
        PrintAlignment pa = new PrintAlignment(resultFilePath, resultAlign);
        pa.setHead(head);
        pa.setTail("</Alignment>\n</rdf:RDF>");
        pa.print();

        CalPerRecF1 cprf = new CalPerRecF1(refAlign,resultAlign);
    }


}
