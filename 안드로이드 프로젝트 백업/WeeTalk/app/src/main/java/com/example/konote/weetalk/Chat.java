package com.example.konote.weetalk;

/**
 * Created by KONOTE on 2017-11-10.
 */

public class Chat {
    int type;
    String username;
    String msg;
    String time;

    public Chat(){}
    public Chat(int _type, String _msg, String _username, String _time){
        this.type = _type;
        this.msg = _msg;
        this.username = _username;
        this.time = _time;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
}
