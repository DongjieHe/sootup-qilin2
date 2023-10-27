package pta.typeconsistency;

/**
 * When CI, m1.f m2.f are both of type A
 * m1 m2 cannot be merged since when object sensitive, v1.f is null
 *
 * @author Ammonia
 */
public class SingleType2 {
    A f;

    public static void main(String[] args) {
        SingleType2 m1 = new SingleType2();
        SingleType2 m2 = new SingleType2();
        SingleType2 v1 = new SingleType2().id(m1);
        SingleType2 v2 = new SingleType2().id(m2);
        v1.f = null;
        v2.f = new A();
        v1.f.foo();
    }

    SingleType2 id(SingleType2 m) {
        return m;
    }

    static class A {
        void foo() {
            System.out.println("this is A:foo()");
        }
    }
}
