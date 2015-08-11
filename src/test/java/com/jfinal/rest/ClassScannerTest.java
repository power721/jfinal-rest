package com.jfinal.rest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.util.List;

/**
 * java类扫描器测试
 * Created by peak on 2015/2/9.
 */
public class ClassScannerTest {

    @Test
    public void test() {
        List<Class<?>> list = ClassScanner.scan("com.jfinal.rest");
        assertTrue(list.contains(ClassScannerTest.class));
    }
}
