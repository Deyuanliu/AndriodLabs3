package com.example.andriodlabs;

public class Message {
    private String message;
    private int message_type;

    private long id;

    public Message(String message) {
        setMessage(message);
    }

    public Message(String message, int message_type) {
        setMessage(message);
        setMessage_type(message_type);
    }
    public Message(String message, int message_type, long id){
        setMessage(message);
        setMessage_type(message_type);
        setId(id);
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public int getMessage_type(){
        return message_type;
    }

    public void setMessage_type(int message_type){
        this.message_type=message_type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}