package pta.selectiveobjectsensitivity;

import static pta.utils.Dummy.notAlias;

public class ReturnThis {
    Object f;

    public ReturnThis() {
    }

    public ReturnThis(Object f) {
        this.f = f;
    }

    static ReturnThis id(ReturnThis p) {
        return p;
    }

    public static void main(String[] args) {
        ReturnThis o1 = new ReturnThis(new Object());
        ReturnThis o2 = new ReturnThis2(new Object());
        o1 = o1.id();
        o2 = o2.id();
        notAlias(o1, o2);
        o1.foo();
    }

    protected ReturnThis id() {
        return this;
    }

    void foo() {
    }

    static class ReturnThis2 extends ReturnThis {
        public ReturnThis2(Object object) {
            super(object);
        }

        @Override
        void foo() {
        }
    }
}
