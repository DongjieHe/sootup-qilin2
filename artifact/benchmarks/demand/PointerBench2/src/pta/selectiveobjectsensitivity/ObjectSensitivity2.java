package pta.selectiveobjectsensitivity;

import static pta.utils.Dummy.notAlias;

/**
 * a testcase where object-sens need a depth of 2.
 *
 * @author Ammonia
 */
class ObjectSensitivity2 {
    public static void main(String[] argv) {
        ObjectSensitivity2 a = new ObjectSensitivity2();
        ObjectSensitivity2 b = new ObjectSensitivity2();
        Object v1 = new Object();
        Object v2 = new Object();
        Object v3 = a.id2(v1);
        Object v4 = b.id2(v2);
        notAlias(v3, v4);
    }

    Object id2(Object a) {
        I i = new I();
        i.id(a);
        return a;
    }

    class I {
        Object id(Object a) {
            return a;
        }
    }
}
