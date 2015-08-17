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

import com.jfinal.core.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rest path or action key for jFinal
 * Created by peak on 2015/1/23.
 * refactored by power721
 */
class RestPath {

    private final boolean isClassLevel;
    private final String originPath;
    private final Map<String, String> methodMap;
    private final List<Segment> segments;

    RestPath(String originPath, Class<? extends Controller> controllerClass, List<Method> methods) {
        this.isClassLevel = (methods == null);
        this.originPath = originPath;
        if (originPath.startsWith("/")) {
            originPath = originPath.substring(1);
        }

        String[] arr = originPath.split("/");
        segments = new ArrayList<Segment>();
        for (String str : arr) {
            Segment segment = new Segment();
            if (str.startsWith(":")) {
                segment.name = str.substring(1);
                segment.isVariable = true;
            } else {
                segment.name = str;
            }
            segments.add(segment);
        }

        methodMap = new HashMap<String, String>();
        if (isClassLevel) {
            methods = getClassLevelAPIMethod(controllerClass);
        }

        buildRequestMethodMap(controllerClass, methods);
    }

    RestPath(String originPath) {
        this(originPath, null, null);
    }

    /**
     * 匹配
     *
     * @param target  the original target
     * @param request http request
     * @return new path if validate; otherwise, null for 404
     */
    String match(String target, HttpServletRequest request) {
        //将target按斜线拆分成数组，用于匹配
        if (target.startsWith("/")) {
            target = target.substring(1);
        }

        String[] arr = target.split("/");
        //url结尾的参数，在controller里可以通过getPara()获得
        String para = null;
        if (arr.length == segments.size() + 1) {
            para = arr[arr.length - 1];
        } else if (arr.length != segments.size()) {
            return null;
        }

        //逐个部分进行比较
        Map<String, String> paras = new HashMap<String, String>();
        for (int i = 0; i < segments.size(); i++) {
            String str = arr[i];
            Segment segment = segments.get(i);
            if (segment.isVariable) {
                paras.put(segment.name, str);
            } else {
                if (!segment.name.equals(str)) {
                    return null;
                }
            }
        }

        //在request里放入rest参数
        for (Map.Entry<String, String> entry : paras.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }

        //根据请求方法生成新的路径
        String httpMethod = request.getMethod().toLowerCase();
        String method = methodMap.get(httpMethod) != null ? methodMap.get(httpMethod) : httpMethod;
        String newPath;
        if (method.isEmpty()) {
            newPath = originPath;
        } else {
            newPath = originPath + "/" + method;
        }

        if (para != null && isClassLevel) {
            return newPath + "/" + para;
        } else {
            return newPath;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        RestPath other = (RestPath) obj;
        if (segments.size() != other.segments.size()) {
            return false;
        }

        for (int i = 0; i < segments.size(); i++) {
            if (!segments.get(i).equals(other.segments.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Segment segment : segments) {
            hash = hash * 13 + segment.hashCode();
        }
        return hash;
    }

    private void buildRequestMethodMap(Class<? extends Controller> controllerClass, List<Method> methods) {
        if (controllerClass == null) {
            return;
        }

        for (Method method : methods) {
            // index is the default method in the jFinal controller
            String methodName = "index".equals(method.getName()) ? "" : method.getName();

            GET get = method.getAnnotation(GET.class);
            if (get != null) {
                if (methodMap.put("get", methodName) != null) {
                    throw new RuntimeException("Duplicate GET request method in " + controllerClass.getName());
                }
                continue;
            }

            PUT put = method.getAnnotation(PUT.class);
            if (put != null) {
                if (methodMap.put("put", methodName) != null) {
                    throw new RuntimeException("Duplicate PUT request method in " + controllerClass.getName());
                }
                continue;
            }

            POST post = method.getAnnotation(POST.class);
            if (post != null) {
                if (methodMap.put("post", methodName) != null) {
                    throw new RuntimeException("Duplicate POST request method in " + controllerClass.getName());
                }
                continue;
            }

            PATCH patch = method.getAnnotation(PATCH.class);
            if (patch != null) {
                if (methodMap.put("patch", methodName) != null) {
                    throw new RuntimeException("Duplicate PATCH request method in " + controllerClass.getName());
                }
                continue;
            }

            DELETE delete = method.getAnnotation(DELETE.class);
            if (delete != null) {
                if (methodMap.put("delete", methodName) != null) {
                    throw new RuntimeException("Duplicate DELETE request method in " + controllerClass.getName());
                }
                continue;
            }

            if (!isClassLevel && methodMap.get("get") == null) {
                methodMap.put("get", methodName);
            }
        }
    }

    private List<Method> getClassLevelAPIMethod(Class<? extends Controller> controllerClass) {
        List<Method> methods = new ArrayList<Method>();

        if (controllerClass == null) {
            return methods;
        }

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.getParameterTypes().length != 0 || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            API api = method.getAnnotation(API.class);
            if (isClassLevel && api != null) {
                continue;
            }

            methods.add(method);
        }

        return methods;
    }


    private static class Segment {
        boolean isVariable;

        /**
         * segment name
         * e.g. the "tickets" or "ticketId" of "/tickets/:ticketId/"
         */
        String name;

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Segment other = (Segment) obj;
            return isVariable == other.isVariable && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

}
