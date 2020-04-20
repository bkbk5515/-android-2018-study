package com.example.konote.nettychat;

public class ListViewItem  {

    private String chattext ;

    public ListViewItem(){}
    public ListViewItem(String chat){
        this.chattext = chat;
    }

    public String getChattext() {
        return chattext;
    }

    public void setChattext(String chattext) {
        this.chattext = chattext;
    }
}//출처: http://recipes4dev.tistory.com/43?category=605791 [개발자를 위한 레시피]