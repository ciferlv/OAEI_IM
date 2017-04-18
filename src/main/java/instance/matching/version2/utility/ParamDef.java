package instance.matching.version2.utility;

/**
 * Created by ciferlv on 17-2-28.
 */
public class ParamDef {

    public static String[] INST1_PATH = {
            "src/main/resources/dataSet/PR/person1/person11.rdf",
            "src/main/resources/dataSet/PR/person2/person21.rdf",
            "src/main/resources/dataSet/PR/restaurants/restaurant1.rdf",
//            "src/main/resources/dataSet/UOBM_small/Abox1.nt",
            "src/main/resources/dataSet/SPIMBENCH_small/Abox1.nt"
    };

    public static String[] INST2_PATH = {
            "src/main/resources/dataSet/PR/person1/person12.rdf",
            "src/main/resources/dataSet/PR/person2/person22.rdf",
            "src/main/resources/dataSet/PR/restaurants/restaurant2.rdf",
//            "src/main/resources/dataSet/UOBM_small/Abox2.nt",
            "src/main/resources/dataSet/SPIMBENCH_small/Abox2.nt"
    };

    public static String[] SUPP1_PATH = {
            "src/main/resources/dataSet/PR/person1/ontology_people1.owl",
            "src/main/resources/dataSet/PR/person2/ontology_people1.owl",
            "src/main/resources/dataSet/PR/restaurants/ontology_restaurant1.owl"
    };

    public static String[] SUPP2_PATH = {
            "src/main/resources/dataSet/PR/person1/ontology_people2.owl",
            "src/main/resources/dataSet/PR/person2/ontology_people2.owl",
            "src/main/resources/dataSet/PR/restaurants/ontology_restaurant2.owl"
    };

    public static String[] STANDARD_PATH = {
            "src/main/resources/dataSet/PR/person1/dataset11_dataset12_goldstandard_person.xml",
            "src/main/resources/dataSet/PR/person2/dataset21_dataset22_goldstandard_person.xml",
            "src/main/resources/dataSet/PR/restaurants/restaurant1_restaurant2_goldstandard.rdf",
//            "src/main/resources/dataSet/UOBM_small/refalign.rdf",
            "src/main/resources/dataSet/SPIMBENCH_small/refalign.rdf"

    };

    public static String[] TARGET_TYPE1 = {
            "http://www.okkam.org/ontology_person1.owl#Person",
            "http://www.okkam.org/ontology_person1.owl#Person",
            "http://www.okkam.org/ontology_restaurant1.owl#Restaurant",
//            "http://semantics.crl.ibm.com/univ-bench-dl.owl#woman",
            "http://www.bbc.co.uk/ontologies/creativework/NewsItem"
    };

    public static String[] TARGET_TYPE2 = {
            "http://www.okkam.org/ontology_person2.owl#Person",
            "http://www.okkam.org/ontology_person2.owl#Person",
            "http://www.okkam.org/ontology_restaurant2.owl#Restaurant",
//            "http://semantics.crl.ibm.com/univ-bench-dl.owl#person",
            "http://www.bbc.co.uk/ontologies/creativework/NewsItem"
    };

    public static String[] AIM_LOCAL_NAME = {
            "person[0-9]-person.*",
            "person[0-9]-person.*",
            "restaurant[0-9]-restaurant.*"
    };

    public static String[] RESULT_FILE_PATH = {
            "src/main/result/person1/person1Result.xml",
            "src/main/result/person2/person2Result.xml",
            "src/main/result/restaurants/restaurantsResult.xml",
            "src/main/result/SPIMBENCH_small/SPIMBENCH_smallResult.xml",
    };
    public static String[] CORRECT_RESULT_FILE_PATH = {
            "src/main/result/person1/person1CorrectResult.txt",
            "src/main/result/person2/person2CorrectResult.txt",
            "src/main/result/restaurants/restaurantsCorrectResult.txt",
            "src/main/result/SPIMBENCH_small/SPIMBENCH_smallCorrectResult.txt",
    };
    public static String[] WRONG_RESULT_FILE_PATH = {
            "src/main/result/person1/person1WrongResult.txt",
            "src/main/result/person2/person2WrongResult.txt",
            "src/main/result/restaurants/restaurantsWrongResult.txt",
            "src/main/result/SPIMBENCH_small/SPIMBENCH_smallWrongResult.txt",
    };

    public static String[] INSTANCE_SET1_FILE_PATH = {
            "src/main/result/person1/person1Set1.txt",
            "src/main/result/person2/person2Set1.txt",
            "src/main/result/restaurants/restaurantsSet1.txt",
            "src/main/result/SPIMBENCH_small/SPIMBENCH_smallSet1.txt"
    };
    public static String[] INSTANCE_SET2_FILE_PATH = {
            "src/main/result/person1/person1Set2.txt",
            "src/main/result/person2/person2Set2.txt",
            "src/main/result/restaurants/restaurantsSet2.txt",
            "src/main/result/SPIMBENCH_small/SPIMBENCH_smallSet2.txt"
    };
    public static String[] PROPPAIRLIST_FILE_PATH = {
            "src/main/result/person1/person1PropPairList.txt",
            "src/main/result/person2/person2PropPairList.txt",
            "src/main/result/restaurants/restaurantsPropPairList.txt",
            "src/main/result/SPIMBENCH_small/SPIMBENCH_smallPropPairList.txt"
    };
    public static String[] UNFOUND_RESULT_FILE_PATH = {
            "src/main/result/person1/person1UnFoundResult.txt",
            "src/main/result/person2/person2UnFoundResult.txt",
            "src/main/result/restaurants/restaurantsUnFoundResult.txt",
            "src/main/result/SPIMBENCH_small/SPIMBENCH_smallUnFoundResult.txt",
    };

    public static final String STOPWORDS_FILE_PATH = "src/main/resources/config/stopwords_more.txt";

    public static final double SIMILAR_RATE = 0.618;

    public static final char LINK_SIGNAL = '@';

    public static final String TYPE_FULL_NAME = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

    public static final String CLASS_TYPE = "http://www.w3.org/2002/07/owl#class";

    public static final String OBJECT_TYPE_PROPERTY = "http://www.w3.org/2002/07/owl#objectproperty";

    public static final String DATA_TYPE_PROPERTY = "http://www.w3.org/2002/07/owl#datatypeproperty";

    public static final String SUBCLASSOF_FULL_NAME = "http://www.w3.org/2000/01/rdf-schema#subclassof";

    public static final String STRING_TYPE = "http://www.w3.org/2001/XMLSchema#string";

    public static final String BOOLEAN_TYPE = "http://www.w3.org/2001/XMLSchema#boolean";

    public static final String DATETIME_TYPE = "http://www.w3.org/2001/XMLSchema#dateTime";

    public static final String INTEGER_TYPE = "http://www.w3.org/2001/XMLSchema#int";

    public static final String FLOAT_TYPE = "http://www.w3.org/2001/XMLSchema#float";

    public static final double INITIAL_SAMPLE_PERSENT = 0.1;

    public static final int PROP_PAIR_SIZE = 3;

    public static final int PROP_PAIR_NUM_NEED_THRESHOLD = 2;

    public static final double PROP_PAIR_THRESHOLD = 0.65;

    public static final double ALIGN_THRESHOLD = 0.65;

    public static final double INFO_GAIN_THRESHOLD = 0.3;

    public static final int URI_TYPE_INDEX = 0;
    public static final int STRING_TYPE_INDEX = 1;
    public static final int INTEGER_TYPE_INDEX = 2;
    public static final int FLOAT_TYPE_INDEX = 3;
    public static final int DATETIME_TYPE_INDEX = 4;
    public static final int THING_TYPE_INDEX = 5;
    public static final int BOOLEAN_TYPE_INDEX=6;

    public static final boolean USE_REINFORCE = false;

    public static final boolean USE_AVERAGE_SIMI = false;

    public static final int DATASET_INDEX = 3;


}

