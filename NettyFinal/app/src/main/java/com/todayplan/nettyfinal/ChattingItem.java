package com.todayplan.nettyfinal;


public class ChattingItem {

    private String ID, MSG;

    public ChattingItem(String id, String msg){
        this.ID = id;
        this.MSG = msg;
    }
    public ChattingItem(){    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }
}