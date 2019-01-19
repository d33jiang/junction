package ca.djiang.junction.core.cpu;

import ca.djiang.junction.core.JunCpu;

@FunctionalInterface
public interface JunCpuCore<Word extends Number, RegisterIndex> {

    public void step(JunCpu<Word, RegisterIndex>.StepInterface system);

}
