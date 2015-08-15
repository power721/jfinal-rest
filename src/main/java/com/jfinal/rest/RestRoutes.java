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

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * restful路由
 * Created by peak on 2015/1/23.
 */
class RestRoutes {

    private final String basePath;
    private final Routes routes;
    private final Set<RestPath> restPathSet = new HashSet<RestPath>();

    RestRoutes(String basePath, Routes routes) {
        if (!basePath.startsWith("/")) {
            basePath = "/" + basePath;
        }

        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }

        this.basePath = basePath;
        this.routes = routes;
    }

    String getBasePath() {
        return basePath;
    }

    void addRoute(String restPath, Class<? extends Controller> controllerClass, List<Method> methods) {
        if (restPath.startsWith("/")) {
            restPath = restPath.substring(1);
        }
        routes.add(basePath + restPath, controllerClass);
        RestPath path = new RestPath(restPath, controllerClass, methods);
        if (restPathSet.contains(path)) {
            throw new RuntimeException("Duplicate restPath：" + path);
        }
        restPathSet.add(path);
    }

    void addRoute(String restPath, Class<? extends Controller> controllerClass) {
        addRoute(restPath, controllerClass, null);
    }

    /**
     * 匹配请求
     *
     * @param target  the original url
     * @param request HttpServletRequest
     * @return new target
     */
    String match(String target, HttpServletRequest request) {
        if (!target.startsWith(basePath)) {
            return null;
        }

        String path = target.substring(basePath.length());

        for (RestPath restPath : restPathSet) {
            String newRestPath = restPath.match(path, request);
            if (newRestPath != null) {
                return basePath + newRestPath;
            }
        }
        return null;
    }

}
