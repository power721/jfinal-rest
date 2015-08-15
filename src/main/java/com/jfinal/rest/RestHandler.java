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
class RestHandler extends Handler {

    private static final Logger LOGGER = Logger.getLogger(RestHandler.class);

    /**
     * 访问路径
     */
    private final RestRoutes routes;

//    private String viewPostfix;

    /**
     * construct RestHandler with the RestRoutes
     *
     * @param routes the rest routes
     */
    public RestHandler(RestRoutes routes) {
        this.routes = routes;
    }

//    public RestHandler(RestRoutes routes, String viewPostfix) {
//        if (StrKit.isBlank(viewPostfix) || !viewPostfix.contains(".")) {
//            throw new IllegalArgumentException("invalid view postfix.");
//        }
//        this.routes = routes;
//        this.viewPostfix = viewPostfix;
//    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        String basePath = routes.getBasePath();
        if (!target.startsWith(basePath)) {
            nextHandler.handle(target, request, response, isHandled);
            return;
        }

        String newTarget = routes.match(target, request);
        LOGGER.info(basePath + " " + target + "  " + newTarget);
        if (newTarget == null) {
            if (LOGGER.isWarnEnabled()) {
                String qs = request.getQueryString();
                LOGGER.warn("404 Action Not Found: " + (qs == null ? target : target + "?" + qs));
            }
            RenderFactory.me().getErrorRender(404).setContext(request, response).render();
            return;
        }

        isHandled[0] = true;
//        if (viewPostfix != null) {
//            newTarget = newTarget.replace(viewPostfix, "");
//        }
        nextHandler.handle(newTarget, request, response, isHandled);
    }

}
