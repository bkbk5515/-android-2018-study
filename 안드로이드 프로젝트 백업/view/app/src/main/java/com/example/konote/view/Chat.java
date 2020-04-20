package com.example.konote.view;

import org.w3c.dom.Comment;

/**
 * Created by KONOTE on 2017-11-10.
 */

public class Chat {
    int type;
    String username;
    String msg;
    String time;

    public Chat(){}
    public Chat(int _type, String _msg, String _username){
        this.type = _type;
        this.msg = _msg;
        this.username = _username;
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
}
