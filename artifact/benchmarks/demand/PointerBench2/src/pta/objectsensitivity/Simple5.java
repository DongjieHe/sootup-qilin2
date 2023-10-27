/**
 * With a context-insensitive heap abstraction, the fields o1 and o2
 * will point to the same heap object.
 */
package pta.objectsensitivity;

import pta.utils.objects.B;

import static pta.utils.Dummy.mayAlias;
import static pta.utils.Dummy.notAlias;

public class Simple5 {
    static B o;

    public static void main(String[] args) {
        Simple5 simple1 = new Simple5();
        Simple5 simple2 = new Simple5();
        simple1.o = simple1.call();
        simple2.o = simple2.call();
        B o1 = simple1.o;
        B o2 = simple2.o;
        B o1a = simple1.allocate();
        B o2a = simple2.allocate();

        mayAlias(o1, o2);
        mayAlias(o1a, o1);
        mayAlias(o2a, o2);
        notAlias(o2a, o1a);


    }

    B call() {
        return allocate();
    }

    B allocate() {
        return new B();
    }

}
