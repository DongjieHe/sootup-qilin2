package pta.typeconsistency;

/**
 * When CI, m1.f m2.f are both of type A&B
 * m1 m2 cannot be merged since when object sensitive, v1.f is only of A
 *
 * @author Ammonia
 */
public class MultiType {
    A f;

    public static void main(String[] args) {
        MultiType m1 = new MultiType();
        MultiType m2 = new MultiType();
        MultiType v1 = new MultiType().id(m1);
        MultiType v2 = new MultiType().id(m2);
        v1.f = new A();
        v2.f = new B();
        v1.f.foo();
    }

    MultiType id(MultiType m) {
        return m;
    }

    static class A {
        void foo() {
            System.out.println("this is A:foo()");
        }
    }

    static class B extends A {
        @Override
        void foo() {
            System.out.println("this is B:foo()");
        }
    }
}
