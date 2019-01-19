package ca.djiang.junction.core.cpu.mips.instruction.util;

import ca.djiang.junction.core.JunCpu;
import ca.djiang.junction.core.cpu.mips.MipsRegisterMap;

public final class JumpBranchUtils {

    //
    // Construction: Disabled

    private JumpBranchUtils() {}

    //
    // Static Interface Implementation: Utility Methods

    public static void jump(JunCpu<Integer, Integer>.StepInterface system, int newPc) {
        system.pc().store(newPc);
    }

    public static void jumpRel(JunCpu<Integer, Integer>.StepInterface system, int offset) {
        system.pc().store(system.pc().load() + offset);
    }

    public static void linkRegister(JunCpu<Integer, Integer>.StepInterface system) {
        system.registers().store(MipsRegisterMap.RL, system.pc().load());
    }

}
