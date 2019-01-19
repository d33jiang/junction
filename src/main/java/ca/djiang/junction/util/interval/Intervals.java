package ca.djiang.junction.util.interval;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public final class Intervals {

    //
    // Construction: Disabled

    private Intervals() {}

    //
    // Interface Implementation: Interval Overlap

    public static boolean containsOverlap(Set<Interval> intervals) {
        return containsOverlap(new TreeSet<>(intervals));
    }

    public static boolean containsOverlap(NavigableSet<Interval> intervals) {
        Iterator<Interval> iter = intervals.iterator();

        if (!iter.hasNext()) {
            // Empty
            return false;
        }

        Interval lastInterval = iter.next();
        while (iter.hasNext()) {
            Interval nextInterval = iter.next();

            if (lastInterval.overlaps(nextInterval)) {
                return true;
            }

            lastInterval = nextInterval;
        }

        return false;
    }

}
