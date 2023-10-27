package osdp;

import benchmark.internal.Benchmark;
import benchmark.objects.A;
import pointerbench.markers.Alloc;

public class Field1 {

    public static void main(String[] args) {
        A a = new A();
        Object y = new Alloc();
        a.f = y;
        Object x = a.f;
        Benchmark.pointsToQuery(x);
        Benchmark.mayAliasQuery(x, y, true);
    }

}
