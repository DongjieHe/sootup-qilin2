package pta.selectiveobjectsensitivity;

/**
 * a testcase where object-sens need a depth of 2.
 *
 * @author Ammonia
 */
class ObjectNotCaller {
    public static void main(String[] argv) {
        ObjectNotCaller a = new ObjectNotCaller();//os1
        Object v1 = new Object();//o1
        I i = new I();//oi
        Object v3 = a.id2(i, v1);
    }

    Object id2(I i, Object a) {
        return i.id(a);
    }

    static class I {
        Object id(Object a) {
            return a;
        }
    }
}
