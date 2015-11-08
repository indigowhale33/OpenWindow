package com.kevinandsteve.openwindow;

/**
 * Created by Steve on 2015-10-22.
 */
import android.graphics.Bitmap;

/**
 * Created by Trinity Tuts on 10-01-2015.
 */
public class SelectUser {
    String name;
    String phone;
    Boolean checkedBox = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getCheckedBox() {
        return checkedBox;
    }
    public void setCheckedBox(Boolean checkedBox) {
        this.checkedBox = checkedBox;
    }


}