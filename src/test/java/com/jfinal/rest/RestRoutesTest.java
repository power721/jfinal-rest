package com.jfinal.rest;

import com.jfinal.config.Routes;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
public class RestRoutesTest {

    private HttpServletRequest request;
    private Routes routes;

    @Before
    public void setUp() {
        routes = new Routes() {
            @Override
            public void config() {
                // empty
            }
        };

        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();
    }

    @Test
    public void test() {
        PowerMock.replayAll();

        RestRoutes restRoutes = new RestRoutes("/v1", routes);
        restRoutes.addRoute("/tickets/:ticketId/messages", MockController.class);
        String newPath = restRoutes.match("/v1/tickets/1/messages/", request);

        assertEquals("/v1/tickets/:ticketId/messages", newPath);
    }

    @Test
    public void testWithoutLeadingSlash() {
        PowerMock.replayAll();

        RestRoutes restRoutes = new RestRoutes("v1/", routes);
        restRoutes.addRoute("/tickets/:ticketId/messages", MockController.class);
        String newPath = restRoutes.match("/v1/tickets/1/messages/", request);

        assertEquals("/v1/tickets/:ticketId/messages", newPath);
    }

    @Test
    public void testWithWrongBasePath() {
        PowerMock.replayAll();

        RestRoutes restRoutes = new RestRoutes("/v1", routes);
        restRoutes.addRoute("/tickets/:ticketId/messages", MockController.class);
        String newPath = restRoutes.match("/v2/tickets/1/messages/", request);

        assertNull(newPath);
    }

    @Test
    public void testWithWrongRestPath() {
        PowerMock.replayAll();

        RestRoutes restRoutes = new RestRoutes("/v1", routes);
        restRoutes.addRoute("/tickets/:ticketId/messages", MockController.class);
        String newPath = restRoutes.match("/v1/tickets/1/message/", request);

        assertNull(newPath);
    }

    @Test(expected = RuntimeException.class)
    public void testDuplicateRoute() {
        PowerMock.replayAll();

        RestRoutes restRoutes = new RestRoutes("/v1", routes);
        restRoutes.addRoute("/tickets/:ticketId/messages", MockController.class);
        restRoutes.addRoute("/tickets/:ticketId/messages", MockController.class);
    }

}