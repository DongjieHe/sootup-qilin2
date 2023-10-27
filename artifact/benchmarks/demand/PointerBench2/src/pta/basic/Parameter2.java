package pta.basic;

import pta.utils.objects.A;

import static pta.utils.Dummy.mayAlias;

/*
 * @testcase ParameterAlias2
 * @description Aliasing through non static method parameter
 */
public class Parameter2 {

    public Parameter2() {
    }

    public static void main(String[] args) {
        A a = new A();
        Parameter2 p2 = new Parameter2();
        p2.test(a);
    }

    public void test(A x) {
        A b = x;
        mayAlias(b, x);
    }
}
