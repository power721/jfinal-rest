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

import com.jfinal.handler.Handler;
import com.jfinal.log.Logger;
import com.jfinal.render.RenderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * restful handler
 * Created by peak on 2015/1/22.
 */
public class RestHandler extends Handler {
    /**
     * 访问路径
     */
    private RestRoutes routes = null;
    private final Logger log = Logger.getLogger(RestHandler.class);

    public RestHandler(RestRoutes routes) {
        this.routes = routes;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        String visitPath = routes.getVisitPath();
        if (!target.startsWith(visitPath + "/")) {
            nextHandler.handle(target, request, response, isHandled);
            return;
        }
        isHandled[0] = true;
        String newTarget = routes.match(target, request);
        if (newTarget == null) {
            if (log.isWarnEnabled()) {
                String qs = request.getQueryString();
                log.warn("404 Action Not Found: " + (qs == null ? target : target + "?" + qs));
            }
            RenderFactory.me().getErrorRender(404).setContext(request, response).render();
            return;
        }
        nextHandler.handle(newTarget, request, response, isHandled);
    }
}
