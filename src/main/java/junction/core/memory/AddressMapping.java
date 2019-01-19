package junction.core.memory;

import junction.core.JunMemory;

public class AddressMapping<Word extends Number> implements JunMemory<Word> {

    //
    // Interface: Map

    @FunctionalInterface
    public static interface AddressMap {
        public int map(int addr);
    }

    //
    // Configuration: Backing Memory

    private final JunMemory<Word> memory;

    //
    // Configuration: Address Map

    private final AddressMap mapping;

    //
    // Construction

    public AddressMapping(JunMemory<Word> memory, AddressMap map) {
        this.memory = memory;
        this.mapping = map;
    }

    //
    // Interface Implementation: Memory Operations

    @Override
    public Word loadWord(int addr) {
        return memory.loadWord(mapping.map(addr));
    }

    @Override
    public void storeWord(int addr, Word value) {
        memory.storeWord(mapping.map(addr), value);
    }

}
