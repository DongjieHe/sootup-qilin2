package osdp;

import benchmark.internal.Benchmark;

public class StrBuilder {

    public static void main(String[] args) {
        String[] array = {"Hi", "this", "is", "an", "English", "sentence"};
        StringBuilder builder = new StringBuilder();
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(" " + array[i]);
        }

        Benchmark.pointsToQuery(builder);
    }

}
