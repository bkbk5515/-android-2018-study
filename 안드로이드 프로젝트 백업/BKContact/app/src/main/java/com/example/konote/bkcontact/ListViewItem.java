package com.example.konote.bkcontact;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by KONOTE on 2017-07-21.
 */

public class ListViewItem {
        private Bitmap iconDrawable ;
        private String nameStr ;
        private String number1Str ;
        private String number2Str ;
        private String mailStr;
        private String memoStr;

        public void setIcon(Bitmap icon) {
            iconDrawable = icon ;
        }
        public void setName(String name) {
            nameStr = name ;
        }
        public void setNumber1(String number1) {number1Str = number1 ;}
        public void setNumber2(String number2) {number2Str = number2 ;}
        public void setMail(String mail) {mailStr = mail ;}
        public void setMemo(String memo) {memoStr = memo ;}

        public Bitmap getIcon() {
            return this.iconDrawable ;
        }
        public String getName() {
        return this.nameStr ;
    }
        public String getNumber1() {
            return this.number1Str ;
        }
        public String getNumber2() {
        return this.number2Str ;
    }
        public String getMail() {
        return this.mailStr ;
    }
        public String getMemo() { return this.memoStr ; }

    public ListViewItem(Bitmap icon, String name, String number1, String number2, String mail, String memo){
        this.iconDrawable = icon ;
        this.nameStr = name ;
        this.number1Str =number1;
        this.number2Str=number2;
        this.mailStr=mail;
        this.memoStr=memo;
    }
    public ListViewItem(){

    }

}
