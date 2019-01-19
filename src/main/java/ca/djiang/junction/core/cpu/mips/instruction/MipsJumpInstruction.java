package ca.djiang.junction.core.cpu.mips.instruction;

import ca.djiang.junction.core.JunCpu;
import ca.djiang.junction.core.cpu.mips.instruction.util.JumpBranchUtils;

public final class MipsJumpInstruction extends MipsInstruction {

    //
    // Static Interface Implementation: Instruction Type

    @Override
    public MipsInstructionType getInstructionType() {
        return MipsInstructionType.J_TYPE;
    }

    //
    // Static Implementation: Instruction Decoding

    static MipsJumpInstruction decode(byte opcode, int word) {
        return new MipsJumpInstruction(opcode, word);
    }

    //
    // Configuration: Data

    private final int addr;

    //
    // Construction

    private MipsJumpInstruction(byte opcode, int word) {
        super(opcode);

        addr = extractBits(word, 0, BitMask.M26);
    }

    //
    // Implementation Stub: Instruction Execution

    @Override
    public void step(JunCpu<Integer, Integer>.StepInterface system) {
        int newPc = (system.pc().load() & 0xf000_0000) | (addr << 2);

        switch (opcode) {
            case 3:
                // Jump and Link
                JumpBranchUtils.linkRegister(system);
            case 2:
                // Jump
                JumpBranchUtils.jump(system, newPc);
                break;
            default:
                throwForUnrecognizedOpcode();
        }
    }

}
