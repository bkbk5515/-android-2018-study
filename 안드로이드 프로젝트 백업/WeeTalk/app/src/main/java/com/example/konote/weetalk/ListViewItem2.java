package com.example.konote.weetalk;

/**
 * Created by KONOTE on 2017-11-15.
 */

public class ListViewItem2 {

    private String userID ;

    public ListViewItem2(String _userID){
        this.userID = _userID ;
    }

    public String getUserID() { return userID;}

    public void setUserID(String userID) {
        this.userID = userID;
    }

    //삭제하지말것
    public ListViewItem2(){}

}
