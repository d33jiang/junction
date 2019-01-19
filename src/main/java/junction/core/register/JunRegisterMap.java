package junction.core.register;

public interface JunRegisterMap<Word extends Number, Index> {

    public static class $$Embedded {
        private $$Embedded() {}

        private static void throwIfNullIndex(Object index) {
            if (index == null)
                throw new NullPointerException("index is null");
        }
    }

    public JunRegister<Word> register(Index index);

    public default Word load(Index index) {
        $$Embedded.throwIfNullIndex(index);
        return register(index).load();
    }

    public default void store(Index index, Word value) {
        $$Embedded.throwIfNullIndex(index);
        register(index).store(value);
    }

}
