package org.power.demo.core;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.render.JsonErrorRender;
import com.jfinal.render.IErrorRenderFactory;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.JsonRender;
import com.jfinal.render.Render;
import com.jfinal.rest.RestKit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AppConfig extends JFinalConfig {

    private static final Logger LOGGER = LogManager.getLogger(AppConfig.class);

    @Override
    public void configConstant(Constants me) {
        LOGGER.trace("configConstant");
        me.setMainRenderFactory(new IMainRenderFactory() {
            @Override
            public Render getRender(String view) {
                return new JsonRender();
            }

            @Override
            public String getViewExtension() {
                return null;
            }
        });

        me.setErrorRenderFactory(new IErrorRenderFactory() {
            @Override
            public Render getRender(int errorCode, String view) {
                return new JsonErrorRender(errorCode);
            }
        });
        me.setDevMode(true);
    }

    @Override
    public void configRoute(Routes me) {
        String packagePath = "/" + this.getClass().getPackage().getName().replace('.', '/');
        String classPath = this.getClass().getResource("").getPath().replace(packagePath, "");
        LOGGER.trace("configRoute {}", classPath);
        LOGGER.trace(this.getClass().getPackage().getName());
        RestKit.buildRoutes("/v0", classPath, me, "org.power.demo.api.v0");
    }

    @Override
    public void configPlugin(Plugins me) {
        LOGGER.trace("configPlugin");
    }

    @Override
    public void configInterceptor(Interceptors me) {
        LOGGER.trace("configInterceptor");
    }

    @Override
    public void configHandler(Handlers me) {
        LOGGER.trace("configHandler");
        RestKit.buildHandler(me);
    }

    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 8080, "/", 5);
    }

}
