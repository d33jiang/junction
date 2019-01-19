package ca.djiang.junction.core.cpu.mips.instruction;

import ca.djiang.junction.core.JunCpu;
import ca.djiang.junction.core.cpu.mips.MipsRegisterMap;
import ca.djiang.junction.core.cpu.mips.instruction.util.JumpBranchUtils;
import ca.djiang.junction.core.register.JunRegister;

public final class MipsRegisterInstruction extends MipsInstruction {

    //
    // Static Interface Implementation: Instruction Type

    @Override
    public MipsInstructionType getInstructionType() {
        return MipsInstructionType.R_TYPE;
    }

    //
    // Static Implementation: Instruction Decoding

    static MipsRegisterInstruction decode(byte opcode, int word) {
        return new MipsRegisterInstruction(opcode, word);
    }

    //
    // Configuration: Data

    private final byte rs;
    private final byte rt;
    private final byte rd;

    private final byte shamt;

    private final byte funct;

    //
    // Construction

    private MipsRegisterInstruction(byte opcode, int word) {
        super(opcode);

        rs = (byte) extractBits(word, 21, BitMask.M5);
        rt = (byte) extractBits(word, 16, BitMask.M5);
        rd = (byte) extractBits(word, 11, BitMask.M5);

        shamt = (byte) extractBits(word, 6, BitMask.M5);

        funct = (byte) extractBits(word, 0, BitMask.M6);
    }

    //
    // Implementation Stub: Instruction Execution

    @Override
    public void step(JunCpu<Integer, Integer>.StepInterface system) {
        int irs = system.registers().load((int) rs);
        int irt = system.registers().load((int) rt);
        JunRegister<Integer> rrd = system.registers().register((int) rd);

        if (testFunctBit(5)) {
            // [32, 64] - ALU
            if (testFunctBit(4)) {
                // [48, 64]
                throwForUnrecognizedFunct();
            } else {
                // [32, 48]
                if (testFunctBit(3)) {
                    // [32, 40] - Arithmetic & Logic
                    switch (funct) {
                        // FUTURE: Exceptions - Add overflow exceptions to 32, 34
                        case 32:
                        case 33:
                            // Add
                            rrd.store(irs + irt);
                            break;
                        case 34:
                        case 35:
                            // Subtract
                            rrd.store(irs - irt);
                            break;
                        case 36:
                            // And
                            rrd.store(irs & irt);
                            break;
                        case 37:
                            // Or
                            rrd.store(irs | irt);
                            break;
                        case 38:
                            // Xor
                            rrd.store(irs ^ irt);
                            break;
                        case 39:
                            // Nor
                            rrd.store(~(irs | irt));
                            break;
                    }
                } else {
                    // [40, 48] - Compare Less Than
                    switch (funct) {
                        case 42:
                            // SLT
                            rrd.store(irs < irt ? 1 : 0);
                            break;
                        case 43:
                            // SLTU
                            rrd.store(Integer.compareUnsigned(irs, irt) < 0 ? 1 : 0);
                            break;
                        default:
                            throwForUnrecognizedFunct();
                    }
                }
            }
        } else {
            // [0, 32]
            if (testFunctBit(4)) {
                // [16, 32] - Multiplication & Division
                if (testFunctBit(3)) {
                    // [24, 32] - Arithmetic
                    switch (funct) {
                        // FUTURE: Exceptions - Add overflow exceptions to 24, 26, 27
                        case 24:
                        case 25:
                            // Multiply
                            long product = ((long) irs) * ((long) irt);
                            long intMask = Integer.toUnsignedLong(-1);
                            system.registers().store(MipsRegisterMap.LO, (int) (product & intMask));
                            system.registers().store(MipsRegisterMap.HI, (int) (product >>> 32));
                            break;
                        case 26:
                        case 27:
                            // Divide
                            system.registers().store(MipsRegisterMap.LO, irs / irt);
                            system.registers().store(MipsRegisterMap.HI, irs % irt);
                            break;
                        default:
                            throwForUnrecognizedFunct();
                    }
                } else {
                    // [16, 24] - Register Access
                    if (funct >= 20) {
                        // [20, 24]
                        throwForUnrecognizedFunct();
                    } else {
                        // [16, 20]
                        JunRegister<Integer> hiloReg = system.registers().register(
                                testFunctBit(1) ? MipsRegisterMap.LO : MipsRegisterMap.HI);
                        if (testFunctBit(0)) {
                            // Move to HI/LO register
                            hiloReg.store(irs);
                        } else {
                            // Move from HI/LO register
                            rrd.store(hiloReg.load());
                        }
                    }
                }
            } else {
                // [0, 16] - Special
                if (testFunctBit(3)) {
                    // [8, 16] - Special
                    int newPc = rrd.load();
                    switch (funct) {
                        case 9:
                            // Jump and Link Register
                            JumpBranchUtils.linkRegister(system);
                        case 8:
                            // Jump Register
                            JumpBranchUtils.jump(system, newPc);
                            break;
                        case 12:
                            // OS, System Call (Unsupported)
                            throw new UnsupportedOperationException();
                        case 13:
                            // OS, Breakpoint (Unsupported)
                            throw new UnsupportedOperationException();
                        default:
                            throwForUnrecognizedFunct();
                    }
                } else {
                    // [0, 8] - Bit Shift
                    int shift = testFunctBit(2) ? irs : shamt;
                    switch (funct % 4) {
                        case 0:
                            // Left Logical
                            rrd.store(irt << shift);
                            break;
                        case 2:
                            // Right Logical
                            rrd.store(irt >>> shift);
                            break;
                        case 3:
                            // Right Arithmetic
                            rrd.store(irt >> shift);
                            break;
                        default:
                            throwForUnrecognizedFunct();
                    }
                }
            }
        }
    }

    private boolean testFunctBit(int i) {
        return ((funct >>> i) & 1) > 0;
    }

    private void throwForUnrecognizedFunct() {
        throw new IllegalArgumentException("Unrecognized R-format instruction function: " + Byte.toUnsignedInt(funct));
    }

}
