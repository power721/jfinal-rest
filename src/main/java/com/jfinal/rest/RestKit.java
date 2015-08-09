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

import java.util.ArrayList;
import java.util.List;

/**
 * restful请求工具类，两个功能：创建路由和Handler
 * Created by peak on 2015/1/30.
 */
public class RestKit {

    private static final List<RestRoutes> routesList = new ArrayList<RestRoutes>();

    /**
     * 创建路由
     *
     * @param visitPath 访问路径，如/v1，/v2
     * @param routes    路由，jfinal自带的路由
     * @param pack      包名，将会扫描该下带有@Api注解的controller
     */
    public static void buildRoutes(String visitPath, Routes routes, String pack) {
        RestRoutes restRoutes = new RestRoutes(visitPath, routes);
        //扫描包下的controller
        List<Class<?>> list = ClassScanner.scan(pack);
        for (Class<?> clazz : list) {
            if (!Controller.class.isAssignableFrom(clazz)) {
                continue;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Controller> controllerClass = (Class<? extends Controller>) clazz;
            API api = clazz.getAnnotation(API.class);
            if (api == null) {
                continue;
            }
            String restKey = api.value();
            restRoutes.addRoute(restKey, controllerClass);
        }
        routesList.add(restRoutes);
    }

    /**
     * 构建handler
     *
     * @param handlers
     */
    public static void buildHandler(Handlers handlers) {
        for (RestRoutes routes : routesList) {
            handlers.add(new RestHandler(routes));
        }
    }

    /**
     * 切割key里包含的斜杠，去掉末尾的，保留开头的（没有就加上）
     *
     * @param key
     * @return
     */
    static String cutSlash(String key) {
        if (!key.startsWith("/")) {
            key = "/" + key;
        }
        if (key.endsWith("/")) {
            key = key.substring(0, key.length() - 1);
        }
        return key;
    }

}
