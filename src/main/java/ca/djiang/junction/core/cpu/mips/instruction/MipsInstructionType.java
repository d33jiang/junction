package ca.djiang.junction.core.cpu.mips.instruction;

import ca.djiang.junction.core.cpu.mips.instruction.MipsInstruction.BitMask;

public enum MipsInstructionType {

    //
    // Enumerable Constants

    R_TYPE(MipsRegisterInstruction::decode),
    I_TYPE(MipsImmediateInstruction::decode),
    J_TYPE(MipsJumpInstruction::decode);

    //
    // Support Interface: Instruction Decoding

    @FunctionalInterface
    public static interface MipsInstructionDecoder {
        public MipsInstruction decode(byte opcode, int word);
    }

    //
    // Configuration: Instruction Type

    private final MipsInstructionDecoder instructionDecoder;

    //
    // Construction: Enumerable Configuration

    private MipsInstructionType(MipsInstructionDecoder instructionDecoder) {
        this.instructionDecoder = instructionDecoder;
    }

    //
    // Static Implementation: Instruction Decoding

    static MipsInstruction decode(int word) {
        byte opcode = (byte) MipsInstruction.extractBits(word, 26, BitMask.M6);

        MipsInstructionType type;
        if (opcode == 0) {
            type = R_TYPE;
        } else if (opcode < 2 || opcode >= 4) {
            type = I_TYPE;
        } else {
            type = J_TYPE;
        }

        return type.instructionDecoder.decode(opcode, word);
    }

}
