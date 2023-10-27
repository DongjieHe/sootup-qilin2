package pta.selectiveobjectsensitivity;

import static pta.utils.Dummy.notAlias;

/**
 * a testcase where object-sens need a depth of 2.
 *
 * @author Ammonia
 */
public class Set2Get2 {
    Set1Get1 g;

    public Set2Get2(Set1Get1 g) {
        this.g = g;
    }

    public static void main(String[] argv) {
        Set2Get2 a = new Set2Get2(new Set1Get1());
        Set2Get2 b = new Set2Get2(new Set1Get1P());
        a.setF(new B());
        b.setF(new BP());
        notAlias(a.getF(), b.getF());
        a.getF().foo();
    }

    public B getF() {
        return g.getF();
    }

    public void setF(B f) {
        g.setF(f);
    }
}
