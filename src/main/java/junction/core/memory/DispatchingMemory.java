package junction.core.memory;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import junction.core.JunMemory;
import junction.util.interval.Interval;
import junction.util.interval.Intervals;

public class DispatchingMemory<Word extends Number> implements JunMemory<Word> {

    //
    // State: Dispatch Table

    private final NavigableMap<Interval, JunMemory<Word>> dispatchTable;

    //
    // State: Backing Memory

    private final JunMemory<Word> backingMemory;

    //
    // Construction

    public DispatchingMemory(Map<Interval, JunMemory<Word>> dispatchTable) {
        this(dispatchTable, null);
    }

    public DispatchingMemory(Map<Interval, JunMemory<Word>> dispatchTable, JunMemory<Word> backingMemory) {
        if (dispatchTable == null)
            throw new NullPointerException("dispatchTable is null");

        this.dispatchTable = new TreeMap<>(dispatchTable);

        if (Intervals.containsOverlap(this.dispatchTable.keySet())) {
            throw new IllegalArgumentException("dispatchTable contains overlapping intervals");
        }

        this.backingMemory = backingMemory;
    }

    //
    // Implementation: Segment Lookup

    protected static final class LookupResult<Word extends Number> {
        private final JunMemory<Word> segment;
        private final int segmentAddr;

        public JunMemory<Word> segment() {
            return segment;
        }

        public int segmentAddr() {
            return segmentAddr;
        }

        private LookupResult(JunMemory<Word> segment, int segmentAddr) {
            this.segment = segment;
            this.segmentAddr = segmentAddr;
        }
    }

    protected LookupResult<Word> lookup(int addr) {
        Map.Entry<Interval, JunMemory<Word>> addrSegment = dispatchTable.lowerEntry(new Interval(addr + 1));

        if (!addrSegment.getKey().contains(addr))
            return null;

        return new LookupResult<>(addrSegment.getValue(), addr - addrSegment.getKey().lower());
    }

    //
    // Interface Implementation: Memory Operations

    @Override
    public Word loadWord(int addr) {
        LookupResult<Word> lookupResult = lookup(addr);

        if (lookupResult != null) {
            return lookupResult.segment().loadWord(lookupResult.segmentAddr());
        }
        // No segment is addressed by addr

        if (backingMemory != null) {
            return backingMemory.loadWord(addr);
        }
        // No backing memory is available to refer to

        throw new IndexOutOfBoundsException("No segment is addressed by addr and no backing memory is available");
    }

    @Override
    public void storeWord(int addr, Word value) {
        LookupResult<Word> lookupResult = lookup(addr);

        if (lookupResult != null) {
            lookupResult.segment().storeWord(lookupResult.segmentAddr(), value);
        }
        // No segment is addressed by addr

        if (backingMemory != null) {
            backingMemory.storeWord(addr, value);
        }
        // No backing memory is available to refer to

        throw new IndexOutOfBoundsException("No segment is addressed by addr and no backing memory is available");
    }

}
