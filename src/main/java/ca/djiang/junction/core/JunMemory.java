package ca.djiang.junction.core;

public interface JunMemory<Word extends Number> {

    public Word loadWord(int addr);

    public void storeWord(int addr, Word value);

}
