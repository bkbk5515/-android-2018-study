package com.example.konote.weetalk;

import android.graphics.Bitmap;

/**
 * Created by KONOTE on 2017-07-21.
 */

public class ListViewItem {
        private String roomName ;

    public ListViewItem(String _roomName){ this.roomName = _roomName ; }

    public String getRoomName() { return roomName; }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ListViewItem(){}

}
