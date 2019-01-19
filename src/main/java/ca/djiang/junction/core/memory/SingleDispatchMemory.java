package ca.djiang.junction.core.memory;

import java.util.function.IntPredicate;

import ca.djiang.junction.core.JunMemory;

public class SingleDispatchMemory<Word extends Number> implements JunMemory<Word> {

    //
    // Configuration: Dispatch Memory

    private final IntPredicate dispatchTest;
    private final JunMemory<Word> dispatchMemory;

    //
    // Configuration: Default Memory

    private final JunMemory<Word> defaultMemory;

    //
    // Construction

    public SingleDispatchMemory(IntPredicate dispatchTest, JunMemory<Word> dispatchMemory,
            JunMemory<Word> defaultMemory) {
        this.dispatchTest = dispatchTest;
        this.dispatchMemory = dispatchMemory;

        this.defaultMemory = defaultMemory;
    }

    //
    // Interface Implementation: Memory Operations

    @Override
    public Word loadWord(int addr) {
        if (dispatchTest.test(addr)) {
            return dispatchMemory.loadWord(addr);
        }

        return defaultMemory.loadWord(addr);
    }

    @Override
    public void storeWord(int addr, Word value) {
        if (dispatchTest.test(addr)) {
            dispatchMemory.storeWord(addr, value);
        }

        defaultMemory.storeWord(addr, value);
    }

}
