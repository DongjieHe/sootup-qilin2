package generalJava;

import benchmark.internal.Benchmark;
import benchmark.objects.A;

/*
 * @testcase Null1
 *
 * @version 1.0
 *
 * @author Johannes Späth, Nguyen Quang Do Lisa (Secure Software Engineering Group, Fraunhofer
 * Institute SIT)
 *
 * @description Direct alias to null
 */
public class Null1 {

    public static void main(String[] args) {

        // No allocation site
        A h = new A();
        Object a = h.getH();
        Object b = a;
        Benchmark.pointsToQuery(b);
        Benchmark.mayAliasQuery(a, b, false);
    }
}
