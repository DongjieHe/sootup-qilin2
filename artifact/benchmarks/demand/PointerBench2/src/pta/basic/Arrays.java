package pta.basic;

import static pta.utils.Dummy.mayAlias;

public class Arrays {
    public static void main(String[] ps) {
        dummyAllocations();
        ArrayTest1.test();
        ArrayTest2.test();
        ArrayTest3.test();
        ArrayTest4.test();
        ArrayTest5.test();
        ArrayTest6.test();
        ArrayTest7.test();
    }

    /**
     * Allocate some arrays to make sure some array types used in the
     * testsuite are available.
     */
    public static void dummyAllocations() {
        Object o1 = new Integer[5];
        Object o2 = new Integer[5][5];
        Object o3 = new java.io.Serializable[5];
        Object o4 = new java.io.Serializable[5][5];
        Object o5 = new java.util.Collection[5][5];
        Object o6 = new java.util.List[5][5];

        float[] xs = new float[5];
        java.util.TreeSet set = new java.util.TreeSet();
        java.util.ArrayList list = new java.util.ArrayList();
    }
}

class ArrayTest1 {
    static Object o1;
    static Object o2;

    public static void test() {
        Object[] xs = new Object[5];
        xs[0] = new Object();
        xs[1] = new Object();

        o1 = xs[0];
        o2 = xs[1];

        mayAlias(xs[0], o1);
        mayAlias(xs[1], o2);

    }
}

class ArrayTest2 {
    static java.io.Serializable[] allocSerializable;
    static java.util.Collection[] allocList;
    static java.util.List[] allocArrayList;

    static java.lang.Object[] assignObject;

    static java.io.Serializable[] assignSerializable;
    static java.util.Collection[] assignCollection;
    static java.util.List[] assignList;

    static java.util.List[] castList;
    static java.util.ArrayList[] castArrayList;

    public static void test() {
        allocSerializable = new java.io.Serializable[5];
        allocList = new java.util.List[5];
        allocArrayList = new java.util.ArrayList[5];

        testassign();
        testcast();
        java.io.Serializable[] serial = allocSerializable;
        java.util.Collection[] list = allocList;
        java.util.List[] arraylist = allocArrayList;

        mayAlias(allocSerializable, serial);
        mayAlias(allocList, list);
        mayAlias(allocArrayList, arraylist);

    }

    public static void testassign() {
        assignObject = allocSerializable;
        assignObject = allocList;
        assignObject = allocArrayList;

        assignSerializable = allocSerializable;

        assignCollection = allocList;
        assignCollection = allocArrayList;

        assignList = allocArrayList;
    }

    public static void testcast() {
        castList = (java.util.List[]) allocList;
        castList = (java.util.List[]) allocArrayList;
        castArrayList = (java.util.ArrayList[]) allocArrayList;
    }
}

class ArrayTest3 {
    // o1 will point to Object/0
    static Object o;

    public static void test() {
        Object[][][] xs = new Object[5][5][5];
        xs[1][1][1] = new Object();
        o = xs[1][1][1];
        mayAlias(xs[1][1][1], o);
    }
}

class ArrayTest4 {
    // o1 should point to Object/0 and Object/1
    static Object o1;

    // o2 should point to Object/0 and Object/1
    static Object o2;

    public static void test() {
        Object[][][] xs = new Object[5][5][5];
        Object[] subarray = new Object[5];
        xs[1][1] = subarray;

        xs[1][1][1] = new Object();
        subarray[1] = new Object();

        o1 = xs[1][1][1];
        o2 = subarray[1];
        mayAlias(xs[1][1][1], o1);
        mayAlias(subarray[1], o2);
    }
}

class ArrayTest5 {
    // o should point to Object/1
    static Object o;

    public static void test() {
        Object[][][] xs = new Object[5][5][5];
        Object[] subarray = xs[1][1];
        subarray[1] = new Object();

        o = xs[1][1][1];
        mayAlias(o, xs[1][1][1]);
    }
}

class ArrayTest6 {
    static Object o1;
    static Object o2;

    public static void test() {
        Object[] xs = new Object[5];
        xs[0] = new Object();
        xs[1] = new Object();

        Object[] ys = new Object[5];
        System.arraycopy(xs, 0, ys, 0, 2);

        o1 = xs[0];
        o2 = ys[1];
        mayAlias(o1, xs[0]);
        mayAlias(o1, ys[0]);
        mayAlias(o2, ys[1]);
        mayAlias(o2, xs[1]);
    }
}

class ArrayTest7 {
    static Object o1;
    static Object o2;

    public static void test() {
        Object[] xs = new Object[5];
        xs[1] = new String();
        xs[2] = new String();

        Object[] ys = new Integer[5];
        System.arraycopy(xs, 0, ys, 0, 2);

        o1 = xs[0];
        o2 = ys[1];
        mayAlias(o1, xs[0]);
        mayAlias(xs[0], ys[0]); //ByteCode implementation of java methods not yet supported?

    }
}

