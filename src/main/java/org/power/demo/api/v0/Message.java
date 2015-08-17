package org.power.demo.api.v0;

import java.util.concurrent.atomic.AtomicInteger;

public class Message {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int id;
    private String text;
    private String author;

    public Message() {
    }

    public void save() {
        id = idGenerator.incrementAndGet();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
