package osdp;

import benchmark.internal.Benchmark;

import java.util.ArrayList;

public class List1 {
    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        Object a = new Object();
        list.add(a);
        Object c = list.get(0);
        Benchmark.pointsToQuery(c);
        Benchmark.mayAliasQuery(c, a, true);
    }
}
