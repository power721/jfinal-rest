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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rest path or action key for jFinal
 * Created by peak on 2015/1/23.
 */
class RestPath {
    private final String originPath;
    private final List<Segment> segments;

    RestPath(String originPath) {
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
    }

    /**
     * 匹配
     *
     * @param target the original target
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
        String method = request.getMethod().toLowerCase();
        String newPath = originPath + "/" + method;
        if (para != null) {
            newPath = newPath + "/" + para;
        }

        return newPath;
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
