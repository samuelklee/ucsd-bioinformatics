package ucsd;

public class GappedPattern implements Comparable<GappedPattern> {
    private String pattern1;
    private String pattern2;

    public GappedPattern(String pattern1, String pattern2) {
        this.pattern1 = pattern1;
        this.pattern2 = pattern2;
    }

    public String getPattern1() {
        return pattern1;
    }

    public String getPattern2() {
        return pattern2;
    }

    @Override
    public int compareTo(GappedPattern o) {
        if (this.pattern1.compareTo(o.getPattern1()) == 0) {
            return this.pattern2.compareTo(o.getPattern2());
        }
        return this.pattern1.compareTo(o.getPattern1());
    }
}