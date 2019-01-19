package ca.djiang.junction.core.cpu.mips.instruction;

import ca.djiang.junction.core.JunCpu;

public abstract class MipsInstruction {

    //
    // Static Interface: Instruction Decoding

    public static MipsInstruction decode(int word) {
        return MipsInstructionType.decode(word);
    }

    //
    // Static Support: Bit Pattern Masking

    static enum BitMask {
        M5(5),
        M6(6),
        M16(16),
        M26(26);

        private final int mask;

        private static int generateBitMask(int numBits) {
            return -1 >>> (Integer.SIZE - numBits);
        }

        private BitMask(int numBits) {
            this.mask = generateBitMask(numBits);
        }
    }

    static int extractBits(int word, int shiftAmount, BitMask bitMask) {
        return (word >>> shiftAmount) & bitMask.mask;
    }

    //
    // Static Support: Throw for Unrecognized Opcode

    void throwForUnrecognizedOpcode() {
        throw new IllegalArgumentException("Unrecognized opcode: " + Byte.toUnsignedInt(opcode));
    }

    //
    // Protected Configuration: Data

    final byte opcode;

    //
    // Construction

    MipsInstruction(byte opcode) {
        this.opcode = opcode;
    }

    //
    // Interface: Instruction Type

    abstract MipsInstructionType getInstructionType();

    //
    // Implementation Stub: Instruction Execution

    public abstract void step(JunCpu<Integer, Integer>.StepInterface system);

}
