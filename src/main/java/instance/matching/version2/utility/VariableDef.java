package instance.matching.version2.utility;

/**
 * Created by ciferlv on 17-2-28.
 */
public class VariableDef {

    public static String[] RDF1_PATH = {
            "dataSet/PR/person1/person11.rdf",
            "dataSet/PR/person2/person21.rdf",
            "dataSet/PR/restaurants/restaurant1.rdf"
    };

    public static String[] RDF2_PATH = {
            "dataSet/PR/person1/person12.rdf",
            "dataSet/PR/person2/person22.rdf",
            "dataSet/PR/restaurants/restaurant2.rdf"
    };

    public static String[] OWL1_PATH = {
            "dataSet/PR/person1/ontology_people1.owl",
            "dataSet/PR/person2/ontology_people1.owl",
            "dataSet/PR/restaurants/ontology_restaurant1.owl"
    };

    public static String[] OWL2_PATH = {
            "dataSet/PR/person1/ontology_people2.owl",
            "dataSet/PR/person2/ontology_people2.owl",
            "dataSet/PR/restaurants/ontology_restaurant2.owl"
    };

    public static String[] STANDARD_PATH = {
            "src/main/resources/dataSet/PR/person1/dataset11_dataset12_goldstandard_person.xml",
            "src/main/resources/dataSet/PR/person2/dataset21_dataset22_goldstandard_person.xml",
            "src/main/resources/dataSet/PR/restaurants/restaurant1_restaurant2_goldstandard.rdf"
    };

    public static String[] AIM_LOCAL_NAME = {
            "person[0-9]-person.*",
            "person[0-9]-person.*",
            "restaurant[0-9]-restaurant.*"
    };

    public static String[] RESULT_FILE_PATH = {
            "target/person1/person1Result.xml",
            "target/person2/person2Result.xml",
            "target/restaurant/restaurantResult.xml",
            "target/sabine_linguistic/sabine_linguisticResult.xml",
            "target/sabine_linking/sabine_linkingResult.xml"
    };
    public static String[] CORRECT_RESULT_FILE_PATH = {
            "target/person1/person1CorrectResult.txt",
            "target/person2/person2CorrectResult.txt",
            "target/restaurant/CorrectResult.txt",
            "target/sabine_linguistic/sabine_linguisticCorrectResult.txt",
            "target/sabine_linking/sabine_linkingCorrectResult.txt"
    };
    public static String[] WRONG_RESULT_FILE_PATH = {
            "target/person1/person1WrongResult.txt",
            "target/person2/person2WrongResult.txt",
            "target/restaurant/restaurantWrongResult.txt",
            "target/sabine_linguistic/sabine_linguisticWrongResult.txt",
            "target/sabine_linking/sabine_linkingWrongResult.txt"
    };

    public static String[] UNFOUND_RESULT_FILE_PATH = {
            "target/person1/person1UnFoundResult.txt",
            "target/person2/person2UnFoundResult.txt",
            "target/restaurant/restaurantUnFoundResult.txt",
            "target/sabine_linguistic/sabine_linguisticUnFoundResult.txt",
            "target/sabine_linking/sabine_linkingUnFoundResult.txt",
    };

    public static final double SIMILAR_RATE = 0.618;

    public static final char LINK_SIGNAL = '@';

    public static final String CLASS_TYPE = "http://www.w3.org/2002/07/owl#class";

    public static final String OBJECT_TYPE_PROPERTY = "http://www.w3.org/2002/07/owl#objectproperty";

    public static final String DATA_TYPE_PROPERTY = "http://www.w3.org/2002/07/owl#datatypeproperty";

    public static final double initialSamplePersent = 0.2;

    public static final int predPairSize = 3;

    public static final double predPairThreshold = 0.65;

    public static final double alignThreshold = 0.65;

}

