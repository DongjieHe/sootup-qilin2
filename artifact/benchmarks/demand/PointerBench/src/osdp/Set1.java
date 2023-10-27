package osdp;

import benchmark.internal.Benchmark;

import java.util.TreeSet;

public class Set1 {

    public static void main(String[] args) {

        TreeSet<Object> set = new TreeSet<Object>();
        Object a = new Object();
        set.add(a);
        for (Object i : set) {
            Benchmark.pointsToQuery(i);
        }
    }
}
