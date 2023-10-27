package pta.select;

import pta.utils.Dummy;

public class NewAcceptor {
    Object f;

    static void foo(NewAcceptor a, NewAcceptor b) {
        NewAcceptor x = new NewAcceptor();
        bar(a, x, x, b);
    }

    static void bar(NewAcceptor a, NewAcceptor x, NewAcceptor y, NewAcceptor b) {
        y.f = b.f;
        a.f = x.f;
    }

    public static void main(String[] args) {
        NewAcceptor a1 = new NewAcceptor(), a2 = new NewAcceptor(), b1 = new NewAcceptor(), b2 = new NewAcceptor();
        b1.f = new Object();
        b2.f = new Object();
        foo(a1, b1);
        foo(a2, b2);
        Dummy.notAlias(a1.f, a2.f);
    }
}
