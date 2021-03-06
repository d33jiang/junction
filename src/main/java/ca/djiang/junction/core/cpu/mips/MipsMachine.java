package ca.djiang.junction.core.cpu.mips;

import ca.djiang.junction.core.JunCpu;
import ca.djiang.junction.core.cpu.JunCpuCore;
import ca.djiang.junction.core.cpu.mips.instruction.MipsInstruction;

public final class MipsMachine implements JunCpuCore<Integer, Integer> {

    //
    // Interface Implementation: Instruction Execution

    @Override
    public void step(JunCpu<Integer, Integer>.StepInterface system) {
        int ipc = system.pc().load();
        if ((ipc & 3) > 0)
            throw new IllegalStateException("Program counter is misaligned");

        Integer instructionWord = system.memory().loadWord(ipc);
        system.pc().store(ipc + 4);

        MipsInstruction.decode(instructionWord).step(system);
    }

}
