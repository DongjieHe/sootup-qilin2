package pta.selectiveobjectsensitivity;

import static pta.utils.Dummy.notAlias;

/**
 * test for (k=1) .
 *
 * @author Ammonia
 */
class CallsiteSensitivitySummary {
    static Object id(Object a) {
        return a;
    }

    static Object id2(Object a) {
        return id(a);
    }

    public static void main(String[] argv) {
        Object o1 = new Object();
        Object o2 = new Object();
        Object v1 = id2(o1);
        Object v2 = id2(o2);
        notAlias(v1, v2);
    }
}
