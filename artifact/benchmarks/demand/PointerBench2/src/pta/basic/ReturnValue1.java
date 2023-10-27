package pta.basic;

import pta.utils.objects.A;

import static pta.utils.Dummy.mayAlias;

public class ReturnValue1 {


    public static A id(A x) {
        return x;
    }

    public static void main(String[] args) {

        A a = new A();
        A b = id(a);
        mayAlias(a, b);
    }
}
