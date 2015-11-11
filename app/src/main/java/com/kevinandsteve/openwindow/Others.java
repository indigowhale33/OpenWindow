package com.kevinandsteve.openwindow;

/**
 * Created by Steve on 2015-11-08.
 */
public class Others {
    public String name;
    public String number;
    public boolean checked = false;
    public Others(String name, String number, boolean checked){
        this.name = name;
        this.number = number;
        this.checked = checked;
    }

    public String getName(){
        return name;
    }

    public boolean getCheck(){
        return checked;
    }

    public String getNumber(){
        return number;
    }

}
