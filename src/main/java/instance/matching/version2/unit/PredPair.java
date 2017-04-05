package instance.matching.version2.unit;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPair implements Comparable {

    private String pred1, pred2, token;

    private int time;
    private double infoGain;

    public PredPair(String pred1, String pred2) {

        this.pred1 = pred1;
        this.pred2 = pred2;
        time = 1;
        token = pred1 + pred2;
        infoGain = 0;
    }

    public int compareTo(Object o) {

        if (o instanceof PredPair) {

            double oInfoGain = ((PredPair) o).infoGain;
            if (this.infoGain == oInfoGain) {

                double oTime = ((PredPair) o).time;
                if (this.time < oTime) {
                    return 1;
                } else if (this.time > oTime) {
                    return -1;
                }
            } else if (this.infoGain < oInfoGain) {
                return 1;
            } else return -1;
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
        buffer.append("infoGain: " + infoGain + "\n");

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

    public void setInfoGain(double infoGain) {
        this.infoGain = infoGain;
    }
}
