package osdp;

public class Example {
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
            b.bar(this);
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

        void bar(A p) {
            C c = new C();
            a = c.baz(p);
        }

        void bar2() {
            a.foo2();
        }
    }

    static class C {
        public A baz(A p) {
            return p;
        }

    }
}
