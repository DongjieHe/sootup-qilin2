package pta.basic;

/**
 * CallEdge for static invoke
 *
 * @author Ammonia
 */
public class EdgeStaticInvoke {
    static void foo() {
        bar();
    }

    static void bar() {
        tar();
    }

    static void tar() {
    }

    public static void main(String[] args) {
        foo();
    }
}
