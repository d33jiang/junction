package junction.core.cpu.mips.instruction;

import junction.core.JunCpu;
import junction.core.cpu.mips.instruction.util.JumpBranchUtils;
import junction.core.register.JunRegister;

public final class MipsImmediateInstruction extends MipsInstruction {

    //
    // Static Interface Implementation: Instruction Type

    @Override
    public MipsInstructionType getInstructionType() {
        return MipsInstructionType.I_TYPE;
    }

    //
    // Static Implementation: Instruction Decoding

    static MipsImmediateInstruction decode(byte opcode, int word) {
        return new MipsImmediateInstruction(opcode, word);
    }

    //
    // Configuration: Data

    private final byte rs;
    private final byte rt;
    private final short imm;

    //
    // Construction

    private MipsImmediateInstruction(byte opcode, int word) {
        super(opcode);

        rs = (byte) extractBits(word, 21, BitMask.M5);
        rt = (byte) extractBits(word, 16, BitMask.M5);

        imm = (short) extractBits(word, 0, BitMask.M16);
    }

    //
    // Implementation Stub: Instruction Execution

    @Override
    public void step(JunCpu<Integer, Integer>.StepInterface system) {
        int signExtension = imm;
        int zeroExtension = Short.toUnsignedInt(imm);

        int irs = system.registers().load((int) rs);
        JunRegister<Integer> rrt = system.registers().register((int) rt);

        if (testOpcodeBit(5)) {
            // [32, 64] - Memory Access
            int addr = irs + signExtension;

            switch (opcode) {
                case 35:
                    // Load Word
                    rrt.store(system.memory().loadWord(addr));
                    break;
                case 43:
                    // Store Word
                    system.memory().storeWord(addr, rrt.load());
                    break;
                default:
                    throwForUnrecognizedOpcode();
            }
        } else {
            // [0, 32]
            if (testOpcodeBit(4)) {
                // [16, 32] - Coprocessors
                throwForUnrecognizedOpcode();
            } else {
                // [0, 16] - Mixed
                if (testOpcodeBit(3)) {
                    // [8, 16] - Arithmetic with Immediate
                    JunRegister<Integer> rrd = rrt;

                    switch (opcode) {
                        case 8:
                            // ADDI
                        case 9:
                            // ADDIU
                            rrd.store(irs + signExtension);
                            break;
                        case 10:
                            // SLTI
                            rrd.store(irs < signExtension ? 1 : 0);
                            break;
                        case 11:
                            // SLTIU
                            rrd.store(Integer.compareUnsigned(irs, signExtension) < 0 ? 1 : 0);
                            break;
                        case 12:
                            // ANDI
                            rrd.store(irs & zeroExtension);
                            break;
                        case 13:
                            // ORI
                            rrd.store(irs | zeroExtension);
                            break;
                        case 14:
                            // XORI
                            rrd.store(irs ^ zeroExtension);
                            break;
                        case 15:
                            // LUI
                            rrd.store((zeroExtension << 16) | (rrd.load() & 0xffff));
                            break;
                        default:
                            throw new IllegalStateException("Should not occur");
                    }
                } else {
                    // [0, 8] - Jump & Branch
                    boolean test;

                    if (opcode >= 4) {
                        // [4, 8]
                        test = testOpcodeBit(1)
                                ? irs <= 0
                                : irs == rrt.load();
                        if (testOpcodeBit(0)) {
                            test = !test;
                        }
                    } else {
                        if (opcode != 1)
                            throwForUnrecognizedOpcode();

                        if (testOpcodeBit(4)) {
                            JumpBranchUtils.linkRegister(system);
                        }

                        test = irs < 0;
                        if (testOpcodeBit(0)) {
                            test = !test;
                        }
                    }

                    if (test) {
                        JumpBranchUtils.jumpRel(system, signExtension << 2);
                    }
                }
            }
        }
    }

    private boolean testOpcodeBit(int i) {
        return ((opcode >>> i) & 1) > 0;
    }

}
