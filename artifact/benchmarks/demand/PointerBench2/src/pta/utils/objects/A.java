package pta.utils.objects;

public class A {
    public int i = 5;
    public B f = new B();
    public B g = new B();
    public B h;

    public A() {
    }

    public A(B b) {
        this.f = b;
    }

    public B setF(B b) {
        this.f = b;
        this.f.foo();
        return this.f;
    }

    public B getF() {
        return f;
    }

    public B getH() {
        return h;
    }

    public B id(B b) {
        return b;
    }
}
