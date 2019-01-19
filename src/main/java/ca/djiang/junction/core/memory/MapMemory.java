package ca.djiang.junction.core.memory;

import ca.djiang.junction.core.JunMemory;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class MapMemory<Word extends Number> implements JunMemory<Word> {

    //
    // State: Backing Map

    private final TIntObjectMap<Word> data;

    //
    // Construction

    public MapMemory() {
        this.data = new TIntObjectHashMap<>();
    }

    //
    // Interface Implementation: Memory Operations

    @Override
    public Word loadWord(int addr) {
        Word w = data.get(addr);

        if (w == null)
            throw new IndexOutOfBoundsException();

        return w;
    }

    @Override
    public void storeWord(int addr, Word value) {
        if (value == null)
            throw new NullPointerException("value");

        data.put(addr, value);
    }

}
