package osdp;

import benchmark.internal.Benchmark;

class T {
    Object foo() {
        Object o1 = new Object(); // O1
        return o1;
    }
}

class S extends T {
    Object foo() {
        Object o2 = new Object(); // O2
        return o2;
    }
}

class U {
    Object bar(T a) {
        Object p = a.foo();
        return p;
    }
}

class Entry {
    public static void main(String args[]) {
        T a1 = new T(); // A1
        T a2 = new S(); // A2
        U ci = new U(); // Ci
        U cj = new U(); // Cj
        Object x = ci.bar(a1);
        Benchmark.pointsToQuery(x);
        Object y = cj.bar(a2);
        // Benchmark.pointsToQuery(y);
    }
}
