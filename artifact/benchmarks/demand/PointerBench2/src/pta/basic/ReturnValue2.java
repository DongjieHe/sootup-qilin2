package pta.basic;

import pta.utils.objects.A;

import static pta.utils.Dummy.mayAlias;

/*
 * @testcase ReturnValue2
 * @description Alias to a return value from a static method
 */
public class ReturnValue2 {

    public ReturnValue2() {
    }

    public static void main(String[] args) {

        A a = new A();
        ReturnValue2 x = new ReturnValue2();
        A b = x.id(a);
        mayAlias(a, b);
    }

    public A id(A x) {
        return x;
    }
}
