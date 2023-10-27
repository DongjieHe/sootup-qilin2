package pta.selectiveobjectsensitivity;

public class Init {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new A();
        a1.setB();
        a2.setBP();
        a1.fFoo();
    }
}

class A {
    B b;

    void setB() {
        b = new B();
    }

    void setBP() {
        b = new BP();
    }

    public void fFoo() {
        b.foo();
    }
}

class B {
    void foo() {
    }
}

class BP extends B {
    @Override
    void foo() {
    }
}