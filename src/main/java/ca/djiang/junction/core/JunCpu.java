package ca.djiang.junction.core;

import ca.djiang.junction.core.cpu.JunCpuCore;
import ca.djiang.junction.core.register.JunRegister;
import ca.djiang.junction.core.register.JunRegisterMap;

public final class JunCpu<Word extends Number, RegisterIndex> {

    //
    // Managed State: System

    private JunSystem<Word> system;

    void setSystem(JunSystem<Word> system) {
        this.system = system;
    }

    //
    // Configuration: Core

    private final JunCpuCore<Word, RegisterIndex> core;

    //
    // Configuration: Registers

    private final JunRegisterMap<Word, RegisterIndex> registers;
    private final JunRegister<Word> pc;

    //
    // Construction

    public JunCpu(JunCpuCore<Word, RegisterIndex> core, JunRegisterMap<Word, RegisterIndex> registers, Word pc) {
        this.core = core;

        this.registers = registers;
        this.pc = new JunRegister<>(pc);
    }

    //
    // Interface: Read State

    public Word readRegister(RegisterIndex index) {
        return registers.load(index);
    }

    public Word readPc() {
        return pc.load();
    }

    //
    // Implementation: Memory Management

    private JunMemory<Word> getMemory() {
        // FUTURE: Caching
        return system.getBus().getMemory();
    }

    //
    // Interface: Instruction Execution

    public void step() {
        core.step(new StepInterface());
    }

    public class StepInterface {

        //
        // Construction: Private

        private StepInterface() {}

        //
        // Interface: Step Interface

        public JunRegisterMap<Word, RegisterIndex> registers() {
            return registers;
        }

        public JunRegister<Word> pc() {
            return pc;
        }

        public JunMemory<Word> memory() {
            return getMemory();
        }

        public JunSystem<Word>.JunBus bus() {
            return system.getBus();
        }

    }

}
