package pta.selectiveobjectsensitivity;

/**
 * static call.
 *
 * @author Ammonia
 */
class CallsiteSensitivity {
    public static void main(String[] argv) {
        CallsiteSensitivity c1 = new CallsiteSensitivity();
        CallsiteSensitivity c2 = new CallsiteSensitivityP();
        Object o1 = new Object();
        Object o2 = new Object();
        Object v1 = c1.id2(o1);
        Object v2 = c2.id2(o2);
//		notAlias(v1, v2);
    }

    private static Object createObjectP() {
        return new Object();
    }

    private static Object createObject() {
        return new Object();
    }

    Object id(Object a) {
        return a;
    }

    Object id2(Object a) {
        return id(a);
    }
}

class CallsiteSensitivityP extends CallsiteSensitivity {
    Object id(Object a) {
        return a;
    }
}