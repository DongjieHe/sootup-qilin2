package pta.typeconsistency;

/**
 * When CI, m1.f m2.f are both of type A&B
 * m1 m2 can be merged since m1 m2 are always accesed together UNDER flow-insensitive analysis
 *
 * @author Ammonia
 */
public class MultiType2 {
    A f;

    public static void main(String[] args) {
        MultiType2 m1 = new MultiType2();
        MultiType2 m2 = new MultiType2();
        MultiType2 v = new MultiType2().id(m1);
        v = new MultiType2().id(m2);
        v.f = new A();
        v.f = new B();
        v.f.foo();
    }

    MultiType2 id(MultiType2 m) {
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
