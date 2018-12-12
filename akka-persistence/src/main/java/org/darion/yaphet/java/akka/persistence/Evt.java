package org.darion.yaphet.java.akka.persistence;

public class Evt {
    private final String data;

    public Evt(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
