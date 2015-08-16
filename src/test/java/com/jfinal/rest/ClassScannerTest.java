package com.jfinal.rest;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;


public class ClassScannerTest {

    @Test
    public void test() {
        List<Class<?>> list = ClassScanner.scan(null, "com.jfinal.rest");
        assertTrue(list.contains(ClassScannerTest.class));
    }

}
