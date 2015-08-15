package com.jfinal.rest;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import java.util.Arrays;


@API("/tickets/:ticketId/messages")
public class MockController extends Controller {

    @GET
    public void index() {
        String ticketId = getAttr("ticketId");
        String messageId = getPara();
        setAttr("method", "get");
        setAttr("q", getPara("q"));
        if (StrKit.isBlank(messageId)) {
            setAttr("data", Arrays.asList(1, 2, 3));
            setAttr("error", 0);
            return;
        }
        setAttr("data", ticketId);
        setAttr("error", 0);
    }

    @POST
    public void create() {
        String ticketId = getAttr("ticketId");
        setAttr("method", "post");
        setAttr("error", 0);
        setAttr("id", ticketId);
    }

    public void patch() {
        String messageId = getPara();
        setAttr("method", "patch");
        setAttr("messageId", messageId);
        setAttr("error", 0);
    }

    @PUT
    public void update() {
        int messageId = getParaToInt();
        setAttr("method", "put");
        setAttr("messageId", messageId);
        setAttr("error", 0);
    }

    @DELETE
    public void remove() {
        String messageId = getPara();
        setAttr("method", "delete");
        setAttr("messageId", messageId);
        setAttr("error", 0);
    }

    @API(":messageId")
    public void status() {
        String para = getPara();

        setAttr("data", para);
        setAttr("status", true);
    }

    @API(":messageId")
    @POST
    public void createStatus() {
        setAttr("created", true);
    }

}
