package ca.djiang.junction.core.register;

public class JunRegister<Word extends Number> {

    //
    // State: Register Value

    private Word value;

    //
    // Construction

    public JunRegister(Word initialValue) {
        store(initialValue);
    }

    //
    // Interface Implementation: Register Operations

    public Word load() {
        return value;
    }

    public void store(Word newValue) {
        if (newValue == null)
            throw new NullPointerException("newValue is null");

        this.value = newValue;
    }

}
