package ca.djiang.junction.core.register;

public class JunHardwiredRegister<Word extends Number> extends JunRegister<Word> {

    //
    // Construction

    public JunHardwiredRegister(Word initialValue) {
        super(initialValue);
        super.store(initialValue);
    }

    //
    // Interface Implementation: Discarded Writes

    @Override
    public void store(Word newValue) {
        // No-op
    }

}
