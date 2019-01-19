package junction.core.cpu;

import junction.core.JunCpu;

@FunctionalInterface
public interface JunCpuCore<Word extends Number, RegisterIndex> {

    public void step(JunCpu<Word, RegisterIndex>.StepInterface system);

}
