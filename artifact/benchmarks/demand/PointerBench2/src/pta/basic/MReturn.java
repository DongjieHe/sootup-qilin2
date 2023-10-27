package pta.basic;

public class MReturn {

    public static void main(String[] args) {
        foo(1);
    }

    private static Integer foo(int x) {
        if (x > 0)
            return (Integer) 1;
        else
            return (Integer) 0;
    }
}
