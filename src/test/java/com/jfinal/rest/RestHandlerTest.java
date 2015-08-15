package com.jfinal.rest;

import com.jfinal.config.Handlers;
import com.jfinal.config.Routes;
import com.jfinal.handler.Handler;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RenderFactory.class})
public class RestHandlerTest {

    private static Handlers handlers;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RestHandler restHandler;

    @BeforeClass
    public static void init() {
        Routes routes = new Routes() {
            @Override
            public void config() {
                // empty
            }
        };

        handlers = new Handlers();

        RestKit.buildRoutes("v1", routes, "com.jfinal.rest");
        RestKit.buildHandler(handlers);
        assertEquals(1, handlers.getHandlerList().size());
    }

    @Before
    public void setUp() {
        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();

        response = PowerMock.createNiceMock(HttpServletResponse.class);

        restHandler = (RestHandler) handlers.getHandlerList().get(0);
    }

    @Test
    public void test() {
        final String newTarget = "/v1/tickets/:ticketId/messages";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle("/v1/tickets/1/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

    @Test
    public void testPost() {
        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("POST");

        final String newTarget = "/v1/tickets/:ticketId/messages/create";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle("/v1/tickets/1/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

    @Test
    public void testPut() {
        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("PUT");

        final String newTarget = "/v1/tickets/:ticketId/messages/update";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle("/v1/tickets/1/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

    @Test
    public void testPatch() {
        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("PATCH");

        final String newTarget = "/v1/tickets/:ticketId/messages/patch";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle("/v1/tickets/1/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

    @Test
    public void testDelete() {
        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("DELETE");

        final String newTarget = "/v1/tickets/:ticketId/messages/remove";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle("/v1/tickets/1/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

    @Test
    public void testApiPathInMethod() {
        final String newTarget = "/v1/tickets/:ticketId/messages/:messageId/status";
        Handler nextHandler = new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                assertEquals(newTarget, target);
            }
        };

        PowerMock.replayAll();

        Whitebox.setInternalState(restHandler, Handler.class, nextHandler);

        restHandler.handle("/v1/tickets/1/messages/2/status", request, response, new boolean[]{false});

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
                // empty
            }
        });
        PowerMock.mockStatic(RenderFactory.class, RenderFactory.class.getMethod("me"));
        EasyMock.expect(RenderFactory.me()).andReturn(renderFactory);

        PowerMock.replayAll();

        restHandler.handle("/v1/tickets/messages/", request, response, new boolean[]{false});

        PowerMock.verifyAll();
    }

}