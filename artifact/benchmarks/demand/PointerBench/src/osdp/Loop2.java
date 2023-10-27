package osdp;

import benchmark.internal.Benchmark;

class N {
    public N next;

    public N() {
        next = new N();
    }
}

public class Loop2 {

    public static void main(String[] args) {
        N node = new N();

        int i = 0;
        if (i < 10) {
            node = node.next;
        }

        Benchmark.pointsToQuery(node);
    }

}
