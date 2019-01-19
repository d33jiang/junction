package junction.util.interval;

public final class Interval implements Comparable<Interval> {

    //
    // Configuration: Bounds

    private final int lower, upper;

    public int lower() {
        return lower;
    }

    public int upper() {
        return upper;
    }

    //
    // Construction

    public Interval(int pos) {
        this(pos, pos);
    }

    public Interval(int lower, int upper) {
        if (upper < lower)
            throw new IllegalArgumentException("The value of upper must be at least that of lower");
        this.lower = lower;
        this.upper = upper;
    }

    //
    // Core Support: Comparable<Interval>

    @Override
    public int compareTo(Interval other) {
        if (this.lower != other.lower) {
            return this.lower - other.lower;
        } else {
            return this.upper - other.upper;
        }
    }

    //
    // Interface Implementation: Test Contains Point

    public boolean contains(int i) {
        return lower <= i && i < upper;
    }

    //
    // Interface Implementation: Test Overlaps Interval

    public boolean overlaps(Interval other) {
        if (this.upper <= other.lower) {
            // Disjoint: this, other
            return false;
        }

        if (this.lower >= other.upper) {
            // Disjoint: other, this
            return false;
        }

        return true;
    }

    //
    // Interface Implementation: Test Disjoint Interval

    public boolean isDisjoint(Interval other) {
        return !overlaps(other);
    }

}
