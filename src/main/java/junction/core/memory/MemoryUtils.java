package junction.core.memory;

import junction.core.JunMemory;

public final class MemoryUtils {

    //
    // Construction

    private MemoryUtils() {}

    //
    // Static Support: Transfer

    public static <Word extends Number> void copyIn(Word[] src, int srcPos,
            JunMemory<Word> dst, int dstAddr, int len) {
        for (int i = 0; i < len; i++) {
            dst.storeWord(dstAddr + i, src[srcPos + i]);
        }
    }

    public static <Word extends Number> void copyOut(JunMemory<Word> src, int srcAddr,
            Word[] dst, int dstPos, int len) {
        for (int i = 0; i < len; i++) {
            dst[dstPos + i] = src.loadWord(srcAddr + i);
        }
    }

    //
    // Static Support: Load

    public static <Word extends Number> MemoryLoader<Word> loader() {
        return new MemoryLoader<Word>();
    }

    public static final class MemoryLoader<Word extends Number> {

        //
        // Configuration: Nested

        private final MemoryLoader<Word> nested;

        //
        // Configuration: Copy In

        private final Word[] src;
        private final int dstAddr;

        //
        // Construction

        @SuppressWarnings("unchecked")
        private MemoryLoader() {
            this((Word[]) new Object[0], 0);
        }

        private MemoryLoader(Word[] src, int dstAddr) {
            this(null, src, dstAddr);
        }

        private MemoryLoader(MemoryLoader<Word> nested, Word[] src, int dstAddr) {
            this.nested = nested;

            this.src = src;
            this.dstAddr = dstAddr;
        }

        public MemoryLoader<Word> add(Word[] src, int dstAddr) {
            return new MemoryLoader<>(this, src, dstAddr);
        }

        //
        // Interface Implementation: Application

        public void apply(JunMemory<Word> memory) {
            if (nested != null) {
                nested.apply(memory);
            }

            applyStep(memory);
        }

        private void applyStep(JunMemory<Word> memory) {
            copyIn(src, 0, memory, dstAddr, src.length);
        }

    }

}
