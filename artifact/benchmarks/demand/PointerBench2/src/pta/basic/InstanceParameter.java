package pta.basic;

import static pta.utils.Dummy.mayAlias;

public class InstanceParameter {
    static Object o = new Object();

    public static void main(String[] args) {
        new InstanceParameter().foo(o);
    }

    void foo(Object a) {
        mayAlias(a, o);
    }
}
