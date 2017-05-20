import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static instance.matching.fileParser.MetaDataFileParser.parseMetaDataSPIM;
import static instance.matching.utility.ParamDef.disjoint;
import static instance.matching.utility.ParamDef.subClass;

/**
 * Created by ciferlv on 17-4-24.
 */
@SuppressWarnings("ALL")
public class readOntologyFileTest {

    private static Logger logger = LoggerFactory.getLogger(readOntologyFileTest.class);

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {

//        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//
//        File file = new File("src/main/resources/dataSet/PR/person1/ontology_people1.owl");
//
//        OWLOntology o = manager.loadOntologyFromOntologyDocument(file);
//
//        OWLAxiom[] owlAxioms =  o.axioms()
//                .filter(axiom -> axiom.getAxiomType().toString().equals("SubClassOf"))
//                .toArray(size -> new OWLAxiom[size]);
//
//        for (int i = 0; i < owlAxioms.length; i ++) {
//
//            Stream<OWLEntity> owlEntityStream = owlAxioms[i].signature();
//            owlEntityStream.forEach(entity -> System.out.println(entity.getIRI()));
//            System.out.println("**************");
//        }


//        parseMetaDataPR("src/main/resources/dataSet/PR/person2/ontology_people2.owl", propDetailMap1);
//        parseMetaDataPR("src/main/resources/dataSet/PR/restaurants/ontology_restaurant2.owl", propDetailMap1);
//        for (String key : propDetailMap1.keySet()) {
//            System.out.println(propDetailMap1.get(key).toString());
//        }


        String filePath = "src/main/resources/dataSet/SPIMBENCH_small/Tbox2.nt";
        parseMetaDataSPIM(filePath);

        logger.info(subClass.toString());
        logger.info(disjoint.toString());

    }
}
