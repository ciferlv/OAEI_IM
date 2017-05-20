import instance.matching.unit.VirtualDoc;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.HashSet;
import java.util.Set;

import static instance.matching.fileParser.TaskFileParser.parseTaskFile;

/**
 * Created by ciferlv on 17-4-16.
 */
public class filterDataSetTest {

    public static void main(String [] args) {

        String filePath = "src/main/resources/dataSet/sabine_linking/sabine_source.owl";
        Model model = ModelFactory.createDefaultModel();

        Set<String> targetType1 = new HashSet<String>();
        targetType1.add("http://big.csr.unibo.it/sabine.owl#Topic".toLowerCase());
        VirtualDoc vd = new VirtualDoc(targetType1);

        parseTaskFile(filePath,vd,model);

        vd.processGraph();
    }
}
