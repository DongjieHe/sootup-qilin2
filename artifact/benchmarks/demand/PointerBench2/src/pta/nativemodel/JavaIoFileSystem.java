package pta.nativemodel;

import java.io.File;

import static pta.utils.Dummy.mayAlias;

public class JavaIoFileSystem {
    static native Object getFileSystem();

    public static void main(String[] args) {
        Object a = getFileSystem();
        mayAlias(a, a);
        String[] sa = new JavaIoFileSystem().list(null);
        mayAlias(sa, sa);
    }

    native String[] list(File file);
}
