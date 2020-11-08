package com.example.firebasechat;

public class Message {
    private String text, name, iamgeUrl, sender, recipient;
    private boolean isMine;

    public Message(String text, String name, String iamgeUrl, String sender, String recipient, boolean isMine) {
        this.text = text;
        this.name = name;
        this.iamgeUrl = iamgeUrl;
        this.sender = sender;
        this.recipient = recipient;
        this.isMine = isMine;
    }

    public Message() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIamgeUrl() {
        return iamgeUrl;
    }

    public void setIamgeUrl(String iamgeUrl) {
        this.iamgeUrl = iamgeUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
