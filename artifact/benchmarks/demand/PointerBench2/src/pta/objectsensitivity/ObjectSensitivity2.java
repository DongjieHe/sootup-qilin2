package pta.objectsensitivity;

import static pta.utils.Dummy.notAlias;

/**
 * a testcase where object-sens need a depth of 2.
 *
 * @author Ammonia
 */
class ObjectSensitivity2 {
    public static void main(String[] argv) {
        ObjectSensitivity2 a = new ObjectSensitivity2();//os1
        ObjectSensitivity2 b = new ObjectSensitivity2();//os2
        Object v1 = new Object();//o1
        Object v2 = new Object();//o2
        Object v3 = a.id2(v1);
        Object v4 = b.id2(v2);
        notAlias(v3, v4);
    }

    Object id2(Object a) {
        I i = new I();//oi
        return i.id(a);
    }

    class I {
        Object id(Object a) {
            return a;
        }
    }
}
