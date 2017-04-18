package instance.matching.version1;

import instance.matching.version1.unit.SubjectSimilarity;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static instance.matching.version2.nlp.EditDistance.editDistance;
import static instance.matching.version1.helper.LinkSubjectStringHelper.linkSubjectString;
import static instance.matching.version2.utility.ParamDef.SIMILAR_RATE;

/**
 * Created by ciferlv on 17-3-4.
 */
public class InsMatching {

    Logger logger = LoggerFactory.getLogger(InsMatching.class);

    private String rdf1Path, rdf2Path, owl1Path, owl2Path, standardPath, aimLocalName;

    private Model rdf1Model = ModelFactory.createDefaultModel();
    private Model rdf2Model = ModelFactory.createDefaultModel();

    private InputStream rdf1In = null;
    private InputStream rdf2In = null;

    private Map<String, Map<String, String>> rdf1EntityMap = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> rdf2EntityMap = new HashMap<String, Map<String, String>>();

    private List<SubjectSimilarity> resultList = new ArrayList<SubjectSimilarity>();
    private List<SubjectSimilarity> linkedSubjectList = new ArrayList<SubjectSimilarity>();

    private Map<String, SubjectSimilarity> mapCoreferentSubject = new HashMap<String, SubjectSimilarity>();

    private List<String> subjectSet1 = new ArrayList<String>();
    private List<String> subjectSet2 = new ArrayList<String>();

    private int similaritySum;

    InsMatching(String r1Path, String r2Path, String o1Path, String o2Path, String stanPath, String aLocalName) {

        rdf1Path = r1Path;
        rdf2Path = r2Path;
        owl1Path = o1Path;
        owl2Path = o2Path;
        standardPath = stanPath;
        aimLocalName = aLocalName;

        similaritySum = 0;
    }

    private List<String> extractEntity(Model model, Map<String, Map<String, String>> entityMap) throws FileNotFoundException {

        StmtIterator iter = model.listStatements();
        List<String> aim_subject = new ArrayList<String>();

        while (iter.hasNext()) {

            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            String subjectString = subject.toString();
            String predicateString = predicate.getLocalName();
            String objectString = object.toString();

            String subject_localname = subject.getLocalName();
            if (!aim_subject.contains(subjectString) && subject_localname.toLowerCase().matches(aimLocalName)) {
                aim_subject.add(subjectString);
            }

            if (entityMap.containsKey(subjectString)) {

                Map<String, String> tempMap = entityMap.get(subjectString);
                entityMap.remove(subjectString);
                tempMap.put(predicateString, objectString);
                entityMap.put(subjectString, tempMap);

            } else {

                Map<String, String> predicate_object = new HashMap<String, String>();
                predicate_object.put(predicateString, objectString);
                entityMap.put(subjectString, predicate_object);
            }
        }

        return aim_subject;
    }

    public void readAndGetEntity() throws FileNotFoundException {

        rdf1In = FileManager.get().open(rdf1Path);
        rdf2In = FileManager.get().open(rdf2Path);

        if (rdf1In == null) {
            throw new IllegalArgumentException("File: " + rdf1Path + " not found");
        }
        if (rdf2In == null) {
            throw new IllegalArgumentException("File: " + rdf2Path + " not found");
        }

        rdf1Model.read(rdf1In, "");
        rdf2Model.read(rdf2In, "");

        subjectSet1 = extractEntity(rdf1Model, rdf1EntityMap);
        subjectSet2 = extractEntity(rdf2Model, rdf2EntityMap);
    }

    private void alignment(String subject1, String subject2, double similarity) {

        SubjectSimilarity tempSubSim1 = new SubjectSimilarity(subject1, subject2, similarity);
        linkedSubjectList.add(tempSubSim1);

        mapCoreferentSubject.put(linkSubjectString(subject1, subject2), tempSubSim1);

        String subject1LocalName = rdf1Model.getResource(subject1).getLocalName().toLowerCase();
        String subject2LocalName = rdf2Model.getResource(subject2).getLocalName().toLowerCase();

//        如果是所需要的person或restaurant名字。
        if (subject1LocalName.matches(aimLocalName) && subject2LocalName.matches(aimLocalName)) {

            SubjectSimilarity tempSubSim2 = new SubjectSimilarity(subject1, subject2, similarity);
            resultList.add(tempSubSim2);
            mapCoreferentSubject.put(linkSubjectString(subject1, subject2), tempSubSim2);
        }

    }

    public void compare() {

//        取subjectSet1中的一个subject
        for (String key1 : subjectSet1) {
//        取subjectSet2中的一个subject
            for (String key2 : subjectSet2) {

                similaritySum = 0;
                double similarValue = countSimilarity(rdf1EntityMap.get(key1), rdf2EntityMap.get(key2));
                if (similarValue > similaritySum * SIMILAR_RATE) {
                    alignment(key1, key2, similarValue);
                }
            }
        }
    }

    //    用于计算object和subject的相似度，从subject里找到和object最相似的值的相似程度
    private double findDetailSimilarity(String predicate, String object, Map<String, String> predicate_subject, boolean isRDF1) {

        double maxSimilarity = 0;

        if (predicate_subject.containsKey(predicate)) {

            String getObj = predicate_subject.get(predicate);
            maxSimilarity = Math.max(maxSimilarity, editDistance(object, getObj));

        } else {
            for (String key : predicate_subject.keySet()) {

//                不比较type(例:<rdf:type rdf:resource="http://www.okkam.org/ontology_restaurant1.owl#Restaurant"/>)
                if (key.equals("type")) continue;

                String getObj = predicate_subject.get(key);

                if (isRDF1) {
                    if (rdf1EntityMap.containsKey(getObj)) {
                        maxSimilarity = Math.max(maxSimilarity, findDetailSimilarity(predicate, object, rdf1EntityMap.get(getObj), isRDF1));
                    } else {
                        maxSimilarity = Math.max(maxSimilarity, editDistance(object, getObj));
                    }
                } else {
                    if (rdf2EntityMap.containsKey(getObj)) {
                        maxSimilarity = Math.max(maxSimilarity, findDetailSimilarity(predicate, object, rdf2EntityMap.get(getObj), isRDF1));
                    } else {
                        maxSimilarity = Math.max(maxSimilarity, editDistance(object, getObj));
                    }
                }
            }
        }

        return maxSimilarity;
    }

    private double countSimilarity(Map<String, String> pre_obj1, Map<String, String> pre_obj2) {

        double tempSimilarity = 0;

        for (Object eachKey : pre_obj1.keySet()) {

//            不比较type(例:<rdf:type rdf:resource="http://www.okkam.org/ontology_restaurant1.owl#Restaurant"/>)
            if (eachKey.equals("type")) continue;

            if (pre_obj2.containsKey(eachKey.toString())) {

                String object1 = pre_obj1.get(eachKey.toString());
                String object2 = pre_obj2.get(eachKey.toString());

                boolean isContainedRdf1 = rdf1EntityMap.containsKey(object1);
                boolean isContainedRdf2 = rdf2EntityMap.containsKey(object2);

                if (isContainedRdf1 && isContainedRdf2) {

                    String linkedString = linkSubjectString(object1, object2);
                    if (mapCoreferentSubject.containsKey(linkedString)) {

                        tempSimilarity += mapCoreferentSubject.get(linkedString).getSimilarity();
                    } else {

                        double similarValue = countSimilarity(rdf1EntityMap.get(object1), rdf2EntityMap.get(object2));

                        tempSimilarity += similarValue;

//                        if (similarValue > ParamDef.SIMILAR_RATE) {
//
//                            alignment(object1, object2, similarValue);
//                        }
                    }
                } else if (!isContainedRdf1 && !isContainedRdf2) {

                    tempSimilarity += editDistance(object1, object2);
                    similaritySum++;

                } else if (isContainedRdf1 && !isContainedRdf2) {

                    tempSimilarity += findDetailSimilarity(eachKey.toString(), object2, rdf1EntityMap.get(object1), true);
                    similaritySum++;
                } else if (!isContainedRdf1 && isContainedRdf2) {

                    tempSimilarity += findDetailSimilarity(eachKey.toString(), object1, rdf2EntityMap.get(object2), false);
                    similaritySum++;
                }

            } else {

//                tempSimilarity += findDetailSimilarity(eachKey.toString(), pre_obj1.get(eachKey), pre_obj2, false);
//                similaritySum++;
            }
        }
        return tempSimilarity;
    }


    public Map<String, Map<String, String>> getRdf1EntityMap() {
        return rdf1EntityMap;
    }

    public void setRdf1EntityMap(Map<String, Map<String, String>> rdf1EntityMap) {
        this.rdf1EntityMap = rdf1EntityMap;
    }

    public Map<String, Map<String, String>> getRdf2EntityMap() {
        return rdf2EntityMap;
    }

    public void setRdf2EntityMap(Map<String, Map<String, String>> rdf2EntityMap) {
        this.rdf2EntityMap = rdf2EntityMap;
    }

    public List<String> getSubjectSet1() {
        return subjectSet1;
    }

    public void setSubjectSet1(List<String> subjectSet1) {
        this.subjectSet1 = subjectSet1;
    }

    public List<String> getSubjectSet2() {
        return subjectSet2;
    }

    public void setSubjectSet2(List<String> subjectSet2) {
        this.subjectSet2 = subjectSet2;
    }

    public List<SubjectSimilarity> getResultList() {
        return resultList;
    }

    public void setResultList(List<SubjectSimilarity> resultList) {
        this.resultList = resultList;
    }

    public List<SubjectSimilarity> getLinkedSubjectList() {
        return linkedSubjectList;
    }

    public void setLinkedSubjectList(List<SubjectSimilarity> linkedSubjectList) {
        this.linkedSubjectList = linkedSubjectList;
    }

    public Map<String, SubjectSimilarity> getMapCoreferentSubject() {
        return mapCoreferentSubject;
    }

    public void setMapCoreferentSubject(Map<String, SubjectSimilarity> mapCoreferentSubject) {
        this.mapCoreferentSubject = mapCoreferentSubject;
    }
}
