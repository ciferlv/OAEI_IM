package instance.matching.version2.fileParser;

import instance.matching.version2.unit.Property;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static instance.matching.version2.utility.ParamDef.DATA_PROPERTY_INDEX;
import static instance.matching.version2.utility.ParamDef.OBJECT_PROPERTY_INDEX;
//import java.util.stream.Stream;

/**
 * Created by ciferlv on 17-4-24.
 */
public class MetaDataFileParser {

    private static Logger logger = LoggerFactory.getLogger(MetaDataFileParser.class);

    private static void addToPropDetailMap(boolean isRange,
                                           int PropType,
                                           String propName,
                                           String propVal,
                                           Map<String, Property> propDetail) {

        if (isRange) {
            if (propDetail.containsKey(propName)) {

                Property myProp = propDetail.get(propName);
                myProp.addRange(propVal);
            } else {
                Property myProp = new Property(propName, PropType);
                myProp.addRange(propVal);
                propDetail.put(propName, myProp);
            }
        } else {
            if (propDetail.containsKey(propName)) {

                Property myProp = propDetail.get(propName);
                myProp.addDomain(propVal);
            } else {
                Property myProp = new Property(propName, PropType);
                myProp.addDomain(propVal);
                propDetail.put(propName, myProp);
            }
        }
    }

    public static void parseMetaDataPR(String filePath, Map<String, Property> propDetail) throws OWLOntologyCreationException {

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
                logger.info(entityType);
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

    public static void parseMetaDataSPIM(String filePath, Map<String, Property> propDetail) {

    }
}
