package osdp;

import benchmark.internal.Benchmark;

class A {
    Object m(A a) {
        return a.m(new B());
    }
}

class B extends A {
    Object m(A a) {
        return a.m(new A());
    }
}

public class Loop3 {

    public static void main(String[] args) {
        A a = new A();
        A a1 = new A();
        Object o = a.m(a1);
        Benchmark.pointsToQuery(o);
    }

}
