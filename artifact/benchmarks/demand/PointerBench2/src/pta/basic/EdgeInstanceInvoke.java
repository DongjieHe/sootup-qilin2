package pta.basic;

import static pta.utils.Dummy.mayAlias;

/**
 * CallEdge for Instance invoke
 *
 * @author Ammonia
 */
public class EdgeInstanceInvoke {
    public static void main(String[] args) throws Exception {
        new EdgeInstanceInvoke().foo();
    }

    void foo() {
        Object A = new Object();
        mayAlias(A, A);
    }
}
