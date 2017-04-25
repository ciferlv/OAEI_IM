import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static instance.matching.version2.fileParser.MetaDataFileParser.parseMetaDataPR;
import static instance.matching.version2.utility.ParamDef.propDetail1;

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


        parseMetaDataPR("src/main/resources/dataSet/PR/person2/ontology_people2.owl", propDetail1);
//        parseMetaDataPR("src/main/resources/dataSet/PR/restaurants/ontology_restaurant2.owl", propDetail1);
        for (String key : propDetail1.keySet()) {
            System.out.println(propDetail1.get(key).toString());
        }

    }
}
