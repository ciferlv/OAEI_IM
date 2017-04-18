import instance.matching.version2.unit.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static instance.matching.version2.fileParser.AlignFileParser.parseAlignFile;
import static instance.matching.version2.fileParser.TaskFileParser.parseTaskFile;
import static instance.matching.version2.nlp.CalSimilarity.calValSetSim;
import static instance.matching.version2.nlp.FormatData.getStopWords;
import static instance.matching.version2.train.InfoGainCalculator.calInfoGain;
import static instance.matching.version2.train.NegetiveFinder.findNegetives;
import static instance.matching.version2.train.PropPairFinder.findPropPair;
import static instance.matching.version2.utility.ParamDef.*;

/**
 * Created by ciferlv on 17-4-18.
 */
public class instMatchTest {

    private static Logger logger = LoggerFactory.getLogger(instMatchTest.class);

    public static void init() {

        getStopWords();
    }

    public static void compResult(PropPairList ppl, Instance inst1, Instance inst2) {

        double totalSimi = 0;
        int simiCnt = 0;
        int matchedCnt = 0;
        Map<String, Set<Value>> propVal1 = inst1.getPropValue();
        Map<String, Set<Value>> propVal2 = inst2.getPropValue();

        for (PropPair pp : ppl.getPropPairList()) {

            logger.info("Prop1:" + pp.getProp1());
            logger.info("Prop2:" + pp.getProp2());

            Set<Value> valSet1 = propVal1.get(pp.getProp1());
            Set<Value> valSet2 = propVal2.get(pp.getProp2());

            if (valSet1 != null && valSet2 != null) {

                double eachSimi = calValSetSim(valSet1, valSet2);

                logger.info("simi:" + eachSimi);

                totalSimi += eachSimi;
                simiCnt++;
                if (eachSimi > PROP_PAIR_THRESHOLD) matchedCnt++;
            }


        }

    }


    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        init();

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

        for (String str : doc1.getTarSubList()) {

            logger.info(str);
        }

        Alignment refAlign = new Alignment();
        parseAlignFile(STANDARD_PATH[DATASET_INDEX], refAlign);

        Alignment positives = refAlign.generatePositives();

        Alignment negetives = new Alignment();
        findNegetives(positives, doc2, negetives);

        PropPairList ppl = new PropPairList();
        findPropPair(positives, doc1, doc2, ppl);
        calInfoGain(positives, negetives, doc1, doc2, ppl);

//        String sub1 = "http://www.bbc.co.uk/things/4#id";
//        String sub2 = "http://www.bbc.co.uk/things/4#id";
//
//        Map<String, Instance> graph1 = doc1.getGraph();
//        Map<String, Instance> graph2 = doc2.getGraph();
//        Instance inst1 = graph1.get(sub1);
//        Instance inst2 = graph2.get(sub2);

//        compResult(ppl, inst1, inst2);

//        printToFile(INSTANCE_SET1_FILE_PATH[DATASET_INDEX], doc1.graphToString());
//        printToFile(INSTANCE_SET2_FILE_PATH[DATASET_INDEX], doc2.graphToString());
//        printToFile(PROPPAIRLIST_FILE_PATH[DATASET_INDEX], ppl.toString());
    }
}
