package com.restful.api.domain;

/**
 * Message class for when the insert of movies, show a message via the webpage.
 */
public class Message {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
