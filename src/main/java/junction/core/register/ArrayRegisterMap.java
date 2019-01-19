package junction.core.register;

public class ArrayRegisterMap<Word extends Number> implements JunRegisterMap<Word, Integer> {

    //
    // Configuration: Registers

    private final JunRegister<Word>[] registers;

    //
    // Construction

    public ArrayRegisterMap(JunRegister<Word>[] registers) {
        this.registers = registers;
    }

    //
    // Implementation: Register Operations

    @Override
    public JunRegister<Word> register(Integer index) {
        return registers[index];
    }

}
