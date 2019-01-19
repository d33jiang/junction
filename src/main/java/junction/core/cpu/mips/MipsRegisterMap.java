package junction.core.cpu.mips;

import junction.core.register.ArrayRegisterMap;
import junction.core.register.JunHardwiredRegister;
import junction.core.register.JunRegister;

public final class MipsRegisterMap extends ArrayRegisterMap<Integer> {

    //
    // Constant: Standard Indices

    public static final int RZ = 0;
    public static final int RL = 31;
    public static final int LO = 32;
    public static final int HI = 33;

    public static final int NUM_GPR = 32;
    public static final int NUM_REG = 34;

    //
    // Static Support: Register Map Configuration

    private static final Integer ZERO = 0;
    private static final Integer REGISTER_INITIAL_VALUE = 0;

    private static JunRegister<Integer>[] generateRegisters() {
        @SuppressWarnings("unchecked")
        JunRegister<Integer>[] registers = new JunRegister[NUM_REG];

        registers[RZ] = new JunHardwiredRegister<>(ZERO);

        for (int i = 1; i < registers.length; i++) {
            registers[i] = new JunRegister<>(REGISTER_INITIAL_VALUE);
        }

        return registers;
    }

    //
    // Construction

    public MipsRegisterMap() {
        super(generateRegisters());
    }

}
