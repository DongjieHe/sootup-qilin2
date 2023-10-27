/**
 * With a context-insensitive heap abstraction, the fields o1 and o2
 * will point to the same heap object.
 */
package pta.objectsensitivity;

import pta.utils.objects.B;

import static pta.utils.Dummy.notAlias;

public class Simple2 {
    static B o;

    public static void main(String[] args) {
        Simple2 simple1 = new Simple2();
        Simple2 simple2 = new Simple2();
        B o = simple1.allocate();
        B o1 = simple2.allocate();
        notAlias(o, o1);
    }

    B allocate() {
        return new B();
    }
}
