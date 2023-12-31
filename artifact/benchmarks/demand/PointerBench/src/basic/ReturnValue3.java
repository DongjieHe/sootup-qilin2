package basic;

import benchmark.internal.Benchmark;
import benchmark.objects.A;
import pointerbench.markers.Alloc;

/*
 * @testcase ReturnValue1
 *
 * @version 1.0
 *
 * @author Johannes Späth, Nguyen Quang Do Lisa (Secure Software Engineering Group, Fraunhofer
 * Institute SIT)
 *
 * @description Alias to a return value from a static method
 */
public class ReturnValue3 {

    public static A id(A x) {
        A y = new A();
        y.j = new Alloc();
        return y;
    }

    public static void main(String[] args) {

        A a = new A();
        A b = id(a);
        Object x = b.j;
        Object y = a.j;
        Benchmark.pointsToQuery(x);
        Benchmark.mayAliasQuery(a, b, false);
        Benchmark.mayAliasQuery(a, y, false);
        Benchmark.mayAliasQuery(b, y, false);
        Benchmark.mayAliasQuery(x, y, false);
    }
}
