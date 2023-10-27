package pta.osdp;

class A {
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

class AP extends A {
    @Override
    void m() {
    }
}

class B {
    A a;
}

public class Test1 {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new AP();
        a1.foo();
        a2.foo();
        a1.foo2();
    }
}

