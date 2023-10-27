package pta.selectiveobjectsensitivity;

/*
 * @testcase ObjectSensitivity5
 * @description Object sensitive alias from caller object
 */
public class ObjectSensitivity5 {

    public static void main(String[] args) {
//	B b1 = new B();
//	B b2 = new BP();

        A a1 = new A();
        A a2 = new A();
        a1.setB();
        a2.setBP();
//	a1.id(b1);
//	a2.id(b2);
        a1.fFoo();

    }

    static class A {
        public B f;

        public A() {
        }

        public A(B b) {
            this.f = b;
        }

        static private void foo(A a, B f) {
            a.f = f;
        }

        void setB() {
            f = new B();
        }

        void setBP() {
            f = new BP();
        }

        public B id(B b) {
            return b;
        }

        public void fFoo() {
            f.foo();
        }

        public void idFoo() {
            id(null).foo();
        }

    }

    static class B {
        String s;

        void foo() {
            s = "B";
        }
    }

    static class BP extends B {
        @Override
        void foo() {
            s = "BP";
        }
    }
}
