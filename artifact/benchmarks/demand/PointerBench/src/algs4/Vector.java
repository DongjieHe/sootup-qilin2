package algs4;

import benchmark.internal.Benchmark;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Vector {
    private double[] data;

    public Vector(int d) {
        data = new double[d];
        Benchmark.pointsToQuery(data);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Vector z = new Vector(5);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
        out.println(z);
    }
}
