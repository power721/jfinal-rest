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
import java.util.HashSet;
import java.util.Set;

/**
 * restful路由
 * Created by peak on 2015/1/23.
 */
class RestRoutes {

    private String visitPath;
    private Routes routes;
    private Set<RestKey> restKeySet = new HashSet<RestKey>();

    RestRoutes(String visitPath, Routes routes) {
        this.visitPath = RestKit.cutSlash(visitPath);
        this.routes = routes;
    }

    String getVisitPath() {
        return visitPath;
    }

    void addRoute(String restKey, Class<? extends Controller> controllerClass) {
        restKey = RestKit.cutSlash(restKey);

        RestKey key = new RestKey(restKey);
        if (restKeySet.contains(key)) {
            throw new RuntimeException("restKey重复：" + key);
        }

        routes.add(visitPath + restKey, controllerClass);
        restKeySet.add(key);
    }

    /**
     * 匹配请求
     *
     * @param target
     * @param request
     * @return
     */
    String match(String target, HttpServletRequest request) {
        String key = target;
        if (visitPath != null) {
            key = key.substring(visitPath.length());
        }
        for (RestKey restKey : restKeySet) {
            String t = restKey.match(key, request);
            if (t != null) {
                return visitPath + t;
            }
        }
        return null;
    }
}
