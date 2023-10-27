package osdp;

public final class Test3 {
    public static void main(String[] args) {
        new CharPropertyNames();
    }

    private static class CharPropertyNames {
        static {
            defClone("javaLowerCase", new Object());
        }

        private static void defClone(String name, Object p) {
        }
    }
}

