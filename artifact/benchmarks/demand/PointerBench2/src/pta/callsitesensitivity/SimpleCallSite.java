/**
 * With a context-insensitive heap abstraction, the fields o1 and o2
 * will point to the same heap object.
 */
package pta.callsitesensitivity;

import pta.utils.objects.B;

import static pta.utils.Dummy.notAlias;

public class SimpleCallSite {
    static B o;

    public static void main(String[] args) {
        SimpleCallSite simple = new SimpleCallSite();
        o = simple.allocate();

        B o1 = simple.allocate();
        notAlias(o, o1); //?

    }

    B allocate() {
        return new B();
    }

}
