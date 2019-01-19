package junction.core.memory;

import java.util.Arrays;

import junction.core.JunMemory;

public class ArrayMemory<Word extends Number> implements JunMemory<Word> {

    //
    // State: Backing Array

    private final Word[] data;

    //
    // Construction

    @SuppressWarnings("unchecked")
    public ArrayMemory(int size, Word defaultValue) {
        this.data = (Word[]) new Number[size];
        Arrays.fill(this.data, defaultValue);
    }

    //
    // Interface Implementation: Memory Operations

    @Override
    public Word loadWord(int addr) {
        return data[addr];
    }

    @Override
    public void storeWord(int addr, Word value) {
        data[addr] = value;
    }

}
