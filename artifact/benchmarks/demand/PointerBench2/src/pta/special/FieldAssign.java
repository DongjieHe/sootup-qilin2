package pta.special;

import static pta.utils.Dummy.notAlias;

public class FieldAssign {
    static FieldAssign v;
    Object f, g;

    public static void main(String[] args) {
        FieldAssign v1 = new FieldAssign();
        FieldAssign v2 = new FieldAssign();
        v1.f = new Object();
        v2.f = new Object();
        v = v1;
        v = v2;
        v.g = v.f;
        //v.setG();
        notAlias(v1.g, v2.g);
    }

    void setG() {
        g = f;
    }
}
