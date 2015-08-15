package com.jfinal.rest;

import com.jfinal.config.Handlers;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestKitTest {

    private Routes routes;

    @Before
    public void setUp() {
        routes = new Routes() {
            @Override
            public void config() {
                // empty
            }
        };

        HttpServletRequest request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();
    }

    @Test
    public void testBuildRoutes() throws Exception {
        RestKit.buildRoutes("v1", routes, "com.jfinal.rest");

        assertEquals(2, routes.getEntrySet().size());
//        Map.Entry<String, Class<? extends Controller>> entry = new ArrayList<Map.Entry<String, Class<? extends Controller>>>(routes.getEntrySet()).get(0);
//        assertEquals("/v1/tickets/:ticketId/messages", entry.getKey());
//        assertEquals(MockController.class, entry.getValue());
    }

    @Test
    public void testBuildHandler() throws Exception {
        Handlers handlers = new Handlers();
        RestKit.buildRoutes("v1", routes, "com.jfinal.rest");

        RestKit.buildHandler(handlers);

        assertEquals(1, handlers.getHandlerList().size());
        assertTrue(handlers.getHandlerList().get(0) instanceof RestHandler);
    }

}