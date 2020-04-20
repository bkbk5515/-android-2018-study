package com.todayplan.nettyfinal;


public class ChatRoomListViewItem {

    private String ID, Room_Name;

    public ChatRoomListViewItem(String id, String room_name){
        this.ID = id;
        this.Room_Name = room_name;
    }
    public ChatRoomListViewItem(){    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRoom_Name() {
        return Room_Name;
    }

    public void setRoom_Name(String room_Name) {
        Room_Name = room_Name;
    }
}