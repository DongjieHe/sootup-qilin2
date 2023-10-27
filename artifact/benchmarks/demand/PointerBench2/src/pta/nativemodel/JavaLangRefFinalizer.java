package pta.nativemodel;

import java.io.FileNotFoundException;

import static pta.utils.Dummy.mayAlias;

public class JavaLangRefFinalizer {
    static Object f;

    static native void invokeFinalizeMethod(Object object);

    public static void main(String[] args) throws FileNotFoundException {
        invokeFinalizeMethod(new JavaLangRefFinalizer());
        mayAlias(f, f);
    }

    void foo() {
        f = new Object();
    }
}
