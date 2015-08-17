package org.power.demo.api.v0;

import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int id;
    private String title;
    private String author;

    public Ticket() {
    }

    public void save() {
        id = idGenerator.incrementAndGet();
    }

    public void update(Ticket ticket) {
        setAuthor(ticket.getAuthor());
        setTitle(ticket.getTitle());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }
}
