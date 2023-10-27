package pta.reflog;

import static pta.utils.Dummy.mayAlias;

/**
 * to test, ConstrucorNewInstance.log file should be put under the same directory
 */
public class ConstructorNewInstance {
    Object f;

    public ConstructorNewInstance(Object f) {
        this.f = f;
    }

    public static void main(String[] args) throws Exception {
        Object f = new Object();
        ConstructorNewInstance a = ConstructorNewInstance.class.getConstructor(Object.class).newInstance(f);
        mayAlias(f, a.f);
    }
}
