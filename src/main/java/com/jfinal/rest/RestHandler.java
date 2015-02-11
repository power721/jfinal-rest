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
        if (!target.startsWith(visitPath)) {
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
