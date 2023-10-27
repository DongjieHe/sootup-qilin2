package pta.basic;

import static pta.utils.Dummy.mayAlias;

public class InstanceIdent {
    static InstanceIdent o;

    public static void main(String[] args) {
        o = new InstanceIdent();
        o.foo();
    }

    void foo() {
        mayAlias(this, o);
    }
}
