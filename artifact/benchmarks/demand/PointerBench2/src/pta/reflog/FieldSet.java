package pta.reflog;

import static pta.utils.Dummy.mayAlias;
import static pta.utils.Dummy.notAlias;

/**
 * to test, FieldSet.log file should be put under the same directory
 */
public class FieldSet {
    static Object F;
    Object f;

    public static void main(String[] args) throws Exception {
        //static field
        Object a = new Object();
        FieldSet.class.getField("F").set(null, a);
        Object b = F;
        mayAlias(a, b);

        //instance field
        FieldSet o = new FieldSet();
        FieldSet op = new FieldSet();
        Object c = new Object();
        Object cp = new Object();
        FieldSet.class.getField("f").set(o, c);
        FieldSet.class.getField("f").set(op, cp);
        Object d = o.f;
        mayAlias(c, d);
        notAlias(cp, d);
    }
}