package pta.nativemodel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static pta.utils.Dummy.mayAlias;

public class JavaLangSystem {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream newInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        System.setIn(newInputStream);
        mayAlias(newInputStream, System.in);

        PrintStream newPrintStream = new PrintStream("");
        System.setOut(newPrintStream);
        mayAlias(newPrintStream, System.out);

        System.setErr(newPrintStream);
        mayAlias(newPrintStream, System.err);

        Object[] src = new Object[]{new Object()}, dest = new Object[1];
        System.arraycopy(src, 0, dest, 0, 1);
        mayAlias(src[0], dest[0]);
    }
}
