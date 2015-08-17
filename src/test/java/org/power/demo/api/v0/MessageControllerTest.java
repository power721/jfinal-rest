package org.power.demo.api.v0;

import com.jfinal.ext.test.RestControllerTestCase;
import org.junit.Test;
import org.power.demo.core.AppConfig;

public class MessageControllerTest extends RestControllerTestCase<AppConfig> {

    @Test
    public void testIndex() throws Exception {
        String url = "/v0/tickets/2/messages";
        String response = use(url).invoke();
        System.out.println(response);
    }

    @Test
    public void testSingle() throws Exception {
        String url = "/v0/tickets/2/messages/1";
        String response = use(url).invoke();
        System.out.println(response);
    }

    @Test
    public void testCreate() throws Exception {
        String url = "/v0/tickets/2/messages/";
        String response = use(url).post("{\"id\":\"5\"}").invoke();
        System.out.println(response);
    }

    @Test
    public void testPatch() throws Exception {
        String url = "/v0/tickets/2/messages/1";
        String response = use(url).patch("{\"id\":\"5\"}").invoke();
        System.out.println(response);
    }

    @Test
    public void testUpdate() throws Exception {
        String url = "/v0/tickets/2/messages/1";
        String response = use(url).put("{\"id\":\"5\", \"name\":\"test\"}").invoke();
        System.out.println(response);
    }

    @Test
    public void testRemove() throws Exception {
        String url = "/v0/tickets/2/messages/1";
        String response = use(url).delete().invoke();
        System.out.println(response);
    }

    @Test
    public void test404() {
        String url = "/tickets/2/messages/?q=1";
        String response = use(url).invoke();
        System.out.println(response);
    }

    @Test
    public void testRest404() {
        String url = "/v0/tickets/messages/?q=2";
        String response = use(url).invoke();
        System.out.println(response);
    }

    @Test
    public void testStatus() throws Exception {

    }

    @Test
    public void testCreateStatus() throws Exception {

    }

    @Test
    public void testUpdateStatus() throws Exception {

    }

    @Test
    public void testPatchStatus() throws Exception {

    }

    @Test
    public void testRemoveStatus() throws Exception {

    }
}