package osdp;

public class Test1 {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new AP();
        a1.foo();
        a2.foo();
        a1.foo2();
    }

    static class A {
        B b;

        void foo() {
            b = new B();
            b.a = this;
        }

        void foo2() {
            b.a.m();
        }

        void m() {
        }
    }

    static class AP extends A {
        @Override
        void m() {
        }
    }

    static class B {
        A a;
    }
}

