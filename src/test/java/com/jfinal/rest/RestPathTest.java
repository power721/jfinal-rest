package com.jfinal.rest;

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
public class RestPathTest {

    private HttpServletRequest request;

    @Before
    public void setUp() {
        request = PowerMock.createNicePartialMock(HttpServletRequest.class, "getMethod");
        EasyMock.expect(request.getMethod()).andReturn("GET").anyTimes();
    }

    @Test
    public void testDefault() {
        PowerMock.replayAll();
        RestPath path = new RestPath("/tickets/:ticketId/messages");
        String newPath = path.match("/tickets/1/messages", request);

        assertEquals("/tickets/:ticketId/messages/get", newPath);
        PowerMock.verifyAll();
    }

    @Test
    public void testWithPara() {
        PowerMock.replayAll();
        RestPath path = new RestPath("/tickets/:ticketId/messages");
        String newPath = path.match("/tickets/1/messages/2?q=1", request);

        assertEquals("/tickets/:ticketId/messages/get/2?q=1", newPath);
        PowerMock.verifyAll();
    }

    @Test
    public void testNotMatchSize() {
        PowerMock.replayAll();
        RestPath path = new RestPath("/tickets/:ticketId/messages");
        String newPath = path.match("/tickets/1/messages/2/3", request);

        assertNull(newPath);
        PowerMock.verifyAll();
    }

    @Test
    public void testNotMatchPath() {
        PowerMock.replayAll();
        RestPath path = new RestPath("/tickets/:ticketId/messages");
        String newPath = path.match("/tickets/messages", request);

        assertNull(newPath);
        PowerMock.verifyAll();
    }

}