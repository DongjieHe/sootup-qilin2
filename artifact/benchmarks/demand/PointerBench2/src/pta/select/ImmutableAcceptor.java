package pta.select;

import pta.utils.Dummy;

public class ImmutableAcceptor {
    int i;

    public static void main(String[] args) {
        ImmutableAcceptor a1 = new ImmutableAcceptor(), a2 = new ImmutableAcceptor(), b1 = new ImmutableAcceptor(), b2 = new ImmutableAcceptor();
        b1.i = 1;
        b2.i = 2;
        a1.setI(b1);
        a2.setI(b2);
        Dummy.notAlias(a1.i, a2.i);
    }

    void setI(int i) {
        this.i = i;
    }

    void setI(ImmutableAcceptor b) {
        int i = b.i;
        setI(i);
    }
}
