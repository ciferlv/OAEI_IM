package instance.matching.version2.fileParser;

import instance.matching.version2.unit.PropertyDetail;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static instance.matching.version2.utility.ParamDef.*;
//import java.util.stream.Stream;

/**
 * Created by ciferlv on 17-4-24.
 */
public class MetaDataFileParser {

    private static Logger logger = LoggerFactory.getLogger(MetaDataFileParser.class);

    private static void addToPropDetailMap(boolean isRange,
                                           int propType,
                                           String propName,
                                           String propVal,
                                           Map<String, PropertyDetail> propDetail) {

        if (isRange) {
            if (propDetail.containsKey(propName)) {

                PropertyDetail myProp = propDetail.get(propName);
                myProp.addRange(propVal);
            } else {
                PropertyDetail myProp = new PropertyDetail(propName, propType);
                myProp.addRange(propVal);
                propDetail.put(propName, myProp);
            }
        } else {
            if (propDetail.containsKey(propName)) {

                PropertyDetail myProp = propDetail.get(propName);
                myProp.addDomain(propVal);
            } else {
                PropertyDetail myProp = new PropertyDetail(propName, propType);
                myProp.addDomain(propVal);
                propDetail.put(propName, myProp);
            }
        }
    }

    public static void parseMetaDataPR(String filePath, Map<String, PropertyDetail> propDetail) throws OWLOntologyCreationException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        File file = new File(filePath);

        OWLOntology o = manager.loadOntologyFromOntologyDocument(file);

        Set<OWLAxiom> dataProp = o.axioms().collect(Collectors.toSet());

        for (OWLAxiom axiom : dataProp) {

            String type = axiom.getAxiomType().getName().toLowerCase();
            int type_index;
            boolean isRange;
            if (type.equals("objectpropertydomain")) {

                isRange = false;
                type_index = OBJECT_PROPERTY_INDEX;
            } else if (type.equals("datapropertydomain")) {

                isRange = false;
                type_index = DATA_PROPERTY_INDEX;
            } else if (type.equals("datapropertyrange")) {

                isRange = true;
                type_index = DATA_PROPERTY_INDEX;
            } else if (type.equals("objectpropertyrange")) {

                isRange = true;
                type_index = OBJECT_PROPERTY_INDEX;
            } else continue;

            Set<OWLEntity> entitySet = axiom.signature().collect(Collectors.toSet());

            String propName = null, propVal = null;

            for (OWLEntity entity : entitySet) {

                String entityType = entity.getEntityType().getName().toLowerCase().toLowerCase();

                if (entityType.equals("dataproperty") || entityType.equals("objectproperty")) {

                    propName = entity.getIRI().getIRIString();
                } else {
                    propVal = entity.getIRI().getIRIString();
                }

                if (propName != null && propVal != null) {
                    addToPropDetailMap(isRange, type_index, propName, propVal, propDetail);
                }
            }
        }
    }

    public static void parseMetaDataSPIM(String filePath) {

        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open(filePath);
        if (in == null) {
            throw new IllegalArgumentException("File: " + filePath + " not found");
        }

        if (filePath.endsWith(".nt")) {
            model.read(in, "", "N3");
        } else {
            model.read(in, "");
        }

        StmtIterator iter = model.listStatements();

        while (iter.hasNext()) {

            Statement stmt = iter.nextStatement();
            Resource sub = stmt.getSubject();
            Property prop = stmt.getPredicate();
            RDFNode val = stmt.getObject();

            if (prop.getLocalName().toLowerCase().equals("disjointwith")) {

                disjoint.addDisjoint(sub.getURI(), val.asResource().getURI());
            }
            if (prop.getLocalName().toLowerCase().equals("subclassof")) {

                subClass.addsSubClass(val.asResource().getURI(),sub.getURI());
            }

        }
    }
}
