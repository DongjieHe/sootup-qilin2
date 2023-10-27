package pta.selectiveobjectsensitivity;

import static pta.utils.Dummy.notAlias;

/**
 * a testcase where object-sens need a depth of 1.
 *
 * @author Ammonia
 */
public class Set1Get1 {
    B f;

    public static void main(String[] args) {
        Set1Get1 s1 = new Set1Get1();
        Set1Get1 s2 = new Set1Get1();
        s1.setF(new B());
        s2.setF(new BP());
        notAlias(s1.getF(), s2.getF());
        s1.getF().foo();
    }

    public B getF() {
        return f;
    }

    public void setF(B f) {
        this.f = f;
    }
}

class Set1Get1P extends Set1Get1 {
    //	B f;
    public B getF() {
        return f;
    }

    public void setF(B f) {
        this.f = f;
    }
}