package osdp;

import java.util.HashMap;

public final class Pattern {
    static Node accept = new Node();
    transient Node root;
    transient Node matchRoot;
    transient int capturingGroupCount;
    transient int localCount;

    private Pattern(String p) {
        capturingGroupCount = 1;
        localCount = 0;
    }

    public static void main(String[] args) {
        new CharPropertyNames();
    }

    static class Node extends Object {
        Node next;

        Node() {
            next = Pattern.accept;
        }
    }

    private static abstract class CharProperty extends Node {
        abstract boolean isSatisfiedBy();
    }


    private static class CharPropertyNames {
        private static final HashMap<String, CharPropertyFactory> map = new HashMap<>();

        static {
            defClone("javaLowerCase", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
        }

        private static void defClone(String name, final CloneableProperty p) {
            map.put(name, new CharPropertyFactory() {
                CharProperty make() {
                    return p.clone();
                }
            });
        }

        private static abstract class CharPropertyFactory {
            abstract CharProperty make();
        }

        private static abstract class CloneableProperty extends CharProperty implements Cloneable {
            public CloneableProperty clone() {
                try {
                    return (CloneableProperty) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new AssertionError(e);
                }
            }
        }
    }
}

