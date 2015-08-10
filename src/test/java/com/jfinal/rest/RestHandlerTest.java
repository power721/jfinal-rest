package com.jfinal.rest;

import com.jfinal.config.Routes;
import com.jfinal.handler.Handler;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RenderFactory.class})
public class RestHandlerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    Routes routes;
    RestRoutes restRoutes;

    @Before
    public void setUp() {
        routes = new Routes() {
            @Override
            public void config() {

            }
        };

        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();

        response = PowerMock.createNiceMock(HttpServletResponse.class);

        restRoutes = new RestRoutes("/v1", routes);
        restRoutes.addRoute("/tickets/:ticketId/messages", MockController.class);
    }

    @Test
    public void test() {
        final String newTarget = "/v1/tickets/:ticketId/messages/get";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        RestHandler restHandler = new RestHandler(restRoutes);
        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle("/v1/tickets/1/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

    @Test
    public void testNotMatchBasePath() {
        final String newTarget = "/v2/tickets/1/messages/";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        RestHandler restHandler = new RestHandler(restRoutes);
        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle(newTarget, request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

    @Test
    public void test404() throws NoSuchMethodException {
        RenderFactory renderFactory = PowerMock.createMock(RenderFactory.class);
        EasyMock.expect(renderFactory.getErrorRender(404)).andReturn(new Render() {
            @Override
            public void render() {

            }
        });
        PowerMock.mockStatic(RenderFactory.class, RenderFactory.class.getMethod("me"));
        EasyMock.expect(RenderFactory.me()).andReturn(renderFactory);

        PowerMock.replayAll();

        RestHandler restHandler = new RestHandler(restRoutes);

        restHandler.handle("/v1/tickets/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

}