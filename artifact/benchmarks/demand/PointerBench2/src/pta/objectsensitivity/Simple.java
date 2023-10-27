/**
 * With a context-insensitive heap abstraction, the fields o1 and o2
 * will point to the same heap object.
 */
package pta.objectsensitivity;

import pta.utils.objects.B;

import static pta.utils.Dummy.mayAlias;

public class Simple {
    static B o;

    public static void main(String[] args) {
        Simple simple = new Simple();
        o = simple.allocate();

        B o1 = simple.allocate();
        mayAlias(o, o1);

    }

    B allocate() {
        return new B();
    }

}
