package com.jfinal.rest;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import java.util.Arrays;

@API("/tickets/:ticketId/messages")
public class MockController extends Controller {

    public void get() {
        String ticketId = getAttr("ticketId");
        String messageId = getPara();
        setAttr("method", "get");
        setAttr("q", getPara("q"));
        if (StrKit.isBlank(messageId)) {
            setAttr("error", 0);
            setAttr("data", Arrays.asList(1, 2, 3));
            return;
        }
        setAttr("error", 0);
        setAttr("data", ticketId);
    }

    public void post() {
        String ticketId = getAttr("ticketId");
        setAttr("method", "post");
        setAttr("error", 0);
        setAttr("id", ticketId);
    }

    public void patch() {
        String messageId = getPara();
        setAttr("method", "patch");
        setAttr("error", 0);
    }

    public void put() {
        int messageId = getParaToInt();
        setAttr("method", "put");
        setAttr("error", 0);
    }

    public void delete() {
        String messageId = getPara();
        setAttr("error", 0);
        setAttr("method", "delete");
        setAttr("messageId", messageId);
    }

}
