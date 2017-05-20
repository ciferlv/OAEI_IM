package instance.matching.version2.utility;

import instance.matching.version2.unit.Disjoint;
import instance.matching.version2.unit.PropertyDetail;
import instance.matching.version2.unit.SubClass;

import java.util.*;

/**
 * Created by ciferlv on 17-4-25.
 */
public class ParamDef {

    public static int confFileIndex;

    public static final String[] CONF_FILE_PATH = {
            "src/main/resources/config/person1.properties",
            "src/main/resources/config/person2.properties",
            "src/main/resources/config/restaurants.properties",
            "src/main/resources/config/spimbench_small.properties",
            "src/main/resources/config/spimbench_large.properties"
    };

    public static String inst1_path;
    public static String inst2_path;
    public static String supp1_path;
    public static String supp2_path;
    public static String standard_path;
    public static String result_file_path;
    public static String metrics_file_path;
    public static String correct_result_file_path;
    public static String wrong_result_file_path;
    public static String unfound_result_file_path;
    public static String instance_set1_file_path;
    public static String instance_set2_file_path;
    public static String prop_pair_list_file_path;
    public static String stopwords_file_path;

    public static boolean use_reinforce;
    public static boolean use_average_simi;

    public static Set<String> tarTypeSet1 = new HashSet<String>();
    public static Set<String> tarTypeSet2 = new HashSet<String>();

    public static Set<String> stopWordSet = new HashSet<String>();

    public static Map<String, PropertyDetail> propDetailMap1 = new HashMap<String, PropertyDetail>();
    public static Map<String, PropertyDetail> propDetailMap2 = new HashMap<String, PropertyDetail>();

    public static Disjoint disjoint = new Disjoint();
    public static SubClass subClass = new SubClass();

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

    public static final String DATE_TYPE = "http://www.w3.org/2001/XMLSchema#date";

    public static final String INTEGER_TYPE = "http://www.w3.org/2001/XMLSchema#int";

    public static final String FLOAT_TYPE = "http://www.w3.org/2001/XMLSchema#float";

    public static final double INITIAL_SAMPLE_PERSENT = 0.1;

    public static final int PROP_PAIR_SIZE = 3;

    public static int PROP_PAIR_NUM_NEED_THRESHOLD = 3;

    public static final double PROP_PAIR_THRESHOLD = 0.65;

    public static final double ALIGN_THRESHOLD = 0.65;

    public static final double INFO_GAIN_THRESHOLD = 0.3;

    public static final int URI_TYPE_INDEX = 0;
    public static final int STRING_TYPE_INDEX = 1;
    public static final int INTEGER_TYPE_INDEX = 2;
    public static final int FLOAT_TYPE_INDEX = 3;
    public static final int DATETIME_TYPE_INDEX = 4;
    public static final int THING_TYPE_INDEX = 5;
    public static final int BOOLEAN_TYPE_INDEX = 6;
    public static final int DATE_TYPE_INDEX = 7;
    public static final int OBJECT_PROPERTY_INDEX = 8;
    public static final int DATA_PROPERTY_INDEX = 9;

}
