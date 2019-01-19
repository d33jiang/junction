package junction.core;

public final class JunSystem<Word extends Number> {

    //
    // Managed State: Components

    private JunMemory<Word> memory;
    private JunCpu<Word, ?> cpu;

    public JunMemory<Word> installMemory(JunMemory<Word> newMemory) {
        JunMemory<Word> oldMemory = memory;
        memory = newMemory;
        return oldMemory;
    }

    public JunCpu<Word, ?> installCpu(JunCpu<Word, ?> newCpu) {

        if (cpu != null) {
            cpu.setSystem(null);
        }
        JunCpu<Word, ?> oldCpu = cpu;

        cpu = newCpu;
        if (cpu != null) {
            cpu.setSystem(this);
        }

        return oldCpu;
    }

    //
    // Construction

    public JunSystem(JunMemory<Word> memory, JunCpu<Word, ?> cpu) {
        installMemory(memory);
        installCpu(cpu);
    }

    //
    // Interface: Step

    public void step() {
        cpu.step();
    }

    //
    // Interface Implementation: Bus

    JunBus getBus() {
        return new JunBus();
    }

    public final class JunBus {

        //
        // Construction: Private

        private JunBus() {}

        //
        // Interface: Memory

        public JunMemory<Word> getMemory() {
            return memory;
        }

    }

}
