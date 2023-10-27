package pta.gradualobjectsensitivity;

public class Gradual1 {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new A();
        A a3 = new A();
        A a4 = new A();

        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = new Object();
        Object o4 = new Object();

        Object v1 = a1.foo(o1);
        Object v2 = a2.foo(o2);
        Object v3 = a3.bar(o3);
        Object v4 = a4.bar(o4);
    }

    static class A {
        Object foo(Object o) {
            B b1 = new B();
            return b1.id(o);
        }

        Object bar(Object o) {
            B b2 = new B();
            Object o5 = new Object();
            Object v5 = b2.id(o5);
            return b2.id2(o);
        }
    }

    static class B {
        Object id(Object o) {
            return o;
        }

        Object id2(Object o) {
            return o;
        }
    }
}
