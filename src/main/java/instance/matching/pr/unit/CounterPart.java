package instance.matching.pr.unit;

/**
 * Created by xinzelv on 17-3-27.
 */
public class CounterPart {

    String subject1, subject2;

    public CounterPart() {}

    public CounterPart(String uri1, String uri2) {

        subject1 = uri1;
        subject2 = uri2;
    }

    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("subject1: " + subject1 + "\n");
        buffer.append("subject2: " + subject2 + "\n");
        return String.valueOf(buffer);
    }

}
