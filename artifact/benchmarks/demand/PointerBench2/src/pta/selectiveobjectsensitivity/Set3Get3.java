package pta.selectiveobjectsensitivity;

import static pta.utils.Dummy.notAlias;

/**
 * a testcase where object-sens need a depth of 2.
 *
 * @author Ammonia
 */
class Set3Get3 {
    Set2Get2 h = new Set2Get2(new Set1Get1());

    public static void main(String[] argv) {
        Set3Get3 a = new Set3Get3();
        Set3Get3 b = new Set3Get3();
        a.setF(new B());
        b.setF(new BP());
        notAlias(a.getF(), b.getF());
        a.getF().foo();
    }

    public B getF() {
        return h.getF();
    }

    public void setF(B f) {
        h.setF(f);
    }
}
