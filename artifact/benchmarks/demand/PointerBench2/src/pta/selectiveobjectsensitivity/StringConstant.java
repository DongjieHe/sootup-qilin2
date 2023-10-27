package pta.selectiveobjectsensitivity;

/**
 * different String Constant used in poly calls
 *
 * @author Ammonia
 */
public class StringConstant {
    public static void main(String[] argv) {
        String s1 = new StringConstant().foo();
        String s2 = new StringConstantP().foo();
    }

    String foo() {
        return bar();
    }

    String bar() {
        return "StringConstant";
    }
}

class StringConstantP extends StringConstant {
    @Override
    String bar() {
        return "StringConstantP";
    }
}