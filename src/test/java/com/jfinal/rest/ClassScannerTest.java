package com.jfinal.rest;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;


public class ClassScannerTest {

    @Test
    public void test() {
        List<Class<?>> list = ClassScanner.scan("com.jfinal");
        assertTrue(list.contains(ClassScannerTest.class));
    }
}
