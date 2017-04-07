package instance.matching.version2.unit;

/**
 * Created by ciferlv on 17-4-6.
 */
public class Value {

    private String value;

    private int type;

    public Value(String value, int type) {

        this.value = value;
        this.type = type;
    }

    public Value(String value) {

        this.value = value;
    }

    private int judgeType() {

        return 0;
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

    public void setType(int type) {
        this.type = type;
    }
}
