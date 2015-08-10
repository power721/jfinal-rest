/**
 * Copyright 2015 Peak Tai,台俊峰(taijunfeng_it@sina.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类扫描器
 * Created by peak on 2015/1/27.
 */
final class ClassScanner {

    private static final ClassLoader classLoader;
    private static final String classPath;

    static {
        classLoader = ClassScanner.class.getClassLoader();
        classPath = classLoader.getResource("").getPath();
    }

    private ClassScanner() {
    }

    /**
     * 扫描包
     *
     * @param pack 包名
     * @return list of restful classes
     */
    @SuppressWarnings("unchecked")
    public static List<Class<?>> scan(String pack) {
        String path = classPath + pack.replace(".", "/");
        File dir = new File(path);
        if (!dir.isDirectory()) {
            return Collections.EMPTY_LIST;
        }

        try {
            return scan(dir);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Class<?>> scan(File dir) throws ClassNotFoundException {
        List<Class<?>> list = new ArrayList<Class<?>>();
        File[] files = dir.listFiles();
        if (files == null) {
            return list;
        }

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
                className = className.substring(0, className.length() - ".class".length());
                className = className.replace(File.separator, ".");
                list.add(classLoader.loadClass(className));
            } else if (file.isDirectory()) {
                list.addAll(scan(file));
            }
        }
        return list;
    }

}
