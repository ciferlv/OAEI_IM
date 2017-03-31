package instance.matching.version2;

import instance.matching.version2.eval.CalPerRecF1;
import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.Document;
import instance.matching.version2.unit.PredPairList;
import instance.matching.version2.utility.PrintAlignment;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static instance.matching.version2.fileParser.AlignFileParser.parseAlignFile;
import static instance.matching.version2.fileParser.TaskFileParser.parseTaskFile;
import static instance.matching.version2.train.AlignmentFinder.findResultAlign;
import static instance.matching.version2.train.PredPairFinder.findPredPair;

/**
 * Created by xinzelv on 17-3-27.
 */
public class prMatching {

        private static Logger logger = LoggerFactory.getLogger(prMatching.class);

        public static void main(String[] args) throws FileNotFoundException, DocumentException {

//        String refAlignFilePath = "src/main/resources/dataSet/PR/person1/dataset11_dataset12_goldstandard_person.xml";
//        String taskFilePath1 = "src/main/resources/dataSet/PR/person1/person11.rdf";
//        String taskFilePath2 = "src/main/resources/dataSet/PR/person1/person12.rdf";

                String refAlignFilePath = "src/main/resources/dataSet/PR/restaurants/restaurant1_restaurant2_goldstandard.rdf";
                String taskFilePath1 = "src/main/resources/dataSet/PR/restaurants/restaurant1.rdf";
                String taskFilePath2 = "src/main/resources/dataSet/PR/restaurants/restaurant2.rdf";

                Set<String> targetType1 = new HashSet<String>();
//        targetType1.add("http://www.okkam.org/ontology_person1.owl#Person");
                targetType1.add("http://www.okkam.org/ontology_restaurant1.owl#Restaurant");

                Set<String> targetType2 = new HashSet<String>();
//        targetType2.add("http://www.okkam.org/ontology_person2.owl#Person");
                targetType2.add("http://www.okkam.org/ontology_restaurant2.owl#Restaurant");

                Document doc1 = new Document(targetType1);
                Document doc2 = new Document(targetType2);

                parseTaskFile(taskFilePath1, doc1);
                parseTaskFile(taskFilePath2, doc2);

                logger.info(doc1.getGraph().toString());

                Alignment refAlign = new Alignment();
                parseAlignFile(refAlignFilePath, refAlign);

                Alignment alignSample = refAlign.generateSample();

                PredPairList ppl = new PredPairList();
                findPredPair(alignSample, doc1, doc2, ppl);
                logger.info(ppl.toString());

                Alignment resultAlign = new Alignment();
                findResultAlign(doc1, doc2, ppl, resultAlign);
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

                CalPerRecF1 cprf = new CalPerRecF1(refAlign, resultAlign);
        }


}
