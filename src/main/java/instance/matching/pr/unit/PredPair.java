package instance.matching.pr.unit;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPair implements Comparable {

    private String pred1, pred2, token;

    private int time;

    public PredPair(String pred1, String pred2) {

        this.pred1 = pred1;
        this.pred2 = pred2;
        time = 0;
        token = pred1 + pred2;
    }

    public int compareTo(Object o) {

        if (o instanceof PredPair) {

            if (this.time < ((PredPair) o).time) {
                return 1;
            } else if (this.time > ((PredPair) o).time) {
                return -1;
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof PredPair) {
            if (((PredPair) obj).getToken().equals(this.token)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("pred1: " + pred1 + "\n");
        buffer.append("pred2: " + pred2 + "\n");
        buffer.append("token: " + token + "\n");
        buffer.append("time: " + time + "\n");

        return String.valueOf(buffer);
    }


    public String getToken() {
        return token;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPred1() {
        return pred1;
    }

    public String getPred2() {
        return pred2;
    }
}
