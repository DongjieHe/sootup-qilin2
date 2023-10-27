package pta.special;

import java.util.Map;

public class NullCheckPattern {

    private Map<Integer, Double> derivatives = null;

    public static void main(String[] args) {
        NullCheckPattern a = new NullCheckPattern();
        a.getDeribative(0);
    }

    //	private double value;
//
//	private void SparseGradient(final double value, final Map<Integer, Double> derivatives) {
//        this.value = value;
//        this.derivatives = new HashMap<Integer, Double>();
//        if (derivatives != null) {
//            this.derivatives.putAll(derivatives);
//        }
//    }
    public double getDeribative(final int index) {
        final Double out = derivatives.get(index);
        return (out == null) ? 0 : out;
    }
}
