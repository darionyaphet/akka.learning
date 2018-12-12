package org.darion.yaphet.java.akka.persistence;

public class Cmd {
    private final String data;

    public Cmd(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
