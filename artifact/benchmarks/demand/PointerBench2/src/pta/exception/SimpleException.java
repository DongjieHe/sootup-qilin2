package pta.exception;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SimpleException {
    public static void main(String[] args) throws Exception {

        try {
            try {
                foo();
            } catch (FileNotFoundException | EOFException io) {

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("haha");
        } finally {
        }
    }

    static void foo() throws IOException {
        throw new IOException();
    }
}
