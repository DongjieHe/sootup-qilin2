package pta.generalJava;

import pta.utils.objects.A;
import pta.utils.objects.B;

import static pta.utils.Dummy.mayAlias;

/*
 * @testcase Null2
 * @description Implicit alias to null
 */
public class Null2 {

    public static void main(String[] args) {

        // No allocation site
        A a = new A();
        A b = a;
        B x = b.h; // a.h is null
        mayAlias(b, a);
		/*Benchmark
				.test("x",
						"{NULLALLOC, mayAlias:[], notMayAlias:[b,a], mustAlias:[b,a], notMustAlias:[i]}");*/
    }
}
