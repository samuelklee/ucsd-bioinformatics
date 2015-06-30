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

    public String getPattern2() { return pattern2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GappedPattern that = (GappedPattern) o;

        if (!pattern1.equals(that.pattern1)) return false;
        return pattern2.equals(that.pattern2);

    }

    @Override
    public int hashCode() {
        int result = pattern1.hashCode();
        result = 31 * result + pattern2.hashCode();
        return result;
    }

    @Override
    public int compareTo(GappedPattern o) {
        if (this.pattern1.compareTo(o.getPattern1()) == 0) {
            return this.pattern2.compareTo(o.getPattern2());
        }
        return this.pattern1.compareTo(o.getPattern1());
    }

    @Override
    public String toString() {
        return this.pattern1 + "|" + this.pattern2;
    }
}