package pta.objectsensitivity;

import pta.utils.objects.B;

import static pta.utils.Dummy.notAlias;

public class Simple3 {
    static B o;

    public static void main(String[] args) {
        Simple3 simple1 = new Simple3();
        Simple3 simple2 = new Simple3();
        B o = simple1.call();
        B o1 = simple2.call();
        notAlias(o, o1);
    }

    B call() {
        return allocate();
    }

    B allocate() {
        return new B();
    }
}
