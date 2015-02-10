package com.jfinal.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类扫描器
 * Created by peak on 2015/1/27.
 */
public class ClassScanner {

    private ClassScanner() {
    }

    private static ClassLoader classLoader = null;
    private static String classPath = null;

    static {
        classLoader = ClassScanner.class.getClassLoader();
        classPath = classLoader.getResource("").getFile();
        classPath = new File(classPath).getAbsolutePath();
    }

    /**
     * 扫描包
     *
     * @param pack 包名
     * @return
     */
    public static List<Class> scan(String pack) {
        String classPath = classLoader.getResource("").getFile();
        String path = classPath + pack.replace(".", "/");
        File dir = new File(path);
        if (!dir.isDirectory()) {
            return null;
        }
        try {
            return scan(dir);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Class> scan(File dir) throws ClassNotFoundException {
        List<Class> list = new ArrayList<Class>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String filePath = file.getAbsolutePath();
                if (!filePath.endsWith(".class")) {
                    continue;
                }
                //去掉classPath
                String className = filePath.substring(classPath.length());
                if (className.startsWith(File.separator)) {
                    className = className.substring(1);
                }
                //去掉.class
                className = className.substring(0, className.length() - 6);
                className = className.replace(File.separator, ".");
                list.add(classLoader.loadClass(className));
                continue;
            }
            if (file.isDirectory()) {
                list.addAll(scan(file));
            }
        }
        return list;
    }

}