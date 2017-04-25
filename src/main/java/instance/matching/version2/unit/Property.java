package instance.matching.version2.unit;

import java.util.HashSet;
import java.util.Set;

import static instance.matching.version2.utility.ParamDef.DATA_PROPERTY_INDEX;
import static instance.matching.version2.utility.ParamDef.OBJECT_PROPERTY_INDEX;

/**
 * Created by ciferlv on 17-4-24.
 */
public class Property {

    String propName = null;
    private Set<String> domain = new HashSet<String>();
    private Set<String> range = new HashSet<String>();

    private int type;

    public Property(String propName, int type) {

        this.type = type;
        this.propName = propName;
    }


    public boolean containsRange(String myRange) {

        if (range.contains(myRange)) return true;
        else return false;
    }

    public boolean containsDomain(String myDomain) {

        if (domain.contains(myDomain)) {
            return true;
        } else return false;
    }

    public void addRange(String myRange) {

        range.add(myRange);
    }

    public void addDomain(String myDomain) {

        domain.add(myDomain);
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("PropName:\n" + propName + "\n");

        if (type == DATA_PROPERTY_INDEX) {

            buffer.append("DATA_PROPERTY_TYPE\n");
        } else if(type == OBJECT_PROPERTY_INDEX){

            buffer.append("OBJECT_PROPERTY_TYPE\n");
        }

        buffer.append("Domain:\n");

        for (String dom : domain) {

            buffer.append(dom + "\n");
        }

        buffer.append("Range:\n");

        for (String ran : range) {

            buffer.append(ran + "\n");
        }
        buffer.append("\n");

        return String.valueOf(buffer);
    }

}
