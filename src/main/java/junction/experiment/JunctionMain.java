package junction.experiment;

import junction.core.JunCpu;
import junction.core.JunMemory;
import junction.core.JunSystem;
import junction.core.cpu.mips.MipsMachine;
import junction.core.cpu.mips.MipsRegisterMap;
import junction.core.memory.AddressMapping;
import junction.core.memory.ArrayMemory;
import junction.core.memory.PagingMemory;

public class JunctionMain {

    public static void main(String[] args) {

        JunMemory<Integer> memory = new PagingMemory<>(24, s -> new ArrayMemory<>(s, 0));
        memory = new AddressMapping<>(memory, i -> i >>> 2);
        loadTestMipsProgram(memory);

        MipsMachine core = new MipsMachine();
        JunCpu<Integer, Integer> cpu = new JunCpu<>(core, new MipsRegisterMap(), 0);

        JunSystem<Integer> mips = new JunSystem<>(memory, cpu);

        System.out.print(readMemory(memory, cpu));
        System.out.println();

        while (cpu.readPc() < 60) {
            System.out.printf("PC (0x%02x)     $8 (%2d)     $9 (%2d)%n",
                    cpu.readPc(),
                    cpu.readRegister(8),
                    cpu.readRegister(9));
            mips.step();
        }

        System.out.printf("PC (0x%02x)     $8 (%2d)     $9 (%2d)%n",
                cpu.readPc(),
                cpu.readRegister(8),
                cpu.readRegister(9));

        System.out.println();
        System.out.print(readMemory(memory, cpu));

        System.out.println();
    }

    private static void loadTestMipsProgram(JunMemory<Integer> mipsMemory) {
        int[] instructions = {
            0b001000_00000_01000_00000000_00000000, // ADDI__$8,__$0__(0) @ 40 0x28
            0b001000_00000_01001_00000000_00100000, // ADDI__$9,__$0_(32) @ 44 0x2c
            0b101011_01000_01000_00000000_00000000, // SW____$8,__$8__(0) @ 48 0x30
            0b001000_01000_01000_00000000_00000100, // ADDI__$8,__$8__(4) @ 52 0x34
            0b000101_01000_01001_11111111_11111101, // BNE___$8,__$9_(-3) @ 56 0x38
            0b000100_00000_00000_11111111_11111111, // BEQ___$0,__$0_(-1) @ 60 0x3c
        };

        for (int i = 0, addr = 40; i < instructions.length; i++, addr += 4) {
            mipsMemory.storeWord(addr, instructions[i]);
        }
    }

    private static String readMemory(JunMemory<Integer> memory, JunCpu<Integer, Integer> cpu) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int addr = 4 * i;
            int word = memory.loadWord(addr);
            sb.append(String.format("0x%02x %08x%n", addr, word));
        }

        return sb.toString();
    }

}
