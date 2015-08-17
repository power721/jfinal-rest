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

import com.jfinal.config.Handlers;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>restful请求工具类，两个功能：创建路由和Handler</p>
 * <p>Created by peak on 2015/1/30.</p>
 * Refactored by power721
 */
public final class RestKit {

    private static final List<RestRoutes> ROUTES = new ArrayList<RestRoutes>();

    private RestKit() {
    }

    /**
     * 创建路由
     *
     * @param basePath  访问路径，如/v1，/v2
     * @param classPath the class path to find the packages
     * @param routes    路由，jFinal自带的路由
     * @param packages  包名，将会扫描该下带有@Api注解的controller
     */
    public static void buildRoutes(String basePath, String classPath, Routes routes, String... packages) {
        RestRoutes restRoutes = new RestRoutes(basePath, routes);
        //扫描包下的controller
        List<Class<?>> list = ClassScanner.scan(classPath, packages);
        for (Class<?> clazz : list) {
            if (!Controller.class.isAssignableFrom(clazz)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends Controller> controllerClass = (Class<? extends Controller>) clazz;
            API api = clazz.getAnnotation(API.class);
            String classRestPath = "";
            if (api != null) {
                classRestPath = api.value();
                restRoutes.addRoute(classRestPath, controllerClass);
            }

            for (Map.Entry<String, List<Method>> entry : buildMethodLevelAPIs(classRestPath, clazz).entrySet()) {
                restRoutes.addRoute(entry.getKey(), controllerClass, entry.getValue());
            }
        }
        ROUTES.add(restRoutes);
    }

    /**
     * 创建路由
     *
     * @param basePath 访问路径，如/v1，/v2
     * @param routes   路由，jFinal自带的路由
     * @param packages 包名，将会扫描该下带有@Api注解的controller
     */
    public static void buildRoutes(String basePath, Routes routes, String... packages) {
        buildRoutes(basePath, null, routes, packages);
    }

    private static Map<String, List<Method>> buildMethodLevelAPIs(String classRestPath, Class<?> clazz) {
        Map<String, List<Method>> methodMap = new HashMap<String, List<Method>>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterTypes().length != 0 || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            API api = method.getAnnotation(API.class);
            if (api != null) {
                String restPath = api.value();
                if (!restPath.startsWith("/")) {
                    restPath = classRestPath + "/" + restPath;
                }

                List<Method> methods = methodMap.get(restPath);
                if (methods == null) {
                    methods = new ArrayList<Method>();
                    methodMap.put(restPath, methods);
                }
                methods.add(method);
            }
        }

        return methodMap;
    }

    /**
     * 构建handler
     *
     * @param handlers jFinal handler stack
     */
    public static void buildHandler(Handlers handlers) {
        for (RestRoutes routes : ROUTES) {
            handlers.add(new RestHandler(routes));
        }
    }

}
