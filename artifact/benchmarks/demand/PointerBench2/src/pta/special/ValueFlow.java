package pta.special;

public class ValueFlow {
    public static void main(String[] args) {
        A v1 = new A();
        v1.f = new Object();
        A v2 = new A();
        while (true) {
            A v;
            if (v1 instanceof Object)
                v = v1;
            else
                v = v2;
            Object l = v.f;
            v.f = l;
        }
    }

    static class A {
        Object f;
    }
}
