package pta.objectsensitivity;

/**
 * original testcase where object-sens only need a depth of 1 while callsite-sens needs depth 3.
 *
 * @author Ammonia
 */
class ObjectSensitivity {
    public static void main(String[] argv) {
        ObjectSensitivity a = new ObjectSensitivity();
        ObjectSensitivity b = new ObjectSensitivity();
        Object o1 = new Object();
//        Object o2 = new Object();
//        Object o3 = a.id2(o1);
//        Object o4 = b.id2(o2);
//        notAlias(o3, o4);
    }

//    Object id(Object a) {
//        return a;
//    }
//
//    Object id2(Object a) {
//        return id(a);
//    }
//
//    Object id3(Object a) {
//        return id2(a);
//    }
}
