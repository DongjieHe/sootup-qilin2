package pta.special;

public class Flow {
    Object f;
    Flow g;

    public static void main(String[] args) {
        Flow flow = new Flow();
        flow.run();
    }

    private void run() {
        Flow a = new Flow();
        a.f = "a.f";
        Flow b = new Flow();
        b.f = "b.f";
        Object l;

        g = a;
        l = g.f;
        g = b;
        g.f = l;
    }
}
