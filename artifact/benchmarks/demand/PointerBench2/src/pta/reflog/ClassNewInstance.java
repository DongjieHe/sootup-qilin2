package pta.reflog;

import static pta.utils.Dummy.mayAlias;

/**
 * to test, ClassNewInstance.log file should be put under the same directory
 */
public class ClassNewInstance {
    public static void main(String[] args) throws Exception {
        Object a = ClassNewInstance.class.newInstance();
        mayAlias(a, a);
    }
}
