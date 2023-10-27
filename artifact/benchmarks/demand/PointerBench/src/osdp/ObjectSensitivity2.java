package osdp;

import benchmark.internal.Benchmark;
import benchmark.objects.A;
import pointerbench.markers.Alloc;

public class ObjectSensitivity2 {

    public static void main(String[] args) {
        Object b1 = new Object();
        Object b2 = new Alloc();

        A a1 = new A();
        A a2 = new A();

        Object b3 = a1.id(b1);
        Object b4 = a2.id(b2);

        Benchmark.pointsToQuery(b4);
        Benchmark.mayAliasQuery(b3, b4, false);
    }

}
