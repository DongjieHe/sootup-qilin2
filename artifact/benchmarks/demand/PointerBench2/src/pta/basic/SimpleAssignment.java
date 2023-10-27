package pta.basic;

import static pta.utils.Dummy.mayAlias;

/**
 * basic testcase where simple assignment in a static method
 *
 * @author Ammonia
 */
public class SimpleAssignment {
    public static void main(String[] argv) {
        Object a = new Object();
        Object b = a;
        mayAlias(a, b);
    }
}
