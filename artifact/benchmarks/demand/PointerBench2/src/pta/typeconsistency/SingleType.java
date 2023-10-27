package pta.typeconsistency;

/**
 * When CI, m1.f m2.f are both of type A
 * m1 m2 can be merged since when object sensitive, v1.f is only of A
 *
 * @author Ammonia
 */
public class SingleType {
    A f;

    public static void main(String[] args) {
        SingleType m1 = new SingleType();
        SingleType m2 = new SingleType();
        SingleType v1 = new SingleType().id(m1);
        SingleType v2 = new SingleType().id(m2);
        v1.f = new A();
        v2.f = new A();
        v1.f.foo();
    }

    SingleType id(SingleType m) {
        return m;
    }

    static class A {
        void foo() {
            System.out.println("this is A:foo()");
        }
    }
}
