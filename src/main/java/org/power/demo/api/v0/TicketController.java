package org.power.demo.api.v0;

import com.jfinal.kit.StrKit;
import com.jfinal.rest.API;
import com.jfinal.rest.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@API("/tickets")
public class TicketController extends RestController {

    private static final Map<Integer, Ticket> tickets = new HashMap<>();

    static {
        Ticket ticket = new Ticket();
        ticket.setAuthor("power");
        ticket.setTitle("jFinal-rest dev");
        ticket.save();
        tickets.put(ticket.getId(), ticket);

        ticket = new Ticket();
        ticket.setAuthor("Harold");
        ticket.setTitle("PowerOJ dev");
        ticket.save();
        tickets.put(ticket.getId(), ticket);

        ticket = new Ticket();
        ticket.setAuthor("jfinal");
        ticket.setTitle("jFinal 2.0 publish");
        ticket.save();
        tickets.put(ticket.getId(), ticket);

        ticket = new Ticket();
        ticket.setAuthor("jfinal-rest");
        ticket.setTitle("jFinal rest demo");
        ticket.save();
        tickets.put(ticket.getId(), ticket);
    }

    public void get() {
        // curl http://localhost:8080/v0/tickets/
        // curl http://localhost:8080/v0/tickets/2
        String ticketId = getPara();
        if (StrKit.isBlank(ticketId)) {
            setAttr("error", 0);
            setAttr("tickets", new ArrayList<>(tickets.values()));
            return;
        }
        setAttr("error", 0);
        setAttr("ticket", tickets.get(Integer.valueOf(ticketId)));
    }

    public void post() {
        // curl -H "Content-Type: application/json" -X POST -d '{"title":"test","author":"root"}' http://localhost:8080/v0/tickets/
        Ticket ticket = getJsonData(Ticket.class);
        ticket.save();
        tickets.put(ticket.getId(), ticket);

        setAttr("error", 0);
        setAttr("ticket", ticket);
        setAttr("created", true);
    }

    public void patch() {
        // curl -H "Content-Type: application/json" -X PATCH -d '{"title":"test"}' http://localhost:8080/v0/tickets/1
        Ticket newTicket = getJsonData(Ticket.class);
        int ticketId;
        if (!StrKit.isBlank(getPara())) {
            ticketId = getParaToInt();
        } else {
            ticketId = newTicket.getId();
        }

        Ticket ticket = tickets.get(ticketId);
        setAttr("ticketId", ticketId);
        if (ticket != null) {
            ticket.patch(newTicket);
            setAttr("error", 0);
            setAttr("ticket", ticket);
            setAttr("updated", true);
        } else {
            setAttr("error", "cannot find ticket");
        }
    }

    public void put() {
        // curl -H "Content-Type: application/json" -X PUT -d '{"title":"other title","author":"root"}' http://localhost:8080/v0/tickets/4
        Ticket newTicket = getJsonData(Ticket.class);
        int ticketId;
        if (!StrKit.isBlank(getPara())) {
            ticketId = getParaToInt();
        } else {
            ticketId = newTicket.getId();
        }

        Ticket ticket = tickets.get(ticketId);
        setAttr("ticketId", ticketId);
        if (ticket != null) {
            ticket.update(newTicket);
            setAttr("error", 0);
            setAttr("ticket", ticket);
            setAttr("updated", true);
        } else {
            setAttr("error", "cannot find ticket");
        }
    }

    public void delete() {
        // curl -X DELETE http://localhost:8080/v0/tickets/4
        int ticketId = getParaToInt();
        setAttr("error", 0);
        setAttr("ticketId", ticketId);
        if (tickets.remove(ticketId) != null) {
            setAttr("deleted", true);
        } else {
            setAttr("deleted", false);
        }
    }

}
