package ca.djiang.junction.core.register;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Sets;

public class EnumRegisterMap<Word extends Number, Index extends Enum<Index>> implements JunRegisterMap<Word, Index> {

    //
    // State: Registers

    private final EnumMap<Index, JunRegister<Word>> registers;

    //
    // Construction

    // from map; no fallback
    public EnumRegisterMap(Class<Index> indexType, Map<Index, JunRegister<Word>> registerMap) {
        registers = new EnumMap<Index, JunRegister<Word>>(registerMap);

        Set<Index> missingMappings = Sets.difference(EnumSet.allOf(indexType), registers.keySet());
        if (!missingMappings.isEmpty())
            throw new IllegalArgumentException(
                    "registerMap is missing mappings for the following indices: " + missingMappings);
    }

    // from map; fallback on default value
    public EnumRegisterMap(Class<Index> indexType, Map<Index, JunRegister<Word>> registerMap, Word initialValue) {
        registers = new EnumMap<>(registerMap);

        Index[] indices = indexType.getEnumConstants();
        for (Index index : indices) {
            registers.computeIfAbsent(index, (Index i) -> new JunRegister<>(initialValue));
        }
    }

    // from index-to-register function
    public EnumRegisterMap(Class<Index> indexType, Function<Index, JunRegister<Word>> registerFactory) {
        registers = new EnumMap<>(indexType);

        Index[] indices = indexType.getEnumConstants();
        for (Index index : indices) {
            registers.put(index, registerFactory.apply(index));
        }
    }

    //
    // Implementation: Register Operations

    @Override
    public JunRegister<Word> register(Index index) {
        return registers.get(index);
    }

}
