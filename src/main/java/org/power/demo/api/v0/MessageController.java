package org.power.demo.api.v0;

import com.jfinal.kit.StrKit;
import com.jfinal.rest.API;
import com.jfinal.rest.DELETE;
import com.jfinal.rest.GET;
import com.jfinal.rest.PATCH;
import com.jfinal.rest.POST;
import com.jfinal.rest.PUT;
import com.jfinal.rest.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@API("/tickets/:ticketId/messages")
public class MessageController extends RestController {

    private static final Logger LOGGER = LogManager.getLogger(MessageController.class);

    private static final Map<Integer, Message> messages = new HashMap<>();

    static {
        Message message = new Message();
        message.setAuthor("power");
        message.setText("jFinal-rest");
        message.save();
        messages.put(message.getId(), message);

        message = new Message();
        message.setAuthor("Harold");
        message.setText("PowerOJ");
        message.save();
        messages.put(message.getId(), message);

        message = new Message();
        message.setAuthor("jfinal");
        message.setText("jFinal 2.0");
        message.save();
        messages.put(message.getId(), message);
    }

    /**
     * 获取单个数据或者列表
     * http://localhost:8080/v0/tickets/2/messages/
     * http://localhost:8080/v0/tickets/2/messages/1
     */
    @GET
    public void index() {
        //路径里定义的参数变量，需要通过getAttr()方法获取
        String ticketId = getAttr("ticketId");
        String messageId = getPara(0);
        setAttr("method", "get");
        setAttr("q", getPara("q"));

        if (StrKit.isBlank(messageId)) {
            setAttr("error", 0);
            LOGGER.info(messages.size());
            setAttr("messages", new ArrayList<>(messages.values()));
            return;
        }

        setAttr("error", 0);
        setAttr("message", messages.get(Integer.valueOf(messageId)));
        setAttr("messageId", messageId);
        setAttr("data", ticketId + messageId);
    }

    /**
     * 添加新数据
     */
    @POST
    public void create() {
        //POST /v1/tickets/xxxx/messages
        Message message = getJsonData(Message.class);
        message.save();
        messages.put(message.getId(), message);
        LOGGER.info("id: {} text: {} author: {}", message.getId(), message.getText(), message.getAuthor());
        String ticketId = getAttr("ticketId");
        setAttr("method", "post");
        setAttr("error", 0);
        setAttr("message", message);
    }

    /**
     * 部分更新数据
     */
    public void patch() {
        //PATCH /v1/tickets/xxxx/messages/xxxxx
        String messageId = getPara();
        setAttr("method", "patch");
        setAttr("error", 0);
        setAttr("messageId", messageId);
    }

    /**
     * 数据整体更新
     */
    @PUT
    public void update() {
        //PUT /v1/tickets/xxxx/messages/xxxxx
        int messageId = getParaToInt();
        setAttr("data", "updated");
        setAttr("method", "put");
        setAttr("messageId", messageId);
        setAttr("error", 0);
    }

    /**
     * 删除数据
     * curl -X DELETE http://localhost:8080/v0/tickets/2/messages/1
     */
    @DELETE
    public void remove() {
        String messageId = getPara();
        setAttr("error", 0);
        setAttr("method", "delete");
        setAttr("messageId", messageId);
    }

    /**
     * curl http://localhost:8080/v0/tickets/2/messages/1/status
     */
    @API(":messageId/status")
    public void status() {
        // /v0/tickets/30/messages/5/status --> /v0/tickets/:ticketId/messages/status/5
        String para = getPara();

        setAttr("data", para);
        setAttr("status", true);
    }

    @API(":messageId/status")
    @POST
    public void createStatus() {
        setAttr("created", true);
    }

    @API(":messageId/status")
    @PUT
    public void updateStatus() {
        setAttr("updated", true);
    }

    @API(":messageId/status")
    @PATCH
    public void patchStatus() {
        setAttr("patched", true);
    }

    @API(":messageId/status")
    @DELETE
    public void removeStatus() {
        setAttr("deleted", true);
    }

}
