package org.power.demo.api.v0;

import com.google.gson.Gson;
import com.jfinal.ext.test.RestControllerTestCase;
import org.junit.Test;
import org.power.demo.core.AppConfig;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TicketControllerTest extends RestControllerTestCase<AppConfig> {

    private Gson gson = new Gson();

    @Test
    public void testGet() throws Exception {
        String url = "/v0/tickets/";
        String response = use(url).invoke();
        TicketResponse ticketResponse = gson.fromJson(response, TicketResponse.class);

        assertEquals(4, ticketResponse.getTickets().size());
    }

    @Test
    public void testPost() throws Exception {
        String url = "/v0/tickets/";
        String response = use(url).post("{\"title\":\"test\",\"author\":\"root\"}").invoke();
        TicketResponse ticketResponse = gson.fromJson(response, TicketResponse.class);

        assertEquals(5, ticketResponse.getTicket().getId());
        assertEquals("root", ticketResponse.getTicket().getAuthor());
        assertEquals("test", ticketResponse.getTicket().getTitle());
        assertTrue(ticketResponse.isCreated());
    }

    @Test
    public void testPatch() throws Exception {
        String url = "/v0/tickets/1";
        String response = use(url).patch("{\"title\":\"patch\"}").invoke();
        TicketResponse ticketResponse = gson.fromJson(response, TicketResponse.class);

        assertEquals(1, ticketResponse.getTicket().getId());
        assertEquals("power", ticketResponse.getTicket().getAuthor());
        assertEquals("patch", ticketResponse.getTicket().getTitle());
        assertTrue(ticketResponse.isUpdated());
    }

    @Test
    public void testPut() throws Exception {
        String url = "/v0/tickets/3";
        String response = use(url).put("{\"title\":\"test\",\"author\":\"root\"}").invoke();
        TicketResponse ticketResponse = gson.fromJson(response, TicketResponse.class);

        assertEquals(3, ticketResponse.getTicket().getId());
        assertEquals("root", ticketResponse.getTicket().getAuthor());
        assertEquals("test", ticketResponse.getTicket().getTitle());
        assertTrue(ticketResponse.isUpdated());
    }

    @Test
    public void testDelete() throws Exception {
        String url = "/v0/tickets/4";
        String response = use(url).delete().invoke();
        TicketResponse ticketResponse = gson.fromJson(response, TicketResponse.class);

        assertEquals(4, ticketResponse.getTicketId());
        assertTrue(ticketResponse.isDeleted());
    }


    private static class TicketResponse {
        private boolean created;
        private boolean deleted;
        private boolean updated;
        private String error;
        private int ticketId;
        private Ticket ticket;
        private List<Ticket> tickets;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public int getTicketId() {
            return ticketId;
        }

        public void setTicketId(int ticketId) {
            this.ticketId = ticketId;
        }

        public Ticket getTicket() {
            return ticket;
        }

        public void setTicket(Ticket ticket) {
            this.ticket = ticket;
        }

        public List<Ticket> getTickets() {
            return tickets;
        }

        public void setTickets(List<Ticket> tickets) {
            this.tickets = tickets;
        }

        public boolean isCreated() {
            return created;
        }

        public void setCreated(boolean created) {
            this.created = created;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public boolean isUpdated() {
            return updated;
        }

        public void setUpdated(boolean updated) {
            this.updated = updated;
        }
    }
}