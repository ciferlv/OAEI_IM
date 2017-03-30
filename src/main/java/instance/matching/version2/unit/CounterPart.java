package instance.matching.version2.unit;

/**
 * Created by xinzelv on 17-3-27.
 */
public class CounterPart {

    private String subject1, subject2;

    public CounterPart() {
    }

    public CounterPart(String uri1, String uri2) {

        subject1 = uri1;
        subject2 = uri2;
    }

    @Override
    public boolean equals(Object obj) {

        if( obj instanceof CounterPart )
        {
            CounterPart cp = (CounterPart) obj;
            if (cp.getSubject1().equals(subject1)
                    && cp.getSubject2().equals(subject2)) {
                return true;
            }
            if (cp.getSubject2().equals(subject1)
                    && cp.getSubject1().equals(subject2)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("subject1: " + subject1 + "\n");
        buffer.append("subject2: " + subject2 + "\n");
        return String.valueOf(buffer);
    }

    public synchronized String getSubject1() {
        return subject1;
    }

    public synchronized String getSubject2() {
        return subject2;
    }
}
