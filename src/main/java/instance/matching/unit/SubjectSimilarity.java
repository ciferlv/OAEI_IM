package instance.matching.unit;

/**
 * Created by ciferlv on 17-3-5.
 */
public class SubjectSimilarity {

    String subject1, subject2;
    double similarity;

    public SubjectSimilarity(String subject1, String subject2, double similarity) {

        this.subject1 = subject1;
        this.subject2 = subject2;
        this.similarity = similarity;
    }

    public String getSubject1() {
        return subject1;
    }

    public void setSubject1(String subject1) {
        this.subject1 = subject1;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
