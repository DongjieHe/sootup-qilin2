package pta.reflog;

import static pta.utils.Dummy.mayAlias;
import static pta.utils.Dummy.notAlias;

/**
 * to test, FieldGet.log file should be put under the same directory
 */
public class FieldGet {
    static Object F;
    Object f;

    public static void main(String[] args) throws Exception {
        //static field
        Object a = new Object();
        F = a;
        Object b = FieldGet.class.getField("F").get(null);
        mayAlias(a, b);

        //instance field
        FieldGet o = new FieldGet();
        FieldGet op = new FieldGet();
        Object c = new Object();
        Object cp = new Object();
        o.f = c;
        op.f = cp;
        Object d = FieldGet.class.getField("f").get(o);
        ;
        mayAlias(c, d);
        notAlias(cp, d);
    }
}