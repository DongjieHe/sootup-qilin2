package pta.selectiveobjectsensitivity;

import pta.selectiveobjectsensitivity.GlobalPolymorphic.A.X;

public class GlobalPolymorphic {
    public static void main(String[] args) {
        A a = new A();
        A b = new B();
        X x = a.foo();
        X y = b.foo();
        x.x();
    }

    static class A {
        static X a = new X(), b = new Y();

        X bar() {
            return a;
        }

        X foo() {
            return bar();
        }

        static class X {
            void x() {
            }
        }

        static class Y extends X {
            @Override
            void x() {
            }
        }
    }

    static class B extends A {
        @Override
        X bar() {
            return b;
        }
    }
}
