package pta.selectiveobjectsensitivity;

import static pta.utils.Dummy.notAlias;

/**
 * a testcase where object-sens need a depth of 1.
 *
 * @author Ammonia
 */
public class NoMethod {
    B f;

    public static void main(String[] args) {
        NoMethod s1 = new NoMethod();
        NoMethod s2 = new NoMethod();
        s1.f = (new B());
        s2.f = (new BP());
        notAlias(s1.f, s2.f);
        s1.f.foo();
    }
}