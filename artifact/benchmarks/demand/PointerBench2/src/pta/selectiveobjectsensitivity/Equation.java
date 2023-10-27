package pta.selectiveobjectsensitivity;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 */
public class Equation {
    static double a;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner("");
        a = scanner.nextDouble();
//		scanner.close();
//	
//		PrintStream out=new PrintStream("b");
//		out.printf("%1$.2f %2$.2f %3$.2f\n",1,2,3);
//		out.close();
//		
//		HashMap<Object, Object> map = new HashMap<>();
//		map.put(1,null);
//		map.entrySet().iterator().next().getKey().toString();
    }

    static class A {
        @Override
        public String toString() {
            return "A";
        }
    }
}
