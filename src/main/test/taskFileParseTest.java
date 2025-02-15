import instance.matching.unit.VirtualDoc;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import static instance.matching.fileParser.TaskFileParser.parseTaskFile;

/**
 * Created by ciferlv on 17-4-16.
 */
public class taskFileParseTest {

    private static Logger logger = LoggerFactory.getLogger(taskFileParseTest.class);

    public static void main(String [] args) throws FileNotFoundException {

        Set<String> targetType1 = new HashSet<String>();
        targetType1.add("http://www.okkam.org/ontology_person1.owl#Person");
        targetType1.add("http://www.okkam.org/ontology_restaurant1.owl#Restaurant");
        VirtualDoc vd = new VirtualDoc(targetType1);

        Model model =  ModelFactory.createDefaultModel();

        String filePath = "src/main/resources/dataSet/UOBM_small/Abox2.nt";

        parseTaskFile(filePath,vd,model);

        PrintWriter pw = new PrintWriter(new FileOutputStream("target/format1.txt"));


        pw.write(vd.graphToString());
    }
}


















