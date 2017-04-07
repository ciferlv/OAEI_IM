package instance.matching.version2.unit;

/**
 * Created by xinzelv on 17-3-27.
 */
public class CounterPart {

    private String sub1, sub2;

    public CounterPart() {
    }

    public CounterPart(String sub1, String sub2) {

        this.sub1 = sub1;
        this.sub2 = sub2;
    }

    @Override
    public boolean equals(Object obj) {

        if( obj instanceof CounterPart )
        {
            CounterPart cp = (CounterPart) obj;
            if (cp.getSub1().equals(sub1)
                    && cp.getSub2().equals(sub2)) {
                return true;
            }
            if (cp.getSub2().equals(sub1)
                    && cp.getSub1().equals(sub2)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("sub1: " + sub1 + "\n");
        buffer.append("sub2: " + sub2 + "\n");
        return String.valueOf(buffer);
    }

    public synchronized String getSub1() {
        return sub1;
    }

    public synchronized String getSub2() {
        return sub2;
    }
}
