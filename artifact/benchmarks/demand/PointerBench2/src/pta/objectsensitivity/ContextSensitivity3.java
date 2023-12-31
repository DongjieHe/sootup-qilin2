package pta.objectsensitivity;

import pta.utils.objects.A;

import static pta.utils.Dummy.mayAlias;

/*
 * @testcase ContextSensitivity3
 * @description Object sensitive alias from caller object (3-CS)
 */
public class ContextSensitivity3 {

    public ContextSensitivity3() {
    }

    public static void main(String[] args) {
        ContextSensitivity3 cs1 = new ContextSensitivity3();
        cs1.test1();
        cs1.test2();
    }

    public void callee(A a, A b) {
        mayAlias(a, b);
    /*Benchmark.test("b",
        "{allocId:1, mayAlias:[a,b], notMayAlias:[], mustAlias:[a,b], notMustAlias:[]},"
            + "{allocId:2, mayAlias:[a], notMayAlias:[b], mustAlias:[a], notMustAlias:[b]}");*/
    }

    public void test1() {
        //Benchmark.alloc(1);
        A a1 = new A();
        A b1 = a1;
        test11(a1, b1);
    }

    private void test11(A a1, A b1) {
        test111(a1, b1);
    }

    private void test111(A a1, A b1) {
        callee(a1, b1);
    }

    public void test2() {
        A a2 = new A();
        //Benchmark.alloc(2);
        A b2 = new A();
        test22(a2, b2);
    }

    private void test22(A a2, A b2) {
        test222(a2, b2);
    }

    private void test222(A a2, A b2) {
        callee(a2, b2);
    }
}
