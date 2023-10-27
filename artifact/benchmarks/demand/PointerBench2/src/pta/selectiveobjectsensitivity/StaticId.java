package pta.selectiveobjectsensitivity;

public class StaticId {

    static B id(B p) {
        return p;
    }

    public static void main(String[] args) {
        B b1 = new StaticId().foo();
        B b2 = new StaticId().bar();

        //b1.foo();
    }

    private B foo() {
        // TODO Auto-generated method stub
        return id(new B());
    }

    private B bar() {
        // TODO Auto-generated method stub
        return id(new BP());
    }
}
