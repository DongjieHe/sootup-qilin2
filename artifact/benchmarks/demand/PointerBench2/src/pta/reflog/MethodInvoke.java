package pta.reflog;

import static pta.utils.Dummy.mayAlias;

/**
 * to test, MethodInvoke.log file should be put under the same directory
 */
public class MethodInvoke {
    static Object id(Object a) {
        return a;
    }

    ;

    public static void main(String[] args) throws Exception {
        //statci method
        Object a = new Object();
        Object b = MethodInvoke.class.getMethod("id", Object.class).invoke(null, a);
        mayAlias(a, b);

        //instance method
        Object c = new Object();
        Object d = MethodInvoke.class.getMethod("idi").invoke(new MethodInvokeInstance(), c);
        mayAlias(c, d);
    }

    Object idi(Object a) {
        return null;
    }
}

class MethodInvokeInstance extends MethodInvoke {
    Object idi(Object a) {
        return a;
    }
}