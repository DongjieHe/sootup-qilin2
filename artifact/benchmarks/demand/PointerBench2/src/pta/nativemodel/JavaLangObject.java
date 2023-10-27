package pta.nativemodel;

import static pta.utils.Dummy.mayAlias;

public class JavaLangObject {
    public static void main(String[] args) {
        JavaLangObject a = new JavaLangObject();
        Object b = a.clone();
        mayAlias(a, b);
    }

    protected native Object clone();
}
