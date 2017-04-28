package instance.matching.version2;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.PropPairList;
import instance.matching.version2.unit.VirtualDoc;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static instance.matching.version2.eval.Metrics.calMetrics;
import static instance.matching.version2.fileParser.AlignFileParser.parseAlignFile;
import static instance.matching.version2.fileParser.MetaDataFileParser.parseMetaDataSPIM;
import static instance.matching.version2.fileParser.TaskFileParser.parseTaskFile;
import static instance.matching.version2.nlp.FormatData.getStopWords;
import static instance.matching.version2.train.AlignmentFinder.findResultAlign;
import static instance.matching.version2.train.InfoGainCalculator.calInfoGain;
import static instance.matching.version2.train.NegetiveFinder.findNegetives;
import static instance.matching.version2.train.PropPairFinder.findPropPair;
import static instance.matching.version2.utility.AlignWriter.printAlign;
import static instance.matching.version2.utility.FileWriter.printToFile;
import static instance.matching.version2.utility.ParamDef.*;

/**
 * Created by ciferlv on 17-4-25.
 */
public class PRMatchingEntry {

    private static Logger logger = LoggerFactory.getLogger(PRMatchingEntry.class);

    private static void init() throws IOException, OWLOntologyCreationException {

        confFileIndex = 3;
        Properties pro = new Properties();
        FileInputStream in = new FileInputStream(CONF_FILE_PATH[confFileIndex]);
        pro.load(in);
        in.close();

        inst1_path = pro.getProperty("inst1_path");
        inst2_path = pro.getProperty("inst2_path");
        supp1_path = pro.getProperty("supp1_path");
        supp2_path = pro.getProperty("supp2_path");
        standard_path = pro.getProperty("standard_path");
        result_file_path = pro.getProperty("result_file_path");
        correct_result_file_path = pro.getProperty("correct_result_file_path");
        wrong_result_file_path = pro.getProperty("wrong_result_file_path");
        unfound_result_file_path = pro.getProperty("unfound_result_file_path");
        instance_set1_file_path = pro.getProperty("instance_set1_file_path");
        instance_set2_file_path = pro.getProperty("instance_set2_file_path");
        prop_pair_list_file_path = pro.getProperty("prop_pair_list_file_path");
        stopwords_file_path = pro.getProperty("stopwords_file_path");

        use_average_simi = Boolean.parseBoolean(pro.getProperty("use_average_simi"));
        use_reinforce = Boolean.parseBoolean(pro.getProperty("use_reinforce"));

        String[] typeStrArray1 = pro.getProperty("target_type1").split(";");
        for (int i = 0; i < typeStrArray1.length; i++) {
            tarTypeSet1.add(typeStrArray1[i].toLowerCase());
        }
        String[] typeStrArray2 = pro.getProperty("target_type2").split(";");
        for (int i = 0; i < typeStrArray2.length; i++) {
            tarTypeSet2.add(typeStrArray2[i].toLowerCase());
        }

        getStopWords();

//        if (confFileIndex < 3) {
//            parseMetaDataPR(supp1_path, propDetailMap1);
//            parseMetaDataPR(supp2_path, propDetailMap2);
//        }
        if (confFileIndex > 2) {

            parseMetaDataSPIM(supp1_path);
            parseMetaDataSPIM(supp2_path);
        }

    }

    private static void matching() throws FileNotFoundException {

        VirtualDoc doc1 = new VirtualDoc(tarTypeSet1);
        VirtualDoc doc2 = new VirtualDoc(tarTypeSet2);

        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();

        parseTaskFile(inst1_path, doc1, model1);
        logger.info(String.valueOf(doc1.getGraph().size()));

        parseTaskFile(inst2_path, doc2, model2);
        logger.info(String.valueOf(doc2.getGraph().size()));

        doc1.processGraph();
        doc2.processGraph();

        Alignment refAlign = new Alignment();
        parseAlignFile(standard_path, refAlign);
        logger.info(String.valueOf(refAlign.size()));

        Alignment positives = refAlign.generatePositives();

        Alignment negetives = new Alignment();
        findNegetives(positives, doc2, negetives);

        PropPairList ppl = new PropPairList();
        findPropPair(positives, doc1, doc2, ppl);
        calInfoGain(positives, negetives, doc1, doc2, ppl);


        Alignment resultAlign = new Alignment();
        findResultAlign(doc1, doc2, ppl, resultAlign);

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

        calMetrics(refAlign, resultAlign);

        printToFile(instance_set1_file_path, doc1.graphToString());
        printToFile(instance_set2_file_path, doc2.graphToString());
        printAlign(result_file_path, head, tail, resultAlign);
        printToFile(prop_pair_list_file_path, ppl.toString());

    }

    public static void main(String[] args) throws IOException, OWLOntologyCreationException {

        init();
        matching();
    }
}
