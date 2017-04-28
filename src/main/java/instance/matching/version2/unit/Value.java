package instance.matching.version2.unit;

import static instance.matching.version2.utility.ParamDef.*;
import static instance.matching.version2.utility.ParamDef.DATETIME_TYPE_INDEX;

/**
 * Created by ciferlv on 17-4-6.
 */
public class Value {

    private String value;

    private String localName;

    private int type = -1;

    public Value(String value, String localName, int type) {

        this.value = value;
        this.type = type;
        this.localName = localName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {

        return type;
    }

    public String getTypeName() {

        String mT = "NULL";
        if (type == URI_TYPE_INDEX) {
            mT = "URI";
        } else if (type == THING_TYPE_INDEX) {
            mT = "THING";
        } else if (type == INTEGER_TYPE_INDEX) {
            mT = "INTEGER";
        } else if (type == FLOAT_TYPE_INDEX) {
            mT = "FLOAT";
        } else if (type == STRING_TYPE_INDEX) {
            mT = "STRING";
        } else if (type == BOOLEAN_TYPE_INDEX) {
            mT = "BOOLEAN";
        } else if (type == DATE_TYPE_INDEX) {
            mT = "DATE";
        } else if (type == DATETIME_TYPE_INDEX) {
            mT = "DATETIME";
        }
        return mT;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocalName() {
        return localName;
    }
}
